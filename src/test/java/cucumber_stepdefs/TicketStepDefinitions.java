package cucumber_stepdefs;

import io.cucumber.java.en.*;
import com.microsoft.playwright.Page;
import pages.AgentDashboardPage;
import pages.NewTicketPage;
import pages.OpenTicketsPage;
import base.BaseTests;
import utils.TicketData;

import static org.testng.Assert.assertTrue;
import java.util.Map;

public class TicketStepDefinitions {

    private Page page;
    private AgentDashboardPage dashboardPage;
    private NewTicketPage newTicketPage;
    private OpenTicketsPage openTicketsPage;

    private final String ticketTitle = "Printer not working";

    public TicketStepDefinitions() {

        this.page = BaseTests.getPage();
        this.dashboardPage = new AgentDashboardPage(page);
        this.newTicketPage = new NewTicketPage(page);
        this.openTicketsPage = new OpenTicketsPage(page);
    }




    @Given("The user is on the Open Tickets page")
    public void the_user_is_on_the_open_tickets_page() {

        dashboardPage.switchToAgentUI();
        dashboardPage.clickOpenTicketsMenu();
    }

    @When("the user creates a new ticket with title Printer not working")
    public void the_user_creates_a_new_ticket_with_title_printer_not_working() {

        dashboardPage.clickNewTicketButton();
        

        Map<String, String> ticketData = TicketData.Templates.PRINTER_ISSUE;
        newTicketPage.createNewTicket(ticketData);
        

        newTicketPage.saveTicket();
    }

    @Then("the ticket titled Printer not working should appear in the open tickets list")
    public void the_ticket_titled_printer_not_working_should_appear_in_the_open_tickets_list() {
        System.out.println("=== Verifying Ticket Creation ===");
        

        System.out.println("Navigating back to Open Tickets list...");
        dashboardPage.clickOpenTicketsMenu();
        

        System.out.println("Waiting for ticket to appear in the list...");
        boolean ticketFound = openTicketsPage.waitForTicketToAppear(ticketTitle, 30); // Wait up to 30 seconds
        

        assertTrue(ticketFound,
                "Ticket 'Printer not working' was not found in the open tickets list within 30 seconds");
        
        System.out.println("Ticket verification completed successfully!");
    }
}
