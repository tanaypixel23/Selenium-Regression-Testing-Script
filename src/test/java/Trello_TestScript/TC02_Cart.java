package Trello_TestScript;

import commonTest.TestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class TC02_Cart extends TestBase {

    @BeforeMethod
    public void navigateToProducts() {
        driver.findElement(By.linkText("Products")).click();
    }
    @Test
    public void addSingleProductToCart() throws InterruptedException {

        List<WebElement> addButtons =
                driver.findElements(By.cssSelector(".btn-add-to-cart"));

        System.out.println("Add to cart buttons: " + addButtons.size());

        addButtons.get(0).click();

        Thread.sleep(1000);

        System.out.println("Current URL: " + driver.getCurrentUrl());

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement counter = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("cart-counter"))
        );

        Assert.assertEquals(counter.getText(), "1");
    }
    @Test
    public void addingMultipleProduct(){

        List<WebElement> addButtons =
                driver.findElements(By.cssSelector(".btn-add-to-cart"));

        System.out.println("Add to cart buttons: " + addButtons.size());
        addButtons.get(0).click();
        addButtons.get(7).click();
        addButtons.get(2).click();
        addButtons.get(3).click();
        addButtons.get(1).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement counter = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("cart-counter"))
        );
        System.out.println(counter.getText());
        Assert.assertTrue(Integer.parseInt(counter.getText()) > 1);
    }

    @Test
    public void addSameProducttwice() throws InterruptedException {

        List<WebElement> addButtons =
                driver.findElements(By.cssSelector(".btn-add-to-cart"));

        System.out.println("Add to cart buttons: " + addButtons.size());

        addButtons.get(0).click();
        WebElement cart = driver.findElement(By.linkText("Cart"));
        cart.click();
        Thread.sleep(2000);
        WebElement increase = driver.findElement(By.cssSelector(".quantity-btn.increase"));
        increase.click();
        Thread.sleep(2000);

        WebElement counter = driver.findElement(By.id("cart-counter"));

        Assert.assertEquals(counter.getText().trim(), "2");


    }
    @Test
    public void verifyCartAfterRefreshing() throws InterruptedException {

        List<WebElement> addButtons = driver.findElements(By.cssSelector(".btn-add-to-cart"));
        addButtons.get(0).click();

        Thread.sleep(1000);
        WebElement cart = driver.findElement(By.linkText("Cart"));
        cart.click();

        WebElement cartDetail = driver.findElement(By.className("cart-item-details"));
        String cartItemDetail = cartDetail.getText();
        System.out.println(cartItemDetail);
        Thread.sleep(1000);
        driver.navigate().refresh();
        Thread.sleep(1000);

        Assert.assertEquals(cartItemDetail,driver.findElement(By.className("cart-item-details")).getText());
    }

    @Test
    public void increaseAndDecreaseProdQuality() throws InterruptedException {

        List<WebElement> addButtons =
                driver.findElements(By.cssSelector(".btn-add-to-cart"));

        System.out.println("Add to cart buttons: " + addButtons.size());

        addButtons.get(0).click();
        WebElement cart = driver.findElement(By.linkText("Cart"));
        cart.click();
        Thread.sleep(2000);
        WebElement increase = driver.findElement(By.cssSelector(".quantity-btn.increase"));

        WebElement counter = driver.findElement(By.id("cart-counter"));

        increase.click();
        Thread.sleep(1000);
        Assert.assertTrue(Integer.parseInt(counter.getText()) > 1);
        WebElement decrease = driver.findElement(By.cssSelector(".quantity-btn.decrease"));
        decrease.click();
        Thread.sleep(1000);
        Assert.assertEquals(Integer.parseInt(counter.getText()), 1);
    }

    @Test
    public void verifyCartTotal() throws InterruptedException {

        // Get all Add to Cart buttons
        List<WebElement> addButtons =
                driver.findElements(By.cssSelector(".btn-add-to-cart"));

        // Add first two products
        addButtons.get(0).click();
        addButtons.get(1).click();

        // Open Cart
        driver.findElement(By.linkText("Cart")).click();

        Thread.sleep(1000);

        // Get product prices from cart
        List<WebElement> prices =
                driver.findElements(By.className("cart-item-price"));

        System.out.println(prices.get(0).getText());
        double price1 = Double.parseDouble( prices.get(0).getText().replace("$", "") );
        double price2 = Double.parseDouble( prices.get(1).getText().replace("$", "") );

        List<WebElement> increase_button = driver.findElements(By.cssSelector(".quantity-btn.increase"));

        increase_button.get(0).click();
        Thread.sleep(1000);

        double expectedSubtotal = (price1 * 2) + price2;
        double actualSubtotal = Double.parseDouble( driver.findElement(By.id("total")) .getText() .replace("$", "") );

        Assert.assertEquals( actualSubtotal, expectedSubtotal, 0.01, "Subtotal is incorrect" );

        double tax = Double.parseDouble( driver.findElement(By.id("tax")) .getText() .replace("$", "") );
        double shipping = Double.parseDouble( driver.findElement(By.id("shipping")) .getText() .replace("$", "") );

        double expectedTotal = expectedSubtotal + tax + shipping;
        double actualTotal = Double.parseDouble( driver.findElement(By.id("total-cost")) .getText() .replace("$", "") );

        Assert.assertEquals( actualTotal, expectedTotal, 0.01, "Final cart total is incorrect" );

    }

}
