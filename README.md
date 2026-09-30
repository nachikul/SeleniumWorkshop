# Selenium WebDriver + TestNG Workshop

A hands-on workshop codebase for teaching **Selenium WebDriver** and **TestNG**
to MCA students. Instead of automating a live, unstable site (Amazon.in
changes its DOM constantly and shows CAPTCHAs to bots), the workshop ships
with **ShopLite** — a tiny, self-contained "Amazon.in-style" demo site
(login → search → add to cart → checkout) under [`webapp/`](webapp). It runs
entirely from static HTML/CSS/JS with no backend, so it never flakes due to
network issues and students can read exactly what they're automating.

## What's inside

```
webapp/                  the application under test ("ShopLite")
  login.html              login form (valid/invalid login, lockout, forgot password)
  dashboard.html           product search + add to cart
  cart.html                 cart view, remove item, checkout

src/main/java/com/workshop/
  pages/                   Page Object Model classes (LoginPage, DashboardPage, CartPage)
  utils/                   DriverFactory (creates/quits WebDriver), ConfigReader

src/test/java/com/workshop/
  tests/BaseTest.java                       shared @BeforeMethod/@AfterMethod setup
  tests/Module1_LoginTests.java             Selenium + TestNG fundamentals
  tests/Module2_DataDrivenLoginTests.java   @DataProvider (inline + CSV-driven)
  tests/Module3_ShoppingFlowTests.java      multi-page E2E flow, priority, dependsOnMethods
  listeners/TestListener.java               screenshot-on-failure listener

testng.xml                main suite (all tests, parallel="classes")
testng-smoke.xml           demo of running only the "smoke" group
```

## Prerequisites

- JDK 11+ (`java -version`)
- Maven 3.6+ (`mvn -version`)
- Google Chrome installed (WebDriverManager downloads the matching
  ChromeDriver automatically — no manual driver setup)

## Running the tests

```bash
mvn test
```

This compiles the project and runs `testng.xml` (configured as the default
suite in `pom.xml`). To run only the smoke-tagged tests:

```bash
mvn test -DsuiteXmlFile=testng-smoke.xml
```

Test results: `target/surefire-reports/`. Failure screenshots (captured by
`TestListener`): `test-output/screenshots/`.

By default `headless=false` in `src/test/resources/config.properties`, so
the browser window is visible — recommended while teaching, so students can
watch Selenium drive the browser. Set it to `true` to run silently/faster
(e.g. on a CI machine or a lab PC without a display).

## Suggested workshop flow (see the companion slide deck)

1. **Explore `webapp/login.html`** in a normal browser first — students
   should understand the app before automating it. Demo credentials:
   `student` / `Workshop@123`.
2. **Module 1 — fundamentals**: open `Module1_LoginTests.java`. Walk through
   `@Test`, locators (`By.id`), explicit waits, and TestNG assertions.
3. **Page Object Model**: open `pages/LoginPage.java` next to the test to
   show *why* the test reads like plain English — the test never touches a
   `By` locator directly.
4. **Module 2 — data-driven testing**: `@DataProvider`, first inline, then
   reading rows from `testdata/logindata.csv`.
5. **Module 3 — a realistic flow**: chaining Page Objects across three pages
   (login → dashboard → cart), `priority`, `dependsOnMethods`, `groups`.
6. **testng.xml**: suites, `<classes>`, `<groups>`, `parallel="classes"`.
7. **Listeners**: `TestListener.java` — screenshot on failure, without
   touching any test code.
8. **Exercise for students**: add a `Module4_...Tests.java` that tests the
   "no search results" empty state, or extends `CartPage` to assert removing
   the *last* item shows the empty-cart message — both are one new Page
   Object method plus one new `@Test` away.

## Adapting this to a real site (e.g. Amazon.in)

The Page Object Model here is deliberately identical in shape to what you'd
write against a real e-commerce site: a `LoginPage`, a product/search page,
a cart page, each exposing user-level methods. To point this framework at a
real site instead of `webapp/`, you would only need to:

- Change `ConfigReader.webappUrl(...)` calls to a real base URL.
- Update the `By` locators in each Page Object to match that site's DOM.

Live public sites are **not recommended for a classroom** because their
markup changes without notice, they may show CAPTCHAs or rate-limit
automated traffic, and flaky network/page-load timing makes it much harder
to teach waits and assertions reliably. `webapp/` gives students the same
skills without that flakiness.
