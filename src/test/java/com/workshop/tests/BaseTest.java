package com.workshop.tests;

import com.workshop.pages.LoginPage;
import com.workshop.utils.ConfigReader;
import com.workshop.utils.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * Every test class extends this. It centralizes the "open a browser
 * before each test, close it after each test" lifecycle using TestNG's
 * @BeforeMethod / @AfterMethod annotations, so individual test classes
 * only contain test logic.
 */
public abstract class BaseTest {

    protected WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        driver = DriverFactory.getDriver();
        driver.get(ConfigReader.webappUrl("login.html"));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverFactory.quitDriver();
    }

    protected LoginPage loginPage() {
        return new LoginPage(driver);
    }
}
