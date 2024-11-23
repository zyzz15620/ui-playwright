package testcases.components;

import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.Test;
import testcases.BaseTest;
import java.util.Arrays;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TableTests extends BaseTest {
    String url = "https://test-with-me-app.vercel.app/learning/web-elements/components/table";
    @Test
    void verifyNavigateSuccess() {
        page.navigate(url);
        String pageHeaderXpath = "//span[contains(concat(' ', normalize-space(@class), ' '), 'ant-divider-inner-text')][contains(text(), 'Table')]";
        assertThat(page).hasTitle("Test With Me aka Tho Test");
        assertThat(page.locator(pageHeaderXpath)).isVisible();
    }

    @Test
    void verifyTable() throws InterruptedException {
        page.navigate(url);
        //Verify number of columns (table header)
        Thread.sleep(2000);
        String columnsXpath = "//thead[contains(concat(' ',normalize-space(@class),' '),' ant-table-thead ')]" +
                "//th[contains(concat(' ',normalize-space(@class),' '),' ant-table-cell ')]";
        String[] columnsLabelList = page.locator(columnsXpath).allInnerTexts().toArray(new String[0]);
        int indexOfAddress = Arrays.asList(columnsLabelList).indexOf("Address");
        String[] expectedColumnsLabelList = { "Tags", "Age", "Address", "Name", "Action" };
        Arrays.sort(expectedColumnsLabelList);
        Arrays.sort(columnsLabelList);
        assertArrayEquals(columnsLabelList, expectedColumnsLabelList);

        //Verify number of rows
        String addressXpath = String.format("//tbody[contains(concat(' ',normalize-space(@class),' '),' ant-table-tbody ')]" +
                "//tr//td[contains(concat(' ',normalize-space(@class),' '),' ant-table-cell ')][%d]", indexOfAddress+1);
        Locator addresses = page.locator(addressXpath);
        String[] addressList = addresses.allInnerTexts().toArray(new String[0]);
        assertTrue(addressList.length == 10);

        //Verify pagination
        String paginationXpath = "//li[contains(concat(' ',normalize-space(@class),' '),' ant-pagination-item ')]";
        int numberOfPage = page.locator(paginationXpath).count();
        page.locator(paginationXpath).nth(numberOfPage-1).click();
        String[] addressListLastPage = addresses.allInnerTexts().toArray(new String[0]);
        assertFalse(Arrays.equals(addressList, addressListLastPage));
    }
}