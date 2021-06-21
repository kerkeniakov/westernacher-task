import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.HomePage;
import pages.LoginPage;
import utils.Constants;
import utils.FrameworkProperties;
import utils.Utils;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import static io.qameta.allure.Allure.step;

public class LeaveCRUDTest {
    private WebDriver driver;
    static LoginPage loginPage;
    static HomePage homePage;
    static FrameworkProperties frameworkProperties;

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
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        driver.manage().window().maximize();
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

    @Test
    @DisplayName("Create/Read/Update/Delete a leave")
    public void LeaveCRUD() throws InterruptedException {
        loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        step("1. Initialize WebDriver and navigate to Login Page\"", () -> {
            driver.get(frameworkProperties.getProperty("baseurl"));
        });
        step("2. Perform Login", () -> {
            loginPage.login(frameworkProperties.getProperty(Constants.USER_EMAIL), frameworkProperties.getProperty(Constants.USER_PASSWORD));
        });

        step("3. Deleting any existing leave requests that might interfere with the testcase", () -> {
            homePage.deleteAllLeaveRequests();
        });
        step("4. Click on Add button to add a new leave", () -> {
            homePage.addLeaveButton.click();
            WebDriverWait wait = new WebDriverWait(driver, 5);
            wait.until(ExpectedConditions.visibilityOf(homePage.dialogPopUpContainer));
            Assertions.assertTrue(homePage.dialogPopUpContainer.isDisplayed());
        });
        step("5. Populate leave with FROM date in modal window", () -> {
            homePage.leaveStartDate.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
            homePage.leaveStartDate.sendKeys(Utils.getCurrentDate());
        });
        step("6. Populate leave with TO date in modal window", () -> {
            homePage.leaveEndDate.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
            homePage.populateLeaveToField();
        });
        step("7. Submit leave request by clicking Add in the modal window", () -> {
            homePage.selectDialogButton("Add");
        });
        step("8. Validate that correct data is displayed in leave table row", () -> {
            homePage.waitForButtonVisible("Edit", 10);
            homePage.assertLeaveTableIsRenderedWithCorrectDataRows(Utils.getLeaveRowData());
        });
        step("9. Edit leave request -> add one more day to toDate (extend leave period by 1 day)", () -> {
            homePage.returnButtonByText("Edit").click();
            homePage.editDateAddOneDay();
            homePage.selectDialogButton("Save");
        });
        step("10. Validate that leave table data has updated correctly with the extra day", () -> {
            homePage.waitForButtonVisible("Edit", 10);
            homePage.validateDaysCountInTable("3");
        });
        step("11. Approve leave request", () -> {
            homePage.returnButtonByText("Request").click();
            homePage.waitForAlertMessage("successfully added.",10);
        });
        step("12. Check that leave status has been updated to Accepted", () -> {
            homePage.validateStatusTextInTable("Accepted");
        });
        step("13. Refresh and click on today to verify Leave requestee's name is shown in the calendar", () -> {
            driver.navigate().refresh();
            homePage.clickOnTodayInCalendar();
            homePage.personNameIsShownOnCalendarDayClick(frameworkProperties.getProperty(Constants.USER_NAME),true);
        });
        step("14. Click on Export and wait for Print button to be visible", () -> {
            homePage.returnButtonByText("Export").click();
            homePage.waitForButtonVisible("Print", 10);
            homePage.returnButtonByText("Cancel").click();
        });
        step("15. Delete leave request", () -> {
            homePage.returnButtonByText("Delete").click();
            homePage.returnButtonByText("Yes").click();
            homePage.waitForAlertMessage(" successfully deleted. ",10);
            homePage.assertThatPersonalLeavesTableIsEmpty(true);
        });

    }
}
