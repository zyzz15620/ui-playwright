package testcases.form;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import testcases.BaseTest;

import java.util.*;
import java.util.stream.Stream;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static data.FormTestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FormTests extends BaseTest {
    String url = "https://test-with-me-app.vercel.app/learning/web-elements/form";

    static Stream<?> formValidationData() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Map<String, String>>> dataList = mapper.readValue(FORM_VALIDATION_DATA, new TypeReference<>() {
        });
        return dataList.stream();
    }
    static Stream<?> formValidData() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, String>> dataList = mapper.readValue(FORM_VALID_DATA, new TypeReference<>() {
        });
        return dataList.stream();
    }

    @ParameterizedTest
    @MethodSource("formValidationData")
    void verifyFormInvalidCases(Map<String, Map<String, String>> data) throws InterruptedException {
        page.navigate(url);

        for(Map.Entry<String, Map<String, String>> fieldData : data.entrySet()){
            String inputXpath =String.format("(//div[.//text()[normalize-space()='Common button type']]//following::input[.//preceding::div//label[contains(text(),'%s')]])[1]", fieldData.getKey());
            page.locator(inputXpath).fill(fieldData.getValue().get("input"));
        }

        String submitButtonXpath = "//button[.//text()[normalize-space()='Submit']]";
        page.locator(submitButtonXpath).click();

        Map<String, String> expectedError = new HashMap<>();
        Map<String, String> actualError = new HashMap<>();
        for(Map.Entry<String, Map<String, String>> fieldData : data.entrySet()){
            String errorMessage = String.format("(//div[@role='alert'][.//preceding::div//label[contains(text(),'%s')]])[1]", fieldData.getKey()); //Chỗ này mới thấy dùng preceding/following rất nguy hiểm, ví dụ mình trigger cái errorMessage ở cái field khác bên dưới thì cái errorMessage đó là cái duy nhất mà vẫn đáp ứng được preceding nên có thể sẽ bị lấy lộn errorMessage nếu thứ tự input field ko theo trình tự từ trên xuống dưới
            String errorMessageMoreStable = String.format("//div[contains(concat(' ',normalize-space(@class),' '),' ant-form-item ') and .//label[.//text()[normalize-space()='%s']]]//div[@role='alert']", fieldData.getKey()); //Xpath này đảm bảo hơn
            expectedError.put(fieldData.getKey(), fieldData.getValue().get("expected"));
            actualError.put(fieldData.getKey(), page.locator(errorMessageMoreStable).textContent());
        }
        assertEquals(expectedError, actualError);
    }

    @ParameterizedTest
    @MethodSource("formValidData")
    void verifyFormValidCases(Map<String, String> data) throws InterruptedException {
        page.navigate(url);
        //Filling form and click Submit
        for(Map.Entry<String, String> entry : data.entrySet()){
            String inputXpath =String.format("(//div[.//text()[normalize-space()='Common button type']]//following::input[.//preceding::div//label[contains(text(),'%s')]])[1]", entry.getKey());
            page.locator(inputXpath).fill(entry.getValue());
        }
        String submitButtonXpath = "//button[.//text()[normalize-space()='Submit']]";
        page.locator(submitButtonXpath).click();

        //Assert Success Notification
        String notificationXpath = "//div[@role='alert' and //div[normalize-space(text())='Your application has been submitted successfully.']]";
        assertThat(page.locator(notificationXpath)).hasText(String.format("Application of \"%s\"Your application has been submitted successfully.", data.get("Full Name")));
        Thread.sleep(5000);
    }
}
