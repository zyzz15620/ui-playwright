package testcases.elements;

import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import testcases.BaseTest;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RadioTests extends BaseTest {
    @BeforeEach
    void createContextAndPage() {
        context = browser.newContext();
        page = context.newPage();
        page.navigate("https://test-with-me-app.vercel.app/learning/web-elements/elements/radio");

    }

    @Test
    void verifyNavigateSuccess() {
        Locator pageHeader = page.locator("//span[contains(concat(' ', normalize-space(@class), ' '), 'ant-divider-inner-text')][contains(text(), 'Radio button')]");
        assertThat(page).hasTitle("Test With Me aka Tho Test");
        assertThat(pageHeader).isVisible();
    }

    @ParameterizedTest
    @ValueSource(strings = "Apple")
    void verifyRadio1(String inputData){
        String buttonXpath = String.format("//input[contains(concat(' ',normalize-space(@class),' '),' ant-radio-input ')][./..//following-sibling::span[normalize-space(text())=\"%s\"]]", inputData);
        Locator button = page.locator(buttonXpath);
        Locator buttons = page.locator("//input[contains(concat(' ',normalize-space(@class),' '),' ant-radio-input ')]/..//following-sibling::span[text()]");
        Locator result = page.locator("//div[normalize-space(text())=\"Value:\"][1]");

        button.check();

        assertThat(result).hasText(String.format("Value: %s", inputData));
        assertTrue(onlyOneButtonIsChecked(buttons));
    }

    boolean onlyOneButtonIsChecked(Locator buttons){
        int checkedCount = 0;
        for(int i = 0; i < buttons.count(); i++){
            if(buttons.nth(i).isChecked()){
                checkedCount += 1;
            }
        }
        return checkedCount == 1;
    }

}