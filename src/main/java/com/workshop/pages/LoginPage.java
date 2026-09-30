package com.workshop.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object for webapp/login.html.
 *
 * Every public method reads like a step a real user would take
 * ("login", "enter username") — tests should never need to know
 * about By locators directly; that detail stays encapsulated here.
 */
public class LoginPage extends BasePage {

    private final By usernameInput = By.id("username");
    private final By passwordInput = By.id("password");
    private final By rememberMeCheckbox = By.id("remember-me");
    private final By loginButton = By.id("login-btn");
    private final By errorMessage = By.id("error-message");
    private final By usernameFieldError = By.id("username-error");
    private final By passwordFieldError = By.id("password-error");
    private final By forgotPasswordLink = By.id("forgot-password-link");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage enterUsername(String username) {
        type(usernameInput, username);
        return this;
    }

    public LoginPage enterPassword(String password) {
        type(passwordInput, password);
        return this;
    }

    public LoginPage toggleRememberMe() {
        click(rememberMeCheckbox);
        return this;
    }

    public boolean isRememberMeChecked() {
        return driver.findElement(rememberMeCheckbox).isSelected();
    }

    public DashboardPage clickLogin() {
        click(loginButton);
        return new DashboardPage(driver);
    }

    /** Convenience method combining the three steps above for the common case. */
    public DashboardPage loginAs(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        return clickLogin();
    }

    /** Submits the form without navigating away, for negative-path tests. */
    public LoginPage attemptLogin(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        click(loginButton);
        return this;
    }

    public String getErrorMessage() {
        return textOf(errorMessage);
    }

    public boolean isErrorMessageDisplayed() {
        return isDisplayed(errorMessage);
    }

    public String getUsernameFieldError() {
        return textOf(usernameFieldError);
    }

    public String getPasswordFieldError() {
        return textOf(passwordFieldError);
    }

    public boolean isLoginButtonEnabled() {
        return driver.findElement(loginButton).isEnabled();
    }

    public void clickForgotPassword() {
        click(forgotPasswordLink);
    }
}
