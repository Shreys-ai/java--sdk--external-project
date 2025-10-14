package com.automation.pages;

import com.automation.utils.Wait;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TasksPage extends BasePage {

    private static final Logger logger = LoggerFactory.getLogger(TasksPage.class);
    private static final String LIST_OF_TASK_XPATH = "//div[@class='tasks-section']//h3[text()='@title']";
    private static final String IN_COMPLETE_TASK_XPATH = "//h3[text()='@title']//parent::div//following-sibling::button";
    private static final String COMPLETED_TASK_XPATH = "//h3[text()='@title']//ancestor::div[contains(@class,'completed')]";
    private static final String ADDED = "added";
    private static final String TITLE = "@title";

    private final By pageTitle = By.cssSelector("h1, h2");
    private final By byTaskTitleCssSelector = By.cssSelector("input[placeholder='Task Title']");
    private final By byTaskPriorityCssSelector = By.cssSelector("form > select:nth-of-type(1)");
    private final By byModeratePriorityCssSelector = By.cssSelector("form > select:nth-of-type(2)");
    private final By bySubmitButtonSelector = By.cssSelector("button[type='submit'], .submit-btn, .create-task-btn");
    private final By byNotificationToastSelector = By.cssSelector("div.Toastify__toast-container");
    private final By byTaskPrioritySelector = By.cssSelector("form > select:nth-of-type(2) > option");

    public TasksPage(WebDriver driver) {
        super(driver);
        logger.info("TasksPage initialized");
    }

    public boolean isPageLoaded() {
        try {
            seleniumUtils.waitForElementVisible(pageTitle);
            logger.info("Tasks page loaded successfully");
            return true;
        } catch (Exception e) {
            logger.error("Tasks page not loaded", e);
            return false;
        }
    }

    public TasksPage createTaskWithDetails(String title, String priority) {
        seleniumUtils.type(byTaskTitleCssSelector, title);
        seleniumUtils.selectByText(byTaskPriorityCssSelector, priority);
        seleniumUtils.click(bySubmitButtonSelector);

        logger.info("✅ Task creation form submitted: {} ({})", title, priority);
        return this;
    }

    public boolean verifyTaskCreationSuccess() {
        Wait.waitFor(2);
        String notificationText = seleniumUtils.getText(byNotificationToastSelector);
        logger.info("✅ Task creation success notification verified");

        return notificationText.toLowerCase().contains(ADDED);
    }

    public boolean verifyTaskInList(String title) {
        seleniumUtils.waitForPageLoad();
        By byCurrentTaskXpath = By.xpath(LIST_OF_TASK_XPATH.replace(TITLE, title));
        logger.info("✅ Task verified in list: {}", title);
        return seleniumUtils.isElementPresent(byCurrentTaskXpath);
    }

    public TasksPage completeTask(String title) {
        By byInCompleteTaskXpath = By.xpath(IN_COMPLETE_TASK_XPATH.replace(TITLE, title));
        List<WebElement> completeButtons = driver.findElements(byInCompleteTaskXpath);
        completeButtons.get(0).click(); //Explicitly clicking the first button only.
        logger.info("✅ Task completion initiated: {}", title);
        return this;
    }

    public boolean verifyTaskIsCompleted(String title) {
        seleniumUtils.waitForPageLoad();
        By byCompletedTaskXpath = By.xpath(COMPLETED_TASK_XPATH.replace(TITLE, title));
        return seleniumUtils.isElementPresent(byCompletedTaskXpath);
    }

    public void createTaskWithUserAssignment(String title, String priority, int userIndex) {
        seleniumUtils.type(byTaskTitleCssSelector, title);
        seleniumUtils.selectByText(byTaskPriorityCssSelector, priority);
        List<WebElement> options = driver.findElements(byTaskPrioritySelector);
        seleniumUtils.selectByValue(byModeratePriorityCssSelector, options.get(userIndex).getAttribute("value"));
        seleniumUtils.click(bySubmitButtonSelector);

        logger.info("✅ Task created with assignment: {}", title);
    }

    public boolean isTaskPageLoaded() {
        return new HomePage(driver)
                .clickTasks()
                .isPageLoaded();
    }
}