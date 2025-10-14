package com.automation.pages;

import com.automation.utils.SeleniumUtils;
import org.openqa.selenium.WebDriver;

public abstract class BasePage {

    protected WebDriver driver;
    protected SeleniumUtils seleniumUtils;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.seleniumUtils = new SeleniumUtils(driver);
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public String getPageSource() {
        return driver.getPageSource();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public void waitForPageLoad() {
        seleniumUtils.waitForPageLoad();
    }

    public void refreshPage() {
        driver.navigate().refresh();
    }

    public void navigateBack() {
        driver.navigate().back();
    }

    public void navigateForward() {
        driver.navigate().forward();
    }
}
