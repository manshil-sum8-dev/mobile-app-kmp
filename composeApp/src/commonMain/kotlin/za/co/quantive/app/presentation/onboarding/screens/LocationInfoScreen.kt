package za.co.quantive.app.presentation.onboarding.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import za.co.quantive.app.domain.profile.BusinessAddress
import za.co.quantive.app.domain.profile.Country
import za.co.quantive.app.domain.profile.Province

@Composable
fun LocationInfoScreen(
    businessAddress: BusinessAddress?,
    onBusinessAddressChange: (BusinessAddress) -> Unit,
    selectedCountry: Country?,
    onCountrySelected: (Country) -> Unit,
    selectedProvince: Province?,
    onProvinceSelected: (Province) -> Unit,
    phone: String?,
    onPhoneChange: (String) -> Unit,
    description: String?,
    onDescriptionChange: (String) -> Unit,
    countries: List<Country>,
    provinces: List<Province>,
    isLoadingProvinces: Boolean,
    onLoadCountries: () -> Unit,
    onLoadProvinces: (String) -> Unit,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    // Load countries on screen entry
    LaunchedEffect(Unit) {
        if (countries.isEmpty()) {
            onLoadCountries()
        }
    }

    // Load provinces when country changes
    LaunchedEffect(selectedCountry) {
        selectedCountry?.let { country ->
            onLoadProvinces(country.id)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(24.dp),
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Header
        Text(
            text = "Location & Contact Information",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Step 2 of 2 • Complete your profile",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Country Selection
        var countryDropdownExpanded by remember { mutableStateOf(false) }

        Box {
            OutlinedTextField(
                value = selectedCountry?.name ?: "",
                onValueChange = { },
                readOnly = true,
                label = { Text("Country *") },
                placeholder = { Text("Select your country") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { countryDropdownExpanded = true },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                ),
                shape = RoundedCornerShape(12.dp),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Select Country",
                        modifier = Modifier.clickable {
                            countryDropdownExpanded = !countryDropdownExpanded
                        },
                    )
                },
            )

            DropdownMenu(
                expanded = countryDropdownExpanded,
                onDismissRequest = { countryDropdownExpanded = false },
                modifier = Modifier.fillMaxWidth(),
            ) {
                countries.forEach { country ->
                    DropdownMenuItem(
                        text = { Text("${country.name} (${country.iso_code})") },
                        onClick = {
                            onCountrySelected(country)
                            countryDropdownExpanded = false
                        },
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Province/State Selection
        var provinceDropdownExpanded by remember { mutableStateOf(false) }

        Box {
            OutlinedTextField(
                value = selectedProvince?.name ?: "",
                onValueChange = { },
                readOnly = true,
                label = { Text("Province/State") },
                placeholder = { Text("Select your province/state") },
                enabled = selectedCountry != null && !isLoadingProvinces,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        if (selectedCountry != null && !isLoadingProvinces) {
                            provinceDropdownExpanded = true
                        }
                    },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                ),
                shape = RoundedCornerShape(12.dp),
                trailingIcon = {
                    if (isLoadingProvinces) {
                        CircularProgressIndicator(
                            modifier = Modifier.padding(12.dp),
                            strokeWidth = 2.dp,
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Select Province",
                            modifier = Modifier.clickable {
                                if (selectedCountry != null) {
                                    provinceDropdownExpanded = !provinceDropdownExpanded
                                }
                            },
                        )
                    }
                },
            )

            DropdownMenu(
                expanded = provinceDropdownExpanded,
                onDismissRequest = { provinceDropdownExpanded = false },
                modifier = Modifier.fillMaxWidth(),
            ) {
                provinces.forEach { province ->
                    DropdownMenuItem(
                        text = { Text("${province.name} (${province.code})") },
                        onClick = {
                            onProvinceSelected(province)
                            provinceDropdownExpanded = false
                        },
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Street Address
        OutlinedTextField(
            value = businessAddress?.street ?: "",
            onValueChange = { street ->
                onBusinessAddressChange(
                    (businessAddress ?: BusinessAddress()).copy(street = street),
                )
            },
            label = { Text("Street Address") },
            placeholder = { Text("Enter your street address") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
        )

        Spacer(modifier = Modifier.height(16.dp))

        // City and Postal Code Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // City
            OutlinedTextField(
                value = businessAddress?.city ?: "",
                onValueChange = { city ->
                    onBusinessAddressChange(
                        (businessAddress ?: BusinessAddress()).copy(city = city),
                    )
                },
                label = { Text("City") },
                placeholder = { Text("Enter city") },
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                ),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
            )

            // Postal Code
            OutlinedTextField(
                value = businessAddress?.postal_code ?: "",
                onValueChange = { postalCode ->
                    onBusinessAddressChange(
                        (businessAddress ?: BusinessAddress()).copy(postal_code = postalCode),
                    )
                },
                label = { Text("Postal Code") },
                placeholder = { Text("12345") },
                modifier = Modifier.weight(0.7f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                ),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Phone Number
        OutlinedTextField(
            value = phone ?: "",
            onValueChange = onPhoneChange,
            label = {
                Text(
                    if (selectedCountry?.phone_prefix != null) {
                        "Phone Number (${selectedCountry.phone_prefix})"
                    } else {
                        "Phone Number"
                    },
                )
            },
            placeholder = { Text("Enter your phone number") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Business Description
        OutlinedTextField(
            value = description ?: "",
            onValueChange = onDescriptionChange,
            label = { Text("Business Description (Optional)") },
            placeholder = { Text("Tell us what your business does...") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
            ),
            shape = RoundedCornerShape(12.dp),
            maxLines = 4,
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Complete Button
        Button(
            onClick = onComplete,
            enabled = selectedCountry != null,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
            shape = RoundedCornerShape(28.dp),
        ) {
            Text(
                text = "Complete Profile",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Progress indicator
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            repeat(2) { index ->
                Box(
                    modifier = Modifier
                        .height(4.dp)
                        .width(if (index == 1) 32.dp else 16.dp)
                        .background(
                            color = if (index == 1) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                            },
                            shape = RoundedCornerShape(2.dp),
                        ),
                )
                if (index < 1) Spacer(modifier = Modifier.width(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
