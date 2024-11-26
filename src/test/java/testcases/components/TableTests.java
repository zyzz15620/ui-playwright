package testcases.components;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.Locator;
import model.Customer;
import org.junit.jupiter.api.Test;
import testcases.BaseTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static data.TableTestData.CUSTOMERS_EXPECTED_DATA;
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
    void verifyTable() throws InterruptedException, JsonProcessingException {
        page.navigate(url);
        Thread.sleep(2000);
        String headersXpath = "//thead[contains(concat(' ',normalize-space(@class),' '),' ant-table-thead ')]" +
                "//th[contains(concat(' ',normalize-space(@class),' '),' ant-table-cell ')]";
        String[] headerList = page.locator(headersXpath).allInnerTexts().toArray(new String[0]);
        int nameIndex = Arrays.asList(headerList).indexOf("Name")+1;
        int addressIndex = Arrays.asList(headerList).indexOf("Address")+1;
        int ageIndex = Arrays.asList(headerList).indexOf("Age")+1;
        int tagsIndex = Arrays.asList(headerList).indexOf("Tags")+1;

        String cellBodyXpath = "//tbody[contains(concat(' ',normalize-space(@class),' '),' ant-table-tbody ')]//tr[@data-row-key='%d']//td[contains(concat(' ',normalize-space(@class),' '),' ant-table-cell ')][%d]";
        String rowsXpath = "//tbody[contains(concat(' ',normalize-space(@class),' '),' ant-table-tbody ')]//tr[contains(concat(' ',normalize-space(@class),' '),' ant-table-row ')]";
        Locator rows = page.locator(rowsXpath);
        int rowCount = rows.count();
        ArrayList<Customer> actualCustomer = new ArrayList<>();
        String nextButtonXpath = "//li[contains(concat(' ',normalize-space(@class),' '),' ant-pagination-next ')]";
        for(int rowNum = 1; true ; rowNum++){
            if(page.locator(String.format(cellBodyXpath, rowNum, nameIndex)).count() == 0){
                break;
            }

            Customer customer = new Customer();
            customer.setName(page.locator(String.format(cellBodyXpath, rowNum, nameIndex)).innerText());
            customer.setAddress(page.locator(String.format(cellBodyXpath, rowNum, addressIndex)).innerText());
            customer.setAge(page.locator(String.format(cellBodyXpath, rowNum, ageIndex)).innerText());
            customer.setTags(page.locator(String.format(cellBodyXpath, rowNum, tagsIndex)).innerText());
            actualCustomer.add(customer);

            if(rowNum%rows.count() == 0){
                Locator nextButton = page.locator(nextButtonXpath);
                boolean isNext = !Boolean.parseBoolean(nextButton.getAttribute("aria-disabled"));
                if(isNext){
                    nextButton.click();
                    rowCount = rowCount + rows.count();
                } else {
                    break;
                }
            }
        }

        ObjectMapper objectMapper = new ObjectMapper();
        List<Customer> expectedCustomers = objectMapper.readValue(CUSTOMERS_EXPECTED_DATA, new TypeReference<List<Customer>>() {}) ;

        boolean isActualContainAllExpected = actualCustomer.containsAll(expectedCustomers);
        assertTrue(isActualContainAllExpected);
        boolean isExpectedContainsAllActual = expectedCustomers.containsAll(actualCustomer);
        assertTrue(isExpectedContainsAllActual);
    }

    @Test
    void verifyTable2ndWay() throws InterruptedException {
        page.navigate(url);
        Thread.sleep(2000);
        String headersXpath = "//thead[contains(concat(' ',normalize-space(@class),' '),' ant-table-thead ')]" +
                "//th[contains(concat(' ',normalize-space(@class),' '),' ant-table-cell ')]";
        String[] headerList = page.locator(headersXpath).allInnerTexts().toArray(new String[0]);
        String cellXpath = "";
        String rowDataXpath = "//tbody[contains(concat(' ',normalize-space(@class),' '),' ant-table-tbody ')]//tr[contains(concat(' ',normalize-space(@class),' '),' ant-table-row ')]";

        List<Locator> rowLocators = page.locator(rowDataXpath).all();
        List<Customer> customers = new ArrayList<>();
        for (Locator row : rowLocators){
            Customer customer = new Customer();
            int indexOfName = Arrays.asList(headerList).indexOf("Name");
            String cellNameLocator = String.format("", indexOfName);
            String name = row.locator(cellNameLocator).textContent();
            customer.setName(name);

            int indexOfAge = Arrays.asList(headerList).indexOf("Age");
            String cellAgeLocator = String.format("", indexOfAge);
            String age = row.locator(cellAgeLocator).textContent();
            customer.setName(age);

            int indexOfAddress = Arrays.asList(headerList).indexOf("Address");
            String cellAddressLocator = String.format("", indexOfAddress);
            String address = row.locator(cellAddressLocator).textContent();
            customer.setName(address);

            int indexOfTags = Arrays.asList(headerList).indexOf("Tags");
            String cellTagsLocator = String.format("", indexOfTags);
            String tags = row.locator(cellTagsLocator).textContent();
            customer.setName(tags);

            customers.add(customer);
        }


        for(Customer customer:customers){
            System.out.println(customer.toString());
        }


        String nextButtonXpath = "";


    }
}