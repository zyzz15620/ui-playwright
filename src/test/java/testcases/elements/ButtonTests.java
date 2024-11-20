package testcases.elements;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import testcases.BaseTest;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

class ButtonTests extends BaseTest {
    String url = "https://test-with-me-app.vercel.app/learning/web-elements/elements/button";

    @Test
    void verifyNavigateSuccess() {
        page.navigate(url);
        String pageHeaderXpath = "//span[contains(concat(' ', normalize-space(@class), ' '), 'ant-divider-inner-text')][contains(text(), 'Common button type')]";
        assertThat(page).hasTitle("Test With Me aka Tho Test");
        assertThat(page.locator(pageHeaderXpath)).isVisible();
    }

    @ParameterizedTest
    @ValueSource(strings = {"Div button", "Origin button", "Input button", "Default", "Primary", "Dashed", "Text", "Link", "Icon button"})
    void shouldClickButton(String buttonLabelData){
        page.navigate(url);
        String clickedButtonXpath ="//div[contains(., 'Button') and contains(., 'was clicked!') and ./span[contains(concat(' ', normalize-space(@class), ' '), ' text-rose-500 ')]]";
        String normalButtonXpath = String.format("//button[normalize-space(.//text())='%s']", buttonLabelData);
        String tagButtonXpath = String.format("//*[@role='button' and normalize-space(.//text())='%s']", buttonLabelData);
        String typeButtonXpath = String.format("//input[@type='button' and normalize-space(.//@value)='%s']", buttonLabelData);
        String buttonXpath = String.format("%s | %s | %s", normalButtonXpath, tagButtonXpath, typeButtonXpath);
        Locator buttonLocator = page.locator(buttonXpath);
        buttonLocator.click();
        assertThat(page.locator(clickedButtonXpath)).hasText(String.format("Button %s was clicked!", buttonLabelData));
    }
}