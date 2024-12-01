package testcases.components;

import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.Test;
import testcases.BaseTest;

import java.util.ArrayList;
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

        //Move to right
        String inputToRight = "Apple > Banane";
        String[] itemMoveToRight = inputToRight.split("\\s*>\\s*");
        for(String item : itemMoveToRight){
            String itemXpath = String.format("(//div[.//text()[normalize-space()='Drag n Drop']]//following::div[contains(concat(' ',normalize-space(@class),' '),' ant-space ')])[1]//button[.//text()[normalize-space()='%s']]", item);
            page.locator(itemXpath).dragTo(rightPanelLocator);
        }

        //Assert Left
        List<String> expectedCurLeftItems = List.of("Orange", "Peach");
        List<String> actualCurLeftItems = leftPanelLocator.locator("//button").allTextContents();
        assertTrue(expectedCurLeftItems.containsAll(actualCurLeftItems));
        assertTrue(actualCurLeftItems.containsAll(expectedCurLeftItems));

        //Assert Right
        List<String> expectedCurRightItems = List.of("Strawberry", "Mango", "Pineapple", "Grapes", "Apple", "Banane");
        List<String> actualCurRightItems = rightPanelLocator.locator("//button").allTextContents();
        assertTrue(expectedCurRightItems.containsAll(actualCurRightItems));
        assertTrue(actualCurRightItems.containsAll(expectedCurRightItems));

        //Move to left
        String inputToLeft = "Mango > Grapes";
        String[] itemMoveToLeft = inputToLeft.split("\\s*>\\s*");
        for(String item : itemMoveToLeft){
            String itemXpath = String.format("(//div[.//text()[normalize-space()='Drag n Drop']]//following::div[contains(concat(' ',normalize-space(@class),' '),' ant-space ')])[1]//button[.//text()[normalize-space()='%s']]", item);
            page.locator(itemXpath).dragTo(leftPanelLocator);
        }

        //Assert Left
        expectedCurLeftItems = List.of("Orange", "Peach", "Mango", "Grapes");
        actualCurLeftItems = leftPanelLocator.locator("//button").allTextContents();
        assertTrue(expectedCurLeftItems.containsAll(actualCurLeftItems));
        assertTrue(actualCurLeftItems.containsAll(expectedCurLeftItems));

        //Assert Right
        expectedCurRightItems = List.of("Strawberry", "Pineapple", "Apple", "Banane");
        actualCurRightItems = rightPanelLocator.locator("//button").allTextContents();
        assertTrue(expectedCurRightItems.containsAll(actualCurRightItems));
        assertTrue(actualCurRightItems.containsAll(expectedCurRightItems));
    }
}
