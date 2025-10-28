package pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.Page.WaitForURLOptions;

public class AgentDashboardPage extends BasePage {

    // --- Locators for Modern Agent UI Navigation (After the switch) ---
    // Service Desk Agent - based on HTML structure: span with class="node-name" containing "Service Desk Agent"
    private static final String SERVICE_DESK_AGENT_ROLE_COLLAPSED = "span.node-name:has-text('Service Desk Agent')";
    // Open Tickets menu item - based on HTML structure: span with class="node-name" containing "06. Open Tickets"
    private static final String OPEN_TICKETS_MENU_ITEM = "span.node-name:has-text('06. Open Tickets')";

    // Locator for the 'New' button - based on HTML structure: span with class="e-tbar-btn-text" containing "New"
    private static final String NEW_TICKET_BUTTON = "span.e-tbar-btn-text:has-text('New')";

    // CRITICAL: Locator for the switch button. This button switches from Classic UI to Modern Agent UI.
    // Based on the HTML structure: button with aria-label="Classic" and class="switch-button"
    private static final String SWITCH_TO_AGENT_UI_BUTTON = "button[aria-label='Classic'].switch-button";

    // The target URL after the switch
    private static final String AGENT_UI_HOME_URL = "**/agentui/home";
    // The initial URL after login (Classic UI)
    private static final String CLASSIC_UI_START_URL = "**/workspace/start";

    public AgentDashboardPage(Page page) {
        super(page);
        // Wait for the initial page to load after login - be more flexible with URL matching
        try {
            // Try to wait for either the start URL or workspace/start URL
            this.page.waitForURL("**/start", new Page.WaitForURLOptions().setTimeout(10000));
            System.out.println("Successfully loaded page after login: " + this.page.url());
        } catch (Exception e) {
            System.out.println("Primary URL wait failed, trying alternative: " + e.getMessage());
            // Fallback to workspace/start if the first one fails
            this.page.waitForURL(CLASSIC_UI_START_URL, new Page.WaitForURLOptions().setTimeout(10000));
            System.out.println("Successfully loaded page after login (fallback): " + this.page.url());
        }
    }

    /**
     * Clicks the switch button with aria-label='Classic' to transition from the
     * Classic UI (Start URL) to the Modern Agent UI (agentui/home).
     */
    public void switchToAgentUI() {
        System.out.println("Attempting to switch from Classic UI to Modern Agent UI...");
        System.out.println("Current URL before switch: " + this.page.url());

        try {
            // Wait for the switch button to be visible before clicking
            System.out.println("Waiting for switch button to be visible...");
            this.page.waitForSelector(SWITCH_TO_AGENT_UI_BUTTON, new Page.WaitForSelectorOptions().setTimeout(10000));
            
            // Find and click the button with aria-label="Classic" to initiate the switch
            System.out.println("Clicking switch button...");
            this.page.click(SWITCH_TO_AGENT_UI_BUTTON);
            System.out.println("Switch button clicked successfully!");

            // Wait for the URL to stabilize on the Agent UI home page
            System.out.println("Waiting for navigation to Agent UI...");
            Page.WaitForURLOptions options = new Page.WaitForURLOptions()
                    .setTimeout(15000);
            this.page.waitForURL(AGENT_UI_HOME_URL, options);
            System.out.println("✅ Successfully switched to Modern Agent UI. Current URL: " + this.page.url());
        } catch (PlaywrightException e) {
            System.err.println("❌ Failed to switch to Agent UI. Error: " + e.getMessage());
            System.err.println("Current URL: " + this.page.url());
            System.err.println("Button selector used: " + SWITCH_TO_AGENT_UI_BUTTON);
            
            // Try to find if the button exists with a different approach
            try {
                boolean buttonExists = this.page.isVisible(SWITCH_TO_AGENT_UI_BUTTON);
                System.err.println("Button visibility check: " + buttonExists);
            } catch (Exception ex) {
                System.err.println("Could not check button visibility: " + ex.getMessage());
            }
            
            // Optionally re-throw to fail the test if the switch is mandatory
            throw new RuntimeException("Switch to Agent UI failed.", e);
        }
    }


    /**
     * Navigates the Modern Agent UI's left-hand menu to the Open Tickets view.
     * This must be called *after* switchToAgentUI().
     */
    public void clickOpenTicketsMenu() {
        System.out.println("=== Navigating to Open Tickets ===");
        
        try {
            // 1. Wait for and click the 'Service Desk Agent' role menu to expand it
            System.out.println("Waiting for 'Service Desk Agent' menu item...");
            this.page.waitForSelector(SERVICE_DESK_AGENT_ROLE_COLLAPSED, new Page.WaitForSelectorOptions().setTimeout(10000));
            
            System.out.println("Clicking 'Service Desk Agent' to expand menu...");
            this.page.click(SERVICE_DESK_AGENT_ROLE_COLLAPSED);
            
            // Wait a moment for the menu to expand
            this.page.waitForTimeout(1000);

            // 2. Wait for and click the '06. Open Tickets' menu item
            System.out.println("Waiting for '06. Open Tickets' menu item...");
            this.page.waitForSelector(OPEN_TICKETS_MENU_ITEM, new Page.WaitForSelectorOptions().setTimeout(10000));
            
            System.out.println("Clicking '06. Open Tickets' link...");
            this.page.click(OPEN_TICKETS_MENU_ITEM);

            // 3. Wait for the Open Tickets URL to load (The list view)
            System.out.println("Waiting for Open Tickets page to load...");
            this.page.waitForURL("**/agentui/workspace/list-view/**", new Page.WaitForURLOptions().setTimeout(15000));
            System.out.println("✅ Successfully navigated to the Open Tickets list view. Current URL: " + this.page.url());
            
        } catch (Exception e) {
            System.err.println("❌ Failed to navigate to Open Tickets. Error: " + e.getMessage());
            System.err.println("Current URL: " + this.page.url());
            throw new RuntimeException("Navigation to Open Tickets failed.", e);
        }
    }

    /**
     * Clicks the 'New' button to initiate the ticket creation process.
     */
    public void clickNewTicketButton() {
        System.out.println("=== Creating New Ticket ===");
        
        try {
            // 1. Wait for the 'New' button to appear
            System.out.println("Waiting for 'New' button to be visible...");
            this.page.waitForSelector(NEW_TICKET_BUTTON, new Page.WaitForSelectorOptions().setTimeout(10000));

            // 2. Click the button to create a new ticket
            System.out.println("Clicking 'New' button to create a ticket...");
            this.page.click(NEW_TICKET_BUTTON);

            // 3. Wait for the new ticket creation page URL
            System.out.println("Waiting for New Ticket creation page to load...");
            this.page.waitForURL("**/agentui/workspace/datacard/new/**", new Page.WaitForURLOptions().setTimeout(15000));
            System.out.println("✅ Successfully navigated to the New Ticket creation page. Current URL: " + this.page.url());
            
        } catch (Exception e) {
            System.err.println("❌ Failed to click New Ticket button. Error: " + e.getMessage());
            System.err.println("Current URL: " + this.page.url());
            throw new RuntimeException("New Ticket button click failed.", e);
        }
    }
}
