package testcases.components;

import org.junit.jupiter.api.Test;
import testcases.BaseTest;

class MenuTests extends BaseTest {
    @Test
    void verifyMenu(){
        page.navigate("https://test-with-me-app.vercel.app/learning/web-elements/components/menu");

        String menuXpath = "//div[@role='menuitem' and span[normalize-space(text())='My Menu']]";
        page.locator(menuXpath).hover();

        String options = "//ul[contains(concat(' ',normalize-space(@class),' '),' ant-menu-item-group-list ')]//li[span[normalize-space(text())='Option 1']]";
        page.locator(options).click();

        String expectedLabelXpath = "//div[contains(., 'Current value: ') and ./span[contains(concat(' ', normalize-space(@class),' '),' text-rose-500 ')]]";

    }
}
