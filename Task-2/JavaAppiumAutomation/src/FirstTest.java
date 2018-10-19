import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class FirstTest {

    private AppiumDriver driver;

    @Before
    public void setUp() throws Exception {

        DesiredCapabilities capabilites = new DesiredCapabilities();

        capabilites.setCapability("platformName", "Android");
        capabilites.setCapability("deviceName", "AndroidTestDevice");
        capabilites.setCapability("platformVersion", "8.0");
        capabilites.setCapability("automationName", "Appium");
        capabilites.setCapability("appPackage", "org.wikipedia");
        capabilites.setCapability("appActivity", ".main.MainActivity");
        capabilites.setCapability("app", "/Users/apalnov/Desktop/Auto Mobile/Lesson-3/Lesson_project/JavaAppiumAutomation/apks/org.wikipedia.apk"); // MacOS
        // capabilites.setCapability("app","E:\\Auto Mobile\\Lesson-2\\JavaAppiumAutomation\\apks\\org.wikipedia.apk"); // Windows

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilites);
    }

    @After
    public void tearDown() {
        driver.quit();
    }


    @Test
    public void testCancelSearch() {

        String searchString = "Navy";

        waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find 'search Wikipedia' input",
                5);

        waitForElementAndSendKeys(
                By.xpath("//*[contains(@text,'Search…')]"),
                searchString,
                "Cannot find search input",
                5);

        waitForElementPresent(
                By.id("org.wikipedia:id/page_list_item_title"),
                "Cannot find search results",
                5);

        ArrayList<WebElement> elements = searchForElements(By.id("org.wikipedia:id/page_list_item_title"));

//        if (elements.isEmpty()) { Assert.fail("No elements having " + searchString); }

        System.out.println("Search returned: " + elements.size() + " articles with following headers");

        for(WebElement element : elements) {

            String title = element.getText();
            System.out.println(title);
        }

        waitForElementAndClick(
                By.id("org.wikipedia:id/search_close_btn"),
                "Cannot find X to cancel Search",
                10);

/*        ArrayList<WebElement> elementsPresentAfterClear = searchForElements(By.id("org.wikipedia:id/page_list_item_title"));

        System.out.println("\nSearch returned: " + elementsPresentAfterClear.size() + " articles with following headers");

        for(WebElement element : elementsPresentAfterClear) {

            String title = element.getText();
            System.out.println(title);
        }
*/

        waitForElementNotPresent(
                By.id("org.wikipedia:id/page_list_item_title"),
                "Search is not cleared yet",
                15);
    }

// Test methods

    private WebElement waitForElementPresent(By by, String errorMessage, long timeoutInSeconds) {

        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(errorMessage + "\n");
        return wait.until(
                ExpectedConditions.presenceOfElementLocated(by));

    }

    private WebElement waitForElementPresent(By by, String errorMessage) {

        return waitForElementPresent(by, errorMessage, 5);

    }

    private WebElement waitForElementAndClick(By by, String errorMessage, long timeoutInSeconds) {

        WebElement element = waitForElementPresent(by, errorMessage, timeoutInSeconds);
        element.click();
        return element;

    }

    private WebElement waitForElementAndSendKeys(By by, String value, String errorMessage, long timeoutInSeconds) {

        WebElement element = waitForElementPresent(by, errorMessage, timeoutInSeconds);
        element.sendKeys(value);
        return element;

    }

    private boolean waitForElementNotPresent(By by, String errorMessage, long timeoutInSeconds) {

        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(errorMessage + "\n");
        return wait.until(
                ExpectedConditions.invisibilityOfElementLocated(by)
        );

    }

    private WebElement waitForElementAndClear(By by, String errorMessage, long timeoutInSeconds) {

        WebElement element = waitForElementPresent(by, errorMessage, timeoutInSeconds);
        element.clear();
        return element;

    }

    private ArrayList<WebElement> searchForElements (By by) {


        waitForElementPresent(by, "No elements were found", 20);
        List<WebElement> elements = driver.findElements(by);
        ArrayList<WebElement> result = new ArrayList<WebElement>(elements);
        return result;
    }

}