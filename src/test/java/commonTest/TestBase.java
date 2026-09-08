package commonTest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

import java.io.File;

import static java.sql.DriverManager.getDriver;

public class TestBase {
    public static WebDriver driver;
    public static File website;

    @BeforeMethod
    public void setupdriver() throws InterruptedException {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://surajkumar-ibm.github.io/Selenium-Miniproject-Application/");
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
