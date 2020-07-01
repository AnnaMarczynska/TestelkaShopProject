import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class CouponTests {

WebDriver driver;
WebDriverWait wait;
Actions action;

@BeforeEach
public void driverSetUp(){
    System.setProperty("webdriver.chrome.driver","src/main/resources/chromedriver.exe");
    driver = new ChromeDriver();
    driver.manage().window().maximize();
    driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
    driver.navigate().to("https://fakestore.testelka.pl/");

    wait = new WebDriverWait(driver, 10);
    action = new Actions(driver);

    //hide info about demo page
    wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("a[class=\"woocommerce-store-notice__dismiss-link\"]"))));
    driver.findElement(By.cssSelector("a[class=\"woocommerce-store-notice__dismiss-link\"]")).click();
}

@AfterEach
public void afterFinishedTest(){
    driver.close();
    driver.quit();
}

public void addingProduct(){
    //adding first available product to cart
    driver.findElement(By.xpath(".//a[contains(text(), \"Dodaj do koszyka\")]")).click();

    //giving page time to refresh after adding products to cart
    wait.until(ExpectedConditions.stalenessOf(driver.findElement(By.cssSelector("span[class=\"woocommerce-Price-currencySymbol\"]"))));

    //moving to cart page
    driver.findElement(By.cssSelector("a[class=\"cart-contents\"]")).click();
}

@Test
public void addingCorrectCoupon(){
    addingProduct();
    String correctCoupon = "10procent";

    //typing coupon text
    driver.findElement(By.cssSelector("input[name=\"coupon_code\"]")).sendKeys(correctCoupon);

    //adding coupon
    driver.findElement(By.cssSelector("button[name=\"apply_coupon\"]")).click();

    //giving page time to refresh after adding coupon
    wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("div[class=\"woocommerce-notices-wrapper\"]"))));

    //alert massage
    WebElement alertMassage = driver.findElement(By.cssSelector("div[class=\"woocommerce-notices-wrapper\"]"));
    Assertions.assertTrue(alertMassage.getText().contains("Kupon został pomyślnie użyty"), "Coupon has not been added");
}

@Test
public void addingIncorrectCouponTest(){
    addingProduct();
    String incorrectCoupon = "100procent";

    //typing coupon text
    driver.findElement(By.cssSelector("input[name=\"coupon_code\"]")).sendKeys(incorrectCoupon);

    //adding coupon
    driver.findElement(By.cssSelector("button[name=\"apply_coupon\"]")).click();

    //giving page time to refresh after adding coupon
    wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("div[class=\"woocommerce-notices-wrapper\"]"))));

    //alert massage
    WebElement alertMassage = driver.findElement(By.cssSelector("div[class=\"woocommerce-notices-wrapper\"]"));
    Assertions.assertTrue(alertMassage.getText().contains("nie istnieje"), "Coupon was correct");
}

@Test
public void addingEmptyCouponTest(){
    addingProduct();

    //adding coupon
    driver.findElement(By.cssSelector("button[name=\"apply_coupon\"]")).click();

    //giving page time to refresh after adding coupon
    wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("div[class=\"woocommerce-notices-wrapper\"]"))));

    Assertions.assertTrue(driver.findElement(By.cssSelector("div[class=\"woocommerce-notices-wrapper\"]")).getText().contains("Proszę wpisać kod kuponu"),
            "Coupon has been entered");
}

@Test
public void addingCouponTwiceTest(){
    addingProduct();
    String correctCoupon = "10procent";

    //typing coupon text
    driver.findElement(By.cssSelector("input[name=\"coupon_code\"]")).sendKeys(correctCoupon);

    //adding coupon
    driver.findElement(By.cssSelector("button[name=\"apply_coupon\"]")).click();

    //giving page time to refresh after adding coupon
    wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("div[class=\"woocommerce-notices-wrapper\"]"))));

    //adding the same coupon once more
    driver.findElement(By.cssSelector("input[name=\"coupon_code\"]")).sendKeys(correctCoupon);
    driver.findElement(By.cssSelector("button[name=\"apply_coupon\"]")).click();

    wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("ul[class=\"woocommerce-error\"]")));
    wait.withTimeout(Duration.ofMillis(2000));

    //alert massage
    WebElement alertMassage = driver.findElement(By.cssSelector("ul[class=\"woocommerce-error\"]"));
    Assertions.assertTrue(alertMassage.getText().contains("Kupon został zastosowany"), "Coupon " + correctCoupon + " was not used twice");
}
}
