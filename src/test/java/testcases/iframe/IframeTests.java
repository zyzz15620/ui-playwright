package testcases.iframe;

import com.microsoft.playwright.FrameLocator;
import org.junit.jupiter.api.Test;
import testcases.BaseTest;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class IframeTests extends BaseTest {
    String url = "https://test-with-me-app.vercel.app/learning/web-elements/frames";

    @Test
    void verifyIframe(){
        page.navigate(url);
        FrameLocator iframe = page.frameLocator("//iframe[@title='Example Frame']");
        assertThat(iframe.getByText("Welcome to Test With Me")).isVisible();
        assertThat(iframe.getByText("Java for Tester")).isVisible();
        page.getByText("©2024 Test With Me aka Tho Test").isVisible();
    }
}
