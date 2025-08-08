-- 0002_countries_provinces.sql: Geographic data for onboarding location selection

-- Countries table with comprehensive data
create table if not exists public.countries (
  id uuid primary key default gen_random_uuid(),
  iso_code char(2) not null unique, -- ISO 3166-1 alpha-2 (e.g., "US", "ZA")
  iso3_code char(3) not null unique, -- ISO 3166-1 alpha-3 (e.g., "USA", "ZAF")
  name text not null,
  official_name text,
  currency_code char(3), -- ISO 4217 currency code
  phone_prefix text,
  continent text not null,
  region text,
  subregion text,
  is_active boolean not null default true,
  display_order int default 999,
  created_at timestamptz not null default now()
);

-- Provinces/States table for subdivision data
create table if not exists public.provinces (
  id uuid primary key default gen_random_uuid(),
  country_id uuid not null references public.countries(id) on delete cascade,
  code text not null, -- Province/state code (e.g., "CA" for California, "GP" for Gauteng)
  name text not null,
  type text not null default 'province', -- 'province', 'state', 'region', 'territory'
  is_active boolean not null default true,
  display_order int default 999,
  created_at timestamptz not null default now(),
  unique(country_id, code)
);

-- Indexes for performance
create index if not exists idx_countries_iso_code on public.countries(iso_code);
create index if not exists idx_countries_active on public.countries(is_active) where is_active = true;
create index if not exists idx_provinces_country on public.provinces(country_id);
create index if not exists idx_provinces_active on public.provinces(is_active) where is_active = true;

-- RLS policies (read-only for all authenticated users)
alter table public.countries enable row level security;
alter table public.provinces enable row level security;

-- Countries are readable by all authenticated users (reference data)
create policy countries_select on public.countries 
  for select using (auth.uid() is not null);

-- Provinces are readable by all authenticated users (reference data) 
create policy provinces_select on public.provinces
  for select using (auth.uid() is not null);

-- Insert seed data for major countries and their subdivisions
-- Priority countries for business operations
insert into public.countries (iso_code, iso3_code, name, official_name, currency_code, phone_prefix, continent, region, subregion, display_order) values 
  ('ZA', 'ZAF', 'South Africa', 'Republic of South Africa', 'ZAR', '+27', 'Africa', 'Africa', 'Southern Africa', 1),
  ('US', 'USA', 'United States', 'United States of America', 'USD', '+1', 'North America', 'Americas', 'Northern America', 2),
  ('GB', 'GBR', 'United Kingdom', 'United Kingdom of Great Britain and Northern Ireland', 'GBP', '+44', 'Europe', 'Europe', 'Northern Europe', 3),
  ('CA', 'CAN', 'Canada', 'Canada', 'CAD', '+1', 'North America', 'Americas', 'Northern America', 4),
  ('AU', 'AUS', 'Australia', 'Commonwealth of Australia', 'AUD', '+61', 'Oceania', 'Oceania', 'Australia and New Zealand', 5),
  ('DE', 'DEU', 'Germany', 'Federal Republic of Germany', 'EUR', '+49', 'Europe', 'Europe', 'Western Europe', 6),
  ('FR', 'FRA', 'France', 'French Republic', 'EUR', '+33', 'Europe', 'Europe', 'Western Europe', 7),
  ('NL', 'NLD', 'Netherlands', 'Kingdom of the Netherlands', 'EUR', '+31', 'Europe', 'Europe', 'Western Europe', 8),
  ('SG', 'SGP', 'Singapore', 'Republic of Singapore', 'SGD', '+65', 'Asia', 'Asia', 'South-Eastern Asia', 9),
  ('AE', 'ARE', 'United Arab Emirates', 'United Arab Emirates', 'AED', '+971', 'Asia', 'Asia', 'Western Asia', 10)
on conflict (iso_code) do nothing;

-- Get country IDs for province insertion
do $$
declare
  za_id uuid;
  us_id uuid; 
  gb_id uuid;
  ca_id uuid;
  au_id uuid;
