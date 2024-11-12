package testcases.elements;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import testcases.BaseTest;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

class ButtonTests extends BaseTest {
    Locator pageHeader;
    Locator clickedButton;
    Locator buttons;

    @BeforeEach
    void createContextAndPage() {
        context = browser.newContext();
        page = context.newPage();
        page.navigate("https://test-with-me-app.vercel.app/learning/web-elements/elements/button");

        pageHeader = page.locator("//span[contains(concat(' ', normalize-space(@class), ' '), 'ant-divider-inner-text')][contains(text(), 'Common button type')]");
        clickedButton = page.locator("//div[contains(., 'Button') and contains(., 'was clicked!') and ./span[contains(concat(' ', normalize-space(@class), ' '), ' text-rose-500 ')]]");
        buttons = page.locator("//div[contains(concat(' ',normalize-space(@class),' '),' ant-flex css-vryruh ant-flex-wrap-wrap ant-flex-align-center ant-flex-gap-small ')]/*");
    }

    @Test
    void verifyNavigateSuccess() {
        assertThat(page).hasTitle("Test With Me aka Tho Test");
        assertThat(pageHeader).isVisible();
    }

    @Test
    void verifyButtonFunction(){ //vấn đề của cách này là fail một cái thì nó break hết nên là chưa hay
        for(int i = 0; i < buttons.count(); i++){
            buttons.nth(i).click();
            String buttonValue = buttons.nth(i).getAttribute("value");
            String buttonText = buttons.nth(i).innerText();

            if(buttonValue != null){
                assertThat(clickedButton).hasText(buttonValue);
            } else {
                assertThat(clickedButton).hasText(buttonText);
            }
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"Div button", "Origin button", "Input button", "Default", "Primary", "Dashed", "Text", "Link", "Icon button"})
    void shouldClickButton(String buttonLabelData){
        String buttonLabel = buttonLabelData;
        String normalButtonXpath = String.format("//button[normalize-space(.//text())='%s']", buttonLabel);
        String tagButtonXpath = String.format("//*[@role='button' and normalize-space(.//text())='%s']", buttonLabel);
        String typeButtonXpath = String.format("//input[@type='button' and normalize-space(.//@value)='%s']", buttonLabel);
        String buttonXpath = String.format("%s | %s | %s", normalButtonXpath, tagButtonXpath, typeButtonXpath);
        Locator buttonLocator = page.locator(buttonXpath);
        buttonLocator.click();
        assertThat(clickedButton).hasText(String.format("Button %s was clicked!", buttonLabel));
    }
}