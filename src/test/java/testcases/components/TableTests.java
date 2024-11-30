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
        assertEquals(10, addressList.length);

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

        String cellXpath = "//tbody[contains(concat(' ',normalize-space(@class),' '),' ant-table-tbody ')]//tr[@data-row-key='%d']//td[contains(concat(' ',normalize-space(@class),' '),' ant-table-cell ')][%d]";
        String rowsXpath = "//tbody[contains(concat(' ',normalize-space(@class),' '),' ant-table-tbody ')]//tr[contains(concat(' ',normalize-space(@class),' '),' ant-table-row ')]";
        Locator rows = page.locator(rowsXpath);
        ArrayList<Customer> actualCustomer = new ArrayList<>();
        String nextButtonXpath = "//li[contains(concat(' ',normalize-space(@class),' '),' ant-pagination-next ')]";
        for(int rowNum = 1; true ; rowNum++){
            if(page.locator(String.format(cellXpath, rowNum, nameIndex)).count() == 0){
                break;
            }

            Customer customer = new Customer();
            customer.setName(page.locator(String.format(cellXpath, rowNum, nameIndex)).innerText());
            customer.setAddress(page.locator(String.format(cellXpath, rowNum, addressIndex)).innerText());
            customer.setAge(Integer.parseInt(page.locator(String.format(cellXpath, rowNum, ageIndex)).innerText()));
            customer.setTags(page.locator(String.format(cellXpath, rowNum, tagsIndex)).innerText());
            actualCustomer.add(customer);

            if(rowNum%rows.count() == 0){
                Locator nextButton = page.locator(nextButtonXpath);
                boolean isNextButtonEnable = !Boolean.parseBoolean(nextButton.getAttribute("aria-disabled"));
                if(isNextButtonEnable){
                    nextButton.click();
                } else {
                    break;
                }
            }
        }

        ObjectMapper objectMapper = new ObjectMapper();
        List<Customer> expectedCustomers = objectMapper.readValue(CUSTOMERS_EXPECTED_DATA, new TypeReference<>() {}) ;

        boolean isActualContainAllExpected = actualCustomer.containsAll(expectedCustomers);
        assertTrue(isActualContainAllExpected);
        boolean isExpectedContainsAllActual = expectedCustomers.containsAll(actualCustomer);
        assertTrue(isExpectedContainsAllActual);
    }

    @Test
    void verifyTable2ndWay() throws InterruptedException, JsonProcessingException {
        page.navigate(url);
        Thread.sleep(2000);
        String tableXpath = "(//div[.//text()[normalize-space()='Table']]//following::table)[1]";  //This is just to make sure if there are 2 tables, this is better than the 1st way above
        String headersXpath = String.format("%s//th[contains(concat(' ',normalize-space(@class),' '),' ant-table-cell ')]", tableXpath);
        String[] headerList = page.locator(headersXpath).allInnerTexts().toArray(new String[0]);
        String rowsXpath = String.format("%s//tr[contains(concat(' ',normalize-space(@class),' '),' ant-table-row ')]", tableXpath);
        String nextButtonXpath = "//li[contains(concat(' ',normalize-space(@class),' '),' ant-pagination-next ')]";
        List<Customer> actualCustomer = new ArrayList<>();
        boolean isNextButtonEnable;

        do {
            List<Locator> rowLocators = page.locator(rowsXpath).all();
            for (Locator row : rowLocators) {
                Customer customer = new Customer();
//                int indexOfName = Arrays.asList(headerList).indexOf("Name");
//                String cellNameLocator = String.format("//td[contains(concat(' ',normalize-space(@class),' '),' ant-table-cell ')][%d]", indexOfName + 1);
//                String name = row.locator(cellNameLocator).textContent();
//                customer.setName(name);
//
//                int indexOfAge = Arrays.asList(headerList).indexOf("Age");
//                String cellAgeLocator = String.format("//td[contains(concat(' ',normalize-space(@class),' '),' ant-table-cell ')][%d]", indexOfAge + 1);
//                int age = Integer.parseInt(row.locator(cellAgeLocator).textContent());
//                customer.setAge(age);
//
//                int indexOfAddress = Arrays.asList(headerList).indexOf("Address");
//                String cellAddressLocator = String.format("//td[contains(concat(' ',normalize-space(@class),' '),' ant-table-cell ')][%d]", indexOfAddress + 1);
//                String address = row.locator(cellAddressLocator).textContent();
//                customer.setAddress(address);
//
//                int indexOfTags = Arrays.asList(headerList).indexOf("Tags");
//                String cellTagsLocator = String.format("//td[contains(concat(' ',normalize-space(@class),' '),' ant-table-cell ')][%d]", indexOfTags + 1);
//                String tags = row.locator(cellTagsLocator).textContent();
//                customer.setTags(tags);

                for(String header : headerList){
                    int indexOfHeader = Arrays.asList(headerList).indexOf(header);
                    String headerXpath = String.format("//td[contains(concat(' ',normalize-space(@class),' '),' ant-table-cell ')][%d]", indexOfHeader + 1);
                    String headerValue = row.locator(headerXpath).textContent();
                    switch (header){
                        case "Name":
                            customer.setName(headerValue);
                            break;
                        case "Age":
                            customer.setAge(Integer.parseInt(headerValue));
                            break;
                        case "Address":
                            customer.setAddress(headerValue);
                            break;
                        case "Tags":
                            customer.setTags(headerValue);
                            break;
                        default:
                            break;
                    }
                }

                actualCustomer.add(customer);
            }
            Locator nextButton = page.locator(nextButtonXpath);
            isNextButtonEnable = !Boolean.parseBoolean(nextButton.getAttribute("aria-disabled"));
            if(isNextButtonEnable){
                nextButton.click();
            }
        } while (isNextButtonEnable);

        ObjectMapper objectMapper = new ObjectMapper();
        List<Customer> expectedCustomers = objectMapper.readValue(CUSTOMERS_EXPECTED_DATA, new TypeReference<>() {}) ;

        boolean isActualContainAllExpected = actualCustomer.containsAll(expectedCustomers);
        assertTrue(isActualContainAllExpected);
        boolean isExpectedContainsAllActual = expectedCustomers.containsAll(actualCustomer);
        assertTrue(isExpectedContainsAllActual);
    }
    //So there is 2 ways to loop the rows
    //1st is to loop infinity until the next row is not exist, and go to the next page when (curRowIndex % numberOfRows == 0)
    //2nd way to loop each row in list<row> rows, and click next page when last row went through
}