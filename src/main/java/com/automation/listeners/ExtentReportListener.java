package com.automation.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExtentReportListener implements ITestListener {

    private static final Logger logger = LoggerFactory.getLogger(ExtentReportListener.class);
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    static {
        setupExtentReports();
    }

    private static void setupExtentReports() {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String reportPath = "build/reports/extent-report-" + timestamp + ".html";

            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            sparkReporter.config().setDocumentTitle("Automation Test Report");
            sparkReporter.config().setReportName("TestNG Selenium Test Report");

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
            extent.setSystemInfo("Browser", System.getProperty("browser", "chrome"));
            extent.setSystemInfo("Environment", System.getProperty("env", "local"));

            logger.info("ExtentReports initialized. Report will be generated at: {}", reportPath);
        } catch (Exception e) {
            logger.error("Failed to initialize ExtentReports", e);
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();

        ExtentTest test = extent.createTest(testName, description);
        test.assignCategory(result.getTestClass().getName());

        extentTest.set(test);
        logger.debug("ExtentTest created for: {}", testName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTest test = extentTest.get();
        if (test != null) {
            test.log(Status.PASS, "Test passed successfully");
            long duration = result.getEndMillis() - result.getStartMillis();
            test.info("Execution time: " + duration + "ms");
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest test = extentTest.get();
        if (test != null) {
            test.log(Status.FAIL, "Test failed");
            test.fail(result.getThrowable());

            // Add screenshot if available
            // String screenshotPath = takeScreenshot(result.getMethod().getMethodName());
            // if (screenshotPath != null) {
            //     test.addScreenCaptureFromPath(screenshotPath);
            // }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTest test = extentTest.get();
        if (test != null) {
            test.log(Status.SKIP, "Test skipped");
            test.skip(result.getThrowable());
        }
    }

    @Override
    public void onFinish(org.testng.ITestContext context) {
        if (extent != null) {
            extent.flush();
            logger.info("ExtentReports report generated successfully");
        }
    }

    public static ExtentTest getExtentTest() {
        return extentTest.get();
    }
}
