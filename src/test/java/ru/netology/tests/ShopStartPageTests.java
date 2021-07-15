package ru.netology.tests;

import com.codeborne.selenide.junit5.ScreenShooterExtension;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.val;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import ru.netology.page.ShopStartPage;

import static com.codeborne.selenide.Selenide.open;

@ExtendWith({ScreenShooterExtension.class})
public class ShopStartPageTests {

    @BeforeAll
    static void setUpSelenideAllure(){
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownSelenideAllure(){
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void startWeb() {
        open("http://localhost:8080");
    }

    @Test
    @DisplayName("Переход на страницу оплаты по карте")
    void shouldSelectPaymentPage() {
        val shopStartPage = new ShopStartPage();
        shopStartPage.choosePayment();
    }

    @Test
    @DisplayName("Переход на страницу покупки в кредит")
    void shouldSelectCreditPage() {
        val shopStartPage = new ShopStartPage();
        shopStartPage.chooseCredit();
    }

    @Test
    @DisplayName("Переход на страницу покупки в кредит со страницы оплаты по карте")
    void shouldSwitchPaymentToCreditPage() {
        val shopStartPage = new ShopStartPage();
        shopStartPage.choosePayment();
        shopStartPage.chooseCredit();
    }

    @Test
    @DisplayName("Переход на страницу оплаты по карте со страницы покупки в кредит")
    void shouldSwitchCreditToPaymentPage() {
        val shopStartPage = new ShopStartPage();
        shopStartPage.chooseCredit();
        shopStartPage.choosePayment();
    }
}
