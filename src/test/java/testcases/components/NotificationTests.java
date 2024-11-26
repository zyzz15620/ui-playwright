package testcases.components;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import testcases.BaseTest;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

class NotificationTests extends BaseTest {
    String url = "https://test-with-me-app.vercel.app/learning/web-elements/components/notification";
    @Test
    void verifyNavigateSuccess() {
        page.navigate(url);
        String pageHeaderXpath = "//span[contains(concat(' ', normalize-space(@class), ' '), 'ant-divider-inner-text')][contains(text(), 'Modal / Popup')]";
        assertThat(page).hasTitle("Test With Me aka Tho Test");
        assertThat(page.locator(pageHeaderXpath)).isVisible();
    }

    @ParameterizedTest
    @ValueSource(strings = {"Success", "Info"})
    void verifyNotification(String input) {
        page.navigate(url);
        String notificationButton = String.format("//div[contains(concat(' ',normalize-space(@class),' '),' ant-space-item ')]//button[span[normalize-space(text())='%s']]", input);
        page.locator(notificationButton).click();
        page.pause();
    }
}