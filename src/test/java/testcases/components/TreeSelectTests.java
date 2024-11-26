package testcases.components;

import com.microsoft.playwright.Locator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import testcases.BaseTest;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

class TreeSelectTests extends BaseTest {
    @ParameterizedTest
    @ValueSource(strings = {"Light", "Light > Bamboo", "Heavy > Walnut"})
    void verifyTreeSelect(String input){
        page.navigate("https://test-with-me-app.vercel.app/learning/web-elements/components/tree-select");
        String treeSelectInputXpath = "//span[normalize-space(text())='Please select']/..//input[contains(concat(' ',normalize-space(@class),' '),' ant-select-selection-search-input ')]";
        page.locator(treeSelectInputXpath).click();
        String[] inputs = input.split(">");

        for(int i = 0; i<inputs.length ; i++){
            String item = inputs[i];
            String itemXpath = String.format("//span[contains(concat(' ',normalize-space(@class),' '),' ant-select-tree-title ')][normalize-space(text())='%s']", item.trim());
            String arrowXpath = String.format("//span[contains(concat(' ',normalize-space(@class),' '),' ant-select-tree-switcher ')]" +
                    "[./following-sibling::span[.//span[contains(concat(' ',normalize-space(@class),' '),' ant-select-tree-title ') and normalize-space(text())='%s']]]", item.trim());
            if(i< inputs.length-1){
                if(!page.locator(arrowXpath).getAttribute("class").contains("ant-select-tree-switcher_open")){
                    page.locator(arrowXpath).click();
                }
            } else {
                page.locator(itemXpath).click();
            }
        }
        String expectedLabelXpath = "//div[contains(., 'Current value: ') and ./span[contains(concat(' ', normalize-space(@class),' '),' text-rose-500 ')]]";
        assertThat(page.locator(expectedLabelXpath)).hasText(String.format("Current value: %s", inputs[inputs.length-1].trim().toLowerCase()));
    }
}
