# Quantive Onboarding Flow - UX Design Specification

## Overview

This document outlines the comprehensive onboarding experience for Quantive, a professional business management and invoicing platform designed for South African businesses. The onboarding flow introduces users to the platform's capabilities while collecting essential business information to personalize their experience.

## Design Philosophy

### Core Principles
- **Professional yet approachable**: Business users need confidence in a professional tool while feeling welcome
- **Value-first communication**: Lead with benefits before asking for information
- **Progressive disclosure**: Introduce complexity gradually 
- **Platform consistency**: Maintain Material 3 Expressive design language
- **Accessibility-first**: Support screen readers, keyboard navigation, and various abilities

### Visual Language
- **Material 3 Expressive**: Spring animations, morphing shapes, expressive colors
- **Teal Primary Palette**: Professional #00695C with growth green accents
- **South African Context**: Localized content, VAT compliance, Rand currency
- **Emotional Resonance**: Colors that convey trust, growth, and professionalism

## User Journey Map

### Target User Profile
- **Primary**: Small-to-medium business owners in South Africa
- **Business types**: Freelancers, consultants, service providers, small retailers
- **Tech comfort**: Medium (familiar with smartphones, basic business software)
- **Pain points**: Manual invoicing, tax compliance, payment tracking, customer management

### Journey Stages
1. **Awareness** → First app launch after account creation
2. **Orientation** → Understanding Quantive's value proposition  
3. **Setup** → Configuring business profile and preferences
4. **Activation** → First successful interaction with core features
5. **Retention** → Ongoing engagement with the platform

## Detailed Screen Specifications

### 1. Welcome Screen - Brand Introduction

#### Purpose
Establish trust, communicate value proposition, and create emotional connection with the brand.

#### Visual Design
```
┌─────────────────────────────────────┐
│  Progress: ●○○○○○ (1 of 6)          │
├─────────────────────────────────────┤
│                                     │
│            ┌───────┐                │
│            │   Q   │ <- Brand Logo  │
│            │       │    (120dp)     │
│            └───────┘                │
│                                     │
│         Welcome to Quantive         │
│                                     │
│  Professional invoicing and         │
│    business management              │
│                                     │
│    Built for South African          │
│          businesses                 │
│                                     │
│  ┌─────┐ ┌─────┐ ┌─────┐            │
│  │Prof │ │Secure│ │Smart│            │
│  │VAT  │ │Bank │ │AI   │            │
│  │comp │ │grade│ │Insig│            │
│  └─────┘ └─────┘ └─────┘            │
│                                     │
│  ┌─────────────────────────────────┐ │
│  │       Get Started    →          │ │
│  └─────────────────────────────────┘ │
└─────────────────────────────────────┘
```

#### Content Strategy
- **Headline**: "Welcome to Quantive" (Display Small, Bold)
- **Subheadline**: "Professional invoicing and business management" (Headline Small)
- **Localization**: "Built for South African businesses" (Body Large, Primary color)
- **Value Props**: Professional/VAT compliant, Secure/Bank-grade, Smart/AI insights

#### Animations
- **Entry**: Logo scales in with bounce (SpringBouncy)
- **Content**: Text fades in sequentially with 200ms stagger
- **Value cards**: Slide up with spring animation
- **Button**: Scale on press with ExpressiveEaseOut

#### Technical Implementation
```kotlin
LaunchedEffect(Unit) {
    delay(300)
    showContent = true
}

AnimatedVisibility(
    visible = showContent,
    enter = fadeIn(tween(ExpressiveLong)) + 
           slideInHorizontally(initialOffsetX = { it / 3 })
)
```

### 2. Feature Highlights Screen - Core Capabilities

#### Purpose
Educate users about Quantive's key features and create excitement about capabilities.

#### Visual Design
```
┌─────────────────────────────────────┐
│  Progress: ●●○○○○ (2 of 6)          │
├─────────────────────────────────────┤
│        Powerful Features            │
│   Everything you need to manage     │
│        your business               │
│                                     │
│  ← ┌─────────────────────────────┐ →│
│    │         📄                  │  │
│    │    Smart Invoicing          │  │
│    │                             │  │
│    │  Create professional        │  │
│    │  invoices with automatic    │  │
│    │  VAT calculations and       │  │
│    │  compliance checking        │  │
│    └─────────────────────────────┘  │
│                                     │
│      ○ ● ○ ○   <- Page indicators   │
│                                     │
│  ┌─────────┐ ┌─────────────────────┐│
│  │  Back   │ │    Continue    →    ││
│  └─────────┘ └─────────────────────┘│
└─────────────────────────────────────┘
```

