package commonTest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

import java.io.File;
import java.time.Duration;

import static java.sql.DriverManager.getDriver;

public class TestBase {
    public static WebDriver driver;
    public static WebDriverWait wait;
    public static File website;

    @BeforeMethod
    public void setupdriver() throws InterruptedException {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        driver.manage().window().maximize(); 
        driver.get("https://surajkumar-ibm.github.io/Selenium-Miniproject-Application/");

        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    @AfterMethod
    public void teardown(){
        driver.quit();
    }
    
 
    public static void clickUntilLoaded(By clickLocator) {

        for (int i = 0; i < 5; i++) {

            driver.findElement(clickLocator).click();

            try {
                Thread.sleep(2000);

                if (driver.findElements(By.cssSelector(".no-products")).isEmpty()) {
                    return; // Success
                }

                System.out.println("Backend did not load, retrying again...");

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        throw new RuntimeException("Backend did not load after 5 attempts.");
    }

}
