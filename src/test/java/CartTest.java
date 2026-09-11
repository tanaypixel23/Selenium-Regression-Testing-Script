import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.devtools.idealized.Javascript;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.time.Duration;

public class CartTest {
    WebDriver driver;
    SoftAssert softAssert=new SoftAssert();

    @BeforeMethod
    public void setup(){
        driver=new EdgeDriver();
    }
    @Test
    public void verifyEmptyCartState(){

        driver.get("https://ibrahim2656.github.io/E-commerce-Site/cart.html");
        // we want selenium to execut javascript inside the webpage localStorage.removeItem("miniMartCart") is js not selenium
        JavascriptExecutor js=(JavascriptExecutor) driver;
        js.executeScript("localStorage.removeItem('miniMartCart');");
        driver.navigate().refresh();
        //verify empty cart message


        // Assert.assertEquals(driver.findElement(By.id("#cart-item-counter")).getText(),"Your Cart is empty");
        //Verify item count
        softAssert.assertEquals(driver.findElement(By.id("cart-item-counter")).getText(),"0 items");
        softAssert.assertEquals(driver.findElement(By.id("total")).getText(),"$0.00");
        softAssert.assertEquals(driver.findElement(By.id("shipping")).getText(),"$0.00");
        softAssert.assertEquals(driver.findElement(By.id("tax")).getText(),"$0.00");
        softAssert.assertEquals(driver.findElement(By.id("total-cost")).getText(),"$0.00");
        //verify checkout is disabled
        softAssert.assertFalse(driver.findElement(By.id("btn-checkout")).isEnabled());
        softAssert.assertAll();
    }
    @Test
    public void verifyProductCanBeAddedToCart(){
        driver.get("https://ibrahim2656.github.io/E-commerce-Site/products.html");

        WebDriverWait wait=new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement button=wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".btn-add-to-cart")));
        button.click();
    }
    @Test
    public void verifyContinueShoppingNavigation(){
        driver.get("https://ibrahim2656.github.io/E-commerce-Site/cart.html");
        driver.findElement(By.linkText("Continue Shopping")).click();
        String currentUrl= driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("products.html"));


    }
    @Test
    public void VerifyStartShoppingNavigationFromEmptyCart(){
        driver.get("https://ibrahim2656.github.io/E-commerce-Site/cart.html");
        JavascriptExecutor js=(JavascriptExecutor) driver;
        js.executeScript("localStorage.removeItem('minMartCart');");
        driver.navigate().refresh();

    }
    @Test
    public void VerifyCheckoutwithProductsinCart(){
        driver.get("https://ibrahim2656.github.io/E-commerce-Site/products.html");
        WebDriverWait wait=new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement button=wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".btn-add-to-cart")));
        button.click();
        driver.navigate().to("https://ibrahim2656.github.io/E-commerce-Site/cart.html");
        WebElement checkout=driver.findElement(By.id("btn-checkout"));
        Assert.assertTrue(checkout.isEnabled());
        checkout.click();
        String actual=driver.getCurrentUrl();
        Assert.assertTrue(actual.contains("checkout.html"));

    }
    @Test
    public void VerifyOrderSummaryCalculation(){
        driver.get("https://ibrahim2656.github.io/E-commerce-Site/products.html");
        WebDriverWait wait= new WebDriverWait(driver,Duration.ofSeconds(10));
        WebElement productCard=wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".product-card")));
        String priceText=productCard.findElement(By.cssSelector(".product-price-final")).getText();
        //String discountText=productCard.findElement(By.cssSelector(".product-card-discount")).getText();
        double price=Double.parseDouble(priceText.replace("$",""));
        WebElement addToCart=productCard.findElement(By.cssSelector(".btn-add-to-cart"));
        addToCart.click();
        driver.navigate().to("https://ibrahim2656.github.io/E-commerce-Site/cart.html");
        String quantityText=driver.findElement(By.cssSelector(".quantity")).getText();
        int quantity=Integer.parseInt(quantityText);
        double expectedSubtotal=price*quantity;
        double taxRate=0.14;
        double expectedTax=expectedSubtotal*taxRate;
        double shipping=10.00;
        double expectedTotal=expectedSubtotal+expectedTax+shipping;
        String totalText=driver.findElement(By.id("total-cost")).getText();
        double actualTotal=Double.parseDouble(totalText.replace("$",""));
        Assert.assertEquals(actualTotal,expectedTotal,0.01);


    }
    @Test
    public void VerifyDiscountedProductPriceIsDisplayedCorrectly(){
        driver.get("https://ibrahim2656.github.io/E-commerce-Site/products.html");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement productCard = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector(".product-card")));

        String discountText = productCard.findElement(
                        By.cssSelector(".product-card-discount"))
                .getText();

        String originalText = productCard.findElement(
                        By.cssSelector(".product-card-previous-price"))
                .getText();

        String finalPriceText = productCard.findElement(
                        By.cssSelector(".product-price-final"))
                .getText();


        double discount = Double.parseDouble(discountText.replace("%", "")
                .replace("-", "").trim());

        double originalPrice = Double.parseDouble(
                originalText.replace("$", "").trim());

        double actualFinalPrice = Double.parseDouble(
                finalPriceText.replace("$", "").trim());

        double expectedFinalPrice =
                originalPrice * (1 - discount / 100.0);

        Assert.assertEquals(
                actualFinalPrice,
                expectedFinalPrice,
                0.01);


    }

    //@AfterMethod
    //public void tearDown(){
    // driver.quit();
    //}

}

