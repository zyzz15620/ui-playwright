package testcases.components;

import org.junit.jupiter.api.Test;
import testcases.BaseTest;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

class AutoCompleteTest extends BaseTest {
    @Test
    void verifyAutoComplete(){
        page.navigate("https://test-with-me-app.vercel.app/learning/web-elements/components/auto-complete");
        assertThat(page).hasTitle("Test With Me aka Tho Test");
        assertThat(page.locator("//span[normalize-space(text())=\"Auto complete\"]")).isVisible();

        String autoCompleteInputXpath = "//div[normalize-space() = 'Auto complete']//following::input[@type='search']";
        page.locator(autoCompleteInputXpath).click();
        String inputValue = "Downing Street";
        page.locator(autoCompleteInputXpath).fill(inputValue);
        String selectItemXpath = String.format("//div[contains(concat(' ',normalize-space(@class),' '),' ant-select-item-option-content ')][.//text()[contains(normalize-space(), '%s')]]", inputValue);
        page.locator(selectItemXpath).click();
        String expectedElementXpath = "//div[contains(., 'Value: ') and contains(., ' was selected!') and ./span[contains(concat(' ', normalize-space(@class),' '),' text-rose-500 ')]]";
        assertThat(page.locator(expectedElementXpath)).hasText(String.format("Value: %s was selected!", inputValue));
    }
}
