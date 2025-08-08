SHELL := /bin/bash

SUPABASE_DIR := supabase/quantive-backend
SUPABASE_CLI := supabase
CONFIG := $(SUPABASE_DIR)/supabase/config.toml

.PHONY: help supa-start supa-stop supa-status supa-reset supa-seed supa-migrate supa-diff android-build ios-framework clean scaffold-feature quality-check quality-fix ktlint-check ktlint-format detekt install-hooks pre-push

help:
	@echo "Targets:"
	@echo ""
	@echo "Backend:"
	@echo "  supa-start    Start local Supabase (project_id from $(CONFIG))"
	@echo "  supa-stop     Stop local Supabase"
	@echo "  supa-status   Show running containers"
	@echo "  supa-reset    Reset db (drops, migrates, seeds)"
	@echo "  supa-seed     Apply seed.sql"
	@echo "  supa-migrate  Run migrations from supabase/quantive-backend/migrations"
	@echo "  supa-diff     Create migration diff (name=MSG)"
	@echo ""
	@echo "Build:"
	@echo "  android-build Build Android debug APK"
	@echo "  ios-framework Build iOS simulator framework"
	@echo "  clean         Clean build artifacts"
	@echo ""
	@echo "Development:"
	@echo "  scaffold-feature Interactive feature scaffolding wizard"
	@echo ""
	@echo "Quality Gates:"
	@echo "  quality-check Run all quality checks (ktlint + detekt)"
	@echo "  quality-fix   Auto-fix formatting issues"
	@echo "  ktlint-check  Run code formatting check"
	@echo "  ktlint-format Auto-format code with ktlint"
	@echo "  detekt        Run static analysis"
	@echo "  install-hooks Install git quality hooks"
	@echo "  pre-push      Run all quality checks + tests (pre-push validation)"

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

scaffold-feature:
	./gradlew scaffoldFeature --no-daemon

# Quality Gates - Enterprise Standards Enforcement
quality-check:
	@echo "🔍 Running all quality checks..."
	./gradlew qualityCheck

quality-fix:
	@echo "🔧 Auto-fixing quality issues..."
	./gradlew qualityFix

ktlint-check:
	@echo "📝 Checking code formatting..."
	./gradlew ktlintCheck

ktlint-format:
	@echo "✨ Formatting code..."
	./gradlew ktlintFormat

detekt:
	@echo "🔍 Running static analysis..."
	./gradlew detekt

detekt-baseline:
	@echo "📋 Creating detekt baseline..."
	./gradlew detektBaseline

install-hooks:
	@echo "🔧 Installing git quality hooks..."
	chmod +x scripts/install-hooks.sh
	./scripts/install-hooks.sh

# Combined quality and test check for pre-push validation
pre-push: quality-check test
	@echo "✅ Ready for push - all quality gates passed!"

test:
	@echo "🧪 Running unit tests..."
	./gradlew :composeApp:testDebugUnitTest
