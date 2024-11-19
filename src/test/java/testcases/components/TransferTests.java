package testcases.components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import testcases.BaseTest;

class TransferTests extends BaseTest {
    @BeforeEach
    void createContextAndPage() {
        context = browser.newContext();
        page = context.newPage();
        page.navigate("https://test-with-me-app.vercel.app/learning/web-elements/components/cascader");
    }

    @Test
    void verifyMenu(){
        String menuXpath = "";
        String expectedLabelXpath = "";

    }
}
