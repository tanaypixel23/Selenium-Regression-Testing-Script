package commonTest;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import java.io.File;

import static java.sql.DriverManager.getDriver;

public class TestBase {
    public static WebDriver driver;
    public static File website;

    @BeforeTest
    public void setupdriver() throws InterruptedException {
        driver = new ChromeDriver();
        driver.get("https://surajkumar-ibm.github.io/Selenium-Miniproject-Application/");
    }

    @AfterTest
    public void teardown(){
        driver.quit();
    }
}
