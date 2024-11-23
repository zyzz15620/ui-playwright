package testcases.components;

import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.Test;
import testcases.BaseTest;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

class SwitchTests extends BaseTest {
    @Test
    void verifyMenu(){
        page.navigate("https://test-with-me-app.vercel.app/learning/web-elements/components/switch");
        String switchButtonXpath = "//div[.//text()[normalize-space()='Switch']]//button[@role='switch']";
        Locator switchButton = page.locator(switchButtonXpath);
        String expectedLabelXpath = "//div[contains(., 'Current Value: ') and ./span[contains(concat(' ', normalize-space(@class),' '),' text-rose-500 ')]]";
        boolean input = false;
        if(Boolean.parseBoolean(switchButton.getAttribute("aria-checked")) != input){
            switchButton.click();
        }
        assertThat(page.locator(expectedLabelXpath)).hasText(String.format("Current Value: %s", input));
    }
}
