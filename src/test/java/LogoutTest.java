import base.BaseTests;
import pages.AgentDashboardPage;
import pages.LoginPage;
import utils.ConfigReader;

public class LogoutTest {
    public static void main(String[] args) {
        try {
            System.out.println("🚀 Starting Login and Logout Test...");
            
            // Initialize Playwright
            BaseTests baseTests = new BaseTests();
            baseTests.setUp();
            
            // Get the page
            var page = BaseTests.getPage();
            
            // Step 1: Login
            System.out.println("\n=== STEP 1: LOGIN ===");
            String loginUrl = ConfigReader.get("base.url");
            System.out.println("Navigating to: " + loginUrl);
            page.navigate(loginUrl);
            
            LoginPage loginPage = new LoginPage(page);
            String username = ConfigReader.get("username");
            String password = ConfigReader.get("password");
            
            System.out.println("Starting login process...");
            loginPage.login(username, password);
            System.out.println("✅ Login completed successfully!");
            
            // Step 2: Switch to Agent UI (optional)
            System.out.println("\n=== STEP 2: SWITCH TO AGENT UI ===");
            AgentDashboardPage dashboardPage = new AgentDashboardPage(page);
            dashboardPage.switchToAgentUI();
            System.out.println("✅ Successfully switched to Agent UI!");
            
            // Step 3: Logout
            System.out.println("\n=== STEP 3: LOGOUT ===");
            loginPage.logout();
            System.out.println("✅ Logout completed successfully!");
            
            System.out.println("\n🎉 LOGIN AND LOGOUT TEST COMPLETED! 🎉");
            
            // Wait a bit to see the final result
            Thread.sleep(3000);
            
        } catch (Exception e) {
            System.err.println("❌ Login/Logout test failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Clean up
            try {
                BaseTests baseTests = new BaseTests();
                baseTests.tearDown();
            } catch (Exception e) {
                System.err.println("Error during cleanup: " + e.getMessage());
            }
        }
    }
}
