package testcases.components;

import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import testcases.BaseTest;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

class SliderTests extends BaseTest {
    @Test
    void verifySlider(){
        page.navigate("https://test-with-me-app.vercel.app/learning/web-elements/components/slider");
        String sliderBarXpath = "//div[contains(concat(' ',normalize-space(@class),' '),' ant-slider-rail ')]";
        String expectedLabelXpath = "//div[contains(text(), 'Current Value: ') and ./span[contains(concat(' ', normalize-space(@class),' '),' text-rose-500 ')]]";

        Locator sliderBar = page.locator(sliderBarXpath);
        Locator expectedLabel = page.locator(expectedLabelXpath);

        double input = 0.71;
        double sliderLength = sliderBar.boundingBox().width * input;

        page.mouse().move(sliderBar.boundingBox().x, sliderBar.boundingBox().y);
        page.mouse().down();
        page.mouse().move(sliderBar.boundingBox().x + sliderLength, sliderBar.boundingBox().y);
        page.mouse().up();

        assertThat(expectedLabel).hasText(String.format("Current Value: %s", Math.round(input*100)));
    }
}
