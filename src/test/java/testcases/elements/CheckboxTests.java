package testcases.elements;

import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        checkedCheckboxText = page.locator("//div[contains(text(), 'Selected values:')]//span[contains(concat(' ',normalize-space(@class),' '),' text-rose-500 ')]");
        allCheckbox = page.locator("//div[contains(concat(' ',normalize-space(@class),' '),' ant-checkbox-group css-vryruh ')]/*//input[@type='checkbox']");
        allCheckboxText = page.locator("//div[contains(concat(' ',normalize-space(@class),' '),' ant-checkbox-group css-vryruh ')]/*//span[text()]");

    }

    @Test
    void verifyNavigateSuccess() {
        assertThat(page).hasTitle("Test With Me aka Tho Test");
        assertThat(pageHeader).isVisible();
    }

    @Test
    void verifyFirstIsCheckedByDefault(){
        for(int i = 0; i < allCheckbox.count(); i++ ){
            if(i == 0) {
                assertThat(allCheckbox.nth(0)).isChecked();
            } else {
                assertThat(allCheckbox.nth(i)).not().isChecked();
            }
        }
    }

    @Test
    void verifyEachCheckbox(){
        String actual;
        String expect;
        for(int i = 0; i < allCheckbox.count(); i++ ){
            allCheckbox.nth(i).uncheck();
            allCheckbox.nth(i).check();
            expect = allCheckboxText.nth(i).innerText();
            actual = checkedCheckboxText.textContent().replace("\u00A0", "").trim();
            assertEquals(expect, actual);
            allCheckbox.nth(i).uncheck();
        }
    }

    @Test
    void verifyMultipleCheckbox(){
        String actual = "";
        String expect = "";
        allCheckbox.nth(0).uncheck();

        for(int i = 0; i < allCheckbox.count(); i++ ){
            allCheckbox.nth(i).check();
            expect = expect + " " + allCheckboxText.nth(i).innerText();
            if(checkedCheckboxText.count() >= 1){
                for(int j = 0; j < checkedCheckboxText.count(); j++ ){
                    actual = actual + " " + checkedCheckboxText.nth(j).innerText().replace("\u00A0", "").trim();
                }
            }
            assertEquals(expect, actual);
            System.out.println(expect + " =" +actual);
            actual = "";
        }
    }
}