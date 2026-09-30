package com.workshop.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.stream.Collectors;

/** Page Object for webapp/dashboard.html (product search + add to cart). */
public class DashboardPage extends BasePage {

    private final By welcomeMessage = By.id("welcome-msg");
    private final By cartCount = By.id("cart-count");
    private final By searchBox = By.id("search-box");
    private final By searchButton = By.id("search-btn");
    private final By productCards = By.cssSelector(".product-card");
    private final By productNames = By.cssSelector(".product-card .product-name");
    private final By noResultsMessage = By.id("no-results");
    private final By logoutButton = By.id("logout-btn");
    private final By cartLink = By.id("cart-link");

    public DashboardPage(WebDriver driver) {
        super(driver);
    }

    public String getWelcomeMessage() {
        return textOf(welcomeMessage);
    }

    public int getCartCount() {
        return Integer.parseInt(textOf(cartCount));
    }

    public DashboardPage searchProduct(String term) {
        type(searchBox, term);
        click(searchButton);
        return this;
    }

    public List<String> getVisibleProductNames() {
        return driver.findElements(productNames).stream()
                .map(element -> element.getText())
                .collect(Collectors.toList());
    }

    public int getVisibleProductCount() {
        return driver.findElements(productCards).size();
    }

    public boolean isNoResultsMessageDisplayed() {
        return isDisplayed(noResultsMessage);
    }

    public DashboardPage addProductToCart(String productName) {
        By addButton = By.xpath(
                "//div[@class='product-card'][.//div[@class='product-name'][normalize-space()='"
                        + productName + "']]//button[@class='add-to-cart-btn']");
        click(addButton);
        return this;
    }

    public CartPage goToCart() {
        click(cartLink);
        return new CartPage(driver);
    }

    public LoginPage logout() {
        click(logoutButton);
        return new LoginPage(driver);
    }
}
