package pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

public class OpenTicketsPage extends BasePage {

    // Locators for the Open Tickets list
    private static final String TICKET_LIST_CONTAINER = ".e-grid, .ticket-list, [class*='list'], [class*='grid']";
    private static final String TICKET_ROW = ".e-row, .ticket-row, [class*='row']";
    private static final String TICKET_SUBJECT_COLUMN = ".e-cell, .ticket-subject, [class*='subject']";
    private static final String REFRESH_BUTTON = "button:has-text('Refresh'), .refresh-button, [title*='refresh']";

    public OpenTicketsPage(Page page) {
        super(page);
    }

    /**
     * Refreshes the ticket list to ensure we have the latest data
     */
    public void refreshTicketList() {
        System.out.println("Refreshing ticket list...");
        try {
            // Try to find and click refresh button
            this.page.waitForSelector(REFRESH_BUTTON, new Page.WaitForSelectorOptions()
                .setTimeout(5000));
            this.page.click(REFRESH_BUTTON);
            System.out.println("✅ Ticket list refreshed");
        } catch (Exception e) {
            // If no refresh button, just wait a moment for any auto-refresh
            System.out.println("No refresh button found, waiting for auto-refresh...");
            this.page.waitForTimeout(2000);
        }
    }

    /**
     * Checks if a ticket with the given title/subject is visible in the Open Tickets list
     */
    public boolean isTicketVisible(String title) {
        System.out.println("Checking if ticket '" + title + "' is visible in the list...");
        
        try {
            // First refresh the list to get latest data
            refreshTicketList();
            
            // Wait for the ticket list to be visible
            this.page.waitForSelector(TICKET_LIST_CONTAINER, new Page.WaitForSelectorOptions()
                .setTimeout(10000));
            
            // Look for the ticket in various possible locations
            String[] possibleSelectors = {
                "text=" + title,  // Direct text match
                "[title*='" + title + "']",  // Title attribute
                "[data-title*='" + title + "']",  // Data attribute
                ".e-cell:has-text('" + title + "')",  // Grid cell
                ".ticket-subject:has-text('" + title + "')",  // Subject column
                "td:has-text('" + title + "')",  // Table cell
                "div:has-text('" + title + "')"  // Div element
            };
            
            for (String selector : possibleSelectors) {
                try {
                    boolean visible = this.page.isVisible(selector);
                    if (visible) {
                        System.out.println("✅ Ticket found with selector: " + selector);
                        return true;
                    }
                } catch (Exception e) {
                    // Continue to next selector
                }
            }
            
            System.out.println("❌ Ticket '" + title + "' not found in the list");
            return false;
            
        } catch (Exception e) {
            System.err.println("❌ Error checking ticket visibility: " + e.getMessage());
            return false;
        }
    }

    /**
     * Gets the count of tickets in the list
     */
    public int getTicketCount() {
        try {
            this.page.waitForSelector(TICKET_ROW, new Page.WaitForSelectorOptions()
                .setTimeout(5000));
            return this.page.locator(TICKET_ROW).count();
        } catch (Exception e) {
            System.err.println("Error getting ticket count: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Waits for a specific ticket to appear in the list (useful after creating a new ticket)
     */
    public boolean waitForTicketToAppear(String title, int timeoutSeconds) {
        System.out.println("Waiting for ticket '" + title + "' to appear in the list...");
        
        long startTime = System.currentTimeMillis();
        long timeoutMs = timeoutSeconds * 1000;
        
        while (System.currentTimeMillis() - startTime < timeoutMs) {
            if (isTicketVisible(title)) {
                System.out.println("✅ Ticket '" + title + "' appeared in the list!");
                return true;
            }
            
            // Wait a bit before checking again
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        
        System.out.println("❌ Ticket '" + title + "' did not appear within " + timeoutSeconds + " seconds");
        return false;
    }
}
