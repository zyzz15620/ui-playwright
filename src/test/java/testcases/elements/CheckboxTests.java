package testcases.elements;

import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CheckboxTests extends BaseTest {
    Locator pageHeader;
    Locator clickedButton;
    Locator buttons;

    @BeforeEach
    void createContextAndPage() {
        context = browser.newContext();
        page = context.newPage();

        pageHeader = page.locator("//span[contains(concat(' ', normalize-space(@class), ' '), 'ant-divider-inner-text')][contains(text(), 'Common button type')]");
        clickedButton = page.locator("//div[contains(text()[2], 'was clicked!')]//span[contains(concat(' ', normalize-space(@class), ' '), ' text-rose-500 ')]");
        buttons = page.locator("//div[contains(concat(' ',normalize-space(@class),' '),' ant-flex css-vryruh ant-flex-wrap-wrap ant-flex-align-center ant-flex-gap-small ')]/*");
    }

    @Test
    void verifyNavigateSuccess() {
        page.navigate("https://test-with-me-app.vercel.app/learning/web-elements/elements/button");
        assertThat(page).hasTitle("Test With Me aka Tho Test");
        assertThat(pageHeader).isVisible();
    }

    @Test
    void verifyButtonFunction(){
        page.navigate("https://test-with-me-app.vercel.app/learning/web-elements/elements/button");

        for(int i = 0; i < buttons.count(); i++){
            buttons.nth(i).click();
            String buttonValue = buttons.nth(i).getAttribute("value");
            String buttonText = buttons.nth(i).innerText();

            if(buttonValue != null){
                assertEquals(buttonValue, clickedButton.innerText());
            } else {
                assertEquals(buttonText, clickedButton.innerText());
            }
        }
    }
}