package tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.MainPage;
import data.TestData;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class FaqTest {
    private WebDriver driver;
    private MainPage mainPage;

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox", "--headless", "--disable-dev-shm-usage", "--window-size=1920,1080");
        driver = new ChromeDriver(options);
        driver.get("https://qa-scooter.praktikum-services.ru/");

        // Ждем загрузки страницы
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete'"));

        mainPage = new MainPage(driver);
        mainPage.acceptCookies();

        // Дополнительное ожидание после принятия cookies
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @ParameterizedTest
    @MethodSource("faqDataProvider")
    public void testFaqItems(int questionIndex, String expectedAnswer) {
        // Кликаем на вопрос
        mainPage.clickFaqQuestion(questionIndex);

        // Ждем немного перед проверкой
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Проверяем, что ответ отображается и содержит правильный текст
        assertTrue(mainPage.isFaqAnswerDisplayed(questionIndex),
                "Ответ на вопрос " + questionIndex + " не отображается");

        String actualAnswer = mainPage.getFaqAnswerText(questionIndex);
        assertEquals(expectedAnswer, actualAnswer,
                "Текст ответа не совпадает для вопроса " + questionIndex);
    }

    static Object[][] faqDataProvider() {
        return TestData.getFaqData();
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}