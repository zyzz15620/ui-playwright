package testcases.components;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import testcases.BaseTest;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

class PopConfirmTests extends BaseTest {
    String url = "https://test-with-me-app.vercel.app/learning/web-elements/components/pop-confirm";
    @Test
    void verifyNavigateSuccess() {
        page.navigate(url);
        String pageHeaderXpath = "//span[contains(concat(' ', normalize-space(@class), ' '), 'ant-divider-inner-text')][contains(text(), 'Modal / Popup')]";
        assertThat(page).hasTitle("Test With Me aka Tho Test");
        assertThat(page.locator(pageHeaderXpath)).isVisible();
    }

    @ParameterizedTest
    @ValueSource(strings = {"Yes", "No"})
    void verifyPop(String input) {
        page.navigate(url);
        String deleteButton = "//button[contains(concat(' ',normalize-space(@class),' '),' ant-btn-dangerous ')][span[normalize-space(text())='Delete']]";
        page.locator(deleteButton).click();

        String optionXpath = String.format("//div[contains(concat(' ',normalize-space(@class),' '),' ant-popconfirm-inner-content ') and .//div[normalize-space(text())='Delete the task']]" +
                "//button[.//text()[normalize-space()='%s']]", input);
        page.locator(optionXpath).click();

        assertThat(page.locator("//div[contains(concat(' ',normalize-space(@class),' '),' ant-message-notice-content ')]")).hasText(String.format("Click on %s", input));
    }
}