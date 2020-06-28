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
import org.openqa.selenium.Keys;

import java.util.concurrent.TimeUnit;

public class ShopTests {

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

    @Test
    public void addProductToCartTest(){
        //go to product page by clicking the image
        //driver.findElement(By.cssSelector("img[alt=\"Yoga i pilates\"]")).click();

        //go to product page by clicking the link
        driver.findElement(By.xpath(".//h2[contains(text(), \"Yoga i pilates\")]")).click();

        //selecting fist product on page
        driver.findElement(By.cssSelector("ul[class=\"products columns-3\"]>li")).click();

        String productName = driver.findElement(By.cssSelector("h1[class^=\"product_title\"]")).getText();

        //adding product to cart
        driver.findElement(By.cssSelector("button[name=\"add-to-cart\"]")).click();

        //checking if product was add to cart
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("div[class=\"woocommerce-message\"]"))));

        //by checking alert
        //Assertions.assertTrue(driver.findElement(By.cssSelector("div[class=\"woocommerce-message\"]")).getText().contains("został dodany do koszyka"),
        //       "Product was not add to the cart");

        //by going to cart
        driver.findElement(By.cssSelector("div[class=\"woocommerce-message\"]>a")).click();
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("td[class=\"product-remove\"]"))));
        Assertions.assertTrue(driver.findElement(By.cssSelector("article[id=\"post-6\"]")).getText().contains(productName),
                "Product " + productName + " was not add to the cart");
    }

    @Test
    public void addingManyProductsToCart(){
        //go to product page by clicking the link
        driver.findElement(By.xpath(".//h2[contains(text(), \"Yoga i pilates\")]")).click();

        //adding fist product on page to cart
        driver.findElement(By.cssSelector("a[data-product_id=\"61\"]")).click();

        //adding third product on page to cart
        driver.findElement(By.cssSelector("a[data-product_id=\"60\"]")).click();

        //giving page time to refresh after adding products to cart
        wait.until(ExpectedConditions.stalenessOf(driver.findElement(By.cssSelector("a>span"))));

        //moving to cart page
        driver.findElement(By.className("cart-contents")).click();

        //getting number of products in cart
        int numberOfProductsInCart = driver.findElements(By.xpath(".//td[@class=\"product-name\"]")).size();

        Assertions.assertTrue(numberOfProductsInCart>1, "Number of products in cart is lower than 2");
    }

    @Test
    public void addingManyItemsOfProductToCart(){
        //go to product page by clicking the link
        driver.findElement(By.xpath(".//h2[contains(text(), \"Yoga i pilates\")]")).click();

        //adding fist product on page to cart
        driver.findElement(By.cssSelector("a[data-product_id=\"61\"]")).click();

        //giving page time to refresh after adding products to cart
        wait.until(ExpectedConditions.stalenessOf(driver.findElement(By.cssSelector("a>span"))));

        //moving to cart page
        driver.findElement(By.className("cart-contents")).click();

        //adding another item to cart from cart page
        driver.findElement(By.cssSelector("input[type=\"number\"]")).sendKeys(Keys.ARROW_UP);

        driver.findElement(By.cssSelector("button[name=\"update_cart\"]")).click();

        //getting number of items of product in cart
        WebElement productInCart = driver.findElement(By.cssSelector("input[type=\"number\"]"));
        int numberOfItems = Integer.parseInt(productInCart.getAttribute("value"));
        Assertions.assertTrue(numberOfItems>1, "Number of items of product is lower than 2");

    }

}
