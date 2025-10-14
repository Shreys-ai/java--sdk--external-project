package com.automation.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    private static final Logger logger = LoggerFactory.getLogger(TestListener.class);

    @Override
    public void onTestStart(ITestResult result) {
        logger.info("========================================");
        logger.info("STARTING TEST: {} in class {}",
                result.getMethod().getMethodName(),
                result.getTestClass().getName());
        logger.info("Description: {}", result.getMethod().getDescription());
        logger.info("========================================");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        long duration = result.getEndMillis() - result.getStartMillis();
        logger.info("✅ TEST PASSED: {} (Duration: {}ms)",
                result.getMethod().getMethodName(), duration);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        long duration = result.getEndMillis() - result.getStartMillis();
        logger.error("❌ TEST FAILED: {} (Duration: {}ms)",
                result.getMethod().getMethodName(), duration);
        logger.error("Failure reason: {}", result.getThrowable().getMessage());

        // Take screenshot on failure
        takeScreenshot(result);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger.warn("⏭️ TEST SKIPPED: {}", result.getMethod().getMethodName());
        logger.warn("Skip reason: {}", result.getThrowable().getMessage());
    }

    private void takeScreenshot(ITestResult result) {
        try {
            logger.info("📷 Screenshot would be taken here for failed test: {}",
                    result.getMethod().getMethodName());
        } catch (Exception e) {
            logger.error("Failed to take screenshot", e);
        }
    }
}
