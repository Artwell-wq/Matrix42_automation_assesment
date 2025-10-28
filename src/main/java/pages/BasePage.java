package pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Locator;

public class BasePage {
    protected Page page;

    public BasePage(Page page) {
        this.page = page;
    }

    public void click(String selector) {
        page.click(selector);
    }

    public void fill(String selector, String value) {
        page.fill(selector, value);
    }

    public void waitForURL(String urlPattern) {
        page.waitForURL(urlPattern);
    }

    public void waitForText(String text) {
        Locator locator = page.locator("text=" + text);
        locator.waitFor();
    }

    public boolean isVisible(String selector) {
        return page.isVisible(selector);
    }
}
