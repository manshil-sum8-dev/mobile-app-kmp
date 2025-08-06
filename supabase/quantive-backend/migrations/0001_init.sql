-- 0001_init.sql: Quantive schema and RLS

-- Enable required extensions
create extension if not exists pgcrypto;
create extension if not exists pgjwt;

-- Common updated_at trigger
create or replace function set_updated_at()
returns trigger as $$
begin
  new.updated_at = now();
  return new;
end;
$$ language plpgsql;

-- business_profile
create table if not exists public.business_profile (
  id uuid primary key default gen_random_uuid(),
  owner_id uuid not null,
  name text not null,
  currency text not null default 'USD',
  tax_id text,
  terms text,
  brand_color text,
  logo_path text,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);
create index if not exists idx_business_profile_owner on public.business_profile(owner_id);
create trigger business_profile_updated_at before update on public.business_profile for each row execute function set_updated_at();

-- contacts
create table if not exists public.contacts (
  id uuid primary key default gen_random_uuid(),
  owner_id uuid not null,
  type smallint not null, -- 0 customer, 1 supplier
  display_name text not null,
  email text,
  phone text,
  billing_address jsonb,
  shipping_address jsonb,
  tax_id text,
  payment_terms text,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);
create index if not exists idx_contacts_owner on public.contacts(owner_id);
create trigger contacts_updated_at before update on public.contacts for each row execute function set_updated_at();

-- items
create table if not exists public.items (
  id uuid primary key default gen_random_uuid(),
  owner_id uuid not null,
  name text not null,
  sku text,
  type smallint not null, -- 0 product, 1 service
  unit_price_cents int not null default 0,
  currency text not null default 'USD',
  tax_category text,
  description text,
  uom text,
  track_inventory boolean not null default false,
  qty_on_hand numeric,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);
create index if not exists idx_items_owner on public.items(owner_id);
create trigger items_updated_at before update on public.items for each row execute function set_updated_at();

-- invoices
create table if not exists public.invoices (
  id uuid primary key default gen_random_uuid(),
  owner_id uuid not null,
  invoice_number text not null,
  customer_id uuid not null references public.contacts(id) on delete restrict,
  currency text not null default 'USD',
  issue_date date not null,
  due_date date not null,
  status smallint not null default 0, -- 0 Draft,1 Sent,2 Paid,3 Overdue,4 Void
  notes text,
  terms text,
  pdf_path text,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);
create index if not exists idx_invoices_owner on public.invoices(owner_id);
create index if not exists idx_invoices_customer on public.invoices(customer_id);
create trigger invoices_updated_at before update on public.invoices for each row execute function set_updated_at();

-- invoice_lines
create table if not exists public.invoice_lines (
  id uuid primary key default gen_random_uuid(),
  owner_id uuid not null,
  invoice_id uuid not null references public.invoices(id) on delete cascade,
  item_id uuid references public.items(id),
  description text,
  quantity numeric not null default 1,
  unit_price_cents int not null default 0,
  tax_rate numeric not null default 0,
  discount_cents int not null default 0
);
create index if not exists idx_invoice_lines_owner on public.invoice_lines(owner_id);
create index if not exists idx_invoice_lines_invoice on public.invoice_lines(invoice_id);

-- payments
create table if not exists public.payments (
  id uuid primary key default gen_random_uuid(),
  owner_id uuid not null,
  invoice_id uuid not null references public.invoices(id) on delete cascade,
  amount_cents int not null,
  date date not null,
  method text,
  reference text,
  notes text,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);
create index if not exists idx_payments_owner on public.payments(owner_id);
create index if not exists idx_payments_invoice on public.payments(invoice_id);
create trigger payments_updated_at before update on public.payments for each row execute function set_updated_at();

-- RLS
alter table public.business_profile enable row level security;
alter table public.contacts enable row level security;
alter table public.items enable row level security;
alter table public.invoices enable row level security;
alter table public.invoice_lines enable row level security;
alter table public.payments enable row level security;

-- Helper: ownership check
create or replace function is_owner(owner uuid)
returns boolean as $$
  select owner = auth.uid();
$$ language sql stable;

-- Policies (select/insert/update only for owner)
create policy if not exists business_profile_select on public.business_profile for select using (owner_id = auth.uid());
create policy if not exists business_profile_ins on public.business_profile for insert with check (owner_id = auth.uid());
create policy if not exists business_profile_upd on public.business_profile for update using (owner_id = auth.uid()) with check (owner_id = auth.uid());

create policy if not exists contacts_select on public.contacts for select using (owner_id = auth.uid());
create policy if not exists contacts_ins on public.contacts for insert with check (owner_id = auth.uid());
create policy if not exists contacts_upd on public.contacts for update using (owner_id = auth.uid()) with check (owner_id = auth.uid());

create policy if not exists items_select on public.items for select using (owner_id = auth.uid());
create policy if not exists items_ins on public.items for insert with check (owner_id = auth.uid());
create policy if not exists items_upd on public.items for update using (owner_id = auth.uid()) with check (owner_id = auth.uid());

create policy if not exists invoices_select on public.invoices for select using (owner_id = auth.uid());
create policy if not exists invoices_ins on public.invoices for insert with check (owner_id = auth.uid());
create policy if not exists invoices_upd on public.invoices for update using (owner_id = auth.uid()) with check (owner_id = auth.uid());

create policy if not exists invoice_lines_select on public.invoice_lines for select using (owner_id = auth.uid());
create policy if not exists invoice_lines_ins on public.invoice_lines for insert with check (owner_id = auth.uid());
create policy if not exists invoice_lines_upd on public.invoice_lines for update using (owner_id = auth.uid()) with check (owner_id = auth.uid());

create policy if not exists payments_select on public.payments for select using (owner_id = auth.uid());
create policy if not exists payments_ins on public.payments for insert with check (owner_id = auth.uid());
create policy if not exists payments_upd on public.payments for update using (owner_id = auth.uid()) with check (owner_id = auth.uid());

-- RPC: next invoice number per owner
create table if not exists public.invoice_sequences (
  owner_id uuid primary key,
  current int not null default 0
);

create or replace function public.next_invoice_number()
returns text as $$
declare
  seq int;
  owner uuid := auth.uid();
begin
  insert into public.invoice_sequences(owner_id, current)
  values (owner, 0)
  on conflict (owner_id) do nothing;

  update public.invoice_sequences
  set current = invoice_sequences.current + 1
  where owner_id = owner
  returning current into seq;

  return 'INV-' || to_char(seq, 'FM000000');
end;
$$ language plpgsql security definer;
