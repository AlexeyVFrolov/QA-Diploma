package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import lombok.val;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.sleep;

public class CreditPage {

    public final String creditApproved = "Кредит одобрен банком";
    public final String creditDeclined = "Банк отказал в покупке в кредит";
    public final String creditUndefinedStatusError = "Операция одобрена и отклонена одновременно";
    public final String creditNoStatusError = "Нет ответа банка";
    public final String sendingSuccess = "Заявка успешно отправлена";
    public final String sendingError = "Заявка не отправлена";
    
    private SelenideElement creditPageHeading = $$(".heading").find(exactText("Кредит по данным карты"));
    private SelenideElement sendCreditFormButton = $$(".button").find(exactText("Продолжить"));
    private SelenideElement sendingMessage = $$(".button").find(exactText("Отправляем запрос в банк..."));

    public CreditPage(){
        creditPageHeading.shouldBe(visible);
    }

    // Запрс на кредит по данным карты и анализ ответа системы
    public String requestCredit(String cardNumber, String month, String year, String owner, String code) {
        CardFormElement.fillForm(cardNumber, month, year, owner, code);
        sendCreditFormButton.click();
        if (!sendingMessage.isDisplayed()) {
            return sendingError;
        }
        sendCreditFormButton.shouldBe(visible, Duration.ofSeconds(20));
        return sendingSuccess;
    }

    public String bankResponse(){
        sleep(500);
        val approved = BankServiceMessage.isApproveMessage();
        val declined = BankServiceMessage.isDeclineMessage();

        if (approved) {
            if (!declined) {
                return creditApproved;
            } else {
                return creditUndefinedStatusError;
            }
        }
        if (declined) {
            return creditDeclined;
        }
        return creditNoStatusError;
    }

    // Есть ли собщение о необходимости заполнения поля Номер карты
    public boolean isCreditFormEmptyCardNumberAlert() {
        return CardFormElement.isEmptyCardNumber();
    }

    // Есть ли собщение о неполном номере карты
    public boolean isCreditFormShortCardNumberAlert() {
        return CardFormElement.isShortCardNumber();
    }

    // Допустим ли ввод слишком длинного номера карты
    public boolean canNotInputLongCreditCardNumber(String string) {
        return CardFormElement.canNotInputLongCardNumber(string);
    }

    // Допустим ли ввод буквы в номере карты
    public boolean canNotInputLettersInCreditCardNumber(String letter) {
        return CardFormElement.canNotInputLettersInCardNumber(letter);
    }

    // Допустим ли ввод специального символа в номере карты
    public boolean canNotInputSpecialSymbolsInCreditCardNumber(String symbol) {
        return CardFormElement.canNotInputSpecialSymbolsInCardNumber(symbol);
    }

    // Возможно ли редактирование номера карты с помощью Backspace
    public boolean canEditWithBackspaceCreditCardNumber(String string) {
        return CardFormElement.canEditWithBackspaceCardNumber(string);
    }

    // Возможно ли редактирование номера карты с помощью Delete
    public boolean canEditWithDeleteCreditCardNumber(String string) {
        return CardFormElement.canEditWithDeleteCardNumber(string);
    }

    // Есть ли собщение о необходимости заполнения поля Месяц
    public boolean isCreditFormEmptyMonthNumberAlert() {
        return CardFormElement.isEmptyMonthNumber();
    }

    // Есть ли собщение о неполном номере месяца
    public boolean isCreditFormShortMonthNumberAlert() {
        return CardFormElement.isShortMonthNumber();
    }

    // Допустим ли ввод слишком длинного номера месяца
    public boolean canNotInputLongCreditMonthNumber(String string) {
        return CardFormElement.canNotInputLongMonthNumber(string);
    }

    // Допустим ли ввод номера месяца 00
    public boolean isCreditFormZeroMonthNumberAlert() {
        return CardFormElement.isZeroMonthNumber();
    }

    // Допустим ли ввод номера месяца 13
    public boolean isCreditFormTooBigMonthNumberAlert() {
        return CardFormElement.isTooBigMonthNumber();
    }

    // Допускается ли ввод букв в номере месяца
    public boolean canNotInputLettersInCreditMonthNumber(String letter) {
        return CardFormElement.canNotInputLettersInMonthNumber(letter);
    }

    // Допускается ли ввод спецсимволов в номере месяца
    public boolean canNotInputSpecialSymbolsInCreditMonthNumber(String symbol) {
        return CardFormElement.canNotInputSpecialSymbolsInMonthNumber(symbol);
    }

    // Возможно ли редактирование номера месяца с помощью Backspace
    public boolean canEditWithBackspaceCreditMonthNumber(String string) {
        return CardFormElement.canEditWithBackspaceMonthdNumber(string);
    }

    // Возможно ли редактирование номера месяца с помощью Delete
    public boolean canEditWithDeleteCreditMonthNumber(String string) {
        return CardFormElement.canEditWithDeleteMonthNumber(string);
    }

