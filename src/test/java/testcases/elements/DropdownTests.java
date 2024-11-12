package testcases.elements;

import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import testcases.BaseTest;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DropdownTests extends BaseTest {
    @BeforeEach
    void createContextAndPage() {
        context = browser.newContext();
        page = context.newPage();
        page.navigate("https://test-with-me-app.vercel.app/learning/web-elements/elements/dropdown");

    }

    @Test
    void verifyNavigateSuccess() {
        Locator pageHeader = page.locator("//span[contains(concat(' ', normalize-space(@class), ' '), 'ant-divider-inner-text')][contains(text(), 'Dropdown')]");
        assertThat(page).hasTitle("Test With Me aka Tho Test");
        assertThat(pageHeader).isVisible();
    }

    @ParameterizedTest
    @ValueSource(strings = "1st menu item")
    void verifyRadio1(String inputData){
        String buttonXpath = String.format("//li[contains(concat(' ',normalize-space(@class),' '),' ant-dropdown-menu-item ')][.//text()[normalize-space()=\"%s\"]]", inputData);
        Locator option = page.locator(buttonXpath);
        Locator hoverButton = page.locator("//button[.//span[@aria-label=\"ellipsis\"]]");
        Locator result = page.locator("//div[normalize-space(text())=\"Value:\"]");

        hoverButton.click();
        option.click();

        assertThat(result).hasText(String.format("Value: %s", inputData));
    }



}