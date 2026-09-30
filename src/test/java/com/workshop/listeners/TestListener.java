package com.workshop.listeners;

import com.workshop.utils.DriverFactory;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

/**
 * Registered via testng.xml <listeners>. Demonstrates the TestNG listener
 * extension point: hook into the test lifecycle without touching test code.
 * Here we log to the console and save a screenshot whenever a test fails.
 */
public class TestListener implements ITestListener {

    private static final Path SCREENSHOT_DIR = Paths.get("test-output", "screenshots");

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("▶ START  " + testName(result));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("✔ PASS   " + testName(result));
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("✘ FAIL   " + testName(result) + " - " + result.getThrowable());
        saveScreenshot(testName(result));
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("⏭ SKIP   " + testName(result));
    }

    private String testName(ITestResult result) {
        return result.getTestClass().getRealClass().getSimpleName() + "." + result.getMethod().getMethodName();
    }

    private void saveScreenshot(String testName) {
        try {
            WebDriver driver = DriverFactory.getDriver();
            if (!(driver instanceof TakesScreenshot)) {
                return;
            }
            Files.createDirectories(SCREENSHOT_DIR);
            File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            Path destination = SCREENSHOT_DIR.resolve(testName + "_" + timestamp + ".png");
            Files.copy(source.toPath(), destination);
            System.out.println("  screenshot saved: " + destination);
        } catch (IOException | RuntimeException e) {
            System.out.println("  could not save screenshot: " + e.getMessage());
        }
    }
}
