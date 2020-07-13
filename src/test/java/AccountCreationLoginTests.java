import org.junit.jupiter.api.Assertions;
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

    String correctEmail = "aaaaaa.mar@gmail.com";
    String incorrectEmail = "a.mar@gmail.com";
    String correctPassword = "c0rr3ctTestP@ssword";
    String incorrectPassword = "incorrectPassword";


    @Test
    public void creatingNewAccountTest() {
        driver.navigate().to("https://fakestore.testelka.pl/moje-konto/");
        testHelpers.closeInfoMassage();
        driver.findElement(By.cssSelector(registrationEmailFieldLocator)).sendKeys(correctEmail);
        driver.findElement(By.cssSelector(registrationPasswordFieldLocator)).sendKeys(correctPassword);

        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.cssSelector(registrationButtonLocator))));
        driver.findElement(By.cssSelector(registrationButtonLocator)).click();

        //checking if any error during creation of account
        List<WebElement> isAlertPresent = driver.findElements(By.cssSelector(alertMessageLocator));
        Assertions.assertTrue(isAlertPresent.size() == 0, "Account could not be created");

        //checking if moved to user profile page
        wait.withTimeout(Duration.ofMillis(2000));
        Assertions.assertTrue(driver.findElement(By.xpath(".//p[contains(text(), \"Witaj\")]")).isDisplayed(), "Account has not been created");

        testHelpers.afterFinishedTest();
    }

    @Test
    //for purpose of this test, testing account should already exist
    public void creatingAccountAlreadyExistingTest(){
        driver.navigate().to("https://fakestore.testelka.pl/moje-konto/");
        testHelpers.closeInfoMassage();
        driver.findElement(By.cssSelector(registrationEmailFieldLocator)).sendKeys(correctEmail);
        driver.findElement(By.cssSelector(registrationPasswordFieldLocator)).sendKeys(correctPassword);

        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.cssSelector(registrationButtonLocator))));
        driver.findElement(By.cssSelector(registrationButtonLocator)).click();

        List<WebElement> isAlertPresent = driver.findElements(By.cssSelector(alertMessageLocator));
        if(isAlertPresent.size() > 0){
            Assertions.assertTrue(driver.findElement(By.cssSelector(alertMessageLocator)).getText().contains("Zaloguj"), "Account has not been noticed as duplicate");
        }
    }

}
