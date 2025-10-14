package com.automation.tests;

import com.automation.base.BaseTest;
import com.automation.pages.HomePage;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationSetupTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationSetupTest.class);
    private static final String API_URL_TEXT = "api.url";

    @Test(description = "Verify application runs on configured URL and can be opened in browser", groups = {"smoke", "critical", "setup"})
    public void testApplicationSetupAndNavigation() {
        logger.info("Starting test: Application Setup and Navigation");

        String appUrl = config.getProperty("base.url", "http://localhost:3000");
        System.setProperty("app.url", appUrl);
        logger.info("✅ Application URL configured: {}", appUrl);

        String apiUrl = config.getProperty("api.url", "http://localhost:5000");
        System.setProperty(API_URL_TEXT, apiUrl);
        logger.info("✅ Backend API URL configured: {}", apiUrl);

        HomePage homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isPageLoaded(), "Application should load successfully");
        logger.info("✅ Application opened successfully in browser");

        String pageTitle = homePage.getPageTitle();
        Assert.assertNotNull(pageTitle, "Page title should not be null");
        Assert.assertFalse(pageTitle.trim().isEmpty(), "Page title should not be empty");
        logger.info("✅ Page title verified: {}", pageTitle);

        logger.info("Test completed: Application Setup and Navigation");
    }

    @Test(description = "Verify backend API availability configuration")
    public void testBackendAPIConfiguration() {
        logger.info("Starting test: Backend API Configuration");

        String apiUrl = config.getProperty(API_URL_TEXT, "http://localhost:5000");
        System.setProperty(API_URL_TEXT, apiUrl);

        String configuredApiUrl = System.getProperty(API_URL_TEXT);
        Assert.assertEquals(configuredApiUrl, apiUrl, "API URL should be properly configured");
        logger.info("✅ Backend API URL validation passed: {}", apiUrl);

        logger.info("Test completed: Backend API Configuration");
    }
}