package testcases.components;

import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import testcases.BaseTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NotificationTests extends BaseTest {
    String url = "https://test-with-me-app.vercel.app/learning/web-elements/components/notification";
    @Test
    void verifyNavigateSuccess() {
        page.navigate(url);
        String pageHeaderXpath = "//span[contains(concat(' ', normalize-space(@class), ' '), 'ant-divider-inner-text')][contains(text(), 'Notification')]";
        assertThat(page).hasTitle("Test With Me aka Tho Test");
        assertThat(page.locator(pageHeaderXpath)).isVisible();
    }

    @ParameterizedTest
    @ValueSource(strings = {"Info", "Success", "Warning", "Error"})
    void verifyNotification(String input){
        page.navigate(url);
        String htmlBefore = page.locator("//body").innerHTML();
        String notificationButton = String.format("//div[.//text()[normalize-space()='Notification']]//following::div[contains(concat(' ',normalize-space(@class),' '),' ant-space-item ')]//button[span[normalize-space(text())='%s']]", input);
        page.locator(notificationButton).click();
        page.locator(notificationButton).click();

        String htmlAfter = page.locator("//body").innerHTML();

        Locator notification = page.locator("(//div[contains(concat(' ',normalize-space(@class),' '),' ant-notification-notice-content ')])[1]");
        String notificationHtml = notification.innerHTML();
        assertThat(notification).hasText(String.format("Notification %sYou have clicked the %s button.", input.toUpperCase(), input.toUpperCase()));
        assertTrue(notification.locator("//span[@role = 'img']").getAttribute("class")
                .contains(String.format("ant-notification-notice-icon-%s", input.toLowerCase())));
    }
}