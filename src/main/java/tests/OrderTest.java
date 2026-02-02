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
import pages.OrderPage;
import data.TestData;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {
    private WebDriver driver;
    private MainPage mainPage;
    private OrderPage orderPage;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox", "--headless", "--disable-dev-shm-usage", "--window-size=1920,1080");
        driver = new ChromeDriver(options);
        driver.get("https://qa-scooter.praktikum-services.ru/");

        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Ждем загрузки страницы
        wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete'"));

        mainPage = new MainPage(driver);
        orderPage = new OrderPage(driver);
        mainPage.acceptCookies();

        // Явное ожидание вместо sleep - ждем скрытия cookie банера
        wait.until(ExpectedConditions.invisibilityOfElementLocated(mainPage.getCookieButtonLocator()));
    }

    @ParameterizedTest
    @MethodSource("orderDataProvider")
    public void testOrderFromTopButton(String name, String lastName, String address,
                                       String phone, String date, String period,
                                       String color, String comment) {
        // Заказ через верхнюю кнопку
        testOrderFlow(mainPage::clickTopOrderButton, name, lastName, address, phone, date, period, color, comment);
    }

    @ParameterizedTest
    @MethodSource("orderDataProvider")
    public void testOrderFromBottomButton(String name, String lastName, String address,
                                          String phone, String date, String period,
                                          String color, String comment) {
        // Заказ через нижнюю кнопку
        testOrderFlow(mainPage::clickBottomOrderButton, name, lastName, address, phone, date, period, color, comment);
    }

    private void testOrderFlow(Runnable orderButtonClick, String name, String lastName,
                               String address, String phone, String date, String period,
                               String color, String comment) {
        // Нажимаем на кнопку заказа
        orderButtonClick.run();

        // Ждем загрузки формы заказа
        wait.until(ExpectedConditions.urlContains("order"));

        // Заполняем первую часть формы
        orderPage.fillFirstStep(name, lastName, address, phone);
        orderPage.clickNextButton();

        // Ждем загрузки второй части формы - проверяем видимость поля даты
        wait.until(ExpectedConditions.visibilityOfElementLocated(orderPage.getDateFieldLocator()));

        // Заполняем вторую часть формы
        orderPage.fillSecondStep(date, period, color, comment);
        orderPage.clickOrderButton();

        // Ждем появления окна подтверждения
        wait.until(ExpectedConditions.visibilityOfElementLocated(orderPage.getConfirmButtonLocator()));

        // Подтверждаем заказ
        orderPage.confirmOrder();

        // Проверяем успешное оформление
        assertTrue(orderPage.isSuccessMessageDisplayed(), "Сообщение об успешном заказе не отображается");
    }

    static Object[][] orderDataProvider() {
        return TestData.getOrderData();
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}

//

