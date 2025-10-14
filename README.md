# TestNG Automation Framework

A comprehensive TestNG-based automation framework with Selenium WebDriver for web application testing, featuring multiple test execution strategies and BrowserStack integration.

## 🚀 Features

- ✅ **Multi-Browser Support**: Chrome, Firefox, Edge, Safari
- ✅ **BrowserStack Integration**: Cloud testing on 3000+ browser/OS combinations  
- ✅ **Multiple Execution Strategies**: Package, Class, Method, and Tag-based execution
- ✅ **Maven Profiles**: 13 pre-configured profiles for different test scenarios
- ✅ **Test Tags/Groups**: Organized tests by functionality (smoke, regression, critical, etc.)
- ✅ **Page Object Model**: Well-structured page objects for maintainable tests
- ✅ **TestNG Integration**: Powerful testing framework with annotations and listeners
- ✅ **ExtentReports**: Beautiful HTML test reports with screenshots
- ✅ **WebDriverManager**: Automatic driver management
- ✅ **Parallel Execution**: Run tests in parallel for faster execution
- ✅ **Configurable**: Environment-specific configurations
- ✅ **Comprehensive Logging**: Detailed logging with Logback
- ✅ **Maven Build**: Modern build system with multiple execution profiles

## 🚀 Quick Start

### Prerequisites
- Java 11 or higher
- Maven 3.6 or higher  
- Google Chrome, Firefox, or Edge browser
- Internet connection (for WebDriverManager)
- `For the monorepo approach`, ensure that the source keyword is commented out. Both the development code and automation code should reside in a single repository. If no development code is present, the system will consider the automation code’s Git directory instead.
- `For the Git cloning approach`, the development repository should be cloned locally. Ensure that the source keyword is used, and the development repository URLs are provided in an array format. Also, verify that you have switched to a different repository and that there is at least one commit made against the main branch.
- `For the GitHub App approach`, the GitHub integration should be completed through BrowserStack, using the same credentials. The config.json path should be specified under the source key as a string. Also, ensure that the configuration is provided in JSON format.

## 🌐 BrowserStack Integration

### Setup BrowserStack

1. **Get BrowserStack Account**: Sign up at [BrowserStack](https://www.browserstack.com/)

2. **Configure Credentials**: Update `browserstack.yml` with your credentials:

```yaml
userName: YOUR_USERNAME
accessKey: YOUR_ACCESS_KEY
```

3. **Run on BrowserStack**:

```bash
# Run smoke tests on BrowserStack
mvn clean test -P browserstack-smoke-tests

# Run package tests on BrowserStack  
mvn clean test -P browserstack-package-tests

# Run default suite on BrowserStack
mvn clean test -P browserstack-test
```

## For Mono Repo Approach
- Ensure that the **`source`** keyword is commented out.  
- Both the development code and automation code should reside in a single repository.  
- If no development code is present, the system will consider the automation code’s Git directory instead.


## For Git Cloning Approach
- The development repository should be cloned locally.  
- Ensure that the **`source`** keyword is used, and the development repository URLs are provided in an **array** format.  
- Verify that you have switched to a different repository and that there is at least one commit made against the **main** branch.
- For test running via package in java project use ```mvn clean test -P browserstack-test```
- For test running via class in java project use ```mvn clean test -P browserstack-class-tests```

## For GitHub App Approach
- The GitHub integration should be completed through BrowserStack, using the same credentials.  
- The **`test.json`** path should be specified under the **`source`** key as a **string**.  
- Ensure that the configuration is provided in **JSON** format.
- For test running via package in java project use ```mvn clean test -P browserstack-test```
- For test running via class in java project use ```mvn clean test -P browserstack-class-tests```

## ⚙️ Configuration

### TestNG Suite Configuration
The framework includes multiple TestNG XML configurations:

- **`testng.xml`** - Main test suite with parallel execution
- **`testng-package-execution.xml`** - Package-based test execution
- **`testng-class-execution.xml`** - Class-based test execution  
- **`testng-method-execution.xml`** - Method-based test execution
- **`testng-tag-execution.xml`** - Tag/Group-based test execution

## 📚 Additional Resources

- **TestNG Documentation**: [TestNG Official Guide](https://testng.org/doc/)
- **Selenium Documentation**: [Selenium WebDriver Guide](https://selenium.dev/documentation/webdriver/)
- **BrowserStack Documentation**: [BrowserStack Automate](https://www.browserstack.com/docs/automate)
- **ExtentReports**: [ExtentReports Documentation](https://extentreports.com/docs/)