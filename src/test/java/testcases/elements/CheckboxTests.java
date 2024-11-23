package testcases.elements;

import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import testcases.BaseTest;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

class CheckboxTests extends BaseTest {
    String url = "https://test-with-me-app.vercel.app/learning/web-elements/elements/checkbox";
    String checkedCheckboxTextXpath = "//div[contains(.//text(), 'Selected values:')]";

    @Test
    void verifyNavigateSuccess() {
        page.navigate(url);
        String pageHeader = "//span[contains(concat(' ', normalize-space(@class), ' '), 'ant-divider-inner-text')][contains(text(), 'Checkbox')]";
        assertThat(page).hasTitle("Test With Me aka Tho Test");
        assertThat(page.locator(pageHeader)).isVisible();
    }

    void uncheckAll(){
        Locator allCheckbox = page.locator("//div[contains(concat(' ',normalize-space(@class),' '),' ant-checkbox-group css-vryruh ')]/*//input[@type='checkbox']");
        for(int i = 0; i < allCheckbox.count(); i++ ){
            allCheckbox.nth(i).uncheck();
        }
    }

    void selectCheckboxWithLabel(String label){
        String checkBoxXpath = String.format("//label[contains(., '%s')]", label);
        Locator checkbox = page.locator(checkBoxXpath);
        if(!checkbox.isChecked()){
            checkbox.setChecked(true);
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"Apple", "Pear", "Orange"})
    void verifyEachCheckbox(String expect){
        page.navigate(url);
        uncheckAll();
        selectCheckboxWithLabel(expect);
        assertThat(page.locator(checkedCheckboxTextXpath)).hasText(String.format("Selected values: %s", expect));
    }

    @Test
    void verifyAllCheckbox(){
        page.navigate(url);
        selectCheckboxWithLabel("Apple");
        selectCheckboxWithLabel("Pear");
        selectCheckboxWithLabel("Orange");
        assertThat(page.locator(checkedCheckboxTextXpath)).hasText("Selected values: Apple Pear Orange");
    }
}