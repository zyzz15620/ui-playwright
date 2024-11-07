package testcases.elements;

import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

class InputTests extends BaseTest {
    @BeforeEach
    void createContextAndPage() {
        context = browser.newContext();
        page = context.newPage();
        page.navigate("https://test-with-me-app.vercel.app/learning/web-elements/elements/input");
    }

    @Test
    void verifyNavigateSuccess() {
        assertThat(page).hasTitle("Test With Me aka Tho Test");
        assertThat(page.locator("//span[normalize-space(text())=\"Normal Input\"]")).isVisible();
    }

    @ParameterizedTest
    @ValueSource(strings = {"Apple"})
    void verifyNormalInput(String inputData){
        Locator normalInput = page.locator("//input[normalize-space(@placeholder)=\"Hello!\"]");
        Locator result = page.locator("//div[.//text()[normalize-space() = \"Normal Input\"]][@role=\"separator\"]//following-sibling::div[contains(text(), \"Value:\")][1]");

        normalInput.fill(inputData);
        assertThat(result).hasText(String.format("Value: %s", inputData));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Apple"})
    void verifyTextArea(String inputData){
        Locator textArea = page.locator("//textarea[normalize-space(@placeholder)=\"Test with me!\"]");
        Locator result = page.locator("//div[.//text()[normalize-space() = \"Text Area\"]][@role=\"separator\"]//following-sibling::div[contains(text(), \"Value:\")][1]");

        textArea.fill(inputData);
        assertThat(result).hasText(String.format("Value: %s", inputData));
    }

    @ParameterizedTest
    @ValueSource(ints = {2})
    void verifyInputNumber(Integer inputData){
        Integer number = inputData;
        Locator inputNumber = page.locator("//input[normalize-space(@role)=\"spinbutton\"]");
        Locator increaseButton = page.locator("//span[@aria-label=\"Increase Value\"]");
        Locator decreaseButton = page.locator("//span[@aria-label=\"Decrease Value\"]");
        Locator result = page.locator("//div[.//text()[normalize-space() = \"Input Number\"]][@role=\"separator\"]//following-sibling::div[contains(text(), \"Value:\")][1]");

        inputNumber.fill(String.valueOf(number));
        assertThat(result).hasText(String.format("Value: %d", number));

        inputNumber.press("ArrowUp");
        inputNumber.press("ArrowUp");
        number = number + 10;
        assertThat(result).hasText(String.format("Value: %d", number));

        inputNumber.press("ArrowDown");
        number = number - 5;
        assertThat(result).hasText(String.format("Value: %d", number));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Apple"})
    void verifyPasswordBox(String inputData){
        Locator passwordBox = page.locator("//input[normalize-space(@placeholder)=\"Input password\"]");
        Locator result = page.locator("//div[.//text()[normalize-space() = \"Password Box\"]][@role=\"separator\"]//following-sibling::div[contains(text(), \"Value:\")][1]");
        Locator eyeToggle = page.locator("//span[contains(concat(' ',normalize-space(@class),' '),' ant-input-suffix ')]");

        passwordBox.fill(inputData);
        assertThat(result).hasText(String.format("Value: %s", inputData));

        assertThat(passwordBox).hasAttribute("type", "password");
        eyeToggle.click();
        assertThat(passwordBox).hasAttribute("type", "text");
    }

    @ParameterizedTest
    @ValueSource(ints = {222222})
    void verifyOtpBox(Integer inputData) {
        Integer number = inputData;
        Locator otpBox = page.locator("(//div[contains(.//text(), \"OTP Box\")]//following-sibling::div//input[@type=\"text\"])[1]");
        Locator result = page.locator("//div[.//text()[normalize-space() = \"OTP Box\"]][@role=\"separator\"]//following-sibling::div[contains(text(), \"Value:\")][1]");
        otpBox.fill(String.valueOf(inputData));
        assertThat(result).hasText(String.format("Value: %d", number));
    }
}