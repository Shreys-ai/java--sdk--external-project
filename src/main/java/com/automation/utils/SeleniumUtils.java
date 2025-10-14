package com.automation.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.Duration;
import java.util.List;

/**
 * Selenium utility class with common WebDriver operations
 */
public class SeleniumUtils {

    private static final Logger logger = LoggerFactory.getLogger(SeleniumUtils.class);
    private static final int DEFAULT_TIMEOUT = 10;

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final Actions actions;

    public SeleniumUtils(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
        this.actions = new Actions(driver);
    }

    /**
     * Wait for element to be visible and return it
     *
     * @param locator Element locator
     * @return WebElement
     */
    public WebElement waitForElementVisible(By locator) {
        try {
            logger.debug("Waiting for element to be visible: {}", locator);
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            logger.error("Element not visible within timeout: {}", locator);
            throw e;
        }
    }


    public boolean isElementPresent(By locator) {
        return driver.findElements(locator).size() > 0;
    }

    /**
     * Wait for element to be clickable and return it
     *
     * @param locator Element locator
     * @return WebElement
     */
    public WebElement waitForElementClickable(By locator) {
        try {
            logger.debug("Waiting for element to be clickable: {}", locator);
            return wait.until(ExpectedConditions.elementToBeClickable(locator));
        } catch (TimeoutException e) {
            logger.error("Element not clickable within timeout: {}", locator);
            throw e;
        }
    }

    /**
     * Wait for element to be present in DOM
     *
     * @param locator Element locator
     * @return WebElement
     */
    public WebElement waitForElementPresent(By locator) {
        try {
            logger.debug("Waiting for element to be present: {}", locator);
            return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        } catch (TimeoutException e) {
            logger.error("Element not present within timeout: {}", locator);
            throw e;
        }
    }

    /**
     * Click on element with wait
     *
     * @param locator Element locator
     */
    public void click(By locator) {
        WebElement element = waitForElementClickable(locator);
        element.click();
        logger.debug("Clicked on element: {}", locator);
    }

    /**
     * Type text into element with wait
     *
     * @param locator Element locator
     * @param text    Text to type
     */
    public void type(By locator, String text) {
        WebElement element = waitForElementVisible(locator);
        element.clear();
        element.sendKeys(text);
        logger.debug("Typed '{}' into element: {}", text, locator);
    }

    /**
     * Get text from element with wait
     *
     * @param locator Element locator
     * @return Element text
     */
    public String getText(By locator) {
        WebElement element = waitForElementVisible(locator);
        String text = element.getText();
        logger.debug("Got text '{}' from element: {}", text, locator);
        return text;
    }

    /**
     * Get attribute value from element
     *
     * @param locator   Element locator
     * @param attribute Attribute name
     * @return Attribute value
     */
    public String getAttribute(By locator, String attribute) {
        WebElement element = waitForElementVisible(locator);
        String value = element.getAttribute(attribute);
        logger.debug("Got attribute '{}' value '{}' from element: {}", attribute, value, locator);
        return value;
    }

    /**
     * Check if element is displayed
     *
     * @param locator Element locator
     * @return true if displayed, false otherwise
     */
    public boolean isElementDisplayed(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            boolean displayed = element.isDisplayed();
            logger.debug("Element {} displayed: {}", locator, displayed);
            return displayed;
        } catch (NoSuchElementException e) {
            logger.debug("Element not found: {}", locator);
            return false;
        }
    }

    /**
     * Select dropdown option by visible text
     *
     * @param locator Dropdown locator
     * @param text    Visible text to select
     */
    public void selectByText(By locator, String text) {
        WebElement element = waitForElementVisible(locator);
        Select select = new Select(element);
        select.selectByVisibleText(text);
        logger.debug("Selected '{}' from dropdown: {}", text, locator);
    }

    /**
     * Select dropdown option by value
     *
     * @param locator Dropdown locator
     * @param value   Value to select
     */
    public void selectByValue(By locator, String value) {
        WebElement element = waitForElementVisible(locator);
        Select select = new Select(element);
        select.selectByValue(value);
        logger.debug("Selected value '{}' from dropdown: {}", value, locator);
    }

    /**
     * Hover over element
     *
     * @param locator Element locator
     */
    public void hover(By locator) {
        WebElement element = waitForElementVisible(locator);
        actions.moveToElement(element).perform();
        logger.debug("Hovered over element: {}", locator);
    }

    /**
     * Double click on element
     *
     * @param locator Element locator
     */
    public void doubleClick(By locator) {
        WebElement element = waitForElementClickable(locator);
        actions.doubleClick(element).perform();
        logger.debug("Double clicked on element: {}", locator);
    }

    /**
     * Right click on element
     *
     * @param locator Element locator
     */
    public void rightClick(By locator) {
        WebElement element = waitForElementClickable(locator);
        actions.contextClick(element).perform();
        logger.debug("Right clicked on element: {}", locator);
    }

    /**
     * Scroll to element
     *
     * @param locator Element locator
     */
    public void scrollToElement(By locator) {
        WebElement element = waitForElementPresent(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        logger.debug("Scrolled to element: {}", locator);
    }

    /**
     * Wait for page to load
     */
    public void waitForPageLoad() {
        wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState").equals("complete"));
        logger.debug("Page loaded completely");
    }

    /**
     * Get all elements matching locator
     *
     * @param locator Element locator
     * @return List of WebElements
     */
    public List<WebElement> getElements(By locator) {
        List<WebElement> elements = driver.findElements(locator);
        logger.debug("Found {} elements for locator: {}", elements.size(), locator);
        return elements;
    }

    /**
     * Wait for text to be present in element
     *
     * @param locator Element locator
     * @param text    Text to wait for
     */
    public void waitForTextPresent(By locator, String text) {
        wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
        logger.debug("Text '{}' present in element: {}", text, locator);
    }

    /**
     * Execute JavaScript code
     *
     * @param script JavaScript code
     * @param args   Script arguments
     * @return Script result
     */
    public Object executeScript(String script, Object... args) {
        Object result = ((JavascriptExecutor) driver).executeScript(script, args);
        logger.debug("Executed JavaScript: {}", script);
        return result;
    }

    public SeleniumUtils uploadFile(String fileName, By locator) {
        File file = new File("src/test/resources/files/" + fileName);
        driver.findElement(locator).sendKeys(file.getAbsolutePath());
        logger.debug("Uploaded file: {}", file.getAbsolutePath());
        return this;
    }
}