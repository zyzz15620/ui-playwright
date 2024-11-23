package testcases.components;

import com.microsoft.playwright.FileChooser;
import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.Test;
import testcases.BaseTest;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class UploadTests extends BaseTest {
    @Test
    void verifyUploadFile() {
        page.navigate("https://test-with-me-app.vercel.app/learning/web-elements/components/upload");
        String input = "image1.png, image2.png, JMeter_QuickStart_Guide.pdf";
        ArrayList<String> inputUploadListLabel = new ArrayList<>(Arrays.asList(input.split(",\\s*")));

        //Method 1 if there are no input node
        String clickToUploadButtonXpath = "//button[span[normalize-space(text())='Click to Upload']]";
        FileChooser fileChooser = page.waitForFileChooser(() -> page.locator(clickToUploadButtonXpath).click());
        fileChooser.setFiles(Paths.get(inputUploadListLabel.get(0)));

        //Method 2 using input node
        String clickToUploadXpath = "//button[span[normalize-space(text())='Click to Upload']]//preceding-sibling::input[@type='file']";
        page.locator(clickToUploadXpath).setInputFiles(Paths.get(inputUploadListLabel.get(1)));
        page.locator(clickToUploadXpath).setInputFiles(Paths.get(inputUploadListLabel.get(2)));

        //Get actual upload list and Assert
        String actualUploadListXpath = "//div[contains(concat(' ',normalize-space(@class),' '),' ant-upload-list ')]//span[contains(concat(' ',normalize-space(@class),' '),' ant-upload-list-item-name ')]";
        Locator actualUploadList = page.locator(actualUploadListXpath);
        int initialCount = actualUploadList.count();
        String[] actualUploadListLabel = actualUploadList.allInnerTexts().toArray(new String[0]);
        assertArrayEquals(inputUploadListLabel.toArray(), actualUploadListLabel);

        //Delete first and Assert again
        int itemToDelete = 1;
        String deleteButton = String.format("(%s)[%d]//following-sibling::span//button[@title='Remove file']", actualUploadListXpath, itemToDelete);
        page.locator(deleteButton).click();
        inputUploadListLabel.remove(itemToDelete-1);

        //DOM need time to refresh
        page.waitForCondition(() -> actualUploadList.count() < initialCount);
        String[] actualUploadListLabelAfterDelete = actualUploadList.allInnerTexts().toArray(new String[0]);
        assertArrayEquals(inputUploadListLabel.toArray(), actualUploadListLabelAfterDelete);
    }
}
