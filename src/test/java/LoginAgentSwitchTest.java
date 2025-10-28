import base.BaseTests;
import pages.AgentDashboardPage;
import pages.LoginPage;
import utils.ConfigReader;

public class LoginAgentSwitchTest {
    public static void main(String[] args) {
        try {
            System.out.println("Starting Login and Logout Test...");

            BaseTests baseTests = new BaseTests();
            baseTests.setUp();

            var page = BaseTests.getPage();

            System.out.println("\n=== STEP 1: LOGIN ===");
            String loginUrl = ConfigReader.get("base.url");
            System.out.println("Navigating to: " + loginUrl);
            page.navigate(loginUrl);
            
            LoginPage loginPage = new LoginPage(page);
            String username = ConfigReader.get("username");
            String password = ConfigReader.get("password");
            
            System.out.println("Starting login process...");
            loginPage.login(username, password);
            System.out.println("Login completed successfully!");
            

            System.out.println("\n=== STEP 2: SWITCH TO AGENT UI ===");
            AgentDashboardPage dashboardPage = new AgentDashboardPage(page);
            dashboardPage.switchToAgentUI();
            System.out.println("Successfully switched to Agent UI!");

            
        } catch (Exception e) {
            System.err.println("Login/Logout test failed: " + e.getMessage());
            e.printStackTrace();
        } finally {

            try {
                BaseTests baseTests = new BaseTests();
                baseTests.tearDown();
            } catch (Exception e) {
                System.err.println("Error during cleanup: " + e.getMessage());
            }
        }
    }
}
