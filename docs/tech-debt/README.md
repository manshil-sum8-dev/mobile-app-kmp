# Tech Debt Management System

This directory contains structured tech debt tracking for the Quantive KMP application based on enterprise blueprint compliance assessment.

## Structure

- `critical/` - Critical priority items (production blockers)
- `high/` - High priority items (major feature gaps)  
- `medium/` - Medium priority items (enhancements)
- `completed/` - Archive of resolved tech debt items

## Tech Debt Item Format

Each tech debt item is tracked as a markdown file with:
- **ID**: Unique identifier (TECH-DEBT-{DOMAIN}-{NUMBER})
- **Title**: Descriptive name
- **Severity**: Critical/High/Medium/Low
- **Domain**: Security/Architecture/Performance/QA/Backend
- **Effort Estimate**: Time to complete
- **Assigned Agent**: Specialized agent responsible
- **Status**: Open/In Progress/Under Review/Completed
- **Acceptance Criteria**: Specific requirements for completion
- **Related Files**: Files that need modification
- **Dependencies**: Other tech debt items that must be completed first

## Usage

1. Review tech debt items by priority directory
2. Assign items to specialized agents
3. Track progress by updating status in individual files
4. Move completed items to `completed/` directory
5. Regular reviews during sprint planning

## Current Stats

- **Critical**: 5 items (production blockers)
- **High**: 6 items (major gaps)
- **Medium**: 4 items (enhancements)
- **Low**: 1 item (cosmetic fixes)
- **Total**: 16 items

See individual directories for detailed tracking.