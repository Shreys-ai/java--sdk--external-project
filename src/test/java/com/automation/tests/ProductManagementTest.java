package com.automation.tests;

import com.automation.base.BaseTest;
import com.automation.pages.HomePage;
import com.automation.pages.ProductsPage;
import com.automation.utils.Product;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductManagementTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(ProductManagementTest.class);
    private static final String EXPECTED_TOAST_MESSAGE = "Please fill in all required fields: name, price, and category";
    private static final String TEST_CATEGORY = "Electronics";
    private static final String SEARCH_TERM = "Laptop";
    private static final String SUCCESS = "success";

    @Test(description = "Verify adding a new product with valid details", groups = {"smoke", "product-management", "create"})
    public void testAddNewProductWithValidDetails() {
        logger.info("Starting test: Add New Product with Valid Details");
        ProductsPage productsPage = this.isProductPageLoaded();
        Product product = new Product("Test Laptop", "999.99", "Electronics", "10", "High-performance laptop for testing");

        String notificationText = productsPage
                .addNewProductDetails(product)
                .submitTheProductForm()
                .validatesSuccessNotification();

        Assert.assertTrue(notificationText.toLowerCase().contains(SUCCESS), "Success notification should be displayed");
        logger.info("✅ Success notification verified");

        String productName = product.getName();
        String pageContent = productsPage.getPageSource();
        Assert.assertTrue(pageContent.contains(productName), "Product should appear in the list");
        logger.info("✅ Product verified in list: {}", productName);
        logger.info("✅ Product added successfully: {}", productName);

        logger.info("Test completed: Add New Product with Valid Details");
    }

    @Test(description = "Verify product form validation with invalid data", dataProvider = "invalidProductData")
    public void testProductFormValidation(String name, String price, String category, String stock, String description, String expectedValidation) {
        logger.info("Starting test: Product Form Validation with invalid data");
        logger.info("Testing with: Name='{}', Price='{}', Category='{}', Stock='{}', Description='{}'",
                name, price, category, stock, description);

        Product invalidProduct = new Product(name, price, category, stock, description);
        ProductsPage productsPage = this.isProductPageLoaded();

        String actualMessage = productsPage
                .addNewProductDetails(invalidProduct)
                .submitTheProductForm()
                .verifyFormValidation();
        Assert.assertEquals(actualMessage, EXPECTED_TOAST_MESSAGE, "Validation message should match expected");

        logger.info("✅ Form validation test completed for: {}", expectedValidation);
        logger.info("Test completed: Product Form Validation");
    }

    @Test(description = "Verify product category filtering functionality")
    public void testProductCategoryFiltering() {
        logger.info("Starting test: Product Category Filtering");
        ProductsPage productsPage = this.isProductPageLoaded();

        Product electronicsProduct = new Product("Test Laptop", "999.99", "Electronics", "5", "Test electronics product");
        productsPage.addNewProductDetails(electronicsProduct)
                .submitTheProductForm();

        Product booksProduct = new Product("Test Book", "29.99", "Books", "10", "Test books product");
        productsPage.addNewProductDetails(booksProduct)
                .submitTheProductForm();

        boolean isSelectedCategoryProductVisible = productsPage
                .selectCategoryFilter(TEST_CATEGORY)
                .verifyProductsFromCategoryShown(TEST_CATEGORY);
        Assert.assertTrue(isSelectedCategoryProductVisible, "Products from selected category should be visible");
        logger.info("✅ Category filtering verified for: {}", TEST_CATEGORY);

        boolean isAppliedFilteredClear = productsPage
                .clearCategoryFilter()
                .verifyAllProductsShown();
        Assert.assertTrue(isAppliedFilteredClear, "All products should be visible after clearing filter");
        logger.info("✅ Filter clearing verified - all products shown");

        logger.info("Test completed: Product Category Filtering");
    }

    @Test(description = "Verify default product details are displayed correctly")
    public void testDefaultProductDetails() {
        logger.info("Starting test: Default Product Details");
        ProductsPage productsPage = this.isProductPageLoaded();

        boolean isDefaultProductExist = productsPage.verifyDefaultProductsExist();
        Assert.assertTrue(isDefaultProductExist, "Default products should exist on the page");

        logger.info("✅ Default product details verification completed");
        logger.info("Test completed: Default Product Details");
    }

    @Test(description = "Verify product search functionality", groups = {"smoke", "product-management", "search"})
    public void testProductSearchFunctionality() {
        logger.info("Starting test: Product Search Functionality");

        ProductsPage productsPage = this.isProductPageLoaded();
        String actualTerm = productsPage
                .searchProduct(SEARCH_TERM)
                .verifySearchResults();
        Assert.assertEquals(actualTerm, SEARCH_TERM, "Search term should match the input term");

        logger.info("✅ Product search functionality verified for term: {}", SEARCH_TERM);
        logger.info("Test completed: Product Search Functionality");
    }

    @DataProvider(name = "invalidProductData")
    public Object[][] getInvalidProductData() {
        return new Object[][]{
                {"", "99.99", "Electronics", "10", "Test Description", "Name required"},
                {"Test Product", "", "Electronics", "10", "Test Description", "Price required"},
                {"Test Product", "99.99", "", "10", "Test Description", "Category required"},
                {"Test Product", "", "", "10", "Test Description", "Price must be positive"},
                {"", "", "", "01", "Test Description", "Stock required"},
                {"", "", "Electronics", "-5", "Test Description", "Stock must be positive"},
                {"", "invalid", "", "10", "Test Description", "Invalid price format"},
        };
    }

    private ProductsPage isProductPageLoaded() {
        ProductsPage productsPage = new ProductsPage(driver);
        Assert.assertTrue(productsPage.isProductPageLoaded(), "Products page should be loaded");

        return productsPage;
    }
}