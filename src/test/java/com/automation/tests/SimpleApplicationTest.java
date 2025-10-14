package com.automation.tests;

import com.automation.base.BaseTest;
import com.automation.pages.HomePage;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleApplicationTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(SimpleApplicationTest.class);

    @Test(description = "Verify specific page title matches expected value")
    public void testPageTitleMatchesExpected() {
        logger.info("Starting test: Page Title Matches Expected");

        String expectedTitle = config.getProperty("app.title");
        String actualTitle = new HomePage(driver).getPageTitle();
        logger.info("🎯 Expected title: {}", expectedTitle);
        logger.info("🔍 Actual title: {}", actualTitle);

        Assert.assertTrue(actualTitle.contains(expectedTitle), String.format("Page title should contain '%s', but was '%s'", expectedTitle, actualTitle));

        logger.info("✅ Title validation passed! Expected title matches actual title");
        logger.info("Test completed: Page Title Matches Expected");
    }

    @Test(description = "Verify application loads within reasonable time", groups = {"performance", "regression"})
    public void testApplicationLoadTime() {
        logger.info("Starting test: Application Load Time");

        long startTime = System.currentTimeMillis();
        HomePage homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isPageLoaded(), "Application should load successfully");

        long endTime = System.currentTimeMillis();
        long loadTime = endTime - startTime;

        logger.info("✅ Application load time: {} ms", loadTime);
        Assert.assertTrue(loadTime < 30000,
                String.format("Application should load within 30 seconds, but took %d ms", loadTime));

        logger.info("✅ Application load time validation passed");
        logger.info("Test completed: Application Load Time");
    }

    @Test(description = "Verify page structure and basic elements are present")
    public void testBasicPageStructure() {
        logger.info("Starting test: Basic Page Structure");

        HomePage homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isPageLoaded(), "Page should be loaded");

        String pageSource = homePage.getPageSource();
        Assert.assertTrue(pageSource.contains("<html"), "Page should have valid HTML structure");
        Assert.assertTrue(pageSource.contains("<body"), "Page should have body element");
        Assert.assertTrue(pageSource.contains("<head"), "Page should have head element");

        logger.info("✅ Basic HTML structure verified");

        boolean isNavigationMenuVisible = homePage.isNavigationMenuVisible();
        Assert.assertTrue(isNavigationMenuVisible, "Navigation menu should be visible");

        logger.info("✅ Navigation structure verified");
        logger.info("Test completed: Basic Page Structure");
    }

    @Test(description = "Verify application URL is accessible")
    public void testApplicationURLAccessibility() {
        logger.info("Starting test: Application URL Accessibility");

        String currentUrl = new HomePage(driver).getCurrentUrl();
        logger.info("🔗 Current URL: {}", currentUrl);

        Assert.assertNotNull(currentUrl, "Current URL should not be null");
        Assert.assertTrue(currentUrl.startsWith("http"), "URL should start with http");

        String expectedBaseUrl = config.getProperty("base.url");
        Assert.assertTrue(currentUrl.contains(expectedBaseUrl), String.format("Current URL should be based on %s", expectedBaseUrl));

        logger.info("✅ URL accessibility validation passed");
        logger.info("Test completed: Application URL Accessibility");
    }
}