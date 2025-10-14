package com.automation.pages;

import com.automation.utils.Product;
import com.automation.utils.Wait;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ProductsPage extends BasePage {

    private static final Logger logger = LoggerFactory.getLogger(ProductsPage.class);
    private static final String ALL_CATEGORIES_TEXT = "All Categories";

    // Page Elements
    private final By pageTitle = By.cssSelector("h1, h2");
    private final By byTitleXpath = By.cssSelector("h3");
    private final By searchBox = By.cssSelector("input.product-search-input");
    private final By byProductNameSelector = By.cssSelector("input[placeholder='Product Name']");
    private final By byProductPriceSelector = By.cssSelector("input[placeholder='Price']");
    private final By byProductCategorySelector = By.cssSelector("input[placeholder='Category']");
    private final By byProductStockSelector = By.cssSelector("input[placeholder='Stock'], #productStock");
    private final By byProductDescriptionSelector = By.cssSelector("textarea[placeholder='Description'], #productDescription");
    private final By bySubmitButtonSelector = By.cssSelector("button[type='submit'], .submit-btn");
    private final By byNotificationToastSelector = By.cssSelector("div.Toastify__toast-container");
    private final By byFilterSelector = By.cssSelector("div > select");
    private final By byFilterProductOptionSelector = By.cssSelector("div.product-card > p:nth-of-type(2)");
    private final By byProductCardSelector = By.cssSelector("div.product-card");
    private final By byProductSectionsSelector = By.cssSelector("div.products-section");
    private final By byElectronicsXpath = By.xpath("//p[text()='Electronics']");
    private final By byEducationXpath = By.xpath("//p[text()='Education']");
    private final By byHomeXpath = By.xpath("//p[text()='Home']");

    public ProductsPage(WebDriver driver) {
        super(driver);
        logger.info("ProductsPage initialized");
    }

    public boolean isPageLoaded() {
        try {
            seleniumUtils.waitForElementVisible(pageTitle);
            logger.info("Products page loaded successfully");
            return true;
        } catch (Exception e) {
            logger.error("Products page not loaded", e);
            return false;
        }
    }

    public String getPageTitleText() {
        return seleniumUtils.getText(pageTitle);
    }

    public ProductsPage searchProduct(String productName) {
        seleniumUtils.scrollToElement(byProductSectionsSelector);
        seleniumUtils.type(searchBox, productName);
        return this;
    }

    public ProductsPage addNewProductDetails(Product product) {
        seleniumUtils.type(byProductNameSelector, product.getName());
        seleniumUtils.type(byProductPriceSelector, product.getPrice());
        seleniumUtils.type(byProductCategorySelector, product.getCategory());
        seleniumUtils.type(byProductStockSelector, product.getStock());
        seleniumUtils.type(byProductDescriptionSelector, product.getDescription());
        return this;
    }

    public ProductsPage submitTheProductForm() {
        seleniumUtils.click(bySubmitButtonSelector);
        return this;
    }

    public String validatesSuccessNotification() {
        Wait.waitFor(2);
        return seleniumUtils.getText(byNotificationToastSelector);
    }

    public String verifyFormValidation() {
        return seleniumUtils.getText(byNotificationToastSelector);
    }

    public ProductsPage selectCategoryFilter(String category) {
        seleniumUtils.selectByText(byFilterSelector, category);
        logger.info("✅ Category filter selected: {}", category);
        return this;
    }

    public boolean verifyProductsFromCategoryShown(String category) {
        List<WebElement> elementList = driver.findElements(byFilterProductOptionSelector);
        for (WebElement element : elementList) {
            String text = element.getText().toLowerCase();
            if (!text.contains(category.toLowerCase())) {
                return false;
            }
        }
        return true;
    }

    public ProductsPage clearCategoryFilter() {
        this.selectCategoryFilter(ALL_CATEGORIES_TEXT);
        logger.info("✅ Category filter cleared");
        return this;
    }

    public boolean verifyAllProductsShown() {
        boolean isElectronicsCategoryDisplayed = seleniumUtils.isElementPresent(byElectronicsXpath);
        boolean isEducationCategoryDisplayed = seleniumUtils.isElementPresent(byEducationXpath);
        boolean isHomeCategoryDisplayed = seleniumUtils.isElementPresent(byHomeXpath);

        return isElectronicsCategoryDisplayed && isEducationCategoryDisplayed && isHomeCategoryDisplayed;
    }

    public boolean verifyDefaultProductsExist() {
        return seleniumUtils.isElementPresent(byProductCardSelector);
    }

    public String verifySearchResults() {
        seleniumUtils.waitForPageLoad();
        WebElement element = seleniumUtils.waitForElementVisible(byProductCardSelector);
        return element.findElement(byTitleXpath).getText();
    }

    public boolean isProductPageLoaded() {
        return new HomePage(driver)
                .clickProducts()
                .isPageLoaded();
    }
}