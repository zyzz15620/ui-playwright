package testcases.elements;

import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import testcases.BaseTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.stream.Stream;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DateTimeTests extends BaseTest {
    String timeXpath ="//ul[contains(concat(' ',normalize-space(@class),' '),' ant-picker-time-panel-column ')][@data-type=\"%s\"]//li[@data-value=\"%d\"]";
    String url = "https://test-with-me-app.vercel.app/learning/web-elements/elements/date-time";

    @Test
    void verifyNavigateSuccess() {
        page.navigate(url);
        assertThat(page).hasTitle("Test With Me aka Tho Test");
        assertThat(page.locator("//span[normalize-space(text())=\"Time Picker\"]")).isVisible();
    }

    static Stream<Arguments> timeProvider(){
        return Stream.of(
                Arguments.of(23, 30, 20)
        );
    }
    static Stream<Arguments> timeRangeProvider(){
        return Stream.of(
                Arguments.of(1, 30, 20, 23, 30, 20)
        );
    }
    void selectTime(int hour, int min, int sec){
        Locator hourOptions = page.locator(String.format(timeXpath, "hour" , hour));
        Locator minuteOptions = page.locator(String.format(timeXpath, "minute" , min));
        Locator secondOptions = page.locator(String.format(timeXpath, "second" , sec));
        hourOptions.click();
        minuteOptions.click();
        secondOptions.click();
    }
    static String getOrdinalSuffix(int number) {
        if (number >= 11 && number <= 13) {
            return "th"; // Các số 11, 12, 13 luôn kết thúc bằng "th"
        }
        switch (number % 10) {
            case 1: return "st";
            case 2: return "nd";
            case 3: return "rd";
            default: return "th";
        }
    }

    @ParameterizedTest
    @MethodSource("timeProvider")
    void verifyTimePicker(int hour, int min, int sec){
        page.navigate(url);
        Locator input = page.locator("//input[@placeholder=\"Select time\"]");
        Locator result = page.locator("//div[text()[normalize-space()=\"Current time:\"]]");

        input.click();
        selectTime(hour, min, sec);
        input.press("Enter");

        assertThat(result).hasText(String.format("Current time: %02d:%02d:%02d", hour, min, sec));
    }

    @Test
    void verifyNowButton() {
        page.navigate(url);
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

    @Test
    void verifyTodayButton(){
        page.navigate(url);
        Locator input = page.locator("//input[@placeholder=\"Select date\"]");
        Locator todayButton = page.locator("//a[contains(concat(' ',normalize-space(@class),' '),' ant-picker-now-btn ')][normalize-space(text())='Today']");
        Locator result = page.locator("//div[span[normalize-space(text())='Date Picker']]//following-sibling::div[text()[normalize-space()=\"Current date:\"]][1]");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("'Current date: 'yyyy-MM-dd");

        LocalDate beforeClick = LocalDate.now();
        input.click();
        todayButton.click();
        LocalDate actualResult = LocalDate.parse(result.innerText(), formatter);
        assertTrue(beforeClick.equals(actualResult));
    }

    @ParameterizedTest
    @MethodSource("timeRangeProvider")
    void verifyTimeRangePicker(int startHour, int startMin, int startSec, int endHour, int endMin, int endSec){
        page.navigate(url);
        Locator input = page.locator("//input[@placeholder=\"Start time\"]");
        Locator result = page.locator("//div[text()[normalize-space()=\"Current time range:\"]]");
        Locator okButton = page.locator("//ul[contains(concat(' ',normalize-space(@class),' '),' ant-picker-ranges ')]//button[span[normalize-space(text())='OK']]");

        input.click();
        selectTime(startHour, startMin, startSec);
        okButton.click();
        selectTime(endHour, endMin, endSec);
        okButton.click();

        assertThat(result).hasText(String.format("Current time range: %02d:%02d:%02d - %02d:%02d:%02d", startHour, startMin, startSec, endHour, endMin, endSec));
    }

    @Test
    void verifyDatePicker(){
        page.navigate(url);
        int day = 1;
        Locator selectDate = page.locator("//input[@placeholder=\"Select date\"]");
        Locator dateOption = page.locator(String.format("(//div[contains(concat(' ',normalize-space(@class),' '),' ant-picker-date-panel ')]" +
                "//td[div[normalize-space()='%d']])[1]", day));
        Locator result = page.locator("//div[span[normalize-space(text())='Date Picker']]//following-sibling::div[text()[normalize-space()=\"Current date:\"]][1]");
        selectDate.click();
        dateOption.click();
        LocalDate currentMonthYear = LocalDate.now();
        assertThat(result).hasText(String.format("Current date: %04d-%02d-%02d", currentMonthYear.getYear(), currentMonthYear.getMonthValue(), day));
    }

    @Test
    void verifyWeekPicker(){
        page.navigate(url);
        Locator selectWeek = page.locator("//input[@placeholder=\"Select week\"]");
        Locator weekOption = page.locator(String.format("(//div[contains(concat(' ',normalize-space(@class),' '),' ant-picker-week-panel ')]//td[div[normalize-space()='%d']])[1]", LocalDate.now().getDayOfMonth()));
        Locator result = page.locator("//div[span[normalize-space(text())='Date Picker']]//following-sibling::div[text()[normalize-space()=\"Current date:\"]][1]");
        selectWeek.click();
        weekOption.click();
        LocalDate currentDate = LocalDate.now();
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int weekOfYear = currentDate.get(weekFields.weekOfYear());
        String weekOfYearWithOrdinalSuffix = String.format("%d%s", weekOfYear, getOrdinalSuffix(weekOfYear));
        assertThat(result).hasText(String.format("Current date: %04d-%s", currentDate.getYear(),weekOfYearWithOrdinalSuffix ));
    }
}