package pages;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


public class HomePage {
    private WebDriver driver;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "(//div[contains(@class, 'dropdown-menu')]//following::span)[1]")
    public WebElement loggedInUserName;

    @FindBy(xpath = "//h2[contains(text(),'Personal leaves')]//following::button")
    public WebElement addLeaveButton;

    @FindBy(xpath = "//mat-dialog-container")
    public WebElement dialogPopUpContainer;

    @FindBy(id = "fromDate")
    public WebElement leaveStartDate;

    @FindBy(id = "toDate")
    public WebElement leaveEndDate;

    public String getLoggedInUserName() {
        return loggedInUserName.getText();
    }

    /** Test is sometimes flaky when using this method I should refactor.
     * meanwhile thread.sleep :(
     * */
    public WebElement returnButtonByText(String buttonText) throws InterruptedException {
        Thread.sleep(1000);
        return driver.findElement(By.xpath("//button[contains(text(),'" + buttonText + "')]"));
    }

    public WebElement returnAtagByText(String aTagText) {
        return driver.findElement(By.xpath("//a[contains(text(),'" + aTagText + "')]"));
    }

    public WebElement leaveTableRowName(String rowName) {
        return driver.findElement(By.xpath("//table//th[contains(text(),'" + rowName + "')]"));
    }

    public void waitForButtonVisible(String buttonName, int timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//button[text()= '" + buttonName + "']"))));
    }

    public void waitForButtonClickable(String buttonName, int timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//button[text()= '" + buttonName + "']"))));
    }

    public void waitForAlertMessage(String alertMessage, int timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//div[contains(text(),'" + alertMessage + "')]"))));
    }

    public void assertLeaveTableIsRenderedWithCorrectDataRows(String[] dataRow) {
        for (int i = 0; i < dataRow.length; i++) {
            if (i == 0) continue;
            String currentElementText = driver.findElement(By.xpath("(//table//tr//td)[" + i + "]")).getText();
            Assertions.assertEquals(currentElementText, dataRow[i]);
        }
    }

    public void deleteAllLeaveRequests() throws InterruptedException {
        if (driver.findElements(By.xpath("//button[text()=\"Delete\"]")).size() > 0) {
            int size = driver.findElements(By.xpath("//button[text()=\"Delete\"]")).size();
            for (int i = 1; i <= size; i++) {
                String locator = String.format("(//button[text()=\"Delete\"])", i);
                WebElement currentItem = driver.findElement(By.xpath(locator));
                WebDriverWait wait = new WebDriverWait(driver, 5);
                wait.until(ExpectedConditions.elementToBeClickable(currentItem));
                currentItem.click();
                this.waitForButtonVisible("Yes", 5);
                this.selectDialogButton("Yes");
                /** current score : stale element 1 , gencho 0
                 *  need more time to learn how to handle it in java. Sorry for the Thread.sleep.
                 *  Or I can probably all the existing leave requests using an API call?
                 */
                Thread.sleep(1500);
            }

        }
    }

    public void populateLeaveToField() throws Exception {
        if (Utils.getCurrentDay() == "FRIDAY") {
            this.leaveEndDate.sendKeys(Utils.getCurrentDate("AfterThreeDays"));
        } else {
            this.leaveEndDate.sendKeys(Utils.getCurrentDate("Tommorow"));
        }
    }

    public void selectNavigationOption(String navigationOption) {
        driver.findElement(By.xpath("//a[contains(@class, 'nav-link') and normalize-space(text()) = '" + navigationOption + "']")).click();
    }

    public void selectDialogButton(String buttonName) {
        driver.findElement(By.xpath("//mat-dialog-container//button[contains(text(),'" + buttonName + "')]")).click();
    }

    public void editDateAddOneDay() throws Exception {
        /** Add leave max date + 1 day.
         * I really should find a java dateTime library that is more human readable...
         */
        this.leaveEndDate.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        if (Utils.getCurrentDay() == "FRIDAY") {
            this.leaveEndDate.sendKeys(new SimpleDateFormat("MM/dd/yyyy").format(new Date(new Date().getTime() + 345600000)));
        } else {
            this.leaveEndDate.sendKeys(new SimpleDateFormat("MM/dd/yyyy").format(new Date(new Date().getTime() + 172800000)));
        }
    }

    public void validateDaysCountInTable(String days) {
        String daysCount = driver.findElement(By.xpath("(//table//tr//td)[3]")).getText();
        Assertions.assertEquals(daysCount, days);
    }

    public void validateStatusTextInTable(String statusText) {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("(//table//tr//td)[5][contains(text(),'" + statusText + "')]"))));
        String daysCount = driver.findElement(By.xpath("(//table//tr//td)[5]")).getText();
        Assertions.assertEquals(daysCount, statusText);
    }


    public void assertThatPersonalLeavesTableIsEmpty(boolean bool) {
        String tableText = driver.findElement(By.xpath("//tr//td")).getText();
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//tr//td"))));
        if (bool == true) {
            Assertions.assertEquals(tableText, "No planned leaves");
        } else {
            Assertions.assertNotEquals(tableText, "No planned leaves");
        }
    }

    public void clickOnTodayInCalendar() {
        String calendarTodayElement = "(//mwl-calendar-month-cell[contains(@class, 'cal-today')]//following::div)[1]";
        driver.findElement(By.xpath(calendarTodayElement)).click();
    }

    public void personNameIsShownOnCalendarDayClick(String name, boolean bool) {
        String shownNameInCalendar = driver.findElement(By.xpath("//mwl-calendar-month-cell[contains(@class, 'cal-today')]//following::a")).getText();
        if (bool == true) {
            Assertions.assertEquals(shownNameInCalendar, name);
        } else {
            Assertions.assertNotEquals(shownNameInCalendar, name);
        }
    }

    public void changeLanguage(String language) {
        driver.findElement(By.xpath("//mat-select")).click();
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//span[@class='mat-option-text' and contains(text(),'" + language + "')]"))));
        driver.findElement(By.xpath("//span[@class='mat-option-text' and contains(text(),'" + language + "')]")).click();
        if(language=="Български") {
        this.waitForButtonVisible("Добави",5);
        }
    }

    public void testLanguageTranslationOfElementsFromDataSet(Map<String, String> map) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            Assertions.assertEquals(driver.findElement(By.xpath(key)).getText().trim(), value);
        }
    }
}