begin
  -- Get country UUIDs
  select id into za_id from public.countries where iso_code = 'ZA';
  select id into us_id from public.countries where iso_code = 'US';
  select id into gb_id from public.countries where iso_code = 'GB';
  select id into ca_id from public.countries where iso_code = 'CA';
  select id into au_id from public.countries where iso_code = 'AU';
  
  -- South African provinces
  insert into public.provinces (country_id, code, name, type, display_order) values
    (za_id, 'GP', 'Gauteng', 'province', 1),
    (za_id, 'WC', 'Western Cape', 'province', 2),
    (za_id, 'KZN', 'KwaZulu-Natal', 'province', 3),
    (za_id, 'EC', 'Eastern Cape', 'province', 4),
    (za_id, 'FS', 'Free State', 'province', 5),
    (za_id, 'MP', 'Mpumalanga', 'province', 6),
    (za_id, 'LP', 'Limpopo', 'province', 7),
    (za_id, 'NW', 'North West', 'province', 8),
    (za_id, 'NC', 'Northern Cape', 'province', 9)
  on conflict (country_id, code) do nothing;
  
  -- US states (major ones)
  insert into public.provinces (country_id, code, name, type, display_order) values
    (us_id, 'CA', 'California', 'state', 1),
    (us_id, 'NY', 'New York', 'state', 2),
    (us_id, 'TX', 'Texas', 'state', 3),
    (us_id, 'FL', 'Florida', 'state', 4),
    (us_id, 'IL', 'Illinois', 'state', 5),
    (us_id, 'PA', 'Pennsylvania', 'state', 6),
    (us_id, 'OH', 'Ohio', 'state', 7),
    (us_id, 'GA', 'Georgia', 'state', 8),
    (us_id, 'NC', 'North Carolina', 'state', 9),
    (us_id, 'MI', 'Michigan', 'state', 10)
  on conflict (country_id, code) do nothing;
  
  -- UK regions
  insert into public.provinces (country_id, code, name, type, display_order) values
    (gb_id, 'ENG', 'England', 'region', 1),
    (gb_id, 'SCT', 'Scotland', 'region', 2),
    (gb_id, 'WLS', 'Wales', 'region', 3),
    (gb_id, 'NIR', 'Northern Ireland', 'region', 4)
  on conflict (country_id, code) do nothing;
  
  -- Canadian provinces
  insert into public.provinces (country_id, code, name, type, display_order) values
    (ca_id, 'ON', 'Ontario', 'province', 1),
    (ca_id, 'QC', 'Quebec', 'province', 2),
    (ca_id, 'BC', 'British Columbia', 'province', 3),
    (ca_id, 'AB', 'Alberta', 'province', 4),
    (ca_id, 'MB', 'Manitoba', 'province', 5),
    (ca_id, 'SK', 'Saskatchewan', 'province', 6),
    (ca_id, 'NS', 'Nova Scotia', 'province', 7),
    (ca_id, 'NB', 'New Brunswick', 'province', 8),
    (ca_id, 'NL', 'Newfoundland and Labrador', 'province', 9),
    (ca_id, 'PE', 'Prince Edward Island', 'province', 10),
    (ca_id, 'NT', 'Northwest Territories', 'territory', 11),
    (ca_id, 'YT', 'Yukon', 'territory', 12),
    (ca_id, 'NU', 'Nunavut', 'territory', 13)
  on conflict (country_id, code) do nothing;
  
  -- Australian states and territories
  insert into public.provinces (country_id, code, name, type, display_order) values
    (au_id, 'NSW', 'New South Wales', 'state', 1),
    (au_id, 'VIC', 'Victoria', 'state', 2),
    (au_id, 'QLD', 'Queensland', 'state', 3),
    (au_id, 'WA', 'Western Australia', 'state', 4),
    (au_id, 'SA', 'South Australia', 'state', 5),
    (au_id, 'TAS', 'Tasmania', 'state', 6),
    (au_id, 'ACT', 'Australian Capital Territory', 'territory', 7),
    (au_id, 'NT', 'Northern Territory', 'territory', 8)
  on conflict (country_id, code) do nothing;
end $$;

-- Create view for easy querying with country-province relationships
create or replace view public.countries_with_provinces as
select 
  c.id as country_id,
  c.iso_code,
  c.name as country_name,
  c.currency_code,
  c.phone_prefix,
  p.id as province_id,
  p.code as province_code,
  p.name as province_name,
  p.type as province_type
from public.countries c
left join public.provinces p on c.id = p.country_id
where c.is_active = true 
  and (p.is_active = true or p.id is null)
order by c.display_order, p.display_order;

-- RLS policy for the view
create policy countries_with_provinces_select on public.countries_with_provinces
  for select using (auth.uid() is not null);

-- Add extended columns to business_profile for onboarding data
alter table public.business_profile 
add column if not exists business_type text, -- 'individual', 'company'
add column if not exists company_registration_number text,
add column if not exists website text,
add column if not exists industry text,
add column if not exists employee_count text, -- '1', '2-10', '11-50', '51-200', '200+'
add column if not exists business_address jsonb, -- Full business address
add column if not exists country_id uuid references public.countries(id),
add column if not exists province_id uuid references public.provinces(id),
add column if not exists phone text,
add column if not exists description text;

-- Create indexes for the new columns
create index if not exists idx_business_profile_country on public.business_profile(country_id);
create index if not exists idx_business_profile_province on public.business_profile(province_id);
create index if not exists idx_business_profile_type on public.business_profile(business_type);