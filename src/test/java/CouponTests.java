import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

public class CouponTests extends TestHelpers {

    TestHelpers testHelpers = new TestHelpers();

    String addFirstProductToCartButtonLocator = ".//a[contains(text(), \"Dodaj do koszyka\")]";
    String priceInformationLocator = "a[class=\"cart-contents\"]>span";
    String cartContentButtonLocator = "a[class=\"cart-contents\"]";
    String couponInputLocator = "input[name=\"coupon_code\"]";
    String addCouponButtonLocator = "button[name=\"apply_coupon\"]";
    String cartContentTableLocator = "div[class=\"woocommerce-notices-wrapper\"]";
    String alertMessageTextLocator = "ul[class=\"woocommerce-error\"]";

    public void addingProduct() {
        //adding first available product to cart
        driver.findElement(By.xpath(addFirstProductToCartButtonLocator)).click();

        //giving page time to refresh after adding products to cart
        wait.until(ExpectedConditions.stalenessOf(driver.findElement(By.cssSelector(priceInformationLocator))));

        //moving to cart page
        driver.findElement(By.cssSelector(cartContentButtonLocator)).click();
    }

    @Test
    public void addingCorrectCoupon() {
        testHelpers.driverSetUp();
        testHelpers.closeInfoMassage();

        addingProduct();
        String correctCoupon = "10procent";

        //typing coupon text
        driver.findElement(By.cssSelector(couponInputLocator)).sendKeys(correctCoupon);

        //adding coupon
        driver.findElement(By.cssSelector(addCouponButtonLocator)).click();

        //giving page time to refresh after adding coupon
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector(cartContentTableLocator))));

        //alert massage
        WebElement alertMessage = driver.findElement(By.cssSelector(cartContentTableLocator));
        Assertions.assertTrue(alertMessage.getText().contains("Kupon został pomyślnie użyty"), "Coupon has not been added");

        testHelpers.afterFinishedTest();
    }

    @Test
    public void addingIncorrectCouponTest() {
        testHelpers.driverSetUp();
        testHelpers.closeInfoMassage();

        addingProduct();
        String incorrectCoupon = "100procent";

        //typing coupon text
        driver.findElement(By.cssSelector(couponInputLocator)).sendKeys(incorrectCoupon);

        //adding coupon
        driver.findElement(By.cssSelector(addCouponButtonLocator)).click();

        //giving page time to refresh after adding coupon
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector(cartContentTableLocator))));

        //alert massage
        WebElement alertMessage = driver.findElement(By.cssSelector(cartContentTableLocator));
        Assertions.assertTrue(alertMessage.getText().contains("nie istnieje"), "Coupon was correct");

        testHelpers.afterFinishedTest();
    }

    @Test
    public void addingEmptyCouponTest() {
        testHelpers.driverSetUp();
        testHelpers.closeInfoMassage();

        addingProduct();

        //adding coupon
        driver.findElement(By.cssSelector(addCouponButtonLocator)).click();

        //giving page time to refresh after adding coupon
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector(cartContentTableLocator))));

        Assertions.assertTrue(driver.findElement(By.cssSelector(cartContentTableLocator)).getText().contains("Proszę wpisać kod kuponu"),
                "Coupon has been entered");

        testHelpers.afterFinishedTest();
    }

    @Test
    public void addingCouponTwiceTest() {
        testHelpers.driverSetUp();
        testHelpers.closeInfoMassage();

        addingProduct();
        String correctCoupon = "10procent";

        //typing coupon text
        driver.findElement(By.cssSelector(couponInputLocator)).sendKeys(correctCoupon);

        //adding coupon
        driver.findElement(By.cssSelector(addCouponButtonLocator)).click();

        //giving page time to refresh after adding coupon
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector(cartContentTableLocator))));

        //adding the same coupon once more
        driver.findElement(By.cssSelector(couponInputLocator)).sendKeys(correctCoupon);
        driver.findElement(By.cssSelector(addCouponButtonLocator)).click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(alertMessageTextLocator)));
        wait.withTimeout(Duration.ofMillis(2000));

        //alert massage
        WebElement alertMessage = driver.findElement(By.cssSelector(alertMessageTextLocator));
        Assertions.assertTrue(alertMessage.getText().contains("Kupon został zastosowany"), "Coupon " + correctCoupon + " was not used twice");

        testHelpers.afterFinishedTest();
    }
}
