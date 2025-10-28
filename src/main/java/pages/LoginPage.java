package  pages;
import com.microsoft.playwright.Page;
import pages.BasePage;

public class LoginPage extends BasePage {


    private static final String DIRECTORY_LOGIN_BUTTON = "text=Login with Directory account";
    private static final String USERNAME_FIELD = "input[name='username']";
    private static final String PASSWORD_FIELD = "input[name='password']";
    private static final String SUBMIT_LOGIN_BUTTON = "xpath=//*[@id='kc-login']";

    private static final String KICKOUT_BUTTON = "input[name='kickoutButton']";

    public LoginPage(Page page) {
        super(page);
    }

    public void login(String username, String password) {
        this.page.click(DIRECTORY_LOGIN_BUTTON);
        this.page.fill(USERNAME_FIELD, username);
        this.page.fill(PASSWORD_FIELD, password);
        this.page.click(SUBMIT_LOGIN_BUTTON);
        handleConcurrentSession();
        this.page.waitForURL("**/start");
    }

    private void handleConcurrentSession() {
        try {

            this.page.waitForSelector(KICKOUT_BUTTON, new Page.WaitForSelectorOptions().setTimeout(1000));

            System.out.println("Concurrent session detected. Terminating previous session...");
            this.page.click(KICKOUT_BUTTON);
        } catch (Exception e) {

        }
    }
}
