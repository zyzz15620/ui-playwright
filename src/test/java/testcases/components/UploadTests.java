package testcases.components;

import com.microsoft.playwright.FileChooser;
import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.Test;
import testcases.BaseTest;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class UploadTests extends BaseTest {
    @Test
    void verifyUploadFile() {
        page.navigate("https://test-with-me-app.vercel.app/learning/web-elements/components/upload");
        String[] inputUploadListLabel = {"image1.png", "image2.png", "JMeter_QuickStart_Guide.pdf"};

        //Method 1 if there are no input node
        String clickToUploadButtonXpath = "//button[span[normalize-space(text())='Click to Upload']]";
        FileChooser fileChooser = page.waitForFileChooser(() -> page.locator(clickToUploadButtonXpath).click());
        fileChooser.setFiles(Paths.get(inputUploadListLabel[0]));

        //Method 2 using input node
        String clickToUploadXpath = "//button[span[normalize-space(text())='Click to Upload']]//preceding-sibling::input[@type='file']";
        page.locator(clickToUploadXpath).setInputFiles(Paths.get(inputUploadListLabel[1]));
        page.locator(clickToUploadXpath).setInputFiles(Paths.get(inputUploadListLabel[2]));

        //Get actual upload list
        String actualUploadListXpath = "//div[contains(concat(' ',normalize-space(@class),' '),' ant-upload-list ')]//span[contains(concat(' ',normalize-space(@class),' '),' ant-upload-list-item-name ')]";
        Locator actualUploadList = page.locator(actualUploadListXpath);
        String[] actualUploadListLabel = new String[actualUploadList.count()];
        for (int i = 0; i < actualUploadList.count(); i++) {
            actualUploadListLabel[i] = actualUploadList.nth(i).innerText();
        }

        assertArrayEquals(inputUploadListLabel, actualUploadListLabel);
    }
}
