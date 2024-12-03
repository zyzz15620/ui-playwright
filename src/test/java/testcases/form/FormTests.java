package testcases.form;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import testcases.BaseTest;

import java.util.*;
import java.util.stream.Stream;

import static data.FormTestData.FORM_VALIDATION_DATA;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FormTests extends BaseTest {
    String url = "https://test-with-me-app.vercel.app/learning/web-elements/form";

    static Stream<?> formValidationData() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Map<String, String>>> dataList = mapper.readValue(FORM_VALIDATION_DATA, new TypeReference<>() {
        });
        return dataList.stream();
    }
    @ParameterizedTest
    @MethodSource("formValidationData")
    void verifyForm(Map<String, Map<String, String>> data) throws InterruptedException {
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
            String errorMessage = String.format("(//div[@role='alert'][.//preceding::div//label[contains(text(),'%s')]])[1]", fieldData.getKey()); //vì lý do nào đó mà xpath này chạy lấy sai element bên máy a Dương, nó lấy error message của một field khác
            String errorMessageMoreStable = String.format("//div[contains(concat(' ',normalize-space(@class),' '),' ant-form-item ') and .//label[.//text()[normalize-space()='%s']]]//div[@role='alert']", fieldData.getKey()); //Xpath này đảm bảo hơn
            expectedError.put(fieldData.getKey(), fieldData.getValue().get("expected"));
            actualError.put(fieldData.getKey(), page.locator(errorMessage).textContent());
        }
        assertEquals(expectedError, actualError);
    }
}
