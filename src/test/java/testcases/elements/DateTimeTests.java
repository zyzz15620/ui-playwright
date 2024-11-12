package testcases.elements;

import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import testcases.BaseTest;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.stream.Stream;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DateTimeTests extends BaseTest {
    String timeXpath ="//ul[contains(concat(' ',normalize-space(@class),' '),' ant-picker-time-panel-column ')][@data-type=\"%s\"]//li[@data-value=\"%d\"]";
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

    static Stream<Arguments> timeProvider(){
        return Stream.of(
                Arguments.of(23, 30, 20)
        );
    }

    @ParameterizedTest
    @MethodSource("timeProvider")
    void verifyTimePicker(int hour, int min, int sec){
        Locator input = page.locator("//input[@placeholder=\"Select time\"]");
        Locator hourOptions = page.locator(String.format(timeXpath, "hour" , hour));
        Locator minuteOptions = page.locator(String.format(timeXpath, "minute" , min));
        Locator secondOptions = page.locator(String.format(timeXpath, "second" , sec));
        Locator result = page.locator("//div[text()[normalize-space()=\"Current time:\"]]");

        input.click();
        hourOptions.click();
        minuteOptions.click();
        secondOptions.click();
        input.press("Enter");

        assertThat(result).hasText(String.format("Current time: %02d:%02d:%02d", hour, min, sec));
    }

    @Test
    void verifyNowButton() {
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