package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ShopStartPage {

    private SelenideElement startPageHeading = $(".heading");
    private SelenideElement paymentButton = $$(".button").find(exactText("Купить"));
    private SelenideElement creditButton = $$(".button").find(exactText("Купить в кредит"));

    public ShopStartPage() {
        startPageHeading.shouldBe(visible);
    }

    // Выбор страницы оплаты по карте
    public PaymentPage choosePayment(){
        paymentButton.click();
        return new PaymentPage();
    }

    // Выбор страницы кредита по данным карты
    public CreditPage chooseCredit(){
        creditButton.click();
        return new CreditPage();
    }
}
