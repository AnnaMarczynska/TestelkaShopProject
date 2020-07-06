import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.Keys;

public class AddProductToCartTests extends TestHelpers {

TestHelpers testHelpers = new TestHelpers();

//putting all locators in one place as strings in case one of them may change so manual change in dozen of places is prevented
    String productImageLocator = "img[alt=\"Yoga i pilates\"]";
    String productLinkLocator = ".//h2[contains(text(), \"Yoga i pilates\")]";
    String firstProductLocator = "ul[class=\"products columns-3\"]>li";
    String addProductButtonLocator = "button[name=\"add-to-cart\"]";
    String alertMassage = "div[class=\"woocommerce-message\"]";
    String removeProductIconLocator = "td[class=\"product-remove\"]";
    String cartContainerLocator = "article[id=\"post-6\"]";
    String thirdProductLocator = "a[data-product_id=\"60\"]";
    String cartsProductsValueLocator = "a>span";
    String productsInCartLocator = ".//td[@class=\"product-name\"]";
    String updateCartButtonLocator = "\"button[name=\\\"update_cart\\\"]\"";

@Test
public void addProductToCartTest(){
    testHelpers.driverSetUp();
    testHelpers.closeInfoMassage();

    //go to product page by clicking the image
    driver.findElement(By.cssSelector(productImageLocator)).click();

    //go to product page by clicking the link
    //driver.findElement(By.xpath(productLinkLocator)).click();

    //selecting fist product on page
    driver.findElement(By.cssSelector(firstProductLocator)).click();

    String productName = driver.findElement(By.cssSelector("h1[class^=\"product_title\"]")).getText();

    //adding product to cart
    driver.findElement(By.cssSelector(addProductButtonLocator)).click();

    //checking if product was add to cart
    wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector(alertMassage))));

    //by checking alert
    //Assertions.assertTrue(driver.findElement(By.cssSelector(alertMassage)).getText().contains("został dodany do koszyka"),
    //       "Product was not add to the cart");

    //by going to cart
    driver.findElement(By.cssSelector("div[class=\"woocommerce-message\"]>a")).click();
    wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector(removeProductIconLocator))));
    Assertions.assertTrue(driver.findElement(By.cssSelector(cartContainerLocator)).getText().contains(productName),
            "Product " + productName + " was not add to the cart");

    testHelpers.afterFinishedTest();
}

@Test
public void addingManyProductsToCart(){
    testHelpers.driverSetUp();
    testHelpers.closeInfoMassage();

    //go to product page by clicking the link
    driver.findElement(By.xpath(productLinkLocator)).click();

    //adding fist product on page to cart
    driver.findElement(By.cssSelector(firstProductLocator)).click();

    //adding third product on page to cart
    driver.findElement(By.cssSelector(thirdProductLocator)).click();

    //giving page time to refresh after adding products to cart
    wait.until(ExpectedConditions.stalenessOf(driver.findElement(By.cssSelector(cartsProductsValueLocator))));

    //moving to cart page
    driver.findElement(By.className("cart-contents")).click();

    //getting number of products in cart
    int numberOfProductsInCart = driver.findElements(By.xpath(productsInCartLocator)).size();

    Assertions.assertTrue(numberOfProductsInCart>1, "Number of products in cart is lower than 2");

    testHelpers.afterFinishedTest();
}

@Test
public void addingManyItemsOfProductToCart(){
    testHelpers.driverSetUp();
    testHelpers.closeInfoMassage();

    //go to product page by clicking the link
    driver.findElement(By.xpath(productLinkLocator)).click();

    //adding fist product on page to cart
    driver.findElement(By.cssSelector(firstProductLocator)).click();

    //giving page time to refresh after adding products to cart
    wait.until(ExpectedConditions.stalenessOf(driver.findElement(By.cssSelector(cartsProductsValueLocator))));

    //moving to cart page
    driver.findElement(By.className("cart-contents")).click();

    //adding another item to cart from cart page
    driver.findElement(By.cssSelector("input[type=\"number\"]")).sendKeys(Keys.ARROW_UP);

    driver.findElement(By.cssSelector(updateCartButtonLocator)).click();

    //getting number of items of product in cart
    WebElement productInCart = driver.findElement(By.cssSelector("input[type=\"number\"]"));
    int numberOfItems = Integer.parseInt(productInCart.getAttribute("value"));
    Assertions.assertTrue(numberOfItems>1, "Number of items of product is lower than 2");
}

}