#### Features Showcase
1. **Smart Invoicing** 
   - Icon: Receipt/Document
   - Color: Expressive Primary
   - Value: "Professional invoices with automatic VAT calculations"

2. **Contact Management**
   - Icon: Group/People
   - Color: Growth Green
   - Value: "Complete business relationship tracking"

3. **Business Analytics**
   - Icon: Trending Up/Chart
   - Color: Trust Blue
   - Value: "Cash flow insights and performance metrics"

4. **Tax Compliance** 
   - Icon: Business/Government
   - Color: Warning Orange
   - Value: "SARS-compliant reporting for SA businesses"

#### Interaction Design
- **Horizontal Pager**: Swipe between feature cards
- **Page Indicators**: Animated dots showing current position
- **Card Animation**: Scale and color transition on focus
- **Auto-progression**: Optional 5-second auto-advance

### 3. Permissions Screen - Trust Building

#### Purpose
Request necessary permissions with clear value explanations to build user trust.

#### Visual Design
```
┌─────────────────────────────────────┐
│  Progress: ●●●○○○ (3 of 6)          │
├─────────────────────────────────────┤
│          App Permissions            │
│    Help us provide the best         │
│         experience                  │
│                                     │
│  ┌─────────────────────────────────┐ │
│  │  📱 Notifications              │ │
│  │                                │ │
│  │  Get notified when invoices    │ │
│  │  are viewed or paid            │ │
│  │                                │ │
│  │        [Allow] [Later]         │ │
│  └─────────────────────────────────┘ │
│                                     │
│  ┌─────────────────────────────────┐ │
│  │  📸 Camera Access              │ │
│  │                                │ │
│  │  Scan business documents and   │ │
│  │  add photos to invoices        │ │
│  │                                │ │
│  │        [Allow] [Later]         │ │
│  └─────────────────────────────────┘ │
│                                     │
│  ┌─────────┐ ┌─────────────────────┐│
│  │  Back   │ │    Continue    →    ││
│  └─────────┘ └─────────────────────┘│
└─────────────────────────────────────┘
```

#### Permission Requests
1. **Notifications**
   - **Why**: Payment notifications, invoice status updates
   - **Value**: "Never miss a payment or important update"
   - **Fallback**: Email notifications available

2. **Camera Access**
   - **Why**: Document scanning, receipt capture, logo upload
   - **Value**: "Quick document capture and professional branding" 
   - **Fallback**: File picker alternative

3. **Storage Access** (if needed)
   - **Why**: Export invoices, save documents locally
   - **Value**: "Easy document management and backup"

#### Trust Elements
- **Privacy Badge**: "Your data stays secure and private"
- **Granular Control**: Individual permission toggles
- **Skip Option**: "Set up later" for optional permissions
- **Explanation Cards**: Clear benefit communication

### 4. Business Profile Setup - Core Information

#### Purpose
Collect essential business information to personalize the experience and enable core functionality.

#### Visual Design
```
┌─────────────────────────────────────┐
│  Progress: ●●●●○○ (4 of 6)          │
├─────────────────────────────────────┤
│      Set Up Your Business          │
│   Tell us about your business      │
│                                     │
│  ┌─────────────────────────────────┐ │
│  │  Business Name *               │ │
│  │  [___________________]         │ │
│  └─────────────────────────────────┘ │
│                                     │
│  ┌─────────────────────────────────┐ │
│  │  Business Type                 │ │
│  │  [ Freelancer ▼ ]             │ │
│  └─────────────────────────────────┘ │
│                                     │
│  ┌─────────────────────────────────┐ │
│  │  VAT Registration              │ │
│  │  [___________________]         │ │
│  │  Optional - for compliance     │ │
│  └─────────────────────────────────┘ │
│                                     │
│  ┌─────────────────────────────────┐ │
│  │  Preferred Currency            │ │
│  │  [ ZAR (South African Rand) ] │ │
│  └─────────────────────────────────┘ │
│                                     │
│  ┌─────────┐ ┌─────────────────────┐│
│  │  Back   │ │    Continue    →    ││
│  └─────────┘ └─────────────────────┘│
└─────────────────────────────────────┘
```

