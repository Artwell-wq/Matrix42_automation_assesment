package pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import java.util.Map;

public class NewTicketPage extends BasePage {

    // Locators for the New Ticket form fields - based on actual HTML structure
    private static final String QUICKFILL_FIELD = "input[placeholder*='Quickfill'], input[name*='quickfill'], input[id*='quickfill']";
    
    // Dropdown fields (type 3 letters then select from dropdown)
    private static final String TICKET_TYPE_FIELD = "div.e-ddl[aria-label='dropdownlist'] input, .e-ddl input, [role='combobox'] input";
    private static final String CUSTOMER_FIELD = "div.e-ddl[aria-label='dropdownlist'] input, .e-ddl input, [role='combobox'] input";
    private static final String TEAM_FIELD = "div.e-ddl[aria-label='dropdownlist'] input, .e-ddl input, [role='combobox'] input";
    
    // Direct typing fields
    private static final String SUBJECT_FIELD = "input[title='Subject']";
    private static final String DETAILS_FIELD = "div[contenteditable='true'][id*='rte-edit-view']";
    
    // Dropdown options (appear after typing)
    private static final String DROPDOWN_OPTION = ".e-dropdownbase .e-list-item";
    private static final String DROPDOWN_LOADING = ".e-dropdownbase .e-list-loading";
    
    // Save and Cancel buttons - based on actual HTML structure
    private static final String SAVE_BUTTON = "button[data-test='save-datacard-button'], button.esm-ui-button.primary";
    private static final String CANCEL_BUTTON = "button[data-test='cancel-datacard-button'], button.esm-ui-button.secondary";
    
    // Success/confirmation messages
    private static final String SUCCESS_MESSAGE = ".success-message, .alert-success, [class*='success']";
    private static final String TICKET_CREATED_MESSAGE = "text=Ticket created, text=Successfully created, text=Saved successfully";

    public NewTicketPage(Page page) {
        super(page);
    }

    /**
     * Waits for the new ticket form to be fully loaded
     */
    public void waitForFormToLoad() {
        System.out.println("Waiting for new ticket form to load...");
        try {
            // Wait for the Subject field (direct typing field) to be visible
            this.page.waitForSelector(SUBJECT_FIELD, new Page.WaitForSelectorOptions().setTimeout(15000));
            System.out.println("✅ New ticket form loaded successfully!");
        } catch (Exception e) {
            System.err.println("❌ Form did not load within timeout. Error: " + e.getMessage());
            throw new RuntimeException("New ticket form failed to load.", e);
        }
    }

    /**
     * Fills a dropdown field by typing the first 3 letters and selecting from the dropdown.
     * This method handles the "enter first 3 letters then select from dropdown" pattern.
     */
    public void fillDropdownField(String fieldSelector, String searchText, String selectText) {
        System.out.println("Filling dropdown field: " + fieldSelector + " with search: '" + searchText + "'");
        
        try {
            // 1. Wait for the field to be visible and clickable
            this.page.waitForSelector(fieldSelector, new Page.WaitForSelectorOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(10000));
            
            // 2. Clear the field and type the search text
            this.page.fill(fieldSelector, "");
            this.page.type(fieldSelector, searchText);
            System.out.println("Typed search text: '" + searchText + "'");
            
            // 3. Wait for dropdown to appear and loading to complete
            this.page.waitForSelector(DROPDOWN_OPTION, new Page.WaitForSelectorOptions()
                .setTimeout(5000));
            
            // Wait for loading to complete (if present)
            try {
                this.page.waitForSelector(DROPDOWN_LOADING, new Page.WaitForSelectorOptions()
                    .setTimeout(2000));
                this.page.waitForSelector(DROPDOWN_LOADING, new Page.WaitForSelectorOptions()
                    .setState(WaitForSelectorState.HIDDEN)
                    .setTimeout(5000));
            } catch (Exception e) {
                // Loading indicator might not be present, continue
            }
            
            // 4. Find and click the desired option
            String optionSelector = DROPDOWN_OPTION + ":has-text('" + selectText + "')";
            this.page.waitForSelector(optionSelector, new Page.WaitForSelectorOptions()
                .setTimeout(5000));
            
            this.page.click(optionSelector);
            System.out.println("Selected option: '" + selectText + "'");
            
        } catch (Exception e) {
            System.err.println("❌ Failed to fill dropdown field. Error: " + e.getMessage());
            throw new RuntimeException("Dropdown field filling failed for: " + fieldSelector, e);
        }
    }

    /**
     * Fills the Quickfill field
     */
    public void fillQuickfill(String quickfillText) {
        System.out.println("Filling Quickfill field...");
        this.page.fill(QUICKFILL_FIELD, quickfillText);
    }

