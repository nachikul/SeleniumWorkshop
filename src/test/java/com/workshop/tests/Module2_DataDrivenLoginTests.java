package com.workshop.tests;

import com.workshop.pages.LoginPage;
import com.workshop.utils.ConfigReader;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * MODULE 2 - Data-driven testing with TestNG's @DataProvider.
 *
 * Instead of writing one @Test method per credential combination, we write
 * the test logic ONCE and feed it many rows of data. Two flavors are shown:
 *  1. An inline, hardcoded DataProvider (quick and readable).
 *  2. A DataProvider that reads rows from a CSV file, which is how most
 *     real projects manage larger data sets.
 */
public class Module2_DataDrivenLoginTests extends BaseTest {

    @DataProvider(name = "inlineLoginData")
    public Object[][] inlineLoginData() {
        return new Object[][]{
                {"student", "Workshop@123", true},
                {"admin", "Admin@123", true},
                {"student", "wrongpass", false},
                {"", "Workshop@123", false},
        };
    }

    @Test(dataProvider = "inlineLoginData", groups = "regression",
          description = "Login outcome for several username/password combinations (inline data)")
    public void testLoginWithInlineData(String username, String password, boolean expectSuccess) {
        assertLoginOutcome(username, password, expectSuccess);
    }

    @DataProvider(name = "csvLoginData")
    public Object[][] csvLoginData() throws IOException {
        List<Object[]> rows = new ArrayList<>();
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("testdata/logindata.csv")) {
            if (in == null) {
                throw new IOException("testdata/logindata.csv not found on classpath");
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            String line = reader.readLine(); // skip header row
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                rows.add(new Object[]{parts[0], parts[1], Boolean.parseBoolean(parts[2])});
            }
        }
        return rows.toArray(new Object[0][]);
    }

    @Test(dataProvider = "csvLoginData", groups = "regression",
          description = "Login outcome for credential combinations sourced from a CSV file")
    public void testLoginWithCsvData(String username, String password, boolean expectSuccess) {
        assertLoginOutcome(username, password, expectSuccess);
    }

    private void assertLoginOutcome(String username, String password, boolean expectSuccess) {
        LoginPage loginPage = loginPage().attemptLogin(username, password);

        if (expectSuccess) {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.explicitWaitSeconds()));
            wait.until(ExpectedConditions.titleIs("ShopLite - Dashboard"));
            Assert.assertEquals(driver.getTitle(), "ShopLite - Dashboard",
                    "expected successful login for " + username + "/" + password);
        } else {
            boolean hasFormError = loginPage.isErrorMessageDisplayed()
                    || !loginPage.getUsernameFieldError().isEmpty()
                    || !loginPage.getPasswordFieldError().isEmpty();
            Assert.assertTrue(hasFormError,
                    "expected a validation/error message for " + username + "/" + password);
        }
    }
}
