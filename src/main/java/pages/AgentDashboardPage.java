package pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.Page.WaitForURLOptions;

public class AgentDashboardPage extends BasePage {


    private static final String SERVICE_DESK_AGENT_ROLE_COLLAPSED = "span.node-name:has-text('Service Desk Agent')";
    private static final String OPEN_TICKETS_MENU_ITEM = "span.node-name:has-text('06. Open Tickets')";
    private static final String NEW_TICKET_BUTTON = "span.e-tbar-btn-text:has-text('New')";
    private static final String SWITCH_TO_AGENT_UI_BUTTON = "button[aria-label='Classic'].switch-button";
    private static final String AGENT_UI_HOME_URL = "**/agentui/home";
    private static final String CLASSIC_UI_START_URL = "**/workspace/start";

    public AgentDashboardPage(Page page) {
        super(page);

        try {
            this.page.waitForURL("**/start", new Page.WaitForURLOptions().setTimeout(10000));
            System.out.println("Successfully loaded page after login: " + this.page.url());
        } catch (Exception e) {
            System.out.println("Primary URL wait failed, trying alternative: " + e.getMessage());
            this.page.waitForURL(CLASSIC_UI_START_URL, new Page.WaitForURLOptions().setTimeout(10000));
            System.out.println("Successfully loaded page after login (fallback): " + this.page.url());
        }
    }

    public void switchToAgentUI() {
        System.out.println("Attempting to switch from Classic UI to Modern Agent UI...");
        System.out.println("Current URL before switch: " + this.page.url());

        try {
            System.out.println("Waiting for switch button to be visible...");
            this.page.waitForSelector(SWITCH_TO_AGENT_UI_BUTTON, new Page.WaitForSelectorOptions().setTimeout(10000));

            System.out.println("Clicking switch button...");
            this.page.click(SWITCH_TO_AGENT_UI_BUTTON);
            System.out.println("Switch button clicked successfully!");
            System.out.println("Waiting for navigation to Agent UI...");
            Page.WaitForURLOptions options = new Page.WaitForURLOptions()
                    .setTimeout(15000);
            this.page.waitForURL(AGENT_UI_HOME_URL, options);
            System.out.println("Successfully switched to Modern Agent UI. Current URL: " + this.page.url());
        } catch (PlaywrightException e) {
            System.err.println("Failed to switch to Agent UI. Error: " + e.getMessage());
            System.err.println("Current URL: " + this.page.url());
            System.err.println("Button selector used: " + SWITCH_TO_AGENT_UI_BUTTON);

            try {
                boolean buttonExists = this.page.isVisible(SWITCH_TO_AGENT_UI_BUTTON);
                System.err.println("Button visibility check: " + buttonExists);
            } catch (Exception ex) {
                System.err.println("Could not check button visibility: " + ex.getMessage());
            }

            throw new RuntimeException("Switch to Agent UI failed.", e);
        }
    }

    public void clickOpenTicketsMenu() {
        System.out.println("=== Navigating to Open Tickets ===");
        
        try {

            System.out.println("Waiting for 'Service Desk Agent' menu item...");
            this.page.waitForSelector(SERVICE_DESK_AGENT_ROLE_COLLAPSED, new Page.WaitForSelectorOptions().setTimeout(10000));
            
            System.out.println("Clicking 'Service Desk Agent' to expand menu...");
            this.page.click(SERVICE_DESK_AGENT_ROLE_COLLAPSED);
            this.page.waitForTimeout(1000);

            System.out.println("Waiting for '06. Open Tickets' menu item...");
            this.page.waitForSelector(OPEN_TICKETS_MENU_ITEM, new Page.WaitForSelectorOptions().setTimeout(10000));
            
            System.out.println("Clicking '06. Open Tickets' link...");
            this.page.click(OPEN_TICKETS_MENU_ITEM);

            System.out.println("Waiting for Open Tickets page to load...");
            this.page.waitForURL("**/agentui/workspace/list-view/**", new Page.WaitForURLOptions().setTimeout(15000));
            System.out.println(" Successfully navigated to the Open Tickets list view. Current URL: " + this.page.url());
            
        } catch (Exception e) {
            System.err.println("Failed to navigate to Open Tickets. Error: " + e.getMessage());
            System.err.println("Current URL: " + this.page.url());
            throw new RuntimeException("Navigation to Open Tickets failed.", e);
        }
    }

    public void clickNewTicketButton() {
        System.out.println("=== Creating New Ticket ===");
        
        try {

            System.out.println("Waiting for 'New' button to be visible...");
            this.page.waitForSelector(NEW_TICKET_BUTTON, new Page.WaitForSelectorOptions().setTimeout(10000));

            System.out.println("Clicking 'New' button to create a ticket...");
            this.page.click(NEW_TICKET_BUTTON);

            System.out.println("Waiting for New Ticket creation page to load...");
            this.page.waitForURL("**/agentui/workspace/datacard/new/**", new Page.WaitForURLOptions().setTimeout(15000));
            System.out.println("Successfully navigated to the New Ticket creation page. Current URL: " + this.page.url());
            
        } catch (Exception e) {
            System.err.println("Failed to click New Ticket button. Error: " + e.getMessage());
            System.err.println("Current URL: " + this.page.url());
            throw new RuntimeException("New Ticket button click failed.", e);
        }
    }
}
