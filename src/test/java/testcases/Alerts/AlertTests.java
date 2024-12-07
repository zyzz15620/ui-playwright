package testcases.Alerts;

import com.microsoft.playwright.Dialog;
import org.junit.jupiter.api.Test;
import testcases.BaseTest;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AlertTests extends BaseTest {
    String url = "https://test-with-me-app.vercel.app/learning/web-elements/alerts";

    void clickButtonByLabel(String label){
        String xpath = String.format("//button[.//text()[normalize-space()='%s']]", label);
        page.locator(xpath).click();
    }
    @Test
    void verifyAlertDemo(){
        page.navigate(url);
        page.onDialog(Dialog::accept);
        clickButtonByLabel("Show Alert");
        System.out.println();
    }

    @Test
    void verifyAlertConfirm(){
        page.navigate(url);
        String actualResult ="//div[.//text()[normalize-space()='Selected value:']]";
        page.onDialog(Dialog::dismiss);
        clickButtonByLabel("Show Confirm");
        assertThat(page.locator(actualResult)).hasText("Selected value: Cancel");
    }

    @Test
    void verifyAlertPrompt(){
        page.navigate(url);
        String input = "abc";
        String actualResult = "//div[.//text()[normalize-space()='Entered value:']]";
        page.onDialog(dialog -> dialog.accept(input));
        clickButtonByLabel("Show Prompt");
        assertThat(page.locator(actualResult)).hasText(String.format("Entered value: %s", input));
        System.out.println();
    }

    @Test
    void verifyAlertPromptEmptyCase(){ //2 alert box if empty
        page.navigate(url);
        String input = "";
        String actualResult = "//div[.//text()[normalize-space()='Entered value:']]";
//        page.onDialog(dialog -> dialog.accept(input)); //this will automatically accept 2 alert box but what we want is to assert the content of 2nd alert box
        page.onDialog(dialog -> {
            if("Please enter your name:".equals(dialog.message())){
                dialog.accept(input);
            } else {
                assertEquals("", dialog.message());
                dialog.accept();
            }
        });
        clickButtonByLabel("Show Prompt");
        assertThat(page.locator(actualResult)).hasText("Entered value:");
        System.out.println();
    }
}
