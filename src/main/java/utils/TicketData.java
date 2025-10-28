package utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for managing ticket data and providing different ticket templates
 */
public class TicketData {
    
    /**
     * Creates a ticket data map with the provided values
     */
    public static Map<String, String> createTicketData(String quickfill, String ticketTypeSearch, String ticketTypeSelect,
                                                      String customerSearch, String customerSelect, String teamSearch, 
                                                      String teamSelect, String subject, String details) {
        Map<String, String> ticketData = new HashMap<>();
        ticketData.put("quickfill", quickfill);
        ticketData.put("ticketTypeSearch", ticketTypeSearch);
        ticketData.put("ticketTypeSelect", ticketTypeSelect);
        ticketData.put("customerSearch", customerSearch);
        ticketData.put("customerSelect", customerSelect);
        ticketData.put("teamSearch", teamSearch);
        ticketData.put("teamSelect", teamSelect);
        ticketData.put("subject", subject);
        ticketData.put("details", details);
        return ticketData;
    }
    
    /**
     * Predefined ticket templates for common scenarios
     */
    public static class Templates {
        
        /**
         * Printer issue ticket template
         */
        public static Map<String, String> PRINTER_ISSUE = createTicketData(
            "Printer not working",
            "inc", "Incident",
            "cus", "Customer Name",
            "tea", "Service Desk Team",
            "Printer not working",
            "The printer in the office is not working properly. Please investigate and fix the issue."
        );
        
        /**
         * Email issue ticket template
         */
        public static Map<String, String> EMAIL_ISSUE = createTicketData(
            "Email access problem",
            "inc", "Incident",
            "cus", "Customer Name",
            "tea", "Service Desk Team",
            "Cannot access email",
            "User is unable to access their email account. Please check the email server configuration."
        );
        
        /**
         * Password reset ticket template
         */
        public static Map<String, String> PASSWORD_RESET = createTicketData(
            "Password reset request",
            "req", "Request",
            "cus", "Customer Name",
            "tea", "Service Desk Team",
            "Password reset needed",
            "User has forgotten their password and needs it to be reset."
        );
        
        /**
         * Software installation ticket template
         */
        public static Map<String, String> SOFTWARE_INSTALLATION = createTicketData(
            "Software installation",
            "req", "Request",
            "cus", "Customer Name",
            "tea", "Service Desk Team",
            "Install new software",
            "User needs to have new software installed on their computer."
        );
        
        /**
         * Network connectivity issue ticket template
         */
        public static Map<String, String> NETWORK_ISSUE = createTicketData(
            "Network connectivity problem",
            "inc", "Incident",
            "cus", "Customer Name",
            "tea", "Service Desk Team",
            "Network connection issues",
            "User is experiencing intermittent network connectivity issues. Please investigate."
        );
    }
    
    /**
     * Creates a custom ticket with specific data
     */
    public static Map<String, String> createCustomTicket(String subject, String details, 
                                                        String ticketType, String priority) {
        return createTicketData(
            subject, // quickfill
            ticketType.toLowerCase().substring(0, 3), ticketType, // ticket type
            "cus", "Customer Name", // customer
            "tea", "Service Desk Team", // team
            subject, // subject
            details // details
        );
    }
    
    /**
     * Gets ticket data from configuration file (if you want to externalize data)
     */
    public static Map<String, String> getTicketDataFromConfig() {
        return createTicketData(
            ConfigReader.get("ticket.quickfill"),
            ConfigReader.get("ticket.type.search"),
            ConfigReader.get("ticket.type.select"),
            ConfigReader.get("ticket.customer.search"),
            ConfigReader.get("ticket.customer.select"),
            ConfigReader.get("ticket.team.search"),
            ConfigReader.get("ticket.team.select"),
            ConfigReader.get("ticket.subject"),
            ConfigReader.get("ticket.details")
        );
    }
}

