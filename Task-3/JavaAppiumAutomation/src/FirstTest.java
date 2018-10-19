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
    public void testSearchResultCheck() {

        String searchString = "Naval";
        int resultCount = 0; // Для хранения результата сравнения элемента списка с искомой строкой
        int resultsFound; // Для хранения количества полученных результатов

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

        ArrayList<WebElement> elementsHeader = searchForElements(By.id("org.wikipedia:id/page_list_item_title"));
        ArrayList<WebElement> elementsText = searchForElements(By.id("org.wikipedia:id/page_list_item_description"));

/*        System.out.println("Search returned: " + elementsHeader.size() + " articles with following headers");

        for(WebElement element : elementsHeader) {

            String title = element.getText();
            System.out.println(title);
        }

        System.out.println("\nSearch returned: " + elementsText.size() + " articles with following text");

        for(WebElement element : elementsText) {

            String title = element.getText();
            System.out.println(title);
        }
*/

        resultsFound = elementsHeader.size();
        int[] headerNumbers = new int[resultsFound];

        for(WebElement element : elementsHeader) {

            String title = element.getText();
            int titleIndex = elementsHeader.indexOf(title);
            if (title.indexOf(searchString) == -1) {
                System.out.println(title); }
            else {
                resultCount++;
            }
        }

        if (resultCount == resultsFound) {System.out.println("All found headers contain search string");}
        else {
            System.out.println(resultCount + " headers of " + resultsFound + " contain search string");
            Assert.fail("\nNot all headers contain search string");
        }

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

    public static boolean equals(String str1, String str2) {
        return str1 == null ? str2 == null : str1.equals(str2);
    }


}