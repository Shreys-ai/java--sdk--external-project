package com.automation.tests;

import com.automation.base.BaseTest;
import com.automation.pages.FileAndThemePage;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThemeAndFileUploadTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(ThemeAndFileUploadTest.class);

    @Test(description = "Test file upload with valid file types", dataProvider = "validFileData")
    public void testFileUploadWithValidFiles(String fileType, String fileName) {
        logger.info("Starting test: File Upload with Valid Files - {} ({})", fileName, fileType);
        FileAndThemePage fileAndThemePage = new FileAndThemePage(driver);

        boolean isFileDetailedDisplay = new FileAndThemePage(driver)
                .navigateToFileUploadSection()
                .performFileUpload(fileType, fileName)
                .verifyFileDetailsDisplay(fileName, fileType);
        Assert.assertTrue(isFileDetailedDisplay, "File details should be displayed correctly");

        boolean isUploadedFilePresentInList = fileAndThemePage.verifyFileInUploadedList(fileName);
        Assert.assertTrue(isUploadedFilePresentInList, "Uploaded file should be present in the uploaded files list");

        logger.info("✅ File upload completed successfully: {} ({})", fileName, fileType);
        logger.info("Test completed: File Upload with Valid Files");
    }

    @Test(description = "Test background theme selection")
    public void testBackgroundThemeSelection() {
        logger.info("Starting test: Background Theme Selection");
        String[] themes = {"Light", "Dark", "Blue", "Purple"};

        FileAndThemePage fileAndThemePage = new FileAndThemePage(driver)
                .navigateToThemesSection();

        for (String theme : themes) {
            boolean themeApplied = fileAndThemePage
                    .selectBackgroundTheme(theme)
                    .verifyThemeSelection(theme);
            Assert.assertTrue(themeApplied, "Theme should be applied correctly: " + theme);
        }

        logger.info("Test completed: Background Theme Selection");
    }

    @Test(description = "Test image file upload and auto-background feature")
    public void testImageFileUploadAndAutoBackground() {
        logger.info("Starting test: Image File Upload and Auto-Background");

        String imageFileName = "test-document.jpg";
        String imageFileType = "image/jpeg";

        String actualToastMessage = new FileAndThemePage(driver)
                .navigateToThemesSection()
                .performFileUpload(imageFileType, imageFileName)
                .verifyFileUploadedToastMessage();
        Assert.assertTrue(actualToastMessage.contains("will be set as background"), "Image file upload should be successful");

        boolean isFileVisibleInBackground = new FileAndThemePage(driver)
                .selectUploadedFile()
                .verifyUploadCompletion();
        Assert.assertTrue(isFileVisibleInBackground, "Image file upload should be successful");

        logger.info("✅ Image file upload and auto-background feature verified");
        logger.info("Test completed: Image File Upload and Auto-Background");
    }

    @Test(description = "Test themes and files section navigation and functionality")
    public void testThemesAndFilesSectionFunctionality() {
        logger.info("Starting test: Themes and Files Section Functionality");
        FileAndThemePage fileAndThemePage = new FileAndThemePage(driver);

        boolean isThemeAndFilesSectionVisible = fileAndThemePage
                .navigateToThemesSection()
                .verifyThemesAndFilesSectionLoaded();
        Assert.assertTrue(isThemeAndFilesSectionVisible, "Themes and files section should be visible");

        boolean basicThemeVisible = fileAndThemePage.testBasicThemesAndFilesFeatures();
        Assert.assertTrue(basicThemeVisible, "Basic themes and files features should be functional");

        logger.info("✅ Themes and files section functionality verified");
        logger.info("Test completed: Themes and Files Section Functionality");
    }

    @DataProvider(name = "validFileData")
    public Object[][] getValidFileData() {
        return new Object[][]{
                {"image/jpeg", "test-document.jpg"},
                {"image/png", "test-document.png"},
                {"text/plain", "test-document.txt"},
                {"application/pdf", "test-document.pdf"},
        };
    }
}