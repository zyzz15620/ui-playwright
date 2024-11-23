package testcases.components;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import testcases.BaseTest;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

class TreeSelectTests extends BaseTest {
    @ParameterizedTest
    @ValueSource(strings = {"Light", "Light > Bamboo", "Heavy > Walnut"})
    void verifyTreeSelect(String input){
        page.navigate("https://test-with-me-app.vercel.app/learning/web-elements/components/tree-select");
        String inputFieldXpath = "//span[normalize-space(text())='Please select']/..//input[contains(concat(' ',normalize-space(@class),' '),' ant-select-selection-search-input ')]";
        page.locator(inputFieldXpath).click();
        String[] inputs = input.split(">");

        for(int i = 0; i<inputs.length ; i++){
            String item = inputs[i];
            String optionXpath = String.format("//span[contains(concat(' ',normalize-space(@class),' '),' ant-select-tree-title ')][normalize-space(text())='%s']", item.trim());
            String openTreeButton = String.format("//span[contains(concat(' ',normalize-space(@class),' '),' ant-select-tree-switcher ')]" +
                    "[./following-sibling::span[.//span[contains(concat(' ',normalize-space(@class),' '),' ant-select-tree-title ') and normalize-space(text())='%s']]]", item.trim());
            if(inputs.length > 1 && i==0){
                page.locator(openTreeButton).click();
            } else {
                page.locator(optionXpath).click();
            }
        }

        String expectedLabelXpath = "//div[contains(., 'Current value: ') and ./span[contains(concat(' ', normalize-space(@class),' '),' text-rose-500 ')]]";
        assertThat(page.locator(expectedLabelXpath)).hasText(String.format("Current value: %s", inputs[inputs.length-1].trim().toLowerCase()));
    }
}
