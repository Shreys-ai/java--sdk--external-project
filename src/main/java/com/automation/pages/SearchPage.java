package com.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchPage extends BasePage {

    private static final Logger logger = LoggerFactory.getLogger(SearchPage.class);

    // Page Elements
    private final By searchHeading = By.xpath("//h2[contains(text(), 'Search')]");
    private final By searchInputField = By.xpath("//input[@placeholder='Search users, products, tasks...']");
    private final By searchButton = By.xpath("//button[contains(text(), 'Search')]");
    private final By searchResultsSection = By.xpath("//div[contains(@class, 'search-results') or contains(text(), 'Search Results')]");
    private final By searchResultsHeading = By.xpath("//h3[contains(text(), 'Search Results for')]");
    private final By noResultsMessage = By.xpath("//p[contains(text(), 'No results found')]");

    public SearchPage(WebDriver driver) {
        super(driver);
        logger.info("SearchPage initialized");
    }

    public boolean isPageLoaded() {
        try {
            seleniumUtils.waitForElementVisible(searchHeading);
            logger.info("Search page loaded successfully");
            return true;
        } catch (Exception e) {
            logger.error("Search page not loaded", e);
            return false;
        }
    }

    public boolean isSearchInputFieldVisible() {
        boolean visible = seleniumUtils.isElementDisplayed(searchInputField);
        logger.info("Search input field visible: {}", visible);
        return visible;
    }

    public String getSearchInputPlaceholderText() {
        try {
            String placeholderText = seleniumUtils.getAttribute(searchInputField, "placeholder");
            logger.info("Search input placeholder text: {}", placeholderText);
            return placeholderText;
        } catch (Exception e) {
            logger.error("Failed to get placeholder text", e);
            return "";
        }
    }

    public boolean isSearchButtonVisible() {
        boolean visible = seleniumUtils.isElementDisplayed(searchButton);
        logger.info("Search button visible: {}", visible);
        return visible;
    }

    public boolean isSearchButtonClickable() {
        try {
            seleniumUtils.waitForElementClickable(searchButton);
            logger.info("Search button is clickable");
            return true;
        } catch (Exception e) {
            logger.error("Search button is not clickable", e);
            return false;
        }
    }

    public SearchPage clickSearchButton() {
        logger.info("Clicking search button");
        seleniumUtils.click(searchButton);
        return this;
    }

    public SearchPage enterSearchText(String searchText) {
        logger.info("Entering search text: {}", searchText);
        seleniumUtils.type(searchInputField, searchText);
        return this;
    }

    public boolean isSearchResultsSectionVisible() {
        boolean visible = seleniumUtils.isElementDisplayed(searchResultsSection);
        logger.info("Search results section visible: {}", visible);
        return visible;
    }

    public boolean isSearchResultsHeadingDisplayed() {
        boolean displayed = seleniumUtils.isElementDisplayed(searchResultsHeading);
        logger.info("Search results heading displayed: {}", displayed);
        return displayed;
    }

    public String getSearchResultsHeadingText() {
        try {
            String headingText = seleniumUtils.getText(searchResultsHeading);
            logger.info("Search results heading text: {}", headingText);
            return headingText;
        } catch (Exception e) {
            logger.error("Failed to get search results heading text", e);
            return "";
        }
    }

    public boolean isNoResultsMessageDisplayed() {
        boolean displayed = seleniumUtils.isElementDisplayed(noResultsMessage);
        logger.info("No results message displayed: {}", displayed);
        return displayed;
    }

    public String getNoResultsMessageText() {
        try {
            String messageText = seleniumUtils.getText(noResultsMessage);
            logger.info("No results message text: {}", messageText);
            return messageText;
        } catch (Exception e) {
            logger.error("Failed to get no results message text", e);
            return "";
        }
    }

    public SearchPage performSearch(String searchTerm) {
        logger.info("Performing search for term: {}", searchTerm);
        enterSearchText(searchTerm);
        clickSearchButton();
        // Wait for search results to load
        try {
            Thread.sleep(2000); // Allow time for search operation
        } catch (InterruptedException e) {
            logger.warn("Sleep interrupted during search operation");
        }
        return this;
    }

    public boolean verifySearchExecuted(String searchTerm) {
        logger.info("Verifying search was executed for term: {}", searchTerm);

        // Check if search results heading is displayed with the search term
        if (isSearchResultsHeadingDisplayed()) {
            String headingText = getSearchResultsHeadingText();
            boolean containsSearchTerm = headingText.contains(searchTerm);
            logger.info("Search results heading contains search term '{}': {}", searchTerm, containsSearchTerm);
            return containsSearchTerm;
        }

        // Alternative check - if no results message is displayed with the search term
        if (isNoResultsMessageDisplayed()) {
            String messageText = getNoResultsMessageText();
            boolean containsSearchTerm = messageText.contains(searchTerm);
            logger.info("No results message contains search term '{}': {}", searchTerm, containsSearchTerm);
            return containsSearchTerm;
        }

        logger.warn("Neither search results heading nor no results message found");
        return false;
    }
}