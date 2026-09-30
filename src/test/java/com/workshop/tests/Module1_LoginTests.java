package com.workshop.tests;

import com.workshop.pages.DashboardPage;
import com.workshop.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * MODULE 1 - Selenium + TestNG fundamentals.
 *
 * Concepts covered: @Test annotation, TestNG assertions, locating elements
 * through a Page Object, explicit waits (inside BasePage), and grouping
 * tests with the `groups` attribute so a suite can run a subset
 * (see testng.xml, group "smoke").
 */
public class Module1_LoginTests extends BaseTest {

    @Test(groups = "smoke", description = "Sanity check: the login page loads with the right title")
    public void testLoginPageLoads() {
        Assert.assertEquals(driver.getTitle(), "ShopLite - Login");
    }

    @Test(groups = "smoke", description = "Valid credentials should land the user on the dashboard")
    public void testValidLoginRedirectsToDashboard() {
        DashboardPage dashboard = loginPage().loginAs("student", "Workshop@123");
        Assert.assertEquals(dashboard.getWelcomeMessage(), "student");
    }

    @Test(groups = "regression", description = "Wrong password should show an inline error and stay on the login page")
    public void testInvalidPasswordShowsError() {
        LoginPage loginPage = loginPage().attemptLogin("student", "WrongPassword");

        Assert.assertTrue(loginPage.isErrorMessageDisplayed());
        Assert.assertEquals(loginPage.getErrorMessage(), "Invalid username or password.");
        Assert.assertEquals(driver.getTitle(), "ShopLite - Login", "user should not be navigated away");
    }

    @Test(groups = "regression", description = "Submitting with no username shows a field-level validation error")
    public void testEmptyUsernameShowsFieldError() {
        LoginPage loginPage = loginPage().attemptLogin("", "Workshop@123");
        Assert.assertEquals(loginPage.getUsernameFieldError(), "Username is required.");
    }

    @Test(groups = "regression", description = "Submitting with no password shows a field-level validation error")
    public void testEmptyPasswordShowsFieldError() {
        LoginPage loginPage = loginPage().attemptLogin("student", "");
        Assert.assertEquals(loginPage.getPasswordFieldError(), "Password is required.");
    }

    @Test(groups = "regression", description = "Clicking 'Forgot your password?' shows the reset message")
    public void testForgotPasswordLinkShowsMessage() {
        LoginPage loginPage = loginPage();
        loginPage.clickForgotPassword();
        Assert.assertTrue(loginPage.getErrorMessage().contains("Password reset link"));
    }

    @Test(groups = "regression", description = "The Remember me checkbox should toggle when clicked")
    public void testRememberMeCheckboxToggles() {
        LoginPage loginPage = loginPage();
        Assert.assertFalse(loginPage.isRememberMeChecked(), "should start unchecked");

        loginPage.toggleRememberMe();
        Assert.assertTrue(loginPage.isRememberMeChecked(), "should be checked after one click");
    }

    @Test(groups = "regression",
          description = "After 3 failed logins the account locks and the Login button is disabled")
    public void testAccountLocksAfterThreeFailedAttempts() {
        LoginPage loginPage = loginPage();

        for (int attempt = 1; attempt <= 3; attempt++) {
            loginPage.attemptLogin("student", "WrongPassword" + attempt);
        }

        Assert.assertFalse(loginPage.isLoginButtonEnabled(), "login button should be disabled after 3 failures");
        Assert.assertTrue(loginPage.getErrorMessage().contains("Account locked"));
    }
}