    /**
     * Fills the Ticket Type field using dropdown selection
     */
    public void fillTicketType(String searchText, String selectText) {
        System.out.println("Filling Ticket Type field...");
        fillDropdownField(TICKET_TYPE_FIELD, searchText, selectText);
    }

    /**
     * Fills the Customer field using dropdown selection
     */
    public void fillCustomer(String searchText, String selectText) {
        System.out.println("Filling Customer field...");
        fillDropdownField(CUSTOMER_FIELD, searchText, selectText);
    }

    /**
     * Fills the Team field using dropdown selection
     */
    public void fillTeam(String searchText, String selectText) {
        System.out.println("Filling Team field...");
        fillDropdownField(TEAM_FIELD, searchText, selectText);
    }

    /**
     * Fills the Subject field (direct typing)
     */
    public void fillSubject(String subject) {
        System.out.println("Filling Subject field (direct typing)...");
        this.page.fill(SUBJECT_FIELD, subject);
    }

    /**
     * Fills the Details field (direct typing in rich text editor)
     */
    public void fillDetails(String details) {
        System.out.println("Filling Details field (rich text editor)...");
        // For contenteditable div, we need to click first then type
        this.page.click(DETAILS_FIELD);
        this.page.fill(DETAILS_FIELD, details);
    }

    /**
     * Creates a new ticket with the provided details
     */
    public void createNewTicket(String quickfill, String ticketTypeSearch, String ticketTypeSelect,
                               String customerSearch, String customerSelect, String teamSearch, 
                               String teamSelect, String subject, String details) {
        System.out.println("=== Creating New Ticket with Details ===");
        
        try {
            // Fill all the required fields
            if (quickfill != null && !quickfill.isEmpty()) {
                fillQuickfill(quickfill);
            }
            
            if (ticketTypeSearch != null && ticketTypeSelect != null) {
                fillTicketType(ticketTypeSearch, ticketTypeSelect);
            }
            
            if (customerSearch != null && customerSelect != null) {
                fillCustomer(customerSearch, customerSelect);
            }
            
            if (teamSearch != null && teamSelect != null) {
                fillTeam(teamSearch, teamSelect);
            }
            
            if (subject != null && !subject.isEmpty()) {
                fillSubject(subject);
            }
            
            if (details != null && !details.isEmpty()) {
                fillDetails(details);
            }
            
            System.out.println("✅ All fields filled successfully!");
            
        } catch (Exception e) {
            System.err.println("❌ Failed to create new ticket. Error: " + e.getMessage());
            throw new RuntimeException("New ticket creation failed.", e);
        }
    }

    /**
     * Creates a new ticket using a data map (from TicketData utility)
     */
    public void createNewTicket(Map<String, String> ticketData) {
        System.out.println("=== Creating New Ticket with Data Map ===");
        
        createNewTicket(
            ticketData.get("quickfill"),
            ticketData.get("ticketTypeSearch"),
            ticketData.get("ticketTypeSelect"),
            ticketData.get("customerSearch"),
            ticketData.get("customerSelect"),
            ticketData.get("teamSearch"),
            ticketData.get("teamSelect"),
            ticketData.get("subject"),
            ticketData.get("details")
        );
    }

    /**
     * Saves the ticket and verifies the save was successful
     */
    public void saveTicket() {
        System.out.println("=== Saving Ticket ===");
        
        try {
            // Wait for save button to be visible
            System.out.println("Waiting for Save button to be visible...");
            this.page.waitForSelector(SAVE_BUTTON, new Page.WaitForSelectorOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(10000));
            
            // Wait for save button to be enabled (not disabled)
            System.out.println("Waiting for Save button to be enabled...");
            this.page.waitForFunction("() => { const btn = document.querySelector('button[data-test=\"save-datacard-button\"]'); return btn && !btn.disabled; }", 
                new Page.WaitForFunctionOptions().setTimeout(15000));
            
            // Click the save button
            System.out.println("Clicking Save button...");
            this.page.click(SAVE_BUTTON);
            
            // Wait for navigation back to the Open Tickets list
            System.out.println("Waiting for navigation back to Open Tickets list...");
            this.page.waitForURL("**/agentui/workspace/list-view/**", new Page.WaitForURLOptions()
                .setTimeout(15000));
            System.out.println("✅ Successfully navigated back to Open Tickets list!");
            
            System.out.println("✅ Ticket saved successfully!");
            
        } catch (Exception e) {
            System.err.println("❌ Failed to save ticket. Error: " + e.getMessage());
            System.err.println("Current URL: " + this.page.url());
            throw new RuntimeException("Ticket save failed.", e);
        }
    }

    /**
     * Cancels the ticket creation
     */
    public void cancelTicket() {
        System.out.println("Canceling ticket creation...");
        this.page.click(CANCEL_BUTTON);
        System.out.println("✅ Ticket creation canceled.");
    }
}
