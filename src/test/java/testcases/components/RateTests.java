package testcases.components;

import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import testcases.BaseTest;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

class RateTests extends BaseTest {
    @BeforeEach
    void createContextAndPage() {
        context = browser.newContext();
        page = context.newPage();
        page.navigate("https://test-with-me-app.vercel.app/learning/web-elements/components/rating");
    }

    @ParameterizedTest
    @CsvSource({
            "1, terrible",
            "2, bad",
            "3, normal",
            "4, good",
            "5, wonderful"
    })
    void verifyRatingFullStar(int input, String expectedOutput){
        assertThat(page).hasTitle("Test With Me aka Tho Test");
        assertThat(page.locator("//span[normalize-space(text())=\"Rate\"]")).isVisible();
        String starXpath = String.format("//ul[@role='radiogroup'][1]//div[@aria-posinset='%d']", input);
        String nextStarXpath = String.format("//ul[@role='radiogroup'][1]//div[@aria-posinset='%d']", input+1);

        Locator starLocator = page.locator(starXpath);
        Locator nextStarLocator = page.locator(nextStarXpath);
        boolean isCurrentSelected = "true".equals(starLocator.getAttribute("aria-checked"));
        boolean isNextSelected = input<4 && "true".equals(nextStarLocator.getAttribute("aria-checked"));

        if(!isCurrentSelected || isNextSelected){
            page.locator(starXpath).click();
        }
        String expectedLabelXpath = "//div[contains(., 'Current rating: ') and ./span[contains(concat(' ', normalize-space(@class),' '),' text-rose-500 ')]][1]";
        assertThat(page.locator(expectedLabelXpath)).hasText(String.format("Current rating: %s", expectedOutput));
    }

    @ParameterizedTest
    @ValueSource(floats = {0.5f, 1, 1.5f, 2, 2.5f, 3, 3.5f, 4, 4.5f, 5})
    void verifyRatingHalfStar(float input){
        assertThat(page).hasTitle("Test With Me aka Tho Test");
        assertThat(page.locator("//span[normalize-space(text())=\"Rate\"]")).isVisible();
        boolean isHalf = input < Math.ceil(input);
        String starXpath = String.format("//div[span[contains(text(), 'Haft Rate')]]" +
                "//following-sibling::ul//div[@aria-posinset='%d']" +
                "//div[contains(concat(' ',normalize-space(@class),' '),' %s ')]",
                Math.round(Math.ceil(input)),
                isHalf? "ant-rate-star-first" : "ant-rate-star-second");

        String fullStarCountXpath = "//div[span[contains(text(), 'Haft Rate')]]//following-sibling::ul//li[contains(concat(' ',normalize-space(@class),' '),' ant-rate-star-full ')]";
        String halfStarCountXpath = "//div[span[contains(text(), 'Haft Rate')]]//following-sibling::ul//li[contains(concat(' ',normalize-space(@class),' '),' ant-rate-star-half ')]";
        float currentStarCount = page.locator(fullStarCountXpath).count() + page.locator(halfStarCountXpath).count()>0? 0.5f : 0;

        Locator starLocator = page.locator(starXpath);
        if(currentStarCount!=input){
            starLocator.click();
        }

        String expectedValue = String.valueOf(input);
        if(!isHalf){
            expectedValue = String.valueOf(Math.round(input));
        }
        String expectedLabelXpath = "//div[contains(., 'Current rating: ') and ./span[contains(concat(' ', normalize-space(@class),' '),' text-rose-500 ')]][2]";
        assertThat(page.locator(expectedLabelXpath)).hasText(String.format("Current rating: %s", expectedValue));

    }
}
