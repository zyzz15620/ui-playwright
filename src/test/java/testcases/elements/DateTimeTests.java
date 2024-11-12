package testcases.elements;

import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DateTimeTests extends BaseTest {
    @BeforeEach
    void createContextAndPage() {
        context = browser.newContext();
        page = context.newPage();
        page.navigate("https://test-with-me-app.vercel.app/learning/web-elements/elements/date-time");
    }

    @Test
    void verifyNavigateSuccess() {
        assertThat(page).hasTitle("Test With Me aka Tho Test");
        assertThat(page.locator("//span[normalize-space(text())=\"Time Picker\"]")).isVisible();
    }

    @Test
    void verifyTimePicker(){
        Locator input = page.locator("//input[@placeholder=\"Select time\"]");
        Locator hourOptions = page.locator("//ul[@class=\"ant-picker-time-panel-column\"][@data-type=\"hour\"]//li[@data-value=\"1\"]");
        Locator minuteOptions = page.locator("//ul[@class=\"ant-picker-time-panel-column\"][@data-type=\"minute\"]//li[@data-value=\"1\"]");
        Locator secondOptions = page.locator("//ul[@class=\"ant-picker-time-panel-column\"][@data-type=\"second\"]//li[@data-value=\"1\"]");
        Locator result = page.locator("//div[text()[normalize-space()=\"Current time:\"]]");

        input.click();
        hourOptions.click();
        minuteOptions.click();
        secondOptions.click();
        input.press("Enter");
        assertThat(result).hasText("Current time: 01:01:01");
    }

    @Test
    void verifyNowButton() throws InterruptedException {
        Locator input = page.locator("//input[@placeholder=\"Select time\"]");
        Locator nowButton = page.locator("//a[normalize-space(text())=\"Now\"]");
        Locator result = page.locator("//div[text()[normalize-space()=\"Current time:\"]]");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("'Current time: 'HH:mm:ss");

        LocalTime beforeClick = LocalTime.now().minus(1, ChronoUnit.SECONDS);
        input.click();
        nowButton.click();
        LocalTime actualResult = LocalTime.parse(result.innerText(), formatter);
        LocalTime afterClick = LocalTime.now();

        assertTrue(beforeClick.isBefore(actualResult));
        assertTrue(afterClick.isAfter(actualResult));
    }
}