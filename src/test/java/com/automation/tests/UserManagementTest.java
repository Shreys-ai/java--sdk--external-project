package com.automation.tests;

import com.automation.base.BaseTest;
import com.automation.pages.HomePage;
import com.automation.pages.UsersPage;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserManagementTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(UserManagementTest.class);
    private static final String USER_ADDED_TOAST_MESSAGE = "user added successfully";

    @Test(description = "Add a new user with valid details", dataProvider = "validUserData", groups = {"smoke", "user-management", "create"})
    public void testAddNewUserWithValidDetails(String name, String email, String role) {
        logger.info("Starting test: Add New User with Valid Details");
        logger.info("User details - Name: {}, Email: {}, Role: {}", name, email, role);

        UsersPage usersPage = this.isUserPageLoaded();
        int initialUserCount = usersPage.getUserCount();

        String actualToastMessage = usersPage.enterUserDetails(name, email, role)
                .userSubmitsForm()
                .verifySuccessToastNotification();

        Assert.assertTrue(actualToastMessage.toLowerCase().contains(USER_ADDED_TOAST_MESSAGE), "Success notification should be displayed");

        boolean isUserPresentInList = usersPage.verifyUserInList(name, email);
        Assert.assertTrue(isUserPresentInList, "Newly added user should appear in the users list");

        int finalUserCount = usersPage.getUserCount();
        Assert.assertTrue(finalUserCount > initialUserCount, "User count should increase after adding a user");

        logger.info("✅ User added successfully: {} ({})", name, email);
        logger.info("Test completed: Add New User with Valid Details");
    }

    @Test(description = "Test user form validation with invalid data", dataProvider = "invalidUserData", groups = {"regression", "user-management", "validation"})
    public void testUserFormValidation(String name, String email, String role, String expectedValidation) {
        logger.info("Starting test: User Form Validation");
        logger.info("Testing with - Name: '{}', Email: '{}', Role: '{}'", name, email, role);

        UsersPage usersPage = this.isUserPageLoaded();
        boolean isValidationDisplayed = usersPage
                .attemptToAddUserWithValidation(name, email, role)
                .verifyFormValidationMessage(expectedValidation);
        Assert.assertTrue(isValidationDisplayed, "Expected validation message should be displayed");

        logger.info("✅ Form validation test completed for: {}", expectedValidation);
        logger.info("Test completed: User Form Validation");
    }

    @Test(description = "Delete a user from the users list", groups = {"regression", "user-management", "delete"})
    public void testDeleteUserFromList() {
        logger.info("Starting test: Delete User from List");

        UsersPage usersPage = this.isUserPageLoaded();

        boolean checkExistingUserCount = usersPage.ensureUserExistsForDeletion();
        Assert.assertTrue(checkExistingUserCount, "At least one user should exist for deletion test");

        int initialUserCount = usersPage.getUserCount();
        int finalUserCount = usersPage
                .deleteUserFromList(1)
                .getUserCount();

        Assert.assertTrue(finalUserCount < initialUserCount,
                "User count should decrease after deletion");

        logger.info("✅ User deleted successfully. Count changed from {} to {}",
                initialUserCount, finalUserCount);
        logger.info("Test completed: Delete User from List");
    }

    @Test(description = "Test user form field interactions")
    public void testUserFormFieldInteractions() {
        logger.info("Starting test: User Form Field Interactions");

        UsersPage usersPage = this.isUserPageLoaded();

        String name = "Test Name";
        String enteredValue = usersPage.testNameFieldInteraction(name);
        Assert.assertEquals(enteredValue, name, "Name field should accept input");

        String email = "test@example.com";
        enteredValue = usersPage.testEmailFieldInteraction(email);
        Assert.assertEquals(enteredValue, email, "Email field should accept input");

        usersPage
                .testRoleFieldInteraction()
                .testCompleteFormFlow();

        logger.info("✅ User form field interactions verified");
        logger.info("Test completed: User Form Field Interactions");
    }

    @Test(description = "Test refresh users functionality")
    public void testRefreshUsersFunctionality() {
        logger.info("Starting test: Refresh Users Functionality");

        UsersPage usersPage = this.isUserPageLoaded();
        int initialUserCount = usersPage.getUserCount();

        boolean usersListVisible = usersPage
                .clickRefreshUsersButton()
                .verifyUsersListAfterRefresh();
        Assert.assertTrue(usersListVisible, "Users list should be visible after refresh");

        int refreshedUserCount = usersPage.getUserCount();
        Assert.assertEquals(initialUserCount, refreshedUserCount, "User count is mismatching on refreshing the page");
        logger.info("User count before refresh: {}, after refresh: {}",
                initialUserCount, refreshedUserCount);

        logger.info("✅ Refresh users functionality verified");
        logger.info("Test completed: Refresh Users Functionality");
    }

    @Test(description = "Test user list display and structure")
    public void testUserListDisplayAndStructure() {
        logger.info("Starting test: User List Display and Structure");

        UsersPage usersPage = this.isUserPageLoaded();

        boolean isListStructurePresent = usersPage.verifyUserListStructure();
        Assert.assertTrue(isListStructurePresent, "User list should have proper structure");

        usersPage.testUserListInteractions();

        logger.info("✅ User list display and structure verified");
        logger.info("Test completed: User List Display and Structure");
    }

    @DataProvider(name = "validUserData")
    public Object[][] getValidUserData() {
        long timestamp = System.currentTimeMillis();
        return new Object[][]{
                {"John Doe " + timestamp, "john.doe" + timestamp + "@example.com", "Admin"},
                {"Jane Smith " + timestamp, "jane.smith" + timestamp + "@example.com", "User"},
                {"Bob Johnson " + timestamp, "bob.johnson" + timestamp + "@example.com", "User"}
        };
    }

    @DataProvider(name = "invalidUserData")
    public Object[][] getInvalidUserData() {
        return new Object[][]{
                {"", "test@example.com", "User", "Name required"},
                {"Test User", "", "User", "Email required"},
                {"Test User", "invalid-email", "User", "Valid email required"},
                {"Test User", "test@example.com", "", "Role required"},
                {"", "", "", "All fields required"}
        };
    }

    private UsersPage isUserPageLoaded() {
        UsersPage usersPage = new UsersPage(driver);
        Assert.assertTrue(usersPage.isUserPageLoaded(), "Users page should be loaded");

        return usersPage;
    }
}