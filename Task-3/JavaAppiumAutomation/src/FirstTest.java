import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.apache.commons.lang3.ArrayUtils;
import org.eclipse.jetty.util.ArrayUtil;
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
import java.util.Arrays;
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
        // capabilites.setCapability("app", "/Users/apalnov/Desktop/Auto Mobile/Lesson-3/Lesson_project/JavaAppiumAutomation/apks/org.wikipedia.apk"); // MacOS
        capabilites.setCapability("app","E:\\Auto Mobile\\Lesson-3\\Task-3\\JavaAppiumAutomation\\apks\\org.wikipedia.apk"); // Windows
        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilites);
    }

    @After
    public void tearDown() {
        driver.quit();
    }


    @Test
    public void testSearchResultCheck() {

        String searchString = "Arsenal";  // Тест проходит если искать по строке "Naval"
        int resultCount = 0; // Для хранения количественного результата сравнения элемента списка с искомой строкой
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
        resultsFound = elementsHeader.size();

        // Извлечение текста из элементов header и text
        String[] elementsHeaderT = new String[resultsFound];
        String[] elementsTextT = new String[resultsFound];

        int iHeaders = 0;

        for(WebElement element : elementsHeader) {

            String title = element.getText();
            elementsHeaderT[iHeaders] = title;
            iHeaders++;
        }

        int iText = 0;

        for(WebElement element : elementsText) {

            String title = element.getText();
            elementsTextT[iText] = title;
            iText++;
        }

        // Проверка вхождения искомой строки
        for(int i = 0; i < resultsFound; i++) {

            String title = elementsHeaderT[i];
            String text = elementsTextT[i];
            String searchStringU = searchString.toLowerCase();

            if (title.indexOf(searchString) == -1) {
                System.out.println("Header does not contain search string: " + title);
                if (text.indexOf(searchStringU) == -1) {
                    System.out.println("Text does not contain search string: " + text);
                }
                else { resultCount++; }
            }
            else { resultCount++; }
        }

        // Анализ результата
        if (resultCount == resultsFound) {System.out.println("All found headers contain search string");}
        else {Assert.fail("\nNot all headers or text contain search string");}

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