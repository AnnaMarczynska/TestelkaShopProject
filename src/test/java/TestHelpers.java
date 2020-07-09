import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class TestHelpers {

    public static WebDriver driver;
    public static WebDriverWait wait;
    public static Actions action;

    String demoInfoMessage = "a[class=\"woocommerce-store-notice__dismiss-link\"]";

    @BeforeAll
    public static void setDriver() {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 10);
        action = new Actions(driver);
    }

    @BeforeEach
    public void driverSetUp() {
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
        driver.navigate().to("https://fakestore.testelka.pl/");
    }

    @AfterEach
    public void afterFinishedTest() {
        driver.quit();
    }

    public void closeInfoMassage() {
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector(demoInfoMessage))));
        driver.findElement(By.cssSelector(demoInfoMessage)).click();
    }

}
