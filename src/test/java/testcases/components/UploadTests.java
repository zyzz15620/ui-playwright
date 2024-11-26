package testcases.components;

import com.microsoft.playwright.FileChooser;
import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.Test;
import testcases.BaseTest;

import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class UploadTests extends BaseTest {
    @Test
    void verifyUploadFile() {
        page.navigate("https://test-with-me-app.vercel.app/learning/web-elements/components/upload");
        String input = "image1.png, image2.png, JMeter_QuickStart_Guide.pdf";
        ArrayList<String> inputUploadListLabel = new ArrayList<>(Arrays.asList(input.split(",\\s*")));
        ClassLoader classLoader = getClass().getClassLoader();

        //Method 1 if there are no input node
        String clickToUploadButtonXpath = "//button[span[normalize-space(text())='Click to Upload']]";
        URL inputStream = classLoader.getResource(String.format("upload/%s", inputUploadListLabel.get(0)));
        FileChooser fileChooser = page.waitForFileChooser(() -> page.locator(clickToUploadButtonXpath).click());
        fileChooser.setFiles(Path.of(inputStream.getPath()));

        //Method 2 using input node
        String clickToUploadXpath = "//button[span[normalize-space(text())='Click to Upload']]//preceding-sibling::input[@type='file']";
        URL inputStream1 = classLoader.getResource(String.format("upload/%s", inputUploadListLabel.get(1)));
        URL inputStream2 = classLoader.getResource(String.format("upload/%s", inputUploadListLabel.get(2)));
        page.locator(clickToUploadXpath).setInputFiles(Path.of(inputStream1.getPath()));
        page.locator(clickToUploadXpath).setInputFiles(Path.of(inputStream2.getPath()));

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
