package testcases.components;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import testcases.BaseTest;


import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

class ModalTests extends BaseTest {
    String url = "https://test-with-me-app.vercel.app/learning/web-elements/components/modal";
    @Test
    void verifyNavigateSuccess() {
        page.navigate(url);
        String pageHeaderXpath = "//span[contains(concat(' ', normalize-space(@class), ' '), 'ant-divider-inner-text')][contains(text(), 'Modal / Popup')]";
        assertThat(page).hasTitle("Test With Me aka Tho Test");
        assertThat(page.locator(pageHeaderXpath)).isVisible();
    }

    @ParameterizedTest
    @ValueSource(strings = {"Yes", "No"})
    void verifyTable(String input) {
        page.navigate(url);
        String showConfirmXpath = "//button[span[normalize-space(text())='Show Confirm']]";
        page.locator(showConfirmXpath).click();

        String optionXpath = String.format("//div[contains(concat(' ',normalize-space(@class),' '),' ant-modal-body ')]//button[span[normalize-space(text())='%s']]", input);
        page.locator(optionXpath).click();

        String actualLabelXpath = "//div[contains(., 'Status:')][span[contains(concat(' ', normalize-space(@class), ' '), ' text-rose-500 ')]]";
        assertThat(page.locator(actualLabelXpath)).hasText(String.format("Status: %s", input.equals("Yes")? "OK": input.equals("No")? "CANCEL" : ""));
        System.out.println();
    }
}