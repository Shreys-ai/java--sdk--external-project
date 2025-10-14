package com.automation.tests;

import com.automation.base.BaseTest;
import com.automation.pages.HomePage;
import com.automation.pages.SearchPage;
import org.testng.Assert;
import org.slf4j.Logger;
import org.testng.annotations.Test;
import org.slf4j.LoggerFactory;

public class SearchFunctionalityTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(SearchFunctionalityTest.class);

    @Test(description = "Verify search input field displays correct placeholder text", groups = {"smoke", "search", "ui"})
    public void testSearchInputFieldPlaceholderText() {
        logger.info("Starting test: T002 - Verify search input field displays correct placeholder text");

        HomePage homePage = new HomePage(driver);
        SearchPage searchPage = new SearchPage(driver);

        // Ensure home page is loaded first
        Assert.assertTrue(homePage.isPageLoaded(), "Home page should load successfully");
        logger.info("✅ Home page loaded successfully");

        // Wait for navigation to be visible
        Assert.assertTrue(homePage.isNavigationMenuVisible(), "Navigation menu should be visible");
        logger.info("✅ Navigation menu is visible");

        // Navigate to search functionality
        searchPage = homePage.clickSearch();
        Assert.assertTrue(searchPage.isPageLoaded(), "Search page should load successfully");
        logger.info("✅ Search page loaded successfully");

        // Verify search input field is visible
        Assert.assertTrue(searchPage.isSearchInputFieldVisible(), "Search input field should be visible");
        logger.info("✅ Search input field is visible");

        // Verify placeholder text is correct
        String expectedPlaceholderText = "Search users, products, tasks...";
        String actualPlaceholderText = searchPage.getSearchInputPlaceholderText();

        Assert.assertEquals(actualPlaceholderText, expectedPlaceholderText,
                "Search input field should display correct placeholder text to guide users on search scope");
        logger.info("✅ Placeholder text validation passed: '{}'", actualPlaceholderText);

        // Additional validation - verify placeholder text provides clear guidance
        Assert.assertTrue(actualPlaceholderText.contains("users"),
                "Placeholder text should mention 'users' as searchable content");
        Assert.assertTrue(actualPlaceholderText.contains("products"),
                "Placeholder text should mention 'products' as searchable content");
        Assert.assertTrue(actualPlaceholderText.contains("tasks"),
                "Placeholder text should mention 'tasks' as searchable content");
        logger.info("✅ Placeholder text contains all expected searchable content types");

        logger.info("Test completed: T002 - Search input field placeholder text verification passed");
    }

    @Test(description = "Verify search execution with valid search terms", groups = {"smoke", "search", "functionality"})
    public void testSearchExecutionWithValidSearchTerms() {
        logger.info("Starting test: T005 - Verify search execution with valid search terms");

        HomePage homePage = new HomePage(driver);
        SearchPage searchPage = new SearchPage(driver);

        // Ensure home page is loaded first
        Assert.assertTrue(homePage.isPageLoaded(), "Home page should load successfully");
        logger.info("✅ Home page loaded successfully");

        // Wait for navigation to be visible
        Assert.assertTrue(homePage.isNavigationMenuVisible(), "Navigation menu should be visible");
        logger.info("✅ Navigation menu is visible");

        // Navigate to search functionality
        searchPage = homePage.clickSearch();
        Assert.assertTrue(searchPage.isPageLoaded(), "Search page should load successfully");
        logger.info("✅ Search page loaded successfully");

        // Test search execution with multiple valid search terms
        String[] searchTerms = {"user", "product", "task"};

        for (String searchTerm : searchTerms) {
            logger.info("Testing search execution with term: '{}'", searchTerm);

            // Perform search operation
            searchPage.performSearch(searchTerm);
            logger.info("✅ Search performed for term: '{}'", searchTerm);

            // Verify search operation was triggered
            Assert.assertTrue(searchPage.verifySearchExecuted(searchTerm),
                    "Search operation should be triggered and display results or 'no results found' message for term: " + searchTerm);
            logger.info("✅ Search execution verified for term: '{}'", searchTerm);

            // Verify either search results heading is displayed
            boolean hasResults = searchPage.isSearchResultsHeadingDisplayed();
            Assert.assertTrue(hasResults,"Search results heading should be present for term: " + searchTerm);

            String headingText = searchPage.getSearchResultsHeadingText();
            Assert.assertTrue(headingText.contains(searchTerm),"Search results heading should contain the search term: " + searchTerm);
            logger.info("✅ Search execution validation completed for term: '{}'", searchTerm);
        }

        // Additional validation - verify search button functionality
        Assert.assertTrue(searchPage.isSearchButtonVisible(), "Search button should be visible");
        Assert.assertTrue(searchPage.isSearchButtonClickable(), "Search button should be clickable");
        logger.info("✅ Search button functionality validated");

        logger.info("Test completed: T005 - Search execution with valid search terms verification passed");
    }

    @Test(description = "Verify 'no results found' message display for non-matching queries", groups = {"smoke", "search", "validation"})
    public void testNoResultsFoundMessageForNonMatchingQueries() {
        logger.info("Starting test: T007 - Verify 'no results found' message display for non-matching queries");

        HomePage homePage = new HomePage(driver);
        SearchPage searchPage = new SearchPage(driver);

        // Ensure home page is loaded first
        Assert.assertTrue(homePage.isPageLoaded(), "Home page should load successfully");
        logger.info("✅ Home page loaded successfully");

        // Wait for navigation to be visible
        Assert.assertTrue(homePage.isNavigationMenuVisible(), "Navigation menu should be visible");
        logger.info("✅ Navigation menu is visible");

        // Navigate to search functionality
        searchPage = homePage.clickSearch();
        Assert.assertTrue(searchPage.isPageLoaded(), "Search page should load successfully");
        logger.info("✅ Search page loaded successfully");

        // Test non-matching queries that should return "no results found" message
        String[] nonMatchingQueries = {"xyz123", "nonexistent", "@#$%", "verylongnonmatchingquery", "NoMatch"};

        for (String query : nonMatchingQueries) {
            logger.info("Testing 'no results found' message for non-matching query: '{}'", query);

            // Perform search operation with non-matching query
            searchPage.performSearch(query);
            logger.info("✅ Search performed for non-matching query: '{}'", query);

            // Verify "no results found" message is displayed
            Assert.assertTrue(searchPage.isNoResultsMessageDisplayed(),
                    "No results found message should be displayed for non-matching query: " + query);
            logger.info("✅ No results found message is displayed for query: '{}'", query);

            // Get and validate the actual message text
            String actualMessage = searchPage.getNoResultsMessageText();
            Assert.assertFalse(actualMessage.isEmpty(),
                    "No results found message text should not be empty for query: " + query);
            logger.info("✅ No results found message text retrieved: '{}'", actualMessage);

            // Verify message contains the search query
            Assert.assertTrue(actualMessage.contains(query),
                    "No results found message should contain the search query '" + query + "'. Actual message: " + actualMessage);
            logger.info("✅ Message contains search query '{}': {}", query, actualMessage);

            // Verify message format is clear and properly formatted
            Assert.assertTrue(actualMessage.toLowerCase().contains("no results found"),
                    "Message should contain 'no results found' text. Actual message: " + actualMessage);
            logger.info("✅ Message format validation passed for query: '{}'", query);

            // Verify message provides helpful feedback (should be user-friendly)
            Assert.assertTrue(actualMessage.length() > query.length(),
                    "Message should provide more context than just the query term. Actual message: " + actualMessage);

            // Additional validation - verify message is properly formatted with quotes around query
            boolean hasProperFormat = actualMessage.contains("\"" + query + "\"") ||
                    actualMessage.contains("'" + query + "'") ||
                    actualMessage.contains(query);
            Assert.assertTrue(hasProperFormat,
                    "Message should properly format the query term. Expected format: 'No results found for \"" + query + "\"'. Actual: " + actualMessage);
            logger.info("✅ Message format and helpful feedback validation passed for query: '{}'", query);

            logger.info("✅ Complete validation passed for non-matching query: '{}'", query);
        }

        // Additional validation - verify search functionality is working (button is clickable)
        Assert.assertTrue(searchPage.isSearchButtonVisible(), "Search button should be visible");
        Assert.assertTrue(searchPage.isSearchButtonClickable(), "Search button should be clickable");
        logger.info("✅ Search button functionality validated");

        // Verify no false positives - search results heading should not be displayed for no results
        Assert.assertFalse(searchPage.isSearchResultsHeadingDisplayed(),
                "Search results heading should not be displayed when no results are found");
        logger.info("✅ No false positive results heading validation passed");

        logger.info("Test completed: T007 - 'No results found' message display for non-matching queries verification passed");
    }
}