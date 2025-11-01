package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class OrderPage {
    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor js;

    // Локаторы для формы заказа
    private By nameField = By.xpath("//input[@placeholder='* Имя']");
    private By lastNameField = By.xpath("//input[@placeholder='* Фамилия']");
    private By addressField = By.xpath("//input[@placeholder='* Адрес: куда привезти заказ']");
    private By metroField = By.xpath("//input[@placeholder='* Станция метро']");
    private By phoneField = By.xpath("//input[@placeholder='* Телефон: на него позвонит курьер']");
    private By nextButton = By.xpath("//button[text()='Далее']");

    // Локаторы для второй части формы
    private By dateField = By.xpath("//input[@placeholder='* Когда привезти самокат']");
    private By rentalPeriod = By.className("Dropdown-placeholder");
    private By colorBlack = By.id("black");
    private By colorGrey = By.id("grey");
    private By commentField = By.xpath("//input[@placeholder='Комментарий для курьера']");
    private By orderButton = By.xpath("//button[text()='Заказать']");
    private By confirmButton = By.xpath("//button[text()='Да']");
    private By successMessage = By.xpath("//div[contains(text(), 'Заказ оформлен')]");

    public OrderPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.js = (JavascriptExecutor) driver;
    }

    public void fillFirstStep(String name, String lastName, String address, String phone) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(nameField)).sendKeys(name);
        driver.findElement(lastNameField).sendKeys(lastName);
        driver.findElement(addressField).sendKeys(address);
        driver.findElement(phoneField).sendKeys(phone);

        // Выбор станции метро
        driver.findElement(metroField).click();
        WebElement metroStation = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[text()='Сокольники']")));
        js.executeScript("arguments[0].click();", metroStation);
    }

    public void clickNextButton() {
        WebElement nextBtn = wait.until(ExpectedConditions.elementToBeClickable(nextButton));
        js.executeScript("arguments[0].click();", nextBtn);
    }

    public void fillSecondStep(String date, String period, String color, String comment) {
        // Ждем загрузки второй части формы
        wait.until(ExpectedConditions.visibilityOfElementLocated(dateField));

        driver.findElement(dateField).sendKeys(date);

        // Выбор периода аренды
        WebElement periodDropdown = wait.until(ExpectedConditions.elementToBeClickable(rentalPeriod));
        js.executeScript("arguments[0].click();", periodDropdown);

        WebElement periodOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[text()='" + period + "']")));
        js.executeScript("arguments[0].click();", periodOption);

        // Выбор цвета
        if ("black".equals(color)) {
            WebElement blackCheckbox = wait.until(ExpectedConditions.elementToBeClickable(colorBlack));
            js.executeScript("arguments[0].click();", blackCheckbox);
        } else {
            WebElement greyCheckbox = wait.until(ExpectedConditions.elementToBeClickable(colorGrey));
            js.executeScript("arguments[0].click();", greyCheckbox);
        }

        driver.findElement(commentField).sendKeys(comment);
    }

    public void clickOrderButton() {
        WebElement orderBtn = wait.until(ExpectedConditions.elementToBeClickable(orderButton));
        js.executeScript("arguments[0].click();", orderBtn);
    }

    public void confirmOrder() {
        WebElement confirmBtn = wait.until(ExpectedConditions.elementToBeClickable(confirmButton));
        js.executeScript("arguments[0].click();", confirmBtn);
    }

    public boolean isSuccessMessageDisplayed() {
        try {
            WebElement message = wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage));
            return message.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public By getDateFieldLocator() {
        return dateField;
    }

    public By getConfirmButtonLocator() {
        return confirmButton;
    }
}
//