#### Required Fields
1. **Business Name** (Required)
   - Auto-suggestions based on email domain
   - Validation: 2-150 characters
   - Error states with helpful messaging

2. **Business Type** (Required)
   - Dropdown: Freelancer, Consultant, Small Business, Retailer, Other
   - Affects default settings and feature visibility
   - Used for onboarding customization

#### Optional Fields
1. **VAT Registration Number**
   - South African VAT format validation
   - Auto-enables VAT features when provided
   - "Why do we need this?" explanation

2. **Business Address**
   - Auto-complete with South African addresses
   - Used for invoice headers and compliance
   - Province selection for local tax rates

3. **Currency Preference**
   - Default: ZAR (South African Rand)  
   - Supports multi-currency for international clients
   - Affects display formatting throughout app

#### Smart Features
- **Email Domain Detection**: Suggest business name from email
- **Address Autocomplete**: South African postal codes
- **Field Dependencies**: VAT number enables compliance features
- **Save Progress**: Auto-save as user types

### 5. Personalization Screen - Feature Preferences

#### Purpose
Customize the user experience based on business needs and preferences.

#### Visual Design
```
┌─────────────────────────────────────┐
│  Progress: ●●●●●○ (5 of 6)          │
├─────────────────────────────────────┤
│       Customize Your Setup         │
│   Configure features for your      │
│        business needs              │
│                                     │
│  ┌─────────────────────────────────┐ │
│  │   📊 Invoice Preferences        │ │
│  │   ○ Simple invoices             │ │
│  │   ● Detailed with line items    │ │
│  │   ○ Service-based templates     │ │
│  └─────────────────────────────────┘ │
│                                     │
│  ┌─────────────────────────────────┐ │
│  │   🔔 Notification Settings      │ │
│  │   [●] Payment notifications     │ │
│  │   [●] Invoice status updates    │ │
│  │   [○] Weekly summaries          │ │
│  └─────────────────────────────────┘ │
│                                     │
│  ┌─────────────────────────────────┐ │
│  │   🎨 Branding Options           │ │
│  │   [ Add Logo ] [ Color Theme ]  │ │
│  └─────────────────────────────────┘ │
│                                     │
│  ┌─────────┐ ┌─────────────────────┐│
│  │  Back   │ │    Continue    →    ││
│  └─────────┘ └─────────────────────┘│
└─────────────────────────────────────┘
```

#### Customization Options

##### Invoice Preferences
- **Template Style**: Simple, Detailed, Service-based
- **Default Terms**: Net 30, Due on Receipt, Custom
- **Auto-numbering**: Pattern preferences
- **Tax Display**: Inclusive vs Exclusive pricing

##### Notification Preferences  
- **Payment Alerts**: Immediate, Daily digest, Weekly
- **Status Updates**: Real-time, Batched
- **Reminders**: Overdue notices, Follow-ups
- **Marketing**: Feature updates, Tips (opt-in)

##### Branding Setup
- **Logo Upload**: Camera, Gallery, or Skip
- **Brand Colors**: Theme customization
- **Business Signature**: Default email signature
- **Invoice Footer**: Terms, contact info

#### Intelligent Defaults
- **Business Type Mapping**: Defaults based on selected business type
- **Industry Best Practices**: Pre-selected optimal settings
- **Regional Settings**: South African compliance defaults
- **Progressive Enhancement**: Advanced features introduced over time

### 6. Completion Screen - Success State

#### Purpose
Celebrate successful onboarding completion and guide users toward their first action.

#### Visual Design  
```
┌─────────────────────────────────────┐
│  Progress: ●●●●●● (6 of 6)          │
├─────────────────────────────────────┤
│                                     │
│              ✅                     │
│         (Success Icon)              │
│                                     │
│      Welcome to Quantive!          │
│                                     │
│   Your professional invoicing      │
│    workspace is ready              │
│                                     │
│  ┌─────────────────────────────────┐ │
│  │     🚀 Quick Start Guide        │ │
│  │     • Create your first invoice │ │  
│  │     • Add a customer            │ │
│  │     • Set up payment methods    │ │
│  └─────────────────────────────────┘ │
│                                     │
│  ┌─────────────────────────────────┐ │
│  │    Start Using Quantive         │ │
│  └─────────────────────────────────┘ │
│                                     │
│     [ Skip Tour ]  [ Start Tour ]   │
│                                     │
└─────────────────────────────────────┘
```

