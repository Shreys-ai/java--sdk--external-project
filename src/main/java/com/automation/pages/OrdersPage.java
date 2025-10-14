package com.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class OrdersPage extends BasePage {

    private static final Logger logger = LoggerFactory.getLogger(OrdersPage.class);
    private final String SELECT_PRODUCT_FIELD = "//option[text()='Select Product']//parent::select//option[contains(text(),'@productName')]";

    // Page Elements
    private final By createOrderTitle = By.xpath("//h2[contains(text(), 'Create New Order')]");
    private final By userDropdown = By.xpath("//option[text()='Select User']//parent::select");
    private final By productDropdown = By.xpath("//option[text()='Select Product']//parent::select");
    private final By quantityInput = By.xpath("//input[@type='number' or @name='quantity']");
    private final By createOrderButton = By.xpath("//button[contains(text(), 'Create Order')]");
    private final By ordersCounter = By.xpath("//div[@class='orders-section']//h2");
    private final By orderItems = By.xpath("//div[contains(@class, 'order-item') or contains(@class, 'order')]");
    private final By successNotification = By.xpath("//div[contains(@class, 'notification') or contains(@class, 'toast') or contains(@class, 'alert')]");
    private final By byInsufficientStockErrorMessage = By.xpath("//div[text()='Error: Insufficient stock']");

    public OrdersPage(WebDriver driver) {
        super(driver);
        logger.info("OrdersPage initialized");
    }

    public boolean isPageLoaded() {
        try {
            seleniumUtils.waitForElementVisible(createOrderTitle);
            logger.info("Orders page loaded successfully");
            return true;
        } catch (Exception e) {
            logger.error("Orders page not loaded", e);
            return false;
        }
    }

    public boolean isCreateOrderFormVisible() {
        boolean visible = seleniumUtils.isElementDisplayed(createOrderTitle) &&
                seleniumUtils.isElementDisplayed(userDropdown) &&
                seleniumUtils.isElementDisplayed(productDropdown) &&
                seleniumUtils.isElementDisplayed(quantityInput) &&
                seleniumUtils.isElementDisplayed(createOrderButton);
        logger.info("Create order form visible: {}", visible);
        return visible;
    }

    public OrdersPage selectUser(String userName) {
        logger.info("Selecting user: {}", userName);
        seleniumUtils.selectByText(userDropdown, userName);
        return this;
    }

    public OrdersPage selectProduct(String productName) {
        logger.info("Selecting product: {}", productName);
        seleniumUtils.click(productDropdown);
        seleniumUtils.click(By.xpath(SELECT_PRODUCT_FIELD.replace("@productName", productName)));
        return this;
    }

    public OrdersPage enterQuantity(String quantity) {
        logger.info("Entering quantity: {}", quantity);
        seleniumUtils.type(quantityInput, quantity);
        return this;
    }

    public OrdersPage clickCreateOrder() {
        logger.info("Clicking Create Order button");
        seleniumUtils.click(createOrderButton);
        return this;
    }

    public OrdersPage createOrder(String userName, String productName, String quantity) {
        logger.info("Creating order - User: {}, Product: {}, Quantity: {}", userName, productName, quantity);
        selectUser(userName)
                .selectProduct(productName)
                .enterQuantity(quantity)
                .clickCreateOrder();
        return this;
    }

    public int getOrdersCount() {
        String counterText = seleniumUtils.getText(ordersCounter);
        // Extract number from text like "Orders (2)"
        String number = counterText.replaceAll("[^0-9]", "");
        int count = Integer.parseInt(number);
        logger.info("Orders count: {}", count);
        return count;
    }

    public boolean isOrderCreatedSuccessfully() {
        boolean success = seleniumUtils.isElementDisplayed(successNotification);
        logger.info("Order creation success notification visible: {}", success);
        return success;
    }

    public List<WebElement> getOrderItems() {
        List<WebElement> orders = seleniumUtils.getElements(orderItems);
        logger.info("Found {} order items", orders.size());
        return orders;
    }

    public OrdersPage clearForm() {
        logger.info("Clearing order form");
        try {
            seleniumUtils.selectByText(userDropdown, "Select User");
            seleniumUtils.selectByText(productDropdown, "Select Product");
            seleniumUtils.type(quantityInput, "1");
        } catch (Exception e) {
            logger.error("Failed to clear form", e);
        }
        return this;
    }

    public List<String> getAvailableUsers() {
        WebElement dropdown = seleniumUtils.waitForElementVisible(userDropdown);
        List<WebElement> options = dropdown.findElements(By.tagName("option"));
        List<String> users = new ArrayList<>();
        for (WebElement option : options) {
            String text = option.getText();
            if (!text.equals("Select User")) {
                users.add(text);
            }
        }
        logger.info("Available users: {}", users);
        return users;
    }

    public List<String> getAvailableProducts() {
        WebElement dropdown = seleniumUtils.waitForElementVisible(productDropdown);
        List<WebElement> options = dropdown.findElements(By.tagName("option"));
        List<String> products = new ArrayList<>();
        for (WebElement option : options) {
            String text = option.getText();
            if (!text.equals("Select Product")) {
                products.add(text);
            }
        }
        logger.info("Available products: {}", products);
        return products;
    }

    public boolean inSuffcientStockNotificationVisible() {
        boolean displayed = seleniumUtils.isElementDisplayed(byInsufficientStockErrorMessage);
        logger.info("Insufficient stock error message displayed: {}", displayed);
        return displayed;
    }

    public boolean navigateToOrdersPage() {
        return new HomePage(driver)
                .clickOrders()
                .isPageLoaded();
    }
}
