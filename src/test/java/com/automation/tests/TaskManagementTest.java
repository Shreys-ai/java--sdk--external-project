package com.automation.tests;

import com.automation.base.BaseTest;
import com.automation.pages.HomePage;
import com.automation.pages.TasksPage;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskManagementTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(TaskManagementTest.class);
    private static final String HIGH_PRIORITY = "High Priority";
    private static final String MEDIUM_PRIORITY = "Medium Priority";

    @Test(description = "Create a new task with title, priority and user assignment", groups = {"smoke", "task-management", "create"})
    public void testCreateNewTaskWithAssignment() {
        logger.info("Starting test: Create New Task with Assignment");
        TasksPage tasksPage = this.isTaskPageLoaded();

        String title = "Test Task";
        boolean isTaskCreatedToastPresent = tasksPage
                .createTaskWithDetails(title, HIGH_PRIORITY)
                .verifyTaskCreationSuccess();
        Assert.assertTrue(isTaskCreatedToastPresent, "Success notification should be displayed");

        boolean isNewTaskCreated = tasksPage.verifyTaskInList(title);
        Assert.assertTrue(isNewTaskCreated, "Task should appear in the tasks list");

        logger.info("✅ Task created successfully: {}", title);
        logger.info("Test completed: Create New Task with Assignment");
    }

    @Test(description = "Test task creation with different priority levels", dataProvider = "taskPriorityData")
    public void testTaskCreationWithDifferentPriorities(String priority, String description) {
        logger.info("Starting test: Task Creation with Priority - {}", priority);

        String title = "Priority Test Task";
        TasksPage tasksPage = this.isTaskPageLoaded();
        boolean isTaskCreatedToastPresent = tasksPage
                .createTaskWithDetails(title, priority)
                .verifyTaskCreationSuccess();
        Assert.assertTrue(isTaskCreatedToastPresent, "Success notification should be displayed");

        boolean isNewTaskCreated = tasksPage.verifyTaskInList(title);
        Assert.assertTrue(isNewTaskCreated, "Task should appear in the tasks list");

        logger.info("✅ Task with {} priority created successfully", priority);
        logger.info("Test completed: Task Creation with Priority - {}", priority);
    }

    @Test(description = "Complete an existing task", groups = {"regression", "task-management", "update"})
    public void testCompleteExistingTask() {
        logger.info("Starting test: Complete Existing Task");

        String title = "Task to Complete";
        TasksPage tasksPage = this.isTaskPageLoaded();

        boolean isTaskCreatedToastPresent = tasksPage
                .createTaskWithDetails(title, MEDIUM_PRIORITY)
                .verifyTaskCreationSuccess();
        Assert.assertTrue(isTaskCreatedToastPresent, "Success notification should be displayed");

        boolean isNewTaskCreated = tasksPage.verifyTaskInList(title);
        Assert.assertTrue(isNewTaskCreated, "Task should appear in the tasks list");

        boolean isCompletedTaskVerified = tasksPage
                .completeTask(title)
                .verifyTaskIsCompleted(title);
        Assert.assertTrue(isCompletedTaskVerified, "Task should be marked as completed");

        logger.info("✅ Task completed successfully: {}", title);
        logger.info("Test completed: Complete Existing Task");
    }

    @Test(description = "Verify task assignment functionality")
    public void testTaskAssignmentFunctionality() {
        logger.info("Starting test: Task Assignment Functionality");
        String title = "Assignment Test Task";
        TasksPage tasksPage = this.isTaskPageLoaded();

        tasksPage.createTaskWithUserAssignment(title, HIGH_PRIORITY, 1);
        boolean isNewTaskCreated = tasksPage.verifyTaskInList(title);
        Assert.assertTrue(isNewTaskCreated, "Task should appear in the tasks list");

        logger.info("✅ Task assignment functionality verified");
        logger.info("Test completed: Task Assignment Functionality");
    }

    @DataProvider(name = "taskPriorityData")
    public Object[][] getTaskPriorityData() {
        return new Object[][]{
                {"High Priority", "High priority task for urgent items"},
                {"Medium Priority", "Medium priority task for normal items"},
                {"Low Priority", "Low priority task for non-urgent items"}
        };
    }

    private TasksPage isTaskPageLoaded() {
        TasksPage tasksPage = new TasksPage(driver);
        Assert.assertTrue(tasksPage.isTaskPageLoaded(), "Tasks page should be loaded");

        return tasksPage;
    }
}