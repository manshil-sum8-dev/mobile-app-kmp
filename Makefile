SHELL := /bin/bash

SUPABASE_DIR := supabase/quantive-backend
SUPABASE_CLI := supabase
CONFIG := $(SUPABASE_DIR)/supabase/config.toml

.PHONY: help supa-start supa-stop supa-status supa-reset supa-seed supa-migrate supa-diff android-build ios-framework clean

help:
	@echo "Targets:"
	@echo "  supa-start    Start local Supabase (project_id from $(CONFIG))"
	@echo "  supa-stop     Stop local Supabase"
	@echo "  supa-status   Show running containers"
	@echo "  supa-reset    Reset db (drops, migrates, seeds)"
	@echo "  supa-seed     Apply seed.sql"
	@echo "  supa-migrate  Run migrations from supabase/quantive-backend/migrations"
	@echo "  supa-diff     Create migration diff (name=MSG)"
	@echo "  android-build Build Android debug APK"
	@echo "  ios-framework Build iOS simulator framework"

supa-start:
	cd $(SUPABASE_DIR) && $(SUPABASE_CLI) start

supa-stop:
	cd $(SUPABASE_DIR) && $(SUPABASE_CLI) stop

supa-status:
	cd $(SUPABASE_DIR) && $(SUPABASE_CLI) status

supa-reset:
	cd $(SUPABASE_DIR) && $(SUPABASE_CLI) db reset --force

supa-seed:
	cd $(SUPABASE_DIR) && $(SUPABASE_CLI) db reset --use-mig-dir --db-url "postgresql://postgres:postgres@localhost:54322/postgres"

supa-migrate:
	cd $(SUPABASE_DIR) && $(SUPABASE_CLI) db push

supa-diff:
	@if [ -z "$(name)" ]; then echo "Usage: make supa-diff name=your_message"; exit 1; fi
	cd $(SUPABASE_DIR) && $(SUPABASE_CLI) db diff --linked --use-mig-dir --name "$(name)"

android-build:
	./gradlew :composeApp:assembleDebug

android-install:
	./gradlew :composeApp:installDebug

ios-framework:
	./gradlew :composeApp:linkDebugFrameworkIosSimulatorArm64

clean:
	./gradlew clean
