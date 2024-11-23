package testcases.components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import testcases.BaseTest;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class TransferTests extends BaseTest {
    @Test
    void verifyTransfer(){
        page.navigate("https://test-with-me-app.vercel.app/learning/web-elements/components/transfer");

        //move to right
        String[] inputs  = {"Apple", "Banana"};
        moveItemToPanel(inputs, "right");
        //Get actual and prepare expected for assertion
        String itemsInRightPanel = "//span[contains(concat(' ',normalize-space(@class),' '),' ant-transfer-list-header-title ') and normalize-space(text())='Target']/..//following-sibling::div//li[contains(concat(' ',normalize-space(@class),' '),' ant-transfer-list-content-item ')]";
        String[] rightItems = page.locator(itemsInRightPanel).allInnerTexts().toArray(new String[0]);
        String[] rightPanelExpected = {"Apple", "Banana", "Orange", "Pineapple", "Strawberry"};
        assertArrayEquals(rightItems, rightPanelExpected);

        //move back to left
        String[] moveToLeftInputs  = {"Apple", "Banana"};
        moveItemToPanel(moveToLeftInputs, "left");
        //Get actual and prepare expected for assertion
        String itemsInLeftPanel = "//span[contains(concat(' ',normalize-space(@class),' '),' ant-transfer-list-header-title ') and normalize-space(text())='Source']/..//following-sibling::div//li[contains(concat(' ',normalize-space(@class),' '),' ant-transfer-list-content-item ')]";
        String[] leftItems = page.locator(itemsInLeftPanel).allInnerTexts().toArray(new String[0]);
        String[] leftPanelExpected = {"Apple", "Banana", "Kiwi"};
        assertArrayEquals(leftItems, leftPanelExpected);
    }

    void moveItemToPanel(String[] inputs, String side){
        //side = 'left' or 'right'
        String itemXpath = "//li[contains(concat(' ',normalize-space(@class),' '),' ant-transfer-list-content-item ') and normalize-space(.//text())='%s']";
        for(String input : inputs){
            page.locator(String.format(itemXpath, input)).click();
        }
        String moveToPanelXpath = String.format("//div[contains(concat(' ',normalize-space(@class),' '),' ant-transfer-operation ')]//button[.//span[@aria-label='%s']]", side);
        page.locator(moveToPanelXpath).click();
    }
}
