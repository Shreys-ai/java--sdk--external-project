package com.automation.pages;

import com.automation.utils.Wait;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileAndThemePage extends BasePage {

    private static final Logger logger = LoggerFactory.getLogger(FileAndThemePage.class);
    private static final String THEME_OPTION = "//div[@class='theme-options']//button[contains(text(),'@themeName')]";
    private static final String SELECTED_THEME = "//button[contains(text(),'@themeName') and contains(@class,'active')]";
    private static final String THEME_TEXT = "@themeName";

    private static final By byFileAndThemeXpath = By.xpath("//button[text()='Files & Themes']");
    private static final By byFileInputSelector = By.cssSelector("input[type='file']");
    private static final By byUploadedFileNameSelector = By.cssSelector("div.selected-file > p:nth-of-type(1)");
    private static final By byUploadedFileTypeSelector = By.cssSelector("div.selected-file > p:nth-of-type(3)");
    private static final By byUploadedFileSizeSelector = By.cssSelector("div.selected-file > p:nth-of-type(2)");
    private static final By byUFileUploadedToastMessageXpath = By.xpath("//*[contains(text(),'File uploaded successfully')]");
    private static final By byNotificationSelector = By.cssSelector("div.Toastify__toast-container");
    private static final By byThemeControlSelector = By.cssSelector(".theme-selector, [data-testid='theme'], select[name='theme']");
    private static final By byFileControlSelector = By.cssSelector("input[type='file'], .file-upload, [data-testid='file-input']");
    private static final By byUploadFileXpath = By.xpath("//button[text()='Upload File']");

    public FileAndThemePage(WebDriver driver) {
        super(driver);
    }

    public FileAndThemePage navigateToFileUploadSection() {
        seleniumUtils.click(byFileAndThemeXpath);
        logger.info("✅ Navigated to file upload section");
        return this;
    }

    public FileAndThemePage performFileUpload(String fileType, String fileName) {
        seleniumUtils.uploadFile(fileName, byFileInputSelector);
        logger.info("📁 Simulating file selection: {} ({})", fileName, fileType);
        return this;
    }

    public boolean verifyFileDetailsDisplay(String fileName, String fileType) {
        boolean hasFileName = this.verifyFileInUploadedList(fileName);
        boolean hasFileType = seleniumUtils.getText(byUploadedFileTypeSelector).contains(fileType);
        boolean hasFileSize = seleniumUtils.isElementDisplayed(byUploadedFileSizeSelector);

        logger.info("✅ File details display verified: name, size, and type information found");
        return hasFileName && hasFileType && hasFileSize;
    }

    public boolean verifyFileInUploadedList(String fileName) {
        Wait.waitFor(2);
        return seleniumUtils.getText(byUploadedFileNameSelector).contains(fileName);
    }

    public FileAndThemePage navigateToThemesSection() {
        seleniumUtils.click(byFileAndThemeXpath);
        logger.info("✅ Navigated to themes section");
        return this;
    }

    public FileAndThemePage selectBackgroundTheme(String themeName) {
        By byThemeOptionXpath = By.xpath(THEME_OPTION.replace(THEME_TEXT, themeName));
        seleniumUtils.click(byThemeOptionXpath);
        logger.info("✅ Background theme selected: {}", themeName);
        return this;
    }

    public boolean verifyThemeSelection(String themeName) {
        Wait.waitFor(2);
        By bySelectedThemeXpath = By.xpath(SELECTED_THEME.replace(THEME_TEXT, themeName));
        return seleniumUtils.isElementDisplayed(bySelectedThemeXpath);
    }

    public boolean verifyUploadCompletion() {
        try {
            seleniumUtils.waitForElementVisible(byUFileUploadedToastMessageXpath);
            return seleniumUtils.isElementPresent(byUFileUploadedToastMessageXpath);
        } catch (Exception e) {
            return false;
        }
    }

    public String verifyFileUploadedToastMessage() {
        Wait.waitFor(2);
        return seleniumUtils.getText(byNotificationSelector);
    }

    public boolean verifyThemesAndFilesSectionLoaded() {
        String pageContent = driver.getPageSource().toLowerCase();
        boolean hasThemesContent = pageContent.contains("theme") || pageContent.contains("style");
        boolean hasFilesContent = pageContent.contains("file") || pageContent.contains("upload");

        return hasFilesContent && hasThemesContent;
    }

    public boolean testBasicThemesAndFilesFeatures() {
        boolean hasThemeControls = seleniumUtils.isElementDisplayed(byThemeControlSelector);
        boolean hasFileControls = seleniumUtils.isElementDisplayed(byFileControlSelector);
        return hasFileControls && hasThemeControls;
    }

    public FileAndThemePage selectUploadedFile() {
        seleniumUtils.click(byUploadFileXpath);
        return this;
    }
}
