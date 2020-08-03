import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.util.List;

public class AccountCreationLoginTests extends TestHelpers {

    TestHelpers testHelpers = new TestHelpers();

    //putting all locators in one place as strings in case one of them may change so manual change in dozen of places is prevented
    String registrationEmailFieldLocator = "input[id=\"reg_email\"]";
    String registrationPasswordFieldLocator = "input[id=\"reg_password\"]";
    String registrationButtonLocator = "button[name=\"register\"]";
    String loginEmailFieldLocator = "input[id=\"username\"]";
    String loginPasswordFieldLocator = "input[id=\"password\"]";
    String rememberMeOptionLocator = "input[name=\"rememberme\"]";
    String loginButtonLocator = "button[name=\"login\"]";
    String forgotPasswordOptionLocator = "p[class$=\"lost_password\"]>a";
    String alertMessageLocator = "ul[class=\"woocommerce-error\"]>li";
    String pageAfterLoginLocator = "h1[class^='entry']";
    String afterLogonLoginLocator = ".//div[@class='woocommerce-MyAccount-content']";

    String correctEmail = "aaaaaa.mar@gmail.com";
    String correctLogin = "aaaaaa.mar";
    String incorrectEmail = "a.mar@gmail.com";
    String incorrectLogin = "b.mar";
    String correctPassword = "c0rr3ctTestP@ssword";
    String incorrectPassword = "incorrectPassword";

    @BeforeAll
    public static void navigateToPage() {
        driver.navigate().to("https://fakestore.testelka.pl/moje-konto/");
    }

    @Test
    public void creatingNewAccountTest() {
        navigateToPage();
        testHelpers.closeInfoMassage();
        driver.findElement(By.cssSelector(registrationEmailFieldLocator)).sendKeys(correctEmail);
        driver.findElement(By.cssSelector(registrationPasswordFieldLocator)).sendKeys(correctPassword);

        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.cssSelector(registrationButtonLocator))));
        driver.findElement(By.cssSelector(registrationButtonLocator)).click();

        //checking if any error during creation of account
        List<WebElement> isAlertPresent = driver.findElements(By.cssSelector(alertMessageLocator));
        Assertions.assertEquals(0, isAlertPresent.size(), "Account could not be created");

        //checking if moved to user profile page
        wait.withTimeout(Duration.ofMillis(2000));
        Assertions.assertTrue(driver.findElement(By.xpath(".//p[contains(text(), \"Witaj\")]")).isDisplayed(), "Account has not been created");

        testHelpers.afterFinishedTest();
    }

    @Test
    //for purpose of this test, testing account should already exist
    public void creatingAccountAlreadyExistingTest() {
        driver.navigate().to("https://fakestore.testelka.pl/moje-konto/");
        testHelpers.closeInfoMassage();
        driver.findElement(By.cssSelector(registrationEmailFieldLocator)).sendKeys(correctEmail);
        driver.findElement(By.cssSelector(registrationPasswordFieldLocator)).sendKeys(correctPassword);

        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.cssSelector(registrationButtonLocator))));
        driver.findElement(By.cssSelector(registrationButtonLocator)).click();

        List<WebElement> isAlertPresent = driver.findElements(By.cssSelector(alertMessageLocator));
        if (isAlertPresent.size() > 0) {
            Assertions.assertTrue(driver.findElement(By.cssSelector(alertMessageLocator)).getText().contains("Zaloguj"), "Account has not been noticed as duplicate");
        }
    }

    @Test
    //for purpose of this test, testing account should already exist
    public void correctLoginAndPasswordTest() {
        navigateToPage();
        driver.findElement(By.cssSelector(loginEmailFieldLocator)).sendKeys(correctEmail);
        driver.findElement(By.cssSelector(loginPasswordFieldLocator)).sendKeys(correctPassword);
        driver.findElement(By.cssSelector(loginButtonLocator)).click();

        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector(pageAfterLoginLocator))));

        Assertions.assertTrue(driver.findElement(By.xpath(afterLogonLoginLocator)).getText().contains(correctLogin), "Proper user has not been logged in");
    }

    @Test
    //for purpose of this test, testing account should already exist
    public void correctLoginIncorrectPasswordTest() {
        navigateToPage();
        driver.findElement(By.cssSelector(loginEmailFieldLocator)).sendKeys(correctEmail);
        driver.findElement(By.cssSelector(loginPasswordFieldLocator)).sendKeys(incorrectPassword);
        driver.findElement(By.cssSelector(loginButtonLocator)).click();

        //checking if any error appears
        List<WebElement> isAlertPresent = driver.findElements(By.cssSelector(alertMessageLocator));
        Assertions.assertEquals(1, isAlertPresent.size(), "Login failed");

        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("ul[class=\"woocommerce-error\"]"))));

        Assertions.assertTrue(driver.findElement(By.cssSelector("ul[class=\"woocommerce-error\"]")).getText().contains("nieprawidłowe hasło"), "Not a password issue");
    }

    @Test
    //for purpose of this test, testing account should already exist
    public void incorrectLoginTest() {

    }

    @Test
    //for purpose of this test, testing account should already exist
    public void rememberMeOptionTest(){

    }

    @Test
    //for purpose of this test, testing account should already exist
    public void forgotPasswordTest(){

    }

}

