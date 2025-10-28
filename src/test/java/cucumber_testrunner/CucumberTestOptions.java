package cucumber_testrunner;

import base.BaseTests;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

@CucumberOptions(
        features = "src/test/java/cucumber_features",
        glue = {"cucumber_stepdefs"},
        plugin = {
                "pretty",
                "html:target/cucumber-report.html",
                "json:target/cucumber-report.json",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        },
        monochrome = true
)
public class CucumberTestOptions extends AbstractTestNGCucumberTests {

        @BeforeClass
        public void initPlaywright() {
                new BaseTests().setUp();
        }

        @AfterClass
        public void closePlaywright() {
                new BaseTests().tearDown();
        }
}
