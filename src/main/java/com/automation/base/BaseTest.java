package com.automation.base;

import com.automation.driver.WebDriverFactory;
import com.automation.utils.ConfigReader;
import com.automation.utils.SeleniumUtils;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;

public abstract class BaseTest {

    protected static final Logger logger = LoggerFactory.getLogger(BaseTest.class);
    protected WebDriver driver;
    protected ConfigReader config;
    protected SeleniumUtils seleniumUtils;

    @BeforeSuite(alwaysRun = true)
    public void suiteSetup() {
        logger.info("=== Test Suite Started ===");
        config = ConfigReader.getInstance();
    }

    @BeforeClass(alwaysRun = true)
    public void classSetup() {
        logger.info("=== Test Class Setup: {} ===", this.getClass().getSimpleName());
    }

    @BeforeMethod(alwaysRun = true)
    public void methodSetup() {
        logger.info("=== Test Method Setup ===");
        driver = WebDriverFactory.initializeDriver();
        seleniumUtils = new SeleniumUtils(driver);
        config = ConfigReader.getInstance();
        String baseUrl = config.getProperty("base.url", "http://localhost:3000");
        logger.info("Navigating to base URL: {}", baseUrl);
        driver.get(baseUrl);
    }

    @AfterMethod(alwaysRun = true)
    public void methodTeardown() {
        logger.info("=== Test Method Teardown ===");
        if (WebDriverFactory.isDriverInitialized()) {
            WebDriverFactory.quitDriver();
        }
    }

    @AfterClass(alwaysRun = true)
    public void classTeardown() {
        logger.info("=== Test Class Teardown: {} ===", this.getClass().getSimpleName());
    }

    @AfterSuite(alwaysRun = true)
    public void suiteTeardown() {
        logger.info("=== Test Suite Completed ===");
    }
}