#### Success Elements
- **Achievement Icon**: Large checkmark with growth color
- **Celebration**: Subtle confetti animation (optional)
- **Personalization**: Include business name in welcome message
- **Quick Wins**: Preview of immediate next actions

#### Next Steps Options
1. **Immediate Action**: "Create First Invoice" (Primary CTA)
2. **Guided Tour**: Interactive feature walkthrough 
3. **Dashboard**: Skip to main app interface
4. **Help Resources**: Link to support materials

#### Onboarding Analytics
- **Completion Tracking**: Which steps are completed/skipped
- **Drop-off Points**: Where users abandon onboarding
- **Time-to-Value**: Duration from signup to first invoice
- **Feature Adoption**: Which preferences are selected

## Technical Implementation

### State Management
```kotlin
data class OnboardingState(
    val currentStep: Int = 0,
    val isLoading: Boolean = false,
    val businessProfile: BusinessProfile? = null,
    val permissions: PermissionState = PermissionState(),
    val preferences: UserPreferences = UserPreferences(),
    val progress: Float = 0f
)
```

### Navigation Flow
```kotlin
sealed class OnboardingStep {
    object Welcome : OnboardingStep()
    object Features : OnboardingStep() 
    object Permissions : OnboardingStep()
    object BusinessSetup : OnboardingStep()
    object Personalization : OnboardingStep()
    object Completion : OnboardingStep()
}
```

### Animation Specifications
- **Page Transitions**: Slide with ExpressiveMedium timing (400ms)
- **Element Entrances**: Staggered fade-in with 200ms delays
- **Interactive Feedback**: Spring-based scale animations
- **Progress Indicator**: Smooth progress bar transitions

### Accessibility Features
- **Screen Reader Support**: Comprehensive content descriptions
- **Keyboard Navigation**: Full keyboard accessibility
- **Focus Management**: Proper focus order and visible indicators  
- **High Contrast**: Accessible color combinations
- **Font Scaling**: Support for large text sizes
- **Motion Reduction**: Respect system motion preferences

### Performance Considerations
- **Lazy Loading**: Load screens as needed
- **Image Optimization**: Compressed assets with multiple densities
- **Animation Performance**: 60fps target with GPU acceleration
- **Memory Management**: Clean up resources between steps
- **Network Efficiency**: Batch API calls where possible

## Success Metrics

### Primary KPIs
- **Onboarding Completion Rate**: Target >80%
- **Time to First Value**: First invoice created <5 minutes post-onboarding  
- **User Activation**: Successful first transaction within 24 hours
- **Retention**: 7-day active user retention >70%

### Secondary Metrics  
- **Feature Discovery**: Which features are used first
- **Setup Completeness**: Percentage of optional fields completed
- **Support Requests**: Reduction in onboarding-related tickets
- **User Satisfaction**: Post-onboarding NPS score

### A/B Testing Opportunities
- **Welcome Message**: Value prop variations
- **Feature Order**: Different feature highlight sequences
- **CTA Language**: Button text variations
- **Visual Style**: Illustration vs photography
- **Skip Options**: Required vs optional field variations

## Future Enhancements

### Phase 2 Features
- **Personalized Recommendations**: AI-driven setup suggestions
- **Industry Templates**: Vertical-specific configurations  
- **Multi-language Support**: Afrikaans, Zulu, Xhosa options
- **Video Tutorials**: Embedded help content
- **Social Proof**: Customer testimonials and success stories

### Advanced Personalization
- **Business Intelligence**: Setup based on business size/industry
- **Integration Suggestions**: Recommend relevant third-party tools
- **Advanced Compliance**: Region-specific tax configurations
- **Team Onboarding**: Multi-user business setup flows

This comprehensive onboarding design balances user education with efficient setup, creating a professional yet welcoming first experience that builds trust and drives user activation in the Quantive platform.