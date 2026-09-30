package com.workshop.tests;

import com.workshop.pages.CartPage;
import com.workshop.pages.DashboardPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

/**
 * MODULE 3 - A realistic end-to-end flow across multiple pages, modeled
 * after a typical e-commerce site (search -> add to cart -> checkout),
 * the same shape of flow you'd automate on a real site like Amazon.in.
 *
 * Concepts covered: chaining Page Objects (each page method returns the
 * next page), TestNG `priority` to control ordering within a class, and
 * `dependsOnMethods` to skip a test if a prerequisite step already failed.
 */
public class Module3_ShoppingFlowTests extends BaseTest {

    private DashboardPage dashboardAfterLogin() {
        return loginPage().loginAs("student", "Workshop@123");
    }

    @Test(groups = "smoke", priority = 1,
          description = "Searching narrows the product grid to matching items")
    public void testSearchFiltersProducts() {
        DashboardPage dashboard = dashboardAfterLogin().searchProduct("Keyboard");

        List<String> visibleNames = dashboard.getVisibleProductNames();
        Assert.assertEquals(visibleNames.size(), 1);
        Assert.assertTrue(visibleNames.get(0).contains("Keyboard"));
    }

    @Test(groups = "regression", priority = 1,
          description = "Searching for a product that doesn't exist shows the empty-state message")
    public void testSearchWithNoMatchesShowsEmptyState() {
        DashboardPage dashboard = dashboardAfterLogin().searchProduct("Smartphone");

        Assert.assertEquals(dashboard.getVisibleProductCount(), 0);
        Assert.assertTrue(dashboard.isNoResultsMessageDisplayed());
    }

    @Test(groups = "smoke", priority = 2,
          description = "Adding a product updates the cart badge count")
    public void testAddProductToCartUpdatesBadge() {
        DashboardPage dashboard = dashboardAfterLogin();
        Assert.assertEquals(dashboard.getCartCount(), 0, "cart should start empty");

        dashboard.addProductToCart("Wireless Mouse");

        Assert.assertEquals(dashboard.getCartCount(), 1);
    }

    @Test(groups = "regression", priority = 2,
          description = "Adding multiple different products accumulates the cart count")
    public void testAddMultipleProductsAccumulatesCount() {
        DashboardPage dashboard = dashboardAfterLogin();

        dashboard.addProductToCart("Coffee Mug");
        dashboard.addProductToCart("Desk Lamp");
        dashboard.addProductToCart("Laptop Stand");

        Assert.assertEquals(dashboard.getCartCount(), 3);
    }

    @Test(groups = "smoke", priority = 3,
          description = "Full flow: search, add to cart, view cart, remove an item, checkout")
    public void testCompleteShoppingAndCheckoutFlow() {
        DashboardPage dashboard = dashboardAfterLogin();

        dashboard.addProductToCart("Selenium WebDriver Book");
        dashboard.addProductToCart("TestNG Cookbook");

        CartPage cart = dashboard.goToCart();
        Assert.assertEquals(cart.getCartItemRowCount(), 2);
        Assert.assertEquals(cart.getCartTotal(), 799 + 649);

        cart.removeProduct("TestNG Cookbook");
        Assert.assertEquals(cart.getCartItemRowCount(), 1);
        Assert.assertEquals(cart.getCartTotal(), 799);

        cart.checkout();
        Assert.assertTrue(cart.isOrderSuccessDisplayed());
        Assert.assertTrue(cart.getOrderSuccessMessage().contains("Order placed successfully"));
        Assert.assertTrue(cart.isEmptyCartMessageDisplayed(), "cart should be cleared after checkout");
    }

    @Test(groups = "smoke", priority = 4,
          dependsOnMethods = "testCompleteShoppingAndCheckoutFlow",
          description = "Logging out returns the user to the login page")
    public void testLogoutReturnsToLoginPage() {
        DashboardPage dashboard = dashboardAfterLogin();
        dashboard.logout();

        Assert.assertEquals(driver.getTitle(), "ShopLite - Login");
    }
}
