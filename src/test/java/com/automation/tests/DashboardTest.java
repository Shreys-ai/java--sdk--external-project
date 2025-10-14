package com.automation.tests;

import com.automation.base.BaseTest;
import com.automation.pages.HomePage;
import com.automation.pages.DashboardPage;
import org.testng.Assert;
import org.slf4j.Logger;
import org.testng.annotations.Test;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class DashboardTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(DashboardTest.class);
    private static final List<String> sections = Arrays.asList("User", "Products", "Tasks", "Orders");

    @Test(description = "Verify dashboard page title and navigation tabs", groups = {"smoke", "ui", "critical"})
    public void testDashboardPageAndNavigation() {
        logger.info("Starting test: Dashboard Page and Navigation");

        HomePage homePage = new HomePage(driver);
        DashboardPage dashboardPage = new DashboardPage(driver);

        Assert.assertTrue(dashboardPage.isPageLoaded(), "Dashboard page should load successfully");
        logger.info("✅ Dashboard page loaded successfully");

        String expectedTitle = config.getProperty("app.title");
        String actualTitle = dashboardPage.getPageTitleText();
        Assert.assertTrue(actualTitle.contains(expectedTitle), "Page title should contain 'Dashboard'");
        logger.info("✅ Page title validation passed: {}", actualTitle);

        Assert.assertTrue(homePage.areAllNavigationLinksPresent(), "All navigation tabs should be available");
        logger.info("✅ Navigation tabs verification passed");

        logger.info("Test completed: Dashboard Page and Navigation");
    }

    @Test(description = "Verify backend health status display")
    public void testBackendHealthStatusDisplay() {
        logger.info("Starting test: Backend Health Status Display");

        DashboardPage dashboardPage = new HomePage(driver)
                .clickDashboard();

        Assert.assertTrue(dashboardPage.isPageLoaded(), "Dashboard should be loaded");

        boolean hasHealthIndicators = dashboardPage.isHealthIndicatorsVisible();
        Assert.assertTrue(hasHealthIndicators, "ℹ️ Health status indicators not visible or not implemented");

        logger.info("Test completed: Backend Health Status Display");
    }

    @Test(description = "Verify analytics dashboard functionality")
    public void testAnalyticsDashboardFunctionality() {
        logger.info("Starting test: Analytics Dashboard Functionality");

        DashboardPage dashboardPage = new HomePage(driver)
                .clickDashboard()
                .userScrollsToAnalyticsSection();

        for (String section : sections) {
            boolean isSectionDisplayed = dashboardPage.isSectionPresentInAnalytics(section);
            Assert.assertTrue(isSectionDisplayed, "❌ " + section + " Analytics section is not displayed");
            logger.info("✅ " + section + " Analytics section verified");
        }

        logger.info("Test completed: Analytics Dashboard Functionality");
    }

    @Test(description = "Verify dashboard content updates when navigating between tabs")
    public void testDashboardTabContentUpdates() {
        logger.info("Starting test: Dashboard Tab Content Updates");
        HomePage homePage = new HomePage(driver);

        String dashboardContent = homePage.getPageSource();
        String productsContent = homePage.clickProducts()
                .getPageSource();
        Assert.assertNotEquals(dashboardContent, productsContent, "Content should change when navigating to different tabs");
        logger.info("✅ Content updates verified when switching tabs");

        boolean isPageLoaded = homePage
                .clickDashboard()
                .isPageLoaded();
        Assert.assertTrue(isPageLoaded, "Should be able to navigate back to dashboard");

        logger.info("Test completed: Dashboard Tab Content Updates");
    }
}