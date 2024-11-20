package testcases.components;

import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import testcases.BaseTest;

import java.util.ArrayList;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

class CascaderTests extends BaseTest {


    @Test
    void verifyCascader(){ //bài này hay dùng nhiều ở các menu nhiều tầng như fptshop
        page.navigate("https://test-with-me-app.vercel.app/learning/web-elements/components/cascader");
        String inputs = "Test, With, Me";

        String cascaderXpath = "//div[normalize-space(.//text())='Cascader']//following::input[contains(concat(' ',normalize-space(@class),' '),' ant-select-selection-search-input ')][1]";
        page.locator(cascaderXpath).click();
        for(String itemString : inputs.split(",")){
            String itemXpath = String.format("(//li[@role='menuitemcheckbox' and .//text()[normalize-space()='%s']])[last()]", itemString.trim()); //dùng last() để đề phòng input là Tho Test UI vì có 2 cái "test" thì nó chỉ luôn dùng cái last
            page.locator(itemXpath).click();
        }
        String expectedLabelXpath = "//div[contains(., 'Current value: ') and ./span[contains(concat(' ', normalize-space(@class),' '),' text-rose-500 ')]]";
        assertThat(page.locator(expectedLabelXpath)).hasText(String.format("Current value: %s", inputs));
    }
}
