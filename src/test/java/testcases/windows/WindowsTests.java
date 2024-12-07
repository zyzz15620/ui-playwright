package testcases.windows;

import com.microsoft.playwright.Page;
import org.junit.jupiter.api.Test;
import testcases.BaseTest;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class WindowsTests extends BaseTest {
    String url = "https://test-with-me-app.vercel.app/learning/web-elements/windows";

    void clickButtonByLabel(String label){
        String xpath = String.format("//button[normalize-space(.//text())='%s']", label);
        page.locator(xpath).click();
    }

    @Test
    void verifyWindow(){
        page.navigate(url);
        Page newPage = context.waitForPage(()->{
            clickButtonByLabel("Open New Tab");
        });
        assertThat(newPage.getByText("The best place to learn about Software Testing")).isVisible();
    }

}
