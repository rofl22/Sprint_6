package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class MainPage {
    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor js;

    // Локаторы для главной страницы
    private By cookieButton = By.id("rcc-confirm-button");
    private By topOrderButton = By.className("Button_Button__ra12g");
    private By bottomOrderButton = By.xpath("//div[contains(@class, 'Home_FinishButton')]//button");
    private By faqItems = By.className("accordion__item");
    private By faqQuestion = By.className("accordion__button");
    private By faqAnswer = By.className("accordion__panel");

    public MainPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.js = (JavascriptExecutor) driver;
    }

    public void acceptCookies() {
        try {
            // Ждем появления кнопки и кликаем через JS
            WebElement cookieBtn = wait.until(ExpectedConditions.elementToBeClickable(cookieButton));
            js.executeScript("arguments[0].click();", cookieBtn);
        } catch (Exception e) {
            System.out.println("Cookie button not found or not clickable: " + e.getMessage());
        }
    }

    public void clickTopOrderButton() {
        WebElement orderBtn = wait.until(ExpectedConditions.elementToBeClickable(topOrderButton));
        js.executeScript("arguments[0].click();", orderBtn);
    }

    public void clickBottomOrderButton() {
        // Скроллим к нижней кнопке
        WebElement bottomBtn = driver.findElement(bottomOrderButton);
        js.executeScript("arguments[0].scrollIntoView(true);", bottomBtn);

        // Кликаем через JS
        wait.until(ExpectedConditions.elementToBeClickable(bottomBtn));
        js.executeScript("arguments[0].click();", bottomBtn);
    }

    public List<WebElement> getFaqItems() {
        return driver.findElements(faqItems);
    }

    public void clickFaqQuestion(int index) {
        // Скроллим к вопросу
        List<WebElement> questions = driver.findElements(faqQuestion);
        WebElement question = questions.get(index);
        js.executeScript("arguments[0].scrollIntoView(true);", question);

        // Кликаем через JS
        wait.until(ExpectedConditions.elementToBeClickable(question));
        js.executeScript("arguments[0].click();", question);

        // Ждем появления ответа с явным ожиданием
        wait.until(ExpectedConditions.visibilityOf(getFaqAnswerElement(index)));
    }

    public String getFaqAnswerText(int index) {
        WebElement answer = getFaqAnswerElement(index);
        return answer.getText();
    }

    public boolean isFaqAnswerDisplayed(int index) {
        WebElement answer = getFaqAnswerElement(index);
        return answer.isDisplayed() && !answer.getText().isEmpty();
    }

    private WebElement getFaqAnswerElement(int index) {
        List<WebElement> answers = driver.findElements(faqAnswer);
        return answers.get(index);
    }


    public By getCookieButtonLocator() {
        return cookieButton;
    }

    public By getFaqQuestionLocator() {
        return faqQuestion;
    }

    public By getFaqAnswerLocator() {
        return faqAnswer;
    }

}
//