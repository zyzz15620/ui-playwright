package testcases.components;

import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.Test;
import testcases.BaseTest;

import java.util.Arrays;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DragAndDropTests extends BaseTest {
    String url = "https://test-with-me-app.vercel.app/learning/web-elements/components/drag-n-drop";
    @Test
    void verifyNavigateSuccess() {
        page.navigate(url);
        String pageHeaderXpath = "//span[contains(concat(' ', normalize-space(@class), ' '), 'ant-divider-inner-text')][contains(text(), 'Drag n Drop')]";
        assertThat(page).hasTitle("Test With Me aka Tho Test");
        assertThat(page.locator(pageHeaderXpath)).isVisible();
    }

    @Test
    void verifyDragAndDrop(){
        String input = "Apple > Banane > Orange > Mango";
        String[] inputs = input.split("\\s*>\\s*");
        page.navigate(url);
        String tablesXpath = "(//div[.//text()[normalize-space()='Drag n Drop']]//following::div[contains(concat(' ',normalize-space(@class),' '),' ant-space ')])[1]";
        String leftTableXpath = String.format("%s//div[contains(concat(' ',normalize-space(@class),' '),' ant-space-item ')][1]", tablesXpath);
        String rightTableXpath = String.format("%s//div[contains(concat(' ',normalize-space(@class),' '),' ant-space-item ')][2]", tablesXpath);
        List<String> expectedLeftTable = page.locator(leftTableXpath).locator("//button").allTextContents();
        List<String> expectedRightTable = page.locator(leftTableXpath).locator("//button").allTextContents();


        for(String item : inputs){
            String buttonXpath = String.format("%s//button[.//text()[normalize-space()='%s']]", tablesXpath, item);
            if(expectedLeftTable.contains(item)){
                //move to right
                expectedLeftTable.remove(item);
                page.locator(buttonXpath).hover();
                page.mouse().down();
                page.locator(rightTableXpath).hover();
                page.mouse().up();
                expectedRightTable.add(item);

            } else if (expectedRightTable.contains(item)) {
                //move to left
                expectedRightTable.remove(item);
                page.locator(buttonXpath).hover();
                page.mouse().down();
                page.locator(leftTableXpath).hover();
                page.mouse().up();
                expectedLeftTable.add(item);
            }
        }


        List<String> actualLeftTable = page.locator(leftTableXpath).locator("//button").allTextContents();
        List<String> actualRightTable = page.locator(rightTableXpath).locator("//button").allTextContents();

        assertTrue(expectedRightTable.equals(actualRightTable));
        assertTrue(expectedLeftTable.equals(actualLeftTable));
    }


    @Test
    void verifyDragAndDrop2ndWay() throws InterruptedException {
        page.navigate(url);
        String leftPanelXpath = "(//div[.//text()[normalize-space()='Drag n Drop']]//following::div[contains(concat(' ',normalize-space(@class),' '),' ant-space ')])[1]//div[contains(concat(' ',normalize-space(@class),' '),' ant-space-item ')][1]";
        String rightPanelXpath = "(//div[.//text()[normalize-space()='Drag n Drop']]//following::div[contains(concat(' ',normalize-space(@class),' '),' ant-space ')])[1]//div[contains(concat(' ',normalize-space(@class),' '),' ant-space-item ')][2]";
        Locator leftPanelLocator = page.locator(leftPanelXpath);
        Locator rightPanelLocator = page.locator(rightPanelXpath);
        List<String> curLeftItems = leftPanelLocator.locator("//button").allTextContents();
        List<String> curRightItems = rightPanelLocator.locator("//button").allTextContents();
        ///----------------------------------------------------------------------------///

        //Move to right
        String inputToRight = "Apple > Banane";
        String[] itemMoveToRight = inputToRight.split("\\s*>\\s*");
        verifyPanel(itemMoveToRight, leftPanelLocator, rightPanelLocator);
        //Verify Left Panel
        curLeftItems.removeAll(Arrays.asList(itemMoveToRight));
        verifyPanel(leftPanelLocator, curLeftItems);
        //Verify Right Panel
        curRightItems.addAll(Arrays.asList(itemMoveToRight));
        verifyPanel(rightPanelLocator, curRightItems);
        ///----------------------------------------------------------------------------///

        //Move to left
        String inputToLeft = "Mango > Grapes";
        String[] itemMoveToLeft = inputToLeft.split("\\s*>\\s*");
        verifyPanel(itemMoveToLeft, rightPanelLocator, leftPanelLocator);
        //Verify Left Panel
        curLeftItems.addAll(Arrays.asList(itemMoveToLeft));
        verifyPanel(leftPanelLocator, curLeftItems);
        //Verify Right Panel
        curRightItems.removeAll(Arrays.asList(itemMoveToLeft));
        verifyPanel(rightPanelLocator, curRightItems);

    }

    private static void verifyPanel(String[] itemToMove, Locator from, Locator to) {
        for(String item : itemToMove){
            String itemXpath = String.format("//button[.//text()[normalize-space()='%s']]", item);
            from.locator(itemXpath).dragTo(to);
        }
    }

    private static void verifyPanel(Locator panelLocator, List<String> expectedCurItems) {
        List<String> actualCurItems = panelLocator.locator("//button").allTextContents();
        assertTrue(expectedCurItems.containsAll(actualCurItems));
        assertTrue(actualCurItems.containsAll(expectedCurItems));
    }
}
