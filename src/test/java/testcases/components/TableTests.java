package testcases.components;

import com.microsoft.playwright.Locator;
import model.Customer;
import org.junit.jupiter.api.Test;
import testcases.BaseTest;

import java.util.ArrayList;
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
    void verifyTableWrongWay() throws InterruptedException {
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

    @Test
    void verifyTable() throws InterruptedException {
        page.navigate(url);
        Thread.sleep(2000);
        String headersXpath = "//thead[contains(concat(' ',normalize-space(@class),' '),' ant-table-thead ')]" +
                "//th[contains(concat(' ',normalize-space(@class),' '),' ant-table-cell ')]";
        String[] headerList = page.locator(headersXpath).allInnerTexts().toArray(new String[0]);
        int nameIndex = Arrays.asList(headerList).indexOf("Name")+1;
        int actionIndex = Arrays.asList(headerList).indexOf("Action")+1;
        int addressIndex = Arrays.asList(headerList).indexOf("Address")+1;
        int ageIndex = Arrays.asList(headerList).indexOf("Age")+1;
        int tagsIndex = Arrays.asList(headerList).indexOf("Tags")+1;

        String cellBodyXpath = "//tbody[contains(concat(' ',normalize-space(@class),' '),' ant-table-tbody ')]//tr[@data-row-key='%d']//td[contains(concat(' ',normalize-space(@class),' '),' ant-table-cell ')][%d]";
        String rowsXpath = "//tbody[contains(concat(' ',normalize-space(@class),' '),' ant-table-tbody ')]//tr[contains(concat(' ',normalize-space(@class),' '),' ant-table-row ')]";
        int rowCount = page.locator(rowsXpath).count();

        ArrayList<Customer> customerArrayList = new ArrayList<>();
        for(int rowNum = 1; rowNum <= rowCount; rowNum++){
            Customer customer = new Customer();
            customer.setName(page.locator(String.format(cellBodyXpath, rowNum, nameIndex)).innerText());
            customer.setAddress(page.locator(String.format(cellBodyXpath, rowNum, addressIndex)).innerText());
            customer.setAge(page.locator(String.format(cellBodyXpath, rowNum, ageIndex)).innerText());
            customer.setTags(page.locator(String.format(cellBodyXpath, rowNum, tagsIndex)).innerText());
            customer.setActions(page.locator(String.format(cellBodyXpath, rowNum, actionIndex)).innerText());
            customerArrayList.add(customer);
        }

        for(Customer customer:customerArrayList){
            System.out.println(customer.toString());
        }
    }
}