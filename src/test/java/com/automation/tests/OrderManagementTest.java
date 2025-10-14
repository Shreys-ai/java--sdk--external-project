package com.automation.tests;

import com.automation.base.BaseTest;
import com.automation.pages.HomePage;
import com.automation.pages.OrdersPage;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class OrderManagementTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(OrderManagementTest.class);
    private static final String TEST_USER = "John Doe";
    private static final String TEST_PRODUCT = "Laptop";

    @Test(description = "Verify orders page loads correctly with all form elements", groups = {"smoke", "order-management", "ui"})
    public void testOrdersPageLoadAndFormElements() {
        logger.info("Starting test: Orders Page Load and Form Elements");

        OrdersPage ordersPage = navigateToOrdersPage();

        Assert.assertTrue(ordersPage.isCreateOrderFormVisible(), "Create order form should be visible");
        logger.info("✅ Create order form is visible");

        List<String> availableUsers = ordersPage.getAvailableUsers();
        Assert.assertFalse(availableUsers.isEmpty(), "User dropdown should have options");
        logger.info("✅ User dropdown has {} options: {}", availableUsers.size(), availableUsers);

        List<String> availableProducts = ordersPage.getAvailableProducts();
        Assert.assertFalse(availableProducts.isEmpty(), "Product dropdown should have options");
        logger.info("✅ Product dropdown has {} options: {}", availableProducts.size(), availableProducts);

        logger.info("Test completed: Orders Page Load and Form Elements");
    }

    @Test(description = "Create a new order with valid details", groups = {"smoke", "order-management", "create"})
    public void testCreateOrderWithValidDetails() {
        logger.info("Starting test: Create Order with Valid Details");

        OrdersPage ordersPage = navigateToOrdersPage();
        int initialOrderCount = ordersPage.getOrdersCount();
        logger.info("Initial order count: {}", initialOrderCount);

        boolean orderCreated = ordersPage
                .createOrder(TEST_USER, TEST_PRODUCT, "2")
                .isOrderCreatedSuccessfully();
        Assert.assertTrue(orderCreated, "Order should be created successfully");

        int finalOrderCount = ordersPage.getOrdersCount();
        Assert.assertTrue(finalOrderCount > initialOrderCount,
                "Order count should increase after creating order");
        logger.info("✅ Order count increased from {} to {}", initialOrderCount, finalOrderCount);

        logger.info("✅ Order created successfully: User={}, Product={}, Quantity=2", TEST_USER, TEST_PRODUCT);
        logger.info("Test completed: Create Order with Valid Details");
    }

    @Test(description = "Test order creation with different products", dataProvider = "validOrderData",
            groups = {"regression", "order-management", "create"})
    public void testCreateOrderWithDifferentProducts(String user, String product, String quantity) {
        logger.info("Starting test: Create Order with Different Products");
        logger.info("Testing with: User='{}', Product='{}', Quantity='{}'", user, product, quantity);

        OrdersPage ordersPage = navigateToOrdersPage();
        int initialOrderCount = ordersPage.getOrdersCount();
        logger.info("Initial order count: {}", initialOrderCount);

        // Verify order creation
        boolean orderCreated = ordersPage
                .createOrder(user, product, quantity)
                .isOrderCreatedSuccessfully();
        Assert.assertTrue(orderCreated, "Order should be created successfully");

        logger.info("✅ Order created successfully with: User={}, Product={}, Quantity={}",
                user, product, quantity);
        logger.info("Test completed: Create Order with Different Products");
    }

    @Test(description = "Test quantity field with boundary values", groups = {"regression", "order-management", "validation"})
    public void testQuantityFieldBoundaryValues() {
        logger.info("Starting test: Quantity Field Boundary Values");

        OrdersPage ordersPage = navigateToOrdersPage();

        int initialOrderCount = ordersPage.getOrdersCount();
        logger.info("Initial order count: {}", initialOrderCount);

        // Test with zero quantity
        int afterOrderCount = ordersPage.selectUser(TEST_USER)
                .selectProduct(TEST_PRODUCT)
                .enterQuantity("0")
                .clickCreateOrder()
                .getOrdersCount();
        Assert.assertEquals(initialOrderCount, afterOrderCount, "Zero quantity should be rejected");
        logger.info("✅ Zero quantity validation working");

        // Test with negative quantity
        int orderCountAfterNegativeValue = ordersPage.clearForm()
                .selectUser(TEST_USER)
                .selectProduct(TEST_PRODUCT)
                .enterQuantity("-1")
                .clickCreateOrder()
                .getOrdersCount();

        Assert.assertEquals(initialOrderCount, orderCountAfterNegativeValue, "Zero quantity should be rejected");
        logger.info("✅ Negative quantity validation working");

        // Test with large quantity
        boolean isInsufficientToastMessagePresent = ordersPage.clearForm()
                .selectUser(TEST_USER)
                .selectProduct(TEST_PRODUCT)
                .enterQuantity("999")
                .clickCreateOrder()
                .inSuffcientStockNotificationVisible();

        Assert.assertTrue(isInsufficientToastMessagePresent, "Large quantity should show insufficient stock message");

        logger.info("✅ Large quantity test completed");
        logger.info("Test completed: Quantity Field Boundary Values");
    }

    @Test(description = "Test multiple order creation", groups = {"regression", "order-management", "create"})
    public void testMultipleOrderCreation() {
        logger.info("Starting test: Multiple Order Creation");

        OrdersPage ordersPage = navigateToOrdersPage();
        int initialOrderCount = ordersPage.getOrdersCount();

        // Create first order
        ordersPage.createOrder("John Doe", "Laptop", "1");
        int firstOrderCount = ordersPage.getOrdersCount();

        // Create second order
        ordersPage.clearForm()
                .createOrder("Jane Smith", "Smartphone", "2");
        int secondOrderCount = ordersPage.getOrdersCount();

        Assert.assertTrue(secondOrderCount > firstOrderCount,
                "Second order should increase the count");
        Assert.assertTrue(secondOrderCount >= initialOrderCount + 2,
                "Should have at least 2 more orders than initial");

        logger.info("✅ Multiple orders created successfully. Count: {} -> {} -> {}",
                initialOrderCount, firstOrderCount, secondOrderCount);
        logger.info("Test completed: Multiple Order Creation");
    }

    @DataProvider(name = "validOrderData")
    public Object[][] getValidOrderData() {
        return new Object[][]{
                {"John Doe", "Laptop", "1"},
                {"Jane Smith", "Smartphone", "2"},
                {"Bob Johnson", "Headphones", "3"},
                {"John Doe", "Coffee Maker", "1"},
                {"Jane Smith", "Book", "5"}
        };
    }

    private OrdersPage navigateToOrdersPage() {
        OrdersPage ordersPage = new OrdersPage(driver);
        Assert.assertTrue(ordersPage.navigateToOrdersPage(), "Orders page should be loaded");

        return ordersPage;
    }
}