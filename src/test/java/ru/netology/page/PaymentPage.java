package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import lombok.val;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class PaymentPage {

    public final String paymentApproved = "Оплата одобрена банком";
    public final String paymentDeclined = "Банк отказал в проведении операции оплаты";
    public final String paymentUndefinedStatusError = "Операция одобрена и отклонена одновременно";
    public final String paymentNoStatusError = "Нет ответа банка";
    public final String sendingSuccess = "Заявка успешно отправлена";
    public final String sendingError = "Заявка не отправлена";

    private SelenideElement paymentPageHeading = $$(".heading").find(exactText("Оплата по карте"));
    private SelenideElement sendPaymentFormButton = $$(".button").find(exactText("Продолжить"));
    private SelenideElement sendingMessage = $$(".button").find(exactText("Отправляем запрос в банк..."));

    public PaymentPage() {
        paymentPageHeading.shouldBe(visible);
    }

    // Запрс на оплату по карте и анализ ответа системы
    public String requestPayment(String cardNumber, String month, String year, String owner, String code) {
        CardFormElement.fillForm(cardNumber, month, year, owner, code);
        sendPaymentFormButton.click();
        if (!sendingMessage.isDisplayed()) {
            return sendingError;
        }
        sendPaymentFormButton.shouldBe(visible, Duration.ofSeconds(20));
        return sendingSuccess;
    }

    public String bankResponse(){
        sleep(500);
        val approved = BankServiceMessage.isApproveMessage();
        val declined = BankServiceMessage.isDeclineMessage();

        if (approved) {
            if (!declined) {
                return paymentApproved;
            } else {
                return paymentUndefinedStatusError;
            }
        }
        if (declined) {
            return paymentDeclined;
        }
        return paymentNoStatusError;
    }

    // Есть ли собщение о необходимости заполнения поля Номер карты
    public boolean isPaymentFormEmptyCardNumberAlert() {
        return CardFormElement.isEmptyCardNumber();
    }

    // Есть ли собщение о неполном номере карты
    public boolean isPaymentFormShortCardNumberAlert() {
        return CardFormElement.isShortCardNumber();
    }

    // Допустим ли ввод слишком длинного номера карты
    public boolean canNotInputLongPaymentCardNumber(String string) {
        return CardFormElement.canNotInputLongCardNumber(string);
    }

    // Допустим ли ввод буквы в номере карты
    public boolean canNotInputLettersInPaymentCardNumber(String letter) {
        return CardFormElement.canNotInputLettersInCardNumber(letter);
    }

    // Допустим ли ввод специального символа в номере карты
    public boolean canNotInputSpecialSymbolsInPaymentCardNumber(String symbol) {
        return CardFormElement.canNotInputSpecialSymbolsInCardNumber(symbol);
    }

    // Возможно ли редактирование номера карты с помощью Backspace
    public boolean canEditWithBackspacePaymentCardNumber(String string) {
        return CardFormElement.canEditWithBackspaceCardNumber(string);
    }

    // Возможно ли редактирование номера карты с помощью Delete
    public boolean canEditWithDeletePaymentCardNumber(String string) {
        return CardFormElement.canEditWithDeleteCardNumber(string);
    }

    // Есть ли собщение о необходимости заполнения поля Месяц
    public boolean isPaymentFormEmptyMonthNumberAlert() {
        return CardFormElement.isEmptyMonthNumber();
    }

    // Есть ли собщение о неполном номере месяца
    public boolean isPaymentFormShortMonthNumberAlert() {
        return CardFormElement.isShortMonthNumber();
    }

    // Допустим ли ввод слишком длинного номера месяца
    public boolean canNotInputLongPaymentMonthNumber(String string) {
        return CardFormElement.canNotInputLongMonthNumber(string);
    }

    // Допустим ли ввод номера месяца 00
    public boolean isPaymentFormZeroMonthNumberAlert() {
        return CardFormElement.isZeroMonthNumber();
    }

    // Допустим ли ввод номера месяца 13
    public boolean isPaymentFormTooBigMonthNumberAlert() {
        return CardFormElement.isTooBigMonthNumber();
    }

    // Допускается ли ввод букв в номере месяца
    public boolean canNotInputLettersInPaymentMonthNumber(String letter) {
         return CardFormElement.canNotInputLettersInMonthNumber(letter);
    }

    // Допускается ли ввод спецсимволов в номере месяца
    public boolean canNotInputSpecialSymbolsInPaymentMonthNumber(String symbol) {
        return CardFormElement.canNotInputSpecialSymbolsInMonthNumber(symbol);
    }

    // Возможно ли редактирование номера месяца с помощью Backspace
    public boolean canEditWithBackspacePaymentMonthNumber(String string) {
        return CardFormElement.canEditWithBackspaceMonthdNumber(string);
    }

    // Возможно ли редактирование номера месяца с помощью Delete
    public boolean canEditWithDeletePaymentMonthNumber(String string) {
        return CardFormElement.canEditWithDeleteMonthNumber(string);
    }

    // Есть ли собщение о необходимости заполнения поля Год
    public boolean isPaymentFormEmptyYearNumberAlert() {
        return CardFormElement.isEmptyYearNumber();
    }

    // Есть ли собщение о неполном номере года
    public boolean isPaymentFormShortYearNumberAlert() {
        return CardFormElement.isShortYearNumber();
    }

    // Допустим ли ввод слишком длинного номера года
    public boolean canNotInputLongPaymentYearNumber(String string) {
        return CardFormElement.canNotInputLongYearNumber(string);
    }

    // Допустим ли ввод года истекшего срока действия карты
    public boolean isPaymentFormExpiredYearNumberAlert() {
        return CardFormElement.isExpiredYearNumber();
    }

    // Допустим ли ввод номера года более возможного срока действия карты
    public boolean isPaymentFormTooBigYearNumberAlert() {
        return CardFormElement.isTooBigYearNumber();
    }

    // Допускается ли ввод букв в номере года
    public boolean canNotInputLettersInPaymentYearNumber(String letter) {
        return CardFormElement.canNotInputLettersInYearNumber(letter);
    }

    // Допускается ли ввод спецсимволов в номере года
    public boolean canNotInputSpecialSymbolsInPaymentYearNumber(String symbol) {
        return CardFormElement.canNotInputSpecialSymbolsInYearNumber(symbol);
    }

    // Возможно ли редактирование номера года с помощью Backspace
    public boolean canEditWithBackspacePaymentYearNumber(String string) {
        return CardFormElement.canEditWithBackspaceYearNumber(string);
    }

    // Возможно ли редактирование номера года с помощью Delete
    public boolean canEditWithDeletePaymentYearNumber(String string) {
        return CardFormElement.canEditWithDeleteYearNumber(string);
    }

    // Допустим ли ввод истекшего срока действия карты
    public boolean isPaymentFormExpiredMonthNumberAlert() {
        return CardFormElement.isExpiredMonthNumber();
    }

    // Есть ли собщение о необходимости заполнения поля Владелец
    public boolean isPaymentFormEmptyCardOwnerAlert() {
        return CardFormElement.isEmptyCardOwner();
    }

    // Допустим ли ввод имени владельца, состоящего из одной буквы
    public boolean isPaymentFormShortCardOwnerAlert() {
        return CardFormElement.isShortCardOwner();
    }

    // Допускается ли ввод цифр в имени владельца карты
    public boolean canNotInputDigitsInPaymentCardOwner(String digit) {
        return CardFormElement.canNotInputDigitInCardOwner(digit);
    }

    // Допускается ли ввод спецсимволов в имени владельца карты
    public boolean canNotInputSpecialSymbolsInPaymentCardOwner(String symbol) {
        return CardFormElement.canNotInputSpecialSymbolsInCardOwner(symbol);
    }

    // Допускается ли ввод последовательности SQL Injection в имени владельца карты
    public boolean isPaymentFormSQLInjectionCardOwnerAlert() {
        return CardFormElement.isSQLInjectionCardOwner();
    }

    // Возможно ли редактирование имени владельца с помощью Backspace
    public boolean canEditWithBackspacePaymentCardOwner(String string) {
        return CardFormElement.canEditWithBackspaceCardOwner(string);
    }

    // Возможно ли редактирование имени владельца с помощью Delete
    public boolean canEditWithDeletePaymentCardOwner(String string) {
        return CardFormElement.canEditWithDeleteCardOwner(string);
    }

    // Есть ли собщение о необходимости заполнения поля CVC/CVV
    public boolean isPaymentFormEmptyCVC_CVVCodeAlert() {
        return CardFormElement.isEmptyCVC_CVVCode();
    }

    // Есть ли собщение о неполном коде CVC/CVV
    public boolean isPaymentFormShortCVC_CVVCodeAlert() {
        return CardFormElement.isShortCVC_CVVCode();
    }

    // Допустим ли ввод слишком длинного кода CVC/CVV
    public boolean canNotInputLongPaymentCVC_CVVCode(String string) {
        return CardFormElement.canNotInputLongCVC_CVVCode(string);
    }

    // Допускается ли ввод букв в коде CVC/CVV
    public boolean canNotInputLettersInPaymentCVC_CVVCode(String letter) {
        return CardFormElement.canNotInputLettersInCVC_CVVCode(letter);
    }

    // Допускается ли ввод спецсимволов в коде CVC/CVV
    public boolean canNotInputSpecialSymbolsInPaymentCVC_CVVCode(String symbol) {
        return CardFormElement.canNotInputSpecialSymbolsInCVC_CVVCode(symbol);
    }

    // Возможно ли редактирование кода CVC/CVV с помощью Backspace
    public boolean canEditWithBackspacePaymentCVC_CVVCode(String string) {
        return CardFormElement.canEditWithBackspaceCVC_CVVCode(string);
    }

    // Возможно ли редактирование кода CVC/CVV с помощью Delete
    public boolean canEditWithDeletePaymentCVC_CVVCode(String string) {
        return CardFormElement.canEditWithDeleteYCVC_CVVCode(string);
    }
}