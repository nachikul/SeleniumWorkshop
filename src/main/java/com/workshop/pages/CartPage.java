package com.workshop.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Page Object for webapp/cart.html (view cart, remove items, checkout). */
public class CartPage extends BasePage {

    private final By cartRows = By.cssSelector("#cart-body tr");
    private final By cartTotal = By.id("cart-total");
    private final By checkoutButton = By.id("checkout-btn");
    private final By orderSuccessMessage = By.id("order-success");
    private final By emptyCartMessage = By.id("empty-cart");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public int getCartItemRowCount() {
        return driver.findElements(cartRows).size();
    }

    public int getCartTotal() {
        return Integer.parseInt(textOf(cartTotal));
    }

    public boolean isEmptyCartMessageDisplayed() {
        return isDisplayed(emptyCartMessage);
    }

    public CartPage removeProduct(String productName) {
        By removeButton = By.xpath(
                "//tr[td[1][normalize-space()='" + productName + "']]//button[@class='remove-btn']");
        click(removeButton);
        return this;
    }

    public CartPage checkout() {
        click(checkoutButton);
        return this;
    }

    public String getOrderSuccessMessage() {
        return textOf(orderSuccessMessage);
    }

    public boolean isOrderSuccessDisplayed() {
        return isDisplayed(orderSuccessMessage);
    }
}
