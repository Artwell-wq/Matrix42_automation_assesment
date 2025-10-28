package pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

public class OpenTicketsPage extends BasePage {

    private static final String TICKET_LIST_CONTAINER = ".e-grid, .ticket-list, [class*='list'], [class*='grid']";
    private static final String TICKET_ROW = ".e-row, .ticket-row, [class*='row']";
    private static final String TICKET_SUBJECT_COLUMN = ".e-cell, .ticket-subject, [class*='subject']";
    private static final String REFRESH_BUTTON = "button:has-text('Refresh'), .refresh-button, [title*='refresh']";

    public OpenTicketsPage(Page page) {
        super(page);
    }

    public void refreshTicketList() {
        System.out.println("Refreshing ticket list...");
        try {

            this.page.waitForSelector(REFRESH_BUTTON, new Page.WaitForSelectorOptions()
                .setTimeout(5000));
            this.page.click(REFRESH_BUTTON);
            System.out.println("Ticket list refreshed");
        } catch (Exception e) {

            System.out.println("No refresh button found, waiting for auto-refresh...");
            this.page.waitForTimeout(2000);
        }
    }

    public boolean isTicketVisible(String title) {
        System.out.println("Checking if ticket '" + title + "' is visible in the list...");
        
        try {

            refreshTicketList();

            this.page.waitForSelector(TICKET_LIST_CONTAINER, new Page.WaitForSelectorOptions()
                .setTimeout(10000));

            String[] possibleSelectors = {
                "text=" + title,
                "[title*='" + title + "']",
                "[data-title*='" + title + "']",
                ".e-cell:has-text('" + title + "')",
                ".ticket-subject:has-text('" + title + "')",
                "td:has-text('" + title + "')",
                "div:has-text('" + title + "')"
            };
            
            for (String selector : possibleSelectors) {
                try {
                    boolean visible = this.page.isVisible(selector);
                    if (visible) {
                        System.out.println("Ticket found with selector: " + selector);
                        return true;
                    }
                } catch (Exception e) {

                }
            }
            
            System.out.println("Ticket '" + title + "' not found in the list");
            return false;
            
        } catch (Exception e) {
            System.err.println("Error checking ticket visibility: " + e.getMessage());
            return false;
        }
    }

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

    public boolean waitForTicketToAppear(String title, int timeoutSeconds) {
        System.out.println("Waiting for ticket '" + title + "' to appear in the list...");
        
        long startTime = System.currentTimeMillis();
        long timeoutMs = timeoutSeconds * 1000;
        
        while (System.currentTimeMillis() - startTime < timeoutMs) {
            if (isTicketVisible(title)) {
                System.out.println("Ticket '" + title + "' appeared in the list!");
                return true;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        
        System.out.println("Ticket '" + title + "' did not appear within " + timeoutSeconds + " seconds");
        return false;
    }
}
