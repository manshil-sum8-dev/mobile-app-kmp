### App Startup & Feature Onboarding

- **App Launch:** Upon first opening the app, a splash screen with the Quantive image is displayed.
- **Initial Onboarding:** Before login/signup, the user is presented with a 3-4 screen carousel.
    - **Purpose:** To showcase Quantive's core value propositions (e.g., "Invoice in Seconds," "Track Your Cashflow," "Get Paid Faster").
    - **Design:** Each screen will feature high-quality animations, bold typography, and concise text. The flow will be visually engaging to capture user interest immediately.
    - **Action:** A clear "Get Started" button on the final screen directs the user to the Authentication flow.

### 3.2. Authentication & Profile Onboarding

- **Registration/Login:** Secure user creation and login using Supabase Auth.

- **User Type Selection:** After signup, the user must select their type. For this phase, the focus is on the **Individual** flow.
    - "I am an Individual / Freelancer"
    - "I am a Company" (future scope)

- **Multi-Step Profile Onboarding:** A guided process with a progress indicator (e.g., "Step 1 of 2").

    - **Phase 1: Business Info**
        - **Fields:**
            - Business Name (Required)
            - Contact Number (Required)
            - Email Address (Required)
            - Company Registration Number (Optional)
            - VAT Registration Number (Optional)
            - Industry (use display_name from the business categories table)
        - **Features:** An option to upload a business logo from the device's gallery or camera. The logo will be uploaded to Supabase Storage.

    - **Phase 2: Location Info**
        - **Fields:** Street Address, City, Province, Postal Code, Country.
        - **Localization:** The country selection list will be pre-populated and automatically scroll to/place the user's detected country at the top of the list.
        - **Currency Selection:** Based on the selected country, automatically suggest the default currency (e.g., ZAR for South Africa) with an option to change it manually.
        - **Phone Number Validation:** Implement country-specific phone number validation and formatting using with automatic country code detection.

Pre-requisites:
* generate a backend table for countries and provinces (Only do this for south africa for now) -> this will be used in Phase 2 above
* province will be a pre-populated drop-down
* the Quantive image is located here: files/quantive_splash.jpeg
* make sure this feature follows the blueprint and project rules
* reorder the fields in the flows to make better sense to the user.
* categories sections to help the user for onboarding, as the onboarding and the app is user centric, so the more info/guidance we give them the better.
* map the onboarding experience to the business_profiles table, so the fields in the table need to be populated by the user.
