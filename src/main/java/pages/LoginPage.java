package  pages;
import com.microsoft.playwright.Page;
import pages.BasePage;

public class LoginPage extends BasePage {

    // Locators
    private static final String DIRECTORY_LOGIN_BUTTON = "text=Login with Directory account";
    private static final String USERNAME_FIELD = "input[name='username']";
    private static final String PASSWORD_FIELD = "input[name='password']";
    private static final String SUBMIT_LOGIN_BUTTON = "xpath=//*[@id='kc-login']";

    // Locator for the session conflict button (from the screenshot)
    private static final String KICKOUT_BUTTON = "input[name='kickoutButton']";

    public LoginPage(Page page) {
        super(page);
    }

    /**
     * Performs the full login sequence, including clicking the directory account button.
     * It also handles the session conflict modal if it appears.
     */
    public void login(String username, String password) {
        // 1. Click the directory account button
        this.page.click(DIRECTORY_LOGIN_BUTTON);

        // 2. Fill credentials and submit login
        this.page.fill(USERNAME_FIELD, username);
        this.page.fill(PASSWORD_FIELD, password);
        this.page.click(SUBMIT_LOGIN_BUTTON);

        // 3. Handle non-deterministic elements (the modal) immediately after login attempt
        handleConcurrentSession();

        // 4. Final wait for the application to load the home page
        // Changed to '**/start' based on your previous logs, assuming that is the first page after login.
        this.page.waitForURL("**/start");
    }

    /**
     * Checks for the session conflict page and terminates the previous session if found.
     * Uses a short wait in a try/catch block to conditionally detect and click the button.
     */
    private void handleConcurrentSession() {
        try {
            // Wait up to 1 second for the KICKOUT_BUTTON to appear (if a concurrent session exists)
            this.page.waitForSelector(KICKOUT_BUTTON, new Page.WaitForSelectorOptions().setTimeout(1000));

            // If the selector is found, click it to terminate the previous session
            System.out.println("Concurrent session detected. Terminating previous session...");
            this.page.click(KICKOUT_BUTTON);
        } catch (Exception e) {
            // If the selector does not appear within 1 second, it means there was no session conflict.
            // We catch the exception and simply proceed without error.
        }
    }
}
