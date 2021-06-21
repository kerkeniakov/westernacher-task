import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import pages.HomePage;
import pages.LoginPage;
import utils.Constants;
import utils.FrameworkProperties;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static io.qameta.allure.Allure.step;


public class MultiLanguageTest {
    private WebDriver driver;
    static LoginPage loginPage;
    static HomePage homePage;
    static FrameworkProperties frameworkProperties;

    static Map<String, String> testData = new HashMap<String, String>();
    static {
        testData.put("(//nav//a)[1]", "Действия");
        testData.put("(//nav//a)[2]", "Отпуски");
        testData.put("//h2", "Личен отпуск");
        testData.put("//h3", "Отпуск на колеги");
        testData.put("//h4", "Легенда");
        testData.put("//app-leaves//button", "Добави");
        testData.put("(//div[contains(@class, 'btn-group')]//div)[1]", "Предишно");
        testData.put("(//div[contains(@class, 'btn-group')]//div)[2]", "Днес");
        testData.put("(//div[contains(@class, 'btn-group')]//div)[3]", "Следващо");
        testData.put("(//div[contains(@class, 'btn-group')]//div)[4]", "Месец");
        testData.put("(//div[contains(@class, 'btn-group')]//div)[5]", "Седмица");

    }
    @BeforeEach
    public void setup() throws URISyntaxException, MalformedURLException {
        frameworkProperties = new FrameworkProperties();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        if(Boolean.parseBoolean(frameworkProperties.getProperty(Constants.USE_LOCAL_CHROME_DRIVER))) {
            System.setProperty("webdriver.chrome.driver","src/test/resources/chromedriver.exe");
            this.driver = new ChromeDriver(options);
        }else{
            this.driver = new RemoteWebDriver(new URL(frameworkProperties.getProperty(Constants.SELENIUM_HUB)), options);
        }
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.manage().window().maximize();
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

    @Test
    @DisplayName("Simple login test case")
    public void LoginTest() {
        loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);

        step("1. Initialize WebDriver and navigate to Login Page\"", () -> {
            driver.get(frameworkProperties.getProperty("baseurl"));
        });

        step("2. Perform Login", () -> {
            loginPage.login(frameworkProperties.getProperty(Constants.USER_EMAIL), frameworkProperties.getProperty(Constants.USER_PASSWORD));
        });
        step("3. Change Language to Bulgarian", () -> {
            homePage.changeLanguage("Български");
        });
        step("3. Select account from navigation to verify that correct user real name is displayed", () -> {
            homePage.testLanguageTranslationOfElementsFromDataSet(testData);
        });
    }
}
