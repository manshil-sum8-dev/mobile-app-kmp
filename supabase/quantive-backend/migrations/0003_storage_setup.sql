-- 0003_storage_setup.sql: Supabase Storage configuration for business logos

-- Create storage bucket for business logos
insert into storage.buckets (id, name, public, file_size_limit, allowed_mime_types)
values (
  'business-logos',
  'business-logos', 
  true, -- Public bucket for logo serving
  5242880, -- 5MB limit per file
  array['image/jpeg', 'image/jpg', 'image/png', 'image/webp', 'image/svg+xml']::text[]
) on conflict (id) do nothing;

-- Enable RLS on storage objects
alter table storage.objects enable row level security;

-- Allow authenticated users to upload logos to their own folders
create policy "Users can upload business logos"
  on storage.objects for insert
  with check (
    bucket_id = 'business-logos' 
    and auth.uid() is not null
    and (storage.foldername(name))[1] = auth.uid()::text
  );

-- Allow users to view their own business logos
create policy "Users can view their own business logos"
  on storage.objects for select
  using (
    bucket_id = 'business-logos' 
    and auth.uid() is not null
    and (storage.foldername(name))[1] = auth.uid()::text
  );

-- Allow users to update their own business logos  
create policy "Users can update their own business logos"
  on storage.objects for update
  using (
    bucket_id = 'business-logos'
    and auth.uid() is not null 
    and (storage.foldername(name))[1] = auth.uid()::text
  );

-- Allow users to delete their own business logos
create policy "Users can delete their own business logos"
  on storage.objects for delete
  using (
    bucket_id = 'business-logos'
    and auth.uid() is not null
    and (storage.foldername(name))[1] = auth.uid()::text
  );

-- Allow public read access to business logo files (since bucket is public)
create policy "Public read access to business logos" 
  on storage.objects for select
  using (bucket_id = 'business-logos');

-- Create helper function to generate logo storage path
create or replace function public.generate_logo_path(file_extension text default 'png')
returns text as $$
  select auth.uid()::text || '/business-logo-' || extract(epoch from now())::bigint || '.' || file_extension;
$$ language sql security definer;

-- Grant usage on storage functions to authenticated users
grant usage on schema storage to authenticated;
grant all on storage.objects to authenticated;
grant all on storage.buckets to authenticated;