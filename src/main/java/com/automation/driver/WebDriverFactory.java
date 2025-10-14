package com.automation.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class WebDriverFactory {

    private static final Logger logger = LoggerFactory.getLogger(WebDriverFactory.class);
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public enum BrowserType {
        CHROME, FIREFOX, EDGE, SAFARI
    }

    public static WebDriver initializeDriver(BrowserType browserType, boolean headless) {
        WebDriver webDriver = null;

        try {
            switch (browserType) {
                case CHROME:
                    webDriver = createChromeDriver(headless);
                    break;
                case FIREFOX:
                    webDriver = createFirefoxDriver(headless);
                    break;
                case EDGE:
                    webDriver = createEdgeDriver(headless);
                    break;
                case SAFARI:
                    webDriver = createSafariDriver();
                    break;
                default:
                    throw new IllegalArgumentException("Browser type not supported: " + browserType);
            }

            // Configure WebDriver settings
            webDriver.manage().window().maximize();
            webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            webDriver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
            webDriver.manage().deleteAllCookies();

            driver.set(webDriver);
            logger.info("WebDriver initialized successfully for browser: {}", browserType);

        } catch (Exception e) {
            logger.error("Failed to initialize WebDriver for browser: {}", browserType, e);
            throw new RuntimeException("WebDriver initialization failed", e);
        }

        return webDriver;
    }

    public static WebDriver initializeDriver() {
        // Check if running on BrowserStack (BrowserStack SDK sets this property)
        String bsConfig = System.getProperty("browserstack.config");
        if (bsConfig != null || isBrowserStackExecution()) {
            return createBrowserStackDriver();
        }

        String browser = System.getProperty("browser", "chrome").toLowerCase();
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "false"));

        BrowserType browserType;
        switch (browser) {
            case "firefox":
                browserType = BrowserType.FIREFOX;
                break;
            case "edge":
                browserType = BrowserType.EDGE;
                break;
            case "safari":
                browserType = BrowserType.SAFARI;
                break;
            default:
                browserType = BrowserType.CHROME;
        }

        return initializeDriver(browserType, headless);
    }

    public static WebDriver getDriver() {
        WebDriver webDriver = driver.get();
        if (webDriver == null) {
            throw new IllegalStateException("WebDriver is not initialized. Call initializeDriver() first.");
        }
        return webDriver;
    }

    public static void quitDriver() {
        WebDriver webDriver = driver.get();
        if (webDriver != null) {
            try {
                webDriver.quit();
                logger.info("WebDriver quit successfully");
            } catch (Exception e) {
                logger.error("Error while quitting WebDriver", e);
            } finally {
                driver.remove();
            }
        }
    }

    private static WebDriver createChromeDriver(boolean headless) {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-web-security");
        options.addArguments("--disable-features=VizDisplayCompositor");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--remote-allow-origins=*");

        if (headless) {
            options.addArguments("--headless");
            logger.info("Chrome driver initialized in headless mode");
        } else {
            logger.info("Chrome driver initialized in normal mode");
        }

        return new ChromeDriver(options);
    }

    private static WebDriver createFirefoxDriver(boolean headless) {
        WebDriverManager.firefoxdriver().setup();

        FirefoxOptions options = new FirefoxOptions();

        if (headless) {
            options.addArguments("--headless");
            logger.info("Firefox driver initialized in headless mode");
        } else {
            logger.info("Firefox driver initialized in normal mode");
        }

        return new FirefoxDriver(options);
    }

    private static WebDriver createEdgeDriver(boolean headless) {
        WebDriverManager.edgedriver().setup();

        EdgeOptions options = new EdgeOptions();
        options.addArguments("--disable-web-security");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");

        if (headless) {
            options.addArguments("--headless");
            logger.info("Edge driver initialized in headless mode");
        } else {
            logger.info("Edge driver initialized in normal mode");
        }

        return new EdgeDriver(options);
    }

    private static WebDriver createSafariDriver() {
        logger.info("Safari driver initialized (headless mode not supported)");
        return new SafariDriver();
    }

    public static boolean isDriverInitialized() {
        return driver.get() != null;
    }

    private static boolean isBrowserStackExecution() {
        // Check for BrowserStack specific environment variables or system properties
        return System.getProperty("browserstack.user") != null ||
                System.getProperty("browserstack.key") != null ||
                System.getenv("BROWSERSTACK_USERNAME") != null ||
                System.getenv("BROWSERSTACK_ACCESS_KEY") != null;
    }

    private static WebDriver createBrowserStackDriver() {
        try {
            // BrowserStack Hub URL
            String username = System.getProperty("browserstack.user", System.getenv("BROWSERSTACK_USERNAME"));
            String accessKey = System.getProperty("browserstack.key", System.getenv("BROWSERSTACK_ACCESS_KEY"));

            if (username == null || accessKey == null) {
                logger.warn("BrowserStack credentials not found. Using local driver instead.");
                return initializeDriver(BrowserType.CHROME, false);
            }

            String hubUrl = String.format("https://%s:%s@hub-cloud.browserstack.com/wd/hub", username, accessKey);

            DesiredCapabilities caps = new DesiredCapabilities();

            // Set browser capabilities from system properties or defaults
            String browser = System.getProperty("browser", "chrome");
            String platform = System.getProperty("platform", "Windows 11");
            String browserVersion = System.getProperty("browserVersion", "latest");

            caps.setCapability("browserName", browser);
            caps.setCapability("browserVersion", browserVersion);
            // caps.setCapability("os", platform.split(" ")[0]);
            // caps.setCapability("osVersion", platform.contains(" ") ? platform.split(" ", 2)[1] : "11");

            // BrowserStack specific capabilities
            Map<String, Object> browserstackOptions = new HashMap<>();
            browserstackOptions.put("projectName", "TestNG Automation Framework");
            browserstackOptions.put("buildName", "TestNG Build - " + System.currentTimeMillis());
            browserstackOptions.put("sessionName", "TestNG Test Session");
            browserstackOptions.put("debug", "false");
            browserstackOptions.put("networkLogs", "true");
            browserstackOptions.put("consoleLogs", "errors");
            browserstackOptions.put("resolution", "1920x1080");

            // Local testing support
            boolean isLocal = Boolean.parseBoolean(System.getProperty("local", "false"));
            if (isLocal) {
                browserstackOptions.put("local", "true");
                browserstackOptions.put("localIdentifier", System.getProperty("localIdentifier"));
            }

            caps.setCapability("bstack:options", browserstackOptions);

            WebDriver webDriver = new RemoteWebDriver(new URL(hubUrl), caps);

            // Configure WebDriver settings
            webDriver.manage().window().maximize();
            webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            webDriver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));

            driver.set(webDriver);
            logger.info("BrowserStack WebDriver initialized successfully for browser: {} on platform: {}", browser, platform);

            return webDriver;

        } catch (Exception e) {
            logger.error("Failed to initialize BrowserStack WebDriver", e);
            throw new RuntimeException("BrowserStack WebDriver initialization failed", e);
        }
    }
}
