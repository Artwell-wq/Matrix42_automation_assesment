package cucumber_stepdefs;

import base.BaseTests;
import com.microsoft.playwright.Page;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import pages.AgentDashboardPage;
import pages.LoginPage;
import utils.ConfigReader;

public class LoginStepsDefinitions {

    private Page page;
    private LoginPage loginPage;
    private AgentDashboardPage dashboardPage;

    public LoginStepsDefinitions() {

        this.page = BaseTests.getPage();
        this.loginPage = new LoginPage(page);
        this.dashboardPage = new AgentDashboardPage(page);
    }

    @Given("the user is on the login page")
    public void the_user_is_on_the_login_page() {
        String loginUrl = ConfigReader.get("base.url");
        this.page.navigate(loginUrl);
    }

    @When("the user logs with valid credentials")
    public void the_user_logs_with_valid_credentials() {
        String username = ConfigReader.get("username");
        String password = ConfigReader.get("password");


        loginPage.login(username, password);
    }
}
