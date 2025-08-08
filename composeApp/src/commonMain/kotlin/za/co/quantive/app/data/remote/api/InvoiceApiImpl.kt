package za.co.quantive.app.data.remote.api

import za.co.quantive.app.data.remote.SupabaseClient
import za.co.quantive.app.domain.entities.Invoice
import za.co.quantive.app.domain.entities.InvoiceFilter

/**
 * Invoice API implementation using Supabase backend
 * Connects to backend REST API endpoints for invoice operations
 */
class InvoiceApiImpl(
    private val client: SupabaseClient,
) : InvoiceApi {

    override suspend fun getInvoices(
        page: Int,
        limit: Int,
        filter: InvoiceFilter?,
    ): ApiResponse<PaginatedResponse<Invoice>> {
        return try {
            // Build query parameters for filtering and pagination
            val params = mutableMapOf<String, String?>(
                "offset" to (page * limit).toString(),
                "limit" to limit.toString(),
                "select" to "*,customer:customers(*),items:invoice_items(*)",
            )

            // Apply filters if provided
            filter?.let { f ->
                f.status?.let { params["status"] = "eq.${it.name}" }
                f.customerId?.let { params["customer_id"] = "eq.$it" }
                f.dateRange?.let { range ->
                    params["created_at"] = "gte.${range.start}"
                    // For date ranges we need multiple parameters - this is simplified
                    // In practice, you'd need to handle this in the backend function
                }
                f.searchQuery?.let { params["or"] = "(invoice_number.ilike.*$it*,customer.name.ilike.*$it*)" }
            }

            // Call Supabase REST API
            val invoices: List<Invoice> = client.get("rest/v1/invoices", params)

            // Get total count for pagination (separate query)
            val countParams = params.filterKeys { it != "offset" && it != "limit" && it != "select" }
                .toMutableMap().apply { put("select", "count") }
            val countResponse: List<Map<String, Int>> = client.get("rest/v1/invoices", countParams)
            val total = countResponse.firstOrNull()?.get("count") ?: 0

            ApiResponse.success(
                PaginatedResponse(
                    data = invoices,
                    pagination = PaginationInfo(
                        page = page,
                        limit = limit,
                        total = total,
                    ),
                ),
            )
        } catch (e: Exception) {
            ApiResponse.error("Failed to fetch invoices: ${e.message}")
        }
    }

    override suspend fun getInvoice(id: String): ApiResponse<Invoice> {
        return try {
            val params = mapOf(
                "select" to "*,customer:customers(*),items:invoice_items(*)",
                "id" to "eq.$id",
            )

            val invoices: List<Invoice> = client.get("rest/v1/invoices", params)
            val invoice = invoices.firstOrNull()

            if (invoice != null) {
                ApiResponse.success(invoice)
            } else {
                ApiResponse.error("Invoice not found")
            }
        } catch (e: Exception) {
            ApiResponse.error("Failed to fetch invoice: ${e.message}")
        }
    }

    override suspend fun createInvoice(request: CreateInvoiceRequest): ApiResponse<Invoice> {
        return try {
            // Create invoice via Supabase stored procedure or function
            // This calls the backend which handles all calculations
            val invoiceData = mapOf(
                "customer_id" to request.customerId,
                "due_date" to request.dueDate,
                "notes" to request.notes,
                "terms_and_conditions" to request.termsAndConditions,
                "payment_terms" to request.paymentTerms,
                "items" to request.items.map { item ->
                    mapOf(
                        "description" to item.description,
                        "quantity" to item.quantity,
                        "unit_price" to item.unitPrice,
                        "notes" to item.notes,
                        "product_id" to item.productId,
                    )
                },
                "metadata" to request.metadata,
            )

            // Call backend function that creates invoice and calculates all totals
            val createdInvoice: Invoice = client.post(
                "rest/v1/rpc/create_invoice",
                invoiceData,
            )

            ApiResponse.success(createdInvoice)
        } catch (e: Exception) {
            ApiResponse.error("Failed to create invoice: ${e.message}")
        }
    }

    override suspend fun updateInvoice(id: String, request: UpdateInvoiceRequest): ApiResponse<Invoice> {
        return try {
            // Update invoice via backend function that recalculates everything
            val updateData = mutableMapOf<String, Any?>(
                "invoice_id" to id,
            ).apply {
                request.customerId?.let { put("customer_id", it) }
                request.dueDate?.let { put("due_date", it) }
                request.notes?.let { put("notes", it) }
                request.termsAndConditions?.let { put("terms_and_conditions", it) }
                request.paymentTerms?.let { put("payment_terms", it) }
                request.items?.let { items ->
                    put(
                        "items",
                        items.map { item ->
                            mapOf(
                                "description" to item.description,
                                "quantity" to item.quantity,
                                "unit_price" to item.unitPrice,
                                "notes" to item.notes,
                                "product_id" to item.productId,
                            )
                        },
                    )
                }
                request.metadata?.let { put("metadata", it) }
            }

            // Call backend function that updates and recalculates
            val updatedInvoice: Invoice = client.post(
                "rest/v1/rpc/update_invoice",
                updateData,
            )

            ApiResponse.success(updatedInvoice)
        } catch (e: Exception) {
            ApiResponse.error("Failed to update invoice: ${e.message}")
        }
    }

    override suspend fun deleteInvoice(id: String): ApiResponse<Unit> {
        return try {
            val params = mapOf("id" to "eq.$id")

            // Soft delete - update status to CANCELLED
            val updateData = mapOf("status" to "CANCELLED")
            client.post<Unit, Map<String, String>>("rest/v1/invoices", updateData, params)

            ApiResponse.success(Unit)
        } catch (e: Exception) {
            ApiResponse.error("Failed to delete invoice: ${e.message}")
        }
    }

    override suspend fun sendInvoice(id: String, request: SendInvoiceRequest): ApiResponse<Invoice> {
        return try {
            // Send invoice via backend email service
            val sendData = mapOf(
                "invoice_id" to id,
                "email_addresses" to request.emailAddresses,
                "subject" to request.subject,
                "message" to request.message,
                "attach_pdf" to request.attachPdf,
                "schedule_datetime" to request.scheduleDateTime,
            )

            // Call backend function that sends email and updates status
            val sentInvoice: Invoice = client.post(
                "rest/v1/rpc/send_invoice",
                sendData,
            )

            ApiResponse.success(sentInvoice)
        } catch (e: Exception) {
            ApiResponse.error("Failed to send invoice: ${e.message}")
        }
    }

    override suspend fun recordPayment(id: String, request: RecordPaymentRequest): ApiResponse<Invoice> {
        return try {
            // Record payment via backend function that updates all calculations
            val paymentData = mapOf(
                "invoice_id" to id,
                "amount" to request.amount,
                "currency" to request.currency,
                "payment_date" to request.paymentDate,
                "payment_method" to request.paymentMethod,
                "reference" to request.reference,
                "notes" to request.notes,
            )

            // Call backend function that records payment and recalculates invoice
            val updatedInvoice: Invoice = client.post(
                "rest/v1/rpc/record_payment",
                paymentData,
            )

            ApiResponse.success(updatedInvoice)
        } catch (e: Exception) {
            ApiResponse.error("Failed to record payment: ${e.message}")
        }
    }

    override suspend fun duplicateInvoice(id: String): ApiResponse<Invoice> {
        return try {
            // Duplicate invoice via backend function that creates copy with new number
            val duplicateData = mapOf("invoice_id" to id)

            // Call backend function that duplicates invoice with new invoice number
            val duplicatedInvoice: Invoice = client.post(
                "rest/v1/rpc/duplicate_invoice",
                duplicateData,
            )

            ApiResponse.success(duplicatedInvoice)
        } catch (e: Exception) {
            ApiResponse.error("Failed to duplicate invoice: ${e.message}")
        }
    }

    override suspend fun patchInvoice(id: String, request: PatchInvoiceRequest): ApiResponse<Invoice> {
        return try {
            // TODO: Implement PATCH invoice
            ApiResponse.error("Patch invoice not implemented")
        } catch (e: Exception) {
            ApiResponse.error("Failed to patch invoice: ${e.message}")
        }
    }

    override suspend fun generatePdf(id: String, options: PdfGenerationOptions): ApiResponse<PdfResponse> {
        return try {
            // Generate PDF via backend service
            val pdfData = mapOf<String, Any?>(
                "invoice_id" to id,
                "template" to options.template,
                "include_payment_stub" to options.includePaymentStub,
                "watermark" to options.watermark,
                "locale" to options.locale,
            )

            // Call backend function that generates PDF and returns download URL
            val pdfResponse: PdfResponse = client.post(
                "rest/v1/rpc/generate_invoice_pdf",
                pdfData,
            )

            ApiResponse.success(pdfResponse)
        } catch (e: Exception) {
            ApiResponse.error("Failed to generate PDF: ${e.message}")
        }
    }

    override suspend fun convertToTemplate(id: String, templateName: String): ApiResponse<InvoiceTemplate> {
        return try {
            // Convert invoice to template via backend function
            val templateData = mapOf(
                "invoice_id" to id,
                "template_name" to templateName,
            )

            // Call backend function that creates template from invoice
            val template: InvoiceTemplate = client.post(
                "rest/v1/rpc/convert_to_template",
                templateData,
            )

            ApiResponse.success(template)
        } catch (e: Exception) {
            ApiResponse.error("Failed to convert to template: ${e.message}")
        }
    }

    override suspend fun createFromTemplate(templateId: String, request: CreateFromTemplateRequest): ApiResponse<Invoice> {
        return try {
            // Create invoice from template via backend function
            val invoiceData = mapOf<String, Any?>(
                "template_id" to templateId,
                "customer_id" to request.customerId,
                "due_date" to request.dueDate,
                "notes" to request.notes,
                "customizations" to request.customizations,
            )

            // Call backend function that creates invoice from template
            val createdInvoice: Invoice = client.post(
                "rest/v1/rpc/create_from_template",
                invoiceData,
            )

            ApiResponse.success(createdInvoice)
        } catch (e: Exception) {
            ApiResponse.error("Failed to create from template: ${e.message}")
        }
    }
}
