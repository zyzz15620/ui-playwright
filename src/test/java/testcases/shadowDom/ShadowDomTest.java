package testcases.shadowDom;

import org.junit.jupiter.api.Test;
import testcases.BaseTest;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class ShadowDomTest extends BaseTest {
    String url = "https://test-with-me-app.vercel.app/learning/web-elements/shadow-dom";

    //Lưu ý nếu Shadow DOM mà close thì xpath ko thể navigate tới element trong shadow dom, mà chỉ dùng get by label hay text, id...
    @Test
    void verifyShadowDom(){
        page.navigate(url);
        String inputCss = "#name-input";
        page.locator(inputCss).fill("Test With Me");
        String buttonSubmitCss = "#shadow-btn";
        page.locator(buttonSubmitCss).click();
        String actualCss = "#name-display";
        assertThat(page.locator(actualCss)).hasText("Test With Me");
    }
}
