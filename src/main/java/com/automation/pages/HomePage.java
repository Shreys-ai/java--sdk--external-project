package com.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HomePage extends BasePage {

    private static final Logger logger = LoggerFactory.getLogger(HomePage.class);

    // Page Elements
    private final By headerElement = By.tagName("h1");
    private final By navigationMenu = By.cssSelector("nav");
    private final By dashboardLink = By.xpath("//button[contains(text(), 'Dashboard')]");
    private final By productsLink = By.xpath("//button[contains(text(), 'Products')]");
    private final By ordersLink = By.xpath("//button[contains(text(), 'Orders')]");
    private final By usersLink = By.xpath("//button[contains(text(), 'Users')]");
    private final By tasksLink = By.xpath("//button[contains(text(), 'Tasks')]");
    private final By searchLink = By.xpath("//button[contains(text(), 'SEARCH')]");

    public HomePage(WebDriver driver) {
        super(driver);
        logger.info("HomePage initialized");
    }

    public boolean isPageLoaded() {
        try {
            seleniumUtils.waitForElementVisible(headerElement);
            logger.info("Home page loaded successfully");
            return true;
        } catch (Exception e) {
            logger.error("Home page not loaded", e);
            return false;
        }
    }

    public boolean isNavigationMenuVisible() {
        boolean visible = seleniumUtils.isElementDisplayed(navigationMenu);
        logger.info("Navigation menu visible: {}", visible);
        return visible;
    }

    public DashboardPage clickDashboard() {
        logger.info("Clicking on Dashboard link");
        seleniumUtils.click(dashboardLink);
        return new DashboardPage(driver);
    }

    public ProductsPage clickProducts() {
        logger.info("Clicking on Products link");
        seleniumUtils.click(productsLink);
        return new ProductsPage(driver);
    }

    public OrdersPage clickOrders() {
        logger.info("Clicking on Orders link");
        seleniumUtils.click(ordersLink);
        return new OrdersPage(driver);
    }

    public UsersPage clickUsers() {
        logger.info("Clicking on Users link");
        seleniumUtils.click(usersLink);
        return new UsersPage(driver);
    }

    public TasksPage clickTasks() {
        logger.info("Clicking on Tasks link");
        seleniumUtils.click(tasksLink);
        return new TasksPage(driver);
    }

    public SearchPage clickSearch() {
        logger.info("Clicking on Search link");
        seleniumUtils.click(searchLink);
        return new SearchPage(driver);
    }

    public boolean areAllNavigationLinksPresent() {
        boolean allPresent = seleniumUtils.isElementDisplayed(dashboardLink) &&
                seleniumUtils.isElementDisplayed(productsLink) &&
                seleniumUtils.isElementDisplayed(ordersLink) &&
                seleniumUtils.isElementDisplayed(usersLink) &&
                seleniumUtils.isElementDisplayed(tasksLink);

        logger.info("All navigation links present: {}", allPresent);
        return allPresent;
    }
}