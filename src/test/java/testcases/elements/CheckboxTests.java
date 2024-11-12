package testcases.elements;

import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import testcases.BaseTest;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CheckboxTests extends BaseTest {
    Locator pageHeader;
    Locator checkedCheckboxText;
    Locator allCheckbox;
    Locator allCheckboxText;

    @BeforeEach
    void createContextAndPage() {
        context = browser.newContext();
        page = context.newPage();
        page.navigate("https://test-with-me-app.vercel.app/learning/web-elements/elements/checkbox");
        pageHeader = page.locator("//span[contains(concat(' ', normalize-space(@class), ' '), 'ant-divider-inner-text')][contains(text(), 'Checkbox')]");
        allCheckboxText = page.locator("//div[contains(concat(' ',normalize-space(@class),' '),' ant-checkbox-group css-vryruh ')]/*//span[text()]");
    }

    @Test
    void verifyNavigateSuccess() {
        assertThat(page).hasTitle("Test With Me aka Tho Test");
        assertThat(pageHeader).isVisible();
    }


    void uncheckAll(){
        allCheckbox = page.locator("//div[contains(concat(' ',normalize-space(@class),' '),' ant-checkbox-group css-vryruh ')]/*//input[@type='checkbox']");
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
        uncheckAll();
        selectCheckboxWithLabel(expect);
        checkedCheckboxText = page.locator("//div[contains(.//text(), 'Selected values:')]");
        assertThat(checkedCheckboxText).hasText(String.format("Selected values: %s", expect));
    }

    @Test
    void verifyAllCheckbox(){
        selectCheckboxWithLabel("Apple");
        selectCheckboxWithLabel("Pear");
        selectCheckboxWithLabel("Orange");
        checkedCheckboxText = page.locator("//div[contains(.//text(), 'Selected values:')]");
        assertThat(checkedCheckboxText).hasText("Selected values: Apple Pear Orange");
    }
}