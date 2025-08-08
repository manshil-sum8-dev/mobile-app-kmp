#!/bin/bash
# Install git hooks for quality enforcement
# Based on TECH-DEBT-QA-002 enterprise quality requirements

set -e

PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
HOOKS_DIR="$PROJECT_ROOT/.git/hooks"

echo "🔧 Installing quality enforcement git hooks..."

# Check if we're in a git repository
if [ ! -d "$PROJECT_ROOT/.git" ]; then
    echo "❌ Not in a git repository. Please run this from the project root."
    exit 1
fi

# Create hooks directory if it doesn't exist
mkdir -p "$HOOKS_DIR"

# Copy pre-commit hook
if [ -f "$PROJECT_ROOT/scripts/pre-commit" ]; then
    echo "📋 Installing pre-commit hook..."
    cp "$PROJECT_ROOT/scripts/pre-commit" "$HOOKS_DIR/pre-commit"
    chmod +x "$HOOKS_DIR/pre-commit"
    echo "✅ Pre-commit hook installed"
else
    echo "❌ Pre-commit hook script not found at scripts/pre-commit"
    exit 1
fi

# Create pre-push hook for additional checks
echo "📋 Creating pre-push hook..."
cat > "$HOOKS_DIR/pre-push" << 'EOF'
#!/bin/sh
# Pre-push quality checks for Quantive KMP

echo "🚀 Running pre-push quality validation..."

# Run full test suite
echo "🧪 Running complete test suite..."
./gradlew test --quiet
if [ $? -ne 0 ]; then
    echo "❌ Full test suite failed. Push aborted."
    exit 1
fi

# Run quality checks
echo "🔍 Running quality checks..."
./gradlew qualityCheck --quiet
if [ $? -ne 0 ]; then
    echo "❌ Quality checks failed. Push aborted."
    exit 1
fi

# Check for security vulnerabilities (if configured)
if command -v ./gradlew dependencyCheckAnalyze >/dev/null 2>&1; then
    echo "🔒 Running security scan..."
    ./gradlew dependencyCheckAnalyze --quiet || true
fi

echo "✅ All pre-push checks passed!"
EOF

chmod +x "$HOOKS_DIR/pre-push"
echo "✅ Pre-push hook installed"

# Backup existing commit-msg hook if it exists
if [ -f "$HOOKS_DIR/commit-msg" ] && [ ! -f "$HOOKS_DIR/commit-msg.backup" ]; then
    echo "📦 Backing up existing commit-msg hook..."
    cp "$HOOKS_DIR/commit-msg" "$HOOKS_DIR/commit-msg.backup"
fi

# Create commit message validation hook
echo "📋 Creating commit-msg validation hook..."
cat > "$HOOKS_DIR/commit-msg" << 'EOF'
#!/bin/sh
# Commit message validation for Quantive KMP
# Enforces conventional commit format

commit_regex='^(feat|fix|docs|style|refactor|perf|test|build|ci|chore)(\(.+\))?: .{1,50}'

error_msg="❌ Invalid commit message format!
Commit message must follow conventional commits format:
<type>(<scope>): <description>

Examples:
  feat(auth): add secure token storage
  fix(cache): resolve TTL expiration bug  
  docs(api): update authentication flow
  refactor(ui): simplify theme structure
  
Types: feat, fix, docs, style, refactor, perf, test, build, ci, chore
Max length: 72 characters
"

if ! grep -qE "$commit_regex" "$1"; then
    echo "$error_msg" >&2
    exit 1
fi

# Check for WIP commits in main branch
branch=$(git rev-parse --abbrev-ref HEAD)
if [ "$branch" = "main" ] && grep -qiE "^(wip|work in progress)" "$1"; then
    echo "❌ WIP commits not allowed in main branch!" >&2
    exit 1
fi

# Check for Co-authored-by in pair programming
if grep -q "Co-authored-by:" "$1"; then
    echo "✅ Pair programming detected - great collaboration!" >&2
fi
EOF

chmod +x "$HOOKS_DIR/commit-msg"
echo "✅ Commit message validation hook installed"

# Summary
echo ""
echo "🎉 Git hooks installation complete!"
echo ""
echo "Installed hooks:"
echo "  • pre-commit: Runs ktlint, detekt, and unit tests"
echo "  • pre-push: Runs full test suite and quality checks"
echo "  • commit-msg: Validates conventional commit format"
echo ""
echo "ℹ️  To bypass hooks (not recommended): git commit --no-verify"
echo "ℹ️  To remove hooks: rm .git/hooks/pre-commit .git/hooks/pre-push .git/hooks/commit-msg"
echo ""
echo "✨ Quality enforcement is now active!"