package testcases.components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import testcases.BaseTest;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

class MenuTests extends BaseTest {
    @BeforeEach
    void createContextAndPage() {
        context = browser.newContext();
        page = context.newPage();
        page.navigate("https://test-with-me-app.vercel.app/learning/web-elements/components/menu");
    }

    @Test
    void verifyMenu(){
        String menuXpath = "//div[@role='menuitem' and span[normalize-space(text())='My Menu']]";
        page.locator(menuXpath).hover();

        String options = "//ul[contains(concat(' ',normalize-space(@class),' '),' ant-menu-item-group-list ')]//li[span[normalize-space(text())='Option 1']]";
        page.locator(options).click();

        String expectedLabelXpath = "//div[contains(., 'Current value: ') and ./span[contains(concat(' ', normalize-space(@class),' '),' text-rose-500 ')]]";

    }
}
