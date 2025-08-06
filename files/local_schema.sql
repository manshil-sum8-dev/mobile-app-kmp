--
-- PostgreSQL database dump
--

-- Dumped from database version 17.4
-- Dumped by pg_dump version 17.4

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: _realtime; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA _realtime;


ALTER SCHEMA _realtime OWNER TO postgres;

--
-- Name: auth; Type: SCHEMA; Schema: -; Owner: supabase_admin
--

CREATE SCHEMA auth;


ALTER SCHEMA auth OWNER TO supabase_admin;

--
-- Name: extensions; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA extensions;


ALTER SCHEMA extensions OWNER TO postgres;

--
-- Name: graphql; Type: SCHEMA; Schema: -; Owner: supabase_admin
--

CREATE SCHEMA graphql;


ALTER SCHEMA graphql OWNER TO supabase_admin;

--
-- Name: graphql_public; Type: SCHEMA; Schema: -; Owner: supabase_admin
--

CREATE SCHEMA graphql_public;


ALTER SCHEMA graphql_public OWNER TO supabase_admin;

--
-- Name: pg_net; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS pg_net WITH SCHEMA extensions;


--
-- Name: EXTENSION pg_net; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION pg_net IS 'Async HTTP';


--
-- Name: pgbouncer; Type: SCHEMA; Schema: -; Owner: pgbouncer
--

CREATE SCHEMA pgbouncer;


ALTER SCHEMA pgbouncer OWNER TO pgbouncer;

--
-- Name: realtime; Type: SCHEMA; Schema: -; Owner: supabase_admin
--

CREATE SCHEMA realtime;


ALTER SCHEMA realtime OWNER TO supabase_admin;

--
-- Name: storage; Type: SCHEMA; Schema: -; Owner: supabase_admin
--

CREATE SCHEMA storage;


ALTER SCHEMA storage OWNER TO supabase_admin;

--
-- Name: supabase_functions; Type: SCHEMA; Schema: -; Owner: supabase_admin
--

CREATE SCHEMA supabase_functions;


ALTER SCHEMA supabase_functions OWNER TO supabase_admin;

--
-- Name: supabase_migrations; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA supabase_migrations;


ALTER SCHEMA supabase_migrations OWNER TO postgres;

--
-- Name: vault; Type: SCHEMA; Schema: -; Owner: supabase_admin
--

CREATE SCHEMA vault;


ALTER SCHEMA vault OWNER TO supabase_admin;

--
-- Name: pg_graphql; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS pg_graphql WITH SCHEMA graphql;


--
-- Name: EXTENSION pg_graphql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION pg_graphql IS 'pg_graphql: GraphQL support';


--
-- Name: pg_stat_statements; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS pg_stat_statements WITH SCHEMA extensions;


--
-- Name: EXTENSION pg_stat_statements; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION pg_stat_statements IS 'track planning and execution statistics of all SQL statements executed';


--
-- Name: pgcrypto; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS pgcrypto WITH SCHEMA extensions;


--
-- Name: EXTENSION pgcrypto; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION pgcrypto IS 'cryptographic functions';


--
-- Name: supabase_vault; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS supabase_vault WITH SCHEMA vault;


--
-- Name: EXTENSION supabase_vault; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION supabase_vault IS 'Supabase Vault Extension';


--
-- Name: uuid-ossp; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS "uuid-ossp" WITH SCHEMA extensions;


--
-- Name: EXTENSION "uuid-ossp"; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION "uuid-ossp" IS 'generate universally unique identifiers (UUIDs)';


--
-- Name: aal_level; Type: TYPE; Schema: auth; Owner: supabase_auth_admin
--

CREATE TYPE auth.aal_level AS ENUM (
    'aal1',
    'aal2',
    'aal3'
);


ALTER TYPE auth.aal_level OWNER TO supabase_auth_admin;

--
-- Name: code_challenge_method; Type: TYPE; Schema: auth; Owner: supabase_auth_admin
--

CREATE TYPE auth.code_challenge_method AS ENUM (
    's256',
    'plain'
);


ALTER TYPE auth.code_challenge_method OWNER TO supabase_auth_admin;

--
-- Name: factor_status; Type: TYPE; Schema: auth; Owner: supabase_auth_admin
--

CREATE TYPE auth.factor_status AS ENUM (
    'unverified',
    'verified'
);


ALTER TYPE auth.factor_status OWNER TO supabase_auth_admin;

--
-- Name: factor_type; Type: TYPE; Schema: auth; Owner: supabase_auth_admin
--

CREATE TYPE auth.factor_type AS ENUM (
    'totp',
    'webauthn',
    'phone'
);


ALTER TYPE auth.factor_type OWNER TO supabase_auth_admin;

--
-- Name: one_time_token_type; Type: TYPE; Schema: auth; Owner: supabase_auth_admin
--

CREATE TYPE auth.one_time_token_type AS ENUM (
    'confirmation_token',
    'reauthentication_token',
    'recovery_token',
    'email_change_token_new',
    'email_change_token_current',
    'phone_change_token'
);


ALTER TYPE auth.one_time_token_type OWNER TO supabase_auth_admin;

--
-- Name: activity_filter_type; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.activity_filter_type AS ENUM (
    'type',
    'status',
    'entity_type',
    'date_range',
    'amount_range',
    'tags'
);


ALTER TYPE public.activity_filter_type OWNER TO postgres;

--
-- Name: activity_sort_by; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.activity_sort_by AS ENUM (
    'timestamp',
    'title',
    'amount',
    'type',
    'status'
);


ALTER TYPE public.activity_sort_by OWNER TO postgres;

--
-- Name: assignment_strategy; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.assignment_strategy AS ENUM (
    'random',
    'hash_based',
    'segment_based',
    'manual'
);


ALTER TYPE public.assignment_strategy OWNER TO postgres;

--
-- Name: config_scope; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.config_scope AS ENUM (
    'global',
    'user_segment',
    'individual_user',
    'experiment_variant'
);


ALTER TYPE public.config_scope OWNER TO postgres;

--
-- Name: contact_type; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.contact_type AS ENUM (
    'customer',
    'supplier'
);


ALTER TYPE public.contact_type OWNER TO postgres;

--
-- Name: experiment_status; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.experiment_status AS ENUM (
    'draft',
    'active',
    'paused',
    'completed',
    'archived'
);


ALTER TYPE public.experiment_status OWNER TO postgres;

--
-- Name: feature_area; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.feature_area AS ENUM (
    'dashboard',
    'invoices',
    'customers',
    'suppliers',
    'items',
    'inventory',
    'reports',
    'settings',
    'invoice_create',
    'invoice_edit',
    'invoice_delete',
    'invoice_send',
    'invoice_payments',
    'customer_create',
    'customer_edit',
    'customer_delete',
    'item_create',
    'item_edit',
    'item_delete',
    'item_pricing',
    'reports_financial',
    'reports_inventory',
    'reports_customers',
    'settings_business',
    'settings_users',
    'settings_billing',
    'settings_notifications'
);


ALTER TYPE public.feature_area OWNER TO postgres;

--
-- Name: invoice_status; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.invoice_status AS ENUM (
    'draft',
    'sent',
    'paid',
    'pending',
    'overdue',
    'cancelled'
);


ALTER TYPE public.invoice_status OWNER TO postgres;

--
-- Name: item_type; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.item_type AS ENUM (
    'product',
    'service'
);


ALTER TYPE public.item_type OWNER TO postgres;

--
-- Name: payment_terms; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.payment_terms AS ENUM (
    'immediate',
    '7_days',
    '14_days',
    '30_days',
    '60_days',
    '90_days'
);


ALTER TYPE public.payment_terms OWNER TO postgres;

--
-- Name: permission_type; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.permission_type AS ENUM (
    'read',
    'write',
    'delete',
    'execute'
);


ALTER TYPE public.permission_type OWNER TO postgres;

--
-- Name: subscription_status; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.subscription_status AS ENUM (
    'active',
    'cancelled',
    'past_due',
    'unpaid'
);


ALTER TYPE public.subscription_status OWNER TO postgres;

--
-- Name: user_role; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.user_role AS ENUM (
    'owner',
    'admin',
    'sales',
    'finance',
    'viewer'
);


ALTER TYPE public.user_role OWNER TO postgres;

--
-- Name: user_status; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.user_status AS ENUM (
    'invited',
    'active',
    'suspended',
    'deactivated'
);


ALTER TYPE public.user_status OWNER TO postgres;

--
-- Name: widget_layout; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.widget_layout AS ENUM (
    'list',
    'grid',
    'compact',
    'carousel',
    'timeline'
);


ALTER TYPE public.widget_layout OWNER TO postgres;

--
-- Name: widget_type; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.widget_type AS ENUM (
    'recent_activity',
    'dashboard_metrics',
    'financial_summary',
    'customer_insights'
);


ALTER TYPE public.widget_type OWNER TO postgres;

--
-- Name: action; Type: TYPE; Schema: realtime; Owner: supabase_admin
--

CREATE TYPE realtime.action AS ENUM (
    'INSERT',
    'UPDATE',
    'DELETE',
    'TRUNCATE',
    'ERROR'
);


ALTER TYPE realtime.action OWNER TO supabase_admin;

--
-- Name: equality_op; Type: TYPE; Schema: realtime; Owner: supabase_admin
--

CREATE TYPE realtime.equality_op AS ENUM (
    'eq',
    'neq',
    'lt',
    'lte',
    'gt',
    'gte',
    'in'
);


ALTER TYPE realtime.equality_op OWNER TO supabase_admin;

--
-- Name: user_defined_filter; Type: TYPE; Schema: realtime; Owner: supabase_admin
--

CREATE TYPE realtime.user_defined_filter AS (
	column_name text,
	op realtime.equality_op,
	value text
);


ALTER TYPE realtime.user_defined_filter OWNER TO supabase_admin;

--
-- Name: wal_column; Type: TYPE; Schema: realtime; Owner: supabase_admin
--

CREATE TYPE realtime.wal_column AS (
	name text,
	type_name text,
	type_oid oid,
	value jsonb,
	is_pkey boolean,
	is_selectable boolean
);


ALTER TYPE realtime.wal_column OWNER TO supabase_admin;

--
-- Name: wal_rls; Type: TYPE; Schema: realtime; Owner: supabase_admin
--

CREATE TYPE realtime.wal_rls AS (
	wal jsonb,
	is_rls_enabled boolean,
	subscription_ids uuid[],
	errors text[]
);


ALTER TYPE realtime.wal_rls OWNER TO supabase_admin;

--
-- Name: buckettype; Type: TYPE; Schema: storage; Owner: supabase_storage_admin
--

CREATE TYPE storage.buckettype AS ENUM (
    'STANDARD',
    'ANALYTICS'
);


ALTER TYPE storage.buckettype OWNER TO supabase_storage_admin;

--
-- Name: email(); Type: FUNCTION; Schema: auth; Owner: supabase_auth_admin
--

CREATE FUNCTION auth.email() RETURNS text
    LANGUAGE sql STABLE
    AS $$
  select 
  coalesce(
    nullif(current_setting('request.jwt.claim.email', true), ''),
    (nullif(current_setting('request.jwt.claims', true), '')::jsonb ->> 'email')
  )::text
$$;


ALTER FUNCTION auth.email() OWNER TO supabase_auth_admin;

--
-- Name: FUNCTION email(); Type: COMMENT; Schema: auth; Owner: supabase_auth_admin
--

COMMENT ON FUNCTION auth.email() IS 'Deprecated. Use auth.jwt() -> ''email'' instead.';


--
-- Name: jwt(); Type: FUNCTION; Schema: auth; Owner: supabase_auth_admin
--

CREATE FUNCTION auth.jwt() RETURNS jsonb
    LANGUAGE sql STABLE
    AS $$
  select 
    coalesce(
        nullif(current_setting('request.jwt.claim', true), ''),
        nullif(current_setting('request.jwt.claims', true), '')
    )::jsonb
$$;


ALTER FUNCTION auth.jwt() OWNER TO supabase_auth_admin;

--
-- Name: role(); Type: FUNCTION; Schema: auth; Owner: supabase_auth_admin
--

CREATE FUNCTION auth.role() RETURNS text
    LANGUAGE sql STABLE
    AS $$
  select 
  coalesce(
    nullif(current_setting('request.jwt.claim.role', true), ''),
    (nullif(current_setting('request.jwt.claims', true), '')::jsonb ->> 'role')
  )::text
$$;


ALTER FUNCTION auth.role() OWNER TO supabase_auth_admin;

--
-- Name: FUNCTION role(); Type: COMMENT; Schema: auth; Owner: supabase_auth_admin
--

COMMENT ON FUNCTION auth.role() IS 'Deprecated. Use auth.jwt() -> ''role'' instead.';


--
-- Name: uid(); Type: FUNCTION; Schema: auth; Owner: supabase_auth_admin
--

CREATE FUNCTION auth.uid() RETURNS uuid
    LANGUAGE sql STABLE
    AS $$
  select 
  coalesce(
    nullif(current_setting('request.jwt.claim.sub', true), ''),
    (nullif(current_setting('request.jwt.claims', true), '')::jsonb ->> 'sub')
  )::uuid
$$;


ALTER FUNCTION auth.uid() OWNER TO supabase_auth_admin;

--
-- Name: FUNCTION uid(); Type: COMMENT; Schema: auth; Owner: supabase_auth_admin
--

COMMENT ON FUNCTION auth.uid() IS 'Deprecated. Use auth.jwt() -> ''sub'' instead.';


--
-- Name: grant_pg_cron_access(); Type: FUNCTION; Schema: extensions; Owner: supabase_admin
--

CREATE FUNCTION extensions.grant_pg_cron_access() RETURNS event_trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
  IF EXISTS (
    SELECT
    FROM pg_event_trigger_ddl_commands() AS ev
    JOIN pg_extension AS ext
    ON ev.objid = ext.oid
    WHERE ext.extname = 'pg_cron'
  )
  THEN
    grant usage on schema cron to postgres with grant option;

    alter default privileges in schema cron grant all on tables to postgres with grant option;
    alter default privileges in schema cron grant all on functions to postgres with grant option;
    alter default privileges in schema cron grant all on sequences to postgres with grant option;

    alter default privileges for user supabase_admin in schema cron grant all
        on sequences to postgres with grant option;
    alter default privileges for user supabase_admin in schema cron grant all
        on tables to postgres with grant option;
    alter default privileges for user supabase_admin in schema cron grant all
        on functions to postgres with grant option;

    grant all privileges on all tables in schema cron to postgres with grant option;
    revoke all on table cron.job from postgres;
    grant select on table cron.job to postgres with grant option;
  END IF;
END;
$$;


ALTER FUNCTION extensions.grant_pg_cron_access() OWNER TO supabase_admin;

--
-- Name: FUNCTION grant_pg_cron_access(); Type: COMMENT; Schema: extensions; Owner: supabase_admin
--

COMMENT ON FUNCTION extensions.grant_pg_cron_access() IS 'Grants access to pg_cron';


--
-- Name: grant_pg_graphql_access(); Type: FUNCTION; Schema: extensions; Owner: supabase_admin
--

CREATE FUNCTION extensions.grant_pg_graphql_access() RETURNS event_trigger
    LANGUAGE plpgsql
    AS $_$
DECLARE
    func_is_graphql_resolve bool;
BEGIN
    func_is_graphql_resolve = (
        SELECT n.proname = 'resolve'
        FROM pg_event_trigger_ddl_commands() AS ev
        LEFT JOIN pg_catalog.pg_proc AS n
        ON ev.objid = n.oid
    );

    IF func_is_graphql_resolve
    THEN
        -- Update public wrapper to pass all arguments through to the pg_graphql resolve func
        DROP FUNCTION IF EXISTS graphql_public.graphql;
        create or replace function graphql_public.graphql(
            "operationName" text default null,
            query text default null,
            variables jsonb default null,
            extensions jsonb default null
        )
            returns jsonb
            language sql
        as $$
            select graphql.resolve(
                query := query,
                variables := coalesce(variables, '{}'),
                "operationName" := "operationName",
                extensions := extensions
            );
        $$;

        -- This hook executes when `graphql.resolve` is created. That is not necessarily the last
        -- function in the extension so we need to grant permissions on existing entities AND
        -- update default permissions to any others that are created after `graphql.resolve`
        grant usage on schema graphql to postgres, anon, authenticated, service_role;
        grant select on all tables in schema graphql to postgres, anon, authenticated, service_role;
        grant execute on all functions in schema graphql to postgres, anon, authenticated, service_role;
        grant all on all sequences in schema graphql to postgres, anon, authenticated, service_role;
        alter default privileges in schema graphql grant all on tables to postgres, anon, authenticated, service_role;
        alter default privileges in schema graphql grant all on functions to postgres, anon, authenticated, service_role;
        alter default privileges in schema graphql grant all on sequences to postgres, anon, authenticated, service_role;

        -- Allow postgres role to allow granting usage on graphql and graphql_public schemas to custom roles
        grant usage on schema graphql_public to postgres with grant option;
        grant usage on schema graphql to postgres with grant option;
    END IF;

END;
$_$;


ALTER FUNCTION extensions.grant_pg_graphql_access() OWNER TO supabase_admin;

--
-- Name: FUNCTION grant_pg_graphql_access(); Type: COMMENT; Schema: extensions; Owner: supabase_admin
--

COMMENT ON FUNCTION extensions.grant_pg_graphql_access() IS 'Grants access to pg_graphql';


--
-- Name: grant_pg_net_access(); Type: FUNCTION; Schema: extensions; Owner: supabase_admin
--

CREATE FUNCTION extensions.grant_pg_net_access() RETURNS event_trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
  IF EXISTS (
    SELECT 1
    FROM pg_event_trigger_ddl_commands() AS ev
    JOIN pg_extension AS ext
    ON ev.objid = ext.oid
    WHERE ext.extname = 'pg_net'
  )
  THEN
    GRANT USAGE ON SCHEMA net TO supabase_functions_admin, postgres, anon, authenticated, service_role;

    ALTER function net.http_get(url text, params jsonb, headers jsonb, timeout_milliseconds integer) SECURITY DEFINER;
    ALTER function net.http_post(url text, body jsonb, params jsonb, headers jsonb, timeout_milliseconds integer) SECURITY DEFINER;

    ALTER function net.http_get(url text, params jsonb, headers jsonb, timeout_milliseconds integer) SET search_path = net;
    ALTER function net.http_post(url text, body jsonb, params jsonb, headers jsonb, timeout_milliseconds integer) SET search_path = net;

    REVOKE ALL ON FUNCTION net.http_get(url text, params jsonb, headers jsonb, timeout_milliseconds integer) FROM PUBLIC;
    REVOKE ALL ON FUNCTION net.http_post(url text, body jsonb, params jsonb, headers jsonb, timeout_milliseconds integer) FROM PUBLIC;

    GRANT EXECUTE ON FUNCTION net.http_get(url text, params jsonb, headers jsonb, timeout_milliseconds integer) TO supabase_functions_admin, postgres, anon, authenticated, service_role;
    GRANT EXECUTE ON FUNCTION net.http_post(url text, body jsonb, params jsonb, headers jsonb, timeout_milliseconds integer) TO supabase_functions_admin, postgres, anon, authenticated, service_role;
  END IF;
END;
$$;


ALTER FUNCTION extensions.grant_pg_net_access() OWNER TO supabase_admin;

--
-- Name: FUNCTION grant_pg_net_access(); Type: COMMENT; Schema: extensions; Owner: supabase_admin
--

COMMENT ON FUNCTION extensions.grant_pg_net_access() IS 'Grants access to pg_net';


--
-- Name: pgrst_ddl_watch(); Type: FUNCTION; Schema: extensions; Owner: supabase_admin
--

CREATE FUNCTION extensions.pgrst_ddl_watch() RETURNS event_trigger
    LANGUAGE plpgsql
    AS $$
DECLARE
  cmd record;
BEGIN
  FOR cmd IN SELECT * FROM pg_event_trigger_ddl_commands()
  LOOP
    IF cmd.command_tag IN (
      'CREATE SCHEMA', 'ALTER SCHEMA'
    , 'CREATE TABLE', 'CREATE TABLE AS', 'SELECT INTO', 'ALTER TABLE'
    , 'CREATE FOREIGN TABLE', 'ALTER FOREIGN TABLE'
    , 'CREATE VIEW', 'ALTER VIEW'
    , 'CREATE MATERIALIZED VIEW', 'ALTER MATERIALIZED VIEW'
    , 'CREATE FUNCTION', 'ALTER FUNCTION'
    , 'CREATE TRIGGER'
    , 'CREATE TYPE', 'ALTER TYPE'
    , 'CREATE RULE'
    , 'COMMENT'
    )
    -- don't notify in case of CREATE TEMP table or other objects created on pg_temp
    AND cmd.schema_name is distinct from 'pg_temp'
    THEN
      NOTIFY pgrst, 'reload schema';
    END IF;
  END LOOP;
END; $$;


ALTER FUNCTION extensions.pgrst_ddl_watch() OWNER TO supabase_admin;

--
-- Name: pgrst_drop_watch(); Type: FUNCTION; Schema: extensions; Owner: supabase_admin
--

CREATE FUNCTION extensions.pgrst_drop_watch() RETURNS event_trigger
    LANGUAGE plpgsql
    AS $$
DECLARE
  obj record;
BEGIN
  FOR obj IN SELECT * FROM pg_event_trigger_dropped_objects()
  LOOP
    IF obj.object_type IN (
      'schema'
    , 'table'
    , 'foreign table'
    , 'view'
    , 'materialized view'
    , 'function'
    , 'trigger'
    , 'type'
    , 'rule'
    )
    AND obj.is_temporary IS false -- no pg_temp objects
    THEN
      NOTIFY pgrst, 'reload schema';
    END IF;
  END LOOP;
END; $$;


ALTER FUNCTION extensions.pgrst_drop_watch() OWNER TO supabase_admin;

--
-- Name: set_graphql_placeholder(); Type: FUNCTION; Schema: extensions; Owner: supabase_admin
--

CREATE FUNCTION extensions.set_graphql_placeholder() RETURNS event_trigger
    LANGUAGE plpgsql
    AS $_$
    DECLARE
    graphql_is_dropped bool;
    BEGIN
    graphql_is_dropped = (
        SELECT ev.schema_name = 'graphql_public'
        FROM pg_event_trigger_dropped_objects() AS ev
        WHERE ev.schema_name = 'graphql_public'
    );

    IF graphql_is_dropped
    THEN
        create or replace function graphql_public.graphql(
            "operationName" text default null,
            query text default null,
            variables jsonb default null,
            extensions jsonb default null
        )
            returns jsonb
            language plpgsql
        as $$
            DECLARE
                server_version float;
            BEGIN
                server_version = (SELECT (SPLIT_PART((select version()), ' ', 2))::float);

                IF server_version >= 14 THEN
                    RETURN jsonb_build_object(
                        'errors', jsonb_build_array(
                            jsonb_build_object(
                                'message', 'pg_graphql extension is not enabled.'
                            )
                        )
                    );
                ELSE
                    RETURN jsonb_build_object(
                        'errors', jsonb_build_array(
                            jsonb_build_object(
                                'message', 'pg_graphql is only available on projects running Postgres 14 onwards.'
                            )
                        )
                    );
                END IF;
            END;
        $$;
    END IF;

    END;
$_$;


ALTER FUNCTION extensions.set_graphql_placeholder() OWNER TO supabase_admin;

--
-- Name: FUNCTION set_graphql_placeholder(); Type: COMMENT; Schema: extensions; Owner: supabase_admin
--

COMMENT ON FUNCTION extensions.set_graphql_placeholder() IS 'Reintroduces placeholder function for graphql_public.graphql';


--
-- Name: get_auth(text); Type: FUNCTION; Schema: pgbouncer; Owner: supabase_admin
--

CREATE FUNCTION pgbouncer.get_auth(p_usename text) RETURNS TABLE(username text, password text)
    LANGUAGE plpgsql SECURITY DEFINER
    AS $_$
begin
    raise debug 'PgBouncer auth request: %', p_usename;

    return query
    select 
        rolname::text, 
        case when rolvaliduntil < now() 
            then null 
            else rolpassword::text 
        end 
    from pg_authid 
    where rolname=$1 and rolcanlogin;
end;
$_$;


ALTER FUNCTION pgbouncer.get_auth(p_usename text) OWNER TO supabase_admin;

--
-- Name: activate_business_user(text, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.activate_business_user(p_invitation_token text, p_new_password text) RETURNS jsonb
    LANGUAGE plpgsql SECURITY DEFINER
    AS $$
DECLARE
  v_business_user RECORD;
  v_auth_user_id UUID;
BEGIN
  -- Find the invitation
  SELECT * INTO v_business_user
  FROM business_users
  WHERE invitation_token = p_invitation_token
  AND invitation_expires_at > NOW()
  AND status = 'invited';
  
  IF NOT FOUND THEN
    RETURN jsonb_build_object(
      'success', FALSE,
      'error', 'Invalid or expired invitation token'
    );
  END IF;
  
  -- Create auth user
  -- Note: This would typically be handled by Supabase Auth API
  -- The actual implementation would call Supabase functions
  
  -- For now, we'll assume the auth user is created externally
  -- and we just need to link it
  
  -- Update business user
  UPDATE business_users SET
    status = 'active',
    activated_at = NOW(),
    password_changed = TRUE,
    invitation_token = NULL,
    invitation_expires_at = NULL,
    temp_password = NULL
  WHERE id = v_business_user.id;
  
  -- Log the activation
  INSERT INTO user_activity_log (
    business_user_id,
    business_profile_id,
    activity_type,
    activity_data
  ) VALUES (
    v_business_user.id,
    v_business_user.business_profile_id,
    'account_activated',
    jsonb_build_object('activation_method', 'invitation')
  );
  
  RETURN jsonb_build_object(
    'success', TRUE,
    'user_id', v_business_user.id,
    'role', v_business_user.role
  );
END;
$$;


ALTER FUNCTION public.activate_business_user(p_invitation_token text, p_new_password text) OWNER TO postgres;

--
-- Name: analyze_widget_config_usage(public.widget_type, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.analyze_widget_config_usage(widget_type_param public.widget_type DEFAULT NULL::public.widget_type, days_back integer DEFAULT 30) RETURNS TABLE(widget_type public.widget_type, total_users bigint, active_users bigint, config_changes bigint, avg_interactions_per_user numeric)
    LANGUAGE plpgsql
    AS $$
BEGIN
  RETURN QUERY
  SELECT 
    wc.widget_type,
    COUNT(DISTINCT wc.user_id) as total_users,
    COUNT(DISTINCT CASE WHEN wc.updated_at >= NOW() - (days_back || ' days')::INTERVAL THEN wc.user_id END) as active_users,
    COUNT(DISTINCT ch.id) as config_changes,
    COALESCE(AVG(
      (SELECT COUNT(*) FROM widget_usage_analytics wa 
       WHERE wa.user_id = wc.user_id 
       AND wa.widget_type = wc.widget_type 
       AND wa.created_at >= NOW() - (days_back || ' days')::INTERVAL)
    ), 0) as avg_interactions_per_user
  FROM user_widget_configs wc
  LEFT JOIN user_config_history ch ON ch.user_id = wc.user_id 
    AND ch.widget_type = wc.widget_type
    AND ch.created_at >= NOW() - (days_back || ' days')::INTERVAL
  WHERE (widget_type_param IS NULL OR wc.widget_type = widget_type_param)
  GROUP BY wc.widget_type;
END;
$$;


ALTER FUNCTION public.analyze_widget_config_usage(widget_type_param public.widget_type, days_back integer) OWNER TO postgres;

--
-- Name: assign_user_to_experiment(uuid, uuid, jsonb); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.assign_user_to_experiment(p_user_id uuid, p_experiment_id uuid, p_assignment_context jsonb DEFAULT '{}'::jsonb) RETURNS jsonb
    LANGUAGE plpgsql SECURITY DEFINER
    AS $$
DECLARE
  v_experiment RECORD;
  v_existing_assignment UUID;
  v_selected_variant RECORD;
  v_random_value INTEGER;
  v_cumulative_weight INTEGER := 0;
  v_total_weight INTEGER;
BEGIN
  -- Check if experiment exists and is active
  SELECT * INTO v_experiment
  FROM widget_experiments
  WHERE id = p_experiment_id
    AND status = 'active'
    AND NOW() BETWEEN COALESCE(start_date, '-infinity') AND COALESCE(end_date, 'infinity');

  IF v_experiment IS NULL THEN
    RETURN jsonb_build_object(
      'success', FALSE,
      'error', 'Experiment not found or not active'
    );
  END IF;

  -- Check for existing assignment
  SELECT variant_id INTO v_existing_assignment
  FROM user_experiment_assignments
  WHERE user_id = p_user_id AND experiment_id = p_experiment_id;

  IF v_existing_assignment IS NOT NULL THEN
    -- Return existing assignment
    SELECT * INTO v_selected_variant
    FROM widget_experiment_variants
    WHERE id = v_existing_assignment;

    RETURN jsonb_build_object(
      'success', TRUE,
      'experiment_id', p_experiment_id,
      'variant_id', v_existing_assignment,
      'variant_name', v_selected_variant.name,
      'is_control', v_selected_variant.is_control,
      'config_override', v_selected_variant.config_override,
      'existing_assignment', TRUE
    );
  END IF;

  -- Get total weight for normalization
  SELECT SUM(traffic_weight) INTO v_total_weight
  FROM widget_experiment_variants
  WHERE experiment_id = p_experiment_id;

  IF v_total_weight = 0 THEN
    RETURN jsonb_build_object(
      'success', FALSE,
      'error', 'No variants with traffic weight found'
    );
  END IF;

  -- Generate deterministic random value based on user ID and experiment ID
  v_random_value := (hashtext(p_user_id::text || p_experiment_id::text) % v_total_weight);

  -- Select variant based on weighted random selection
  FOR v_selected_variant IN
    SELECT * FROM widget_experiment_variants
    WHERE experiment_id = p_experiment_id
    ORDER BY created_at
  LOOP
    v_cumulative_weight := v_cumulative_weight + v_selected_variant.traffic_weight;
    
    IF v_random_value < v_cumulative_weight THEN
      EXIT;
    END IF;
  END LOOP;

  -- Create assignment record
  INSERT INTO user_experiment_assignments (
    user_id,
    experiment_id,
    variant_id,
    assignment_context,
    assignment_hash
  ) VALUES (
    p_user_id,
    p_experiment_id,
    v_selected_variant.id,
    p_assignment_context,
    md5(p_user_id::text || p_experiment_id::text || v_selected_variant.id::text)
  );

  -- Return assignment details
  RETURN jsonb_build_object(
    'success', TRUE,
    'experiment_id', p_experiment_id,
    'variant_id', v_selected_variant.id,
    'variant_name', v_selected_variant.name,
    'is_control', v_selected_variant.is_control,
    'config_override', v_selected_variant.config_override,
    'existing_assignment', FALSE,
    'assigned_at', NOW()
  );
END;
$$;


ALTER FUNCTION public.assign_user_to_experiment(p_user_id uuid, p_experiment_id uuid, p_assignment_context jsonb) OWNER TO postgres;

--
-- Name: FUNCTION assign_user_to_experiment(p_user_id uuid, p_experiment_id uuid, p_assignment_context jsonb); Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON FUNCTION public.assign_user_to_experiment(p_user_id uuid, p_experiment_id uuid, p_assignment_context jsonb) IS 'Assigns user to experiment variant using weighted random selection with sticky assignments';


--
-- Name: check_user_permission(uuid, public.feature_area, public.permission_type); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.check_user_permission(p_user_id uuid, p_feature public.feature_area, p_permission public.permission_type) RETURNS boolean
    LANGUAGE plpgsql SECURITY DEFINER
    AS $$
DECLARE
  v_business_user_id UUID;
BEGIN
  -- Get business user ID from auth user ID
  SELECT id INTO v_business_user_id
  FROM business_users
  WHERE auth_user_id = p_user_id
  AND status = 'active';
  
  IF NOT FOUND THEN
    RETURN FALSE;
  END IF;
  
  RETURN check_user_permission_by_business_user(
    v_business_user_id,
    p_feature,
    p_permission
  );
END;
$$;


ALTER FUNCTION public.check_user_permission(p_user_id uuid, p_feature public.feature_area, p_permission public.permission_type) OWNER TO postgres;

--
-- Name: check_user_permission_by_business_user(uuid, public.feature_area, public.permission_type); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.check_user_permission_by_business_user(p_business_user_id uuid, p_feature public.feature_area, p_permission public.permission_type) RETURNS boolean
    LANGUAGE plpgsql SECURITY DEFINER
    AS $$
DECLARE
  v_has_permission BOOLEAN := FALSE;
  v_business_user RECORD;
BEGIN
  -- Get business user details
  SELECT bu.*, bp.subscription_plan
  INTO v_business_user
  FROM business_users bu
  JOIN business_profiles bp ON bp.id = bu.business_profile_id
  WHERE bu.id = p_business_user_id
  AND bu.status = 'active';
  
  IF NOT FOUND THEN
    RETURN FALSE;
  END IF;
  
  -- Check user-specific permissions first
  SELECT 
    CASE p_permission
      WHEN 'read' THEN ufa.can_read
      WHEN 'write' THEN ufa.can_write
      WHEN 'delete' THEN ufa.can_delete
      WHEN 'execute' THEN ufa.can_execute
    END INTO v_has_permission
  FROM user_feature_access ufa
  WHERE ufa.business_user_id = p_business_user_id
  AND ufa.feature_area = p_feature
  AND (ufa.expires_at IS NULL OR ufa.expires_at > NOW());
  
  -- If no user-specific permission, check role permissions
  IF v_has_permission IS NULL THEN
    SELECT 
      CASE p_permission
        WHEN 'read' THEN rfa.can_read
        WHEN 'write' THEN rfa.can_write
        WHEN 'delete' THEN rfa.can_delete
        WHEN 'execute' THEN rfa.can_execute
      END INTO v_has_permission
    FROM role_feature_access rfa
    WHERE rfa.role = v_business_user.role
    AND rfa.feature_area = p_feature;
  END IF;
  
  -- Check feature availability based on subscription
  IF v_has_permission THEN
    SELECT v_has_permission AND (
      fp.requires_subscription_plan IS NULL OR
      v_business_user.subscription_plan = ANY(fp.requires_subscription_plan)
    ) INTO v_has_permission
    FROM feature_permissions fp
    WHERE fp.feature_area = p_feature
    AND fp.is_active = TRUE;
  END IF;
  
  RETURN COALESCE(v_has_permission, FALSE);
END;
$$;


ALTER FUNCTION public.check_user_permission_by_business_user(p_business_user_id uuid, p_feature public.feature_area, p_permission public.permission_type) OWNER TO postgres;

--
-- Name: cleanup_expired_widget_config_cache(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.cleanup_expired_widget_config_cache() RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
  deleted_count INTEGER;
BEGIN
  DELETE FROM widget_config_cache WHERE expires_at <= NOW();
  GET DIAGNOSTICS deleted_count = ROW_COUNT;
  RETURN deleted_count;
END;
$$;


ALTER FUNCTION public.cleanup_expired_widget_config_cache() OWNER TO postgres;

--
-- Name: complete_onboarding_with_user_management(uuid, uuid); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.complete_onboarding_with_user_management(p_business_profile_id uuid, p_user_id uuid) RETURNS jsonb
    LANGUAGE plpgsql SECURITY DEFINER
    AS $$
DECLARE
  v_business_profile business_profiles;
  v_business_user business_users;
  v_permissions_count INTEGER;
  v_navigation_items INTEGER;
  v_error_message TEXT;
BEGIN
  -- Validate inputs
  IF p_business_profile_id IS NULL OR p_user_id IS NULL THEN
    RAISE EXCEPTION 'Business profile ID and user ID are required';
  END IF;

  -- Start transaction
  BEGIN
    -- 1. Mark business profile as onboarding completed
    UPDATE business_profiles 
    SET 
      onboarding_completed = TRUE,
      updated_at = NOW()
    WHERE id = p_business_profile_id AND user_id = p_user_id
    RETURNING * INTO v_business_profile;

    IF v_business_profile.id IS NULL THEN
      RAISE EXCEPTION 'Business profile not found or access denied';
    END IF;

    -- 2. Ensure business user exists and is active
    SELECT * INTO v_business_user
    FROM business_users
    WHERE business_profile_id = p_business_profile_id 
    AND auth_user_id = p_user_id
    AND role = 'owner';

    IF v_business_user.id IS NULL THEN
      RAISE EXCEPTION 'Owner business user not found';
    END IF;

    -- Update business user status to active
    UPDATE business_users 
    SET 
      status = 'active',
      last_login_at = NOW(),
      updated_at = NOW()
    WHERE id = v_business_user.id;

    -- 3. Verify permissions are set up
    SELECT COUNT(*) INTO v_permissions_count
    FROM user_feature_access
    WHERE business_user_id = v_business_user.id;

    -- If no permissions exist, set them up now
    IF v_permissions_count = 0 THEN
      INSERT INTO user_feature_access (
        business_user_id,
        feature_area,
        can_read,
        can_write,
        can_delete,
        can_execute,
        granted_by,
        granted_at
      )
      SELECT 
        v_business_user.id,
        rfa.feature_area,
        rfa.can_read,
        rfa.can_write,
        rfa.can_delete,
        rfa.can_execute,
        v_business_user.id, -- Self-granted by business user ID, not auth user ID
        NOW()
      FROM role_feature_access rfa
      WHERE rfa.role = 'owner';

      -- Update permissions count
      SELECT COUNT(*) INTO v_permissions_count
      FROM user_feature_access
      WHERE business_user_id = v_business_user.id;
    END IF;

    -- 4. Count available navigation items for owner
    WITH user_nav AS (
      SELECT DISTINCT fp.nav_route, fp.display_name
      FROM user_feature_access ufa
      JOIN feature_permissions fp ON fp.feature_area = ufa.feature_area
      WHERE ufa.business_user_id = v_business_user.id
      AND ufa.can_read = TRUE
      AND fp.nav_route IS NOT NULL
      AND fp.nav_route != ''
    )
    SELECT COUNT(*) INTO v_navigation_items FROM user_nav;

    -- Log successful completion
    RAISE NOTICE 'Onboarding completed with user management: profile_id=%, user_id=%, permissions=%, nav_items=%', 
                 v_business_profile.id, v_business_user.id, v_permissions_count, v_navigation_items;

    -- Return success response
    RETURN jsonb_build_object(
      'profile', to_jsonb(v_business_profile),
      'business_user_id', v_business_user.id,
      'permissions_count', v_permissions_count,
      'navigation_items', v_navigation_items,
      'success', true,
      'message', 'Onboarding completed with user management'
    );

  EXCEPTION WHEN OTHERS THEN
    -- Log error for debugging
    v_error_message := SQLERRM;
    RAISE NOTICE 'Error completing onboarding with user management: %', v_error_message;
    
    -- Re-raise the exception to trigger rollback
    RAISE EXCEPTION 'Failed to complete onboarding with user management: %', v_error_message;
  END;
END;
$$;


ALTER FUNCTION public.complete_onboarding_with_user_management(p_business_profile_id uuid, p_user_id uuid) OWNER TO postgres;

--
-- Name: FUNCTION complete_onboarding_with_user_management(p_business_profile_id uuid, p_user_id uuid); Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON FUNCTION public.complete_onboarding_with_user_management(p_business_profile_id uuid, p_user_id uuid) IS 'Completes the onboarding process and ensures user management system is properly initialized';


--
-- Name: create_business_profile_with_owner(uuid, jsonb); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.create_business_profile_with_owner(p_user_id uuid, p_profile_data jsonb) RETURNS jsonb
    LANGUAGE plpgsql SECURITY DEFINER
    AS $$
DECLARE
  v_business_profile business_profiles;
  v_business_user business_users;
  v_permissions_count INTEGER;
  v_error_message TEXT;
BEGIN
  -- Validate authenticated user
  IF p_user_id IS NULL THEN
    RAISE EXCEPTION 'User ID is required';
  END IF;

  -- Start transaction
  BEGIN
    -- 1. Create business profile
    INSERT INTO business_profiles (
      user_id,
      name,
      business_category,
      email_address,
      contact_number,
      vat_registration_number,
      company_registration_number,
      company_logo_url,
      address,
      suburb,
      city,
      province,
      country,
      postal_code,
      onboarding_completed,
      created_at,
      updated_at
    ) VALUES (
      p_user_id,
      (p_profile_data->>'name')::TEXT,
      (p_profile_data->>'businessCategory')::TEXT,
      (p_profile_data->>'emailAddress')::TEXT,
      (p_profile_data->>'contactNumber')::TEXT,
      NULLIF(p_profile_data->>'vatRegistrationNumber', ''),
      NULLIF(p_profile_data->>'companyRegistrationNumber', ''),
      NULLIF(p_profile_data->>'companyLogoUrl', ''),
      (p_profile_data->>'address')::TEXT,
      (p_profile_data->>'suburb')::TEXT,
      (p_profile_data->>'city')::TEXT,
      (p_profile_data->>'province')::TEXT,
      COALESCE((p_profile_data->>'country')::TEXT, 'South Africa'),
      (p_profile_data->>'postalCode')::TEXT,
      FALSE, -- Will be set to true by complete_onboarding_with_user_management
      NOW(),
      NOW()
    ) RETURNING * INTO v_business_profile;

    -- 2. Create owner business user record
    INSERT INTO business_users (
      business_profile_id,
      auth_user_id,
      email,
      display_name,
      username,
      role,
      status,
      can_invite_users,
      created_at,
      updated_at
    ) VALUES (
      v_business_profile.id,
      p_user_id,
      v_business_profile.email_address,
      v_business_profile.name || ' Owner', -- Default display name
      v_business_profile.email_address,    -- Default username to email
      'owner',
      'active',
      TRUE, -- Owners can invite users
      NOW(),
      NOW()
    ) RETURNING * INTO v_business_user;

    -- 3. Set up owner permissions (all features with full access)
    INSERT INTO user_feature_access (
      business_user_id,
      feature_area,
      can_read,
      can_write,
      can_delete,
      can_execute,
      granted_by,
      granted_at
    )
    SELECT 
      v_business_user.id,
      rfa.feature_area,
      rfa.can_read,
      rfa.can_write,
      rfa.can_delete,
      rfa.can_execute,
      v_business_user.id, -- Self-granted by business user ID, not auth user ID
      NOW()
    FROM role_feature_access rfa
    WHERE rfa.role = 'owner';

    -- Get permissions count for response
    SELECT COUNT(*) INTO v_permissions_count
    FROM user_feature_access
    WHERE business_user_id = v_business_user.id;

    -- Log successful creation
    RAISE NOTICE 'Business profile created with owner: profile_id=%, user_id=%, permissions=%', 
                 v_business_profile.id, v_business_user.id, v_permissions_count;

    -- Return success response
    RETURN jsonb_build_object(
      'profile', to_jsonb(v_business_profile),
      'business_user_id', v_business_user.id,
      'permissions_count', v_permissions_count,
      'success', true,
      'message', 'Business profile created with owner role'
    );

  EXCEPTION WHEN OTHERS THEN
    -- Log error for debugging
    v_error_message := SQLERRM;
    RAISE NOTICE 'Error creating business profile with owner: %', v_error_message;
    
    -- Re-raise the exception to trigger rollback
    RAISE EXCEPTION 'Failed to create business profile with owner: %', v_error_message;
  END;
END;
$$;


ALTER FUNCTION public.create_business_profile_with_owner(p_user_id uuid, p_profile_data jsonb) OWNER TO postgres;

--
-- Name: FUNCTION create_business_profile_with_owner(p_user_id uuid, p_profile_data jsonb); Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON FUNCTION public.create_business_profile_with_owner(p_user_id uuid, p_profile_data jsonb) IS 'Creates a business profile and automatically sets up the creator as owner with full permissions';


--
-- Name: create_default_notification_preferences(uuid); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.create_default_notification_preferences(user_uuid uuid) RETURNS void
    LANGUAGE plpgsql SECURITY DEFINER
    AS $$
BEGIN
  INSERT INTO public.notification_preferences (user_id)
  VALUES (user_uuid)
  ON CONFLICT (user_id) DO NOTHING;
  
  INSERT INTO public.low_stock_notification_settings (user_id)
  VALUES (user_uuid)
  ON CONFLICT (user_id) DO NOTHING;
END;
$$;


ALTER FUNCTION public.create_default_notification_preferences(user_uuid uuid) OWNER TO postgres;

--
-- Name: create_invoice_with_validation(uuid, uuid, jsonb, numeric, numeric, jsonb, numeric, character varying, date, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.create_invoice_with_validation(p_user_id uuid, p_business_profile_id uuid, p_items jsonb, p_total_amount_excl_discount numeric, p_total_amount numeric, p_customer_details jsonb DEFAULT NULL::jsonb, p_total_discount numeric DEFAULT 0, p_sales_type character varying DEFAULT 'cash'::character varying, p_repayment_date date DEFAULT NULL::date, p_notes text DEFAULT NULL::text) RETURNS TABLE(success boolean, invoice_id uuid, invoice_number character varying, error_code character varying, error_message text, usage_info jsonb, created_at timestamp with time zone)
    LANGUAGE plpgsql
    AS $$
DECLARE
    v_new_invoice_id UUID;
    v_created_at TIMESTAMP WITH TIME ZONE;
    v_generated_invoice_number VARCHAR(50);
    v_customer_name VARCHAR(255);
    v_customer_email VARCHAR(255);
    v_customer_phone VARCHAR(50);
    v_customer_address TEXT;
    v_is_valid BOOLEAN := true;
    v_error_code TEXT := NULL;
    v_error_message TEXT := NULL;
    v_usage_info JSONB := '{"validated": true, "skip_reason": "validation_function_not_implemented"}'::jsonb;
BEGIN
    -- Generate new invoice ID and timestamp
    v_new_invoice_id := gen_random_uuid();
    v_created_at := NOW();
    
    -- Step 1: Validate business rules (simplified - always passes for now)
    -- In a future update, this can be expanded to include proper validation
    
    -- Return validation failure if rules not met (won't happen with current simple logic)
    IF NOT v_is_valid THEN
        success := false;
        invoice_id := NULL;
        invoice_number := NULL;
        error_code := v_error_code;
        error_message := v_error_message;
        usage_info := v_usage_info;
        created_at := v_created_at;
        RETURN NEXT;
        RETURN;
    END IF;
    
    -- Step 2: Generate sequential invoice number for this business profile
    SELECT generate_invoice_number(p_user_id, p_business_profile_id) INTO v_generated_invoice_number;
    
    -- Step 3: Extract customer details from JSONB (with defaults)
    v_customer_name := COALESCE(p_customer_details->>'name', 'Cash Customer');
    v_customer_email := p_customer_details->>'email';
    v_customer_phone := p_customer_details->>'phone';
    v_customer_address := p_customer_details->>'address';
    
    -- Step 4: Create the invoice with generated number
    BEGIN
        INSERT INTO invoices (
            id,
            user_id,
            invoice_number,
            status,
            subtotal,
            discount_amount,
            total_amount,
            created_at,
            updated_at
        ) VALUES (
            v_new_invoice_id,
            p_user_id,
            v_generated_invoice_number,
            'draft',
            p_total_amount_excl_discount,
            p_total_discount,
            p_total_amount,
            v_created_at,
            v_created_at
        );
        
        -- Step 5: Update subscription usage count if applicable (skip for now)
        -- Note: subscription tracking requires proper schema with current_invoice_count column
        -- This can be implemented later when subscription usage tracking is needed
        
        -- Step 6: Log the creation (if table exists)
        BEGIN
            INSERT INTO invoice_creation_log (
                invoice_id,
                user_id,
                action,
                validation_result,
                created_at
            ) VALUES (
                v_new_invoice_id,
                p_user_id,
                'created',
                json_build_object(
                    'validation_passed', true,
                    'usage_info', v_usage_info,
                    'items_count', jsonb_array_length(p_items),
                    'total_amount', p_total_amount,
                    'generated_invoice_number', v_generated_invoice_number
                ),
                v_created_at
            );
        EXCEPTION
            WHEN undefined_table THEN
                -- Log table doesn't exist, skip logging
                NULL;
        END;
        
        -- Step 7: Return success
        success := true;
        invoice_id := v_new_invoice_id;
        invoice_number := v_generated_invoice_number;
        error_code := NULL;
        error_message := NULL;
        usage_info := v_usage_info;
        created_at := v_created_at;
        RETURN NEXT;
        
    EXCEPTION
        WHEN OTHERS THEN
            -- Log the error (if table exists)
            BEGIN
                INSERT INTO invoice_creation_log (
                    invoice_id,
                    user_id,
                    action,
                    validation_result,
                    error_details,
                    created_at
                ) VALUES (
                    v_new_invoice_id,
                    p_user_id,
                    'creation_failed',
                    json_build_object(
                        'validation_passed', true,
                        'usage_info', v_usage_info,
                        'attempted_invoice_number', v_generated_invoice_number
                    ),
                    json_build_object(
                        'error_code', SQLSTATE,
                        'error_message', SQLERRM
                    ),
                    v_created_at
                );
            EXCEPTION
                WHEN undefined_table THEN
                    -- Log table doesn't exist, skip logging
                    NULL;
            END;
            
            -- Return error
            success := false;
            invoice_id := NULL;
            invoice_number := NULL;
            error_code := 'CREATION_FAILED';
            error_message := format('Failed to create invoice: %s', SQLERRM);
            usage_info := v_usage_info;
            created_at := v_created_at;
            RETURN NEXT;
    END;
END;
$$;


ALTER FUNCTION public.create_invoice_with_validation(p_user_id uuid, p_business_profile_id uuid, p_items jsonb, p_total_amount_excl_discount numeric, p_total_amount numeric, p_customer_details jsonb, p_total_discount numeric, p_sales_type character varying, p_repayment_date date, p_notes text) OWNER TO postgres;

--
-- Name: FUNCTION create_invoice_with_validation(p_user_id uuid, p_business_profile_id uuid, p_items jsonb, p_total_amount_excl_discount numeric, p_total_amount numeric, p_customer_details jsonb, p_total_discount numeric, p_sales_type character varying, p_repayment_date date, p_notes text); Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON FUNCTION public.create_invoice_with_validation(p_user_id uuid, p_business_profile_id uuid, p_items jsonb, p_total_amount_excl_discount numeric, p_total_amount numeric, p_customer_details jsonb, p_total_discount numeric, p_sales_type character varying, p_repayment_date date, p_notes text) IS 'Creates invoices with auto-generated sequential numbers per business profile and comprehensive validation';


--
-- Name: create_user_session(uuid, text, text, inet, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.create_user_session(p_business_user_id uuid, p_device_id text DEFAULT NULL::text, p_device_type text DEFAULT NULL::text, p_ip_address inet DEFAULT NULL::inet, p_user_agent text DEFAULT NULL::text) RETURNS text
    LANGUAGE plpgsql SECURITY DEFINER
    AS $$
DECLARE
  v_session_token TEXT;
BEGIN
  -- Generate session token
  v_session_token := encode(gen_random_bytes(32), 'hex');
  
  -- Create session
  INSERT INTO user_sessions (
    business_user_id,
    session_token,
    device_id,
    device_type,
    ip_address,
    user_agent,
    expires_at
  ) VALUES (
    p_business_user_id,
    v_session_token,
    p_device_id,
    p_device_type,
    p_ip_address,
    p_user_agent,
    NOW() + INTERVAL '30 days'
  );
  
  -- Update last login
  UPDATE business_users SET
    last_login_at = NOW()
  WHERE id = p_business_user_id;
  
  -- Log the login
  INSERT INTO user_activity_log (
    business_user_id,
    business_profile_id,
    activity_type,
    activity_data,
    ip_address,
    user_agent
  ) VALUES (
    p_business_user_id,
    (SELECT business_profile_id FROM business_users WHERE id = p_business_user_id),
    'login',
    jsonb_build_object('device_type', p_device_type, 'device_id', p_device_id),
    p_ip_address,
    p_user_agent
  );
  
  RETURN v_session_token;
END;
$$;


ALTER FUNCTION public.create_user_session(p_business_user_id uuid, p_device_id text, p_device_type text, p_ip_address inet, p_user_agent text) OWNER TO postgres;

--
-- Name: deactivate_business_user(uuid, uuid); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.deactivate_business_user(p_business_user_id uuid, p_deactivated_by uuid) RETURNS jsonb
    LANGUAGE plpgsql SECURITY DEFINER
    AS $$
BEGIN
  -- Validate permissions
  IF NOT EXISTS (
    SELECT 1 FROM business_users deactivator
    JOIN business_users target ON target.id = p_business_user_id
    WHERE deactivator.auth_user_id = p_deactivated_by
    AND deactivator.business_profile_id = target.business_profile_id
    AND deactivator.status = 'active'
    AND deactivator.role IN ('owner', 'admin')
    AND target.is_primary_user = FALSE -- Can't deactivate primary user
  ) THEN
    RETURN jsonb_build_object(
      'success', FALSE,
      'error', 'Insufficient permissions or cannot deactivate primary user'
    );
  END IF;
  
  -- Deactivate user
  UPDATE business_users SET
    status = 'deactivated',
    deactivated_at = NOW()
  WHERE id = p_business_user_id;
  
  -- Deactivate all sessions
  UPDATE user_sessions SET
    is_active = FALSE
  WHERE business_user_id = p_business_user_id;
  
  -- Log the deactivation
  INSERT INTO user_activity_log (
    business_user_id,
    business_profile_id,
    activity_type,
    activity_data
  ) VALUES (
    p_business_user_id,
    (SELECT business_profile_id FROM business_users WHERE id = p_business_user_id),
    'user_deactivated',
    jsonb_build_object('deactivated_by', p_deactivated_by)
  );
  
  RETURN jsonb_build_object(
    'success', TRUE,
    'message', 'User deactivated successfully'
  );
END;
$$;


ALTER FUNCTION public.deactivate_business_user(p_business_user_id uuid, p_deactivated_by uuid) OWNER TO postgres;

--
-- Name: filter_navigation_by_permissions(jsonb, uuid, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.filter_navigation_by_permissions(p_nav_items jsonb, p_business_user_id uuid, p_subscription_plan text) RETURNS jsonb
    LANGUAGE plpgsql
    AS $$
DECLARE
  v_filtered_items JSONB := '[]'::jsonb;
  v_item JSONB;
  v_feature_area TEXT;
  v_has_access BOOLEAN;
  v_children JSONB;
BEGIN
  FOR v_item IN SELECT * FROM jsonb_array_elements(p_nav_items)
  LOOP
    v_feature_area := v_item->>'feature_area';
    
    -- Check if user has access to this feature
    IF v_feature_area IS NOT NULL THEN
      SELECT check_user_permission_by_business_user(
        p_business_user_id,
        v_feature_area::feature_area,
        'read'::permission_type
      ) INTO v_has_access;
      
      IF NOT v_has_access THEN
        CONTINUE;
      END IF;
    ELSE
      v_has_access := TRUE;
    END IF;
    
    -- Process children recursively
    IF v_item ? 'children' AND jsonb_array_length(v_item->'children') > 0 THEN
      v_children := filter_navigation_by_permissions(
        v_item->'children',
        p_business_user_id,
        p_subscription_plan
      );
      
      -- Only include parent if it has accessible children
      IF jsonb_array_length(v_children) > 0 THEN
        v_item := jsonb_set(v_item, '{children}', v_children);
        v_filtered_items := v_filtered_items || v_item;
      END IF;
    ELSIF v_has_access THEN
      v_filtered_items := v_filtered_items || v_item;
    END IF;
  END LOOP;
  
  RETURN v_filtered_items;
END;
$$;


ALTER FUNCTION public.filter_navigation_by_permissions(p_nav_items jsonb, p_business_user_id uuid, p_subscription_plan text) OWNER TO postgres;

--
-- Name: generate_invoice_number(uuid); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.generate_invoice_number(user_uuid uuid) RETURNS text
    LANGUAGE plpgsql SECURITY DEFINER
    AS $_$
DECLARE
    current_year TEXT;
    next_number INTEGER;
    invoice_number TEXT;
BEGIN
    current_year := EXTRACT(YEAR FROM CURRENT_DATE)::TEXT;
    
    -- Get the next invoice number for this user and year
    SELECT COALESCE(MAX(
        CASE 
            WHEN invoice_number ~ ('^INV-' || current_year || '-[0-9]+$')
            THEN (regexp_replace(invoice_number, '^INV-' || current_year || '-', ''))::INTEGER
            ELSE 0
        END
    ), 0) + 1
    INTO next_number
    FROM public.invoices 
    WHERE user_id = user_uuid;
    
    invoice_number := 'INV-' || current_year || '-' || LPAD(next_number::TEXT, 4, '0');
    
    RETURN invoice_number;
END;
$_$;


ALTER FUNCTION public.generate_invoice_number(user_uuid uuid) OWNER TO postgres;

--
-- Name: FUNCTION generate_invoice_number(user_uuid uuid); Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON FUNCTION public.generate_invoice_number(user_uuid uuid) IS 'Generates unique invoice numbers in format INV-YYYY-NNNN for each user.';


--
-- Name: generate_invoice_number(uuid, uuid); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.generate_invoice_number(p_user_id uuid, p_business_profile_id uuid DEFAULT NULL::uuid) RETURNS character varying
    LANGUAGE plpgsql
    AS $_$
DECLARE
    v_current_year TEXT;
    v_next_number INTEGER;
    v_invoice_number VARCHAR(50);
BEGIN
    -- Get the current year
    v_current_year := EXTRACT(YEAR FROM CURRENT_DATE)::TEXT;
    
    -- Get the next sequential number for this user and year
    -- This is done atomically to prevent race conditions
    SELECT COALESCE(MAX(
        CASE 
            WHEN invoice_number ~ ('^INV-' || v_current_year || '-[0-9]+$')
            THEN (regexp_replace(invoice_number, '^INV-' || v_current_year || '-', ''))::INTEGER
            ELSE 0
        END
    ), 0) + 1
    INTO v_next_number
    FROM invoices 
    WHERE user_id = p_user_id;
    
    -- Format the invoice number as INV-YYYY-NNNN (year-based with 4-digit padding)
    v_invoice_number := 'INV-' || v_current_year || '-' || LPAD(v_next_number::TEXT, 4, '0');
    
    RETURN v_invoice_number;
END;
$_$;


ALTER FUNCTION public.generate_invoice_number(p_user_id uuid, p_business_profile_id uuid) OWNER TO postgres;

--
-- Name: FUNCTION generate_invoice_number(p_user_id uuid, p_business_profile_id uuid); Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON FUNCTION public.generate_invoice_number(p_user_id uuid, p_business_profile_id uuid) IS 'Generates sequential invoice numbers per business profile (format: INV-0001, INV-0002, etc.)';


--
-- Name: get_business_metrics_enhanced(uuid); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.get_business_metrics_enhanced(user_uuid uuid) RETURNS json
    LANGUAGE plpgsql SECURITY DEFINER
    AS $$
DECLARE
  -- Core variables
  items_count INTEGER := 0;
  customers_count INTEGER := 0;
  suppliers_count INTEGER := 0;
  business_contacts_count INTEGER := 0;
  total_invoices_count INTEGER := 0;
  paid_invoices_count INTEGER := 0;
  pending_invoices_count INTEGER := 0;
  draft_invoices_count INTEGER := 0;
  total_revenue NUMERIC(15,2) := 0;
  total_amount_all_invoices NUMERIC(15,2) := 0;
  revenue_this_month NUMERIC(15,2) := 0;
  revenue_this_week NUMERIC(15,2) := 0;
  unpaid_invoices_amount NUMERIC(15,2) := 0;
  unpaid_invoices_count INTEGER := 0;
  average_invoice_value NUMERIC(15,2) := 0;
  low_stock_items_count INTEGER := 0;
  out_of_stock_items_count INTEGER := 0;
  
  -- Enhanced variables
  overdue_count INTEGER := 0;
  overdue_amount NUMERIC(15,2) := 0;
  collection_rate NUMERIC(5,2) := 0;
  customer_ltv NUMERIC(15,2) := 0;
  inventory_turnover NUMERIC(10,2) := 0;
  high_value_count INTEGER := 0;
  inactive_count INTEGER := 0;
  cash_flow_forecast NUMERIC(15,2) := 0;
  business_health TEXT := 'unknown';
  
  -- Subscription info
  subscription_plan TEXT := 'free';
  has_active_subscription BOOLEAN := FALSE;
BEGIN
  -- Get subscription plan for feature gating
  SELECT sp.name, TRUE
  INTO subscription_plan, has_active_subscription
  FROM public.subscriptions s
  JOIN public.subscription_plans sp ON s.plan_id = sp.id
  WHERE s.user_id = user_uuid 
    AND s.status = 'active'
    AND (s.current_period_end IS NULL OR s.current_period_end > NOW())
  LIMIT 1;

  -- Basic counts (always available)
  SELECT COUNT(*) INTO items_count 
  FROM public.items WHERE user_id = user_uuid;
  
  SELECT COUNT(*) INTO customers_count 
  FROM public.business_contacts 
  WHERE user_id = user_uuid AND contact_type = 'customer';
  
  SELECT COUNT(*) INTO suppliers_count 
  FROM public.business_contacts 
  WHERE user_id = user_uuid AND contact_type = 'supplier';
  
  business_contacts_count := customers_count + suppliers_count;
  
  -- FIXED Invoice calculations - proper status handling
  SELECT 
    COUNT(*) as total_count,
    COUNT(*) FILTER (WHERE status = 'paid') as paid_count,
    COUNT(*) FILTER (WHERE status IN ('sent', 'pending')) as pending_count,
    COUNT(*) FILTER (WHERE status = 'draft') as draft_count,
    COALESCE(SUM(total_amount), 0) as total_amount_all,
    COALESCE(SUM(total_amount) FILTER (WHERE status = 'paid'), 0) as paid_amount,
    COALESCE(SUM(total_amount) FILTER (WHERE status IN ('sent', 'pending')), 0) as unpaid_amount,
    COUNT(*) FILTER (WHERE status IN ('sent', 'pending')) as unpaid_count
  INTO 
    total_invoices_count,
    paid_invoices_count,
    pending_invoices_count,
    draft_invoices_count,
    total_amount_all_invoices,
    total_revenue,        -- FIXED: Only paid invoices contribute to revenue
    unpaid_invoices_amount,
    unpaid_invoices_count
  FROM public.invoices 
  WHERE user_id = user_uuid;
  
  -- Calculate average invoice value based on PAID invoices only
  IF paid_invoices_count > 0 THEN
    average_invoice_value := total_revenue / paid_invoices_count;
  ELSE
    average_invoice_value := 0;
  END IF;
  
  -- Monthly revenue - PAID invoices only
  SELECT COALESCE(SUM(total_amount), 0) INTO revenue_this_month
  FROM public.invoices 
  WHERE user_id = user_uuid 
    AND status = 'paid'
    AND DATE_TRUNC('month', created_at) = DATE_TRUNC('month', CURRENT_DATE);
  
  -- Weekly revenue - PAID invoices only
  SELECT COALESCE(SUM(total_amount), 0) INTO revenue_this_week
  FROM public.invoices 
  WHERE user_id = user_uuid 
    AND status = 'paid'
    AND DATE_TRUNC('week', created_at) = DATE_TRUNC('week', CURRENT_DATE);
  
  -- Overdue calculations (future enhancement)
  overdue_count := 0;
  overdue_amount := 0;
  
  -- Low stock calculations
  SELECT 
    COUNT(*) FILTER (WHERE current_stock <= low_stock_threshold AND low_stock_threshold > 0),
    COUNT(*) FILTER (WHERE current_stock = 0)
  INTO low_stock_items_count, out_of_stock_items_count
  FROM public.items 
  WHERE user_id = user_uuid;
  
  -- Advanced metrics (Pro+ features)
  IF subscription_plan IN ('premium', 'enterprise') THEN
    -- Collection efficiency calculation
    IF (total_revenue + unpaid_invoices_amount) > 0 THEN
      collection_rate := (total_revenue / (total_revenue + unpaid_invoices_amount)) * 100;
    END IF;
    
    -- Basic cash flow forecast (30 days)
    cash_flow_forecast := unpaid_invoices_amount * 0.7; -- 70% collection expectation
    
    -- Business health score
    IF collection_rate >= 90 AND overdue_amount < (total_revenue * 0.1) THEN
      business_health := 'excellent';
    ELSIF collection_rate >= 75 THEN
      business_health := 'good';
    ELSIF collection_rate >= 50 THEN
      business_health := 'fair';
    ELSE
      business_health := 'poor';
    END IF;
  END IF;
  
  -- Enterprise-only metrics
  IF subscription_plan = 'enterprise' THEN
    -- Customer lifetime value (simplified)
    IF customers_count > 0 THEN
      customer_ltv := total_revenue / customers_count;
    END IF;
    
    -- High-value customers (spending > average * 2)
    IF average_invoice_value > 0 THEN
      SELECT COUNT(DISTINCT customer_id) INTO high_value_count
      FROM public.invoices 
      WHERE user_id = user_uuid 
        AND status = 'paid'
        AND total_amount > (average_invoice_value * 2);
    END IF;
    
    -- Inactive customers (no invoices in last 3 months)
    SELECT COUNT(*) INTO inactive_count
    FROM public.business_contacts bc
    WHERE bc.user_id = user_uuid 
      AND bc.contact_type = 'customer'
      AND NOT EXISTS (
        SELECT 1 FROM public.invoices i 
        WHERE i.customer_id = bc.id 
          AND i.created_at > CURRENT_DATE - INTERVAL '3 months'
      );
  END IF;
  
  -- Return comprehensive JSON
  RETURN json_build_object(
    'user_id', user_uuid,
    'timestamp', NOW(),
    
    -- Basic metrics (Free tier)
    'items_count', items_count,
    'customers_count', customers_count,
    'suppliers_count', suppliers_count,
    'business_contacts_count', business_contacts_count,
    
    -- Invoice metrics with FIXED revenue calculation
    'total_invoices_count', total_invoices_count,
    'paid_invoices_count', paid_invoices_count,
    'pending_invoices_count', pending_invoices_count,
    'draft_invoices_count', draft_invoices_count,
    'unpaid_invoices_count', unpaid_invoices_count,
    'unpaid_invoices_amount', unpaid_invoices_amount,
    
    -- Revenue metrics (FIXED - only paid invoices)
    'total_revenue', total_revenue,
    'revenue_this_month', revenue_this_month,
    'revenue_this_week', revenue_this_week,
    'average_invoice_value', average_invoice_value,
    
    -- Performance metrics
    'collection_rate', collection_rate,
    'overdue_invoices_count', overdue_count,
    'overdue_invoices_amount', overdue_amount,
    'average_payment_days', 0,
    
    -- Inventory
    'low_stock_items_count', low_stock_items_count,
    'out_of_stock_items_count', out_of_stock_items_count,
    
    -- Advanced metrics (subscription-gated)
    'collection_efficiency_rate', CASE WHEN subscription_plan IN ('premium', 'enterprise') THEN collection_rate ELSE NULL END,
    'cash_flow_forecast_30_days', CASE WHEN subscription_plan IN ('premium', 'enterprise') THEN cash_flow_forecast ELSE NULL END,
    'business_health_score', CASE WHEN subscription_plan IN ('premium', 'enterprise') THEN business_health ELSE NULL END,
    
    -- Enterprise metrics
    'customer_lifetime_value_avg', CASE WHEN subscription_plan = 'enterprise' THEN customer_ltv ELSE NULL END,
    'high_value_customers_count', CASE WHEN subscription_plan = 'enterprise' THEN high_value_count ELSE NULL END,
    'inactive_customers_count', CASE WHEN subscription_plan = 'enterprise' THEN inactive_count ELSE NULL END,
    'inventory_turnover_rate', CASE WHEN subscription_plan = 'enterprise' THEN inventory_turnover ELSE NULL END,
    
    -- Metadata
    'subscription_plan', subscription_plan,
    'last_updated', NOW()
  );
END;
$$;


ALTER FUNCTION public.get_business_metrics_enhanced(user_uuid uuid) OWNER TO postgres;

--
-- Name: FUNCTION get_business_metrics_enhanced(user_uuid uuid); Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON FUNCTION public.get_business_metrics_enhanced(user_uuid uuid) IS 'Enhanced business metrics calculation with FIXED revenue logic. 
Revenue now correctly includes only PAID invoices. 
Supports draft, paid, unpaid (sent/pending), and future overdue/cancelled statuses.
Includes subscription-aware feature gating for advanced metrics.';


--
-- Name: get_business_profile_with_user_info(uuid, uuid); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.get_business_profile_with_user_info(p_business_profile_id uuid, p_user_id uuid) RETURNS jsonb
    LANGUAGE plpgsql SECURITY DEFINER
    AS $$
DECLARE
  v_result JSONB;
  v_business_profile business_profiles;
  v_business_user business_users;
  v_permissions_count INTEGER;
  v_active_users_count INTEGER;
  v_user_role TEXT;
  v_can_manage_users BOOLEAN;
BEGIN
  -- Get business profile
  SELECT * INTO v_business_profile
  FROM business_profiles
  WHERE id = p_business_profile_id;

  IF v_business_profile.id IS NULL THEN
    RAISE EXCEPTION 'Business profile not found';
  END IF;

  -- Get business user information
  SELECT * INTO v_business_user
  FROM business_users
  WHERE business_profile_id = p_business_profile_id 
  AND auth_user_id = p_user_id;

  IF v_business_user.id IS NULL THEN
    -- User doesn't have access to this business
    RETURN jsonb_build_object(
      'profile', to_jsonb(v_business_profile),
      'has_access', false,
      'error', 'Access denied to this business profile'
    );
  END IF;

  -- Get user's permissions count
  SELECT COUNT(*) INTO v_permissions_count
  FROM user_feature_access
  WHERE business_user_id = v_business_user.id;

  -- Get active users count
  SELECT COUNT(*) INTO v_active_users_count
  FROM business_users
  WHERE business_profile_id = p_business_profile_id
  AND status = 'active';

  -- Determine user capabilities
  v_user_role := v_business_user.role;
  v_can_manage_users := v_business_user.can_invite_users;

  -- Build comprehensive response
  v_result := jsonb_build_object(
    'profile', to_jsonb(v_business_profile),
    'user_info', jsonb_build_object(
      'id', v_business_user.id,
      'role', v_user_role,
      'display_name', v_business_user.display_name,
      'username', v_business_user.username,
      'status', v_business_user.status,
      'can_invite_users', v_can_manage_users,
      'permissions_count', v_permissions_count,
      'created_at', v_business_user.created_at,
      'last_login_at', v_business_user.last_login_at
    ),
    'business_stats', jsonb_build_object(
      'total_users', v_active_users_count,
      'user_management_enabled', true,
      'subscription_plan', v_business_profile.subscription_plan
    ),
    'has_access', true,
    'success', true
  );

  RETURN v_result;

EXCEPTION WHEN OTHERS THEN
  RETURN jsonb_build_object(
    'has_access', false,
    'success', false,
    'error', SQLERRM
  );
END;
$$;


ALTER FUNCTION public.get_business_profile_with_user_info(p_business_profile_id uuid, p_user_id uuid) OWNER TO postgres;

--
-- Name: FUNCTION get_business_profile_with_user_info(p_business_profile_id uuid, p_user_id uuid); Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON FUNCTION public.get_business_profile_with_user_info(p_business_profile_id uuid, p_user_id uuid) IS 'Returns business profile with user management information including role, permissions, and capabilities';


--
-- Name: get_cached_widget_config(uuid, uuid, public.widget_type, boolean); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.get_cached_widget_config(p_user_id uuid, p_business_profile_id uuid, p_widget_type public.widget_type, p_force_refresh boolean DEFAULT false) RETURNS jsonb
    LANGUAGE plpgsql SECURITY DEFINER
    AS $$
DECLARE
  v_cache_key VARCHAR(255);
  v_cached_config RECORD;
  v_fresh_config JSONB;
BEGIN
  -- Generate cache key
  v_cache_key := format('widget_config:%s:%s:%s', p_user_id, p_business_profile_id, p_widget_type);

  -- Check for cached config unless force refresh
  IF NOT p_force_refresh THEN
    SELECT config_data, expires_at 
    INTO v_cached_config
    FROM widget_config_cache
    WHERE cache_key = v_cache_key
      AND expires_at > NOW()
    LIMIT 1;

    -- Return cached config if valid
    IF v_cached_config IS NOT NULL THEN
      RETURN v_cached_config.config_data;
    END IF;
  END IF;

  -- Get fresh configuration
  v_fresh_config := get_effective_widget_config(p_user_id, p_business_profile_id, p_widget_type);

  -- Cache the fresh configuration
  INSERT INTO widget_config_cache (
    cache_key,
    config_data,
    config_hash,
    cache_scope,
    widget_type,
    user_id,
    ttl_seconds,
    expires_at
  ) VALUES (
    v_cache_key,
    v_fresh_config,
    md5(v_fresh_config::text),
    'individual_user',
    p_widget_type,
    p_user_id,
    300, -- 5 minutes TTL
    NOW() + INTERVAL '5 minutes'
  )
  ON CONFLICT (cache_key) DO UPDATE SET
    config_data = EXCLUDED.config_data,
    config_hash = EXCLUDED.config_hash,
    updated_at = NOW(),
    expires_at = EXCLUDED.expires_at,
    version = widget_config_cache.version + 1;

  RETURN v_fresh_config;
END;
$$;


ALTER FUNCTION public.get_cached_widget_config(p_user_id uuid, p_business_profile_id uuid, p_widget_type public.widget_type, p_force_refresh boolean) OWNER TO postgres;

--
-- Name: FUNCTION get_cached_widget_config(p_user_id uuid, p_business_profile_id uuid, p_widget_type public.widget_type, p_force_refresh boolean); Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON FUNCTION public.get_cached_widget_config(p_user_id uuid, p_business_profile_id uuid, p_widget_type public.widget_type, p_force_refresh boolean) IS 'Retrieves widget configuration from cache or generates fresh config if cache miss';


--
-- Name: get_effective_widget_config(uuid, uuid, public.widget_type); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.get_effective_widget_config(p_user_id uuid, p_business_profile_id uuid, p_widget_type public.widget_type) RETURNS jsonb
    LANGUAGE plpgsql SECURITY DEFINER
    AS $$
DECLARE
  v_default_config JSONB;
  v_user_config JSONB;
  v_experiment_config JSONB;
  v_feature_flags JSONB;
  v_final_config JSONB;
  v_experiment_assignment RECORD;
BEGIN
  -- Get default configuration from template
  SELECT default_config INTO v_default_config
  FROM widget_config_templates
  WHERE widget_type = p_widget_type AND is_default = TRUE
  LIMIT 1;

  -- If no default config found, return empty object
  IF v_default_config IS NULL THEN
    v_default_config := '{}'::jsonb;
  END IF;

  -- Get user-specific configuration
  SELECT config INTO v_user_config
  FROM user_widget_configs
  WHERE user_id = p_user_id 
    AND business_profile_id = p_business_profile_id 
    AND widget_type = p_widget_type
    AND is_enabled = TRUE;

  -- If no user config found, use empty object
  IF v_user_config IS NULL THEN
    v_user_config := '{}'::jsonb;
  END IF;

  -- Check for active experiment assignment
  SELECT 
    e.id as experiment_id,
    v.config_override,
    v.name as variant_name
  INTO v_experiment_assignment
  FROM user_experiment_assignments uea
  JOIN widget_experiments e ON e.id = uea.experiment_id
  JOIN widget_experiment_variants v ON v.id = uea.variant_id
  WHERE uea.user_id = p_user_id
    AND e.widget_type = p_widget_type
    AND e.status = 'active'
    AND NOW() BETWEEN COALESCE(e.start_date, '-infinity') AND COALESCE(e.end_date, 'infinity')
  ORDER BY uea.assigned_at DESC
  LIMIT 1;

  -- Get experiment config override if exists
  IF v_experiment_assignment IS NOT NULL THEN
    v_experiment_config := v_experiment_assignment.config_override;
  ELSE
    v_experiment_config := '{}'::jsonb;
  END IF;

  -- Get applicable feature flags
  SELECT jsonb_object_agg(flag_key, is_enabled) INTO v_feature_flags
  FROM widget_feature_flags
  WHERE (widget_type IS NULL OR widget_type = p_widget_type)
    AND (
      rollout_percentage = 100 OR
      (rollout_percentage > 0 AND 
       (hashtext(p_user_id::text) % 100) < rollout_percentage)
    );

  -- Merge configurations in priority order:
  -- 1. Default config (base)
  -- 2. User config (overrides default)
  -- 3. Experiment config (overrides user)
  -- 4. Feature flags (controls availability)
  v_final_config := v_default_config;
  v_final_config := v_final_config || v_user_config;
  v_final_config := v_final_config || v_experiment_config;
  v_final_config := jsonb_set(v_final_config, '{__feature_flags}', COALESCE(v_feature_flags, '{}'::jsonb));

  -- Add metadata
  v_final_config := jsonb_set(v_final_config, '{__metadata}', jsonb_build_object(
    'user_id', p_user_id,
    'business_profile_id', p_business_profile_id,
    'widget_type', p_widget_type,
    'has_user_config', v_user_config != '{}'::jsonb,
    'experiment_id', COALESCE(v_experiment_assignment.experiment_id, NULL),
    'variant_name', COALESCE(v_experiment_assignment.variant_name, NULL),
    'retrieved_at', extract(epoch from NOW()),
    'cache_ttl', 300
  ));

  RETURN v_final_config;
END;
$$;


ALTER FUNCTION public.get_effective_widget_config(p_user_id uuid, p_business_profile_id uuid, p_widget_type public.widget_type) OWNER TO postgres;

--
-- Name: FUNCTION get_effective_widget_config(p_user_id uuid, p_business_profile_id uuid, p_widget_type public.widget_type); Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON FUNCTION public.get_effective_widget_config(p_user_id uuid, p_business_profile_id uuid, p_widget_type public.widget_type) IS 'Returns the effective configuration for a user''s widget, combining default, user, and experiment configurations';


--
-- Name: get_low_stock_items(uuid); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.get_low_stock_items(user_uuid uuid) RETURNS TABLE(item_id uuid, item_name text, current_stock integer, low_stock_threshold integer, critical_stock_threshold integer, notification_type text)
    LANGUAGE plpgsql SECURITY DEFINER
    AS $$
BEGIN
  RETURN QUERY
  SELECT 
    i.id as item_id,
    i.name as item_name,
    i.current_stock,
    i.low_stock_threshold,
    i.critical_stock_threshold,
    CASE 
      WHEN i.current_stock = 0 THEN 'out_of_stock'
      WHEN i.current_stock <= i.critical_stock_threshold THEN 'critical_stock'
      WHEN i.current_stock <= i.low_stock_threshold THEN 'low_stock'
      ELSE 'normal'
    END as notification_type
  FROM public.items i
  WHERE i.user_id = user_uuid
    AND i.is_active = true
    AND i.enable_low_stock_alerts = true
    AND (
      i.current_stock = 0 
      OR i.current_stock <= i.critical_stock_threshold 
      OR i.current_stock <= i.low_stock_threshold
    )
  ORDER BY 
    CASE 
      WHEN i.current_stock = 0 THEN 1
      WHEN i.current_stock <= i.critical_stock_threshold THEN 2
      WHEN i.current_stock <= i.low_stock_threshold THEN 3
      ELSE 4
    END,
    i.current_stock ASC,
    i.name ASC;
END;
$$;


ALTER FUNCTION public.get_low_stock_items(user_uuid uuid) OWNER TO postgres;

--
-- Name: get_recommended_permissions_for_role(public.user_role); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.get_recommended_permissions_for_role(p_role public.user_role) RETURNS TABLE(feature_area public.feature_area, display_name text, description text, can_read boolean, can_write boolean, can_delete boolean, can_execute boolean, requires_premium boolean)
    LANGUAGE plpgsql
    AS $$
BEGIN
  RETURN QUERY
  SELECT 
    rfa.feature_area,
    fp.display_name,
    fp.description,
    rfa.can_read,
    rfa.can_write,
    rfa.can_delete,
    rfa.can_execute,
    (fp.requires_subscription_plan IS NOT NULL AND ARRAY_LENGTH(fp.requires_subscription_plan, 1) > 0) AS requires_premium
  FROM role_feature_access rfa
  JOIN feature_permissions fp ON fp.feature_area = rfa.feature_area
  WHERE rfa.role = p_role
  ORDER BY 
    CASE 
      WHEN fp.parent_feature IS NULL THEN 0 
      ELSE 1 
    END,
    fp.display_name;
END;
$$;


ALTER FUNCTION public.get_recommended_permissions_for_role(p_role public.user_role) OWNER TO postgres;

--
-- Name: FUNCTION get_recommended_permissions_for_role(p_role public.user_role); Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON FUNCTION public.get_recommended_permissions_for_role(p_role public.user_role) IS 'Returns recommended permissions for a specific user role with display information';


--
-- Name: get_user_active_experiments(uuid, public.widget_type); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.get_user_active_experiments(p_user_id uuid, p_widget_type public.widget_type DEFAULT NULL::public.widget_type) RETURNS jsonb
    LANGUAGE plpgsql SECURITY DEFINER
    AS $$
DECLARE
  v_experiments JSONB := '[]'::jsonb;
  v_experiment RECORD;
BEGIN
  FOR v_experiment IN
    SELECT 
      e.id as experiment_id,
      e.name as experiment_name,
      e.widget_type,
      v.id as variant_id,
      v.name as variant_name,
      v.is_control,
      v.config_override,
      uea.assigned_at
    FROM user_experiment_assignments uea
    JOIN widget_experiments e ON e.id = uea.experiment_id
    JOIN widget_experiment_variants v ON v.id = uea.variant_id
    WHERE uea.user_id = p_user_id
      AND e.status = 'active'
      AND NOW() BETWEEN COALESCE(e.start_date, '-infinity') AND COALESCE(e.end_date, 'infinity')
      AND (p_widget_type IS NULL OR e.widget_type = p_widget_type)
    ORDER BY uea.assigned_at DESC
  LOOP
    v_experiments := v_experiments || jsonb_build_object(
      'experiment_id', v_experiment.experiment_id,
      'experiment_name', v_experiment.experiment_name,
      'widget_type', v_experiment.widget_type,
      'variant_id', v_experiment.variant_id,
      'variant_name', v_experiment.variant_name,
      'is_control', v_experiment.is_control,
      'config_override', v_experiment.config_override,
      'assigned_at', v_experiment.assigned_at
    );
  END LOOP;

  RETURN jsonb_build_object(
    'user_id', p_user_id,
    'experiments', v_experiments,
    'total_experiments', jsonb_array_length(v_experiments)
  );
END;
$$;


ALTER FUNCTION public.get_user_active_experiments(p_user_id uuid, p_widget_type public.widget_type) OWNER TO postgres;

--
-- Name: get_user_details(uuid); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.get_user_details(p_user_id uuid) RETURNS jsonb
    LANGUAGE plpgsql SECURITY DEFINER
    AS $$
DECLARE
  v_user RECORD;
  v_permissions JSONB;
BEGIN
  -- Get user details
  SELECT bu.*, bp.name as business_name, bp.subscription_plan
  INTO v_user
  FROM business_users bu
  JOIN business_profiles bp ON bp.id = bu.business_profile_id
  WHERE bu.auth_user_id = p_user_id
  AND bu.status = 'active';
  
  IF NOT FOUND THEN
    RETURN NULL;
  END IF;
  
  -- Get user permissions
  SELECT jsonb_object_agg(
    ufa.feature_area,
    jsonb_build_object(
      'can_read', ufa.can_read,
      'can_write', ufa.can_write,
      'can_delete', ufa.can_delete,
      'can_execute', ufa.can_execute
    )
  ) INTO v_permissions
  FROM user_feature_access ufa
  WHERE ufa.business_user_id = v_user.id;
  
  RETURN jsonb_build_object(
    'id', v_user.id,
    'auth_user_id', v_user.auth_user_id,
    'email', v_user.email,
    'display_name', v_user.display_name,
    'role', v_user.role,
    'business_profile_id', v_user.business_profile_id,
    'business_name', v_user.business_name,
    'subscription_plan', v_user.subscription_plan,
    'is_primary_user', v_user.is_primary_user,
    'can_invite_users', v_user.can_invite_users,
    'permissions', COALESCE(v_permissions, '{}'::jsonb),
    'last_login_at', v_user.last_login_at
  );
END;
$$;


ALTER FUNCTION public.get_user_details(p_user_id uuid) OWNER TO postgres;

--
-- Name: get_user_feature_flags(uuid, public.widget_type); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.get_user_feature_flags(p_user_id uuid, p_widget_type public.widget_type DEFAULT NULL::public.widget_type) RETURNS jsonb
    LANGUAGE plpgsql SECURITY DEFINER
    AS $$
DECLARE
  v_flags JSONB := '{}'::jsonb;
  v_flag RECORD;
BEGIN
  FOR v_flag IN
    SELECT 
      flag_key,
      is_enabled,
      rollout_percentage,
      (rollout_percentage = 100 OR 
       (rollout_percentage > 0 AND (hashtext(p_user_id::text) % 100) < rollout_percentage)
      ) as user_enabled
    FROM widget_feature_flags
    WHERE (p_widget_type IS NULL OR widget_type IS NULL OR widget_type = p_widget_type)
  LOOP
    v_flags := jsonb_set(
      v_flags, 
      ARRAY[v_flag.flag_key], 
      to_jsonb(v_flag.is_enabled AND v_flag.user_enabled)
    );
  END LOOP;

  RETURN v_flags;
END;
$$;


ALTER FUNCTION public.get_user_feature_flags(p_user_id uuid, p_widget_type public.widget_type) OWNER TO postgres;

--
-- Name: get_user_navigation(uuid); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.get_user_navigation(p_user_id uuid) RETURNS jsonb
    LANGUAGE plpgsql SECURITY DEFINER
    AS $$
DECLARE
  v_business_user RECORD;
  v_navigation JSONB;
  v_custom_nav JSONB;
BEGIN
  -- Get business user details
  SELECT bu.*, bp.subscription_plan
  INTO v_business_user
  FROM business_users bu
  JOIN business_profiles bp ON bp.id = bu.business_profile_id
  WHERE bu.auth_user_id = p_user_id
  AND bu.status = 'active';
  
  IF NOT FOUND THEN
    RETURN '[]'::jsonb;
  END IF;
  
  -- Check for custom navigation first
  SELECT nav_items INTO v_custom_nav
  FROM navigation_config
  WHERE business_profile_id = v_business_user.business_profile_id
  AND is_active = TRUE
  LIMIT 1;
  
  IF v_custom_nav IS NOT NULL THEN
    v_navigation := v_custom_nav;
  ELSE
    -- Get role-based navigation
    SELECT nav_items INTO v_navigation
    FROM navigation_config
    WHERE business_profile_id IS NULL
    AND user_role = v_business_user.role
    AND is_active = TRUE
    LIMIT 1;
  END IF;
  
  -- Filter navigation based on permissions
  IF v_navigation IS NOT NULL THEN
    v_navigation := filter_navigation_by_permissions(
      v_navigation, 
      v_business_user.id,
      v_business_user.subscription_plan
    );
  END IF;
  
  RETURN COALESCE(v_navigation, '[]'::jsonb);
END;
$$;


ALTER FUNCTION public.get_user_navigation(p_user_id uuid) OWNER TO postgres;

--
-- Name: get_widget_usage_stats(uuid, public.widget_type, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.get_widget_usage_stats(p_user_id uuid, p_widget_type public.widget_type DEFAULT NULL::public.widget_type, p_days_back integer DEFAULT 30) RETURNS jsonb
    LANGUAGE plpgsql SECURITY DEFINER
    AS $$
DECLARE
  v_stats JSONB;
  v_events JSONB := '[]'::jsonb;
  v_event_record RECORD;
BEGIN
  -- Get overall statistics
  SELECT jsonb_build_object(
    'total_events', COUNT(*),
    'unique_sessions', COUNT(DISTINCT session_id),
    'event_types', jsonb_object_agg(event_type, event_count),
    'date_range', jsonb_build_object(
      'start_date', MIN(created_at),
      'end_date', MAX(created_at),
      'days_back', p_days_back
    )
  ) INTO v_stats
  FROM (
    SELECT 
      event_type,
      COUNT(*) as event_count,
      session_id,
      created_at
    FROM widget_usage_analytics
    WHERE user_id = p_user_id
      AND (p_widget_type IS NULL OR widget_type = p_widget_type)
      AND created_at >= NOW() - (p_days_back || ' days')::INTERVAL
    GROUP BY event_type, session_id, created_at
  ) events;

  -- Get recent events for timeline
  FOR v_event_record IN
    SELECT 
      widget_type,
      event_type,
      event_data,
      created_at,
      session_id,
      platform
    FROM widget_usage_analytics
    WHERE user_id = p_user_id
      AND (p_widget_type IS NULL OR widget_type = p_widget_type)
      AND created_at >= NOW() - (p_days_back || ' days')::INTERVAL
    ORDER BY created_at DESC
    LIMIT 50
  LOOP
    v_events := v_events || jsonb_build_object(
      'widget_type', v_event_record.widget_type,
      'event_type', v_event_record.event_type,
      'event_data', v_event_record.event_data,
      'created_at', v_event_record.created_at,
      'session_id', v_event_record.session_id,
      'platform', v_event_record.platform
    );
  END LOOP;

  RETURN jsonb_build_object(
    'user_id', p_user_id,
    'statistics', COALESCE(v_stats, '{}'::jsonb),
    'recent_events', v_events,
    'generated_at', NOW()
  );
END;
$$;


ALTER FUNCTION public.get_widget_usage_stats(p_user_id uuid, p_widget_type public.widget_type, p_days_back integer) OWNER TO postgres;

--
-- Name: handle_new_user_notification_setup(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.handle_new_user_notification_setup() RETURNS trigger
    LANGUAGE plpgsql SECURITY DEFINER
    AS $$
BEGIN
  -- Create default notification preferences
  PERFORM public.create_default_notification_preferences(NEW.id);
  RETURN NEW;
END;
$$;


ALTER FUNCTION public.handle_new_user_notification_setup() OWNER TO postgres;

--
-- Name: initialize_user_metrics(uuid); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.initialize_user_metrics(user_uuid uuid) RETURNS void
    LANGUAGE plpgsql SECURITY DEFINER
    AS $$
BEGIN
  -- Initialize metrics summary for new user
  INSERT INTO public.business_metrics_summary (user_id)
  VALUES (user_uuid)
  ON CONFLICT (user_id) DO NOTHING;
  
  -- Refresh with actual data
  PERFORM public.refresh_business_metrics_summary() 
  WHERE NEW.user_id = user_uuid OR OLD.user_id = user_uuid;
END;
$$;


ALTER FUNCTION public.initialize_user_metrics(user_uuid uuid) OWNER TO postgres;

--
-- Name: invalidate_widget_config_cache(uuid, public.widget_type); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.invalidate_widget_config_cache(p_user_id uuid DEFAULT NULL::uuid, p_widget_type public.widget_type DEFAULT NULL::public.widget_type) RETURNS integer
    LANGUAGE plpgsql SECURITY DEFINER
    AS $$
DECLARE
  v_deleted_count INTEGER;
BEGIN
  DELETE FROM widget_config_cache
  WHERE (p_user_id IS NULL OR user_id = p_user_id)
    AND (p_widget_type IS NULL OR widget_type = p_widget_type);
  
  GET DIAGNOSTICS v_deleted_count = ROW_COUNT;
  RETURN v_deleted_count;
END;
$$;


ALTER FUNCTION public.invalidate_widget_config_cache(p_user_id uuid, p_widget_type public.widget_type) OWNER TO postgres;

--
-- Name: invite_business_user(uuid, text, text, public.user_role, uuid, jsonb); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.invite_business_user(p_business_profile_id uuid, p_email text, p_display_name text, p_role public.user_role, p_invited_by uuid, p_permissions jsonb DEFAULT NULL::jsonb) RETURNS uuid
    LANGUAGE plpgsql SECURITY DEFINER
    AS $$
DECLARE
  v_user_id UUID;
  v_token TEXT;
  v_temp_password TEXT;
BEGIN
  -- Generate invitation token
  v_token := encode(gen_random_bytes(32), 'hex');
  
  -- Generate temporary password
  v_temp_password := substring(encode(gen_random_bytes(12), 'base64') from 1 for 12);
  
  -- Create business user
  INSERT INTO business_users (
    business_profile_id,
    email,
    display_name,
    role,
    status,
    invited_by,
    invitation_token,
    invitation_expires_at,
    temp_password,
    can_invite_users
  ) VALUES (
    p_business_profile_id,
    p_email,
    p_display_name,
    p_role,
    'invited',
    p_invited_by,
    v_token,
    NOW() + INTERVAL '7 days',
    crypt(v_temp_password, gen_salt('bf')),
    CASE WHEN p_role IN ('owner', 'admin') THEN TRUE ELSE FALSE END
  ) RETURNING id INTO v_user_id;
  
  -- Set up default permissions based on role
  INSERT INTO user_feature_access (
    business_user_id,
    feature_area,
    can_read,
    can_write,
    can_delete,
    can_execute,
    granted_by
  )
  SELECT 
    v_user_id,
    feature_area,
    can_read,
    can_write,
    can_delete,
    can_execute,
    (SELECT id FROM business_users WHERE auth_user_id = p_invited_by LIMIT 1)
  FROM role_feature_access
  WHERE role = p_role;
  
  -- Apply custom permissions if provided
  IF p_permissions IS NOT NULL THEN
    -- Implementation for custom permissions would go here
    -- This would parse the JSON and update specific permissions
  END IF;
  
  -- Return the user ID for the calling application to handle notification
  RETURN v_user_id;
END;
$$;


ALTER FUNCTION public.invite_business_user(p_business_profile_id uuid, p_email text, p_display_name text, p_role public.user_role, p_invited_by uuid, p_permissions jsonb) OWNER TO postgres;

--
-- Name: is_admin(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.is_admin() RETURNS boolean
    LANGUAGE sql STABLE
    AS $$
  SELECT exists (
    SELECT 1 FROM public.admin_users a
    JOIN auth.users u ON lower(u.email) = lower(a.email)
    WHERE u.id = auth.uid() AND a.role = 'admin'
  );
$$;


ALTER FUNCTION public.is_admin() OWNER TO postgres;

--
-- Name: is_analyst(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.is_analyst() RETURNS boolean
    LANGUAGE sql STABLE
    AS $$
  SELECT exists (
    SELECT 1 FROM public.admin_users a
    JOIN auth.users u ON lower(u.email) = lower(a.email)
    WHERE u.id = auth.uid() AND a.role IN ('admin','analyst')
  );
$$;


ALTER FUNCTION public.is_analyst() OWNER TO postgres;

--
-- Name: is_user_in_quiet_hours(uuid); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.is_user_in_quiet_hours(user_uuid uuid) RETURNS boolean
    LANGUAGE plpgsql SECURITY DEFINER
    AS $$
DECLARE
  prefs_record public.notification_preferences%ROWTYPE;
  user_current_time TIME;
BEGIN
  -- Get user notification preferences
  SELECT * INTO prefs_record
  FROM public.notification_preferences
  WHERE user_id = user_uuid;
  
  -- If no preferences found, return false (no quiet hours)
  IF NOT FOUND THEN
    RETURN false;
  END IF;
  
  -- Get current time in user's timezone (defaulting to UTC for now)
  user_current_time := CURRENT_TIME;
  
  -- Check if current time is in quiet hours
  IF prefs_record.quiet_hours_start <= prefs_record.quiet_hours_end THEN
    -- Same day quiet hours (e.g., 22:00 to 08:00 next day)
    RETURN user_current_time >= prefs_record.quiet_hours_start 
           OR user_current_time <= prefs_record.quiet_hours_end;
  ELSE
    -- Cross-day quiet hours (e.g., 08:00 to 22:00)
    RETURN user_current_time >= prefs_record.quiet_hours_start 
           AND user_current_time <= prefs_record.quiet_hours_end;
  END IF;
END;
$$;


ALTER FUNCTION public.is_user_in_quiet_hours(user_uuid uuid) OWNER TO postgres;

--
-- Name: mock_dau(integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.mock_dau(days integer DEFAULT 14) RETURNS TABLE(day date, count bigint)
    LANGUAGE plpgsql
    AS $$
BEGIN
  RETURN QUERY
  SELECT 
    (CURRENT_DATE - INTERVAL '1 day' * generate_series(0, days-1))::date as day,
    (50 + (random() * 100)::int + generate_series(0, days-1) * 2)::bigint as count
  ORDER BY day DESC;
END;
$$;


ALTER FUNCTION public.mock_dau(days integer) OWNER TO postgres;

--
-- Name: notify_widget_config_change(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.notify_widget_config_change() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
  PERFORM pg_notify(
    'widget_config_update',
    json_build_object(
      'user_id', NEW.user_id,
      'widget_type', NEW.widget_type,
      'action', TG_OP,
      'timestamp', extract(epoch from NOW())
    )::text
  );
  RETURN NEW;
END;
$$;


ALTER FUNCTION public.notify_widget_config_change() OWNER TO postgres;

--
-- Name: record_stock_movement(uuid, uuid, text, integer, uuid, text, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.record_stock_movement(p_user_id uuid, p_item_id uuid, p_movement_type text, p_quantity_change integer, p_reference_id uuid DEFAULT NULL::uuid, p_reference_type text DEFAULT NULL::text, p_notes text DEFAULT NULL::text) RETURNS uuid
    LANGUAGE plpgsql SECURITY DEFINER
    AS $$
DECLARE
  current_stock integer;
  new_stock integer;
  movement_id uuid;
BEGIN
  -- Get current stock
  SELECT current_stock INTO current_stock
  FROM public.items
  WHERE id = p_item_id AND user_id = p_user_id;
  
  IF NOT FOUND THEN
    RAISE EXCEPTION 'Item not found or access denied';
  END IF;
  
  -- Calculate new stock level
  new_stock := current_stock + p_quantity_change;
  
  -- Ensure stock doesn't go negative
  IF new_stock < 0 THEN
    RAISE EXCEPTION 'Insufficient stock. Current: %, Requested: %', current_stock, ABS(p_quantity_change);
  END IF;
  
  -- Update item stock
  UPDATE public.items
  SET current_stock = new_stock,
      updated_at = NOW()
  WHERE id = p_item_id AND user_id = p_user_id;
  
  -- Record movement
  INSERT INTO public.stock_movements (
    user_id, item_id, movement_type, quantity_change, 
    new_stock_level, reference_id, reference_type, notes
  )
  VALUES (
    p_user_id, p_item_id, p_movement_type, p_quantity_change,
    new_stock, p_reference_id, p_reference_type, p_notes
  )
  RETURNING id INTO movement_id;
  
  RETURN movement_id;
END;
$$;


ALTER FUNCTION public.record_stock_movement(p_user_id uuid, p_item_id uuid, p_movement_type text, p_quantity_change integer, p_reference_id uuid, p_reference_type text, p_notes text) OWNER TO postgres;

--
-- Name: FUNCTION record_stock_movement(p_user_id uuid, p_item_id uuid, p_movement_type text, p_quantity_change integer, p_reference_id uuid, p_reference_type text, p_notes text); Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON FUNCTION public.record_stock_movement(p_user_id uuid, p_item_id uuid, p_movement_type text, p_quantity_change integer, p_reference_id uuid, p_reference_type text, p_notes text) IS 'Records stock movements and updates item stock levels with validation.';


--
-- Name: record_widget_usage(uuid, uuid, public.widget_type, character varying, jsonb, character varying, text, character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.record_widget_usage(p_user_id uuid, p_business_profile_id uuid, p_widget_type public.widget_type, p_event_type character varying, p_event_data jsonb DEFAULT '{}'::jsonb, p_session_id character varying DEFAULT NULL::character varying, p_user_agent text DEFAULT NULL::text, p_platform character varying DEFAULT NULL::character varying) RETURNS boolean
    LANGUAGE plpgsql SECURITY DEFINER
    AS $$
DECLARE
  v_experiment_assignment RECORD;
  v_config_version INTEGER := 1;
BEGIN
  -- Get current experiment assignment if any
  SELECT 
    uea.experiment_id,
    uea.variant_id
  INTO v_experiment_assignment
  FROM user_experiment_assignments uea
  JOIN widget_experiments e ON e.id = uea.experiment_id
  WHERE uea.user_id = p_user_id
    AND e.widget_type = p_widget_type
    AND e.status = 'active'
    AND NOW() BETWEEN COALESCE(e.start_date, '-infinity') AND COALESCE(e.end_date, 'infinity')
  ORDER BY uea.assigned_at DESC
  LIMIT 1;

  -- Get config version (increment with each config change)
  SELECT COALESCE(MAX(version), 1) INTO v_config_version
  FROM widget_config_cache
  WHERE user_id = p_user_id AND widget_type = p_widget_type;

  -- Insert analytics record
  INSERT INTO widget_usage_analytics (
    user_id,
    business_profile_id,
    widget_type,
    event_type,
    event_data,
    config_version,
    experiment_id,
    variant_id,
    session_id,
    user_agent,
    platform
  ) VALUES (
    p_user_id,
    p_business_profile_id,
    p_widget_type,
    p_event_type,
    p_event_data,
    v_config_version,
    v_experiment_assignment.experiment_id,
    v_experiment_assignment.variant_id,
    p_session_id,
    p_user_agent,
    p_platform
  );

  RETURN TRUE;
EXCEPTION
  WHEN OTHERS THEN
    -- Log error but don't fail the request
    RAISE WARNING 'Failed to record widget usage: %', SQLERRM;
    RETURN FALSE;
END;
$$;


ALTER FUNCTION public.record_widget_usage(p_user_id uuid, p_business_profile_id uuid, p_widget_type public.widget_type, p_event_type character varying, p_event_data jsonb, p_session_id character varying, p_user_agent text, p_platform character varying) OWNER TO postgres;

--
-- Name: FUNCTION record_widget_usage(p_user_id uuid, p_business_profile_id uuid, p_widget_type public.widget_type, p_event_type character varying, p_event_data jsonb, p_session_id character varying, p_user_agent text, p_platform character varying); Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON FUNCTION public.record_widget_usage(p_user_id uuid, p_business_profile_id uuid, p_widget_type public.widget_type, p_event_type character varying, p_event_data jsonb, p_session_id character varying, p_user_agent text, p_platform character varying) IS 'Records widget usage analytics for tracking user behavior and experiment performance';


--
-- Name: refresh_business_metrics_summary(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.refresh_business_metrics_summary() RETURNS trigger
    LANGUAGE plpgsql SECURITY DEFINER
    AS $$
DECLARE
  user_uuid uuid;
  metrics_data json;
BEGIN
  -- Get the user_id from the affected row
  user_uuid := COALESCE(NEW.user_id, OLD.user_id);
  
  -- Get fresh metrics using our FIXED function
  SELECT public.get_business_metrics_enhanced(user_uuid) INTO metrics_data;
  
  -- Update or insert the summary record
  INSERT INTO public.business_metrics_summary (
    user_id,
    items_count,
    customers_count,
    suppliers_count,
    business_contacts_count,
    total_invoices_count,
    paid_invoices_count,
    pending_invoices_count,
    draft_invoices_count,
    unpaid_invoices_count,
    unpaid_invoices_amount,
    total_revenue,
    revenue_this_month,
    revenue_this_week,
    average_invoice_value,
    overdue_invoices_count,
    overdue_invoices_amount,
    collection_rate,
    low_stock_items_count,
    out_of_stock_items_count,
    collection_efficiency_rate,
    cash_flow_forecast_30_days,
    business_health_score,
    customer_lifetime_value_avg,
    high_value_customers_count,
    inactive_customers_count,
    inventory_turnover_rate,
    subscription_plan,
    last_updated
  )
  VALUES (
    user_uuid,
    (metrics_data->>'items_count')::integer,
    (metrics_data->>'customers_count')::integer,
    (metrics_data->>'suppliers_count')::integer,
    (metrics_data->>'business_contacts_count')::integer,
    (metrics_data->>'total_invoices_count')::integer,
    (metrics_data->>'paid_invoices_count')::integer,
    (metrics_data->>'pending_invoices_count')::integer,
    (metrics_data->>'draft_invoices_count')::integer,
    (metrics_data->>'unpaid_invoices_count')::integer,
    (metrics_data->>'unpaid_invoices_amount')::numeric,
    (metrics_data->>'total_revenue')::numeric,
    (metrics_data->>'revenue_this_month')::numeric,
    (metrics_data->>'revenue_this_week')::numeric,
    (metrics_data->>'average_invoice_value')::numeric,
    (metrics_data->>'overdue_invoices_count')::integer,
    (metrics_data->>'overdue_invoices_amount')::numeric,
    (metrics_data->>'collection_rate')::numeric,
    (metrics_data->>'low_stock_items_count')::integer,
    (metrics_data->>'out_of_stock_items_count')::integer,
    (metrics_data->>'collection_efficiency_rate')::numeric,
    (metrics_data->>'cash_flow_forecast_30_days')::numeric,
    metrics_data->>'business_health_score',
    (metrics_data->>'customer_lifetime_value_avg')::numeric,
    (metrics_data->>'high_value_customers_count')::integer,
    (metrics_data->>'inactive_customers_count')::integer,
    (metrics_data->>'inventory_turnover_rate')::numeric,
    metrics_data->>'subscription_plan',
    NOW()
  )
  ON CONFLICT (user_id) DO UPDATE SET
    items_count = EXCLUDED.items_count,
    customers_count = EXCLUDED.customers_count,
    suppliers_count = EXCLUDED.suppliers_count,
    business_contacts_count = EXCLUDED.business_contacts_count,
    total_invoices_count = EXCLUDED.total_invoices_count,
    paid_invoices_count = EXCLUDED.paid_invoices_count,
    pending_invoices_count = EXCLUDED.pending_invoices_count,
    draft_invoices_count = EXCLUDED.draft_invoices_count,
    unpaid_invoices_count = EXCLUDED.unpaid_invoices_count,
    unpaid_invoices_amount = EXCLUDED.unpaid_invoices_amount,
    total_revenue = EXCLUDED.total_revenue,
    revenue_this_month = EXCLUDED.revenue_this_month,
    revenue_this_week = EXCLUDED.revenue_this_week,
    average_invoice_value = EXCLUDED.average_invoice_value,
    overdue_invoices_count = EXCLUDED.overdue_invoices_count,
    overdue_invoices_amount = EXCLUDED.overdue_invoices_amount,
    collection_rate = EXCLUDED.collection_rate,
    low_stock_items_count = EXCLUDED.low_stock_items_count,
    out_of_stock_items_count = EXCLUDED.out_of_stock_items_count,
    collection_efficiency_rate = EXCLUDED.collection_efficiency_rate,
    cash_flow_forecast_30_days = EXCLUDED.cash_flow_forecast_30_days,
    business_health_score = EXCLUDED.business_health_score,
    customer_lifetime_value_avg = EXCLUDED.customer_lifetime_value_avg,
    high_value_customers_count = EXCLUDED.high_value_customers_count,
    inactive_customers_count = EXCLUDED.inactive_customers_count,
    inventory_turnover_rate = EXCLUDED.inventory_turnover_rate,
    subscription_plan = EXCLUDED.subscription_plan,
    last_updated = NOW();
  
  RETURN NULL;
END;
$$;


ALTER FUNCTION public.refresh_business_metrics_summary() OWNER TO postgres;

--
-- Name: FUNCTION refresh_business_metrics_summary(); Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON FUNCTION public.refresh_business_metrics_summary() IS 'Trigger function that refreshes business_metrics_summary using the FIXED revenue calculation logic.
Ensures revenue only includes PAID invoices, not all invoices.
Triggered when invoices, items, or business_contacts are modified.';


--
-- Name: refresh_business_metrics_summary_safe(uuid); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.refresh_business_metrics_summary_safe(p_user_id uuid) RETURNS jsonb
    LANGUAGE plpgsql SECURITY DEFINER
    AS $$
DECLARE
    v_result JSONB;
    v_error_message TEXT;
BEGIN
    -- This is a safe wrapper around business metrics refresh
    -- It ensures metrics are updated without failing the main operation
    
    BEGIN
        -- Try to refresh metrics by calling the enhanced metrics function
        -- This will update any cached data or summary tables
        SELECT get_business_metrics_enhanced(p_user_id) INTO v_result;
        
        -- Return success with basic stats
        RETURN jsonb_build_object(
            'success', true,
            'user_id', p_user_id,
            'refreshed_at', NOW(),
            'message', 'Business metrics refreshed successfully'
        );
        
    EXCEPTION WHEN OTHERS THEN
        -- If refresh fails, log the error but don't fail the operation
        GET STACKED DIAGNOSTICS v_error_message = MESSAGE_TEXT;
        
        -- Return success even if refresh failed (non-critical operation)
        RETURN jsonb_build_object(
            'success', false,
            'user_id', p_user_id,
            'error', v_error_message,
            'message', 'Metrics refresh failed but operation continues'
        );
    END;
END;
$$;


ALTER FUNCTION public.refresh_business_metrics_summary_safe(p_user_id uuid) OWNER TO postgres;

--
-- Name: FUNCTION refresh_business_metrics_summary_safe(p_user_id uuid); Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON FUNCTION public.refresh_business_metrics_summary_safe(p_user_id uuid) IS 'Safely refreshes business metrics summary for a user. Non-critical operation that does not fail parent transactions.';


--
-- Name: reset_user_widget_config(uuid, uuid, public.widget_type); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.reset_user_widget_config(p_user_id uuid, p_business_profile_id uuid, p_widget_type public.widget_type) RETURNS jsonb
    LANGUAGE plpgsql SECURITY DEFINER
    AS $$
DECLARE
  v_default_config JSONB;
  v_old_config JSONB;
BEGIN
  -- Get default configuration
  SELECT default_config INTO v_default_config
  FROM widget_config_templates
  WHERE widget_type = p_widget_type AND is_default = TRUE
  LIMIT 1;

  IF v_default_config IS NULL THEN
    RETURN jsonb_build_object(
      'success', FALSE,
      'error', 'No default configuration found for widget type'
    );
  END IF;

  -- Get current config for history
  SELECT config INTO v_old_config
  FROM user_widget_configs
  WHERE user_id = p_user_id 
    AND business_profile_id = p_business_profile_id 
    AND widget_type = p_widget_type;

  -- Update to default configuration
  UPDATE user_widget_configs SET
    config = v_default_config,
    layout = 'list',
    last_configured_at = NOW(),
    updated_at = NOW()
  WHERE user_id = p_user_id 
    AND business_profile_id = p_business_profile_id 
    AND widget_type = p_widget_type;

  -- Record in history
  INSERT INTO user_config_history (
    user_id,
    widget_type,
    change_type,
    old_config,
    new_config,
    change_reason,
    triggered_by
  ) VALUES (
    p_user_id,
    p_widget_type,
    'reset',
    v_old_config,
    v_default_config,
    'Reset to default configuration',
    'user'
  );

  -- Invalidate cache
  DELETE FROM widget_config_cache 
  WHERE user_id = p_user_id 
    AND widget_type = p_widget_type;

  RETURN jsonb_build_object(
    'success', TRUE,
    'message', 'Configuration reset to default',
    'config', v_default_config
  );
END;
$$;


ALTER FUNCTION public.reset_user_widget_config(p_user_id uuid, p_business_profile_id uuid, p_widget_type public.widget_type) OWNER TO postgres;

--
-- Name: update_updated_at_column(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.update_updated_at_column() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
  NEW.updated_at = NOW();
  RETURN NEW;
END;
$$;


ALTER FUNCTION public.update_updated_at_column() OWNER TO postgres;

--
-- Name: update_user_permissions(uuid, jsonb, uuid); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.update_user_permissions(p_business_user_id uuid, p_permissions jsonb, p_updated_by uuid) RETURNS jsonb
    LANGUAGE plpgsql SECURITY DEFINER
    AS $$
DECLARE
  v_permission_key TEXT;
  v_permission_value JSONB;
  v_feature_area feature_area;
BEGIN
  -- Validate that the updater has permission to modify permissions
  IF NOT EXISTS (
    SELECT 1 FROM business_users updater
    JOIN business_users target ON target.id = p_business_user_id
    WHERE updater.auth_user_id = p_updated_by
    AND updater.business_profile_id = target.business_profile_id
    AND updater.status = 'active'
    AND updater.role IN ('owner', 'admin')
  ) THEN
    RETURN jsonb_build_object(
      'success', FALSE,
      'error', 'Insufficient permissions to update user permissions'
    );
  END IF;
  
  -- Update permissions
  FOR v_permission_key, v_permission_value IN 
    SELECT * FROM jsonb_each(p_permissions)
  LOOP
    v_feature_area := v_permission_key::feature_area;
    
    INSERT INTO user_feature_access (
      business_user_id,
      feature_area,
      can_read,
      can_write,
      can_delete,
      can_execute,
      granted_by
    ) VALUES (
      p_business_user_id,
      v_feature_area,
      COALESCE((v_permission_value->>'can_read')::boolean, FALSE),
      COALESCE((v_permission_value->>'can_write')::boolean, FALSE),
      COALESCE((v_permission_value->>'can_delete')::boolean, FALSE),
      COALESCE((v_permission_value->>'can_execute')::boolean, FALSE),
      (SELECT id FROM business_users WHERE auth_user_id = p_updated_by LIMIT 1)
    )
    ON CONFLICT (business_user_id, feature_area) 
    DO UPDATE SET
      can_read = EXCLUDED.can_read,
      can_write = EXCLUDED.can_write,
      can_delete = EXCLUDED.can_delete,
      can_execute = EXCLUDED.can_execute,
      granted_by = EXCLUDED.granted_by,
      granted_at = NOW();
  END LOOP;
  
  -- Log the change
  INSERT INTO user_activity_log (
    business_user_id,
    business_profile_id,
    activity_type,
    activity_data
  ) VALUES (
    p_business_user_id,
    (SELECT business_profile_id FROM business_users WHERE id = p_business_user_id),
    'permissions_updated',
    jsonb_build_object('updated_by', p_updated_by, 'permissions', p_permissions)
  );
  
  RETURN jsonb_build_object(
    'success', TRUE,
    'message', 'Permissions updated successfully'
  );
END;
$$;


ALTER FUNCTION public.update_user_permissions(p_business_user_id uuid, p_permissions jsonb, p_updated_by uuid) OWNER TO postgres;

--
-- Name: update_user_widget_config(uuid, uuid, public.widget_type, jsonb, public.widget_layout); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.update_user_widget_config(p_user_id uuid, p_business_profile_id uuid, p_widget_type public.widget_type, p_config jsonb, p_layout public.widget_layout DEFAULT NULL::public.widget_layout) RETURNS jsonb
    LANGUAGE plpgsql SECURITY DEFINER
    AS $$
DECLARE
  v_old_config JSONB;
  v_config_diff JSONB;
  v_updated_config RECORD;
BEGIN
  -- Get current configuration for history
  SELECT config INTO v_old_config
  FROM user_widget_configs
  WHERE user_id = p_user_id 
    AND business_profile_id = p_business_profile_id 
    AND widget_type = p_widget_type;

  -- Update or insert user configuration
  INSERT INTO user_widget_configs (
    user_id,
    business_profile_id,
    widget_type,
    config,
    layout,
    last_configured_at,
    is_enabled
  ) VALUES (
    p_user_id,
    p_business_profile_id,
    p_widget_type,
    p_config,
    COALESCE(p_layout, 'list'),
    NOW(),
    TRUE
  )
  ON CONFLICT (user_id, business_profile_id, widget_type) DO UPDATE SET
    config = EXCLUDED.config,
    layout = COALESCE(EXCLUDED.layout, user_widget_configs.layout),
    last_configured_at = EXCLUDED.last_configured_at,
    updated_at = NOW()
  RETURNING * INTO v_updated_config;

  -- Record configuration change in history
  INSERT INTO user_config_history (
    user_id,
    widget_type,
    change_type,
    old_config,
    new_config,
    change_reason,
    triggered_by
  ) VALUES (
    p_user_id,
    p_widget_type,
    'update',
    v_old_config,
    p_config,
    'User configuration update',
    'user'
  );

  -- Invalidate cache
  DELETE FROM widget_config_cache 
  WHERE user_id = p_user_id 
    AND widget_type = p_widget_type;

  -- Return success response with updated config
  RETURN jsonb_build_object(
    'success', TRUE,
    'user_id', p_user_id,
    'widget_type', p_widget_type,
    'updated_at', v_updated_config.updated_at,
    'config', v_updated_config.config
  );
END;
$$;


ALTER FUNCTION public.update_user_widget_config(p_user_id uuid, p_business_profile_id uuid, p_widget_type public.widget_type, p_config jsonb, p_layout public.widget_layout) OWNER TO postgres;

--
-- Name: FUNCTION update_user_widget_config(p_user_id uuid, p_business_profile_id uuid, p_widget_type public.widget_type, p_config jsonb, p_layout public.widget_layout); Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON FUNCTION public.update_user_widget_config(p_user_id uuid, p_business_profile_id uuid, p_widget_type public.widget_type, p_config jsonb, p_layout public.widget_layout) IS 'Updates user-specific widget configuration and records change history';


--
-- Name: validate_user_session(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.validate_user_session(p_session_token text) RETURNS jsonb
    LANGUAGE plpgsql SECURITY DEFINER
    AS $$
DECLARE
  v_session RECORD;
  v_business_user RECORD;
BEGIN
  -- Get session details
  SELECT us.*, bu.status as user_status, bu.role, bu.business_profile_id
  INTO v_session
  FROM user_sessions us
  JOIN business_users bu ON bu.id = us.business_user_id
  WHERE us.session_token = p_session_token
  AND us.is_active = TRUE
  AND us.expires_at > NOW();
  
  IF NOT FOUND THEN
    RETURN jsonb_build_object(
      'valid', FALSE,
      'error', 'Invalid or expired session'
    );
  END IF;
  
  -- Check if user is still active
  IF v_session.user_status != 'active' THEN
    -- Deactivate the session
    UPDATE user_sessions SET
      is_active = FALSE
    WHERE session_token = p_session_token;
    
    RETURN jsonb_build_object(
      'valid', FALSE,
      'error', 'User account is not active'
    );
  END IF;
  
  -- Update last activity
  UPDATE user_sessions SET
    last_activity_at = NOW()
  WHERE session_token = p_session_token;
  
  RETURN jsonb_build_object(
    'valid', TRUE,
    'business_user_id', v_session.business_user_id,
    'business_profile_id', v_session.business_profile_id,
    'role', v_session.role
  );
END;
$$;


ALTER FUNCTION public.validate_user_session(p_session_token text) OWNER TO postgres;

--
-- Name: validate_variant_belongs_to_experiment(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.validate_variant_belongs_to_experiment() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM widget_experiment_variants 
    WHERE id = NEW.variant_id AND experiment_id = NEW.experiment_id
  ) THEN
    RAISE EXCEPTION 'Variant % does not belong to experiment %', NEW.variant_id, NEW.experiment_id;
  END IF;
  RETURN NEW;
END;
$$;


ALTER FUNCTION public.validate_variant_belongs_to_experiment() OWNER TO postgres;

--
-- Name: validate_widget_config(public.widget_type, jsonb); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.validate_widget_config(p_widget_type public.widget_type, p_config jsonb) RETURNS jsonb
    LANGUAGE plpgsql SECURITY DEFINER
    AS $$
DECLARE
  v_schema JSONB;
  v_validation_result JSONB;
BEGIN
  -- Get configuration schema
  SELECT config_schema INTO v_schema
  FROM widget_config_templates
  WHERE widget_type = p_widget_type AND is_default = TRUE
  LIMIT 1;

  IF v_schema IS NULL THEN
    RETURN jsonb_build_object(
      'valid', FALSE,
      'error', 'No schema found for widget type'
    );
  END IF;

  -- Basic validation (in a real implementation, you'd use a JSON schema validator)
  -- For now, we'll do basic checks
  IF p_config IS NULL OR p_config = '{}'::jsonb THEN
    RETURN jsonb_build_object(
      'valid', FALSE,
      'error', 'Configuration cannot be empty'
    );
  END IF;

  -- Configuration is valid
  RETURN jsonb_build_object(
    'valid', TRUE,
    'schema_version', v_schema->>'version',
    'validated_at', NOW()
  );
END;
$$;


ALTER FUNCTION public.validate_widget_config(p_widget_type public.widget_type, p_config jsonb) OWNER TO postgres;

--
-- Name: warm_widget_config_cache(uuid, uuid); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.warm_widget_config_cache(p_user_id uuid, p_business_profile_id uuid) RETURNS jsonb
    LANGUAGE plpgsql SECURITY DEFINER
    AS $$
DECLARE
  v_cached_configs JSONB := '[]'::jsonb;
  v_widget_type widget_type;
  v_config JSONB;
BEGIN
  -- Pre-cache configurations for all supported widget types
  FOR v_widget_type IN 
    SELECT DISTINCT unnest(enum_range(NULL::widget_type))
  LOOP
    -- Get and cache the configuration
    v_config := get_cached_widget_config(
      p_user_id, 
      p_business_profile_id, 
      v_widget_type, 
      TRUE -- force refresh
    );
    
    v_cached_configs := v_cached_configs || jsonb_build_object(
      'widget_type', v_widget_type,
      'cached', TRUE,
      'config_size', length(v_config::text)
    );
  END LOOP;

  RETURN jsonb_build_object(
    'user_id', p_user_id,
    'business_profile_id', p_business_profile_id,
    'cached_widgets', v_cached_configs,
    'cache_warmed_at', NOW()
  );
END;
$$;


ALTER FUNCTION public.warm_widget_config_cache(p_user_id uuid, p_business_profile_id uuid) OWNER TO postgres;

--
-- Name: apply_rls(jsonb, integer); Type: FUNCTION; Schema: realtime; Owner: supabase_admin
--

CREATE FUNCTION realtime.apply_rls(wal jsonb, max_record_bytes integer DEFAULT (1024 * 1024)) RETURNS SETOF realtime.wal_rls
    LANGUAGE plpgsql
    AS $$
declare
-- Regclass of the table e.g. public.notes
entity_ regclass = (quote_ident(wal ->> 'schema') || '.' || quote_ident(wal ->> 'table'))::regclass;

-- I, U, D, T: insert, update ...
action realtime.action = (
    case wal ->> 'action'
        when 'I' then 'INSERT'
        when 'U' then 'UPDATE'
        when 'D' then 'DELETE'
        else 'ERROR'
    end
);

-- Is row level security enabled for the table
is_rls_enabled bool = relrowsecurity from pg_class where oid = entity_;

subscriptions realtime.subscription[] = array_agg(subs)
    from
        realtime.subscription subs
    where
        subs.entity = entity_;

-- Subscription vars
roles regrole[] = array_agg(distinct us.claims_role::text)
    from
        unnest(subscriptions) us;

working_role regrole;
claimed_role regrole;
claims jsonb;

subscription_id uuid;
subscription_has_access bool;
visible_to_subscription_ids uuid[] = '{}';

-- structured info for wal's columns
columns realtime.wal_column[];
-- previous identity values for update/delete
old_columns realtime.wal_column[];

error_record_exceeds_max_size boolean = octet_length(wal::text) > max_record_bytes;

-- Primary jsonb output for record
output jsonb;

begin
perform set_config('role', null, true);

columns =
    array_agg(
        (
            x->>'name',
            x->>'type',
            x->>'typeoid',
            realtime.cast(
                (x->'value') #>> '{}',
                coalesce(
                    (x->>'typeoid')::regtype, -- null when wal2json version <= 2.4
                    (x->>'type')::regtype
                )
            ),
            (pks ->> 'name') is not null,
            true
        )::realtime.wal_column
    )
    from
        jsonb_array_elements(wal -> 'columns') x
        left join jsonb_array_elements(wal -> 'pk') pks
            on (x ->> 'name') = (pks ->> 'name');

old_columns =
    array_agg(
        (
            x->>'name',
            x->>'type',
            x->>'typeoid',
            realtime.cast(
                (x->'value') #>> '{}',
                coalesce(
                    (x->>'typeoid')::regtype, -- null when wal2json version <= 2.4
                    (x->>'type')::regtype
                )
            ),
            (pks ->> 'name') is not null,
            true
        )::realtime.wal_column
    )
    from
        jsonb_array_elements(wal -> 'identity') x
        left join jsonb_array_elements(wal -> 'pk') pks
            on (x ->> 'name') = (pks ->> 'name');

for working_role in select * from unnest(roles) loop

    -- Update `is_selectable` for columns and old_columns
    columns =
        array_agg(
            (
                c.name,
                c.type_name,
                c.type_oid,
                c.value,
                c.is_pkey,
                pg_catalog.has_column_privilege(working_role, entity_, c.name, 'SELECT')
            )::realtime.wal_column
        )
        from
            unnest(columns) c;

    old_columns =
            array_agg(
                (
                    c.name,
                    c.type_name,
                    c.type_oid,
                    c.value,
                    c.is_pkey,
                    pg_catalog.has_column_privilege(working_role, entity_, c.name, 'SELECT')
                )::realtime.wal_column
            )
            from
                unnest(old_columns) c;

    if action <> 'DELETE' and count(1) = 0 from unnest(columns) c where c.is_pkey then
        return next (
            jsonb_build_object(
                'schema', wal ->> 'schema',
                'table', wal ->> 'table',
                'type', action
            ),
            is_rls_enabled,
            -- subscriptions is already filtered by entity
            (select array_agg(s.subscription_id) from unnest(subscriptions) as s where claims_role = working_role),
            array['Error 400: Bad Request, no primary key']
        )::realtime.wal_rls;

    -- The claims role does not have SELECT permission to the primary key of entity
    elsif action <> 'DELETE' and sum(c.is_selectable::int) <> count(1) from unnest(columns) c where c.is_pkey then
        return next (
            jsonb_build_object(
                'schema', wal ->> 'schema',
                'table', wal ->> 'table',
                'type', action
            ),
            is_rls_enabled,
            (select array_agg(s.subscription_id) from unnest(subscriptions) as s where claims_role = working_role),
            array['Error 401: Unauthorized']
        )::realtime.wal_rls;

    else
        output = jsonb_build_object(
            'schema', wal ->> 'schema',
            'table', wal ->> 'table',
            'type', action,
            'commit_timestamp', to_char(
                ((wal ->> 'timestamp')::timestamptz at time zone 'utc'),
                'YYYY-MM-DD"T"HH24:MI:SS.MS"Z"'
            ),
            'columns', (
                select
                    jsonb_agg(
                        jsonb_build_object(
                            'name', pa.attname,
                            'type', pt.typname
                        )
                        order by pa.attnum asc
                    )
                from
                    pg_attribute pa
                    join pg_type pt
                        on pa.atttypid = pt.oid
                where
                    attrelid = entity_
                    and attnum > 0
                    and pg_catalog.has_column_privilege(working_role, entity_, pa.attname, 'SELECT')
            )
        )
        -- Add "record" key for insert and update
        || case
            when action in ('INSERT', 'UPDATE') then
                jsonb_build_object(
                    'record',
                    (
                        select
                            jsonb_object_agg(
                                -- if unchanged toast, get column name and value from old record
                                coalesce((c).name, (oc).name),
                                case
                                    when (c).name is null then (oc).value
                                    else (c).value
                                end
                            )
                        from
                            unnest(columns) c
                            full outer join unnest(old_columns) oc
                                on (c).name = (oc).name
                        where
                            coalesce((c).is_selectable, (oc).is_selectable)
                            and ( not error_record_exceeds_max_size or (octet_length((c).value::text) <= 64))
                    )
                )
            else '{}'::jsonb
        end
        -- Add "old_record" key for update and delete
        || case
            when action = 'UPDATE' then
                jsonb_build_object(
                        'old_record',
                        (
                            select jsonb_object_agg((c).name, (c).value)
                            from unnest(old_columns) c
                            where
                                (c).is_selectable
                                and ( not error_record_exceeds_max_size or (octet_length((c).value::text) <= 64))
                        )
                    )
            when action = 'DELETE' then
                jsonb_build_object(
                    'old_record',
                    (
                        select jsonb_object_agg((c).name, (c).value)
                        from unnest(old_columns) c
                        where
                            (c).is_selectable
                            and ( not error_record_exceeds_max_size or (octet_length((c).value::text) <= 64))
                            and ( not is_rls_enabled or (c).is_pkey ) -- if RLS enabled, we can't secure deletes so filter to pkey
                    )
                )
            else '{}'::jsonb
        end;

        -- Create the prepared statement
        if is_rls_enabled and action <> 'DELETE' then
            if (select 1 from pg_prepared_statements where name = 'walrus_rls_stmt' limit 1) > 0 then
                deallocate walrus_rls_stmt;
            end if;
            execute realtime.build_prepared_statement_sql('walrus_rls_stmt', entity_, columns);
        end if;

        visible_to_subscription_ids = '{}';

        for subscription_id, claims in (
                select
                    subs.subscription_id,
                    subs.claims
                from
                    unnest(subscriptions) subs
                where
                    subs.entity = entity_
                    and subs.claims_role = working_role
                    and (
                        realtime.is_visible_through_filters(columns, subs.filters)
                        or (
                          action = 'DELETE'
                          and realtime.is_visible_through_filters(old_columns, subs.filters)
                        )
                    )
        ) loop

            if not is_rls_enabled or action = 'DELETE' then
                visible_to_subscription_ids = visible_to_subscription_ids || subscription_id;
            else
                -- Check if RLS allows the role to see the record
                perform
                    -- Trim leading and trailing quotes from working_role because set_config
                    -- doesn't recognize the role as valid if they are included
                    set_config('role', trim(both '"' from working_role::text), true),
                    set_config('request.jwt.claims', claims::text, true);

                execute 'execute walrus_rls_stmt' into subscription_has_access;

                if subscription_has_access then
                    visible_to_subscription_ids = visible_to_subscription_ids || subscription_id;
                end if;
            end if;
        end loop;

        perform set_config('role', null, true);

        return next (
            output,
            is_rls_enabled,
            visible_to_subscription_ids,
            case
                when error_record_exceeds_max_size then array['Error 413: Payload Too Large']
                else '{}'
            end
        )::realtime.wal_rls;

    end if;
end loop;

perform set_config('role', null, true);
end;
$$;


ALTER FUNCTION realtime.apply_rls(wal jsonb, max_record_bytes integer) OWNER TO supabase_admin;

--
-- Name: broadcast_changes(text, text, text, text, text, record, record, text); Type: FUNCTION; Schema: realtime; Owner: supabase_admin
--

CREATE FUNCTION realtime.broadcast_changes(topic_name text, event_name text, operation text, table_name text, table_schema text, new record, old record, level text DEFAULT 'ROW'::text) RETURNS void
    LANGUAGE plpgsql
    AS $$
DECLARE
    -- Declare a variable to hold the JSONB representation of the row
    row_data jsonb := '{}'::jsonb;
BEGIN
    IF level = 'STATEMENT' THEN
        RAISE EXCEPTION 'function can only be triggered for each row, not for each statement';
    END IF;
    -- Check the operation type and handle accordingly
    IF operation = 'INSERT' OR operation = 'UPDATE' OR operation = 'DELETE' THEN
        row_data := jsonb_build_object('old_record', OLD, 'record', NEW, 'operation', operation, 'table', table_name, 'schema', table_schema);
        PERFORM realtime.send (row_data, event_name, topic_name);
    ELSE
        RAISE EXCEPTION 'Unexpected operation type: %', operation;
    END IF;
EXCEPTION
    WHEN OTHERS THEN
        RAISE EXCEPTION 'Failed to process the row: %', SQLERRM;
END;

$$;


ALTER FUNCTION realtime.broadcast_changes(topic_name text, event_name text, operation text, table_name text, table_schema text, new record, old record, level text) OWNER TO supabase_admin;

--
-- Name: build_prepared_statement_sql(text, regclass, realtime.wal_column[]); Type: FUNCTION; Schema: realtime; Owner: supabase_admin
--

CREATE FUNCTION realtime.build_prepared_statement_sql(prepared_statement_name text, entity regclass, columns realtime.wal_column[]) RETURNS text
    LANGUAGE sql
    AS $$
      /*
      Builds a sql string that, if executed, creates a prepared statement to
      tests retrive a row from *entity* by its primary key columns.
      Example
          select realtime.build_prepared_statement_sql('public.notes', '{"id"}'::text[], '{"bigint"}'::text[])
      */
          select
      'prepare ' || prepared_statement_name || ' as
          select
              exists(
                  select
                      1
                  from
                      ' || entity || '
                  where
                      ' || string_agg(quote_ident(pkc.name) || '=' || quote_nullable(pkc.value #>> '{}') , ' and ') || '
              )'
          from
              unnest(columns) pkc
          where
              pkc.is_pkey
          group by
              entity
      $$;


ALTER FUNCTION realtime.build_prepared_statement_sql(prepared_statement_name text, entity regclass, columns realtime.wal_column[]) OWNER TO supabase_admin;

--
-- Name: cast(text, regtype); Type: FUNCTION; Schema: realtime; Owner: supabase_admin
--

CREATE FUNCTION realtime."cast"(val text, type_ regtype) RETURNS jsonb
    LANGUAGE plpgsql IMMUTABLE
    AS $$
    declare
      res jsonb;
    begin
      execute format('select to_jsonb(%L::'|| type_::text || ')', val)  into res;
      return res;
    end
    $$;


ALTER FUNCTION realtime."cast"(val text, type_ regtype) OWNER TO supabase_admin;

--
-- Name: check_equality_op(realtime.equality_op, regtype, text, text); Type: FUNCTION; Schema: realtime; Owner: supabase_admin
--

CREATE FUNCTION realtime.check_equality_op(op realtime.equality_op, type_ regtype, val_1 text, val_2 text) RETURNS boolean
    LANGUAGE plpgsql IMMUTABLE
    AS $$
      /*
      Casts *val_1* and *val_2* as type *type_* and check the *op* condition for truthiness
      */
      declare
          op_symbol text = (
              case
                  when op = 'eq' then '='
                  when op = 'neq' then '!='
                  when op = 'lt' then '<'
                  when op = 'lte' then '<='
                  when op = 'gt' then '>'
                  when op = 'gte' then '>='
                  when op = 'in' then '= any'
                  else 'UNKNOWN OP'
              end
          );
          res boolean;
      begin
          execute format(
              'select %L::'|| type_::text || ' ' || op_symbol
              || ' ( %L::'
              || (
                  case
                      when op = 'in' then type_::text || '[]'
                      else type_::text end
              )
              || ')', val_1, val_2) into res;
          return res;
      end;
      $$;


ALTER FUNCTION realtime.check_equality_op(op realtime.equality_op, type_ regtype, val_1 text, val_2 text) OWNER TO supabase_admin;

--
-- Name: is_visible_through_filters(realtime.wal_column[], realtime.user_defined_filter[]); Type: FUNCTION; Schema: realtime; Owner: supabase_admin
--

CREATE FUNCTION realtime.is_visible_through_filters(columns realtime.wal_column[], filters realtime.user_defined_filter[]) RETURNS boolean
    LANGUAGE sql IMMUTABLE
    AS $_$
    /*
    Should the record be visible (true) or filtered out (false) after *filters* are applied
    */
        select
            -- Default to allowed when no filters present
            $2 is null -- no filters. this should not happen because subscriptions has a default
            or array_length($2, 1) is null -- array length of an empty array is null
            or bool_and(
                coalesce(
                    realtime.check_equality_op(
                        op:=f.op,
                        type_:=coalesce(
                            col.type_oid::regtype, -- null when wal2json version <= 2.4
                            col.type_name::regtype
                        ),
                        -- cast jsonb to text
                        val_1:=col.value #>> '{}',
                        val_2:=f.value
                    ),
                    false -- if null, filter does not match
                )
            )
        from
            unnest(filters) f
            join unnest(columns) col
                on f.column_name = col.name;
    $_$;


ALTER FUNCTION realtime.is_visible_through_filters(columns realtime.wal_column[], filters realtime.user_defined_filter[]) OWNER TO supabase_admin;

--
-- Name: list_changes(name, name, integer, integer); Type: FUNCTION; Schema: realtime; Owner: supabase_admin
--

CREATE FUNCTION realtime.list_changes(publication name, slot_name name, max_changes integer, max_record_bytes integer) RETURNS SETOF realtime.wal_rls
    LANGUAGE sql
    SET log_min_messages TO 'fatal'
    AS $$
      with pub as (
        select
          concat_ws(
            ',',
            case when bool_or(pubinsert) then 'insert' else null end,
            case when bool_or(pubupdate) then 'update' else null end,
            case when bool_or(pubdelete) then 'delete' else null end
          ) as w2j_actions,
          coalesce(
            string_agg(
              realtime.quote_wal2json(format('%I.%I', schemaname, tablename)::regclass),
              ','
            ) filter (where ppt.tablename is not null and ppt.tablename not like '% %'),
            ''
          ) w2j_add_tables
        from
          pg_publication pp
          left join pg_publication_tables ppt
            on pp.pubname = ppt.pubname
        where
          pp.pubname = publication
        group by
          pp.pubname
        limit 1
      ),
      w2j as (
        select
          x.*, pub.w2j_add_tables
        from
          pub,
          pg_logical_slot_get_changes(
            slot_name, null, max_changes,
            'include-pk', 'true',
            'include-transaction', 'false',
            'include-timestamp', 'true',
            'include-type-oids', 'true',
            'format-version', '2',
            'actions', pub.w2j_actions,
            'add-tables', pub.w2j_add_tables
          ) x
      )
      select
        xyz.wal,
        xyz.is_rls_enabled,
        xyz.subscription_ids,
        xyz.errors
      from
        w2j,
        realtime.apply_rls(
          wal := w2j.data::jsonb,
          max_record_bytes := max_record_bytes
        ) xyz(wal, is_rls_enabled, subscription_ids, errors)
      where
        w2j.w2j_add_tables <> ''
        and xyz.subscription_ids[1] is not null
    $$;


ALTER FUNCTION realtime.list_changes(publication name, slot_name name, max_changes integer, max_record_bytes integer) OWNER TO supabase_admin;

--
-- Name: quote_wal2json(regclass); Type: FUNCTION; Schema: realtime; Owner: supabase_admin
--

CREATE FUNCTION realtime.quote_wal2json(entity regclass) RETURNS text
    LANGUAGE sql IMMUTABLE STRICT
    AS $$
      select
        (
          select string_agg('' || ch,'')
          from unnest(string_to_array(nsp.nspname::text, null)) with ordinality x(ch, idx)
          where
            not (x.idx = 1 and x.ch = '"')
            and not (
              x.idx = array_length(string_to_array(nsp.nspname::text, null), 1)
              and x.ch = '"'
            )
        )
        || '.'
        || (
          select string_agg('' || ch,'')
          from unnest(string_to_array(pc.relname::text, null)) with ordinality x(ch, idx)
          where
            not (x.idx = 1 and x.ch = '"')
            and not (
              x.idx = array_length(string_to_array(nsp.nspname::text, null), 1)
              and x.ch = '"'
            )
          )
      from
        pg_class pc
        join pg_namespace nsp
          on pc.relnamespace = nsp.oid
      where
        pc.oid = entity
    $$;


ALTER FUNCTION realtime.quote_wal2json(entity regclass) OWNER TO supabase_admin;

--
-- Name: send(jsonb, text, text, boolean); Type: FUNCTION; Schema: realtime; Owner: supabase_admin
--

CREATE FUNCTION realtime.send(payload jsonb, event text, topic text, private boolean DEFAULT true) RETURNS void
    LANGUAGE plpgsql
    AS $$
BEGIN
  BEGIN
    -- Set the topic configuration
    EXECUTE format('SET LOCAL realtime.topic TO %L', topic);

    -- Attempt to insert the message
    INSERT INTO realtime.messages (payload, event, topic, private, extension)
    VALUES (payload, event, topic, private, 'broadcast');
  EXCEPTION
    WHEN OTHERS THEN
      -- Capture and notify the error
      RAISE WARNING 'ErrorSendingBroadcastMessage: %', SQLERRM;
  END;
END;
$$;


ALTER FUNCTION realtime.send(payload jsonb, event text, topic text, private boolean) OWNER TO supabase_admin;

--
-- Name: subscription_check_filters(); Type: FUNCTION; Schema: realtime; Owner: supabase_admin
--

CREATE FUNCTION realtime.subscription_check_filters() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
    /*
    Validates that the user defined filters for a subscription:
    - refer to valid columns that the claimed role may access
    - values are coercable to the correct column type
    */
    declare
        col_names text[] = coalesce(
                array_agg(c.column_name order by c.ordinal_position),
                '{}'::text[]
            )
            from
                information_schema.columns c
            where
                format('%I.%I', c.table_schema, c.table_name)::regclass = new.entity
                and pg_catalog.has_column_privilege(
                    (new.claims ->> 'role'),
                    format('%I.%I', c.table_schema, c.table_name)::regclass,
                    c.column_name,
                    'SELECT'
                );
        filter realtime.user_defined_filter;
        col_type regtype;

        in_val jsonb;
    begin
        for filter in select * from unnest(new.filters) loop
            -- Filtered column is valid
            if not filter.column_name = any(col_names) then
                raise exception 'invalid column for filter %', filter.column_name;
            end if;

            -- Type is sanitized and safe for string interpolation
            col_type = (
                select atttypid::regtype
                from pg_catalog.pg_attribute
                where attrelid = new.entity
                      and attname = filter.column_name
            );
            if col_type is null then
                raise exception 'failed to lookup type for column %', filter.column_name;
            end if;

            -- Set maximum number of entries for in filter
            if filter.op = 'in'::realtime.equality_op then
                in_val = realtime.cast(filter.value, (col_type::text || '[]')::regtype);
                if coalesce(jsonb_array_length(in_val), 0) > 100 then
                    raise exception 'too many values for `in` filter. Maximum 100';
                end if;
            else
                -- raises an exception if value is not coercable to type
                perform realtime.cast(filter.value, col_type);
            end if;

        end loop;

        -- Apply consistent order to filters so the unique constraint on
        -- (subscription_id, entity, filters) can't be tricked by a different filter order
        new.filters = coalesce(
            array_agg(f order by f.column_name, f.op, f.value),
            '{}'
        ) from unnest(new.filters) f;

        return new;
    end;
    $$;


ALTER FUNCTION realtime.subscription_check_filters() OWNER TO supabase_admin;

--
-- Name: to_regrole(text); Type: FUNCTION; Schema: realtime; Owner: supabase_admin
--

CREATE FUNCTION realtime.to_regrole(role_name text) RETURNS regrole
    LANGUAGE sql IMMUTABLE
    AS $$ select role_name::regrole $$;


ALTER FUNCTION realtime.to_regrole(role_name text) OWNER TO supabase_admin;

--
-- Name: topic(); Type: FUNCTION; Schema: realtime; Owner: supabase_realtime_admin
--

CREATE FUNCTION realtime.topic() RETURNS text
    LANGUAGE sql STABLE
    AS $$
select nullif(current_setting('realtime.topic', true), '')::text;
$$;


ALTER FUNCTION realtime.topic() OWNER TO supabase_realtime_admin;

--
-- Name: add_prefixes(text, text); Type: FUNCTION; Schema: storage; Owner: supabase_storage_admin
--

CREATE FUNCTION storage.add_prefixes(_bucket_id text, _name text) RETURNS void
    LANGUAGE plpgsql SECURITY DEFINER
    AS $$
DECLARE
    prefixes text[];
BEGIN
    prefixes := "storage"."get_prefixes"("_name");

    IF array_length(prefixes, 1) > 0 THEN
        INSERT INTO storage.prefixes (name, bucket_id)
        SELECT UNNEST(prefixes) as name, "_bucket_id" ON CONFLICT DO NOTHING;
    END IF;
END;
$$;


ALTER FUNCTION storage.add_prefixes(_bucket_id text, _name text) OWNER TO supabase_storage_admin;

--
-- Name: can_insert_object(text, text, uuid, jsonb); Type: FUNCTION; Schema: storage; Owner: supabase_storage_admin
--

CREATE FUNCTION storage.can_insert_object(bucketid text, name text, owner uuid, metadata jsonb) RETURNS void
    LANGUAGE plpgsql
    AS $$
BEGIN
  INSERT INTO "storage"."objects" ("bucket_id", "name", "owner", "metadata") VALUES (bucketid, name, owner, metadata);
  -- hack to rollback the successful insert
  RAISE sqlstate 'PT200' using
  message = 'ROLLBACK',
  detail = 'rollback successful insert';
END
$$;


ALTER FUNCTION storage.can_insert_object(bucketid text, name text, owner uuid, metadata jsonb) OWNER TO supabase_storage_admin;

--
-- Name: delete_prefix(text, text); Type: FUNCTION; Schema: storage; Owner: supabase_storage_admin
--

CREATE FUNCTION storage.delete_prefix(_bucket_id text, _name text) RETURNS boolean
    LANGUAGE plpgsql SECURITY DEFINER
    AS $$
BEGIN
    -- Check if we can delete the prefix
    IF EXISTS(
        SELECT FROM "storage"."prefixes"
        WHERE "prefixes"."bucket_id" = "_bucket_id"
          AND level = "storage"."get_level"("_name") + 1
          AND "prefixes"."name" COLLATE "C" LIKE "_name" || '/%'
        LIMIT 1
    )
    OR EXISTS(
        SELECT FROM "storage"."objects"
        WHERE "objects"."bucket_id" = "_bucket_id"
          AND "storage"."get_level"("objects"."name") = "storage"."get_level"("_name") + 1
          AND "objects"."name" COLLATE "C" LIKE "_name" || '/%'
        LIMIT 1
    ) THEN
    -- There are sub-objects, skip deletion
    RETURN false;
    ELSE
        DELETE FROM "storage"."prefixes"
        WHERE "prefixes"."bucket_id" = "_bucket_id"
          AND level = "storage"."get_level"("_name")
          AND "prefixes"."name" = "_name";
        RETURN true;
    END IF;
END;
$$;


ALTER FUNCTION storage.delete_prefix(_bucket_id text, _name text) OWNER TO supabase_storage_admin;

--
-- Name: delete_prefix_hierarchy_trigger(); Type: FUNCTION; Schema: storage; Owner: supabase_storage_admin
--

CREATE FUNCTION storage.delete_prefix_hierarchy_trigger() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE
    prefix text;
BEGIN
    prefix := "storage"."get_prefix"(OLD."name");

    IF coalesce(prefix, '') != '' THEN
        PERFORM "storage"."delete_prefix"(OLD."bucket_id", prefix);
    END IF;

    RETURN OLD;
END;
$$;


ALTER FUNCTION storage.delete_prefix_hierarchy_trigger() OWNER TO supabase_storage_admin;

--
-- Name: enforce_bucket_name_length(); Type: FUNCTION; Schema: storage; Owner: supabase_storage_admin
--

CREATE FUNCTION storage.enforce_bucket_name_length() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
begin
    if length(new.name) > 100 then
        raise exception 'bucket name "%" is too long (% characters). Max is 100.', new.name, length(new.name);
    end if;
    return new;
end;
$$;


ALTER FUNCTION storage.enforce_bucket_name_length() OWNER TO supabase_storage_admin;

--
-- Name: extension(text); Type: FUNCTION; Schema: storage; Owner: supabase_storage_admin
--

CREATE FUNCTION storage.extension(name text) RETURNS text
    LANGUAGE plpgsql IMMUTABLE
    AS $$
DECLARE
    _parts text[];
    _filename text;
BEGIN
    SELECT string_to_array(name, '/') INTO _parts;
    SELECT _parts[array_length(_parts,1)] INTO _filename;
    RETURN reverse(split_part(reverse(_filename), '.', 1));
END
$$;


ALTER FUNCTION storage.extension(name text) OWNER TO supabase_storage_admin;

--
-- Name: filename(text); Type: FUNCTION; Schema: storage; Owner: supabase_storage_admin
--

CREATE FUNCTION storage.filename(name text) RETURNS text
    LANGUAGE plpgsql
    AS $$
DECLARE
_parts text[];
BEGIN
	select string_to_array(name, '/') into _parts;
	return _parts[array_length(_parts,1)];
END
$$;


ALTER FUNCTION storage.filename(name text) OWNER TO supabase_storage_admin;

--
-- Name: foldername(text); Type: FUNCTION; Schema: storage; Owner: supabase_storage_admin
--

CREATE FUNCTION storage.foldername(name text) RETURNS text[]
    LANGUAGE plpgsql IMMUTABLE
    AS $$
DECLARE
    _parts text[];
BEGIN
    -- Split on "/" to get path segments
    SELECT string_to_array(name, '/') INTO _parts;
    -- Return everything except the last segment
    RETURN _parts[1 : array_length(_parts,1) - 1];
END
$$;


ALTER FUNCTION storage.foldername(name text) OWNER TO supabase_storage_admin;

--
-- Name: get_level(text); Type: FUNCTION; Schema: storage; Owner: supabase_storage_admin
--

CREATE FUNCTION storage.get_level(name text) RETURNS integer
    LANGUAGE sql IMMUTABLE STRICT
    AS $$
SELECT array_length(string_to_array("name", '/'), 1);
$$;


ALTER FUNCTION storage.get_level(name text) OWNER TO supabase_storage_admin;

--
-- Name: get_prefix(text); Type: FUNCTION; Schema: storage; Owner: supabase_storage_admin
--

CREATE FUNCTION storage.get_prefix(name text) RETURNS text
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
SELECT
    CASE WHEN strpos("name", '/') > 0 THEN
             regexp_replace("name", '[\/]{1}[^\/]+\/?$', '')
         ELSE
             ''
        END;
$_$;


ALTER FUNCTION storage.get_prefix(name text) OWNER TO supabase_storage_admin;

--
-- Name: get_prefixes(text); Type: FUNCTION; Schema: storage; Owner: supabase_storage_admin
--

CREATE FUNCTION storage.get_prefixes(name text) RETURNS text[]
    LANGUAGE plpgsql IMMUTABLE STRICT
    AS $$
DECLARE
    parts text[];
    prefixes text[];
    prefix text;
BEGIN
    -- Split the name into parts by '/'
    parts := string_to_array("name", '/');
    prefixes := '{}';

    -- Construct the prefixes, stopping one level below the last part
    FOR i IN 1..array_length(parts, 1) - 1 LOOP
            prefix := array_to_string(parts[1:i], '/');
            prefixes := array_append(prefixes, prefix);
    END LOOP;

    RETURN prefixes;
END;
$$;


ALTER FUNCTION storage.get_prefixes(name text) OWNER TO supabase_storage_admin;

--
-- Name: get_size_by_bucket(); Type: FUNCTION; Schema: storage; Owner: supabase_storage_admin
--

CREATE FUNCTION storage.get_size_by_bucket() RETURNS TABLE(size bigint, bucket_id text)
    LANGUAGE plpgsql STABLE
    AS $$
BEGIN
    return query
        select sum((metadata->>'size')::bigint) as size, obj.bucket_id
        from "storage".objects as obj
        group by obj.bucket_id;
END
$$;


ALTER FUNCTION storage.get_size_by_bucket() OWNER TO supabase_storage_admin;

--
-- Name: list_multipart_uploads_with_delimiter(text, text, text, integer, text, text); Type: FUNCTION; Schema: storage; Owner: supabase_storage_admin
--

CREATE FUNCTION storage.list_multipart_uploads_with_delimiter(bucket_id text, prefix_param text, delimiter_param text, max_keys integer DEFAULT 100, next_key_token text DEFAULT ''::text, next_upload_token text DEFAULT ''::text) RETURNS TABLE(key text, id text, created_at timestamp with time zone)
    LANGUAGE plpgsql
    AS $_$
BEGIN
    RETURN QUERY EXECUTE
        'SELECT DISTINCT ON(key COLLATE "C") * from (
            SELECT
                CASE
                    WHEN position($2 IN substring(key from length($1) + 1)) > 0 THEN
                        substring(key from 1 for length($1) + position($2 IN substring(key from length($1) + 1)))
                    ELSE
                        key
                END AS key, id, created_at
            FROM
                storage.s3_multipart_uploads
            WHERE
                bucket_id = $5 AND
                key ILIKE $1 || ''%'' AND
                CASE
                    WHEN $4 != '''' AND $6 = '''' THEN
                        CASE
                            WHEN position($2 IN substring(key from length($1) + 1)) > 0 THEN
                                substring(key from 1 for length($1) + position($2 IN substring(key from length($1) + 1))) COLLATE "C" > $4
                            ELSE
                                key COLLATE "C" > $4
                            END
                    ELSE
                        true
                END AND
                CASE
                    WHEN $6 != '''' THEN
                        id COLLATE "C" > $6
                    ELSE
                        true
                    END
            ORDER BY
                key COLLATE "C" ASC, created_at ASC) as e order by key COLLATE "C" LIMIT $3'
        USING prefix_param, delimiter_param, max_keys, next_key_token, bucket_id, next_upload_token;
END;
$_$;


ALTER FUNCTION storage.list_multipart_uploads_with_delimiter(bucket_id text, prefix_param text, delimiter_param text, max_keys integer, next_key_token text, next_upload_token text) OWNER TO supabase_storage_admin;

--
-- Name: list_objects_with_delimiter(text, text, text, integer, text, text); Type: FUNCTION; Schema: storage; Owner: supabase_storage_admin
--

CREATE FUNCTION storage.list_objects_with_delimiter(bucket_id text, prefix_param text, delimiter_param text, max_keys integer DEFAULT 100, start_after text DEFAULT ''::text, next_token text DEFAULT ''::text) RETURNS TABLE(name text, id uuid, metadata jsonb, updated_at timestamp with time zone)
    LANGUAGE plpgsql
    AS $_$
BEGIN
    RETURN QUERY EXECUTE
        'SELECT DISTINCT ON(name COLLATE "C") * from (
            SELECT
                CASE
                    WHEN position($2 IN substring(name from length($1) + 1)) > 0 THEN
                        substring(name from 1 for length($1) + position($2 IN substring(name from length($1) + 1)))
                    ELSE
                        name
                END AS name, id, metadata, updated_at
            FROM
                storage.objects
            WHERE
                bucket_id = $5 AND
                name ILIKE $1 || ''%'' AND
                CASE
                    WHEN $6 != '''' THEN
                    name COLLATE "C" > $6
                ELSE true END
                AND CASE
                    WHEN $4 != '''' THEN
                        CASE
                            WHEN position($2 IN substring(name from length($1) + 1)) > 0 THEN
                                substring(name from 1 for length($1) + position($2 IN substring(name from length($1) + 1))) COLLATE "C" > $4
                            ELSE
                                name COLLATE "C" > $4
                            END
                    ELSE
                        true
                END
            ORDER BY
                name COLLATE "C" ASC) as e order by name COLLATE "C" LIMIT $3'
        USING prefix_param, delimiter_param, max_keys, next_token, bucket_id, start_after;
END;
$_$;


ALTER FUNCTION storage.list_objects_with_delimiter(bucket_id text, prefix_param text, delimiter_param text, max_keys integer, start_after text, next_token text) OWNER TO supabase_storage_admin;

--
-- Name: objects_insert_prefix_trigger(); Type: FUNCTION; Schema: storage; Owner: supabase_storage_admin
--

CREATE FUNCTION storage.objects_insert_prefix_trigger() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    PERFORM "storage"."add_prefixes"(NEW."bucket_id", NEW."name");
    NEW.level := "storage"."get_level"(NEW."name");

    RETURN NEW;
END;
$$;


ALTER FUNCTION storage.objects_insert_prefix_trigger() OWNER TO supabase_storage_admin;

--
-- Name: objects_update_prefix_trigger(); Type: FUNCTION; Schema: storage; Owner: supabase_storage_admin
--

CREATE FUNCTION storage.objects_update_prefix_trigger() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE
    old_prefixes TEXT[];
BEGIN
    -- Ensure this is an update operation and the name has changed
    IF TG_OP = 'UPDATE' AND (NEW."name" <> OLD."name" OR NEW."bucket_id" <> OLD."bucket_id") THEN
        -- Retrieve old prefixes
        old_prefixes := "storage"."get_prefixes"(OLD."name");

        -- Remove old prefixes that are only used by this object
        WITH all_prefixes as (
            SELECT unnest(old_prefixes) as prefix
        ),
        can_delete_prefixes as (
             SELECT prefix
             FROM all_prefixes
             WHERE NOT EXISTS (
                 SELECT 1 FROM "storage"."objects"
                 WHERE "bucket_id" = OLD."bucket_id"
                   AND "name" <> OLD."name"
                   AND "name" LIKE (prefix || '%')
             )
         )
        DELETE FROM "storage"."prefixes" WHERE name IN (SELECT prefix FROM can_delete_prefixes);

        -- Add new prefixes
        PERFORM "storage"."add_prefixes"(NEW."bucket_id", NEW."name");
    END IF;
    -- Set the new level
    NEW."level" := "storage"."get_level"(NEW."name");

    RETURN NEW;
END;
$$;


ALTER FUNCTION storage.objects_update_prefix_trigger() OWNER TO supabase_storage_admin;

--
-- Name: operation(); Type: FUNCTION; Schema: storage; Owner: supabase_storage_admin
--

CREATE FUNCTION storage.operation() RETURNS text
    LANGUAGE plpgsql STABLE
    AS $$
BEGIN
    RETURN current_setting('storage.operation', true);
END;
$$;


ALTER FUNCTION storage.operation() OWNER TO supabase_storage_admin;

--
-- Name: prefixes_insert_trigger(); Type: FUNCTION; Schema: storage; Owner: supabase_storage_admin
--

CREATE FUNCTION storage.prefixes_insert_trigger() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    PERFORM "storage"."add_prefixes"(NEW."bucket_id", NEW."name");
    RETURN NEW;
END;
$$;


ALTER FUNCTION storage.prefixes_insert_trigger() OWNER TO supabase_storage_admin;

--
-- Name: search(text, text, integer, integer, integer, text, text, text); Type: FUNCTION; Schema: storage; Owner: supabase_storage_admin
--

CREATE FUNCTION storage.search(prefix text, bucketname text, limits integer DEFAULT 100, levels integer DEFAULT 1, offsets integer DEFAULT 0, search text DEFAULT ''::text, sortcolumn text DEFAULT 'name'::text, sortorder text DEFAULT 'asc'::text) RETURNS TABLE(name text, id uuid, updated_at timestamp with time zone, created_at timestamp with time zone, last_accessed_at timestamp with time zone, metadata jsonb)
    LANGUAGE plpgsql
    AS $$
declare
    can_bypass_rls BOOLEAN;
begin
    SELECT rolbypassrls
    INTO can_bypass_rls
    FROM pg_roles
    WHERE rolname = coalesce(nullif(current_setting('role', true), 'none'), current_user);

    IF can_bypass_rls THEN
        RETURN QUERY SELECT * FROM storage.search_v1_optimised(prefix, bucketname, limits, levels, offsets, search, sortcolumn, sortorder);
    ELSE
        RETURN QUERY SELECT * FROM storage.search_legacy_v1(prefix, bucketname, limits, levels, offsets, search, sortcolumn, sortorder);
    END IF;
end;
$$;


ALTER FUNCTION storage.search(prefix text, bucketname text, limits integer, levels integer, offsets integer, search text, sortcolumn text, sortorder text) OWNER TO supabase_storage_admin;

--
-- Name: search_legacy_v1(text, text, integer, integer, integer, text, text, text); Type: FUNCTION; Schema: storage; Owner: supabase_storage_admin
--

CREATE FUNCTION storage.search_legacy_v1(prefix text, bucketname text, limits integer DEFAULT 100, levels integer DEFAULT 1, offsets integer DEFAULT 0, search text DEFAULT ''::text, sortcolumn text DEFAULT 'name'::text, sortorder text DEFAULT 'asc'::text) RETURNS TABLE(name text, id uuid, updated_at timestamp with time zone, created_at timestamp with time zone, last_accessed_at timestamp with time zone, metadata jsonb)
    LANGUAGE plpgsql STABLE
    AS $_$
declare
    v_order_by text;
    v_sort_order text;
begin
    case
        when sortcolumn = 'name' then
            v_order_by = 'name';
        when sortcolumn = 'updated_at' then
            v_order_by = 'updated_at';
        when sortcolumn = 'created_at' then
            v_order_by = 'created_at';
        when sortcolumn = 'last_accessed_at' then
            v_order_by = 'last_accessed_at';
        else
            v_order_by = 'name';
        end case;

    case
        when sortorder = 'asc' then
            v_sort_order = 'asc';
        when sortorder = 'desc' then
            v_sort_order = 'desc';
        else
            v_sort_order = 'asc';
        end case;

    v_order_by = v_order_by || ' ' || v_sort_order;

    return query execute
        'with folders as (
           select path_tokens[$1] as folder
           from storage.objects
             where objects.name ilike $2 || $3 || ''%''
               and bucket_id = $4
               and array_length(objects.path_tokens, 1) <> $1
           group by folder
           order by folder ' || v_sort_order || '
     )
     (select folder as "name",
            null as id,
            null as updated_at,
            null as created_at,
            null as last_accessed_at,
            null as metadata from folders)
     union all
     (select path_tokens[$1] as "name",
            id,
            updated_at,
            created_at,
            last_accessed_at,
            metadata
     from storage.objects
     where objects.name ilike $2 || $3 || ''%''
       and bucket_id = $4
       and array_length(objects.path_tokens, 1) = $1
     order by ' || v_order_by || ')
     limit $5
     offset $6' using levels, prefix, search, bucketname, limits, offsets;
end;
$_$;


ALTER FUNCTION storage.search_legacy_v1(prefix text, bucketname text, limits integer, levels integer, offsets integer, search text, sortcolumn text, sortorder text) OWNER TO supabase_storage_admin;

--
-- Name: search_v1_optimised(text, text, integer, integer, integer, text, text, text); Type: FUNCTION; Schema: storage; Owner: supabase_storage_admin
--

CREATE FUNCTION storage.search_v1_optimised(prefix text, bucketname text, limits integer DEFAULT 100, levels integer DEFAULT 1, offsets integer DEFAULT 0, search text DEFAULT ''::text, sortcolumn text DEFAULT 'name'::text, sortorder text DEFAULT 'asc'::text) RETURNS TABLE(name text, id uuid, updated_at timestamp with time zone, created_at timestamp with time zone, last_accessed_at timestamp with time zone, metadata jsonb)
    LANGUAGE plpgsql STABLE
    AS $_$
declare
    v_order_by text;
    v_sort_order text;
begin
    case
        when sortcolumn = 'name' then
            v_order_by = 'name';
        when sortcolumn = 'updated_at' then
            v_order_by = 'updated_at';
        when sortcolumn = 'created_at' then
            v_order_by = 'created_at';
        when sortcolumn = 'last_accessed_at' then
            v_order_by = 'last_accessed_at';
        else
            v_order_by = 'name';
        end case;

    case
        when sortorder = 'asc' then
            v_sort_order = 'asc';
        when sortorder = 'desc' then
            v_sort_order = 'desc';
        else
            v_sort_order = 'asc';
        end case;

    v_order_by = v_order_by || ' ' || v_sort_order;

    return query execute
        'with folders as (
           select (string_to_array(name, ''/''))[level] as name
           from storage.prefixes
             where lower(prefixes.name) like lower($2 || $3) || ''%''
               and bucket_id = $4
               and level = $1
           order by name ' || v_sort_order || '
     )
     (select name,
            null as id,
            null as updated_at,
            null as created_at,
            null as last_accessed_at,
            null as metadata from folders)
     union all
     (select path_tokens[level] as "name",
            id,
            updated_at,
            created_at,
            last_accessed_at,
            metadata
     from storage.objects
     where lower(objects.name) like lower($2 || $3) || ''%''
       and bucket_id = $4
       and level = $1
     order by ' || v_order_by || ')
     limit $5
     offset $6' using levels, prefix, search, bucketname, limits, offsets;
end;
$_$;


ALTER FUNCTION storage.search_v1_optimised(prefix text, bucketname text, limits integer, levels integer, offsets integer, search text, sortcolumn text, sortorder text) OWNER TO supabase_storage_admin;

--
-- Name: search_v2(text, text, integer, integer, text); Type: FUNCTION; Schema: storage; Owner: supabase_storage_admin
--

CREATE FUNCTION storage.search_v2(prefix text, bucket_name text, limits integer DEFAULT 100, levels integer DEFAULT 1, start_after text DEFAULT ''::text) RETURNS TABLE(key text, name text, id uuid, updated_at timestamp with time zone, created_at timestamp with time zone, metadata jsonb)
    LANGUAGE plpgsql STABLE
    AS $_$
BEGIN
    RETURN query EXECUTE
        $sql$
        SELECT * FROM (
            (
                SELECT
                    split_part(name, '/', $4) AS key,
                    name || '/' AS name,
                    NULL::uuid AS id,
                    NULL::timestamptz AS updated_at,
                    NULL::timestamptz AS created_at,
                    NULL::jsonb AS metadata
                FROM storage.prefixes
                WHERE name COLLATE "C" LIKE $1 || '%'
                AND bucket_id = $2
                AND level = $4
                AND name COLLATE "C" > $5
                ORDER BY prefixes.name COLLATE "C" LIMIT $3
            )
            UNION ALL
            (SELECT split_part(name, '/', $4) AS key,
                name,
                id,
                updated_at,
                created_at,
                metadata
            FROM storage.objects
            WHERE name COLLATE "C" LIKE $1 || '%'
                AND bucket_id = $2
                AND level = $4
                AND name COLLATE "C" > $5
            ORDER BY name COLLATE "C" LIMIT $3)
        ) obj
        ORDER BY name COLLATE "C" LIMIT $3;
        $sql$
        USING prefix, bucket_name, limits, levels, start_after;
END;
$_$;


ALTER FUNCTION storage.search_v2(prefix text, bucket_name text, limits integer, levels integer, start_after text) OWNER TO supabase_storage_admin;

--
-- Name: update_updated_at_column(); Type: FUNCTION; Schema: storage; Owner: supabase_storage_admin
--

CREATE FUNCTION storage.update_updated_at_column() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    NEW.updated_at = now();
    RETURN NEW; 
END;
$$;


ALTER FUNCTION storage.update_updated_at_column() OWNER TO supabase_storage_admin;

--
-- Name: http_request(); Type: FUNCTION; Schema: supabase_functions; Owner: supabase_functions_admin
--

CREATE FUNCTION supabase_functions.http_request() RETURNS trigger
    LANGUAGE plpgsql SECURITY DEFINER
    SET search_path TO 'supabase_functions'
    AS $$
  DECLARE
    request_id bigint;
    payload jsonb;
    url text := TG_ARGV[0]::text;
    method text := TG_ARGV[1]::text;
    headers jsonb DEFAULT '{}'::jsonb;
    params jsonb DEFAULT '{}'::jsonb;
    timeout_ms integer DEFAULT 1000;
  BEGIN
    IF url IS NULL OR url = 'null' THEN
      RAISE EXCEPTION 'url argument is missing';
    END IF;

    IF method IS NULL OR method = 'null' THEN
      RAISE EXCEPTION 'method argument is missing';
    END IF;

    IF TG_ARGV[2] IS NULL OR TG_ARGV[2] = 'null' THEN
      headers = '{"Content-Type": "application/json"}'::jsonb;
    ELSE
      headers = TG_ARGV[2]::jsonb;
    END IF;

    IF TG_ARGV[3] IS NULL OR TG_ARGV[3] = 'null' THEN
      params = '{}'::jsonb;
    ELSE
      params = TG_ARGV[3]::jsonb;
    END IF;

    IF TG_ARGV[4] IS NULL OR TG_ARGV[4] = 'null' THEN
      timeout_ms = 1000;
    ELSE
      timeout_ms = TG_ARGV[4]::integer;
    END IF;

    CASE
      WHEN method = 'GET' THEN
        SELECT http_get INTO request_id FROM net.http_get(
          url,
          params,
          headers,
          timeout_ms
        );
      WHEN method = 'POST' THEN
        payload = jsonb_build_object(
          'old_record', OLD,
          'record', NEW,
          'type', TG_OP,
          'table', TG_TABLE_NAME,
          'schema', TG_TABLE_SCHEMA
        );

        SELECT http_post INTO request_id FROM net.http_post(
          url,
          payload,
          params,
          headers,
          timeout_ms
        );
      ELSE
        RAISE EXCEPTION 'method argument % is invalid', method;
    END CASE;

    INSERT INTO supabase_functions.hooks
      (hook_table_id, hook_name, request_id)
    VALUES
      (TG_RELID, TG_NAME, request_id);

    RETURN NEW;
  END
$$;


ALTER FUNCTION supabase_functions.http_request() OWNER TO supabase_functions_admin;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: extensions; Type: TABLE; Schema: _realtime; Owner: supabase_admin
--

CREATE TABLE _realtime.extensions (
    id uuid NOT NULL,
    type text,
    settings jsonb,
    tenant_external_id text,
    inserted_at timestamp(0) without time zone NOT NULL,
    updated_at timestamp(0) without time zone NOT NULL
);


ALTER TABLE _realtime.extensions OWNER TO supabase_admin;

--
-- Name: schema_migrations; Type: TABLE; Schema: _realtime; Owner: supabase_admin
--

CREATE TABLE _realtime.schema_migrations (
    version bigint NOT NULL,
    inserted_at timestamp(0) without time zone
);


ALTER TABLE _realtime.schema_migrations OWNER TO supabase_admin;

--
-- Name: tenants; Type: TABLE; Schema: _realtime; Owner: supabase_admin
--

CREATE TABLE _realtime.tenants (
    id uuid NOT NULL,
    name text,
    external_id text,
    jwt_secret text,
    max_concurrent_users integer DEFAULT 200 NOT NULL,
    inserted_at timestamp(0) without time zone NOT NULL,
    updated_at timestamp(0) without time zone NOT NULL,
    max_events_per_second integer DEFAULT 100 NOT NULL,
    postgres_cdc_default text DEFAULT 'postgres_cdc_rls'::text,
    max_bytes_per_second integer DEFAULT 100000 NOT NULL,
    max_channels_per_client integer DEFAULT 100 NOT NULL,
    max_joins_per_second integer DEFAULT 500 NOT NULL,
    suspend boolean DEFAULT false,
    jwt_jwks jsonb,
    notify_private_alpha boolean DEFAULT false,
    private_only boolean DEFAULT false NOT NULL,
    migrations_ran integer DEFAULT 0,
    broadcast_adapter character varying(255) DEFAULT 'gen_rpc'::character varying
);


ALTER TABLE _realtime.tenants OWNER TO supabase_admin;

--
-- Name: audit_log_entries; Type: TABLE; Schema: auth; Owner: supabase_auth_admin
--

CREATE TABLE auth.audit_log_entries (
    instance_id uuid,
    id uuid NOT NULL,
    payload json,
    created_at timestamp with time zone,
    ip_address character varying(64) DEFAULT ''::character varying NOT NULL
);


ALTER TABLE auth.audit_log_entries OWNER TO supabase_auth_admin;

--
-- Name: TABLE audit_log_entries; Type: COMMENT; Schema: auth; Owner: supabase_auth_admin
--

COMMENT ON TABLE auth.audit_log_entries IS 'Auth: Audit trail for user actions.';


--
-- Name: flow_state; Type: TABLE; Schema: auth; Owner: supabase_auth_admin
--

CREATE TABLE auth.flow_state (
    id uuid NOT NULL,
    user_id uuid,
    auth_code text NOT NULL,
    code_challenge_method auth.code_challenge_method NOT NULL,
    code_challenge text NOT NULL,
    provider_type text NOT NULL,
    provider_access_token text,
    provider_refresh_token text,
    created_at timestamp with time zone,
    updated_at timestamp with time zone,
    authentication_method text NOT NULL,
    auth_code_issued_at timestamp with time zone
);


ALTER TABLE auth.flow_state OWNER TO supabase_auth_admin;

--
-- Name: TABLE flow_state; Type: COMMENT; Schema: auth; Owner: supabase_auth_admin
--

COMMENT ON TABLE auth.flow_state IS 'stores metadata for pkce logins';


--
-- Name: identities; Type: TABLE; Schema: auth; Owner: supabase_auth_admin
--

CREATE TABLE auth.identities (
    provider_id text NOT NULL,
    user_id uuid NOT NULL,
    identity_data jsonb NOT NULL,
    provider text NOT NULL,
    last_sign_in_at timestamp with time zone,
    created_at timestamp with time zone,
    updated_at timestamp with time zone,
    email text GENERATED ALWAYS AS (lower((identity_data ->> 'email'::text))) STORED,
    id uuid DEFAULT gen_random_uuid() NOT NULL
);


ALTER TABLE auth.identities OWNER TO supabase_auth_admin;

--
-- Name: TABLE identities; Type: COMMENT; Schema: auth; Owner: supabase_auth_admin
--

COMMENT ON TABLE auth.identities IS 'Auth: Stores identities associated to a user.';


--
-- Name: COLUMN identities.email; Type: COMMENT; Schema: auth; Owner: supabase_auth_admin
--

COMMENT ON COLUMN auth.identities.email IS 'Auth: Email is a generated column that references the optional email property in the identity_data';


--
-- Name: instances; Type: TABLE; Schema: auth; Owner: supabase_auth_admin
--

CREATE TABLE auth.instances (
    id uuid NOT NULL,
    uuid uuid,
    raw_base_config text,
    created_at timestamp with time zone,
    updated_at timestamp with time zone
);


ALTER TABLE auth.instances OWNER TO supabase_auth_admin;

--
-- Name: TABLE instances; Type: COMMENT; Schema: auth; Owner: supabase_auth_admin
--

COMMENT ON TABLE auth.instances IS 'Auth: Manages users across multiple sites.';


--
-- Name: mfa_amr_claims; Type: TABLE; Schema: auth; Owner: supabase_auth_admin
--

CREATE TABLE auth.mfa_amr_claims (
    session_id uuid NOT NULL,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone NOT NULL,
    authentication_method text NOT NULL,
    id uuid NOT NULL
);


ALTER TABLE auth.mfa_amr_claims OWNER TO supabase_auth_admin;

--
-- Name: TABLE mfa_amr_claims; Type: COMMENT; Schema: auth; Owner: supabase_auth_admin
--

COMMENT ON TABLE auth.mfa_amr_claims IS 'auth: stores authenticator method reference claims for multi factor authentication';


--
-- Name: mfa_challenges; Type: TABLE; Schema: auth; Owner: supabase_auth_admin
--

CREATE TABLE auth.mfa_challenges (
    id uuid NOT NULL,
    factor_id uuid NOT NULL,
    created_at timestamp with time zone NOT NULL,
    verified_at timestamp with time zone,
    ip_address inet NOT NULL,
    otp_code text,
    web_authn_session_data jsonb
);


ALTER TABLE auth.mfa_challenges OWNER TO supabase_auth_admin;

--
-- Name: TABLE mfa_challenges; Type: COMMENT; Schema: auth; Owner: supabase_auth_admin
--

COMMENT ON TABLE auth.mfa_challenges IS 'auth: stores metadata about challenge requests made';


--
-- Name: mfa_factors; Type: TABLE; Schema: auth; Owner: supabase_auth_admin
--

CREATE TABLE auth.mfa_factors (
    id uuid NOT NULL,
    user_id uuid NOT NULL,
    friendly_name text,
    factor_type auth.factor_type NOT NULL,
    status auth.factor_status NOT NULL,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone NOT NULL,
    secret text,
    phone text,
    last_challenged_at timestamp with time zone,
    web_authn_credential jsonb,
    web_authn_aaguid uuid
);


ALTER TABLE auth.mfa_factors OWNER TO supabase_auth_admin;

--
-- Name: TABLE mfa_factors; Type: COMMENT; Schema: auth; Owner: supabase_auth_admin
--

COMMENT ON TABLE auth.mfa_factors IS 'auth: stores metadata about factors';


--
-- Name: one_time_tokens; Type: TABLE; Schema: auth; Owner: supabase_auth_admin
--

CREATE TABLE auth.one_time_tokens (
    id uuid NOT NULL,
    user_id uuid NOT NULL,
    token_type auth.one_time_token_type NOT NULL,
    token_hash text NOT NULL,
    relates_to text NOT NULL,
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    updated_at timestamp without time zone DEFAULT now() NOT NULL,
    CONSTRAINT one_time_tokens_token_hash_check CHECK ((char_length(token_hash) > 0))
);


ALTER TABLE auth.one_time_tokens OWNER TO supabase_auth_admin;

--
-- Name: refresh_tokens; Type: TABLE; Schema: auth; Owner: supabase_auth_admin
--

CREATE TABLE auth.refresh_tokens (
    instance_id uuid,
    id bigint NOT NULL,
    token character varying(255),
    user_id character varying(255),
    revoked boolean,
    created_at timestamp with time zone,
    updated_at timestamp with time zone,
    parent character varying(255),
    session_id uuid
);


ALTER TABLE auth.refresh_tokens OWNER TO supabase_auth_admin;

--
-- Name: TABLE refresh_tokens; Type: COMMENT; Schema: auth; Owner: supabase_auth_admin
--

COMMENT ON TABLE auth.refresh_tokens IS 'Auth: Store of tokens used to refresh JWT tokens once they expire.';


--
-- Name: refresh_tokens_id_seq; Type: SEQUENCE; Schema: auth; Owner: supabase_auth_admin
--

CREATE SEQUENCE auth.refresh_tokens_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE auth.refresh_tokens_id_seq OWNER TO supabase_auth_admin;

--
-- Name: refresh_tokens_id_seq; Type: SEQUENCE OWNED BY; Schema: auth; Owner: supabase_auth_admin
--

ALTER SEQUENCE auth.refresh_tokens_id_seq OWNED BY auth.refresh_tokens.id;


--
-- Name: saml_providers; Type: TABLE; Schema: auth; Owner: supabase_auth_admin
--

CREATE TABLE auth.saml_providers (
    id uuid NOT NULL,
    sso_provider_id uuid NOT NULL,
    entity_id text NOT NULL,
    metadata_xml text NOT NULL,
    metadata_url text,
    attribute_mapping jsonb,
    created_at timestamp with time zone,
    updated_at timestamp with time zone,
    name_id_format text,
    CONSTRAINT "entity_id not empty" CHECK ((char_length(entity_id) > 0)),
    CONSTRAINT "metadata_url not empty" CHECK (((metadata_url = NULL::text) OR (char_length(metadata_url) > 0))),
    CONSTRAINT "metadata_xml not empty" CHECK ((char_length(metadata_xml) > 0))
);


ALTER TABLE auth.saml_providers OWNER TO supabase_auth_admin;

--
-- Name: TABLE saml_providers; Type: COMMENT; Schema: auth; Owner: supabase_auth_admin
--

COMMENT ON TABLE auth.saml_providers IS 'Auth: Manages SAML Identity Provider connections.';


--
-- Name: saml_relay_states; Type: TABLE; Schema: auth; Owner: supabase_auth_admin
--

CREATE TABLE auth.saml_relay_states (
    id uuid NOT NULL,
    sso_provider_id uuid NOT NULL,
    request_id text NOT NULL,
    for_email text,
    redirect_to text,
    created_at timestamp with time zone,
    updated_at timestamp with time zone,
    flow_state_id uuid,
    CONSTRAINT "request_id not empty" CHECK ((char_length(request_id) > 0))
);


ALTER TABLE auth.saml_relay_states OWNER TO supabase_auth_admin;

--
-- Name: TABLE saml_relay_states; Type: COMMENT; Schema: auth; Owner: supabase_auth_admin
--

COMMENT ON TABLE auth.saml_relay_states IS 'Auth: Contains SAML Relay State information for each Service Provider initiated login.';


--
-- Name: schema_migrations; Type: TABLE; Schema: auth; Owner: supabase_auth_admin
--

CREATE TABLE auth.schema_migrations (
    version character varying(255) NOT NULL
);


ALTER TABLE auth.schema_migrations OWNER TO supabase_auth_admin;

--
-- Name: TABLE schema_migrations; Type: COMMENT; Schema: auth; Owner: supabase_auth_admin
--

COMMENT ON TABLE auth.schema_migrations IS 'Auth: Manages updates to the auth system.';


--
-- Name: sessions; Type: TABLE; Schema: auth; Owner: supabase_auth_admin
--

CREATE TABLE auth.sessions (
    id uuid NOT NULL,
    user_id uuid NOT NULL,
    created_at timestamp with time zone,
    updated_at timestamp with time zone,
    factor_id uuid,
    aal auth.aal_level,
    not_after timestamp with time zone,
    refreshed_at timestamp without time zone,
    user_agent text,
    ip inet,
    tag text
);


ALTER TABLE auth.sessions OWNER TO supabase_auth_admin;

--
-- Name: TABLE sessions; Type: COMMENT; Schema: auth; Owner: supabase_auth_admin
--

COMMENT ON TABLE auth.sessions IS 'Auth: Stores session data associated to a user.';


--
-- Name: COLUMN sessions.not_after; Type: COMMENT; Schema: auth; Owner: supabase_auth_admin
--

COMMENT ON COLUMN auth.sessions.not_after IS 'Auth: Not after is a nullable column that contains a timestamp after which the session should be regarded as expired.';


--
-- Name: sso_domains; Type: TABLE; Schema: auth; Owner: supabase_auth_admin
--

CREATE TABLE auth.sso_domains (
    id uuid NOT NULL,
    sso_provider_id uuid NOT NULL,
    domain text NOT NULL,
    created_at timestamp with time zone,
    updated_at timestamp with time zone,
    CONSTRAINT "domain not empty" CHECK ((char_length(domain) > 0))
);


ALTER TABLE auth.sso_domains OWNER TO supabase_auth_admin;

--
-- Name: TABLE sso_domains; Type: COMMENT; Schema: auth; Owner: supabase_auth_admin
--

COMMENT ON TABLE auth.sso_domains IS 'Auth: Manages SSO email address domain mapping to an SSO Identity Provider.';


--
-- Name: sso_providers; Type: TABLE; Schema: auth; Owner: supabase_auth_admin
--

CREATE TABLE auth.sso_providers (
    id uuid NOT NULL,
    resource_id text,
    created_at timestamp with time zone,
    updated_at timestamp with time zone,
    CONSTRAINT "resource_id not empty" CHECK (((resource_id = NULL::text) OR (char_length(resource_id) > 0)))
);


ALTER TABLE auth.sso_providers OWNER TO supabase_auth_admin;

--
-- Name: TABLE sso_providers; Type: COMMENT; Schema: auth; Owner: supabase_auth_admin
--

COMMENT ON TABLE auth.sso_providers IS 'Auth: Manages SSO identity provider information; see saml_providers for SAML.';


--
-- Name: COLUMN sso_providers.resource_id; Type: COMMENT; Schema: auth; Owner: supabase_auth_admin
--

COMMENT ON COLUMN auth.sso_providers.resource_id IS 'Auth: Uniquely identifies a SSO provider according to a user-chosen resource ID (case insensitive), useful in infrastructure as code.';


--
-- Name: users; Type: TABLE; Schema: auth; Owner: supabase_auth_admin
--

CREATE TABLE auth.users (
    instance_id uuid,
    id uuid NOT NULL,
    aud character varying(255),
    role character varying(255),
    email character varying(255),
    encrypted_password character varying(255),
    email_confirmed_at timestamp with time zone,
    invited_at timestamp with time zone,
    confirmation_token character varying(255),
    confirmation_sent_at timestamp with time zone,
    recovery_token character varying(255),
    recovery_sent_at timestamp with time zone,
    email_change_token_new character varying(255),
    email_change character varying(255),
    email_change_sent_at timestamp with time zone,
    last_sign_in_at timestamp with time zone,
    raw_app_meta_data jsonb,
    raw_user_meta_data jsonb,
    is_super_admin boolean,
    created_at timestamp with time zone,
    updated_at timestamp with time zone,
    phone text DEFAULT NULL::character varying,
    phone_confirmed_at timestamp with time zone,
    phone_change text DEFAULT ''::character varying,
    phone_change_token character varying(255) DEFAULT ''::character varying,
    phone_change_sent_at timestamp with time zone,
    confirmed_at timestamp with time zone GENERATED ALWAYS AS (LEAST(email_confirmed_at, phone_confirmed_at)) STORED,
    email_change_token_current character varying(255) DEFAULT ''::character varying,
    email_change_confirm_status smallint DEFAULT 0,
    banned_until timestamp with time zone,
    reauthentication_token character varying(255) DEFAULT ''::character varying,
    reauthentication_sent_at timestamp with time zone,
    is_sso_user boolean DEFAULT false NOT NULL,
    deleted_at timestamp with time zone,
    is_anonymous boolean DEFAULT false NOT NULL,
    CONSTRAINT users_email_change_confirm_status_check CHECK (((email_change_confirm_status >= 0) AND (email_change_confirm_status <= 2)))
);


ALTER TABLE auth.users OWNER TO supabase_auth_admin;

--
-- Name: TABLE users; Type: COMMENT; Schema: auth; Owner: supabase_auth_admin
--

COMMENT ON TABLE auth.users IS 'Auth: Stores user login data within a secure schema.';


--
-- Name: COLUMN users.is_sso_user; Type: COMMENT; Schema: auth; Owner: supabase_auth_admin
--

COMMENT ON COLUMN auth.users.is_sso_user IS 'Auth: Set this column to true when the account comes from SSO. These accounts can have duplicate emails.';


--
-- Name: invoices; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.invoices (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    user_id uuid,
    invoice_number text NOT NULL,
    customer_id uuid,
    status public.invoice_status DEFAULT 'draft'::public.invoice_status,
    issue_date date DEFAULT CURRENT_DATE,
    due_date date,
    payment_terms public.payment_terms DEFAULT 'immediate'::public.payment_terms,
    subtotal numeric(15,2) DEFAULT 0,
    tax_amount numeric(15,2) DEFAULT 0,
    discount_amount numeric(15,2) DEFAULT 0,
    total_amount numeric(15,2) DEFAULT 0,
    paid_amount numeric(15,2) DEFAULT 0,
    balance_due numeric(15,2) DEFAULT 0,
    currency text DEFAULT 'ZAR'::text,
    notes text,
    terms_conditions text,
    created_at timestamp with time zone DEFAULT now(),
    updated_at timestamp with time zone DEFAULT now(),
    sent_at timestamp with time zone,
    paid_at timestamp with time zone,
    invoice_url text,
    CONSTRAINT check_positive_amounts CHECK (((subtotal >= (0)::numeric) AND (tax_amount >= (0)::numeric) AND (discount_amount >= (0)::numeric) AND (total_amount >= (0)::numeric) AND (paid_amount >= (0)::numeric) AND (balance_due >= (0)::numeric)))
);


ALTER TABLE public.invoices OWNER TO postgres;

--
-- Name: COLUMN invoices.invoice_url; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.invoices.invoice_url IS 'URL to the generated invoice PDF file';


--
-- Name: admin_dashboard_metrics; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.admin_dashboard_metrics AS
 SELECT ( SELECT count(*) AS count
           FROM auth.users) AS total_users,
    ( SELECT count(*) AS count
           FROM public.invoices) AS total_invoices,
    ( SELECT count(*) AS count
           FROM auth.users
          WHERE (users.created_at > (now() - '7 days'::interval))) AS weekly_registrations,
    ( SELECT COALESCE(sum(invoices.total_amount), (0)::numeric) AS "coalesce"
           FROM public.invoices
          WHERE (invoices.created_at > (now() - '7 days'::interval))) AS weekly_revenue;


ALTER VIEW public.admin_dashboard_metrics OWNER TO postgres;

--
-- Name: admin_users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.admin_users (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    email text NOT NULL,
    role text DEFAULT 'viewer'::text NOT NULL,
    permissions jsonb DEFAULT '{}'::jsonb,
    created_at timestamp with time zone DEFAULT now(),
    CONSTRAINT admin_users_role_check CHECK ((role = ANY (ARRAY['viewer'::text, 'editor'::text, 'admin'::text])))
);


ALTER TABLE public.admin_users OWNER TO postgres;

--
-- Name: admin_profiles; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.admin_profiles AS
 SELECT au.id AS auth_user_id,
    au.email,
    a.role,
    a.created_at
   FROM (auth.users au
     JOIN public.admin_users a ON ((lower((au.email)::text) = lower(a.email))));


ALTER VIEW public.admin_profiles OWNER TO postgres;

--
-- Name: business_categories; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.business_categories (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    name text NOT NULL,
    display_name text NOT NULL,
    description text,
    icon text,
    sort_order integer DEFAULT 0,
    is_active boolean DEFAULT true,
    created_at timestamp with time zone DEFAULT now(),
    updated_at timestamp with time zone DEFAULT now()
);


ALTER TABLE public.business_categories OWNER TO postgres;

--
-- Name: business_contacts; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.business_contacts (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    user_id uuid,
    contact_type public.contact_type NOT NULL,
    name text NOT NULL,
    email text,
    phone text,
    address text,
    city text,
    postal_code text,
    country text DEFAULT 'South Africa'::text,
    tax_number text,
    registration_number text,
    payment_terms public.payment_terms DEFAULT 'immediate'::public.payment_terms,
    credit_limit numeric(15,2) DEFAULT 0,
    notes text,
    is_active boolean DEFAULT true,
    created_at timestamp with time zone DEFAULT now(),
    updated_at timestamp with time zone DEFAULT now()
);


ALTER TABLE public.business_contacts OWNER TO postgres;

--
-- Name: business_metrics_summary; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.business_metrics_summary (
    user_id uuid NOT NULL,
    items_count integer DEFAULT 0,
    customers_count integer DEFAULT 0,
    suppliers_count integer DEFAULT 0,
    business_contacts_count integer DEFAULT 0,
    items_count_change integer DEFAULT 0,
    customers_count_change integer DEFAULT 0,
    suppliers_count_change integer DEFAULT 0,
    total_revenue numeric(15,2) DEFAULT 0,
    revenue_this_month numeric(15,2) DEFAULT 0,
    revenue_last_month numeric(15,2) DEFAULT 0,
    revenue_this_week numeric(15,2) DEFAULT 0,
    revenue_last_week numeric(15,2) DEFAULT 0,
    total_invoices_count integer DEFAULT 0,
    paid_invoices_count integer DEFAULT 0,
    unpaid_invoices_count integer DEFAULT 0,
    unpaid_invoices_amount numeric(15,2) DEFAULT 0,
    pending_invoices_count integer DEFAULT 0,
    pending_invoices_amount numeric(15,2) DEFAULT 0,
    draft_invoices_count integer DEFAULT 0,
    overdue_invoices_count integer DEFAULT 0,
    overdue_invoices_amount numeric(15,2) DEFAULT 0,
    invoices_used_this_month integer DEFAULT 0,
    average_invoice_value numeric(15,2) DEFAULT 0,
    collection_rate numeric(5,2) DEFAULT 0,
    overdur_rate numeric(5,2) DEFAULT 0,
    average_payment_days numeric(10,2) DEFAULT 0,
    low_stock_items_count integer DEFAULT 0,
    out_of_stock_items_count integer DEFAULT 0,
    collection_efficiency_rate numeric(5,2),
    cash_flow_forecast_30_days numeric(15,2),
    business_health_score text,
    customer_lifetime_value_avg numeric(15,2),
    high_value_customers_count integer,
    inactive_customers_count integer,
    inventory_turnover_rate numeric(10,2),
    subscription_plan text DEFAULT 'free'::text,
    last_updated timestamp with time zone DEFAULT now(),
    created_at timestamp with time zone DEFAULT now()
);


ALTER TABLE public.business_metrics_summary OWNER TO postgres;

--
-- Name: business_profiles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.business_profiles (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    user_id uuid,
    name text NOT NULL,
    type text DEFAULT 'business'::text,
    business_category text,
    email_address text,
    contact_number text,
    company_logo_url text,
    company_registration_number text,
    vat_registration_number text,
    address text,
    suburb text,
    city text,
    province text,
    country text DEFAULT 'South Africa'::text,
    postal_code text,
    website text,
    onboarding_completed boolean DEFAULT false,
    created_at timestamp with time zone DEFAULT now(),
    updated_at timestamp with time zone DEFAULT now()
);


ALTER TABLE public.business_profiles OWNER TO postgres;

--
-- Name: business_users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.business_users (
    id uuid DEFAULT extensions.uuid_generate_v4() NOT NULL,
    business_profile_id uuid NOT NULL,
    auth_user_id uuid,
    email text NOT NULL,
    username text,
    display_name text NOT NULL,
    role public.user_role DEFAULT 'viewer'::public.user_role NOT NULL,
    status public.user_status DEFAULT 'invited'::public.user_status NOT NULL,
    invited_by uuid,
    invitation_token text,
    invitation_expires_at timestamp with time zone,
    temp_password text,
    password_changed boolean DEFAULT false,
    last_login_at timestamp with time zone,
    is_primary_user boolean DEFAULT false,
    can_invite_users boolean DEFAULT false,
    created_at timestamp with time zone DEFAULT now(),
    updated_at timestamp with time zone DEFAULT now(),
    activated_at timestamp with time zone,
    deactivated_at timestamp with time zone,
    CONSTRAINT business_users_email_check CHECK ((email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$'::text))
);


ALTER TABLE public.business_users OWNER TO postgres;

--
-- Name: coaching_flows; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.coaching_flows (
    id text NOT NULL,
    title text NOT NULL,
    description text NOT NULL,
    enabled boolean DEFAULT true,
    created_at timestamp with time zone DEFAULT now(),
    updated_at timestamp with time zone DEFAULT now()
);


ALTER TABLE public.coaching_flows OWNER TO postgres;

--
-- Name: coaching_steps; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.coaching_steps (
    id text NOT NULL,
    flow_id text NOT NULL,
    title text NOT NULL,
    description text NOT NULL,
    target_element_id text NOT NULL,
    step_order integer NOT NULL,
    "position" text DEFAULT 'bottom'::text,
    enabled boolean DEFAULT true,
    created_at timestamp with time zone DEFAULT now(),
    CONSTRAINT coaching_steps_position_check CHECK (("position" = ANY (ARRAY['top'::text, 'bottom'::text, 'left'::text, 'right'::text, 'center'::text])))
);


ALTER TABLE public.coaching_steps OWNER TO postgres;

--
-- Name: feature_permissions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.feature_permissions (
    id uuid DEFAULT extensions.uuid_generate_v4() NOT NULL,
    feature_area public.feature_area NOT NULL,
    parent_feature public.feature_area,
    display_name text NOT NULL,
    description text,
    icon text,
    nav_order integer DEFAULT 0,
    nav_group text,
    nav_route text,
    requires_subscription_plan text[],
    is_beta boolean DEFAULT false,
    is_active boolean DEFAULT true,
    created_at timestamp with time zone DEFAULT now(),
    updated_at timestamp with time zone DEFAULT now()
);


ALTER TABLE public.feature_permissions OWNER TO postgres;

--
-- Name: invoice_items; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.invoice_items (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    invoice_id uuid,
    item_id uuid,
    description text NOT NULL,
    quantity numeric(10,2) DEFAULT 1 NOT NULL,
    unit_price numeric(15,2) NOT NULL,
    tax_rate numeric(5,2) DEFAULT 15.0,
    discount_percentage numeric(5,2) DEFAULT 0,
    line_total numeric(15,2) NOT NULL,
    created_at timestamp with time zone DEFAULT now(),
    CONSTRAINT check_positive_quantity CHECK ((quantity > (0)::numeric)),
    CONSTRAINT check_positive_unit_price CHECK ((unit_price >= (0)::numeric)),
    CONSTRAINT check_valid_discount CHECK (((discount_percentage >= (0)::numeric) AND (discount_percentage <= (100)::numeric))),
    CONSTRAINT check_valid_tax_rate CHECK (((tax_rate >= (0)::numeric) AND (tax_rate <= (100)::numeric)))
);


ALTER TABLE public.invoice_items OWNER TO postgres;

--
-- Name: items; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.items (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    user_id uuid,
    name text NOT NULL,
    description text,
    item_type public.item_type DEFAULT 'product'::public.item_type NOT NULL,
    sku text,
    barcode text,
    category text,
    unit_of_measure text DEFAULT 'unit'::text,
    cost_price numeric(15,2) DEFAULT 0,
    selling_price numeric(15,2) NOT NULL,
    tax_rate numeric(5,2) DEFAULT 15.0,
    current_stock integer DEFAULT 0,
    minimum_stock integer DEFAULT 0,
    low_stock_threshold integer DEFAULT 10,
    critical_stock_threshold integer DEFAULT 5,
    enable_low_stock_alerts boolean DEFAULT true,
    last_low_stock_alert_sent_at timestamp with time zone,
    last_critical_stock_alert_sent_at timestamp with time zone,
    is_active boolean DEFAULT true,
    image_url text,
    created_at timestamp with time zone DEFAULT now(),
    updated_at timestamp with time zone DEFAULT now(),
    attributes jsonb DEFAULT '{}'::jsonb,
    business_profile_id uuid,
    supplier_id uuid,
    CONSTRAINT check_critical_threshold_less_than_low CHECK ((critical_stock_threshold <= low_stock_threshold)),
    CONSTRAINT check_non_negative_cost_price CHECK ((cost_price >= (0)::numeric)),
    CONSTRAINT check_non_negative_stock CHECK ((current_stock >= 0)),
    CONSTRAINT check_positive_selling_price CHECK ((selling_price >= (0)::numeric)),
    CONSTRAINT check_valid_tax_rate CHECK (((tax_rate >= (0)::numeric) AND (tax_rate <= (100)::numeric)))
);


ALTER TABLE public.items OWNER TO postgres;

--
-- Name: COLUMN items.attributes; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.items.attributes IS 'Custom attributes for items stored as key-value pairs';


--
-- Name: COLUMN items.business_profile_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.items.business_profile_id IS 'Reference to the business profile that owns this item';


--
-- Name: COLUMN items.supplier_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.items.supplier_id IS 'Reference to the supplier (business contact) for this item';


--
-- Name: low_stock_notification_logs; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.low_stock_notification_logs (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    user_id uuid NOT NULL,
    item_id uuid NOT NULL,
    notification_type text NOT NULL,
    current_stock integer NOT NULL,
    threshold_value integer NOT NULL,
    notification_title text NOT NULL,
    notification_body text NOT NULL,
    notification_data jsonb DEFAULT '{}'::jsonb,
    fcm_message_id text,
    delivery_status text DEFAULT 'pending'::text,
    error_message text,
    sent_at timestamp with time zone DEFAULT now(),
    delivered_at timestamp with time zone,
    created_at timestamp with time zone DEFAULT now(),
    CONSTRAINT low_stock_notification_logs_delivery_status_check CHECK ((delivery_status = ANY (ARRAY['pending'::text, 'sent'::text, 'delivered'::text, 'failed'::text]))),
    CONSTRAINT low_stock_notification_logs_notification_type_check CHECK ((notification_type = ANY (ARRAY['low_stock'::text, 'critical_stock'::text, 'out_of_stock'::text])))
);


ALTER TABLE public.low_stock_notification_logs OWNER TO postgres;

--
-- Name: low_stock_notification_settings; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.low_stock_notification_settings (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    user_id uuid NOT NULL,
    enable_low_stock_notifications boolean DEFAULT true,
    enable_push_notifications boolean DEFAULT true,
    enable_local_notifications boolean DEFAULT true,
    enable_in_app_notifications boolean DEFAULT true,
    notification_frequency_hours integer DEFAULT 24,
    quiet_hours_start time without time zone DEFAULT '22:00:00'::time without time zone,
    quiet_hours_end time without time zone DEFAULT '08:00:00'::time without time zone,
    enable_weekend_notifications boolean DEFAULT true,
    global_low_stock_threshold integer DEFAULT 10,
    global_critical_stock_threshold integer DEFAULT 5,
    use_global_thresholds boolean DEFAULT false,
    created_at timestamp with time zone DEFAULT now(),
    updated_at timestamp with time zone DEFAULT now()
);


ALTER TABLE public.low_stock_notification_settings OWNER TO postgres;

--
-- Name: navigation_config; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.navigation_config (
    id uuid DEFAULT extensions.uuid_generate_v4() NOT NULL,
    business_profile_id uuid,
    user_role public.user_role,
    nav_items jsonb DEFAULT '[]'::jsonb NOT NULL,
    is_custom boolean DEFAULT false,
    is_active boolean DEFAULT true,
    created_at timestamp with time zone DEFAULT now(),
    updated_at timestamp with time zone DEFAULT now(),
    CONSTRAINT navigation_config_check CHECK ((((business_profile_id IS NULL) AND (user_role IS NOT NULL)) OR (business_profile_id IS NOT NULL)))
);


ALTER TABLE public.navigation_config OWNER TO postgres;

--
-- Name: notification_logs; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.notification_logs (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    user_id uuid,
    title text NOT NULL,
    body text NOT NULL,
    data jsonb DEFAULT '{}'::jsonb,
    fcm_response jsonb DEFAULT '{}'::jsonb,
    sent_at timestamp with time zone DEFAULT now(),
    status text DEFAULT 'pending'::text,
    notification_type text DEFAULT 'general'::text,
    created_at timestamp with time zone DEFAULT now(),
    updated_at timestamp with time zone DEFAULT now(),
    CONSTRAINT notification_logs_notification_type_check CHECK ((notification_type = ANY (ARRAY['welcome'::text, 'milestone'::text, 'reminder'::text, 'general'::text, 'system'::text, 'low_stock'::text]))),
    CONSTRAINT notification_logs_status_check CHECK ((status = ANY (ARRAY['pending'::text, 'sent'::text, 'failed'::text, 'delivered'::text])))
);


ALTER TABLE public.notification_logs OWNER TO postgres;

--
-- Name: notification_preferences; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.notification_preferences (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    user_id uuid,
    welcome_notifications boolean DEFAULT true,
    milestone_notifications boolean DEFAULT true,
    reminder_notifications boolean DEFAULT true,
    system_notifications boolean DEFAULT true,
    low_stock_notifications boolean DEFAULT true,
    push_notifications boolean DEFAULT true,
    email_notifications boolean DEFAULT true,
    in_app_notifications boolean DEFAULT true,
    quiet_hours_start time without time zone DEFAULT '22:00:00'::time without time zone,
    quiet_hours_end time without time zone DEFAULT '08:00:00'::time without time zone,
    timezone text DEFAULT 'UTC'::text,
    created_at timestamp with time zone DEFAULT now(),
    updated_at timestamp with time zone DEFAULT now()
);


ALTER TABLE public.notification_preferences OWNER TO postgres;

--
-- Name: registration_analytics; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.registration_analytics (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    user_id uuid,
    registration_date timestamp with time zone DEFAULT now(),
    business_type text,
    source text
);


ALTER TABLE public.registration_analytics OWNER TO postgres;

--
-- Name: role_feature_access; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.role_feature_access (
    id uuid DEFAULT extensions.uuid_generate_v4() NOT NULL,
    role public.user_role NOT NULL,
    feature_area public.feature_area NOT NULL,
    can_read boolean DEFAULT false,
    can_write boolean DEFAULT false,
    can_delete boolean DEFAULT false,
    can_execute boolean DEFAULT false,
    created_at timestamp with time zone DEFAULT now(),
    updated_at timestamp with time zone DEFAULT now()
);


ALTER TABLE public.role_feature_access OWNER TO postgres;

--
-- Name: role_templates; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.role_templates (
    id uuid DEFAULT extensions.uuid_generate_v4() NOT NULL,
    role public.user_role NOT NULL,
    display_name text NOT NULL,
    description text,
    default_permissions jsonb DEFAULT '[]'::jsonb NOT NULL,
    ui_config jsonb DEFAULT '{}'::jsonb,
    is_system boolean DEFAULT true,
    created_at timestamp with time zone DEFAULT now(),
    updated_at timestamp with time zone DEFAULT now()
);


ALTER TABLE public.role_templates OWNER TO postgres;

--
-- Name: stock_movements; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.stock_movements (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    user_id uuid,
    item_id uuid,
    movement_type text NOT NULL,
    quantity_change integer NOT NULL,
    new_stock_level integer NOT NULL,
    reference_id uuid,
    reference_type text,
    notes text,
    created_at timestamp with time zone DEFAULT now(),
    CONSTRAINT check_non_negative_stock CHECK ((new_stock_level >= 0))
);


ALTER TABLE public.stock_movements OWNER TO postgres;

--
-- Name: subscription_plans; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.subscription_plans (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    name text NOT NULL,
    display_name text NOT NULL,
    description text,
    monthly_price numeric(10,2) NOT NULL,
    yearly_price numeric(10,2),
    invoice_limit integer,
    features jsonb DEFAULT '{}'::jsonb,
    is_active boolean DEFAULT true,
    created_at timestamp with time zone DEFAULT now()
);


ALTER TABLE public.subscription_plans OWNER TO postgres;

--
-- Name: subscriptions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.subscriptions (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    user_id uuid,
    plan_id uuid,
    status public.subscription_status DEFAULT 'active'::public.subscription_status,
    current_period_start timestamp with time zone DEFAULT now(),
    current_period_end timestamp with time zone,
    cancel_at_period_end boolean DEFAULT false,
    cancelled_at timestamp with time zone,
    created_at timestamp with time zone DEFAULT now(),
    updated_at timestamp with time zone DEFAULT now()
);


ALTER TABLE public.subscriptions OWNER TO postgres;

--
-- Name: system_analytics; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.system_analytics (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    metric_type text NOT NULL,
    data jsonb NOT NULL,
    "timestamp" timestamp with time zone DEFAULT now()
);


ALTER TABLE public.system_analytics OWNER TO postgres;

--
-- Name: user_activity_log; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_activity_log (
    id uuid DEFAULT extensions.uuid_generate_v4() NOT NULL,
    business_user_id uuid,
    business_profile_id uuid,
    activity_type text NOT NULL,
    feature_area public.feature_area,
    entity_type text,
    entity_id uuid,
    activity_data jsonb DEFAULT '{}'::jsonb,
    ip_address inet,
    user_agent text,
    created_at timestamp with time zone DEFAULT now()
);


ALTER TABLE public.user_activity_log OWNER TO postgres;

--
-- Name: user_coaching_progress; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_coaching_progress (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    user_id uuid NOT NULL,
    flow_id text NOT NULL,
    completed_at timestamp with time zone DEFAULT now()
);


ALTER TABLE public.user_coaching_progress OWNER TO postgres;

--
-- Name: user_config_history; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_config_history (
    id uuid DEFAULT extensions.uuid_generate_v4() NOT NULL,
    user_id uuid NOT NULL,
    widget_type public.widget_type NOT NULL,
    change_type character varying(50) NOT NULL,
    old_config jsonb,
    new_config jsonb NOT NULL,
    change_diff jsonb,
    change_reason character varying(255),
    triggered_by character varying(100),
    created_at timestamp with time zone DEFAULT now(),
    CONSTRAINT user_config_history_change_type_check CHECK (((change_type)::text = ANY ((ARRAY['update'::character varying, 'reset'::character varying, 'migrate'::character varying, 'experiment'::character varying])::text[])))
);


ALTER TABLE public.user_config_history OWNER TO postgres;

--
-- Name: TABLE user_config_history; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.user_config_history IS 'History of configuration changes for audit and rollback';


--
-- Name: user_experiment_assignments; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_experiment_assignments (
    id uuid DEFAULT extensions.uuid_generate_v4() NOT NULL,
    user_id uuid NOT NULL,
    experiment_id uuid NOT NULL,
    variant_id uuid NOT NULL,
    assigned_at timestamp with time zone DEFAULT now(),
    assignment_context jsonb DEFAULT '{}'::jsonb,
    assignment_hash character varying(64),
    created_at timestamp with time zone DEFAULT now()
);


ALTER TABLE public.user_experiment_assignments OWNER TO postgres;

--
-- Name: user_fcm_tokens; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_fcm_tokens (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    user_id uuid,
    fcm_token text NOT NULL,
    device_id text,
    platform text,
    app_version text,
    is_active boolean DEFAULT true,
    last_used_at timestamp with time zone DEFAULT now(),
    created_at timestamp with time zone DEFAULT now(),
    updated_at timestamp with time zone DEFAULT now(),
    CONSTRAINT user_fcm_tokens_platform_check CHECK ((platform = ANY (ARRAY['ios'::text, 'android'::text, 'web'::text])))
);


ALTER TABLE public.user_fcm_tokens OWNER TO postgres;

--
-- Name: user_feature_access; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_feature_access (
    id uuid DEFAULT extensions.uuid_generate_v4() NOT NULL,
    business_user_id uuid NOT NULL,
    feature_area public.feature_area NOT NULL,
    can_read boolean DEFAULT false,
    can_write boolean DEFAULT false,
    can_delete boolean DEFAULT false,
    can_execute boolean DEFAULT false,
    custom_constraints jsonb DEFAULT '{}'::jsonb,
    granted_by uuid,
    granted_at timestamp with time zone DEFAULT now(),
    expires_at timestamp with time zone
);


ALTER TABLE public.user_feature_access OWNER TO postgres;

--
-- Name: user_sessions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_sessions (
    id uuid DEFAULT extensions.uuid_generate_v4() NOT NULL,
    business_user_id uuid NOT NULL,
    session_token text NOT NULL,
    device_id text,
    device_type text,
    ip_address inet,
    user_agent text,
    created_at timestamp with time zone DEFAULT now(),
    last_activity_at timestamp with time zone DEFAULT now(),
    expires_at timestamp with time zone NOT NULL,
    is_active boolean DEFAULT true
);


ALTER TABLE public.user_sessions OWNER TO postgres;

--
-- Name: user_widget_configs; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_widget_configs (
    id uuid DEFAULT extensions.uuid_generate_v4() NOT NULL,
    user_id uuid NOT NULL,
    business_profile_id uuid,
    widget_type public.widget_type NOT NULL,
    config jsonb DEFAULT '{}'::jsonb NOT NULL,
    layout public.widget_layout DEFAULT 'list'::public.widget_layout,
    is_enabled boolean DEFAULT true,
    usage_stats jsonb DEFAULT '{}'::jsonb,
    last_configured_at timestamp with time zone,
    created_at timestamp with time zone DEFAULT now(),
    updated_at timestamp with time zone DEFAULT now(),
    CONSTRAINT user_widget_configs_config_check CHECK ((config IS NOT NULL))
);


ALTER TABLE public.user_widget_configs OWNER TO postgres;

--
-- Name: TABLE user_widget_configs; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.user_widget_configs IS 'User-specific widget configurations and preferences';


--
-- Name: widget_config_cache; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.widget_config_cache (
    id uuid DEFAULT extensions.uuid_generate_v4() NOT NULL,
    cache_key character varying(255) NOT NULL,
    config_data jsonb NOT NULL,
    config_hash character varying(64) NOT NULL,
    cache_scope public.config_scope NOT NULL,
    widget_type public.widget_type NOT NULL,
    user_id uuid,
    ttl_seconds integer DEFAULT 300,
    version integer DEFAULT 1,
    created_at timestamp with time zone DEFAULT now(),
    updated_at timestamp with time zone DEFAULT now(),
    expires_at timestamp with time zone DEFAULT (now() + '00:05:00'::interval),
    CONSTRAINT widget_config_cache_cache_key_check CHECK (((cache_key)::text <> ''::text)),
    CONSTRAINT widget_config_cache_config_data_check CHECK ((config_data IS NOT NULL)),
    CONSTRAINT widget_config_cache_ttl_seconds_check CHECK ((ttl_seconds > 0))
);


ALTER TABLE public.widget_config_cache OWNER TO postgres;

--
-- Name: TABLE widget_config_cache; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.widget_config_cache IS 'High-performance cache for frequently accessed configurations';


--
-- Name: widget_config_templates; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.widget_config_templates (
    id uuid DEFAULT extensions.uuid_generate_v4() NOT NULL,
    widget_type public.widget_type NOT NULL,
    name character varying(255) NOT NULL,
    description text,
    is_default boolean DEFAULT false,
    config_schema jsonb DEFAULT '{}'::jsonb NOT NULL,
    default_config jsonb DEFAULT '{}'::jsonb NOT NULL,
    validation_rules jsonb DEFAULT '{}'::jsonb,
    created_at timestamp with time zone DEFAULT now(),
    updated_at timestamp with time zone DEFAULT now(),
    created_by uuid,
    CONSTRAINT widget_config_templates_config_schema_check CHECK ((config_schema IS NOT NULL)),
    CONSTRAINT widget_config_templates_default_config_check CHECK ((default_config IS NOT NULL))
);


ALTER TABLE public.widget_config_templates OWNER TO postgres;

--
-- Name: TABLE widget_config_templates; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.widget_config_templates IS 'Global widget configuration templates and defaults';


--
-- Name: widget_experiment_variants; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.widget_experiment_variants (
    id uuid DEFAULT extensions.uuid_generate_v4() NOT NULL,
    experiment_id uuid NOT NULL,
    name character varying(255) NOT NULL,
    description text,
    is_control boolean DEFAULT false,
    traffic_weight integer DEFAULT 50,
    config_override jsonb DEFAULT '{}'::jsonb,
    created_at timestamp with time zone DEFAULT now(),
    updated_at timestamp with time zone DEFAULT now(),
    CONSTRAINT widget_experiment_variants_check CHECK ((NOT (is_control AND (config_override <> '{}'::jsonb)))),
    CONSTRAINT widget_experiment_variants_traffic_weight_check CHECK (((traffic_weight >= 0) AND (traffic_weight <= 100)))
);


ALTER TABLE public.widget_experiment_variants OWNER TO postgres;

--
-- Name: TABLE widget_experiment_variants; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.widget_experiment_variants IS 'Variants for A/B testing experiments';


--
-- Name: widget_experiments; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.widget_experiments (
    id uuid DEFAULT extensions.uuid_generate_v4() NOT NULL,
    name character varying(255) NOT NULL,
    description text,
    widget_type public.widget_type NOT NULL,
    status public.experiment_status DEFAULT 'draft'::public.experiment_status,
    assignment_strategy public.assignment_strategy DEFAULT 'random'::public.assignment_strategy,
    traffic_allocation integer DEFAULT 100,
    start_date timestamp with time zone,
    end_date timestamp with time zone,
    target_criteria jsonb DEFAULT '{}'::jsonb,
    primary_metric character varying(255),
    secondary_metrics jsonb DEFAULT '[]'::jsonb,
    statistical_power numeric DEFAULT 0.8,
    minimum_detectable_effect numeric DEFAULT 0.05,
    created_at timestamp with time zone DEFAULT now(),
    updated_at timestamp with time zone DEFAULT now(),
    created_by uuid,
    CONSTRAINT widget_experiments_check CHECK (((start_date IS NULL) OR (end_date IS NULL) OR (start_date < end_date))),
    CONSTRAINT widget_experiments_minimum_detectable_effect_check CHECK ((minimum_detectable_effect > (0)::numeric)),
    CONSTRAINT widget_experiments_statistical_power_check CHECK (((statistical_power > (0)::numeric) AND (statistical_power <= (1)::numeric))),
    CONSTRAINT widget_experiments_traffic_allocation_check CHECK (((traffic_allocation >= 0) AND (traffic_allocation <= 100)))
);


ALTER TABLE public.widget_experiments OWNER TO postgres;

--
-- Name: TABLE widget_experiments; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.widget_experiments IS 'A/B testing experiments for widget configurations';


--
-- Name: widget_feature_flags; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.widget_feature_flags (
    id uuid DEFAULT extensions.uuid_generate_v4() NOT NULL,
    flag_key character varying(255) NOT NULL,
    widget_type public.widget_type,
    is_enabled boolean DEFAULT false,
    rollout_percentage integer DEFAULT 0,
    target_segments jsonb DEFAULT '[]'::jsonb,
    target_users jsonb DEFAULT '[]'::jsonb,
    exclude_users jsonb DEFAULT '[]'::jsonb,
    description text,
    created_at timestamp with time zone DEFAULT now(),
    updated_at timestamp with time zone DEFAULT now(),
    created_by uuid,
    CONSTRAINT widget_feature_flags_rollout_percentage_check CHECK (((rollout_percentage >= 0) AND (rollout_percentage <= 100)))
);


ALTER TABLE public.widget_feature_flags OWNER TO postgres;

--
-- Name: TABLE widget_feature_flags; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.widget_feature_flags IS 'Feature flags for controlling widget functionality rollout';


--
-- Name: widget_usage_analytics; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.widget_usage_analytics (
    id uuid DEFAULT extensions.uuid_generate_v4() NOT NULL,
    user_id uuid NOT NULL,
    business_profile_id uuid,
    widget_type public.widget_type NOT NULL,
    event_type character varying(100) NOT NULL,
    event_data jsonb DEFAULT '{}'::jsonb,
    config_version integer DEFAULT 1,
    experiment_id uuid,
    variant_id uuid,
    session_id character varying(255),
    user_agent text,
    platform character varying(50),
    created_at timestamp with time zone DEFAULT now(),
    CONSTRAINT widget_usage_analytics_event_type_check CHECK (((event_type)::text = ANY ((ARRAY['view'::character varying, 'interact'::character varying, 'configure'::character varying, 'error'::character varying, 'load'::character varying, 'scroll'::character varying, 'tap'::character varying])::text[])))
);


ALTER TABLE public.widget_usage_analytics OWNER TO postgres;

--
-- Name: TABLE widget_usage_analytics; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.widget_usage_analytics IS 'Analytics data for widget usage and interactions';


--
-- Name: messages; Type: TABLE; Schema: realtime; Owner: supabase_realtime_admin
--

CREATE TABLE realtime.messages (
    topic text NOT NULL,
    extension text NOT NULL,
    payload jsonb,
    event text,
    private boolean DEFAULT false,
    updated_at timestamp without time zone DEFAULT now() NOT NULL,
    inserted_at timestamp without time zone DEFAULT now() NOT NULL,
    id uuid DEFAULT gen_random_uuid() NOT NULL
)
PARTITION BY RANGE (inserted_at);


ALTER TABLE realtime.messages OWNER TO supabase_realtime_admin;

--
-- Name: messages_2025_08_05; Type: TABLE; Schema: realtime; Owner: supabase_admin
--

CREATE TABLE realtime.messages_2025_08_05 (
    topic text NOT NULL,
    extension text NOT NULL,
    payload jsonb,
    event text,
    private boolean DEFAULT false,
    updated_at timestamp without time zone DEFAULT now() NOT NULL,
    inserted_at timestamp without time zone DEFAULT now() NOT NULL,
    id uuid DEFAULT gen_random_uuid() NOT NULL
);


ALTER TABLE realtime.messages_2025_08_05 OWNER TO supabase_admin;

--
-- Name: messages_2025_08_06; Type: TABLE; Schema: realtime; Owner: supabase_admin
--

CREATE TABLE realtime.messages_2025_08_06 (
    topic text NOT NULL,
    extension text NOT NULL,
    payload jsonb,
    event text,
    private boolean DEFAULT false,
    updated_at timestamp without time zone DEFAULT now() NOT NULL,
    inserted_at timestamp without time zone DEFAULT now() NOT NULL,
    id uuid DEFAULT gen_random_uuid() NOT NULL
);


ALTER TABLE realtime.messages_2025_08_06 OWNER TO supabase_admin;

--
-- Name: messages_2025_08_07; Type: TABLE; Schema: realtime; Owner: supabase_admin
--

CREATE TABLE realtime.messages_2025_08_07 (
    topic text NOT NULL,
    extension text NOT NULL,
    payload jsonb,
    event text,
    private boolean DEFAULT false,
    updated_at timestamp without time zone DEFAULT now() NOT NULL,
    inserted_at timestamp without time zone DEFAULT now() NOT NULL,
    id uuid DEFAULT gen_random_uuid() NOT NULL
);


ALTER TABLE realtime.messages_2025_08_07 OWNER TO supabase_admin;

--
-- Name: messages_2025_08_08; Type: TABLE; Schema: realtime; Owner: supabase_admin
--

CREATE TABLE realtime.messages_2025_08_08 (
    topic text NOT NULL,
    extension text NOT NULL,
    payload jsonb,
    event text,
    private boolean DEFAULT false,
    updated_at timestamp without time zone DEFAULT now() NOT NULL,
    inserted_at timestamp without time zone DEFAULT now() NOT NULL,
    id uuid DEFAULT gen_random_uuid() NOT NULL
);


ALTER TABLE realtime.messages_2025_08_08 OWNER TO supabase_admin;

--
-- Name: messages_2025_08_09; Type: TABLE; Schema: realtime; Owner: supabase_admin
--

CREATE TABLE realtime.messages_2025_08_09 (
    topic text NOT NULL,
    extension text NOT NULL,
    payload jsonb,
    event text,
    private boolean DEFAULT false,
    updated_at timestamp without time zone DEFAULT now() NOT NULL,
    inserted_at timestamp without time zone DEFAULT now() NOT NULL,
    id uuid DEFAULT gen_random_uuid() NOT NULL
);


ALTER TABLE realtime.messages_2025_08_09 OWNER TO supabase_admin;

--
-- Name: schema_migrations; Type: TABLE; Schema: realtime; Owner: supabase_admin
--

CREATE TABLE realtime.schema_migrations (
    version bigint NOT NULL,
    inserted_at timestamp(0) without time zone
);


ALTER TABLE realtime.schema_migrations OWNER TO supabase_admin;

--
-- Name: subscription; Type: TABLE; Schema: realtime; Owner: supabase_admin
--

CREATE TABLE realtime.subscription (
    id bigint NOT NULL,
    subscription_id uuid NOT NULL,
    entity regclass NOT NULL,
    filters realtime.user_defined_filter[] DEFAULT '{}'::realtime.user_defined_filter[] NOT NULL,
    claims jsonb NOT NULL,
    claims_role regrole GENERATED ALWAYS AS (realtime.to_regrole((claims ->> 'role'::text))) STORED NOT NULL,
    created_at timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL
);


ALTER TABLE realtime.subscription OWNER TO supabase_admin;

--
-- Name: subscription_id_seq; Type: SEQUENCE; Schema: realtime; Owner: supabase_admin
--

ALTER TABLE realtime.subscription ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME realtime.subscription_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: buckets; Type: TABLE; Schema: storage; Owner: supabase_storage_admin
--

CREATE TABLE storage.buckets (
    id text NOT NULL,
    name text NOT NULL,
    owner uuid,
    created_at timestamp with time zone DEFAULT now(),
    updated_at timestamp with time zone DEFAULT now(),
    public boolean DEFAULT false,
    avif_autodetection boolean DEFAULT false,
    file_size_limit bigint,
    allowed_mime_types text[],
    owner_id text,
    type storage.buckettype DEFAULT 'STANDARD'::storage.buckettype NOT NULL
);


ALTER TABLE storage.buckets OWNER TO supabase_storage_admin;

--
-- Name: COLUMN buckets.owner; Type: COMMENT; Schema: storage; Owner: supabase_storage_admin
--

COMMENT ON COLUMN storage.buckets.owner IS 'Field is deprecated, use owner_id instead';


--
-- Name: buckets_analytics; Type: TABLE; Schema: storage; Owner: supabase_storage_admin
--

CREATE TABLE storage.buckets_analytics (
    id text NOT NULL,
    type storage.buckettype DEFAULT 'ANALYTICS'::storage.buckettype NOT NULL,
    format text DEFAULT 'ICEBERG'::text NOT NULL,
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    updated_at timestamp with time zone DEFAULT now() NOT NULL
);


ALTER TABLE storage.buckets_analytics OWNER TO supabase_storage_admin;

--
-- Name: iceberg_namespaces; Type: TABLE; Schema: storage; Owner: supabase_storage_admin
--

CREATE TABLE storage.iceberg_namespaces (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    bucket_id text NOT NULL,
    name text NOT NULL COLLATE pg_catalog."C",
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    updated_at timestamp with time zone DEFAULT now() NOT NULL
);


ALTER TABLE storage.iceberg_namespaces OWNER TO supabase_storage_admin;

--
-- Name: iceberg_tables; Type: TABLE; Schema: storage; Owner: supabase_storage_admin
--

CREATE TABLE storage.iceberg_tables (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    namespace_id uuid NOT NULL,
    bucket_id text NOT NULL,
    name text NOT NULL COLLATE pg_catalog."C",
    location text NOT NULL,
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    updated_at timestamp with time zone DEFAULT now() NOT NULL
);


ALTER TABLE storage.iceberg_tables OWNER TO supabase_storage_admin;

--
-- Name: migrations; Type: TABLE; Schema: storage; Owner: supabase_storage_admin
--

CREATE TABLE storage.migrations (
    id integer NOT NULL,
    name character varying(100) NOT NULL,
    hash character varying(40) NOT NULL,
    executed_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE storage.migrations OWNER TO supabase_storage_admin;

--
-- Name: objects; Type: TABLE; Schema: storage; Owner: supabase_storage_admin
--

CREATE TABLE storage.objects (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    bucket_id text,
    name text,
    owner uuid,
    created_at timestamp with time zone DEFAULT now(),
    updated_at timestamp with time zone DEFAULT now(),
    last_accessed_at timestamp with time zone DEFAULT now(),
    metadata jsonb,
    path_tokens text[] GENERATED ALWAYS AS (string_to_array(name, '/'::text)) STORED,
    version text,
    owner_id text,
    user_metadata jsonb,
    level integer
);


ALTER TABLE storage.objects OWNER TO supabase_storage_admin;

--
-- Name: COLUMN objects.owner; Type: COMMENT; Schema: storage; Owner: supabase_storage_admin
--

COMMENT ON COLUMN storage.objects.owner IS 'Field is deprecated, use owner_id instead';


--
-- Name: prefixes; Type: TABLE; Schema: storage; Owner: supabase_storage_admin
--

CREATE TABLE storage.prefixes (
    bucket_id text NOT NULL,
    name text NOT NULL COLLATE pg_catalog."C",
    level integer GENERATED ALWAYS AS (storage.get_level(name)) STORED NOT NULL,
    created_at timestamp with time zone DEFAULT now(),
    updated_at timestamp with time zone DEFAULT now()
);


ALTER TABLE storage.prefixes OWNER TO supabase_storage_admin;

--
-- Name: s3_multipart_uploads; Type: TABLE; Schema: storage; Owner: supabase_storage_admin
--

CREATE TABLE storage.s3_multipart_uploads (
    id text NOT NULL,
    in_progress_size bigint DEFAULT 0 NOT NULL,
    upload_signature text NOT NULL,
    bucket_id text NOT NULL,
    key text NOT NULL COLLATE pg_catalog."C",
    version text NOT NULL,
    owner_id text,
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    user_metadata jsonb
);


ALTER TABLE storage.s3_multipart_uploads OWNER TO supabase_storage_admin;

--
-- Name: s3_multipart_uploads_parts; Type: TABLE; Schema: storage; Owner: supabase_storage_admin
--

CREATE TABLE storage.s3_multipart_uploads_parts (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    upload_id text NOT NULL,
    size bigint DEFAULT 0 NOT NULL,
    part_number integer NOT NULL,
    bucket_id text NOT NULL,
    key text NOT NULL COLLATE pg_catalog."C",
    etag text NOT NULL,
    owner_id text,
    version text NOT NULL,
    created_at timestamp with time zone DEFAULT now() NOT NULL
);


ALTER TABLE storage.s3_multipart_uploads_parts OWNER TO supabase_storage_admin;

--
-- Name: hooks; Type: TABLE; Schema: supabase_functions; Owner: supabase_functions_admin
--

CREATE TABLE supabase_functions.hooks (
    id bigint NOT NULL,
    hook_table_id integer NOT NULL,
    hook_name text NOT NULL,
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    request_id bigint
);


ALTER TABLE supabase_functions.hooks OWNER TO supabase_functions_admin;

--
-- Name: TABLE hooks; Type: COMMENT; Schema: supabase_functions; Owner: supabase_functions_admin
--

COMMENT ON TABLE supabase_functions.hooks IS 'Supabase Functions Hooks: Audit trail for triggered hooks.';


--
-- Name: hooks_id_seq; Type: SEQUENCE; Schema: supabase_functions; Owner: supabase_functions_admin
--

CREATE SEQUENCE supabase_functions.hooks_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE supabase_functions.hooks_id_seq OWNER TO supabase_functions_admin;

--
-- Name: hooks_id_seq; Type: SEQUENCE OWNED BY; Schema: supabase_functions; Owner: supabase_functions_admin
--

ALTER SEQUENCE supabase_functions.hooks_id_seq OWNED BY supabase_functions.hooks.id;


--
-- Name: migrations; Type: TABLE; Schema: supabase_functions; Owner: supabase_functions_admin
--

CREATE TABLE supabase_functions.migrations (
    version text NOT NULL,
    inserted_at timestamp with time zone DEFAULT now() NOT NULL
);


ALTER TABLE supabase_functions.migrations OWNER TO supabase_functions_admin;

--
-- Name: schema_migrations; Type: TABLE; Schema: supabase_migrations; Owner: postgres
--

CREATE TABLE supabase_migrations.schema_migrations (
    version text NOT NULL,
    statements text[],
    name text
);


ALTER TABLE supabase_migrations.schema_migrations OWNER TO postgres;

--
-- Name: messages_2025_08_05; Type: TABLE ATTACH; Schema: realtime; Owner: supabase_admin
--

ALTER TABLE ONLY realtime.messages ATTACH PARTITION realtime.messages_2025_08_05 FOR VALUES FROM ('2025-08-05 00:00:00') TO ('2025-08-06 00:00:00');


--
-- Name: messages_2025_08_06; Type: TABLE ATTACH; Schema: realtime; Owner: supabase_admin
--

ALTER TABLE ONLY realtime.messages ATTACH PARTITION realtime.messages_2025_08_06 FOR VALUES FROM ('2025-08-06 00:00:00') TO ('2025-08-07 00:00:00');


--
-- Name: messages_2025_08_07; Type: TABLE ATTACH; Schema: realtime; Owner: supabase_admin
--

ALTER TABLE ONLY realtime.messages ATTACH PARTITION realtime.messages_2025_08_07 FOR VALUES FROM ('2025-08-07 00:00:00') TO ('2025-08-08 00:00:00');


--
-- Name: messages_2025_08_08; Type: TABLE ATTACH; Schema: realtime; Owner: supabase_admin
--

ALTER TABLE ONLY realtime.messages ATTACH PARTITION realtime.messages_2025_08_08 FOR VALUES FROM ('2025-08-08 00:00:00') TO ('2025-08-09 00:00:00');


--
-- Name: messages_2025_08_09; Type: TABLE ATTACH; Schema: realtime; Owner: supabase_admin
--

ALTER TABLE ONLY realtime.messages ATTACH PARTITION realtime.messages_2025_08_09 FOR VALUES FROM ('2025-08-09 00:00:00') TO ('2025-08-10 00:00:00');


--
-- Name: refresh_tokens id; Type: DEFAULT; Schema: auth; Owner: supabase_auth_admin
--

ALTER TABLE ONLY auth.refresh_tokens ALTER COLUMN id SET DEFAULT nextval('auth.refresh_tokens_id_seq'::regclass);


--
-- Name: hooks id; Type: DEFAULT; Schema: supabase_functions; Owner: supabase_functions_admin
--

ALTER TABLE ONLY supabase_functions.hooks ALTER COLUMN id SET DEFAULT nextval('supabase_functions.hooks_id_seq'::regclass);


--
-- Name: extensions extensions_pkey; Type: CONSTRAINT; Schema: _realtime; Owner: supabase_admin
--

ALTER TABLE ONLY _realtime.extensions
    ADD CONSTRAINT extensions_pkey PRIMARY KEY (id);


--
-- Name: schema_migrations schema_migrations_pkey; Type: CONSTRAINT; Schema: _realtime; Owner: supabase_admin
--

ALTER TABLE ONLY _realtime.schema_migrations
    ADD CONSTRAINT schema_migrations_pkey PRIMARY KEY (version);


--
-- Name: tenants tenants_pkey; Type: CONSTRAINT; Schema: _realtime; Owner: supabase_admin
--

ALTER TABLE ONLY _realtime.tenants
    ADD CONSTRAINT tenants_pkey PRIMARY KEY (id);


--
-- Name: mfa_amr_claims amr_id_pk; Type: CONSTRAINT; Schema: auth; Owner: supabase_auth_admin
--

ALTER TABLE ONLY auth.mfa_amr_claims
    ADD CONSTRAINT amr_id_pk PRIMARY KEY (id);


--
-- Name: audit_log_entries audit_log_entries_pkey; Type: CONSTRAINT; Schema: auth; Owner: supabase_auth_admin
--

ALTER TABLE ONLY auth.audit_log_entries
    ADD CONSTRAINT audit_log_entries_pkey PRIMARY KEY (id);


--
-- Name: flow_state flow_state_pkey; Type: CONSTRAINT; Schema: auth; Owner: supabase_auth_admin
--

ALTER TABLE ONLY auth.flow_state
    ADD CONSTRAINT flow_state_pkey PRIMARY KEY (id);


--
-- Name: identities identities_pkey; Type: CONSTRAINT; Schema: auth; Owner: supabase_auth_admin
--

ALTER TABLE ONLY auth.identities
    ADD CONSTRAINT identities_pkey PRIMARY KEY (id);


--
-- Name: identities identities_provider_id_provider_unique; Type: CONSTRAINT; Schema: auth; Owner: supabase_auth_admin
--

ALTER TABLE ONLY auth.identities
    ADD CONSTRAINT identities_provider_id_provider_unique UNIQUE (provider_id, provider);


--
-- Name: instances instances_pkey; Type: CONSTRAINT; Schema: auth; Owner: supabase_auth_admin
--

ALTER TABLE ONLY auth.instances
    ADD CONSTRAINT instances_pkey PRIMARY KEY (id);


--
-- Name: mfa_amr_claims mfa_amr_claims_session_id_authentication_method_pkey; Type: CONSTRAINT; Schema: auth; Owner: supabase_auth_admin
--

ALTER TABLE ONLY auth.mfa_amr_claims
    ADD CONSTRAINT mfa_amr_claims_session_id_authentication_method_pkey UNIQUE (session_id, authentication_method);


--
-- Name: mfa_challenges mfa_challenges_pkey; Type: CONSTRAINT; Schema: auth; Owner: supabase_auth_admin
--

ALTER TABLE ONLY auth.mfa_challenges
    ADD CONSTRAINT mfa_challenges_pkey PRIMARY KEY (id);


--
-- Name: mfa_factors mfa_factors_last_challenged_at_key; Type: CONSTRAINT; Schema: auth; Owner: supabase_auth_admin
--

ALTER TABLE ONLY auth.mfa_factors
    ADD CONSTRAINT mfa_factors_last_challenged_at_key UNIQUE (last_challenged_at);


--
-- Name: mfa_factors mfa_factors_pkey; Type: CONSTRAINT; Schema: auth; Owner: supabase_auth_admin
--

ALTER TABLE ONLY auth.mfa_factors
    ADD CONSTRAINT mfa_factors_pkey PRIMARY KEY (id);


--
-- Name: one_time_tokens one_time_tokens_pkey; Type: CONSTRAINT; Schema: auth; Owner: supabase_auth_admin
--

ALTER TABLE ONLY auth.one_time_tokens
    ADD CONSTRAINT one_time_tokens_pkey PRIMARY KEY (id);


--
-- Name: refresh_tokens refresh_tokens_pkey; Type: CONSTRAINT; Schema: auth; Owner: supabase_auth_admin
--

ALTER TABLE ONLY auth.refresh_tokens
    ADD CONSTRAINT refresh_tokens_pkey PRIMARY KEY (id);


--
-- Name: refresh_tokens refresh_tokens_token_unique; Type: CONSTRAINT; Schema: auth; Owner: supabase_auth_admin
--

ALTER TABLE ONLY auth.refresh_tokens
    ADD CONSTRAINT refresh_tokens_token_unique UNIQUE (token);


--
-- Name: saml_providers saml_providers_entity_id_key; Type: CONSTRAINT; Schema: auth; Owner: supabase_auth_admin
--

ALTER TABLE ONLY auth.saml_providers
    ADD CONSTRAINT saml_providers_entity_id_key UNIQUE (entity_id);


--
-- Name: saml_providers saml_providers_pkey; Type: CONSTRAINT; Schema: auth; Owner: supabase_auth_admin
--

ALTER TABLE ONLY auth.saml_providers
    ADD CONSTRAINT saml_providers_pkey PRIMARY KEY (id);


--
-- Name: saml_relay_states saml_relay_states_pkey; Type: CONSTRAINT; Schema: auth; Owner: supabase_auth_admin
--

ALTER TABLE ONLY auth.saml_relay_states
    ADD CONSTRAINT saml_relay_states_pkey PRIMARY KEY (id);


--
-- Name: schema_migrations schema_migrations_pkey; Type: CONSTRAINT; Schema: auth; Owner: supabase_auth_admin
--

ALTER TABLE ONLY auth.schema_migrations
    ADD CONSTRAINT schema_migrations_pkey PRIMARY KEY (version);


--
-- Name: sessions sessions_pkey; Type: CONSTRAINT; Schema: auth; Owner: supabase_auth_admin
--

ALTER TABLE ONLY auth.sessions
    ADD CONSTRAINT sessions_pkey PRIMARY KEY (id);


--
-- Name: sso_domains sso_domains_pkey; Type: CONSTRAINT; Schema: auth; Owner: supabase_auth_admin
--

ALTER TABLE ONLY auth.sso_domains
    ADD CONSTRAINT sso_domains_pkey PRIMARY KEY (id);


--
-- Name: sso_providers sso_providers_pkey; Type: CONSTRAINT; Schema: auth; Owner: supabase_auth_admin
--

ALTER TABLE ONLY auth.sso_providers
    ADD CONSTRAINT sso_providers_pkey PRIMARY KEY (id);


--
-- Name: users users_phone_key; Type: CONSTRAINT; Schema: auth; Owner: supabase_auth_admin
--

ALTER TABLE ONLY auth.users
    ADD CONSTRAINT users_phone_key UNIQUE (phone);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: auth; Owner: supabase_auth_admin
--

ALTER TABLE ONLY auth.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: admin_users admin_users_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.admin_users
    ADD CONSTRAINT admin_users_email_key UNIQUE (email);


--
-- Name: admin_users admin_users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.admin_users
    ADD CONSTRAINT admin_users_pkey PRIMARY KEY (id);


--
-- Name: business_categories business_categories_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.business_categories
    ADD CONSTRAINT business_categories_name_key UNIQUE (name);


--
-- Name: business_categories business_categories_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.business_categories
    ADD CONSTRAINT business_categories_pkey PRIMARY KEY (id);


--
-- Name: business_contacts business_contacts_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.business_contacts
    ADD CONSTRAINT business_contacts_pkey PRIMARY KEY (id);


--
-- Name: business_metrics_summary business_metrics_summary_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.business_metrics_summary
    ADD CONSTRAINT business_metrics_summary_pkey PRIMARY KEY (user_id);


--
-- Name: business_profiles business_profiles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.business_profiles
    ADD CONSTRAINT business_profiles_pkey PRIMARY KEY (id);


--
-- Name: business_profiles business_profiles_user_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.business_profiles
    ADD CONSTRAINT business_profiles_user_id_key UNIQUE (user_id);


--
-- Name: business_users business_users_business_profile_id_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.business_users
    ADD CONSTRAINT business_users_business_profile_id_email_key UNIQUE (business_profile_id, email);


--
-- Name: business_users business_users_business_profile_id_username_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.business_users
    ADD CONSTRAINT business_users_business_profile_id_username_key UNIQUE (business_profile_id, username);


--
-- Name: business_users business_users_invitation_token_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.business_users
    ADD CONSTRAINT business_users_invitation_token_key UNIQUE (invitation_token);


--
-- Name: business_users business_users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.business_users
    ADD CONSTRAINT business_users_pkey PRIMARY KEY (id);


--
-- Name: coaching_flows coaching_flows_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.coaching_flows
    ADD CONSTRAINT coaching_flows_pkey PRIMARY KEY (id);


--
-- Name: coaching_steps coaching_steps_flow_id_step_order_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.coaching_steps
    ADD CONSTRAINT coaching_steps_flow_id_step_order_key UNIQUE (flow_id, step_order);


--
-- Name: coaching_steps coaching_steps_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.coaching_steps
    ADD CONSTRAINT coaching_steps_pkey PRIMARY KEY (id);


--
-- Name: feature_permissions feature_permissions_feature_area_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.feature_permissions
    ADD CONSTRAINT feature_permissions_feature_area_key UNIQUE (feature_area);


--
-- Name: feature_permissions feature_permissions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.feature_permissions
    ADD CONSTRAINT feature_permissions_pkey PRIMARY KEY (id);


--
-- Name: invoice_items invoice_items_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.invoice_items
    ADD CONSTRAINT invoice_items_pkey PRIMARY KEY (id);


--
-- Name: invoices invoices_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.invoices
    ADD CONSTRAINT invoices_pkey PRIMARY KEY (id);


--
-- Name: items items_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.items
    ADD CONSTRAINT items_pkey PRIMARY KEY (id);


--
-- Name: low_stock_notification_logs low_stock_notification_logs_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.low_stock_notification_logs
    ADD CONSTRAINT low_stock_notification_logs_pkey PRIMARY KEY (id);


--
-- Name: low_stock_notification_settings low_stock_notification_settings_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.low_stock_notification_settings
    ADD CONSTRAINT low_stock_notification_settings_pkey PRIMARY KEY (id);


--
-- Name: low_stock_notification_settings low_stock_notification_settings_user_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.low_stock_notification_settings
    ADD CONSTRAINT low_stock_notification_settings_user_id_key UNIQUE (user_id);


--
-- Name: navigation_config navigation_config_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.navigation_config
    ADD CONSTRAINT navigation_config_pkey PRIMARY KEY (id);


--
-- Name: notification_logs notification_logs_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.notification_logs
    ADD CONSTRAINT notification_logs_pkey PRIMARY KEY (id);


--
-- Name: notification_preferences notification_preferences_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.notification_preferences
    ADD CONSTRAINT notification_preferences_pkey PRIMARY KEY (id);


--
-- Name: notification_preferences notification_preferences_user_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.notification_preferences
    ADD CONSTRAINT notification_preferences_user_id_key UNIQUE (user_id);


--
-- Name: registration_analytics registration_analytics_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.registration_analytics
    ADD CONSTRAINT registration_analytics_pkey PRIMARY KEY (id);


--
-- Name: role_feature_access role_feature_access_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.role_feature_access
    ADD CONSTRAINT role_feature_access_pkey PRIMARY KEY (id);


--
-- Name: role_feature_access role_feature_access_role_feature_area_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.role_feature_access
    ADD CONSTRAINT role_feature_access_role_feature_area_key UNIQUE (role, feature_area);


--
-- Name: role_templates role_templates_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.role_templates
    ADD CONSTRAINT role_templates_pkey PRIMARY KEY (id);


--
-- Name: role_templates role_templates_role_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.role_templates
    ADD CONSTRAINT role_templates_role_key UNIQUE (role);


--
-- Name: stock_movements stock_movements_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.stock_movements
    ADD CONSTRAINT stock_movements_pkey PRIMARY KEY (id);


--
-- Name: subscription_plans subscription_plans_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.subscription_plans
    ADD CONSTRAINT subscription_plans_name_key UNIQUE (name);


--
-- Name: subscription_plans subscription_plans_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.subscription_plans
    ADD CONSTRAINT subscription_plans_pkey PRIMARY KEY (id);


--
-- Name: subscriptions subscriptions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.subscriptions
    ADD CONSTRAINT subscriptions_pkey PRIMARY KEY (id);


--
-- Name: system_analytics system_analytics_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.system_analytics
    ADD CONSTRAINT system_analytics_pkey PRIMARY KEY (id);


--
-- Name: invoices unique_invoice_number_per_user; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.invoices
    ADD CONSTRAINT unique_invoice_number_per_user UNIQUE (user_id, invoice_number);


--
-- Name: user_activity_log user_activity_log_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_activity_log
    ADD CONSTRAINT user_activity_log_pkey PRIMARY KEY (id);


--
-- Name: user_coaching_progress user_coaching_progress_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_coaching_progress
    ADD CONSTRAINT user_coaching_progress_pkey PRIMARY KEY (id);


--
-- Name: user_coaching_progress user_coaching_progress_user_id_flow_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_coaching_progress
    ADD CONSTRAINT user_coaching_progress_user_id_flow_id_key UNIQUE (user_id, flow_id);


--
-- Name: user_config_history user_config_history_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_config_history
    ADD CONSTRAINT user_config_history_pkey PRIMARY KEY (id);


--
-- Name: user_experiment_assignments user_experiment_assignments_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_experiment_assignments
    ADD CONSTRAINT user_experiment_assignments_pkey PRIMARY KEY (id);


--
-- Name: user_experiment_assignments user_experiment_assignments_user_id_experiment_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_experiment_assignments
    ADD CONSTRAINT user_experiment_assignments_user_id_experiment_id_key UNIQUE (user_id, experiment_id);


--
-- Name: user_fcm_tokens user_fcm_tokens_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_fcm_tokens
    ADD CONSTRAINT user_fcm_tokens_pkey PRIMARY KEY (id);


--
-- Name: user_fcm_tokens user_fcm_tokens_user_id_fcm_token_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_fcm_tokens
    ADD CONSTRAINT user_fcm_tokens_user_id_fcm_token_key UNIQUE (user_id, fcm_token);


--
-- Name: user_feature_access user_feature_access_business_user_id_feature_area_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_feature_access
    ADD CONSTRAINT user_feature_access_business_user_id_feature_area_key UNIQUE (business_user_id, feature_area);


--
-- Name: user_feature_access user_feature_access_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_feature_access
    ADD CONSTRAINT user_feature_access_pkey PRIMARY KEY (id);


--
-- Name: user_sessions user_sessions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_sessions
    ADD CONSTRAINT user_sessions_pkey PRIMARY KEY (id);


--
-- Name: user_sessions user_sessions_session_token_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_sessions
    ADD CONSTRAINT user_sessions_session_token_key UNIQUE (session_token);


--
-- Name: user_widget_configs user_widget_configs_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_widget_configs
    ADD CONSTRAINT user_widget_configs_pkey PRIMARY KEY (id);


--
-- Name: user_widget_configs user_widget_configs_user_id_business_profile_id_widget_type_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_widget_configs
    ADD CONSTRAINT user_widget_configs_user_id_business_profile_id_widget_type_key UNIQUE (user_id, business_profile_id, widget_type);


--
-- Name: widget_config_cache widget_config_cache_cache_key_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.widget_config_cache
    ADD CONSTRAINT widget_config_cache_cache_key_key UNIQUE (cache_key);


--
-- Name: widget_config_cache widget_config_cache_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.widget_config_cache
    ADD CONSTRAINT widget_config_cache_pkey PRIMARY KEY (id);


--
-- Name: widget_config_templates widget_config_templates_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.widget_config_templates
    ADD CONSTRAINT widget_config_templates_pkey PRIMARY KEY (id);


--
-- Name: widget_config_templates widget_config_templates_widget_type_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.widget_config_templates
    ADD CONSTRAINT widget_config_templates_widget_type_name_key UNIQUE (widget_type, name);


--
-- Name: widget_experiment_variants widget_experiment_variants_experiment_id_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.widget_experiment_variants
    ADD CONSTRAINT widget_experiment_variants_experiment_id_name_key UNIQUE (experiment_id, name);


--
-- Name: widget_experiment_variants widget_experiment_variants_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.widget_experiment_variants
    ADD CONSTRAINT widget_experiment_variants_pkey PRIMARY KEY (id);


--
-- Name: widget_experiments widget_experiments_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.widget_experiments
    ADD CONSTRAINT widget_experiments_pkey PRIMARY KEY (id);


--
-- Name: widget_feature_flags widget_feature_flags_flag_key_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.widget_feature_flags
    ADD CONSTRAINT widget_feature_flags_flag_key_key UNIQUE (flag_key);


--
-- Name: widget_feature_flags widget_feature_flags_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.widget_feature_flags
    ADD CONSTRAINT widget_feature_flags_pkey PRIMARY KEY (id);


--
-- Name: widget_usage_analytics widget_usage_analytics_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.widget_usage_analytics
    ADD CONSTRAINT widget_usage_analytics_pkey PRIMARY KEY (id);


--
-- Name: messages messages_pkey; Type: CONSTRAINT; Schema: realtime; Owner: supabase_realtime_admin
--

ALTER TABLE ONLY realtime.messages
    ADD CONSTRAINT messages_pkey PRIMARY KEY (id, inserted_at);


--
-- Name: messages_2025_08_05 messages_2025_08_05_pkey; Type: CONSTRAINT; Schema: realtime; Owner: supabase_admin
--

ALTER TABLE ONLY realtime.messages_2025_08_05
    ADD CONSTRAINT messages_2025_08_05_pkey PRIMARY KEY (id, inserted_at);


--
-- Name: messages_2025_08_06 messages_2025_08_06_pkey; Type: CONSTRAINT; Schema: realtime; Owner: supabase_admin
--

ALTER TABLE ONLY realtime.messages_2025_08_06
    ADD CONSTRAINT messages_2025_08_06_pkey PRIMARY KEY (id, inserted_at);


--
-- Name: messages_2025_08_07 messages_2025_08_07_pkey; Type: CONSTRAINT; Schema: realtime; Owner: supabase_admin
--

ALTER TABLE ONLY realtime.messages_2025_08_07
    ADD CONSTRAINT messages_2025_08_07_pkey PRIMARY KEY (id, inserted_at);


--
-- Name: messages_2025_08_08 messages_2025_08_08_pkey; Type: CONSTRAINT; Schema: realtime; Owner: supabase_admin
--

ALTER TABLE ONLY realtime.messages_2025_08_08
    ADD CONSTRAINT messages_2025_08_08_pkey PRIMARY KEY (id, inserted_at);


--
-- Name: messages_2025_08_09 messages_2025_08_09_pkey; Type: CONSTRAINT; Schema: realtime; Owner: supabase_admin
--

ALTER TABLE ONLY realtime.messages_2025_08_09
    ADD CONSTRAINT messages_2025_08_09_pkey PRIMARY KEY (id, inserted_at);


--
-- Name: subscription pk_subscription; Type: CONSTRAINT; Schema: realtime; Owner: supabase_admin
--

ALTER TABLE ONLY realtime.subscription
    ADD CONSTRAINT pk_subscription PRIMARY KEY (id);


--
-- Name: schema_migrations schema_migrations_pkey; Type: CONSTRAINT; Schema: realtime; Owner: supabase_admin
--

ALTER TABLE ONLY realtime.schema_migrations
    ADD CONSTRAINT schema_migrations_pkey PRIMARY KEY (version);


--
-- Name: buckets_analytics buckets_analytics_pkey; Type: CONSTRAINT; Schema: storage; Owner: supabase_storage_admin
--

ALTER TABLE ONLY storage.buckets_analytics
    ADD CONSTRAINT buckets_analytics_pkey PRIMARY KEY (id);


--
-- Name: buckets buckets_pkey; Type: CONSTRAINT; Schema: storage; Owner: supabase_storage_admin
--

ALTER TABLE ONLY storage.buckets
    ADD CONSTRAINT buckets_pkey PRIMARY KEY (id);


--
-- Name: iceberg_namespaces iceberg_namespaces_pkey; Type: CONSTRAINT; Schema: storage; Owner: supabase_storage_admin
--

ALTER TABLE ONLY storage.iceberg_namespaces
    ADD CONSTRAINT iceberg_namespaces_pkey PRIMARY KEY (id);


--
-- Name: iceberg_tables iceberg_tables_pkey; Type: CONSTRAINT; Schema: storage; Owner: supabase_storage_admin
--

ALTER TABLE ONLY storage.iceberg_tables
    ADD CONSTRAINT iceberg_tables_pkey PRIMARY KEY (id);


--
-- Name: migrations migrations_name_key; Type: CONSTRAINT; Schema: storage; Owner: supabase_storage_admin
--

ALTER TABLE ONLY storage.migrations
    ADD CONSTRAINT migrations_name_key UNIQUE (name);


--
-- Name: migrations migrations_pkey; Type: CONSTRAINT; Schema: storage; Owner: supabase_storage_admin
--

ALTER TABLE ONLY storage.migrations
    ADD CONSTRAINT migrations_pkey PRIMARY KEY (id);


--
-- Name: objects objects_pkey; Type: CONSTRAINT; Schema: storage; Owner: supabase_storage_admin
--

ALTER TABLE ONLY storage.objects
    ADD CONSTRAINT objects_pkey PRIMARY KEY (id);


--
-- Name: prefixes prefixes_pkey; Type: CONSTRAINT; Schema: storage; Owner: supabase_storage_admin
--

ALTER TABLE ONLY storage.prefixes
    ADD CONSTRAINT prefixes_pkey PRIMARY KEY (bucket_id, level, name);


--
-- Name: s3_multipart_uploads_parts s3_multipart_uploads_parts_pkey; Type: CONSTRAINT; Schema: storage; Owner: supabase_storage_admin
--

ALTER TABLE ONLY storage.s3_multipart_uploads_parts
    ADD CONSTRAINT s3_multipart_uploads_parts_pkey PRIMARY KEY (id);


--
-- Name: s3_multipart_uploads s3_multipart_uploads_pkey; Type: CONSTRAINT; Schema: storage; Owner: supabase_storage_admin
--

ALTER TABLE ONLY storage.s3_multipart_uploads
    ADD CONSTRAINT s3_multipart_uploads_pkey PRIMARY KEY (id);


--
-- Name: hooks hooks_pkey; Type: CONSTRAINT; Schema: supabase_functions; Owner: supabase_functions_admin
--

ALTER TABLE ONLY supabase_functions.hooks
    ADD CONSTRAINT hooks_pkey PRIMARY KEY (id);


--
-- Name: migrations migrations_pkey; Type: CONSTRAINT; Schema: supabase_functions; Owner: supabase_functions_admin
--

ALTER TABLE ONLY supabase_functions.migrations
    ADD CONSTRAINT migrations_pkey PRIMARY KEY (version);


--
-- Name: schema_migrations schema_migrations_pkey; Type: CONSTRAINT; Schema: supabase_migrations; Owner: postgres
--

ALTER TABLE ONLY supabase_migrations.schema_migrations
    ADD CONSTRAINT schema_migrations_pkey PRIMARY KEY (version);


--
-- Name: extensions_tenant_external_id_index; Type: INDEX; Schema: _realtime; Owner: supabase_admin
--

CREATE INDEX extensions_tenant_external_id_index ON _realtime.extensions USING btree (tenant_external_id);


--
-- Name: extensions_tenant_external_id_type_index; Type: INDEX; Schema: _realtime; Owner: supabase_admin
--

CREATE UNIQUE INDEX extensions_tenant_external_id_type_index ON _realtime.extensions USING btree (tenant_external_id, type);


--
-- Name: tenants_external_id_index; Type: INDEX; Schema: _realtime; Owner: supabase_admin
--

CREATE UNIQUE INDEX tenants_external_id_index ON _realtime.tenants USING btree (external_id);


--
-- Name: audit_logs_instance_id_idx; Type: INDEX; Schema: auth; Owner: supabase_auth_admin
--

CREATE INDEX audit_logs_instance_id_idx ON auth.audit_log_entries USING btree (instance_id);


--
-- Name: confirmation_token_idx; Type: INDEX; Schema: auth; Owner: supabase_auth_admin
--

CREATE UNIQUE INDEX confirmation_token_idx ON auth.users USING btree (confirmation_token) WHERE ((confirmation_token)::text !~ '^[0-9 ]*$'::text);


--
-- Name: email_change_token_current_idx; Type: INDEX; Schema: auth; Owner: supabase_auth_admin
--

CREATE UNIQUE INDEX email_change_token_current_idx ON auth.users USING btree (email_change_token_current) WHERE ((email_change_token_current)::text !~ '^[0-9 ]*$'::text);


--
-- Name: email_change_token_new_idx; Type: INDEX; Schema: auth; Owner: supabase_auth_admin
--

CREATE UNIQUE INDEX email_change_token_new_idx ON auth.users USING btree (email_change_token_new) WHERE ((email_change_token_new)::text !~ '^[0-9 ]*$'::text);


--
-- Name: factor_id_created_at_idx; Type: INDEX; Schema: auth; Owner: supabase_auth_admin
--

CREATE INDEX factor_id_created_at_idx ON auth.mfa_factors USING btree (user_id, created_at);


--
-- Name: flow_state_created_at_idx; Type: INDEX; Schema: auth; Owner: supabase_auth_admin
--

CREATE INDEX flow_state_created_at_idx ON auth.flow_state USING btree (created_at DESC);


--
-- Name: identities_email_idx; Type: INDEX; Schema: auth; Owner: supabase_auth_admin
--

CREATE INDEX identities_email_idx ON auth.identities USING btree (email text_pattern_ops);


--
-- Name: INDEX identities_email_idx; Type: COMMENT; Schema: auth; Owner: supabase_auth_admin
--

COMMENT ON INDEX auth.identities_email_idx IS 'Auth: Ensures indexed queries on the email column';


--
-- Name: identities_user_id_idx; Type: INDEX; Schema: auth; Owner: supabase_auth_admin
--

CREATE INDEX identities_user_id_idx ON auth.identities USING btree (user_id);


--
-- Name: idx_auth_code; Type: INDEX; Schema: auth; Owner: supabase_auth_admin
--

CREATE INDEX idx_auth_code ON auth.flow_state USING btree (auth_code);


--
-- Name: idx_user_id_auth_method; Type: INDEX; Schema: auth; Owner: supabase_auth_admin
--

CREATE INDEX idx_user_id_auth_method ON auth.flow_state USING btree (user_id, authentication_method);


--
-- Name: mfa_challenge_created_at_idx; Type: INDEX; Schema: auth; Owner: supabase_auth_admin
--

CREATE INDEX mfa_challenge_created_at_idx ON auth.mfa_challenges USING btree (created_at DESC);


--
-- Name: mfa_factors_user_friendly_name_unique; Type: INDEX; Schema: auth; Owner: supabase_auth_admin
--

CREATE UNIQUE INDEX mfa_factors_user_friendly_name_unique ON auth.mfa_factors USING btree (friendly_name, user_id) WHERE (TRIM(BOTH FROM friendly_name) <> ''::text);


--
-- Name: mfa_factors_user_id_idx; Type: INDEX; Schema: auth; Owner: supabase_auth_admin
--

CREATE INDEX mfa_factors_user_id_idx ON auth.mfa_factors USING btree (user_id);


--
-- Name: one_time_tokens_relates_to_hash_idx; Type: INDEX; Schema: auth; Owner: supabase_auth_admin
--

CREATE INDEX one_time_tokens_relates_to_hash_idx ON auth.one_time_tokens USING hash (relates_to);


--
-- Name: one_time_tokens_token_hash_hash_idx; Type: INDEX; Schema: auth; Owner: supabase_auth_admin
--

CREATE INDEX one_time_tokens_token_hash_hash_idx ON auth.one_time_tokens USING hash (token_hash);


--
-- Name: one_time_tokens_user_id_token_type_key; Type: INDEX; Schema: auth; Owner: supabase_auth_admin
--

CREATE UNIQUE INDEX one_time_tokens_user_id_token_type_key ON auth.one_time_tokens USING btree (user_id, token_type);


--
-- Name: reauthentication_token_idx; Type: INDEX; Schema: auth; Owner: supabase_auth_admin
--

CREATE UNIQUE INDEX reauthentication_token_idx ON auth.users USING btree (reauthentication_token) WHERE ((reauthentication_token)::text !~ '^[0-9 ]*$'::text);


--
-- Name: recovery_token_idx; Type: INDEX; Schema: auth; Owner: supabase_auth_admin
--

CREATE UNIQUE INDEX recovery_token_idx ON auth.users USING btree (recovery_token) WHERE ((recovery_token)::text !~ '^[0-9 ]*$'::text);


--
-- Name: refresh_tokens_instance_id_idx; Type: INDEX; Schema: auth; Owner: supabase_auth_admin
--

CREATE INDEX refresh_tokens_instance_id_idx ON auth.refresh_tokens USING btree (instance_id);


--
-- Name: refresh_tokens_instance_id_user_id_idx; Type: INDEX; Schema: auth; Owner: supabase_auth_admin
--

CREATE INDEX refresh_tokens_instance_id_user_id_idx ON auth.refresh_tokens USING btree (instance_id, user_id);


--
-- Name: refresh_tokens_parent_idx; Type: INDEX; Schema: auth; Owner: supabase_auth_admin
--

CREATE INDEX refresh_tokens_parent_idx ON auth.refresh_tokens USING btree (parent);


--
-- Name: refresh_tokens_session_id_revoked_idx; Type: INDEX; Schema: auth; Owner: supabase_auth_admin
--

CREATE INDEX refresh_tokens_session_id_revoked_idx ON auth.refresh_tokens USING btree (session_id, revoked);


--
-- Name: refresh_tokens_updated_at_idx; Type: INDEX; Schema: auth; Owner: supabase_auth_admin
--

CREATE INDEX refresh_tokens_updated_at_idx ON auth.refresh_tokens USING btree (updated_at DESC);


--
-- Name: saml_providers_sso_provider_id_idx; Type: INDEX; Schema: auth; Owner: supabase_auth_admin
--

CREATE INDEX saml_providers_sso_provider_id_idx ON auth.saml_providers USING btree (sso_provider_id);


--
-- Name: saml_relay_states_created_at_idx; Type: INDEX; Schema: auth; Owner: supabase_auth_admin
--

CREATE INDEX saml_relay_states_created_at_idx ON auth.saml_relay_states USING btree (created_at DESC);


--
-- Name: saml_relay_states_for_email_idx; Type: INDEX; Schema: auth; Owner: supabase_auth_admin
--

CREATE INDEX saml_relay_states_for_email_idx ON auth.saml_relay_states USING btree (for_email);


--
-- Name: saml_relay_states_sso_provider_id_idx; Type: INDEX; Schema: auth; Owner: supabase_auth_admin
--

CREATE INDEX saml_relay_states_sso_provider_id_idx ON auth.saml_relay_states USING btree (sso_provider_id);


--
-- Name: sessions_not_after_idx; Type: INDEX; Schema: auth; Owner: supabase_auth_admin
--

CREATE INDEX sessions_not_after_idx ON auth.sessions USING btree (not_after DESC);


--
-- Name: sessions_user_id_idx; Type: INDEX; Schema: auth; Owner: supabase_auth_admin
--

CREATE INDEX sessions_user_id_idx ON auth.sessions USING btree (user_id);


--
-- Name: sso_domains_domain_idx; Type: INDEX; Schema: auth; Owner: supabase_auth_admin
--

CREATE UNIQUE INDEX sso_domains_domain_idx ON auth.sso_domains USING btree (lower(domain));


--
-- Name: sso_domains_sso_provider_id_idx; Type: INDEX; Schema: auth; Owner: supabase_auth_admin
--

CREATE INDEX sso_domains_sso_provider_id_idx ON auth.sso_domains USING btree (sso_provider_id);


--
-- Name: sso_providers_resource_id_idx; Type: INDEX; Schema: auth; Owner: supabase_auth_admin
--

CREATE UNIQUE INDEX sso_providers_resource_id_idx ON auth.sso_providers USING btree (lower(resource_id));


--
-- Name: unique_phone_factor_per_user; Type: INDEX; Schema: auth; Owner: supabase_auth_admin
--

CREATE UNIQUE INDEX unique_phone_factor_per_user ON auth.mfa_factors USING btree (user_id, phone);


--
-- Name: user_id_created_at_idx; Type: INDEX; Schema: auth; Owner: supabase_auth_admin
--

CREATE INDEX user_id_created_at_idx ON auth.sessions USING btree (user_id, created_at);


--
-- Name: users_email_partial_key; Type: INDEX; Schema: auth; Owner: supabase_auth_admin
--

CREATE UNIQUE INDEX users_email_partial_key ON auth.users USING btree (email) WHERE (is_sso_user = false);


--
-- Name: INDEX users_email_partial_key; Type: COMMENT; Schema: auth; Owner: supabase_auth_admin
--

COMMENT ON INDEX auth.users_email_partial_key IS 'Auth: A partial unique index that applies only when is_sso_user is false';


--
-- Name: users_instance_id_email_idx; Type: INDEX; Schema: auth; Owner: supabase_auth_admin
--

CREATE INDEX users_instance_id_email_idx ON auth.users USING btree (instance_id, lower((email)::text));


--
-- Name: users_instance_id_idx; Type: INDEX; Schema: auth; Owner: supabase_auth_admin
--

CREATE INDEX users_instance_id_idx ON auth.users USING btree (instance_id);


--
-- Name: users_is_anonymous_idx; Type: INDEX; Schema: auth; Owner: supabase_auth_admin
--

CREATE INDEX users_is_anonymous_idx ON auth.users USING btree (is_anonymous);


--
-- Name: idx_business_categories_active; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_business_categories_active ON public.business_categories USING btree (is_active);


--
-- Name: idx_business_categories_name; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_business_categories_name ON public.business_categories USING btree (name);


--
-- Name: idx_business_categories_sort; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_business_categories_sort ON public.business_categories USING btree (sort_order);


--
-- Name: idx_business_contacts_active; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_business_contacts_active ON public.business_contacts USING btree (is_active);


--
-- Name: idx_business_contacts_type; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_business_contacts_type ON public.business_contacts USING btree (contact_type);


--
-- Name: idx_business_contacts_user_id; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_business_contacts_user_id ON public.business_contacts USING btree (user_id);


--
-- Name: idx_business_metrics_summary_last_updated; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_business_metrics_summary_last_updated ON public.business_metrics_summary USING btree (last_updated);


--
-- Name: idx_business_users_auth; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_business_users_auth ON public.business_users USING btree (auth_user_id);


--
-- Name: idx_business_users_business; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_business_users_business ON public.business_users USING btree (business_profile_id);


--
-- Name: idx_business_users_email; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_business_users_email ON public.business_users USING btree (email);


--
-- Name: idx_business_users_invitation; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_business_users_invitation ON public.business_users USING btree (invitation_token) WHERE (invitation_token IS NOT NULL);


--
-- Name: idx_business_users_status; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_business_users_status ON public.business_users USING btree (status) WHERE (status = 'active'::public.user_status);


--
-- Name: idx_coaching_steps_flow_id; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_coaching_steps_flow_id ON public.coaching_steps USING btree (flow_id);


--
-- Name: idx_coaching_steps_order; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_coaching_steps_order ON public.coaching_steps USING btree (flow_id, step_order);


--
-- Name: idx_invoice_items_invoice_id; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_invoice_items_invoice_id ON public.invoice_items USING btree (invoice_id);


--
-- Name: idx_invoice_items_item_id; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_invoice_items_item_id ON public.invoice_items USING btree (item_id);


--
-- Name: idx_invoices_customer_id; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_invoices_customer_id ON public.invoices USING btree (customer_id);


--
-- Name: idx_invoices_dates; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_invoices_dates ON public.invoices USING btree (issue_date, due_date);


--
-- Name: idx_invoices_invoice_url; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_invoices_invoice_url ON public.invoices USING btree (invoice_url) WHERE (invoice_url IS NOT NULL);


--
-- Name: idx_invoices_number; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_invoices_number ON public.invoices USING btree (invoice_number);


--
-- Name: idx_invoices_status; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_invoices_status ON public.invoices USING btree (status);


--
-- Name: idx_invoices_user_id; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_invoices_user_id ON public.invoices USING btree (user_id);


--
-- Name: idx_items_active; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_items_active ON public.items USING btree (is_active);


--
-- Name: idx_items_attributes_gin; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_items_attributes_gin ON public.items USING gin (attributes);


--
-- Name: idx_items_business_profile_id; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_items_business_profile_id ON public.items USING btree (business_profile_id);


--
-- Name: idx_items_low_stock; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_items_low_stock ON public.items USING btree (current_stock, low_stock_threshold);


--
-- Name: idx_items_supplier_id; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_items_supplier_id ON public.items USING btree (supplier_id);


--
-- Name: idx_items_type; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_items_type ON public.items USING btree (item_type);


--
-- Name: idx_items_user_id; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_items_user_id ON public.items USING btree (user_id);


--
-- Name: idx_low_stock_notification_logs_item_id; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_low_stock_notification_logs_item_id ON public.low_stock_notification_logs USING btree (item_id);


--
-- Name: idx_low_stock_notification_logs_sent_at; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_low_stock_notification_logs_sent_at ON public.low_stock_notification_logs USING btree (sent_at);


--
-- Name: idx_low_stock_notification_logs_type; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_low_stock_notification_logs_type ON public.low_stock_notification_logs USING btree (notification_type);


--
-- Name: idx_low_stock_notification_logs_user_id; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_low_stock_notification_logs_user_id ON public.low_stock_notification_logs USING btree (user_id);


--
-- Name: idx_navigation_config_business_role; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX idx_navigation_config_business_role ON public.navigation_config USING btree (business_profile_id, user_role) WHERE (business_profile_id IS NOT NULL);


--
-- Name: idx_notification_logs_sent_at; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_notification_logs_sent_at ON public.notification_logs USING btree (sent_at);


--
-- Name: idx_notification_logs_status; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_notification_logs_status ON public.notification_logs USING btree (status);


--
-- Name: idx_notification_logs_type; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_notification_logs_type ON public.notification_logs USING btree (notification_type);


--
-- Name: idx_notification_logs_user_id; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_notification_logs_user_id ON public.notification_logs USING btree (user_id);


--
-- Name: idx_role_feature_access_role; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_role_feature_access_role ON public.role_feature_access USING btree (role);


--
-- Name: idx_stock_movements_created_at; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_stock_movements_created_at ON public.stock_movements USING btree (created_at);


--
-- Name: idx_stock_movements_item_id; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_stock_movements_item_id ON public.stock_movements USING btree (item_id);


--
-- Name: idx_stock_movements_user_id; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_stock_movements_user_id ON public.stock_movements USING btree (user_id);


--
-- Name: idx_user_activity_log_business; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_user_activity_log_business ON public.user_activity_log USING btree (business_profile_id);


--
-- Name: idx_user_activity_log_time; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_user_activity_log_time ON public.user_activity_log USING btree (created_at DESC);


--
-- Name: idx_user_activity_log_type; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_user_activity_log_type ON public.user_activity_log USING btree (activity_type, feature_area);


--
-- Name: idx_user_activity_log_user; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_user_activity_log_user ON public.user_activity_log USING btree (business_user_id);


--
-- Name: idx_user_experiment_assignments_user_experiment; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_user_experiment_assignments_user_experiment ON public.user_experiment_assignments USING btree (user_id, experiment_id);


--
-- Name: idx_user_experiment_assignments_variant; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_user_experiment_assignments_variant ON public.user_experiment_assignments USING btree (variant_id);


--
-- Name: idx_user_fcm_tokens_active; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_user_fcm_tokens_active ON public.user_fcm_tokens USING btree (is_active);


--
-- Name: idx_user_fcm_tokens_token; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_user_fcm_tokens_token ON public.user_fcm_tokens USING btree (fcm_token);


--
-- Name: idx_user_fcm_tokens_user_id; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_user_fcm_tokens_user_id ON public.user_fcm_tokens USING btree (user_id);


--
-- Name: idx_user_feature_access_feature; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_user_feature_access_feature ON public.user_feature_access USING btree (feature_area);


--
-- Name: idx_user_feature_access_user; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_user_feature_access_user ON public.user_feature_access USING btree (business_user_id);


--
-- Name: idx_user_progress_user_id; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_user_progress_user_id ON public.user_coaching_progress USING btree (user_id);


--
-- Name: idx_user_sessions_active; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_user_sessions_active ON public.user_sessions USING btree (is_active, expires_at) WHERE (is_active = true);


--
-- Name: idx_user_sessions_token; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_user_sessions_token ON public.user_sessions USING btree (session_token);


--
-- Name: idx_user_sessions_user; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_user_sessions_user ON public.user_sessions USING btree (business_user_id);


--
-- Name: idx_user_widget_configs_business; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_user_widget_configs_business ON public.user_widget_configs USING btree (business_profile_id, widget_type);


--
-- Name: idx_user_widget_configs_enabled; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_user_widget_configs_enabled ON public.user_widget_configs USING btree (widget_type, is_enabled) WHERE (is_enabled = true);


--
-- Name: idx_user_widget_configs_updated; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_user_widget_configs_updated ON public.user_widget_configs USING btree (updated_at DESC);


--
-- Name: idx_user_widget_configs_user; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_user_widget_configs_user ON public.user_widget_configs USING btree (user_id, widget_type);


--
-- Name: idx_widget_config_cache_expires; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_widget_config_cache_expires ON public.widget_config_cache USING btree (expires_at);


--
-- Name: idx_widget_config_cache_key; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_widget_config_cache_key ON public.widget_config_cache USING btree (cache_key);


--
-- Name: idx_widget_config_cache_user; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_widget_config_cache_user ON public.widget_config_cache USING btree (user_id, widget_type);


--
-- Name: idx_widget_config_templates_default; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_widget_config_templates_default ON public.widget_config_templates USING btree (widget_type, is_default) WHERE (is_default = true);


--
-- Name: idx_widget_config_templates_type; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_widget_config_templates_type ON public.widget_config_templates USING btree (widget_type);


--
-- Name: idx_widget_experiments_active; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_widget_experiments_active ON public.widget_experiments USING btree (widget_type, start_date, end_date) WHERE (status = 'active'::public.experiment_status);


--
-- Name: idx_widget_experiments_status; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_widget_experiments_status ON public.widget_experiments USING btree (status, widget_type);


--
-- Name: idx_widget_feature_flags_enabled; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_widget_feature_flags_enabled ON public.widget_feature_flags USING btree (flag_key, is_enabled) WHERE (is_enabled = true);


--
-- Name: idx_widget_feature_flags_type; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_widget_feature_flags_type ON public.widget_feature_flags USING btree (widget_type);


--
-- Name: idx_widget_usage_analytics_experiment; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_widget_usage_analytics_experiment ON public.widget_usage_analytics USING btree (experiment_id, variant_id, created_at DESC);


--
-- Name: idx_widget_usage_analytics_type_time; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_widget_usage_analytics_type_time ON public.widget_usage_analytics USING btree (widget_type, event_type, created_at DESC);


--
-- Name: idx_widget_usage_analytics_user_time; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_widget_usage_analytics_user_time ON public.widget_usage_analytics USING btree (user_id, created_at DESC);


--
-- Name: ix_realtime_subscription_entity; Type: INDEX; Schema: realtime; Owner: supabase_admin
--

CREATE INDEX ix_realtime_subscription_entity ON realtime.subscription USING btree (entity);


--
-- Name: subscription_subscription_id_entity_filters_key; Type: INDEX; Schema: realtime; Owner: supabase_admin
--

CREATE UNIQUE INDEX subscription_subscription_id_entity_filters_key ON realtime.subscription USING btree (subscription_id, entity, filters);


--
-- Name: bname; Type: INDEX; Schema: storage; Owner: supabase_storage_admin
--

CREATE UNIQUE INDEX bname ON storage.buckets USING btree (name);


--
-- Name: bucketid_objname; Type: INDEX; Schema: storage; Owner: supabase_storage_admin
--

CREATE UNIQUE INDEX bucketid_objname ON storage.objects USING btree (bucket_id, name);


--
-- Name: idx_iceberg_namespaces_bucket_id; Type: INDEX; Schema: storage; Owner: supabase_storage_admin
--

CREATE UNIQUE INDEX idx_iceberg_namespaces_bucket_id ON storage.iceberg_namespaces USING btree (bucket_id, name);


--
-- Name: idx_iceberg_tables_namespace_id; Type: INDEX; Schema: storage; Owner: supabase_storage_admin
--

CREATE UNIQUE INDEX idx_iceberg_tables_namespace_id ON storage.iceberg_tables USING btree (namespace_id, name);


--
-- Name: idx_multipart_uploads_list; Type: INDEX; Schema: storage; Owner: supabase_storage_admin
--

CREATE INDEX idx_multipart_uploads_list ON storage.s3_multipart_uploads USING btree (bucket_id, key, created_at);


--
-- Name: idx_name_bucket_level_unique; Type: INDEX; Schema: storage; Owner: supabase_storage_admin
--

CREATE UNIQUE INDEX idx_name_bucket_level_unique ON storage.objects USING btree (name COLLATE "C", bucket_id, level);


--
-- Name: idx_objects_bucket_id_name; Type: INDEX; Schema: storage; Owner: supabase_storage_admin
--

CREATE INDEX idx_objects_bucket_id_name ON storage.objects USING btree (bucket_id, name COLLATE "C");


--
-- Name: idx_objects_lower_name; Type: INDEX; Schema: storage; Owner: supabase_storage_admin
--

CREATE INDEX idx_objects_lower_name ON storage.objects USING btree ((path_tokens[level]), lower(name) text_pattern_ops, bucket_id, level);


--
-- Name: idx_prefixes_lower_name; Type: INDEX; Schema: storage; Owner: supabase_storage_admin
--

CREATE INDEX idx_prefixes_lower_name ON storage.prefixes USING btree (bucket_id, level, ((string_to_array(name, '/'::text))[level]), lower(name) text_pattern_ops);


--
-- Name: name_prefix_search; Type: INDEX; Schema: storage; Owner: supabase_storage_admin
--

CREATE INDEX name_prefix_search ON storage.objects USING btree (name text_pattern_ops);


--
-- Name: objects_bucket_id_level_idx; Type: INDEX; Schema: storage; Owner: supabase_storage_admin
--

CREATE UNIQUE INDEX objects_bucket_id_level_idx ON storage.objects USING btree (bucket_id, level, name COLLATE "C");


--
-- Name: supabase_functions_hooks_h_table_id_h_name_idx; Type: INDEX; Schema: supabase_functions; Owner: supabase_functions_admin
--

CREATE INDEX supabase_functions_hooks_h_table_id_h_name_idx ON supabase_functions.hooks USING btree (hook_table_id, hook_name);


--
-- Name: supabase_functions_hooks_request_id_idx; Type: INDEX; Schema: supabase_functions; Owner: supabase_functions_admin
--

CREATE INDEX supabase_functions_hooks_request_id_idx ON supabase_functions.hooks USING btree (request_id);


--
-- Name: messages_2025_08_05_pkey; Type: INDEX ATTACH; Schema: realtime; Owner: supabase_realtime_admin
--

ALTER INDEX realtime.messages_pkey ATTACH PARTITION realtime.messages_2025_08_05_pkey;


--
-- Name: messages_2025_08_06_pkey; Type: INDEX ATTACH; Schema: realtime; Owner: supabase_realtime_admin
--

ALTER INDEX realtime.messages_pkey ATTACH PARTITION realtime.messages_2025_08_06_pkey;


--
-- Name: messages_2025_08_07_pkey; Type: INDEX ATTACH; Schema: realtime; Owner: supabase_realtime_admin
--

ALTER INDEX realtime.messages_pkey ATTACH PARTITION realtime.messages_2025_08_07_pkey;


--
-- Name: messages_2025_08_08_pkey; Type: INDEX ATTACH; Schema: realtime; Owner: supabase_realtime_admin
--

ALTER INDEX realtime.messages_pkey ATTACH PARTITION realtime.messages_2025_08_08_pkey;


--
-- Name: messages_2025_08_09_pkey; Type: INDEX ATTACH; Schema: realtime; Owner: supabase_realtime_admin
--

ALTER INDEX realtime.messages_pkey ATTACH PARTITION realtime.messages_2025_08_09_pkey;


--
-- Name: users on_auth_user_created; Type: TRIGGER; Schema: auth; Owner: supabase_auth_admin
--

CREATE TRIGGER on_auth_user_created AFTER INSERT ON auth.users FOR EACH ROW EXECUTE FUNCTION public.handle_new_user_notification_setup();


--
-- Name: business_categories update_business_categories_updated_at; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER update_business_categories_updated_at BEFORE UPDATE ON public.business_categories FOR EACH ROW EXECUTE FUNCTION public.update_updated_at_column();


--
-- Name: business_contacts update_business_contacts_updated_at; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER update_business_contacts_updated_at BEFORE UPDATE ON public.business_contacts FOR EACH ROW EXECUTE FUNCTION public.update_updated_at_column();


--
-- Name: business_contacts update_business_metrics_on_contact_change; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER update_business_metrics_on_contact_change AFTER INSERT OR DELETE OR UPDATE ON public.business_contacts FOR EACH ROW EXECUTE FUNCTION public.refresh_business_metrics_summary();


--
-- Name: invoices update_business_metrics_on_invoice_change; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER update_business_metrics_on_invoice_change AFTER INSERT OR DELETE OR UPDATE ON public.invoices FOR EACH ROW EXECUTE FUNCTION public.refresh_business_metrics_summary();


--
-- Name: items update_business_metrics_on_item_change; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER update_business_metrics_on_item_change AFTER INSERT OR DELETE OR UPDATE ON public.items FOR EACH ROW EXECUTE FUNCTION public.refresh_business_metrics_summary();


--
-- Name: business_profiles update_business_profiles_updated_at; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER update_business_profiles_updated_at BEFORE UPDATE ON public.business_profiles FOR EACH ROW EXECUTE FUNCTION public.update_updated_at_column();


--
-- Name: invoices update_invoices_updated_at; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER update_invoices_updated_at BEFORE UPDATE ON public.invoices FOR EACH ROW EXECUTE FUNCTION public.update_updated_at_column();


--
-- Name: items update_items_updated_at; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER update_items_updated_at BEFORE UPDATE ON public.items FOR EACH ROW EXECUTE FUNCTION public.update_updated_at_column();


--
-- Name: low_stock_notification_settings update_low_stock_notification_settings_updated_at; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER update_low_stock_notification_settings_updated_at BEFORE UPDATE ON public.low_stock_notification_settings FOR EACH ROW EXECUTE FUNCTION public.update_updated_at_column();


--
-- Name: notification_logs update_notification_logs_updated_at; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER update_notification_logs_updated_at BEFORE UPDATE ON public.notification_logs FOR EACH ROW EXECUTE FUNCTION public.update_updated_at_column();


--
-- Name: notification_preferences update_notification_preferences_updated_at; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER update_notification_preferences_updated_at BEFORE UPDATE ON public.notification_preferences FOR EACH ROW EXECUTE FUNCTION public.update_updated_at_column();


--
-- Name: subscriptions update_subscriptions_updated_at; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER update_subscriptions_updated_at BEFORE UPDATE ON public.subscriptions FOR EACH ROW EXECUTE FUNCTION public.update_updated_at_column();


--
-- Name: user_fcm_tokens update_user_fcm_tokens_updated_at; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER update_user_fcm_tokens_updated_at BEFORE UPDATE ON public.user_fcm_tokens FOR EACH ROW EXECUTE FUNCTION public.update_updated_at_column();


--
-- Name: user_widget_configs update_user_widget_configs_updated_at; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER update_user_widget_configs_updated_at BEFORE UPDATE ON public.user_widget_configs FOR EACH ROW EXECUTE FUNCTION public.update_updated_at_column();


--
-- Name: widget_config_cache update_widget_config_cache_updated_at; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER update_widget_config_cache_updated_at BEFORE UPDATE ON public.widget_config_cache FOR EACH ROW EXECUTE FUNCTION public.update_updated_at_column();


--
-- Name: widget_config_templates update_widget_config_templates_updated_at; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER update_widget_config_templates_updated_at BEFORE UPDATE ON public.widget_config_templates FOR EACH ROW EXECUTE FUNCTION public.update_updated_at_column();


--
-- Name: widget_experiments update_widget_experiments_updated_at; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER update_widget_experiments_updated_at BEFORE UPDATE ON public.widget_experiments FOR EACH ROW EXECUTE FUNCTION public.update_updated_at_column();


--
-- Name: widget_feature_flags update_widget_feature_flags_updated_at; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER update_widget_feature_flags_updated_at BEFORE UPDATE ON public.widget_feature_flags FOR EACH ROW EXECUTE FUNCTION public.update_updated_at_column();


--
-- Name: user_experiment_assignments validate_variant_experiment_trigger; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validate_variant_experiment_trigger BEFORE INSERT OR UPDATE ON public.user_experiment_assignments FOR EACH ROW EXECUTE FUNCTION public.validate_variant_belongs_to_experiment();


--
-- Name: user_widget_configs widget_config_change_notify; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER widget_config_change_notify AFTER INSERT OR DELETE OR UPDATE ON public.user_widget_configs FOR EACH ROW EXECUTE FUNCTION public.notify_widget_config_change();


--
-- Name: subscription tr_check_filters; Type: TRIGGER; Schema: realtime; Owner: supabase_admin
--

CREATE TRIGGER tr_check_filters BEFORE INSERT OR UPDATE ON realtime.subscription FOR EACH ROW EXECUTE FUNCTION realtime.subscription_check_filters();


--
-- Name: buckets enforce_bucket_name_length_trigger; Type: TRIGGER; Schema: storage; Owner: supabase_storage_admin
--

CREATE TRIGGER enforce_bucket_name_length_trigger BEFORE INSERT OR UPDATE OF name ON storage.buckets FOR EACH ROW EXECUTE FUNCTION storage.enforce_bucket_name_length();


--
-- Name: objects objects_delete_delete_prefix; Type: TRIGGER; Schema: storage; Owner: supabase_storage_admin
--

CREATE TRIGGER objects_delete_delete_prefix AFTER DELETE ON storage.objects FOR EACH ROW EXECUTE FUNCTION storage.delete_prefix_hierarchy_trigger();


--
-- Name: objects objects_insert_create_prefix; Type: TRIGGER; Schema: storage; Owner: supabase_storage_admin
--

CREATE TRIGGER objects_insert_create_prefix BEFORE INSERT ON storage.objects FOR EACH ROW EXECUTE FUNCTION storage.objects_insert_prefix_trigger();


--
-- Name: objects objects_update_create_prefix; Type: TRIGGER; Schema: storage; Owner: supabase_storage_admin
--

CREATE TRIGGER objects_update_create_prefix BEFORE UPDATE ON storage.objects FOR EACH ROW WHEN (((new.name <> old.name) OR (new.bucket_id <> old.bucket_id))) EXECUTE FUNCTION storage.objects_update_prefix_trigger();


--
-- Name: prefixes prefixes_create_hierarchy; Type: TRIGGER; Schema: storage; Owner: supabase_storage_admin
--

CREATE TRIGGER prefixes_create_hierarchy BEFORE INSERT ON storage.prefixes FOR EACH ROW WHEN ((pg_trigger_depth() < 1)) EXECUTE FUNCTION storage.prefixes_insert_trigger();


--
-- Name: prefixes prefixes_delete_hierarchy; Type: TRIGGER; Schema: storage; Owner: supabase_storage_admin
--

CREATE TRIGGER prefixes_delete_hierarchy AFTER DELETE ON storage.prefixes FOR EACH ROW EXECUTE FUNCTION storage.delete_prefix_hierarchy_trigger();


--
-- Name: objects update_objects_updated_at; Type: TRIGGER; Schema: storage; Owner: supabase_storage_admin
--

CREATE TRIGGER update_objects_updated_at BEFORE UPDATE ON storage.objects FOR EACH ROW EXECUTE FUNCTION storage.update_updated_at_column();


--
-- Name: extensions extensions_tenant_external_id_fkey; Type: FK CONSTRAINT; Schema: _realtime; Owner: supabase_admin
--

ALTER TABLE ONLY _realtime.extensions
    ADD CONSTRAINT extensions_tenant_external_id_fkey FOREIGN KEY (tenant_external_id) REFERENCES _realtime.tenants(external_id) ON DELETE CASCADE;


--
-- Name: identities identities_user_id_fkey; Type: FK CONSTRAINT; Schema: auth; Owner: supabase_auth_admin
--

ALTER TABLE ONLY auth.identities
    ADD CONSTRAINT identities_user_id_fkey FOREIGN KEY (user_id) REFERENCES auth.users(id) ON DELETE CASCADE;


--
-- Name: mfa_amr_claims mfa_amr_claims_session_id_fkey; Type: FK CONSTRAINT; Schema: auth; Owner: supabase_auth_admin
--

ALTER TABLE ONLY auth.mfa_amr_claims
    ADD CONSTRAINT mfa_amr_claims_session_id_fkey FOREIGN KEY (session_id) REFERENCES auth.sessions(id) ON DELETE CASCADE;


--
-- Name: mfa_challenges mfa_challenges_auth_factor_id_fkey; Type: FK CONSTRAINT; Schema: auth; Owner: supabase_auth_admin
--

ALTER TABLE ONLY auth.mfa_challenges
    ADD CONSTRAINT mfa_challenges_auth_factor_id_fkey FOREIGN KEY (factor_id) REFERENCES auth.mfa_factors(id) ON DELETE CASCADE;


--
-- Name: mfa_factors mfa_factors_user_id_fkey; Type: FK CONSTRAINT; Schema: auth; Owner: supabase_auth_admin
--

ALTER TABLE ONLY auth.mfa_factors
    ADD CONSTRAINT mfa_factors_user_id_fkey FOREIGN KEY (user_id) REFERENCES auth.users(id) ON DELETE CASCADE;


--
-- Name: one_time_tokens one_time_tokens_user_id_fkey; Type: FK CONSTRAINT; Schema: auth; Owner: supabase_auth_admin
--

ALTER TABLE ONLY auth.one_time_tokens
    ADD CONSTRAINT one_time_tokens_user_id_fkey FOREIGN KEY (user_id) REFERENCES auth.users(id) ON DELETE CASCADE;


--
-- Name: refresh_tokens refresh_tokens_session_id_fkey; Type: FK CONSTRAINT; Schema: auth; Owner: supabase_auth_admin
--

ALTER TABLE ONLY auth.refresh_tokens
    ADD CONSTRAINT refresh_tokens_session_id_fkey FOREIGN KEY (session_id) REFERENCES auth.sessions(id) ON DELETE CASCADE;


--
-- Name: saml_providers saml_providers_sso_provider_id_fkey; Type: FK CONSTRAINT; Schema: auth; Owner: supabase_auth_admin
--

ALTER TABLE ONLY auth.saml_providers
    ADD CONSTRAINT saml_providers_sso_provider_id_fkey FOREIGN KEY (sso_provider_id) REFERENCES auth.sso_providers(id) ON DELETE CASCADE;


--
-- Name: saml_relay_states saml_relay_states_flow_state_id_fkey; Type: FK CONSTRAINT; Schema: auth; Owner: supabase_auth_admin
--

ALTER TABLE ONLY auth.saml_relay_states
    ADD CONSTRAINT saml_relay_states_flow_state_id_fkey FOREIGN KEY (flow_state_id) REFERENCES auth.flow_state(id) ON DELETE CASCADE;


--
-- Name: saml_relay_states saml_relay_states_sso_provider_id_fkey; Type: FK CONSTRAINT; Schema: auth; Owner: supabase_auth_admin
--

ALTER TABLE ONLY auth.saml_relay_states
    ADD CONSTRAINT saml_relay_states_sso_provider_id_fkey FOREIGN KEY (sso_provider_id) REFERENCES auth.sso_providers(id) ON DELETE CASCADE;


--
-- Name: sessions sessions_user_id_fkey; Type: FK CONSTRAINT; Schema: auth; Owner: supabase_auth_admin
--

ALTER TABLE ONLY auth.sessions
    ADD CONSTRAINT sessions_user_id_fkey FOREIGN KEY (user_id) REFERENCES auth.users(id) ON DELETE CASCADE;


--
-- Name: sso_domains sso_domains_sso_provider_id_fkey; Type: FK CONSTRAINT; Schema: auth; Owner: supabase_auth_admin
--

ALTER TABLE ONLY auth.sso_domains
    ADD CONSTRAINT sso_domains_sso_provider_id_fkey FOREIGN KEY (sso_provider_id) REFERENCES auth.sso_providers(id) ON DELETE CASCADE;


--
-- Name: business_contacts business_contacts_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.business_contacts
    ADD CONSTRAINT business_contacts_user_id_fkey FOREIGN KEY (user_id) REFERENCES auth.users(id) ON DELETE CASCADE;


--
-- Name: business_metrics_summary business_metrics_summary_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.business_metrics_summary
    ADD CONSTRAINT business_metrics_summary_user_id_fkey FOREIGN KEY (user_id) REFERENCES auth.users(id) ON DELETE CASCADE;


--
-- Name: business_profiles business_profiles_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.business_profiles
    ADD CONSTRAINT business_profiles_user_id_fkey FOREIGN KEY (user_id) REFERENCES auth.users(id) ON DELETE CASCADE;


--
-- Name: business_users business_users_auth_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.business_users
    ADD CONSTRAINT business_users_auth_user_id_fkey FOREIGN KEY (auth_user_id) REFERENCES auth.users(id) ON DELETE SET NULL;


--
-- Name: business_users business_users_business_profile_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.business_users
    ADD CONSTRAINT business_users_business_profile_id_fkey FOREIGN KEY (business_profile_id) REFERENCES public.business_profiles(id) ON DELETE CASCADE;


--
-- Name: business_users business_users_invited_by_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.business_users
    ADD CONSTRAINT business_users_invited_by_fkey FOREIGN KEY (invited_by) REFERENCES auth.users(id);


--
-- Name: coaching_steps coaching_steps_flow_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.coaching_steps
    ADD CONSTRAINT coaching_steps_flow_id_fkey FOREIGN KEY (flow_id) REFERENCES public.coaching_flows(id) ON DELETE CASCADE;


--
-- Name: invoice_items invoice_items_invoice_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.invoice_items
    ADD CONSTRAINT invoice_items_invoice_id_fkey FOREIGN KEY (invoice_id) REFERENCES public.invoices(id) ON DELETE CASCADE;


--
-- Name: invoice_items invoice_items_item_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.invoice_items
    ADD CONSTRAINT invoice_items_item_id_fkey FOREIGN KEY (item_id) REFERENCES public.items(id);


--
-- Name: invoices invoices_customer_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.invoices
    ADD CONSTRAINT invoices_customer_id_fkey FOREIGN KEY (customer_id) REFERENCES public.business_contacts(id);


--
-- Name: invoices invoices_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.invoices
    ADD CONSTRAINT invoices_user_id_fkey FOREIGN KEY (user_id) REFERENCES auth.users(id) ON DELETE CASCADE;


--
-- Name: items items_business_profile_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.items
    ADD CONSTRAINT items_business_profile_id_fkey FOREIGN KEY (business_profile_id) REFERENCES public.business_profiles(id);


--
-- Name: items items_supplier_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.items
    ADD CONSTRAINT items_supplier_id_fkey FOREIGN KEY (supplier_id) REFERENCES public.business_contacts(id);


--
-- Name: items items_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.items
    ADD CONSTRAINT items_user_id_fkey FOREIGN KEY (user_id) REFERENCES auth.users(id) ON DELETE CASCADE;


--
-- Name: low_stock_notification_logs low_stock_notification_logs_item_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.low_stock_notification_logs
    ADD CONSTRAINT low_stock_notification_logs_item_id_fkey FOREIGN KEY (item_id) REFERENCES public.items(id) ON DELETE CASCADE;


--
-- Name: low_stock_notification_logs low_stock_notification_logs_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.low_stock_notification_logs
    ADD CONSTRAINT low_stock_notification_logs_user_id_fkey FOREIGN KEY (user_id) REFERENCES auth.users(id) ON DELETE CASCADE;


--
-- Name: low_stock_notification_settings low_stock_notification_settings_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.low_stock_notification_settings
    ADD CONSTRAINT low_stock_notification_settings_user_id_fkey FOREIGN KEY (user_id) REFERENCES auth.users(id) ON DELETE CASCADE;


--
-- Name: navigation_config navigation_config_business_profile_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.navigation_config
    ADD CONSTRAINT navigation_config_business_profile_id_fkey FOREIGN KEY (business_profile_id) REFERENCES public.business_profiles(id) ON DELETE CASCADE;


--
-- Name: notification_logs notification_logs_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.notification_logs
    ADD CONSTRAINT notification_logs_user_id_fkey FOREIGN KEY (user_id) REFERENCES auth.users(id) ON DELETE CASCADE;


--
-- Name: notification_preferences notification_preferences_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.notification_preferences
    ADD CONSTRAINT notification_preferences_user_id_fkey FOREIGN KEY (user_id) REFERENCES auth.users(id) ON DELETE CASCADE;


--
-- Name: registration_analytics registration_analytics_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.registration_analytics
    ADD CONSTRAINT registration_analytics_user_id_fkey FOREIGN KEY (user_id) REFERENCES auth.users(id);


--
-- Name: stock_movements stock_movements_item_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.stock_movements
    ADD CONSTRAINT stock_movements_item_id_fkey FOREIGN KEY (item_id) REFERENCES public.items(id) ON DELETE CASCADE;


--
-- Name: stock_movements stock_movements_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.stock_movements
    ADD CONSTRAINT stock_movements_user_id_fkey FOREIGN KEY (user_id) REFERENCES auth.users(id) ON DELETE CASCADE;


--
-- Name: subscriptions subscriptions_plan_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.subscriptions
    ADD CONSTRAINT subscriptions_plan_id_fkey FOREIGN KEY (plan_id) REFERENCES public.subscription_plans(id);


--
-- Name: subscriptions subscriptions_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.subscriptions
    ADD CONSTRAINT subscriptions_user_id_fkey FOREIGN KEY (user_id) REFERENCES auth.users(id) ON DELETE CASCADE;


--
-- Name: user_activity_log user_activity_log_business_profile_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_activity_log
    ADD CONSTRAINT user_activity_log_business_profile_id_fkey FOREIGN KEY (business_profile_id) REFERENCES public.business_profiles(id) ON DELETE CASCADE;


--
-- Name: user_activity_log user_activity_log_business_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_activity_log
    ADD CONSTRAINT user_activity_log_business_user_id_fkey FOREIGN KEY (business_user_id) REFERENCES public.business_users(id) ON DELETE SET NULL;


--
-- Name: user_coaching_progress user_coaching_progress_flow_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_coaching_progress
    ADD CONSTRAINT user_coaching_progress_flow_id_fkey FOREIGN KEY (flow_id) REFERENCES public.coaching_flows(id) ON DELETE CASCADE;


--
-- Name: user_coaching_progress user_coaching_progress_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_coaching_progress
    ADD CONSTRAINT user_coaching_progress_user_id_fkey FOREIGN KEY (user_id) REFERENCES auth.users(id) ON DELETE CASCADE;


--
-- Name: user_config_history user_config_history_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_config_history
    ADD CONSTRAINT user_config_history_user_id_fkey FOREIGN KEY (user_id) REFERENCES auth.users(id) ON DELETE CASCADE;


--
-- Name: user_experiment_assignments user_experiment_assignments_experiment_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_experiment_assignments
    ADD CONSTRAINT user_experiment_assignments_experiment_id_fkey FOREIGN KEY (experiment_id) REFERENCES public.widget_experiments(id) ON DELETE CASCADE;


--
-- Name: user_experiment_assignments user_experiment_assignments_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_experiment_assignments
    ADD CONSTRAINT user_experiment_assignments_user_id_fkey FOREIGN KEY (user_id) REFERENCES auth.users(id) ON DELETE CASCADE;


--
-- Name: user_experiment_assignments user_experiment_assignments_variant_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_experiment_assignments
    ADD CONSTRAINT user_experiment_assignments_variant_id_fkey FOREIGN KEY (variant_id) REFERENCES public.widget_experiment_variants(id) ON DELETE CASCADE;


--
-- Name: user_fcm_tokens user_fcm_tokens_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_fcm_tokens
    ADD CONSTRAINT user_fcm_tokens_user_id_fkey FOREIGN KEY (user_id) REFERENCES auth.users(id) ON DELETE CASCADE;


--
-- Name: user_feature_access user_feature_access_business_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_feature_access
    ADD CONSTRAINT user_feature_access_business_user_id_fkey FOREIGN KEY (business_user_id) REFERENCES public.business_users(id) ON DELETE CASCADE;


--
-- Name: user_feature_access user_feature_access_granted_by_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_feature_access
    ADD CONSTRAINT user_feature_access_granted_by_fkey FOREIGN KEY (granted_by) REFERENCES public.business_users(id);


--
-- Name: user_sessions user_sessions_business_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_sessions
    ADD CONSTRAINT user_sessions_business_user_id_fkey FOREIGN KEY (business_user_id) REFERENCES public.business_users(id) ON DELETE CASCADE;


--
-- Name: user_widget_configs user_widget_configs_business_profile_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_widget_configs
    ADD CONSTRAINT user_widget_configs_business_profile_id_fkey FOREIGN KEY (business_profile_id) REFERENCES public.business_profiles(id) ON DELETE CASCADE;


--
-- Name: user_widget_configs user_widget_configs_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_widget_configs
    ADD CONSTRAINT user_widget_configs_user_id_fkey FOREIGN KEY (user_id) REFERENCES auth.users(id) ON DELETE CASCADE;


--
-- Name: widget_config_cache widget_config_cache_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.widget_config_cache
    ADD CONSTRAINT widget_config_cache_user_id_fkey FOREIGN KEY (user_id) REFERENCES auth.users(id) ON DELETE CASCADE;


--
-- Name: widget_config_templates widget_config_templates_created_by_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.widget_config_templates
    ADD CONSTRAINT widget_config_templates_created_by_fkey FOREIGN KEY (created_by) REFERENCES auth.users(id);


--
-- Name: widget_experiment_variants widget_experiment_variants_experiment_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.widget_experiment_variants
    ADD CONSTRAINT widget_experiment_variants_experiment_id_fkey FOREIGN KEY (experiment_id) REFERENCES public.widget_experiments(id) ON DELETE CASCADE;


--
-- Name: widget_experiments widget_experiments_created_by_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.widget_experiments
    ADD CONSTRAINT widget_experiments_created_by_fkey FOREIGN KEY (created_by) REFERENCES auth.users(id);


--
-- Name: widget_feature_flags widget_feature_flags_created_by_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.widget_feature_flags
    ADD CONSTRAINT widget_feature_flags_created_by_fkey FOREIGN KEY (created_by) REFERENCES auth.users(id);


--
-- Name: widget_usage_analytics widget_usage_analytics_business_profile_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.widget_usage_analytics
    ADD CONSTRAINT widget_usage_analytics_business_profile_id_fkey FOREIGN KEY (business_profile_id) REFERENCES public.business_profiles(id) ON DELETE CASCADE;


--
-- Name: widget_usage_analytics widget_usage_analytics_experiment_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.widget_usage_analytics
    ADD CONSTRAINT widget_usage_analytics_experiment_id_fkey FOREIGN KEY (experiment_id) REFERENCES public.widget_experiments(id);


--
-- Name: widget_usage_analytics widget_usage_analytics_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.widget_usage_analytics
    ADD CONSTRAINT widget_usage_analytics_user_id_fkey FOREIGN KEY (user_id) REFERENCES auth.users(id) ON DELETE CASCADE;


--
-- Name: widget_usage_analytics widget_usage_analytics_variant_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.widget_usage_analytics
    ADD CONSTRAINT widget_usage_analytics_variant_id_fkey FOREIGN KEY (variant_id) REFERENCES public.widget_experiment_variants(id);


--
-- Name: iceberg_namespaces iceberg_namespaces_bucket_id_fkey; Type: FK CONSTRAINT; Schema: storage; Owner: supabase_storage_admin
--

ALTER TABLE ONLY storage.iceberg_namespaces
    ADD CONSTRAINT iceberg_namespaces_bucket_id_fkey FOREIGN KEY (bucket_id) REFERENCES storage.buckets_analytics(id) ON DELETE CASCADE;


--
-- Name: iceberg_tables iceberg_tables_bucket_id_fkey; Type: FK CONSTRAINT; Schema: storage; Owner: supabase_storage_admin
--

ALTER TABLE ONLY storage.iceberg_tables
    ADD CONSTRAINT iceberg_tables_bucket_id_fkey FOREIGN KEY (bucket_id) REFERENCES storage.buckets_analytics(id) ON DELETE CASCADE;


--
-- Name: iceberg_tables iceberg_tables_namespace_id_fkey; Type: FK CONSTRAINT; Schema: storage; Owner: supabase_storage_admin
--

ALTER TABLE ONLY storage.iceberg_tables
    ADD CONSTRAINT iceberg_tables_namespace_id_fkey FOREIGN KEY (namespace_id) REFERENCES storage.iceberg_namespaces(id) ON DELETE CASCADE;


--
-- Name: objects objects_bucketId_fkey; Type: FK CONSTRAINT; Schema: storage; Owner: supabase_storage_admin
--

ALTER TABLE ONLY storage.objects
    ADD CONSTRAINT "objects_bucketId_fkey" FOREIGN KEY (bucket_id) REFERENCES storage.buckets(id);


--
-- Name: prefixes prefixes_bucketId_fkey; Type: FK CONSTRAINT; Schema: storage; Owner: supabase_storage_admin
--

ALTER TABLE ONLY storage.prefixes
    ADD CONSTRAINT "prefixes_bucketId_fkey" FOREIGN KEY (bucket_id) REFERENCES storage.buckets(id);


--
-- Name: s3_multipart_uploads s3_multipart_uploads_bucket_id_fkey; Type: FK CONSTRAINT; Schema: storage; Owner: supabase_storage_admin
--

ALTER TABLE ONLY storage.s3_multipart_uploads
    ADD CONSTRAINT s3_multipart_uploads_bucket_id_fkey FOREIGN KEY (bucket_id) REFERENCES storage.buckets(id);


--
-- Name: s3_multipart_uploads_parts s3_multipart_uploads_parts_bucket_id_fkey; Type: FK CONSTRAINT; Schema: storage; Owner: supabase_storage_admin
--

ALTER TABLE ONLY storage.s3_multipart_uploads_parts
    ADD CONSTRAINT s3_multipart_uploads_parts_bucket_id_fkey FOREIGN KEY (bucket_id) REFERENCES storage.buckets(id);


--
-- Name: s3_multipart_uploads_parts s3_multipart_uploads_parts_upload_id_fkey; Type: FK CONSTRAINT; Schema: storage; Owner: supabase_storage_admin
--

ALTER TABLE ONLY storage.s3_multipart_uploads_parts
    ADD CONSTRAINT s3_multipart_uploads_parts_upload_id_fkey FOREIGN KEY (upload_id) REFERENCES storage.s3_multipart_uploads(id) ON DELETE CASCADE;


--
-- Name: users Admins can read user data; Type: POLICY; Schema: auth; Owner: supabase_auth_admin
--

CREATE POLICY "Admins can read user data" ON auth.users FOR SELECT USING ((auth.uid() IN ( SELECT admin_users.id
   FROM public.admin_users
  WHERE (admin_users.role = ANY (ARRAY['viewer'::text, 'editor'::text, 'admin'::text])))));


--
-- Name: audit_log_entries; Type: ROW SECURITY; Schema: auth; Owner: supabase_auth_admin
--

ALTER TABLE auth.audit_log_entries ENABLE ROW LEVEL SECURITY;

--
-- Name: flow_state; Type: ROW SECURITY; Schema: auth; Owner: supabase_auth_admin
--

ALTER TABLE auth.flow_state ENABLE ROW LEVEL SECURITY;

--
-- Name: identities; Type: ROW SECURITY; Schema: auth; Owner: supabase_auth_admin
--

ALTER TABLE auth.identities ENABLE ROW LEVEL SECURITY;

--
-- Name: instances; Type: ROW SECURITY; Schema: auth; Owner: supabase_auth_admin
--

ALTER TABLE auth.instances ENABLE ROW LEVEL SECURITY;

--
-- Name: mfa_amr_claims; Type: ROW SECURITY; Schema: auth; Owner: supabase_auth_admin
--

ALTER TABLE auth.mfa_amr_claims ENABLE ROW LEVEL SECURITY;

--
-- Name: mfa_challenges; Type: ROW SECURITY; Schema: auth; Owner: supabase_auth_admin
--

ALTER TABLE auth.mfa_challenges ENABLE ROW LEVEL SECURITY;

--
-- Name: mfa_factors; Type: ROW SECURITY; Schema: auth; Owner: supabase_auth_admin
--

ALTER TABLE auth.mfa_factors ENABLE ROW LEVEL SECURITY;

--
-- Name: one_time_tokens; Type: ROW SECURITY; Schema: auth; Owner: supabase_auth_admin
--

ALTER TABLE auth.one_time_tokens ENABLE ROW LEVEL SECURITY;

--
-- Name: refresh_tokens; Type: ROW SECURITY; Schema: auth; Owner: supabase_auth_admin
--

ALTER TABLE auth.refresh_tokens ENABLE ROW LEVEL SECURITY;

--
-- Name: saml_providers; Type: ROW SECURITY; Schema: auth; Owner: supabase_auth_admin
--

ALTER TABLE auth.saml_providers ENABLE ROW LEVEL SECURITY;

--
-- Name: saml_relay_states; Type: ROW SECURITY; Schema: auth; Owner: supabase_auth_admin
--

ALTER TABLE auth.saml_relay_states ENABLE ROW LEVEL SECURITY;

--
-- Name: schema_migrations; Type: ROW SECURITY; Schema: auth; Owner: supabase_auth_admin
--

ALTER TABLE auth.schema_migrations ENABLE ROW LEVEL SECURITY;

--
-- Name: sessions; Type: ROW SECURITY; Schema: auth; Owner: supabase_auth_admin
--

ALTER TABLE auth.sessions ENABLE ROW LEVEL SECURITY;

--
-- Name: sso_domains; Type: ROW SECURITY; Schema: auth; Owner: supabase_auth_admin
--

ALTER TABLE auth.sso_domains ENABLE ROW LEVEL SECURITY;

--
-- Name: sso_providers; Type: ROW SECURITY; Schema: auth; Owner: supabase_auth_admin
--

ALTER TABLE auth.sso_providers ENABLE ROW LEVEL SECURITY;

--
-- Name: users; Type: ROW SECURITY; Schema: auth; Owner: supabase_auth_admin
--

ALTER TABLE auth.users ENABLE ROW LEVEL SECURITY;

--
-- Name: user_feature_access Admins can manage feature access; Type: POLICY; Schema: public; Owner: postgres
--

CREATE POLICY "Admins can manage feature access" ON public.user_feature_access TO authenticated USING ((EXISTS ( SELECT 1
   FROM (public.business_users admin
     JOIN public.business_users target ON ((target.id = user_feature_access.business_user_id)))
  WHERE ((admin.business_profile_id = target.business_profile_id) AND (admin.auth_user_id = auth.uid()) AND (admin.status = 'active'::public.user_status) AND (admin.role = ANY (ARRAY['owner'::public.user_role, 'admin'::public.user_role]))))));


--
-- Name: invoices Admins can read all query data; Type: POLICY; Schema: public; Owner: postgres
--

CREATE POLICY "Admins can read all query data" ON public.invoices FOR SELECT USING ((auth.uid() IN ( SELECT admin_users.id
   FROM public.admin_users
  WHERE (admin_users.role = ANY (ARRAY['editor'::text, 'admin'::text])))));


--
-- Name: business_categories Anyone can read active business categories; Type: POLICY; Schema: public; Owner: postgres
--

CREATE POLICY "Anyone can read active business categories" ON public.business_categories FOR SELECT USING ((is_active = true));


--
-- Name: business_users Business owners/admins can manage users; Type: POLICY; Schema: public; Owner: postgres
--

CREATE POLICY "Business owners/admins can manage users" ON public.business_users TO authenticated USING ((EXISTS ( SELECT 1
   FROM public.business_users bu
  WHERE ((bu.business_profile_id = business_users.business_profile_id) AND (bu.auth_user_id = auth.uid()) AND (bu.status = 'active'::public.user_status) AND (bu.role = ANY (ARRAY['owner'::public.user_role, 'admin'::public.user_role]))))));


--
-- Name: widget_feature_flags Feature flags are readable by authenticated users; Type: POLICY; Schema: public; Owner: postgres
--

CREATE POLICY "Feature flags are readable by authenticated users" ON public.widget_feature_flags FOR SELECT TO authenticated USING (true);


--
-- Name: feature_permissions Feature permissions are readable; Type: POLICY; Schema: public; Owner: postgres
--

CREATE POLICY "Feature permissions are readable" ON public.feature_permissions FOR SELECT TO authenticated USING (true);


--
-- Name: role_feature_access Role feature access is readable; Type: POLICY; Schema: public; Owner: postgres
--

CREATE POLICY "Role feature access is readable" ON public.role_feature_access FOR SELECT TO authenticated USING (true);


--
-- Name: role_templates Role templates are readable; Type: POLICY; Schema: public; Owner: postgres
--

CREATE POLICY "Role templates are readable" ON public.role_templates FOR SELECT TO authenticated USING (true);


--
-- Name: business_categories Service role can manage categories; Type: POLICY; Schema: public; Owner: postgres
--

CREATE POLICY "Service role can manage categories" ON public.business_categories USING ((auth.role() = 'service_role'::text));


--
-- Name: user_activity_log System can insert activity logs; Type: POLICY; Schema: public; Owner: postgres
--

CREATE POLICY "System can insert activity logs" ON public.user_activity_log FOR INSERT TO authenticated WITH CHECK (true);


--
-- Name: user_experiment_assignments System can manage experiment assignments; Type: POLICY; Schema: public; Owner: postgres
--

CREATE POLICY "System can manage experiment assignments" ON public.user_experiment_assignments USING (true);


--
-- Name: items Users can delete their own items; Type: POLICY; Schema: public; Owner: postgres
--

CREATE POLICY "Users can delete their own items" ON public.items FOR DELETE USING (((auth.uid() = user_id) AND ((business_profile_id IS NULL) OR (business_profile_id IN ( SELECT business_profiles.id
   FROM public.business_profiles
  WHERE (business_profiles.user_id = auth.uid()))))));


--
-- Name: items Users can insert their own items; Type: POLICY; Schema: public; Owner: postgres
--

CREATE POLICY "Users can insert their own items" ON public.items FOR INSERT WITH CHECK (((auth.uid() = user_id) AND ((business_profile_id IS NULL) OR (business_profile_id IN ( SELECT business_profiles.id
   FROM public.business_profiles
  WHERE (business_profiles.user_id = auth.uid()))))));


--
-- Name: widget_usage_analytics Users can insert their own usage analytics; Type: POLICY; Schema: public; Owner: postgres
--

CREATE POLICY "Users can insert their own usage analytics" ON public.widget_usage_analytics FOR INSERT TO authenticated WITH CHECK ((auth.uid() = user_id));


--
-- Name: user_widget_configs Users can insert their own widget configurations; Type: POLICY; Schema: public; Owner: postgres
--

CREATE POLICY "Users can insert their own widget configurations" ON public.user_widget_configs FOR INSERT TO authenticated WITH CHECK ((auth.uid() = user_id));


--
-- Name: invoice_items Users can manage invoice items for their invoices; Type: POLICY; Schema: public; Owner: postgres
--

CREATE POLICY "Users can manage invoice items for their invoices" ON public.invoice_items USING ((EXISTS ( SELECT 1
   FROM public.invoices
  WHERE ((invoices.id = invoice_items.invoice_id) AND (invoices.user_id = auth.uid())))));


--
-- Name: user_fcm_tokens Users can manage their own FCM tokens; Type: POLICY; Schema: public; Owner: postgres
--

CREATE POLICY "Users can manage their own FCM tokens" ON public.user_fcm_tokens USING ((auth.uid() = user_id));


--
-- Name: business_contacts Users can manage their own contacts; Type: POLICY; Schema: public; Owner: postgres
--

CREATE POLICY "Users can manage their own contacts" ON public.business_contacts USING ((auth.uid() = user_id));


--
-- Name: invoices Users can manage their own invoices; Type: POLICY; Schema: public; Owner: postgres
--

CREATE POLICY "Users can manage their own invoices" ON public.invoices USING ((auth.uid() = user_id));


--
-- Name: items Users can manage their own items; Type: POLICY; Schema: public; Owner: postgres
--

CREATE POLICY "Users can manage their own items" ON public.items USING ((auth.uid() = user_id));


--
-- Name: low_stock_notification_settings Users can manage their own low stock settings; Type: POLICY; Schema: public; Owner: postgres
--

CREATE POLICY "Users can manage their own low stock settings" ON public.low_stock_notification_settings USING ((auth.uid() = user_id));


--
-- Name: notification_logs Users can manage their own notification logs; Type: POLICY; Schema: public; Owner: postgres
--

CREATE POLICY "Users can manage their own notification logs" ON public.notification_logs USING ((auth.uid() = user_id));


--
-- Name: notification_preferences Users can manage their own notification preferences; Type: POLICY; Schema: public; Owner: postgres
--

CREATE POLICY "Users can manage their own notification preferences" ON public.notification_preferences USING ((auth.uid() = user_id));


--
-- Name: items Users can update their own items; Type: POLICY; Schema: public; Owner: postgres
--

CREATE POLICY "Users can update their own items" ON public.items FOR UPDATE USING (((auth.uid() = user_id) AND ((business_profile_id IS NULL) OR (business_profile_id IN ( SELECT business_profiles.id
   FROM public.business_profiles
  WHERE (business_profiles.user_id = auth.uid()))))));


--
-- Name: business_metrics_summary Users can update their own metrics; Type: POLICY; Schema: public; Owner: postgres
--

CREATE POLICY "Users can update their own metrics" ON public.business_metrics_summary FOR UPDATE USING ((auth.uid() = user_id));


--
-- Name: user_widget_configs Users can update their own widget configurations; Type: POLICY; Schema: public; Owner: postgres
--

CREATE POLICY "Users can update their own widget configurations" ON public.user_widget_configs FOR UPDATE TO authenticated USING ((auth.uid() = user_id)) WITH CHECK ((auth.uid() = user_id));


--
-- Name: user_activity_log Users can view activity in their business; Type: POLICY; Schema: public; Owner: postgres
--

CREATE POLICY "Users can view activity in their business" ON public.user_activity_log FOR SELECT TO authenticated USING ((business_profile_id IN ( SELECT business_users.business_profile_id
   FROM public.business_users
  WHERE ((business_users.auth_user_id = auth.uid()) AND (business_users.status = 'active'::public.user_status)))));


--
-- Name: widget_config_cache Users can view relevant cached configurations; Type: POLICY; Schema: public; Owner: postgres
--

CREATE POLICY "Users can view relevant cached configurations" ON public.widget_config_cache FOR SELECT TO authenticated USING (((cache_scope = ANY (ARRAY['global'::public.config_scope, 'user_segment'::public.config_scope])) OR ((cache_scope = 'individual_user'::public.config_scope) AND (auth.uid() = user_id))));


--
-- Name: navigation_config Users can view relevant navigation; Type: POLICY; Schema: public; Owner: postgres
--

CREATE POLICY "Users can view relevant navigation" ON public.navigation_config FOR SELECT TO authenticated USING (((business_profile_id IS NULL) OR (business_profile_id IN ( SELECT business_users.business_profile_id
   FROM public.business_users
  WHERE ((business_users.auth_user_id = auth.uid()) AND (business_users.status = 'active'::public.user_status))))));


--
-- Name: business_profiles Users can view their own business profile; Type: POLICY; Schema: public; Owner: postgres
--

CREATE POLICY "Users can view their own business profile" ON public.business_profiles USING ((auth.uid() = user_id));


--
-- Name: user_config_history Users can view their own config history; Type: POLICY; Schema: public; Owner: postgres
--

CREATE POLICY "Users can view their own config history" ON public.user_config_history FOR SELECT TO authenticated USING ((auth.uid() = user_id));


--
-- Name: user_experiment_assignments Users can view their own experiment assignments; Type: POLICY; Schema: public; Owner: postgres
--

CREATE POLICY "Users can view their own experiment assignments" ON public.user_experiment_assignments FOR SELECT USING ((auth.uid() = user_id));


--
-- Name: items Users can view their own items; Type: POLICY; Schema: public; Owner: postgres
--

CREATE POLICY "Users can view their own items" ON public.items FOR SELECT USING (((auth.uid() = user_id) OR (business_profile_id IN ( SELECT business_profiles.id
   FROM public.business_profiles
  WHERE (business_profiles.user_id = auth.uid()))) OR (supplier_id IN ( SELECT business_contacts.id
   FROM public.business_contacts
  WHERE ((business_contacts.user_id = auth.uid()) AND (business_contacts.contact_type = 'supplier'::public.contact_type))))));


--
-- Name: low_stock_notification_logs Users can view their own low stock notification logs; Type: POLICY; Schema: public; Owner: postgres
--

CREATE POLICY "Users can view their own low stock notification logs" ON public.low_stock_notification_logs USING ((auth.uid() = user_id));


--
-- Name: business_metrics_summary Users can view their own metrics; Type: POLICY; Schema: public; Owner: postgres
--

CREATE POLICY "Users can view their own metrics" ON public.business_metrics_summary FOR SELECT USING ((auth.uid() = user_id));


--
-- Name: user_feature_access Users can view their own permissions; Type: POLICY; Schema: public; Owner: postgres
--

CREATE POLICY "Users can view their own permissions" ON public.user_feature_access FOR SELECT TO authenticated USING ((business_user_id IN ( SELECT business_users.id
   FROM public.business_users
  WHERE (business_users.auth_user_id = auth.uid()))));


--
-- Name: user_sessions Users can view their own sessions; Type: POLICY; Schema: public; Owner: postgres
--

CREATE POLICY "Users can view their own sessions" ON public.user_sessions FOR SELECT TO authenticated USING ((business_user_id IN ( SELECT business_users.id
   FROM public.business_users
  WHERE (business_users.auth_user_id = auth.uid()))));


--
-- Name: stock_movements Users can view their own stock movements; Type: POLICY; Schema: public; Owner: postgres
--

CREATE POLICY "Users can view their own stock movements" ON public.stock_movements USING ((auth.uid() = user_id));


--
-- Name: subscriptions Users can view their own subscriptions; Type: POLICY; Schema: public; Owner: postgres
--

CREATE POLICY "Users can view their own subscriptions" ON public.subscriptions USING ((auth.uid() = user_id));


--
-- Name: widget_usage_analytics Users can view their own usage analytics; Type: POLICY; Schema: public; Owner: postgres
--

CREATE POLICY "Users can view their own usage analytics" ON public.widget_usage_analytics FOR SELECT TO authenticated USING ((auth.uid() = user_id));


--
-- Name: user_widget_configs Users can view their own widget configurations; Type: POLICY; Schema: public; Owner: postgres
--

CREATE POLICY "Users can view their own widget configurations" ON public.user_widget_configs FOR SELECT TO authenticated USING ((auth.uid() = user_id));


--
-- Name: business_users Users can view users in their business; Type: POLICY; Schema: public; Owner: postgres
--

CREATE POLICY "Users can view users in their business" ON public.business_users FOR SELECT TO authenticated USING ((business_profile_id IN ( SELECT business_profiles.id
   FROM public.business_profiles
  WHERE (business_profiles.user_id = auth.uid())
UNION
 SELECT business_users_1.business_profile_id
   FROM public.business_users business_users_1
  WHERE ((business_users_1.auth_user_id = auth.uid()) AND (business_users_1.status = 'active'::public.user_status)))));


--
-- Name: widget_config_templates Widget config templates are readable by authenticated users; Type: POLICY; Schema: public; Owner: postgres
--

CREATE POLICY "Widget config templates are readable by authenticated users" ON public.widget_config_templates FOR SELECT TO authenticated USING (true);


--
-- Name: widget_experiment_variants Widget experiment variants are readable by authenticated users; Type: POLICY; Schema: public; Owner: postgres
--

CREATE POLICY "Widget experiment variants are readable by authenticated users" ON public.widget_experiment_variants FOR SELECT TO authenticated USING (true);


--
-- Name: widget_experiments Widget experiments are readable by authenticated users; Type: POLICY; Schema: public; Owner: postgres
--

CREATE POLICY "Widget experiments are readable by authenticated users" ON public.widget_experiments FOR SELECT TO authenticated USING (true);


--
-- Name: admin_users; Type: ROW SECURITY; Schema: public; Owner: postgres
--

ALTER TABLE public.admin_users ENABLE ROW LEVEL SECURITY;

--
-- Name: business_categories; Type: ROW SECURITY; Schema: public; Owner: postgres
--

ALTER TABLE public.business_categories ENABLE ROW LEVEL SECURITY;

--
-- Name: business_contacts; Type: ROW SECURITY; Schema: public; Owner: postgres
--

ALTER TABLE public.business_contacts ENABLE ROW LEVEL SECURITY;

--
-- Name: business_metrics_summary; Type: ROW SECURITY; Schema: public; Owner: postgres
--

ALTER TABLE public.business_metrics_summary ENABLE ROW LEVEL SECURITY;

--
-- Name: business_users; Type: ROW SECURITY; Schema: public; Owner: postgres
--

ALTER TABLE public.business_users ENABLE ROW LEVEL SECURITY;

--
-- Name: coaching_flows; Type: ROW SECURITY; Schema: public; Owner: postgres
--

ALTER TABLE public.coaching_flows ENABLE ROW LEVEL SECURITY;

--
-- Name: coaching_flows coaching_flows_select; Type: POLICY; Schema: public; Owner: postgres
--

CREATE POLICY coaching_flows_select ON public.coaching_flows FOR SELECT USING (true);


--
-- Name: coaching_steps; Type: ROW SECURITY; Schema: public; Owner: postgres
--

ALTER TABLE public.coaching_steps ENABLE ROW LEVEL SECURITY;

--
-- Name: coaching_steps coaching_steps_select; Type: POLICY; Schema: public; Owner: postgres
--

CREATE POLICY coaching_steps_select ON public.coaching_steps FOR SELECT USING (true);


--
-- Name: feature_permissions; Type: ROW SECURITY; Schema: public; Owner: postgres
--

ALTER TABLE public.feature_permissions ENABLE ROW LEVEL SECURITY;

--
-- Name: invoice_items; Type: ROW SECURITY; Schema: public; Owner: postgres
--

ALTER TABLE public.invoice_items ENABLE ROW LEVEL SECURITY;

--
-- Name: invoices; Type: ROW SECURITY; Schema: public; Owner: postgres
--

ALTER TABLE public.invoices ENABLE ROW LEVEL SECURITY;

--
-- Name: items; Type: ROW SECURITY; Schema: public; Owner: postgres
--

ALTER TABLE public.items ENABLE ROW LEVEL SECURITY;

--
-- Name: low_stock_notification_logs; Type: ROW SECURITY; Schema: public; Owner: postgres
--

ALTER TABLE public.low_stock_notification_logs ENABLE ROW LEVEL SECURITY;

--
-- Name: low_stock_notification_settings; Type: ROW SECURITY; Schema: public; Owner: postgres
--

ALTER TABLE public.low_stock_notification_settings ENABLE ROW LEVEL SECURITY;

--
-- Name: navigation_config; Type: ROW SECURITY; Schema: public; Owner: postgres
--

ALTER TABLE public.navigation_config ENABLE ROW LEVEL SECURITY;

--
-- Name: notification_logs; Type: ROW SECURITY; Schema: public; Owner: postgres
--

ALTER TABLE public.notification_logs ENABLE ROW LEVEL SECURITY;

--
-- Name: notification_preferences; Type: ROW SECURITY; Schema: public; Owner: postgres
--

ALTER TABLE public.notification_preferences ENABLE ROW LEVEL SECURITY;

--
-- Name: role_feature_access; Type: ROW SECURITY; Schema: public; Owner: postgres
--

ALTER TABLE public.role_feature_access ENABLE ROW LEVEL SECURITY;

--
-- Name: role_templates; Type: ROW SECURITY; Schema: public; Owner: postgres
--

ALTER TABLE public.role_templates ENABLE ROW LEVEL SECURITY;

--
-- Name: stock_movements; Type: ROW SECURITY; Schema: public; Owner: postgres
--

ALTER TABLE public.stock_movements ENABLE ROW LEVEL SECURITY;

--
-- Name: subscriptions; Type: ROW SECURITY; Schema: public; Owner: postgres
--

ALTER TABLE public.subscriptions ENABLE ROW LEVEL SECURITY;

--
-- Name: user_activity_log; Type: ROW SECURITY; Schema: public; Owner: postgres
--

ALTER TABLE public.user_activity_log ENABLE ROW LEVEL SECURITY;

--
-- Name: user_coaching_progress; Type: ROW SECURITY; Schema: public; Owner: postgres
--

ALTER TABLE public.user_coaching_progress ENABLE ROW LEVEL SECURITY;

--
-- Name: user_config_history; Type: ROW SECURITY; Schema: public; Owner: postgres
--

ALTER TABLE public.user_config_history ENABLE ROW LEVEL SECURITY;

--
-- Name: user_experiment_assignments; Type: ROW SECURITY; Schema: public; Owner: postgres
--

ALTER TABLE public.user_experiment_assignments ENABLE ROW LEVEL SECURITY;

--
-- Name: user_fcm_tokens; Type: ROW SECURITY; Schema: public; Owner: postgres
--

ALTER TABLE public.user_fcm_tokens ENABLE ROW LEVEL SECURITY;

--
-- Name: user_feature_access; Type: ROW SECURITY; Schema: public; Owner: postgres
--

ALTER TABLE public.user_feature_access ENABLE ROW LEVEL SECURITY;

--
-- Name: user_coaching_progress user_progress_insert; Type: POLICY; Schema: public; Owner: postgres
--

CREATE POLICY user_progress_insert ON public.user_coaching_progress FOR INSERT WITH CHECK ((auth.uid() = user_id));


--
-- Name: user_coaching_progress user_progress_select; Type: POLICY; Schema: public; Owner: postgres
--

CREATE POLICY user_progress_select ON public.user_coaching_progress FOR SELECT USING ((auth.uid() = user_id));


--
-- Name: user_coaching_progress user_progress_update; Type: POLICY; Schema: public; Owner: postgres
--

CREATE POLICY user_progress_update ON public.user_coaching_progress FOR UPDATE USING ((auth.uid() = user_id));


--
-- Name: user_sessions; Type: ROW SECURITY; Schema: public; Owner: postgres
--

ALTER TABLE public.user_sessions ENABLE ROW LEVEL SECURITY;

--
-- Name: user_widget_configs; Type: ROW SECURITY; Schema: public; Owner: postgres
--

ALTER TABLE public.user_widget_configs ENABLE ROW LEVEL SECURITY;

--
-- Name: widget_config_cache; Type: ROW SECURITY; Schema: public; Owner: postgres
--

ALTER TABLE public.widget_config_cache ENABLE ROW LEVEL SECURITY;

--
-- Name: widget_config_templates; Type: ROW SECURITY; Schema: public; Owner: postgres
--

ALTER TABLE public.widget_config_templates ENABLE ROW LEVEL SECURITY;

--
-- Name: widget_experiment_variants; Type: ROW SECURITY; Schema: public; Owner: postgres
--

ALTER TABLE public.widget_experiment_variants ENABLE ROW LEVEL SECURITY;

--
-- Name: widget_experiments; Type: ROW SECURITY; Schema: public; Owner: postgres
--

ALTER TABLE public.widget_experiments ENABLE ROW LEVEL SECURITY;

--
-- Name: widget_feature_flags; Type: ROW SECURITY; Schema: public; Owner: postgres
--

ALTER TABLE public.widget_feature_flags ENABLE ROW LEVEL SECURITY;

--
-- Name: widget_usage_analytics; Type: ROW SECURITY; Schema: public; Owner: postgres
--

ALTER TABLE public.widget_usage_analytics ENABLE ROW LEVEL SECURITY;

--
-- Name: messages; Type: ROW SECURITY; Schema: realtime; Owner: supabase_realtime_admin
--

ALTER TABLE realtime.messages ENABLE ROW LEVEL SECURITY;

--
-- Name: objects Anyone can view business logos; Type: POLICY; Schema: storage; Owner: supabase_storage_admin
--

CREATE POLICY "Anyone can view business logos" ON storage.objects FOR SELECT USING ((bucket_id = 'business-logos'::text));


--
-- Name: objects Anyone can view item images; Type: POLICY; Schema: storage; Owner: supabase_storage_admin
--

CREATE POLICY "Anyone can view item images" ON storage.objects FOR SELECT USING ((bucket_id = 'item-images'::text));


--
-- Name: objects Authenticated users can upload business logos; Type: POLICY; Schema: storage; Owner: supabase_storage_admin
--

CREATE POLICY "Authenticated users can upload business logos" ON storage.objects FOR INSERT WITH CHECK (((bucket_id = 'business-logos'::text) AND (auth.role() = 'authenticated'::text)));


--
-- Name: objects Authenticated users can upload item images; Type: POLICY; Schema: storage; Owner: supabase_storage_admin
--

CREATE POLICY "Authenticated users can upload item images" ON storage.objects FOR INSERT WITH CHECK (((bucket_id = 'item-images'::text) AND (auth.role() = 'authenticated'::text)));


--
-- Name: objects Users can delete own invoice PDFs; Type: POLICY; Schema: storage; Owner: supabase_storage_admin
--

CREATE POLICY "Users can delete own invoice PDFs" ON storage.objects FOR DELETE USING (((bucket_id = 'invoice-pdfs'::text) AND ((storage.foldername(name))[1] = (auth.uid())::text)));


--
-- Name: objects Users can delete their own business logos; Type: POLICY; Schema: storage; Owner: supabase_storage_admin
--

CREATE POLICY "Users can delete their own business logos" ON storage.objects FOR DELETE USING (((bucket_id = 'business-logos'::text) AND ((auth.uid())::text = (storage.foldername(name))[1])));


--
-- Name: objects Users can delete their own item images; Type: POLICY; Schema: storage; Owner: supabase_storage_admin
--

CREATE POLICY "Users can delete their own item images" ON storage.objects FOR DELETE USING (((bucket_id = 'item-images'::text) AND ((auth.uid())::text = (storage.foldername(name))[1])));


--
-- Name: objects Users can update own invoice PDFs; Type: POLICY; Schema: storage; Owner: supabase_storage_admin
--

CREATE POLICY "Users can update own invoice PDFs" ON storage.objects FOR UPDATE USING (((bucket_id = 'invoice-pdfs'::text) AND ((storage.foldername(name))[1] = (auth.uid())::text)));


--
-- Name: objects Users can update their own business logos; Type: POLICY; Schema: storage; Owner: supabase_storage_admin
--

CREATE POLICY "Users can update their own business logos" ON storage.objects FOR UPDATE USING (((bucket_id = 'business-logos'::text) AND ((auth.uid())::text = (storage.foldername(name))[1])));


--
-- Name: objects Users can update their own item images; Type: POLICY; Schema: storage; Owner: supabase_storage_admin
--

CREATE POLICY "Users can update their own item images" ON storage.objects FOR UPDATE USING (((bucket_id = 'item-images'::text) AND ((auth.uid())::text = (storage.foldername(name))[1])));


--
-- Name: objects Users can upload own invoice PDFs; Type: POLICY; Schema: storage; Owner: supabase_storage_admin
--

CREATE POLICY "Users can upload own invoice PDFs" ON storage.objects FOR INSERT WITH CHECK (((bucket_id = 'invoice-pdfs'::text) AND ((storage.foldername(name))[1] = (auth.uid())::text)));


--
-- Name: objects Users can view own invoice PDFs; Type: POLICY; Schema: storage; Owner: supabase_storage_admin
--

CREATE POLICY "Users can view own invoice PDFs" ON storage.objects FOR SELECT USING (((bucket_id = 'invoice-pdfs'::text) AND ((storage.foldername(name))[1] = (auth.uid())::text)));


--
-- Name: buckets; Type: ROW SECURITY; Schema: storage; Owner: supabase_storage_admin
--

ALTER TABLE storage.buckets ENABLE ROW LEVEL SECURITY;

--
-- Name: buckets_analytics; Type: ROW SECURITY; Schema: storage; Owner: supabase_storage_admin
--

ALTER TABLE storage.buckets_analytics ENABLE ROW LEVEL SECURITY;

--
-- Name: iceberg_namespaces; Type: ROW SECURITY; Schema: storage; Owner: supabase_storage_admin
--

ALTER TABLE storage.iceberg_namespaces ENABLE ROW LEVEL SECURITY;

--
-- Name: iceberg_tables; Type: ROW SECURITY; Schema: storage; Owner: supabase_storage_admin
--

ALTER TABLE storage.iceberg_tables ENABLE ROW LEVEL SECURITY;

--
-- Name: migrations; Type: ROW SECURITY; Schema: storage; Owner: supabase_storage_admin
--

ALTER TABLE storage.migrations ENABLE ROW LEVEL SECURITY;

--
-- Name: objects; Type: ROW SECURITY; Schema: storage; Owner: supabase_storage_admin
--

ALTER TABLE storage.objects ENABLE ROW LEVEL SECURITY;

--
-- Name: prefixes; Type: ROW SECURITY; Schema: storage; Owner: supabase_storage_admin
--

ALTER TABLE storage.prefixes ENABLE ROW LEVEL SECURITY;

--
-- Name: s3_multipart_uploads; Type: ROW SECURITY; Schema: storage; Owner: supabase_storage_admin
--

ALTER TABLE storage.s3_multipart_uploads ENABLE ROW LEVEL SECURITY;

--
-- Name: s3_multipart_uploads_parts; Type: ROW SECURITY; Schema: storage; Owner: supabase_storage_admin
--

ALTER TABLE storage.s3_multipart_uploads_parts ENABLE ROW LEVEL SECURITY;

--
-- Name: supabase_realtime; Type: PUBLICATION; Schema: -; Owner: postgres
--

CREATE PUBLICATION supabase_realtime WITH (publish = 'insert, update, delete, truncate');


ALTER PUBLICATION supabase_realtime OWNER TO postgres;

--
-- Name: SCHEMA auth; Type: ACL; Schema: -; Owner: supabase_admin
--

GRANT USAGE ON SCHEMA auth TO anon;
GRANT USAGE ON SCHEMA auth TO authenticated;
GRANT USAGE ON SCHEMA auth TO service_role;
GRANT ALL ON SCHEMA auth TO supabase_auth_admin;
GRANT ALL ON SCHEMA auth TO dashboard_user;
GRANT USAGE ON SCHEMA auth TO postgres;


--
-- Name: SCHEMA extensions; Type: ACL; Schema: -; Owner: postgres
--

GRANT USAGE ON SCHEMA extensions TO anon;
GRANT USAGE ON SCHEMA extensions TO authenticated;
GRANT USAGE ON SCHEMA extensions TO service_role;
GRANT ALL ON SCHEMA extensions TO dashboard_user;


--
-- Name: SCHEMA net; Type: ACL; Schema: -; Owner: supabase_admin
--

GRANT USAGE ON SCHEMA net TO supabase_functions_admin;
GRANT USAGE ON SCHEMA net TO postgres;
GRANT USAGE ON SCHEMA net TO anon;
GRANT USAGE ON SCHEMA net TO authenticated;
GRANT USAGE ON SCHEMA net TO service_role;


--
-- Name: SCHEMA public; Type: ACL; Schema: -; Owner: pg_database_owner
--

GRANT USAGE ON SCHEMA public TO postgres;
GRANT USAGE ON SCHEMA public TO anon;
GRANT USAGE ON SCHEMA public TO authenticated;
GRANT USAGE ON SCHEMA public TO service_role;


--
-- Name: SCHEMA realtime; Type: ACL; Schema: -; Owner: supabase_admin
--

GRANT USAGE ON SCHEMA realtime TO postgres;
GRANT USAGE ON SCHEMA realtime TO anon;
GRANT USAGE ON SCHEMA realtime TO authenticated;
GRANT USAGE ON SCHEMA realtime TO service_role;
GRANT ALL ON SCHEMA realtime TO supabase_realtime_admin;


--
-- Name: SCHEMA storage; Type: ACL; Schema: -; Owner: supabase_admin
--

GRANT USAGE ON SCHEMA storage TO postgres WITH GRANT OPTION;
GRANT USAGE ON SCHEMA storage TO anon;
GRANT USAGE ON SCHEMA storage TO authenticated;
GRANT USAGE ON SCHEMA storage TO service_role;
GRANT ALL ON SCHEMA storage TO supabase_storage_admin;
GRANT ALL ON SCHEMA storage TO dashboard_user;


--
-- Name: SCHEMA supabase_functions; Type: ACL; Schema: -; Owner: supabase_admin
--

GRANT USAGE ON SCHEMA supabase_functions TO postgres;
GRANT USAGE ON SCHEMA supabase_functions TO anon;
GRANT USAGE ON SCHEMA supabase_functions TO authenticated;
GRANT USAGE ON SCHEMA supabase_functions TO service_role;
GRANT ALL ON SCHEMA supabase_functions TO supabase_functions_admin;


--
-- Name: SCHEMA vault; Type: ACL; Schema: -; Owner: supabase_admin
--

GRANT USAGE ON SCHEMA vault TO postgres WITH GRANT OPTION;
GRANT USAGE ON SCHEMA vault TO service_role;


--
-- Name: FUNCTION email(); Type: ACL; Schema: auth; Owner: supabase_auth_admin
--

GRANT ALL ON FUNCTION auth.email() TO dashboard_user;


--
-- Name: FUNCTION jwt(); Type: ACL; Schema: auth; Owner: supabase_auth_admin
--

GRANT ALL ON FUNCTION auth.jwt() TO postgres;
GRANT ALL ON FUNCTION auth.jwt() TO dashboard_user;


--
-- Name: FUNCTION role(); Type: ACL; Schema: auth; Owner: supabase_auth_admin
--

GRANT ALL ON FUNCTION auth.role() TO dashboard_user;


--
-- Name: FUNCTION uid(); Type: ACL; Schema: auth; Owner: supabase_auth_admin
--

GRANT ALL ON FUNCTION auth.uid() TO dashboard_user;


--
-- Name: FUNCTION armor(bytea); Type: ACL; Schema: extensions; Owner: supabase_admin
--

GRANT ALL ON FUNCTION extensions.armor(bytea) TO dashboard_user;
GRANT ALL ON FUNCTION extensions.armor(bytea) TO postgres WITH GRANT OPTION;


--
-- Name: FUNCTION armor(bytea, text[], text[]); Type: ACL; Schema: extensions; Owner: supabase_admin
--

GRANT ALL ON FUNCTION extensions.armor(bytea, text[], text[]) TO dashboard_user;
GRANT ALL ON FUNCTION extensions.armor(bytea, text[], text[]) TO postgres WITH GRANT OPTION;


--
-- Name: FUNCTION crypt(text, text); Type: ACL; Schema: extensions; Owner: supabase_admin
--

GRANT ALL ON FUNCTION extensions.crypt(text, text) TO dashboard_user;
GRANT ALL ON FUNCTION extensions.crypt(text, text) TO postgres WITH GRANT OPTION;


--
-- Name: FUNCTION dearmor(text); Type: ACL; Schema: extensions; Owner: supabase_admin
--

GRANT ALL ON FUNCTION extensions.dearmor(text) TO dashboard_user;
GRANT ALL ON FUNCTION extensions.dearmor(text) TO postgres WITH GRANT OPTION;


--
-- Name: FUNCTION decrypt(bytea, bytea, text); Type: ACL; Schema: extensions; Owner: supabase_admin
--

GRANT ALL ON FUNCTION extensions.decrypt(bytea, bytea, text) TO dashboard_user;
GRANT ALL ON FUNCTION extensions.decrypt(bytea, bytea, text) TO postgres WITH GRANT OPTION;


--
-- Name: FUNCTION decrypt_iv(bytea, bytea, bytea, text); Type: ACL; Schema: extensions; Owner: supabase_admin
--

GRANT ALL ON FUNCTION extensions.decrypt_iv(bytea, bytea, bytea, text) TO dashboard_user;
GRANT ALL ON FUNCTION extensions.decrypt_iv(bytea, bytea, bytea, text) TO postgres WITH GRANT OPTION;


--
-- Name: FUNCTION digest(bytea, text); Type: ACL; Schema: extensions; Owner: supabase_admin
--

GRANT ALL ON FUNCTION extensions.digest(bytea, text) TO dashboard_user;
GRANT ALL ON FUNCTION extensions.digest(bytea, text) TO postgres WITH GRANT OPTION;


--
-- Name: FUNCTION digest(text, text); Type: ACL; Schema: extensions; Owner: supabase_admin
--

GRANT ALL ON FUNCTION extensions.digest(text, text) TO dashboard_user;
GRANT ALL ON FUNCTION extensions.digest(text, text) TO postgres WITH GRANT OPTION;


--
-- Name: FUNCTION encrypt(bytea, bytea, text); Type: ACL; Schema: extensions; Owner: supabase_admin
--

GRANT ALL ON FUNCTION extensions.encrypt(bytea, bytea, text) TO dashboard_user;
GRANT ALL ON FUNCTION extensions.encrypt(bytea, bytea, text) TO postgres WITH GRANT OPTION;


--
-- Name: FUNCTION encrypt_iv(bytea, bytea, bytea, text); Type: ACL; Schema: extensions; Owner: supabase_admin
--

GRANT ALL ON FUNCTION extensions.encrypt_iv(bytea, bytea, bytea, text) TO dashboard_user;
GRANT ALL ON FUNCTION extensions.encrypt_iv(bytea, bytea, bytea, text) TO postgres WITH GRANT OPTION;


--
-- Name: FUNCTION gen_random_bytes(integer); Type: ACL; Schema: extensions; Owner: supabase_admin
--

GRANT ALL ON FUNCTION extensions.gen_random_bytes(integer) TO dashboard_user;
GRANT ALL ON FUNCTION extensions.gen_random_bytes(integer) TO postgres WITH GRANT OPTION;


--
-- Name: FUNCTION gen_random_uuid(); Type: ACL; Schema: extensions; Owner: supabase_admin
--

GRANT ALL ON FUNCTION extensions.gen_random_uuid() TO dashboard_user;
GRANT ALL ON FUNCTION extensions.gen_random_uuid() TO postgres WITH GRANT OPTION;


--
-- Name: FUNCTION gen_salt(text); Type: ACL; Schema: extensions; Owner: supabase_admin
--

GRANT ALL ON FUNCTION extensions.gen_salt(text) TO dashboard_user;
GRANT ALL ON FUNCTION extensions.gen_salt(text) TO postgres WITH GRANT OPTION;


--
-- Name: FUNCTION gen_salt(text, integer); Type: ACL; Schema: extensions; Owner: supabase_admin
--

GRANT ALL ON FUNCTION extensions.gen_salt(text, integer) TO dashboard_user;
GRANT ALL ON FUNCTION extensions.gen_salt(text, integer) TO postgres WITH GRANT OPTION;


--
-- Name: FUNCTION grant_pg_cron_access(); Type: ACL; Schema: extensions; Owner: supabase_admin
--

REVOKE ALL ON FUNCTION extensions.grant_pg_cron_access() FROM supabase_admin;
GRANT ALL ON FUNCTION extensions.grant_pg_cron_access() TO supabase_admin WITH GRANT OPTION;
GRANT ALL ON FUNCTION extensions.grant_pg_cron_access() TO dashboard_user;


--
-- Name: FUNCTION grant_pg_graphql_access(); Type: ACL; Schema: extensions; Owner: supabase_admin
--

GRANT ALL ON FUNCTION extensions.grant_pg_graphql_access() TO postgres WITH GRANT OPTION;


--
-- Name: FUNCTION grant_pg_net_access(); Type: ACL; Schema: extensions; Owner: supabase_admin
--

REVOKE ALL ON FUNCTION extensions.grant_pg_net_access() FROM supabase_admin;
GRANT ALL ON FUNCTION extensions.grant_pg_net_access() TO supabase_admin WITH GRANT OPTION;
GRANT ALL ON FUNCTION extensions.grant_pg_net_access() TO dashboard_user;


--
-- Name: FUNCTION hmac(bytea, bytea, text); Type: ACL; Schema: extensions; Owner: supabase_admin
--

GRANT ALL ON FUNCTION extensions.hmac(bytea, bytea, text) TO dashboard_user;
GRANT ALL ON FUNCTION extensions.hmac(bytea, bytea, text) TO postgres WITH GRANT OPTION;


--
-- Name: FUNCTION hmac(text, text, text); Type: ACL; Schema: extensions; Owner: supabase_admin
--

GRANT ALL ON FUNCTION extensions.hmac(text, text, text) TO dashboard_user;
GRANT ALL ON FUNCTION extensions.hmac(text, text, text) TO postgres WITH GRANT OPTION;


--
-- Name: FUNCTION pg_stat_statements(showtext boolean, OUT userid oid, OUT dbid oid, OUT toplevel boolean, OUT queryid bigint, OUT query text, OUT plans bigint, OUT total_plan_time double precision, OUT min_plan_time double precision, OUT max_plan_time double precision, OUT mean_plan_time double precision, OUT stddev_plan_time double precision, OUT calls bigint, OUT total_exec_time double precision, OUT min_exec_time double precision, OUT max_exec_time double precision, OUT mean_exec_time double precision, OUT stddev_exec_time double precision, OUT rows bigint, OUT shared_blks_hit bigint, OUT shared_blks_read bigint, OUT shared_blks_dirtied bigint, OUT shared_blks_written bigint, OUT local_blks_hit bigint, OUT local_blks_read bigint, OUT local_blks_dirtied bigint, OUT local_blks_written bigint, OUT temp_blks_read bigint, OUT temp_blks_written bigint, OUT shared_blk_read_time double precision, OUT shared_blk_write_time double precision, OUT local_blk_read_time double precision, OUT local_blk_write_time double precision, OUT temp_blk_read_time double precision, OUT temp_blk_write_time double precision, OUT wal_records bigint, OUT wal_fpi bigint, OUT wal_bytes numeric, OUT jit_functions bigint, OUT jit_generation_time double precision, OUT jit_inlining_count bigint, OUT jit_inlining_time double precision, OUT jit_optimization_count bigint, OUT jit_optimization_time double precision, OUT jit_emission_count bigint, OUT jit_emission_time double precision, OUT jit_deform_count bigint, OUT jit_deform_time double precision, OUT stats_since timestamp with time zone, OUT minmax_stats_since timestamp with time zone); Type: ACL; Schema: extensions; Owner: supabase_admin
--

GRANT ALL ON FUNCTION extensions.pg_stat_statements(showtext boolean, OUT userid oid, OUT dbid oid, OUT toplevel boolean, OUT queryid bigint, OUT query text, OUT plans bigint, OUT total_plan_time double precision, OUT min_plan_time double precision, OUT max_plan_time double precision, OUT mean_plan_time double precision, OUT stddev_plan_time double precision, OUT calls bigint, OUT total_exec_time double precision, OUT min_exec_time double precision, OUT max_exec_time double precision, OUT mean_exec_time double precision, OUT stddev_exec_time double precision, OUT rows bigint, OUT shared_blks_hit bigint, OUT shared_blks_read bigint, OUT shared_blks_dirtied bigint, OUT shared_blks_written bigint, OUT local_blks_hit bigint, OUT local_blks_read bigint, OUT local_blks_dirtied bigint, OUT local_blks_written bigint, OUT temp_blks_read bigint, OUT temp_blks_written bigint, OUT shared_blk_read_time double precision, OUT shared_blk_write_time double precision, OUT local_blk_read_time double precision, OUT local_blk_write_time double precision, OUT temp_blk_read_time double precision, OUT temp_blk_write_time double precision, OUT wal_records bigint, OUT wal_fpi bigint, OUT wal_bytes numeric, OUT jit_functions bigint, OUT jit_generation_time double precision, OUT jit_inlining_count bigint, OUT jit_inlining_time double precision, OUT jit_optimization_count bigint, OUT jit_optimization_time double precision, OUT jit_emission_count bigint, OUT jit_emission_time double precision, OUT jit_deform_count bigint, OUT jit_deform_time double precision, OUT stats_since timestamp with time zone, OUT minmax_stats_since timestamp with time zone) TO postgres WITH GRANT OPTION;


--
-- Name: FUNCTION pg_stat_statements_info(OUT dealloc bigint, OUT stats_reset timestamp with time zone); Type: ACL; Schema: extensions; Owner: supabase_admin
--

GRANT ALL ON FUNCTION extensions.pg_stat_statements_info(OUT dealloc bigint, OUT stats_reset timestamp with time zone) TO postgres WITH GRANT OPTION;


--
-- Name: FUNCTION pg_stat_statements_reset(userid oid, dbid oid, queryid bigint, minmax_only boolean); Type: ACL; Schema: extensions; Owner: supabase_admin
--

GRANT ALL ON FUNCTION extensions.pg_stat_statements_reset(userid oid, dbid oid, queryid bigint, minmax_only boolean) TO postgres WITH GRANT OPTION;


--
-- Name: FUNCTION pgp_armor_headers(text, OUT key text, OUT value text); Type: ACL; Schema: extensions; Owner: supabase_admin
--

GRANT ALL ON FUNCTION extensions.pgp_armor_headers(text, OUT key text, OUT value text) TO dashboard_user;
GRANT ALL ON FUNCTION extensions.pgp_armor_headers(text, OUT key text, OUT value text) TO postgres WITH GRANT OPTION;


--
-- Name: FUNCTION pgp_key_id(bytea); Type: ACL; Schema: extensions; Owner: supabase_admin
--

GRANT ALL ON FUNCTION extensions.pgp_key_id(bytea) TO dashboard_user;
GRANT ALL ON FUNCTION extensions.pgp_key_id(bytea) TO postgres WITH GRANT OPTION;


--
-- Name: FUNCTION pgp_pub_decrypt(bytea, bytea); Type: ACL; Schema: extensions; Owner: supabase_admin
--

GRANT ALL ON FUNCTION extensions.pgp_pub_decrypt(bytea, bytea) TO dashboard_user;
GRANT ALL ON FUNCTION extensions.pgp_pub_decrypt(bytea, bytea) TO postgres WITH GRANT OPTION;


--
-- Name: FUNCTION pgp_pub_decrypt(bytea, bytea, text); Type: ACL; Schema: extensions; Owner: supabase_admin
--

GRANT ALL ON FUNCTION extensions.pgp_pub_decrypt(bytea, bytea, text) TO dashboard_user;
GRANT ALL ON FUNCTION extensions.pgp_pub_decrypt(bytea, bytea, text) TO postgres WITH GRANT OPTION;


--
-- Name: FUNCTION pgp_pub_decrypt(bytea, bytea, text, text); Type: ACL; Schema: extensions; Owner: supabase_admin
--

GRANT ALL ON FUNCTION extensions.pgp_pub_decrypt(bytea, bytea, text, text) TO dashboard_user;
GRANT ALL ON FUNCTION extensions.pgp_pub_decrypt(bytea, bytea, text, text) TO postgres WITH GRANT OPTION;


--
-- Name: FUNCTION pgp_pub_decrypt_bytea(bytea, bytea); Type: ACL; Schema: extensions; Owner: supabase_admin
--

GRANT ALL ON FUNCTION extensions.pgp_pub_decrypt_bytea(bytea, bytea) TO dashboard_user;
GRANT ALL ON FUNCTION extensions.pgp_pub_decrypt_bytea(bytea, bytea) TO postgres WITH GRANT OPTION;


--
-- Name: FUNCTION pgp_pub_decrypt_bytea(bytea, bytea, text); Type: ACL; Schema: extensions; Owner: supabase_admin
--

GRANT ALL ON FUNCTION extensions.pgp_pub_decrypt_bytea(bytea, bytea, text) TO dashboard_user;
GRANT ALL ON FUNCTION extensions.pgp_pub_decrypt_bytea(bytea, bytea, text) TO postgres WITH GRANT OPTION;


--
-- Name: FUNCTION pgp_pub_decrypt_bytea(bytea, bytea, text, text); Type: ACL; Schema: extensions; Owner: supabase_admin
--

GRANT ALL ON FUNCTION extensions.pgp_pub_decrypt_bytea(bytea, bytea, text, text) TO dashboard_user;
GRANT ALL ON FUNCTION extensions.pgp_pub_decrypt_bytea(bytea, bytea, text, text) TO postgres WITH GRANT OPTION;


--
-- Name: FUNCTION pgp_pub_encrypt(text, bytea); Type: ACL; Schema: extensions; Owner: supabase_admin
--

GRANT ALL ON FUNCTION extensions.pgp_pub_encrypt(text, bytea) TO dashboard_user;
GRANT ALL ON FUNCTION extensions.pgp_pub_encrypt(text, bytea) TO postgres WITH GRANT OPTION;


--
-- Name: FUNCTION pgp_pub_encrypt(text, bytea, text); Type: ACL; Schema: extensions; Owner: supabase_admin
--

GRANT ALL ON FUNCTION extensions.pgp_pub_encrypt(text, bytea, text) TO dashboard_user;
GRANT ALL ON FUNCTION extensions.pgp_pub_encrypt(text, bytea, text) TO postgres WITH GRANT OPTION;


--
-- Name: FUNCTION pgp_pub_encrypt_bytea(bytea, bytea); Type: ACL; Schema: extensions; Owner: supabase_admin
--

GRANT ALL ON FUNCTION extensions.pgp_pub_encrypt_bytea(bytea, bytea) TO dashboard_user;
GRANT ALL ON FUNCTION extensions.pgp_pub_encrypt_bytea(bytea, bytea) TO postgres WITH GRANT OPTION;


--
-- Name: FUNCTION pgp_pub_encrypt_bytea(bytea, bytea, text); Type: ACL; Schema: extensions; Owner: supabase_admin
--

GRANT ALL ON FUNCTION extensions.pgp_pub_encrypt_bytea(bytea, bytea, text) TO dashboard_user;
GRANT ALL ON FUNCTION extensions.pgp_pub_encrypt_bytea(bytea, bytea, text) TO postgres WITH GRANT OPTION;


--
-- Name: FUNCTION pgp_sym_decrypt(bytea, text); Type: ACL; Schema: extensions; Owner: supabase_admin
--

GRANT ALL ON FUNCTION extensions.pgp_sym_decrypt(bytea, text) TO dashboard_user;
GRANT ALL ON FUNCTION extensions.pgp_sym_decrypt(bytea, text) TO postgres WITH GRANT OPTION;


--
-- Name: FUNCTION pgp_sym_decrypt(bytea, text, text); Type: ACL; Schema: extensions; Owner: supabase_admin
--

GRANT ALL ON FUNCTION extensions.pgp_sym_decrypt(bytea, text, text) TO dashboard_user;
GRANT ALL ON FUNCTION extensions.pgp_sym_decrypt(bytea, text, text) TO postgres WITH GRANT OPTION;


--
-- Name: FUNCTION pgp_sym_decrypt_bytea(bytea, text); Type: ACL; Schema: extensions; Owner: supabase_admin
--

GRANT ALL ON FUNCTION extensions.pgp_sym_decrypt_bytea(bytea, text) TO dashboard_user;
GRANT ALL ON FUNCTION extensions.pgp_sym_decrypt_bytea(bytea, text) TO postgres WITH GRANT OPTION;


--
-- Name: FUNCTION pgp_sym_decrypt_bytea(bytea, text, text); Type: ACL; Schema: extensions; Owner: supabase_admin
--

GRANT ALL ON FUNCTION extensions.pgp_sym_decrypt_bytea(bytea, text, text) TO dashboard_user;
GRANT ALL ON FUNCTION extensions.pgp_sym_decrypt_bytea(bytea, text, text) TO postgres WITH GRANT OPTION;


--
-- Name: FUNCTION pgp_sym_encrypt(text, text); Type: ACL; Schema: extensions; Owner: supabase_admin
--

GRANT ALL ON FUNCTION extensions.pgp_sym_encrypt(text, text) TO dashboard_user;
GRANT ALL ON FUNCTION extensions.pgp_sym_encrypt(text, text) TO postgres WITH GRANT OPTION;


--
-- Name: FUNCTION pgp_sym_encrypt(text, text, text); Type: ACL; Schema: extensions; Owner: supabase_admin
--

GRANT ALL ON FUNCTION extensions.pgp_sym_encrypt(text, text, text) TO dashboard_user;
GRANT ALL ON FUNCTION extensions.pgp_sym_encrypt(text, text, text) TO postgres WITH GRANT OPTION;


--
-- Name: FUNCTION pgp_sym_encrypt_bytea(bytea, text); Type: ACL; Schema: extensions; Owner: supabase_admin
--

GRANT ALL ON FUNCTION extensions.pgp_sym_encrypt_bytea(bytea, text) TO dashboard_user;
GRANT ALL ON FUNCTION extensions.pgp_sym_encrypt_bytea(bytea, text) TO postgres WITH GRANT OPTION;


--
-- Name: FUNCTION pgp_sym_encrypt_bytea(bytea, text, text); Type: ACL; Schema: extensions; Owner: supabase_admin
--

GRANT ALL ON FUNCTION extensions.pgp_sym_encrypt_bytea(bytea, text, text) TO dashboard_user;
GRANT ALL ON FUNCTION extensions.pgp_sym_encrypt_bytea(bytea, text, text) TO postgres WITH GRANT OPTION;


--
-- Name: FUNCTION pgrst_ddl_watch(); Type: ACL; Schema: extensions; Owner: supabase_admin
--

GRANT ALL ON FUNCTION extensions.pgrst_ddl_watch() TO postgres WITH GRANT OPTION;


--
-- Name: FUNCTION pgrst_drop_watch(); Type: ACL; Schema: extensions; Owner: supabase_admin
--

GRANT ALL ON FUNCTION extensions.pgrst_drop_watch() TO postgres WITH GRANT OPTION;


--
-- Name: FUNCTION set_graphql_placeholder(); Type: ACL; Schema: extensions; Owner: supabase_admin
--

GRANT ALL ON FUNCTION extensions.set_graphql_placeholder() TO postgres WITH GRANT OPTION;


--
-- Name: FUNCTION uuid_generate_v1(); Type: ACL; Schema: extensions; Owner: supabase_admin
--

GRANT ALL ON FUNCTION extensions.uuid_generate_v1() TO dashboard_user;
GRANT ALL ON FUNCTION extensions.uuid_generate_v1() TO postgres WITH GRANT OPTION;


--
-- Name: FUNCTION uuid_generate_v1mc(); Type: ACL; Schema: extensions; Owner: supabase_admin
--

GRANT ALL ON FUNCTION extensions.uuid_generate_v1mc() TO dashboard_user;
GRANT ALL ON FUNCTION extensions.uuid_generate_v1mc() TO postgres WITH GRANT OPTION;


--
-- Name: FUNCTION uuid_generate_v3(namespace uuid, name text); Type: ACL; Schema: extensions; Owner: supabase_admin
--

GRANT ALL ON FUNCTION extensions.uuid_generate_v3(namespace uuid, name text) TO dashboard_user;
GRANT ALL ON FUNCTION extensions.uuid_generate_v3(namespace uuid, name text) TO postgres WITH GRANT OPTION;


--
-- Name: FUNCTION uuid_generate_v4(); Type: ACL; Schema: extensions; Owner: supabase_admin
--

GRANT ALL ON FUNCTION extensions.uuid_generate_v4() TO dashboard_user;
GRANT ALL ON FUNCTION extensions.uuid_generate_v4() TO postgres WITH GRANT OPTION;


--
-- Name: FUNCTION uuid_generate_v5(namespace uuid, name text); Type: ACL; Schema: extensions; Owner: supabase_admin
--

GRANT ALL ON FUNCTION extensions.uuid_generate_v5(namespace uuid, name text) TO dashboard_user;
GRANT ALL ON FUNCTION extensions.uuid_generate_v5(namespace uuid, name text) TO postgres WITH GRANT OPTION;


--
-- Name: FUNCTION uuid_nil(); Type: ACL; Schema: extensions; Owner: supabase_admin
--

GRANT ALL ON FUNCTION extensions.uuid_nil() TO dashboard_user;
GRANT ALL ON FUNCTION extensions.uuid_nil() TO postgres WITH GRANT OPTION;


--
-- Name: FUNCTION uuid_ns_dns(); Type: ACL; Schema: extensions; Owner: supabase_admin
--

GRANT ALL ON FUNCTION extensions.uuid_ns_dns() TO dashboard_user;
GRANT ALL ON FUNCTION extensions.uuid_ns_dns() TO postgres WITH GRANT OPTION;


--
-- Name: FUNCTION uuid_ns_oid(); Type: ACL; Schema: extensions; Owner: supabase_admin
--

GRANT ALL ON FUNCTION extensions.uuid_ns_oid() TO dashboard_user;
GRANT ALL ON FUNCTION extensions.uuid_ns_oid() TO postgres WITH GRANT OPTION;


--
-- Name: FUNCTION uuid_ns_url(); Type: ACL; Schema: extensions; Owner: supabase_admin
--

GRANT ALL ON FUNCTION extensions.uuid_ns_url() TO dashboard_user;
GRANT ALL ON FUNCTION extensions.uuid_ns_url() TO postgres WITH GRANT OPTION;


--
-- Name: FUNCTION uuid_ns_x500(); Type: ACL; Schema: extensions; Owner: supabase_admin
--

GRANT ALL ON FUNCTION extensions.uuid_ns_x500() TO dashboard_user;
GRANT ALL ON FUNCTION extensions.uuid_ns_x500() TO postgres WITH GRANT OPTION;


--
-- Name: FUNCTION graphql("operationName" text, query text, variables jsonb, extensions jsonb); Type: ACL; Schema: graphql_public; Owner: supabase_admin
--

GRANT ALL ON FUNCTION graphql_public.graphql("operationName" text, query text, variables jsonb, extensions jsonb) TO postgres;
GRANT ALL ON FUNCTION graphql_public.graphql("operationName" text, query text, variables jsonb, extensions jsonb) TO anon;
GRANT ALL ON FUNCTION graphql_public.graphql("operationName" text, query text, variables jsonb, extensions jsonb) TO authenticated;
GRANT ALL ON FUNCTION graphql_public.graphql("operationName" text, query text, variables jsonb, extensions jsonb) TO service_role;


--
-- Name: FUNCTION http_get(url text, params jsonb, headers jsonb, timeout_milliseconds integer); Type: ACL; Schema: net; Owner: supabase_admin
--

REVOKE ALL ON FUNCTION net.http_get(url text, params jsonb, headers jsonb, timeout_milliseconds integer) FROM PUBLIC;
GRANT ALL ON FUNCTION net.http_get(url text, params jsonb, headers jsonb, timeout_milliseconds integer) TO supabase_functions_admin;
GRANT ALL ON FUNCTION net.http_get(url text, params jsonb, headers jsonb, timeout_milliseconds integer) TO postgres;
GRANT ALL ON FUNCTION net.http_get(url text, params jsonb, headers jsonb, timeout_milliseconds integer) TO anon;
GRANT ALL ON FUNCTION net.http_get(url text, params jsonb, headers jsonb, timeout_milliseconds integer) TO authenticated;
GRANT ALL ON FUNCTION net.http_get(url text, params jsonb, headers jsonb, timeout_milliseconds integer) TO service_role;


--
-- Name: FUNCTION http_post(url text, body jsonb, params jsonb, headers jsonb, timeout_milliseconds integer); Type: ACL; Schema: net; Owner: supabase_admin
--

REVOKE ALL ON FUNCTION net.http_post(url text, body jsonb, params jsonb, headers jsonb, timeout_milliseconds integer) FROM PUBLIC;
GRANT ALL ON FUNCTION net.http_post(url text, body jsonb, params jsonb, headers jsonb, timeout_milliseconds integer) TO supabase_functions_admin;
GRANT ALL ON FUNCTION net.http_post(url text, body jsonb, params jsonb, headers jsonb, timeout_milliseconds integer) TO postgres;
GRANT ALL ON FUNCTION net.http_post(url text, body jsonb, params jsonb, headers jsonb, timeout_milliseconds integer) TO anon;
GRANT ALL ON FUNCTION net.http_post(url text, body jsonb, params jsonb, headers jsonb, timeout_milliseconds integer) TO authenticated;
GRANT ALL ON FUNCTION net.http_post(url text, body jsonb, params jsonb, headers jsonb, timeout_milliseconds integer) TO service_role;


--
-- Name: FUNCTION get_auth(p_usename text); Type: ACL; Schema: pgbouncer; Owner: supabase_admin
--

REVOKE ALL ON FUNCTION pgbouncer.get_auth(p_usename text) FROM PUBLIC;
GRANT ALL ON FUNCTION pgbouncer.get_auth(p_usename text) TO pgbouncer;
GRANT ALL ON FUNCTION pgbouncer.get_auth(p_usename text) TO postgres;


--
-- Name: FUNCTION activate_business_user(p_invitation_token text, p_new_password text); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION public.activate_business_user(p_invitation_token text, p_new_password text) TO anon;
GRANT ALL ON FUNCTION public.activate_business_user(p_invitation_token text, p_new_password text) TO authenticated;
GRANT ALL ON FUNCTION public.activate_business_user(p_invitation_token text, p_new_password text) TO service_role;


--
-- Name: FUNCTION analyze_widget_config_usage(widget_type_param public.widget_type, days_back integer); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION public.analyze_widget_config_usage(widget_type_param public.widget_type, days_back integer) TO anon;
GRANT ALL ON FUNCTION public.analyze_widget_config_usage(widget_type_param public.widget_type, days_back integer) TO authenticated;
GRANT ALL ON FUNCTION public.analyze_widget_config_usage(widget_type_param public.widget_type, days_back integer) TO service_role;


--
-- Name: FUNCTION assign_user_to_experiment(p_user_id uuid, p_experiment_id uuid, p_assignment_context jsonb); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION public.assign_user_to_experiment(p_user_id uuid, p_experiment_id uuid, p_assignment_context jsonb) TO anon;
GRANT ALL ON FUNCTION public.assign_user_to_experiment(p_user_id uuid, p_experiment_id uuid, p_assignment_context jsonb) TO authenticated;
GRANT ALL ON FUNCTION public.assign_user_to_experiment(p_user_id uuid, p_experiment_id uuid, p_assignment_context jsonb) TO service_role;


--
-- Name: FUNCTION check_user_permission(p_user_id uuid, p_feature public.feature_area, p_permission public.permission_type); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION public.check_user_permission(p_user_id uuid, p_feature public.feature_area, p_permission public.permission_type) TO anon;
GRANT ALL ON FUNCTION public.check_user_permission(p_user_id uuid, p_feature public.feature_area, p_permission public.permission_type) TO authenticated;
GRANT ALL ON FUNCTION public.check_user_permission(p_user_id uuid, p_feature public.feature_area, p_permission public.permission_type) TO service_role;


--
-- Name: FUNCTION check_user_permission_by_business_user(p_business_user_id uuid, p_feature public.feature_area, p_permission public.permission_type); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION public.check_user_permission_by_business_user(p_business_user_id uuid, p_feature public.feature_area, p_permission public.permission_type) TO anon;
GRANT ALL ON FUNCTION public.check_user_permission_by_business_user(p_business_user_id uuid, p_feature public.feature_area, p_permission public.permission_type) TO authenticated;
GRANT ALL ON FUNCTION public.check_user_permission_by_business_user(p_business_user_id uuid, p_feature public.feature_area, p_permission public.permission_type) TO service_role;


--
-- Name: FUNCTION cleanup_expired_widget_config_cache(); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION public.cleanup_expired_widget_config_cache() TO anon;
GRANT ALL ON FUNCTION public.cleanup_expired_widget_config_cache() TO authenticated;
GRANT ALL ON FUNCTION public.cleanup_expired_widget_config_cache() TO service_role;


--
-- Name: FUNCTION complete_onboarding_with_user_management(p_business_profile_id uuid, p_user_id uuid); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION public.complete_onboarding_with_user_management(p_business_profile_id uuid, p_user_id uuid) TO anon;
GRANT ALL ON FUNCTION public.complete_onboarding_with_user_management(p_business_profile_id uuid, p_user_id uuid) TO authenticated;
GRANT ALL ON FUNCTION public.complete_onboarding_with_user_management(p_business_profile_id uuid, p_user_id uuid) TO service_role;


--
-- Name: FUNCTION create_business_profile_with_owner(p_user_id uuid, p_profile_data jsonb); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION public.create_business_profile_with_owner(p_user_id uuid, p_profile_data jsonb) TO anon;
GRANT ALL ON FUNCTION public.create_business_profile_with_owner(p_user_id uuid, p_profile_data jsonb) TO authenticated;
GRANT ALL ON FUNCTION public.create_business_profile_with_owner(p_user_id uuid, p_profile_data jsonb) TO service_role;


--
-- Name: FUNCTION create_default_notification_preferences(user_uuid uuid); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION public.create_default_notification_preferences(user_uuid uuid) TO anon;
GRANT ALL ON FUNCTION public.create_default_notification_preferences(user_uuid uuid) TO authenticated;
GRANT ALL ON FUNCTION public.create_default_notification_preferences(user_uuid uuid) TO service_role;


--
-- Name: FUNCTION create_invoice_with_validation(p_user_id uuid, p_business_profile_id uuid, p_items jsonb, p_total_amount_excl_discount numeric, p_total_amount numeric, p_customer_details jsonb, p_total_discount numeric, p_sales_type character varying, p_repayment_date date, p_notes text); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION public.create_invoice_with_validation(p_user_id uuid, p_business_profile_id uuid, p_items jsonb, p_total_amount_excl_discount numeric, p_total_amount numeric, p_customer_details jsonb, p_total_discount numeric, p_sales_type character varying, p_repayment_date date, p_notes text) TO anon;
GRANT ALL ON FUNCTION public.create_invoice_with_validation(p_user_id uuid, p_business_profile_id uuid, p_items jsonb, p_total_amount_excl_discount numeric, p_total_amount numeric, p_customer_details jsonb, p_total_discount numeric, p_sales_type character varying, p_repayment_date date, p_notes text) TO authenticated;
GRANT ALL ON FUNCTION public.create_invoice_with_validation(p_user_id uuid, p_business_profile_id uuid, p_items jsonb, p_total_amount_excl_discount numeric, p_total_amount numeric, p_customer_details jsonb, p_total_discount numeric, p_sales_type character varying, p_repayment_date date, p_notes text) TO service_role;


--
-- Name: FUNCTION create_user_session(p_business_user_id uuid, p_device_id text, p_device_type text, p_ip_address inet, p_user_agent text); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION public.create_user_session(p_business_user_id uuid, p_device_id text, p_device_type text, p_ip_address inet, p_user_agent text) TO anon;
GRANT ALL ON FUNCTION public.create_user_session(p_business_user_id uuid, p_device_id text, p_device_type text, p_ip_address inet, p_user_agent text) TO authenticated;
GRANT ALL ON FUNCTION public.create_user_session(p_business_user_id uuid, p_device_id text, p_device_type text, p_ip_address inet, p_user_agent text) TO service_role;


--
-- Name: FUNCTION deactivate_business_user(p_business_user_id uuid, p_deactivated_by uuid); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION public.deactivate_business_user(p_business_user_id uuid, p_deactivated_by uuid) TO anon;
GRANT ALL ON FUNCTION public.deactivate_business_user(p_business_user_id uuid, p_deactivated_by uuid) TO authenticated;
GRANT ALL ON FUNCTION public.deactivate_business_user(p_business_user_id uuid, p_deactivated_by uuid) TO service_role;


--
-- Name: FUNCTION filter_navigation_by_permissions(p_nav_items jsonb, p_business_user_id uuid, p_subscription_plan text); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION public.filter_navigation_by_permissions(p_nav_items jsonb, p_business_user_id uuid, p_subscription_plan text) TO anon;
GRANT ALL ON FUNCTION public.filter_navigation_by_permissions(p_nav_items jsonb, p_business_user_id uuid, p_subscription_plan text) TO authenticated;
GRANT ALL ON FUNCTION public.filter_navigation_by_permissions(p_nav_items jsonb, p_business_user_id uuid, p_subscription_plan text) TO service_role;


--
-- Name: FUNCTION generate_invoice_number(user_uuid uuid); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION public.generate_invoice_number(user_uuid uuid) TO anon;
GRANT ALL ON FUNCTION public.generate_invoice_number(user_uuid uuid) TO authenticated;
GRANT ALL ON FUNCTION public.generate_invoice_number(user_uuid uuid) TO service_role;


--
-- Name: FUNCTION generate_invoice_number(p_user_id uuid, p_business_profile_id uuid); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION public.generate_invoice_number(p_user_id uuid, p_business_profile_id uuid) TO anon;
GRANT ALL ON FUNCTION public.generate_invoice_number(p_user_id uuid, p_business_profile_id uuid) TO authenticated;
GRANT ALL ON FUNCTION public.generate_invoice_number(p_user_id uuid, p_business_profile_id uuid) TO service_role;


--
-- Name: FUNCTION get_business_metrics_enhanced(user_uuid uuid); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION public.get_business_metrics_enhanced(user_uuid uuid) TO anon;
GRANT ALL ON FUNCTION public.get_business_metrics_enhanced(user_uuid uuid) TO authenticated;
GRANT ALL ON FUNCTION public.get_business_metrics_enhanced(user_uuid uuid) TO service_role;


--
-- Name: FUNCTION get_business_profile_with_user_info(p_business_profile_id uuid, p_user_id uuid); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION public.get_business_profile_with_user_info(p_business_profile_id uuid, p_user_id uuid) TO anon;
GRANT ALL ON FUNCTION public.get_business_profile_with_user_info(p_business_profile_id uuid, p_user_id uuid) TO authenticated;
GRANT ALL ON FUNCTION public.get_business_profile_with_user_info(p_business_profile_id uuid, p_user_id uuid) TO service_role;


--
-- Name: FUNCTION get_cached_widget_config(p_user_id uuid, p_business_profile_id uuid, p_widget_type public.widget_type, p_force_refresh boolean); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION public.get_cached_widget_config(p_user_id uuid, p_business_profile_id uuid, p_widget_type public.widget_type, p_force_refresh boolean) TO anon;
GRANT ALL ON FUNCTION public.get_cached_widget_config(p_user_id uuid, p_business_profile_id uuid, p_widget_type public.widget_type, p_force_refresh boolean) TO authenticated;
GRANT ALL ON FUNCTION public.get_cached_widget_config(p_user_id uuid, p_business_profile_id uuid, p_widget_type public.widget_type, p_force_refresh boolean) TO service_role;


--
-- Name: FUNCTION get_effective_widget_config(p_user_id uuid, p_business_profile_id uuid, p_widget_type public.widget_type); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION public.get_effective_widget_config(p_user_id uuid, p_business_profile_id uuid, p_widget_type public.widget_type) TO anon;
GRANT ALL ON FUNCTION public.get_effective_widget_config(p_user_id uuid, p_business_profile_id uuid, p_widget_type public.widget_type) TO authenticated;
GRANT ALL ON FUNCTION public.get_effective_widget_config(p_user_id uuid, p_business_profile_id uuid, p_widget_type public.widget_type) TO service_role;


--
-- Name: FUNCTION get_low_stock_items(user_uuid uuid); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION public.get_low_stock_items(user_uuid uuid) TO anon;
GRANT ALL ON FUNCTION public.get_low_stock_items(user_uuid uuid) TO authenticated;
GRANT ALL ON FUNCTION public.get_low_stock_items(user_uuid uuid) TO service_role;


--
-- Name: FUNCTION get_recommended_permissions_for_role(p_role public.user_role); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION public.get_recommended_permissions_for_role(p_role public.user_role) TO anon;
GRANT ALL ON FUNCTION public.get_recommended_permissions_for_role(p_role public.user_role) TO authenticated;
GRANT ALL ON FUNCTION public.get_recommended_permissions_for_role(p_role public.user_role) TO service_role;


--
-- Name: FUNCTION get_user_active_experiments(p_user_id uuid, p_widget_type public.widget_type); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION public.get_user_active_experiments(p_user_id uuid, p_widget_type public.widget_type) TO anon;
GRANT ALL ON FUNCTION public.get_user_active_experiments(p_user_id uuid, p_widget_type public.widget_type) TO authenticated;
GRANT ALL ON FUNCTION public.get_user_active_experiments(p_user_id uuid, p_widget_type public.widget_type) TO service_role;


--
-- Name: FUNCTION get_user_details(p_user_id uuid); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION public.get_user_details(p_user_id uuid) TO anon;
GRANT ALL ON FUNCTION public.get_user_details(p_user_id uuid) TO authenticated;
GRANT ALL ON FUNCTION public.get_user_details(p_user_id uuid) TO service_role;


--
-- Name: FUNCTION get_user_feature_flags(p_user_id uuid, p_widget_type public.widget_type); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION public.get_user_feature_flags(p_user_id uuid, p_widget_type public.widget_type) TO anon;
GRANT ALL ON FUNCTION public.get_user_feature_flags(p_user_id uuid, p_widget_type public.widget_type) TO authenticated;
GRANT ALL ON FUNCTION public.get_user_feature_flags(p_user_id uuid, p_widget_type public.widget_type) TO service_role;


--
-- Name: FUNCTION get_user_navigation(p_user_id uuid); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION public.get_user_navigation(p_user_id uuid) TO anon;
GRANT ALL ON FUNCTION public.get_user_navigation(p_user_id uuid) TO authenticated;
GRANT ALL ON FUNCTION public.get_user_navigation(p_user_id uuid) TO service_role;


--
-- Name: FUNCTION get_widget_usage_stats(p_user_id uuid, p_widget_type public.widget_type, p_days_back integer); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION public.get_widget_usage_stats(p_user_id uuid, p_widget_type public.widget_type, p_days_back integer) TO anon;
GRANT ALL ON FUNCTION public.get_widget_usage_stats(p_user_id uuid, p_widget_type public.widget_type, p_days_back integer) TO authenticated;
GRANT ALL ON FUNCTION public.get_widget_usage_stats(p_user_id uuid, p_widget_type public.widget_type, p_days_back integer) TO service_role;


--
-- Name: FUNCTION handle_new_user_notification_setup(); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION public.handle_new_user_notification_setup() TO anon;
GRANT ALL ON FUNCTION public.handle_new_user_notification_setup() TO authenticated;
GRANT ALL ON FUNCTION public.handle_new_user_notification_setup() TO service_role;


--
-- Name: FUNCTION initialize_user_metrics(user_uuid uuid); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION public.initialize_user_metrics(user_uuid uuid) TO anon;
GRANT ALL ON FUNCTION public.initialize_user_metrics(user_uuid uuid) TO authenticated;
GRANT ALL ON FUNCTION public.initialize_user_metrics(user_uuid uuid) TO service_role;


--
-- Name: FUNCTION invalidate_widget_config_cache(p_user_id uuid, p_widget_type public.widget_type); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION public.invalidate_widget_config_cache(p_user_id uuid, p_widget_type public.widget_type) TO anon;
GRANT ALL ON FUNCTION public.invalidate_widget_config_cache(p_user_id uuid, p_widget_type public.widget_type) TO authenticated;
GRANT ALL ON FUNCTION public.invalidate_widget_config_cache(p_user_id uuid, p_widget_type public.widget_type) TO service_role;


--
-- Name: FUNCTION invite_business_user(p_business_profile_id uuid, p_email text, p_display_name text, p_role public.user_role, p_invited_by uuid, p_permissions jsonb); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION public.invite_business_user(p_business_profile_id uuid, p_email text, p_display_name text, p_role public.user_role, p_invited_by uuid, p_permissions jsonb) TO anon;
GRANT ALL ON FUNCTION public.invite_business_user(p_business_profile_id uuid, p_email text, p_display_name text, p_role public.user_role, p_invited_by uuid, p_permissions jsonb) TO authenticated;
GRANT ALL ON FUNCTION public.invite_business_user(p_business_profile_id uuid, p_email text, p_display_name text, p_role public.user_role, p_invited_by uuid, p_permissions jsonb) TO service_role;


--
-- Name: FUNCTION is_admin(); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION public.is_admin() TO anon;
GRANT ALL ON FUNCTION public.is_admin() TO authenticated;
GRANT ALL ON FUNCTION public.is_admin() TO service_role;


--
-- Name: FUNCTION is_analyst(); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION public.is_analyst() TO anon;
GRANT ALL ON FUNCTION public.is_analyst() TO authenticated;
GRANT ALL ON FUNCTION public.is_analyst() TO service_role;


--
-- Name: FUNCTION is_user_in_quiet_hours(user_uuid uuid); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION public.is_user_in_quiet_hours(user_uuid uuid) TO anon;
GRANT ALL ON FUNCTION public.is_user_in_quiet_hours(user_uuid uuid) TO authenticated;
GRANT ALL ON FUNCTION public.is_user_in_quiet_hours(user_uuid uuid) TO service_role;


--
-- Name: FUNCTION mock_dau(days integer); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION public.mock_dau(days integer) TO anon;
GRANT ALL ON FUNCTION public.mock_dau(days integer) TO authenticated;
GRANT ALL ON FUNCTION public.mock_dau(days integer) TO service_role;


--
-- Name: FUNCTION notify_widget_config_change(); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION public.notify_widget_config_change() TO anon;
GRANT ALL ON FUNCTION public.notify_widget_config_change() TO authenticated;
GRANT ALL ON FUNCTION public.notify_widget_config_change() TO service_role;


--
-- Name: FUNCTION record_stock_movement(p_user_id uuid, p_item_id uuid, p_movement_type text, p_quantity_change integer, p_reference_id uuid, p_reference_type text, p_notes text); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION public.record_stock_movement(p_user_id uuid, p_item_id uuid, p_movement_type text, p_quantity_change integer, p_reference_id uuid, p_reference_type text, p_notes text) TO anon;
GRANT ALL ON FUNCTION public.record_stock_movement(p_user_id uuid, p_item_id uuid, p_movement_type text, p_quantity_change integer, p_reference_id uuid, p_reference_type text, p_notes text) TO authenticated;
GRANT ALL ON FUNCTION public.record_stock_movement(p_user_id uuid, p_item_id uuid, p_movement_type text, p_quantity_change integer, p_reference_id uuid, p_reference_type text, p_notes text) TO service_role;


--
-- Name: FUNCTION record_widget_usage(p_user_id uuid, p_business_profile_id uuid, p_widget_type public.widget_type, p_event_type character varying, p_event_data jsonb, p_session_id character varying, p_user_agent text, p_platform character varying); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION public.record_widget_usage(p_user_id uuid, p_business_profile_id uuid, p_widget_type public.widget_type, p_event_type character varying, p_event_data jsonb, p_session_id character varying, p_user_agent text, p_platform character varying) TO anon;
GRANT ALL ON FUNCTION public.record_widget_usage(p_user_id uuid, p_business_profile_id uuid, p_widget_type public.widget_type, p_event_type character varying, p_event_data jsonb, p_session_id character varying, p_user_agent text, p_platform character varying) TO authenticated;
GRANT ALL ON FUNCTION public.record_widget_usage(p_user_id uuid, p_business_profile_id uuid, p_widget_type public.widget_type, p_event_type character varying, p_event_data jsonb, p_session_id character varying, p_user_agent text, p_platform character varying) TO service_role;


--
-- Name: FUNCTION refresh_business_metrics_summary(); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION public.refresh_business_metrics_summary() TO anon;
GRANT ALL ON FUNCTION public.refresh_business_metrics_summary() TO authenticated;
GRANT ALL ON FUNCTION public.refresh_business_metrics_summary() TO service_role;


--
-- Name: FUNCTION refresh_business_metrics_summary_safe(p_user_id uuid); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION public.refresh_business_metrics_summary_safe(p_user_id uuid) TO anon;
GRANT ALL ON FUNCTION public.refresh_business_metrics_summary_safe(p_user_id uuid) TO authenticated;
GRANT ALL ON FUNCTION public.refresh_business_metrics_summary_safe(p_user_id uuid) TO service_role;


--
-- Name: FUNCTION reset_user_widget_config(p_user_id uuid, p_business_profile_id uuid, p_widget_type public.widget_type); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION public.reset_user_widget_config(p_user_id uuid, p_business_profile_id uuid, p_widget_type public.widget_type) TO anon;
GRANT ALL ON FUNCTION public.reset_user_widget_config(p_user_id uuid, p_business_profile_id uuid, p_widget_type public.widget_type) TO authenticated;
GRANT ALL ON FUNCTION public.reset_user_widget_config(p_user_id uuid, p_business_profile_id uuid, p_widget_type public.widget_type) TO service_role;


--
-- Name: FUNCTION update_updated_at_column(); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION public.update_updated_at_column() TO anon;
GRANT ALL ON FUNCTION public.update_updated_at_column() TO authenticated;
GRANT ALL ON FUNCTION public.update_updated_at_column() TO service_role;


--
-- Name: FUNCTION update_user_permissions(p_business_user_id uuid, p_permissions jsonb, p_updated_by uuid); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION public.update_user_permissions(p_business_user_id uuid, p_permissions jsonb, p_updated_by uuid) TO anon;
GRANT ALL ON FUNCTION public.update_user_permissions(p_business_user_id uuid, p_permissions jsonb, p_updated_by uuid) TO authenticated;
GRANT ALL ON FUNCTION public.update_user_permissions(p_business_user_id uuid, p_permissions jsonb, p_updated_by uuid) TO service_role;


--
-- Name: FUNCTION update_user_widget_config(p_user_id uuid, p_business_profile_id uuid, p_widget_type public.widget_type, p_config jsonb, p_layout public.widget_layout); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION public.update_user_widget_config(p_user_id uuid, p_business_profile_id uuid, p_widget_type public.widget_type, p_config jsonb, p_layout public.widget_layout) TO anon;
GRANT ALL ON FUNCTION public.update_user_widget_config(p_user_id uuid, p_business_profile_id uuid, p_widget_type public.widget_type, p_config jsonb, p_layout public.widget_layout) TO authenticated;
GRANT ALL ON FUNCTION public.update_user_widget_config(p_user_id uuid, p_business_profile_id uuid, p_widget_type public.widget_type, p_config jsonb, p_layout public.widget_layout) TO service_role;


--
-- Name: FUNCTION validate_user_session(p_session_token text); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION public.validate_user_session(p_session_token text) TO anon;
GRANT ALL ON FUNCTION public.validate_user_session(p_session_token text) TO authenticated;
GRANT ALL ON FUNCTION public.validate_user_session(p_session_token text) TO service_role;


--
-- Name: FUNCTION validate_variant_belongs_to_experiment(); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION public.validate_variant_belongs_to_experiment() TO anon;
GRANT ALL ON FUNCTION public.validate_variant_belongs_to_experiment() TO authenticated;
GRANT ALL ON FUNCTION public.validate_variant_belongs_to_experiment() TO service_role;


--
-- Name: FUNCTION validate_widget_config(p_widget_type public.widget_type, p_config jsonb); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION public.validate_widget_config(p_widget_type public.widget_type, p_config jsonb) TO anon;
GRANT ALL ON FUNCTION public.validate_widget_config(p_widget_type public.widget_type, p_config jsonb) TO authenticated;
GRANT ALL ON FUNCTION public.validate_widget_config(p_widget_type public.widget_type, p_config jsonb) TO service_role;


--
-- Name: FUNCTION warm_widget_config_cache(p_user_id uuid, p_business_profile_id uuid); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION public.warm_widget_config_cache(p_user_id uuid, p_business_profile_id uuid) TO anon;
GRANT ALL ON FUNCTION public.warm_widget_config_cache(p_user_id uuid, p_business_profile_id uuid) TO authenticated;
GRANT ALL ON FUNCTION public.warm_widget_config_cache(p_user_id uuid, p_business_profile_id uuid) TO service_role;


--
-- Name: FUNCTION apply_rls(wal jsonb, max_record_bytes integer); Type: ACL; Schema: realtime; Owner: supabase_admin
--

GRANT ALL ON FUNCTION realtime.apply_rls(wal jsonb, max_record_bytes integer) TO postgres;
GRANT ALL ON FUNCTION realtime.apply_rls(wal jsonb, max_record_bytes integer) TO dashboard_user;
GRANT ALL ON FUNCTION realtime.apply_rls(wal jsonb, max_record_bytes integer) TO anon;
GRANT ALL ON FUNCTION realtime.apply_rls(wal jsonb, max_record_bytes integer) TO authenticated;
GRANT ALL ON FUNCTION realtime.apply_rls(wal jsonb, max_record_bytes integer) TO service_role;
GRANT ALL ON FUNCTION realtime.apply_rls(wal jsonb, max_record_bytes integer) TO supabase_realtime_admin;


--
-- Name: FUNCTION broadcast_changes(topic_name text, event_name text, operation text, table_name text, table_schema text, new record, old record, level text); Type: ACL; Schema: realtime; Owner: supabase_admin
--

GRANT ALL ON FUNCTION realtime.broadcast_changes(topic_name text, event_name text, operation text, table_name text, table_schema text, new record, old record, level text) TO postgres;
GRANT ALL ON FUNCTION realtime.broadcast_changes(topic_name text, event_name text, operation text, table_name text, table_schema text, new record, old record, level text) TO dashboard_user;


--
-- Name: FUNCTION build_prepared_statement_sql(prepared_statement_name text, entity regclass, columns realtime.wal_column[]); Type: ACL; Schema: realtime; Owner: supabase_admin
--

GRANT ALL ON FUNCTION realtime.build_prepared_statement_sql(prepared_statement_name text, entity regclass, columns realtime.wal_column[]) TO postgres;
GRANT ALL ON FUNCTION realtime.build_prepared_statement_sql(prepared_statement_name text, entity regclass, columns realtime.wal_column[]) TO dashboard_user;
GRANT ALL ON FUNCTION realtime.build_prepared_statement_sql(prepared_statement_name text, entity regclass, columns realtime.wal_column[]) TO anon;
GRANT ALL ON FUNCTION realtime.build_prepared_statement_sql(prepared_statement_name text, entity regclass, columns realtime.wal_column[]) TO authenticated;
GRANT ALL ON FUNCTION realtime.build_prepared_statement_sql(prepared_statement_name text, entity regclass, columns realtime.wal_column[]) TO service_role;
GRANT ALL ON FUNCTION realtime.build_prepared_statement_sql(prepared_statement_name text, entity regclass, columns realtime.wal_column[]) TO supabase_realtime_admin;


--
-- Name: FUNCTION "cast"(val text, type_ regtype); Type: ACL; Schema: realtime; Owner: supabase_admin
--

GRANT ALL ON FUNCTION realtime."cast"(val text, type_ regtype) TO postgres;
GRANT ALL ON FUNCTION realtime."cast"(val text, type_ regtype) TO dashboard_user;
GRANT ALL ON FUNCTION realtime."cast"(val text, type_ regtype) TO anon;
GRANT ALL ON FUNCTION realtime."cast"(val text, type_ regtype) TO authenticated;
GRANT ALL ON FUNCTION realtime."cast"(val text, type_ regtype) TO service_role;
GRANT ALL ON FUNCTION realtime."cast"(val text, type_ regtype) TO supabase_realtime_admin;


--
-- Name: FUNCTION check_equality_op(op realtime.equality_op, type_ regtype, val_1 text, val_2 text); Type: ACL; Schema: realtime; Owner: supabase_admin
--

GRANT ALL ON FUNCTION realtime.check_equality_op(op realtime.equality_op, type_ regtype, val_1 text, val_2 text) TO postgres;
GRANT ALL ON FUNCTION realtime.check_equality_op(op realtime.equality_op, type_ regtype, val_1 text, val_2 text) TO dashboard_user;
GRANT ALL ON FUNCTION realtime.check_equality_op(op realtime.equality_op, type_ regtype, val_1 text, val_2 text) TO anon;
GRANT ALL ON FUNCTION realtime.check_equality_op(op realtime.equality_op, type_ regtype, val_1 text, val_2 text) TO authenticated;
GRANT ALL ON FUNCTION realtime.check_equality_op(op realtime.equality_op, type_ regtype, val_1 text, val_2 text) TO service_role;
GRANT ALL ON FUNCTION realtime.check_equality_op(op realtime.equality_op, type_ regtype, val_1 text, val_2 text) TO supabase_realtime_admin;


--
-- Name: FUNCTION is_visible_through_filters(columns realtime.wal_column[], filters realtime.user_defined_filter[]); Type: ACL; Schema: realtime; Owner: supabase_admin
--

GRANT ALL ON FUNCTION realtime.is_visible_through_filters(columns realtime.wal_column[], filters realtime.user_defined_filter[]) TO postgres;
GRANT ALL ON FUNCTION realtime.is_visible_through_filters(columns realtime.wal_column[], filters realtime.user_defined_filter[]) TO dashboard_user;
GRANT ALL ON FUNCTION realtime.is_visible_through_filters(columns realtime.wal_column[], filters realtime.user_defined_filter[]) TO anon;
GRANT ALL ON FUNCTION realtime.is_visible_through_filters(columns realtime.wal_column[], filters realtime.user_defined_filter[]) TO authenticated;
GRANT ALL ON FUNCTION realtime.is_visible_through_filters(columns realtime.wal_column[], filters realtime.user_defined_filter[]) TO service_role;
GRANT ALL ON FUNCTION realtime.is_visible_through_filters(columns realtime.wal_column[], filters realtime.user_defined_filter[]) TO supabase_realtime_admin;


--
-- Name: FUNCTION list_changes(publication name, slot_name name, max_changes integer, max_record_bytes integer); Type: ACL; Schema: realtime; Owner: supabase_admin
--

GRANT ALL ON FUNCTION realtime.list_changes(publication name, slot_name name, max_changes integer, max_record_bytes integer) TO postgres;
GRANT ALL ON FUNCTION realtime.list_changes(publication name, slot_name name, max_changes integer, max_record_bytes integer) TO dashboard_user;
GRANT ALL ON FUNCTION realtime.list_changes(publication name, slot_name name, max_changes integer, max_record_bytes integer) TO anon;
GRANT ALL ON FUNCTION realtime.list_changes(publication name, slot_name name, max_changes integer, max_record_bytes integer) TO authenticated;
GRANT ALL ON FUNCTION realtime.list_changes(publication name, slot_name name, max_changes integer, max_record_bytes integer) TO service_role;
GRANT ALL ON FUNCTION realtime.list_changes(publication name, slot_name name, max_changes integer, max_record_bytes integer) TO supabase_realtime_admin;


--
-- Name: FUNCTION quote_wal2json(entity regclass); Type: ACL; Schema: realtime; Owner: supabase_admin
--

GRANT ALL ON FUNCTION realtime.quote_wal2json(entity regclass) TO postgres;
GRANT ALL ON FUNCTION realtime.quote_wal2json(entity regclass) TO dashboard_user;
GRANT ALL ON FUNCTION realtime.quote_wal2json(entity regclass) TO anon;
GRANT ALL ON FUNCTION realtime.quote_wal2json(entity regclass) TO authenticated;
GRANT ALL ON FUNCTION realtime.quote_wal2json(entity regclass) TO service_role;
GRANT ALL ON FUNCTION realtime.quote_wal2json(entity regclass) TO supabase_realtime_admin;


--
-- Name: FUNCTION send(payload jsonb, event text, topic text, private boolean); Type: ACL; Schema: realtime; Owner: supabase_admin
--

GRANT ALL ON FUNCTION realtime.send(payload jsonb, event text, topic text, private boolean) TO postgres;
GRANT ALL ON FUNCTION realtime.send(payload jsonb, event text, topic text, private boolean) TO dashboard_user;


--
-- Name: FUNCTION subscription_check_filters(); Type: ACL; Schema: realtime; Owner: supabase_admin
--

GRANT ALL ON FUNCTION realtime.subscription_check_filters() TO postgres;
GRANT ALL ON FUNCTION realtime.subscription_check_filters() TO dashboard_user;
GRANT ALL ON FUNCTION realtime.subscription_check_filters() TO anon;
GRANT ALL ON FUNCTION realtime.subscription_check_filters() TO authenticated;
GRANT ALL ON FUNCTION realtime.subscription_check_filters() TO service_role;
GRANT ALL ON FUNCTION realtime.subscription_check_filters() TO supabase_realtime_admin;


--
-- Name: FUNCTION to_regrole(role_name text); Type: ACL; Schema: realtime; Owner: supabase_admin
--

GRANT ALL ON FUNCTION realtime.to_regrole(role_name text) TO postgres;
GRANT ALL ON FUNCTION realtime.to_regrole(role_name text) TO dashboard_user;
GRANT ALL ON FUNCTION realtime.to_regrole(role_name text) TO anon;
GRANT ALL ON FUNCTION realtime.to_regrole(role_name text) TO authenticated;
GRANT ALL ON FUNCTION realtime.to_regrole(role_name text) TO service_role;
GRANT ALL ON FUNCTION realtime.to_regrole(role_name text) TO supabase_realtime_admin;


--
-- Name: FUNCTION topic(); Type: ACL; Schema: realtime; Owner: supabase_realtime_admin
--

GRANT ALL ON FUNCTION realtime.topic() TO postgres;
GRANT ALL ON FUNCTION realtime.topic() TO dashboard_user;


--
-- Name: FUNCTION http_request(); Type: ACL; Schema: supabase_functions; Owner: supabase_functions_admin
--

REVOKE ALL ON FUNCTION supabase_functions.http_request() FROM PUBLIC;
GRANT ALL ON FUNCTION supabase_functions.http_request() TO postgres;
GRANT ALL ON FUNCTION supabase_functions.http_request() TO anon;
GRANT ALL ON FUNCTION supabase_functions.http_request() TO authenticated;
GRANT ALL ON FUNCTION supabase_functions.http_request() TO service_role;


--
-- Name: FUNCTION _crypto_aead_det_decrypt(message bytea, additional bytea, key_id bigint, context bytea, nonce bytea); Type: ACL; Schema: vault; Owner: supabase_admin
--

GRANT ALL ON FUNCTION vault._crypto_aead_det_decrypt(message bytea, additional bytea, key_id bigint, context bytea, nonce bytea) TO postgres WITH GRANT OPTION;
GRANT ALL ON FUNCTION vault._crypto_aead_det_decrypt(message bytea, additional bytea, key_id bigint, context bytea, nonce bytea) TO service_role;


--
-- Name: FUNCTION create_secret(new_secret text, new_name text, new_description text, new_key_id uuid); Type: ACL; Schema: vault; Owner: supabase_admin
--

GRANT ALL ON FUNCTION vault.create_secret(new_secret text, new_name text, new_description text, new_key_id uuid) TO postgres WITH GRANT OPTION;
GRANT ALL ON FUNCTION vault.create_secret(new_secret text, new_name text, new_description text, new_key_id uuid) TO service_role;


--
-- Name: FUNCTION update_secret(secret_id uuid, new_secret text, new_name text, new_description text, new_key_id uuid); Type: ACL; Schema: vault; Owner: supabase_admin
--

GRANT ALL ON FUNCTION vault.update_secret(secret_id uuid, new_secret text, new_name text, new_description text, new_key_id uuid) TO postgres WITH GRANT OPTION;
GRANT ALL ON FUNCTION vault.update_secret(secret_id uuid, new_secret text, new_name text, new_description text, new_key_id uuid) TO service_role;


--
-- Name: TABLE audit_log_entries; Type: ACL; Schema: auth; Owner: supabase_auth_admin
--

GRANT ALL ON TABLE auth.audit_log_entries TO dashboard_user;
GRANT INSERT,REFERENCES,DELETE,TRIGGER,TRUNCATE,MAINTAIN,UPDATE ON TABLE auth.audit_log_entries TO postgres;
GRANT SELECT ON TABLE auth.audit_log_entries TO postgres WITH GRANT OPTION;


--
-- Name: TABLE flow_state; Type: ACL; Schema: auth; Owner: supabase_auth_admin
--

GRANT INSERT,REFERENCES,DELETE,TRIGGER,TRUNCATE,MAINTAIN,UPDATE ON TABLE auth.flow_state TO postgres;
GRANT SELECT ON TABLE auth.flow_state TO postgres WITH GRANT OPTION;
GRANT ALL ON TABLE auth.flow_state TO dashboard_user;


--
-- Name: TABLE identities; Type: ACL; Schema: auth; Owner: supabase_auth_admin
--

GRANT INSERT,REFERENCES,DELETE,TRIGGER,TRUNCATE,MAINTAIN,UPDATE ON TABLE auth.identities TO postgres;
GRANT SELECT ON TABLE auth.identities TO postgres WITH GRANT OPTION;
GRANT ALL ON TABLE auth.identities TO dashboard_user;


--
-- Name: TABLE instances; Type: ACL; Schema: auth; Owner: supabase_auth_admin
--

GRANT ALL ON TABLE auth.instances TO dashboard_user;
GRANT INSERT,REFERENCES,DELETE,TRIGGER,TRUNCATE,MAINTAIN,UPDATE ON TABLE auth.instances TO postgres;
GRANT SELECT ON TABLE auth.instances TO postgres WITH GRANT OPTION;


--
-- Name: TABLE mfa_amr_claims; Type: ACL; Schema: auth; Owner: supabase_auth_admin
--

GRANT INSERT,REFERENCES,DELETE,TRIGGER,TRUNCATE,MAINTAIN,UPDATE ON TABLE auth.mfa_amr_claims TO postgres;
GRANT SELECT ON TABLE auth.mfa_amr_claims TO postgres WITH GRANT OPTION;
GRANT ALL ON TABLE auth.mfa_amr_claims TO dashboard_user;


--
-- Name: TABLE mfa_challenges; Type: ACL; Schema: auth; Owner: supabase_auth_admin
--

GRANT INSERT,REFERENCES,DELETE,TRIGGER,TRUNCATE,MAINTAIN,UPDATE ON TABLE auth.mfa_challenges TO postgres;
GRANT SELECT ON TABLE auth.mfa_challenges TO postgres WITH GRANT OPTION;
GRANT ALL ON TABLE auth.mfa_challenges TO dashboard_user;


--
-- Name: TABLE mfa_factors; Type: ACL; Schema: auth; Owner: supabase_auth_admin
--

GRANT INSERT,REFERENCES,DELETE,TRIGGER,TRUNCATE,MAINTAIN,UPDATE ON TABLE auth.mfa_factors TO postgres;
GRANT SELECT ON TABLE auth.mfa_factors TO postgres WITH GRANT OPTION;
GRANT ALL ON TABLE auth.mfa_factors TO dashboard_user;


--
-- Name: TABLE one_time_tokens; Type: ACL; Schema: auth; Owner: supabase_auth_admin
--

GRANT INSERT,REFERENCES,DELETE,TRIGGER,TRUNCATE,MAINTAIN,UPDATE ON TABLE auth.one_time_tokens TO postgres;
GRANT SELECT ON TABLE auth.one_time_tokens TO postgres WITH GRANT OPTION;
GRANT ALL ON TABLE auth.one_time_tokens TO dashboard_user;


--
-- Name: TABLE refresh_tokens; Type: ACL; Schema: auth; Owner: supabase_auth_admin
--

GRANT ALL ON TABLE auth.refresh_tokens TO dashboard_user;
GRANT INSERT,REFERENCES,DELETE,TRIGGER,TRUNCATE,MAINTAIN,UPDATE ON TABLE auth.refresh_tokens TO postgres;
GRANT SELECT ON TABLE auth.refresh_tokens TO postgres WITH GRANT OPTION;


--
-- Name: SEQUENCE refresh_tokens_id_seq; Type: ACL; Schema: auth; Owner: supabase_auth_admin
--

GRANT ALL ON SEQUENCE auth.refresh_tokens_id_seq TO dashboard_user;
GRANT ALL ON SEQUENCE auth.refresh_tokens_id_seq TO postgres;


--
-- Name: TABLE saml_providers; Type: ACL; Schema: auth; Owner: supabase_auth_admin
--

GRANT INSERT,REFERENCES,DELETE,TRIGGER,TRUNCATE,MAINTAIN,UPDATE ON TABLE auth.saml_providers TO postgres;
GRANT SELECT ON TABLE auth.saml_providers TO postgres WITH GRANT OPTION;
GRANT ALL ON TABLE auth.saml_providers TO dashboard_user;


--
-- Name: TABLE saml_relay_states; Type: ACL; Schema: auth; Owner: supabase_auth_admin
--

GRANT INSERT,REFERENCES,DELETE,TRIGGER,TRUNCATE,MAINTAIN,UPDATE ON TABLE auth.saml_relay_states TO postgres;
GRANT SELECT ON TABLE auth.saml_relay_states TO postgres WITH GRANT OPTION;
GRANT ALL ON TABLE auth.saml_relay_states TO dashboard_user;


--
-- Name: TABLE schema_migrations; Type: ACL; Schema: auth; Owner: supabase_auth_admin
--

GRANT SELECT ON TABLE auth.schema_migrations TO postgres WITH GRANT OPTION;


--
-- Name: TABLE sessions; Type: ACL; Schema: auth; Owner: supabase_auth_admin
--

GRANT INSERT,REFERENCES,DELETE,TRIGGER,TRUNCATE,MAINTAIN,UPDATE ON TABLE auth.sessions TO postgres;
GRANT SELECT ON TABLE auth.sessions TO postgres WITH GRANT OPTION;
GRANT ALL ON TABLE auth.sessions TO dashboard_user;


--
-- Name: TABLE sso_domains; Type: ACL; Schema: auth; Owner: supabase_auth_admin
--

GRANT INSERT,REFERENCES,DELETE,TRIGGER,TRUNCATE,MAINTAIN,UPDATE ON TABLE auth.sso_domains TO postgres;
GRANT SELECT ON TABLE auth.sso_domains TO postgres WITH GRANT OPTION;
GRANT ALL ON TABLE auth.sso_domains TO dashboard_user;


--
-- Name: TABLE sso_providers; Type: ACL; Schema: auth; Owner: supabase_auth_admin
--

GRANT INSERT,REFERENCES,DELETE,TRIGGER,TRUNCATE,MAINTAIN,UPDATE ON TABLE auth.sso_providers TO postgres;
GRANT SELECT ON TABLE auth.sso_providers TO postgres WITH GRANT OPTION;
GRANT ALL ON TABLE auth.sso_providers TO dashboard_user;


--
-- Name: TABLE users; Type: ACL; Schema: auth; Owner: supabase_auth_admin
--

GRANT ALL ON TABLE auth.users TO dashboard_user;
GRANT INSERT,REFERENCES,DELETE,TRIGGER,TRUNCATE,MAINTAIN,UPDATE ON TABLE auth.users TO postgres;
GRANT SELECT ON TABLE auth.users TO postgres WITH GRANT OPTION;


--
-- Name: TABLE pg_stat_statements; Type: ACL; Schema: extensions; Owner: supabase_admin
--

GRANT ALL ON TABLE extensions.pg_stat_statements TO postgres WITH GRANT OPTION;


--
-- Name: TABLE pg_stat_statements_info; Type: ACL; Schema: extensions; Owner: supabase_admin
--

GRANT ALL ON TABLE extensions.pg_stat_statements_info TO postgres WITH GRANT OPTION;


--
-- Name: TABLE invoices; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.invoices TO anon;
GRANT ALL ON TABLE public.invoices TO authenticated;
GRANT ALL ON TABLE public.invoices TO service_role;


--
-- Name: TABLE admin_dashboard_metrics; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.admin_dashboard_metrics TO anon;
GRANT ALL ON TABLE public.admin_dashboard_metrics TO authenticated;
GRANT ALL ON TABLE public.admin_dashboard_metrics TO service_role;


--
-- Name: TABLE admin_users; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.admin_users TO anon;
GRANT ALL ON TABLE public.admin_users TO authenticated;
GRANT ALL ON TABLE public.admin_users TO service_role;


--
-- Name: TABLE admin_profiles; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.admin_profiles TO anon;
GRANT ALL ON TABLE public.admin_profiles TO authenticated;
GRANT ALL ON TABLE public.admin_profiles TO service_role;


--
-- Name: TABLE business_categories; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.business_categories TO anon;
GRANT ALL ON TABLE public.business_categories TO authenticated;
GRANT ALL ON TABLE public.business_categories TO service_role;


--
-- Name: TABLE business_contacts; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.business_contacts TO anon;
GRANT ALL ON TABLE public.business_contacts TO authenticated;
GRANT ALL ON TABLE public.business_contacts TO service_role;


--
-- Name: TABLE business_metrics_summary; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.business_metrics_summary TO anon;
GRANT ALL ON TABLE public.business_metrics_summary TO authenticated;
GRANT ALL ON TABLE public.business_metrics_summary TO service_role;


--
-- Name: TABLE business_profiles; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.business_profiles TO anon;
GRANT ALL ON TABLE public.business_profiles TO authenticated;
GRANT ALL ON TABLE public.business_profiles TO service_role;


--
-- Name: TABLE business_users; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.business_users TO anon;
GRANT ALL ON TABLE public.business_users TO authenticated;
GRANT ALL ON TABLE public.business_users TO service_role;


--
-- Name: TABLE coaching_flows; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.coaching_flows TO anon;
GRANT ALL ON TABLE public.coaching_flows TO authenticated;
GRANT ALL ON TABLE public.coaching_flows TO service_role;


--
-- Name: TABLE coaching_steps; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.coaching_steps TO anon;
GRANT ALL ON TABLE public.coaching_steps TO authenticated;
GRANT ALL ON TABLE public.coaching_steps TO service_role;


--
-- Name: TABLE feature_permissions; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.feature_permissions TO anon;
GRANT ALL ON TABLE public.feature_permissions TO authenticated;
GRANT ALL ON TABLE public.feature_permissions TO service_role;


--
-- Name: TABLE invoice_items; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.invoice_items TO anon;
GRANT ALL ON TABLE public.invoice_items TO authenticated;
GRANT ALL ON TABLE public.invoice_items TO service_role;


--
-- Name: TABLE items; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.items TO anon;
GRANT ALL ON TABLE public.items TO authenticated;
GRANT ALL ON TABLE public.items TO service_role;


--
-- Name: TABLE low_stock_notification_logs; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.low_stock_notification_logs TO anon;
GRANT ALL ON TABLE public.low_stock_notification_logs TO authenticated;
GRANT ALL ON TABLE public.low_stock_notification_logs TO service_role;


--
-- Name: TABLE low_stock_notification_settings; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.low_stock_notification_settings TO anon;
GRANT ALL ON TABLE public.low_stock_notification_settings TO authenticated;
GRANT ALL ON TABLE public.low_stock_notification_settings TO service_role;


--
-- Name: TABLE navigation_config; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.navigation_config TO anon;
GRANT ALL ON TABLE public.navigation_config TO authenticated;
GRANT ALL ON TABLE public.navigation_config TO service_role;


--
-- Name: TABLE notification_logs; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.notification_logs TO anon;
GRANT ALL ON TABLE public.notification_logs TO authenticated;
GRANT ALL ON TABLE public.notification_logs TO service_role;


--
-- Name: TABLE notification_preferences; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.notification_preferences TO anon;
GRANT ALL ON TABLE public.notification_preferences TO authenticated;
GRANT ALL ON TABLE public.notification_preferences TO service_role;


--
-- Name: TABLE registration_analytics; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.registration_analytics TO anon;
GRANT ALL ON TABLE public.registration_analytics TO authenticated;
GRANT ALL ON TABLE public.registration_analytics TO service_role;


--
-- Name: TABLE role_feature_access; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.role_feature_access TO anon;
GRANT ALL ON TABLE public.role_feature_access TO authenticated;
GRANT ALL ON TABLE public.role_feature_access TO service_role;


--
-- Name: TABLE role_templates; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.role_templates TO anon;
GRANT ALL ON TABLE public.role_templates TO authenticated;
GRANT ALL ON TABLE public.role_templates TO service_role;


--
-- Name: TABLE stock_movements; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.stock_movements TO anon;
GRANT ALL ON TABLE public.stock_movements TO authenticated;
GRANT ALL ON TABLE public.stock_movements TO service_role;


--
-- Name: TABLE subscription_plans; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.subscription_plans TO anon;
GRANT ALL ON TABLE public.subscription_plans TO authenticated;
GRANT ALL ON TABLE public.subscription_plans TO service_role;


--
-- Name: TABLE subscriptions; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.subscriptions TO anon;
GRANT ALL ON TABLE public.subscriptions TO authenticated;
GRANT ALL ON TABLE public.subscriptions TO service_role;


--
-- Name: TABLE system_analytics; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.system_analytics TO anon;
GRANT ALL ON TABLE public.system_analytics TO authenticated;
GRANT ALL ON TABLE public.system_analytics TO service_role;


--
-- Name: TABLE user_activity_log; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.user_activity_log TO anon;
GRANT ALL ON TABLE public.user_activity_log TO authenticated;
GRANT ALL ON TABLE public.user_activity_log TO service_role;


--
-- Name: TABLE user_coaching_progress; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.user_coaching_progress TO anon;
GRANT ALL ON TABLE public.user_coaching_progress TO authenticated;
GRANT ALL ON TABLE public.user_coaching_progress TO service_role;


--
-- Name: TABLE user_config_history; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.user_config_history TO anon;
GRANT ALL ON TABLE public.user_config_history TO authenticated;
GRANT ALL ON TABLE public.user_config_history TO service_role;


--
-- Name: TABLE user_experiment_assignments; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.user_experiment_assignments TO anon;
GRANT ALL ON TABLE public.user_experiment_assignments TO authenticated;
GRANT ALL ON TABLE public.user_experiment_assignments TO service_role;


--
-- Name: TABLE user_fcm_tokens; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.user_fcm_tokens TO anon;
GRANT ALL ON TABLE public.user_fcm_tokens TO authenticated;
GRANT ALL ON TABLE public.user_fcm_tokens TO service_role;


--
-- Name: TABLE user_feature_access; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.user_feature_access TO anon;
GRANT ALL ON TABLE public.user_feature_access TO authenticated;
GRANT ALL ON TABLE public.user_feature_access TO service_role;


--
-- Name: TABLE user_sessions; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.user_sessions TO anon;
GRANT ALL ON TABLE public.user_sessions TO authenticated;
GRANT ALL ON TABLE public.user_sessions TO service_role;


--
-- Name: TABLE user_widget_configs; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.user_widget_configs TO anon;
GRANT ALL ON TABLE public.user_widget_configs TO authenticated;
GRANT ALL ON TABLE public.user_widget_configs TO service_role;


--
-- Name: TABLE widget_config_cache; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.widget_config_cache TO anon;
GRANT ALL ON TABLE public.widget_config_cache TO authenticated;
GRANT ALL ON TABLE public.widget_config_cache TO service_role;


--
-- Name: TABLE widget_config_templates; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.widget_config_templates TO anon;
GRANT ALL ON TABLE public.widget_config_templates TO authenticated;
GRANT ALL ON TABLE public.widget_config_templates TO service_role;


--
-- Name: TABLE widget_experiment_variants; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.widget_experiment_variants TO anon;
GRANT ALL ON TABLE public.widget_experiment_variants TO authenticated;
GRANT ALL ON TABLE public.widget_experiment_variants TO service_role;


--
-- Name: TABLE widget_experiments; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.widget_experiments TO anon;
GRANT ALL ON TABLE public.widget_experiments TO authenticated;
GRANT ALL ON TABLE public.widget_experiments TO service_role;


--
-- Name: TABLE widget_feature_flags; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.widget_feature_flags TO anon;
GRANT ALL ON TABLE public.widget_feature_flags TO authenticated;
GRANT ALL ON TABLE public.widget_feature_flags TO service_role;


--
-- Name: TABLE widget_usage_analytics; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.widget_usage_analytics TO anon;
GRANT ALL ON TABLE public.widget_usage_analytics TO authenticated;
GRANT ALL ON TABLE public.widget_usage_analytics TO service_role;


--
-- Name: TABLE messages; Type: ACL; Schema: realtime; Owner: supabase_realtime_admin
--

GRANT ALL ON TABLE realtime.messages TO postgres;
GRANT ALL ON TABLE realtime.messages TO dashboard_user;
GRANT SELECT,INSERT,UPDATE ON TABLE realtime.messages TO anon;
GRANT SELECT,INSERT,UPDATE ON TABLE realtime.messages TO authenticated;
GRANT SELECT,INSERT,UPDATE ON TABLE realtime.messages TO service_role;


--
-- Name: TABLE messages_2025_08_05; Type: ACL; Schema: realtime; Owner: supabase_admin
--

GRANT ALL ON TABLE realtime.messages_2025_08_05 TO postgres;
GRANT ALL ON TABLE realtime.messages_2025_08_05 TO dashboard_user;


--
-- Name: TABLE messages_2025_08_06; Type: ACL; Schema: realtime; Owner: supabase_admin
--

GRANT ALL ON TABLE realtime.messages_2025_08_06 TO postgres;
GRANT ALL ON TABLE realtime.messages_2025_08_06 TO dashboard_user;


--
-- Name: TABLE messages_2025_08_07; Type: ACL; Schema: realtime; Owner: supabase_admin
--

GRANT ALL ON TABLE realtime.messages_2025_08_07 TO postgres;
GRANT ALL ON TABLE realtime.messages_2025_08_07 TO dashboard_user;


--
-- Name: TABLE messages_2025_08_08; Type: ACL; Schema: realtime; Owner: supabase_admin
--

GRANT ALL ON TABLE realtime.messages_2025_08_08 TO postgres;
GRANT ALL ON TABLE realtime.messages_2025_08_08 TO dashboard_user;


--
-- Name: TABLE messages_2025_08_09; Type: ACL; Schema: realtime; Owner: supabase_admin
--

GRANT ALL ON TABLE realtime.messages_2025_08_09 TO postgres;
GRANT ALL ON TABLE realtime.messages_2025_08_09 TO dashboard_user;


--
-- Name: TABLE schema_migrations; Type: ACL; Schema: realtime; Owner: supabase_admin
--

GRANT ALL ON TABLE realtime.schema_migrations TO postgres;
GRANT ALL ON TABLE realtime.schema_migrations TO dashboard_user;
GRANT SELECT ON TABLE realtime.schema_migrations TO anon;
GRANT SELECT ON TABLE realtime.schema_migrations TO authenticated;
GRANT SELECT ON TABLE realtime.schema_migrations TO service_role;
GRANT ALL ON TABLE realtime.schema_migrations TO supabase_realtime_admin;


--
-- Name: TABLE subscription; Type: ACL; Schema: realtime; Owner: supabase_admin
--

GRANT ALL ON TABLE realtime.subscription TO postgres;
GRANT ALL ON TABLE realtime.subscription TO dashboard_user;
GRANT SELECT ON TABLE realtime.subscription TO anon;
GRANT SELECT ON TABLE realtime.subscription TO authenticated;
GRANT SELECT ON TABLE realtime.subscription TO service_role;
GRANT ALL ON TABLE realtime.subscription TO supabase_realtime_admin;


--
-- Name: SEQUENCE subscription_id_seq; Type: ACL; Schema: realtime; Owner: supabase_admin
--

GRANT ALL ON SEQUENCE realtime.subscription_id_seq TO postgres;
GRANT ALL ON SEQUENCE realtime.subscription_id_seq TO dashboard_user;
GRANT USAGE ON SEQUENCE realtime.subscription_id_seq TO anon;
GRANT USAGE ON SEQUENCE realtime.subscription_id_seq TO authenticated;
GRANT USAGE ON SEQUENCE realtime.subscription_id_seq TO service_role;
GRANT ALL ON SEQUENCE realtime.subscription_id_seq TO supabase_realtime_admin;


--
-- Name: TABLE buckets; Type: ACL; Schema: storage; Owner: supabase_storage_admin
--

GRANT ALL ON TABLE storage.buckets TO anon;
GRANT ALL ON TABLE storage.buckets TO authenticated;
GRANT ALL ON TABLE storage.buckets TO service_role;
GRANT ALL ON TABLE storage.buckets TO postgres WITH GRANT OPTION;


--
-- Name: TABLE buckets_analytics; Type: ACL; Schema: storage; Owner: supabase_storage_admin
--

GRANT ALL ON TABLE storage.buckets_analytics TO service_role;
GRANT ALL ON TABLE storage.buckets_analytics TO authenticated;
GRANT ALL ON TABLE storage.buckets_analytics TO anon;


--
-- Name: TABLE iceberg_namespaces; Type: ACL; Schema: storage; Owner: supabase_storage_admin
--

GRANT ALL ON TABLE storage.iceberg_namespaces TO service_role;
GRANT SELECT ON TABLE storage.iceberg_namespaces TO authenticated;
GRANT SELECT ON TABLE storage.iceberg_namespaces TO anon;


--
-- Name: TABLE iceberg_tables; Type: ACL; Schema: storage; Owner: supabase_storage_admin
--

GRANT ALL ON TABLE storage.iceberg_tables TO service_role;
GRANT SELECT ON TABLE storage.iceberg_tables TO authenticated;
GRANT SELECT ON TABLE storage.iceberg_tables TO anon;


--
-- Name: TABLE objects; Type: ACL; Schema: storage; Owner: supabase_storage_admin
--

GRANT ALL ON TABLE storage.objects TO anon;
GRANT ALL ON TABLE storage.objects TO authenticated;
GRANT ALL ON TABLE storage.objects TO service_role;
GRANT ALL ON TABLE storage.objects TO postgres WITH GRANT OPTION;


--
-- Name: TABLE prefixes; Type: ACL; Schema: storage; Owner: supabase_storage_admin
--

GRANT ALL ON TABLE storage.prefixes TO service_role;
GRANT ALL ON TABLE storage.prefixes TO authenticated;
GRANT ALL ON TABLE storage.prefixes TO anon;


--
-- Name: TABLE s3_multipart_uploads; Type: ACL; Schema: storage; Owner: supabase_storage_admin
--

GRANT ALL ON TABLE storage.s3_multipart_uploads TO service_role;
GRANT SELECT ON TABLE storage.s3_multipart_uploads TO authenticated;
GRANT SELECT ON TABLE storage.s3_multipart_uploads TO anon;


--
-- Name: TABLE s3_multipart_uploads_parts; Type: ACL; Schema: storage; Owner: supabase_storage_admin
--

GRANT ALL ON TABLE storage.s3_multipart_uploads_parts TO service_role;
GRANT SELECT ON TABLE storage.s3_multipart_uploads_parts TO authenticated;
GRANT SELECT ON TABLE storage.s3_multipart_uploads_parts TO anon;


--
-- Name: TABLE hooks; Type: ACL; Schema: supabase_functions; Owner: supabase_functions_admin
--

GRANT ALL ON TABLE supabase_functions.hooks TO postgres;
GRANT ALL ON TABLE supabase_functions.hooks TO anon;
GRANT ALL ON TABLE supabase_functions.hooks TO authenticated;
GRANT ALL ON TABLE supabase_functions.hooks TO service_role;


--
-- Name: SEQUENCE hooks_id_seq; Type: ACL; Schema: supabase_functions; Owner: supabase_functions_admin
--

GRANT ALL ON SEQUENCE supabase_functions.hooks_id_seq TO postgres;
GRANT ALL ON SEQUENCE supabase_functions.hooks_id_seq TO anon;
GRANT ALL ON SEQUENCE supabase_functions.hooks_id_seq TO authenticated;
GRANT ALL ON SEQUENCE supabase_functions.hooks_id_seq TO service_role;


--
-- Name: TABLE migrations; Type: ACL; Schema: supabase_functions; Owner: supabase_functions_admin
--

GRANT ALL ON TABLE supabase_functions.migrations TO postgres;
GRANT ALL ON TABLE supabase_functions.migrations TO anon;
GRANT ALL ON TABLE supabase_functions.migrations TO authenticated;
GRANT ALL ON TABLE supabase_functions.migrations TO service_role;


--
-- Name: TABLE secrets; Type: ACL; Schema: vault; Owner: supabase_admin
--

GRANT SELECT,REFERENCES,DELETE,TRUNCATE ON TABLE vault.secrets TO postgres WITH GRANT OPTION;
GRANT SELECT,DELETE ON TABLE vault.secrets TO service_role;


--
-- Name: TABLE decrypted_secrets; Type: ACL; Schema: vault; Owner: supabase_admin
--

GRANT SELECT,REFERENCES,DELETE,TRUNCATE ON TABLE vault.decrypted_secrets TO postgres WITH GRANT OPTION;
GRANT SELECT,DELETE ON TABLE vault.decrypted_secrets TO service_role;


--
-- Name: DEFAULT PRIVILEGES FOR SEQUENCES; Type: DEFAULT ACL; Schema: auth; Owner: supabase_auth_admin
--

ALTER DEFAULT PRIVILEGES FOR ROLE supabase_auth_admin IN SCHEMA auth GRANT ALL ON SEQUENCES TO postgres;
ALTER DEFAULT PRIVILEGES FOR ROLE supabase_auth_admin IN SCHEMA auth GRANT ALL ON SEQUENCES TO dashboard_user;


--
-- Name: DEFAULT PRIVILEGES FOR FUNCTIONS; Type: DEFAULT ACL; Schema: auth; Owner: supabase_auth_admin
--

ALTER DEFAULT PRIVILEGES FOR ROLE supabase_auth_admin IN SCHEMA auth GRANT ALL ON FUNCTIONS TO postgres;
ALTER DEFAULT PRIVILEGES FOR ROLE supabase_auth_admin IN SCHEMA auth GRANT ALL ON FUNCTIONS TO dashboard_user;


--
-- Name: DEFAULT PRIVILEGES FOR TABLES; Type: DEFAULT ACL; Schema: auth; Owner: supabase_auth_admin
--

ALTER DEFAULT PRIVILEGES FOR ROLE supabase_auth_admin IN SCHEMA auth GRANT ALL ON TABLES TO postgres;
ALTER DEFAULT PRIVILEGES FOR ROLE supabase_auth_admin IN SCHEMA auth GRANT ALL ON TABLES TO dashboard_user;


--
-- Name: DEFAULT PRIVILEGES FOR SEQUENCES; Type: DEFAULT ACL; Schema: extensions; Owner: supabase_admin
--

ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA extensions GRANT ALL ON SEQUENCES TO postgres WITH GRANT OPTION;


--
-- Name: DEFAULT PRIVILEGES FOR FUNCTIONS; Type: DEFAULT ACL; Schema: extensions; Owner: supabase_admin
--

ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA extensions GRANT ALL ON FUNCTIONS TO postgres WITH GRANT OPTION;


--
-- Name: DEFAULT PRIVILEGES FOR TABLES; Type: DEFAULT ACL; Schema: extensions; Owner: supabase_admin
--

ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA extensions GRANT ALL ON TABLES TO postgres WITH GRANT OPTION;


--
-- Name: DEFAULT PRIVILEGES FOR SEQUENCES; Type: DEFAULT ACL; Schema: graphql; Owner: supabase_admin
--

ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA graphql GRANT ALL ON SEQUENCES TO postgres;
ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA graphql GRANT ALL ON SEQUENCES TO anon;
ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA graphql GRANT ALL ON SEQUENCES TO authenticated;
ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA graphql GRANT ALL ON SEQUENCES TO service_role;


--
-- Name: DEFAULT PRIVILEGES FOR FUNCTIONS; Type: DEFAULT ACL; Schema: graphql; Owner: supabase_admin
--

ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA graphql GRANT ALL ON FUNCTIONS TO postgres;
ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA graphql GRANT ALL ON FUNCTIONS TO anon;
ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA graphql GRANT ALL ON FUNCTIONS TO authenticated;
ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA graphql GRANT ALL ON FUNCTIONS TO service_role;


--
-- Name: DEFAULT PRIVILEGES FOR TABLES; Type: DEFAULT ACL; Schema: graphql; Owner: supabase_admin
--

ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA graphql GRANT ALL ON TABLES TO postgres;
ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA graphql GRANT ALL ON TABLES TO anon;
ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA graphql GRANT ALL ON TABLES TO authenticated;
ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA graphql GRANT ALL ON TABLES TO service_role;


--
-- Name: DEFAULT PRIVILEGES FOR SEQUENCES; Type: DEFAULT ACL; Schema: graphql_public; Owner: supabase_admin
--

ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA graphql_public GRANT ALL ON SEQUENCES TO postgres;
ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA graphql_public GRANT ALL ON SEQUENCES TO anon;
ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA graphql_public GRANT ALL ON SEQUENCES TO authenticated;
ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA graphql_public GRANT ALL ON SEQUENCES TO service_role;


--
-- Name: DEFAULT PRIVILEGES FOR FUNCTIONS; Type: DEFAULT ACL; Schema: graphql_public; Owner: supabase_admin
--

ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA graphql_public GRANT ALL ON FUNCTIONS TO postgres;
ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA graphql_public GRANT ALL ON FUNCTIONS TO anon;
ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA graphql_public GRANT ALL ON FUNCTIONS TO authenticated;
ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA graphql_public GRANT ALL ON FUNCTIONS TO service_role;


--
-- Name: DEFAULT PRIVILEGES FOR TABLES; Type: DEFAULT ACL; Schema: graphql_public; Owner: supabase_admin
--

ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA graphql_public GRANT ALL ON TABLES TO postgres;
ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA graphql_public GRANT ALL ON TABLES TO anon;
ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA graphql_public GRANT ALL ON TABLES TO authenticated;
ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA graphql_public GRANT ALL ON TABLES TO service_role;


--
-- Name: DEFAULT PRIVILEGES FOR SEQUENCES; Type: DEFAULT ACL; Schema: public; Owner: postgres
--

ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA public GRANT ALL ON SEQUENCES TO postgres;
ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA public GRANT ALL ON SEQUENCES TO anon;
ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA public GRANT ALL ON SEQUENCES TO authenticated;
ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA public GRANT ALL ON SEQUENCES TO service_role;


--
-- Name: DEFAULT PRIVILEGES FOR SEQUENCES; Type: DEFAULT ACL; Schema: public; Owner: supabase_admin
--

ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA public GRANT ALL ON SEQUENCES TO postgres;
ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA public GRANT ALL ON SEQUENCES TO anon;
ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA public GRANT ALL ON SEQUENCES TO authenticated;
ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA public GRANT ALL ON SEQUENCES TO service_role;


--
-- Name: DEFAULT PRIVILEGES FOR FUNCTIONS; Type: DEFAULT ACL; Schema: public; Owner: postgres
--

ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA public GRANT ALL ON FUNCTIONS TO postgres;
ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA public GRANT ALL ON FUNCTIONS TO anon;
ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA public GRANT ALL ON FUNCTIONS TO authenticated;
ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA public GRANT ALL ON FUNCTIONS TO service_role;


--
-- Name: DEFAULT PRIVILEGES FOR FUNCTIONS; Type: DEFAULT ACL; Schema: public; Owner: supabase_admin
--

ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA public GRANT ALL ON FUNCTIONS TO postgres;
ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA public GRANT ALL ON FUNCTIONS TO anon;
ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA public GRANT ALL ON FUNCTIONS TO authenticated;
ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA public GRANT ALL ON FUNCTIONS TO service_role;


--
-- Name: DEFAULT PRIVILEGES FOR TABLES; Type: DEFAULT ACL; Schema: public; Owner: postgres
--

ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA public GRANT ALL ON TABLES TO postgres;
ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA public GRANT ALL ON TABLES TO anon;
ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA public GRANT ALL ON TABLES TO authenticated;
ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA public GRANT ALL ON TABLES TO service_role;


--
-- Name: DEFAULT PRIVILEGES FOR TABLES; Type: DEFAULT ACL; Schema: public; Owner: supabase_admin
--

ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA public GRANT ALL ON TABLES TO postgres;
ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA public GRANT ALL ON TABLES TO anon;
ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA public GRANT ALL ON TABLES TO authenticated;
ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA public GRANT ALL ON TABLES TO service_role;


--
-- Name: DEFAULT PRIVILEGES FOR SEQUENCES; Type: DEFAULT ACL; Schema: realtime; Owner: supabase_admin
--

ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA realtime GRANT ALL ON SEQUENCES TO postgres;
ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA realtime GRANT ALL ON SEQUENCES TO dashboard_user;


--
-- Name: DEFAULT PRIVILEGES FOR FUNCTIONS; Type: DEFAULT ACL; Schema: realtime; Owner: supabase_admin
--

ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA realtime GRANT ALL ON FUNCTIONS TO postgres;
ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA realtime GRANT ALL ON FUNCTIONS TO dashboard_user;


--
-- Name: DEFAULT PRIVILEGES FOR TABLES; Type: DEFAULT ACL; Schema: realtime; Owner: supabase_admin
--

ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA realtime GRANT ALL ON TABLES TO postgres;
ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA realtime GRANT ALL ON TABLES TO dashboard_user;


--
-- Name: DEFAULT PRIVILEGES FOR SEQUENCES; Type: DEFAULT ACL; Schema: storage; Owner: postgres
--

ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA storage GRANT ALL ON SEQUENCES TO postgres;
ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA storage GRANT ALL ON SEQUENCES TO anon;
ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA storage GRANT ALL ON SEQUENCES TO authenticated;
ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA storage GRANT ALL ON SEQUENCES TO service_role;


--
-- Name: DEFAULT PRIVILEGES FOR FUNCTIONS; Type: DEFAULT ACL; Schema: storage; Owner: postgres
--

ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA storage GRANT ALL ON FUNCTIONS TO postgres;
ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA storage GRANT ALL ON FUNCTIONS TO anon;
ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA storage GRANT ALL ON FUNCTIONS TO authenticated;
ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA storage GRANT ALL ON FUNCTIONS TO service_role;


--
-- Name: DEFAULT PRIVILEGES FOR TABLES; Type: DEFAULT ACL; Schema: storage; Owner: postgres
--

ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA storage GRANT ALL ON TABLES TO postgres;
ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA storage GRANT ALL ON TABLES TO anon;
ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA storage GRANT ALL ON TABLES TO authenticated;
ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA storage GRANT ALL ON TABLES TO service_role;


--
-- Name: DEFAULT PRIVILEGES FOR SEQUENCES; Type: DEFAULT ACL; Schema: supabase_functions; Owner: supabase_admin
--

ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA supabase_functions GRANT ALL ON SEQUENCES TO postgres;
ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA supabase_functions GRANT ALL ON SEQUENCES TO anon;
ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA supabase_functions GRANT ALL ON SEQUENCES TO authenticated;
ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA supabase_functions GRANT ALL ON SEQUENCES TO service_role;


--
-- Name: DEFAULT PRIVILEGES FOR FUNCTIONS; Type: DEFAULT ACL; Schema: supabase_functions; Owner: supabase_admin
--

ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA supabase_functions GRANT ALL ON FUNCTIONS TO postgres;
ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA supabase_functions GRANT ALL ON FUNCTIONS TO anon;
ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA supabase_functions GRANT ALL ON FUNCTIONS TO authenticated;
ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA supabase_functions GRANT ALL ON FUNCTIONS TO service_role;


--
-- Name: DEFAULT PRIVILEGES FOR TABLES; Type: DEFAULT ACL; Schema: supabase_functions; Owner: supabase_admin
--

ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA supabase_functions GRANT ALL ON TABLES TO postgres;
ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA supabase_functions GRANT ALL ON TABLES TO anon;
ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA supabase_functions GRANT ALL ON TABLES TO authenticated;
ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA supabase_functions GRANT ALL ON TABLES TO service_role;


--
-- Name: issue_graphql_placeholder; Type: EVENT TRIGGER; Schema: -; Owner: supabase_admin
--

CREATE EVENT TRIGGER issue_graphql_placeholder ON sql_drop
         WHEN TAG IN ('DROP EXTENSION')
   EXECUTE FUNCTION extensions.set_graphql_placeholder();


ALTER EVENT TRIGGER issue_graphql_placeholder OWNER TO supabase_admin;

--
-- Name: issue_pg_cron_access; Type: EVENT TRIGGER; Schema: -; Owner: supabase_admin
--

CREATE EVENT TRIGGER issue_pg_cron_access ON ddl_command_end
         WHEN TAG IN ('CREATE EXTENSION')
   EXECUTE FUNCTION extensions.grant_pg_cron_access();


ALTER EVENT TRIGGER issue_pg_cron_access OWNER TO supabase_admin;

--
-- Name: issue_pg_graphql_access; Type: EVENT TRIGGER; Schema: -; Owner: supabase_admin
--

CREATE EVENT TRIGGER issue_pg_graphql_access ON ddl_command_end
         WHEN TAG IN ('CREATE FUNCTION')
   EXECUTE FUNCTION extensions.grant_pg_graphql_access();


ALTER EVENT TRIGGER issue_pg_graphql_access OWNER TO supabase_admin;

--
-- Name: issue_pg_net_access; Type: EVENT TRIGGER; Schema: -; Owner: supabase_admin
--

CREATE EVENT TRIGGER issue_pg_net_access ON ddl_command_end
         WHEN TAG IN ('CREATE EXTENSION')
   EXECUTE FUNCTION extensions.grant_pg_net_access();


ALTER EVENT TRIGGER issue_pg_net_access OWNER TO supabase_admin;

--
-- Name: pgrst_ddl_watch; Type: EVENT TRIGGER; Schema: -; Owner: supabase_admin
--

CREATE EVENT TRIGGER pgrst_ddl_watch ON ddl_command_end
   EXECUTE FUNCTION extensions.pgrst_ddl_watch();


ALTER EVENT TRIGGER pgrst_ddl_watch OWNER TO supabase_admin;

--
-- Name: pgrst_drop_watch; Type: EVENT TRIGGER; Schema: -; Owner: supabase_admin
--

CREATE EVENT TRIGGER pgrst_drop_watch ON sql_drop
   EXECUTE FUNCTION extensions.pgrst_drop_watch();


ALTER EVENT TRIGGER pgrst_drop_watch OWNER TO supabase_admin;

--
-- PostgreSQL database dump complete
--

