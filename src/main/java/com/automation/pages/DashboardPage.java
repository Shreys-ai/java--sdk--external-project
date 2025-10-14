package com.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DashboardPage extends BasePage {

    private static final Logger logger = LoggerFactory.getLogger(DashboardPage.class);

    private static final String ANALYTICS_SECTION_LOCATOR = "//h3[contains(text(),'@option')]//parent::div[@class='analytics-card']";
    private static final String OPTION_TEXT = "@option";
    // Page Elements
    private static final By byPageTitleCssSelector = By.cssSelector("h1, h2");
    private static final By byHealthStatusCssSelector = By.cssSelector("div.health-info");
    private static final By byHealthStatusOkCssSelector = By.cssSelector("span.status-ok");
    private static final By byAnalyticsCssSelector = By.cssSelector("div.analytics-section");

    public DashboardPage(WebDriver driver) {
        super(driver);
        logger.info("DashboardPage initialized");
    }

    public boolean isHealthIndicatorsVisible() {
        boolean isHealthInfoPresent = seleniumUtils.isElementPresent(byHealthStatusCssSelector);
        boolean isStatusPresent = seleniumUtils.isElementPresent(byHealthStatusOkCssSelector);

        return isHealthInfoPresent && isStatusPresent;
    }

    public boolean isPageLoaded() {
        try {
            seleniumUtils.waitForElementVisible(byPageTitleCssSelector);
            logger.info("Dashboard page loaded successfully");
            return true;
        } catch (Exception e) {
            logger.error("Dashboard page not loaded", e);
            return false;
        }
    }

    public String getPageTitleText() {
        return seleniumUtils.getText(byPageTitleCssSelector);
    }

    public DashboardPage userScrollsToAnalyticsSection() {
        seleniumUtils.scrollToElement(byAnalyticsCssSelector);
        return this;
    }

    public boolean isSectionPresentInAnalytics(String option) {
        String sectionLocator = ANALYTICS_SECTION_LOCATOR.replace(OPTION_TEXT, option);
        return seleniumUtils.isElementPresent(By.xpath(sectionLocator));
    }
}