    // Есть ли собщение о необходимости заполнения поля Год
    public boolean isCreditFormEmptyYearNumberAlert() {
        return CardFormElement.isEmptyYearNumber();
    }

    // Есть ли собщение о неполном номере года
    public boolean isCreditFormShortYearNumberAlert() {
        return CardFormElement.isShortYearNumber();
    }

    // Допустим ли ввод слишком длинного номера года
    public boolean canNotInputLongCreditYearNumber(String string) {
        return CardFormElement.canNotInputLongYearNumber(string);
    }

    // Допустим ли ввод года истекшего срока действия карты
    public boolean isCreditFormExpiredYearNumberAlert() {
        return CardFormElement.isExpiredYearNumber();
    }

    // Допустим ли ввод номера года более возможного срока действия карты
    public boolean isCreditFormTooBigYearNumberAlert() {
        return CardFormElement.isTooBigYearNumber();
    }

    // Допускается ли ввод букв в номере года
    public boolean canNotInputLettersInCreditYearNumber(String letter) {
        return CardFormElement.canNotInputLettersInYearNumber(letter);
    }

    // Допускается ли ввод спецсимволов в номере года
    public boolean canNotInputSpecialSymbolsInCreditYearNumber(String symbol) {
        return CardFormElement.canNotInputSpecialSymbolsInYearNumber(symbol);
    }

    // Возможно ли редактирование номера года с помощью Backspace
    public boolean canEditWithBackspaceCreditYearNumber(String string) {
        return CardFormElement.canEditWithBackspaceYearNumber(string);
    }

    // Возможно ли редактирование номера года с помощью Delete
    public boolean canEditWithDeleteCreditYearNumber(String string) {
        return CardFormElement.canEditWithDeleteYearNumber(string);
    }

    // Допустим ли ввод истекшего срока действия карты
    public boolean isCreditFormExpiredMonthNumberAlert() {
        return CardFormElement.isExpiredMonthNumber();
    }

    // Есть ли собщение о необходимости заполнения поля Владелец
    public boolean isCreditFormEmptyCardOwnerAlert() {
        return CardFormElement.isEmptyCardOwner();
    }

    // Допустим ли ввод имени владельца, состоящего из одной буквы
    public boolean isCreditFormShortCardOwnerAlert() {
        return CardFormElement.isShortCardOwner();
    }

    // Допускается ли ввод цифр в имени владельца карты
    public boolean canNotInputDigitsInCreditCardOwner(String digit) {
        return CardFormElement.canNotInputDigitInCardOwner(digit);
    }

    // Допускается ли ввод спецсимволов в имени владельца карты
    public boolean canNotInputSpecialSymbolsInCreditCardOwner(String symbol) {
        return CardFormElement.canNotInputSpecialSymbolsInCardOwner(symbol);
    }

    // Допускается ли ввод последовательности SQL Injection в имени владельца карты
    public boolean isCreditFormSQLInjectionCardOwnerAlert() {
        return CardFormElement.isSQLInjectionCardOwner();
    }

    // Возможно ли редактирование имени владельца с помощью Backspace
    public boolean canEditWithBackspaceCreditCardOwner(String string) {
        return CardFormElement.canEditWithBackspaceCardOwner(string);
    }

    // Возможно ли редактирование имени владельца с помощью Delete
    public boolean canEditWithDeleteCreditCardOwner(String string) {
        return CardFormElement.canEditWithDeleteCardOwner(string);
    }

    // Есть ли собщение о необходимости заполнения поля CVC/CVV
    public boolean isCreditFormEmptyCVC_CVVCodeAlert() {
        return CardFormElement.isEmptyCVC_CVVCode();
    }

    // Есть ли собщение о неполном коде CVC/CVV
    public boolean isCreditFormShortCVC_CVVCodeAlert() {
        return CardFormElement.isShortCVC_CVVCode();
    }

    // Допустим ли ввод слишком длинного кода CVC/CVV
    public boolean canNotInputLongCreditCVC_CVVCode(String string) {
        return CardFormElement.canNotInputLongCVC_CVVCode(string);
    }

    // Допускается ли ввод букв в коде CVC/CVV
    public boolean canNotInputLettersInCreditCVC_CVVCode(String letter) {
        return CardFormElement.canNotInputLettersInCVC_CVVCode(letter);
    }

    // Допускается ли ввод спецсимволов в коде CVC/CVV
    public boolean canNotInputSpecialSymbolsInCreditCVC_CVVCode(String symbol) {
        return CardFormElement.canNotInputSpecialSymbolsInCVC_CVVCode(symbol);
    }

    // Возможно ли редактирование кода CVC/CVV с помощью Backspace
    public boolean canEditWithBackspaceCreditCVC_CVVCode(String string) {
        return CardFormElement.canEditWithBackspaceCVC_CVVCode(string);
    }

    // Возможно ли редактирование кода CVC/CVV с помощью Delete
    public boolean canEditWithDeleteCreditCVC_CVVCode(String string) {
        return CardFormElement.canEditWithDeleteYCVC_CVVCode(string);
    }
}
