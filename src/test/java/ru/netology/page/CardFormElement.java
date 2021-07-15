package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import lombok.val;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$$;

public class CardFormElement {

    private static SelenideElement cardNumberInputField = $$(".input").find(exactText("Номер карты")).$(".input__control");
    private static SelenideElement cardExpiryDateMonthInputField = $$(".input").find(exactText("Месяц")).$(".input__control");
    private static SelenideElement cardExpiryDateYearInputField = $$(".input").find(exactText("Год")).$(".input__control");
    private static SelenideElement cardOwnerInputField = $$(".input").find(exactText("Владелец")).$(".input__control");
    private static SelenideElement cardCVC_CVVCodeInputField = $$(".input").find(exactText("CVC/CVV")).$(".input__control");

    private static SelenideElement cardNumberErrorMessage = $$(".input").find(text("Номер карты")).find(".input__sub");
    private static SelenideElement cardExpiryDateMonthErrorMessage = $$(".input").find(text("Месяц")).find(".input__sub");
    private static SelenideElement cardExpiryDateYearErrorMessage = $$(".input").find(text("Год")).find(".input__sub");
    private static SelenideElement cardOwnerErrorMessage = $$(".input").find(text("Владелец")).find(".input__sub");
    private static SelenideElement cardCVC_CVVCodeErrorMessage = $$(".input").find(text("CVC/CVV")).find(".input__sub");

    private static String invalidFormatMessage = "Неверный формат";
    private static String fillRequiredMessage = "Поле обязательно для заполнения";
    private static String invalidCardExpiryDateMessage = "Неверно указан срок действия карты";
    private static String cardExpiredMessage = "Истёк срок действия карты";

    // Заполнение полей формы запроса
    public static void fillForm(String cardNumber, String month, String year, String owner, String code){
        cardNumberInputField.sendKeys(cardNumber);
        cardExpiryDateMonthInputField.setValue(month);
        cardExpiryDateYearInputField.setValue(year);
        cardOwnerInputField.setValue(owner);
        cardCVC_CVVCodeInputField.setValue(code);
    }

    // Есть ли собщение о необходимости заполнения поля Номер карты
    public static boolean isEmptyCardNumber(){
        return cardNumberErrorMessage.getText().equals(fillRequiredMessage);
    }

    // Есть ли собщение о неверном формате номера карты
    public static boolean isShortCardNumber(){
        return cardNumberErrorMessage.getText().equals(invalidFormatMessage);
    }

    // Игнорируется ли ввод лишнего символа в номере карты
    public static boolean canNotInputLongCardNumber(String string){
        cardNumberInputField.setValue(string);
        return cardNumberInputField.getValue().equals(string.substring(0, string.length()-1));
    }

    // Игнорируется ли ввод буквы в номере карты
    public static boolean canNotInputLettersInCardNumber(String letter){
        val emptyString = "";

        cardNumberInputField.sendKeys(letter);
        return cardNumberInputField.getValue().equals(emptyString);
    }

    // Игнорируется ли ввод специального символа в номере карты
    public static boolean canNotInputSpecialSymbolsInCardNumber(String symbol){
        val emptyString = "";

        cardNumberInputField.sendKeys(symbol);
        return cardNumberInputField.getValue().equals(emptyString);
    }

    // Удаляется ли символ номера карты с помощью Backspace
    public static boolean canEditWithBackspaceCardNumber(String string){
        cardNumberInputField.setValue(string);
        cardNumberInputField.sendKeys(Keys.BACK_SPACE);
        return cardNumberInputField.getValue().equals(string.substring(0, string.length()-1));
    }

    // Удаляется ли символ номера карты с помощью Delete
    public static boolean canEditWithDeleteCardNumber(String string){
        cardNumberInputField.setValue(string);
        cardNumberInputField.sendKeys(Keys.HOME);
        cardNumberInputField.sendKeys(Keys.DELETE);
        return cardNumberInputField.getValue().equals(string.substring(1));
    }

    // Есть ли собщение о необходимости заполнения поля Месяц
    public static boolean isEmptyMonthNumber(){
        return cardExpiryDateMonthErrorMessage.getText().equals(fillRequiredMessage);
    }

    // Есть ли собщение о неверном формате номера месяца
    public static boolean isShortMonthNumber(){
        return cardExpiryDateMonthErrorMessage.getText().equals(invalidFormatMessage);
    }

    // Игнорируется ли ввод лишнего символа в номере месяца
    public static boolean canNotInputLongMonthNumber(String string){
        cardExpiryDateMonthInputField.setValue(string);
        return cardExpiryDateMonthInputField.getValue().equals(string.substring(0, string.length()-1));
    }

    // Есть ли сообщение о неверном номере месяца
    public static boolean isZeroMonthNumber(){
        return cardExpiryDateMonthErrorMessage.getText().equals(invalidCardExpiryDateMessage);
    }

    // Есть ли сообщение о неверном номере месяца
    public static boolean isTooBigMonthNumber(){
        return cardExpiryDateMonthErrorMessage.getText().equals(invalidCardExpiryDateMessage);
    }

    // Игнорируется ли ввод буквы в номере месяца
    public static boolean canNotInputLettersInMonthNumber(String letter){
        val emptyString = "";

        cardExpiryDateMonthInputField.sendKeys(letter);
        return cardExpiryDateMonthInputField.getValue().equals(emptyString);
    }

    // Игнорируется ли ввод специального символа в номере месяца
    public static boolean canNotInputSpecialSymbolsInMonthNumber(String symbol){
        val emptyString = "";

        cardExpiryDateMonthInputField.sendKeys(symbol);
        return cardExpiryDateMonthInputField.getValue().equals(emptyString);
    }

    // Удаляется ли символ номера месяца с помощью Backspace
    public static boolean canEditWithBackspaceMonthdNumber(String string){
        cardExpiryDateMonthInputField.setValue(string);
        cardExpiryDateMonthInputField.sendKeys(Keys.BACK_SPACE);
        return cardExpiryDateMonthInputField.getValue().equals(string.substring(0, string.length()-1));
    }

    // Удаляется ли символ номера месяца с помощью Delete
    public static boolean canEditWithDeleteMonthNumber(String string){
        cardExpiryDateMonthInputField.setValue(string);
        cardExpiryDateMonthInputField.sendKeys(Keys.HOME);
        cardExpiryDateMonthInputField.sendKeys(Keys.DELETE);
        return cardExpiryDateMonthInputField.getValue().equals(string.substring(1));
    }

    // Есть ли собщение о необходимости заполнения поля Год
    public static boolean isEmptyYearNumber(){
        return cardExpiryDateYearErrorMessage.getText().equals(fillRequiredMessage);
    }

    // Есть ли собщение о неверном формате номера года
    public static boolean isShortYearNumber(){
        return cardExpiryDateYearErrorMessage.getText().equals(invalidFormatMessage);
    }

    // Игнорируется ли ввод лишнего символа в номере года
    public static boolean canNotInputLongYearNumber(String string){
        cardExpiryDateYearInputField.setValue(string);
        return cardExpiryDateYearInputField.getValue().equals(string.substring(0, string.length()-1));
    }

    // Есть ли сообщение об истекшем сроке действия карты
    public static boolean isExpiredYearNumber(){
        return cardExpiryDateYearErrorMessage.getText().equals(cardExpiredMessage);
    }

    // Есть ли сообщение о неверном сроке действия карты
    public static boolean isTooBigYearNumber(){
        return cardExpiryDateYearErrorMessage.getText().equals(invalidCardExpiryDateMessage);
    }

    // Игнорируется ли ввод буквы в номере месяцам
    public static boolean canNotInputLettersInYearNumber(String letter){
        val emptyString = "";

        cardExpiryDateYearInputField.sendKeys(letter);
        return cardExpiryDateYearInputField.getValue().equals(emptyString);
    }

    // Игнорируется ли ввод специального символа в номере месяца
    public static boolean canNotInputSpecialSymbolsInYearNumber(String symbol){
        val emptyString = "";

        cardExpiryDateYearInputField.sendKeys(symbol);
        return cardExpiryDateYearInputField.getValue().equals(emptyString);
    }

    // Удаляется ли символ номера года с помощью Backspace
    public static boolean canEditWithBackspaceYearNumber(String string){
        cardExpiryDateYearInputField.setValue(string);
        cardExpiryDateYearInputField.sendKeys(Keys.BACK_SPACE);
        return cardExpiryDateYearInputField.getValue().equals(string.substring(0, string.length()-1));
    }

    // Удаляется ли символ номера года с помощью Delete
    public static boolean canEditWithDeleteYearNumber(String string){
        cardExpiryDateYearInputField.setValue(string);
        cardExpiryDateYearInputField.sendKeys(Keys.HOME);
        cardExpiryDateYearInputField.sendKeys(Keys.DELETE);
         return cardExpiryDateYearInputField.getValue().equals(string.substring(1));
    }

    // Есть ли сообщение об истекшем сроке действия карты
    public static boolean isExpiredMonthNumber(){
        return cardExpiryDateMonthErrorMessage.getText().equals(cardExpiredMessage);
    }

    // Есть ли собщение о необходимости заполнения поля Владелец
    public static boolean isEmptyCardOwner(){
        return cardOwnerErrorMessage.getText().equals(fillRequiredMessage);
    }

    // Есть ли собщение о слишком коротком имени владельца карты
    public static boolean isShortCardOwner(){
        return cardOwnerErrorMessage.getText().equals(invalidFormatMessage);
    }

    // Игнорируется ли ввод цифры в имени владельца карты
    public static boolean canNotInputDigitInCardOwner(String digit){
        val emptyString = "";

        cardOwnerInputField.sendKeys(digit);
        return cardOwnerInputField.getValue().equals(emptyString);
    }

    // Игнорируется ли ввод специального символа в имени владельца карты
    public static boolean canNotInputSpecialSymbolsInCardOwner(String symbol){
        val emptyString = "";

        cardOwnerInputField.sendKeys(symbol);
        return cardOwnerInputField.getValue().equals(emptyString);
    }

    // Есть ли сообщение о неверном формате имени владельца карты
    public static boolean isSQLInjectionCardOwner(){
        return cardOwnerErrorMessage.getText().equals(invalidFormatMessage);
    }

    // Удаляется ли символ номера года с помощью Backspace
    public static boolean canEditWithBackspaceCardOwner(String string){
        cardOwnerInputField.setValue(string);
        cardOwnerInputField.sendKeys(Keys.BACK_SPACE);
        return cardOwnerInputField.getValue().equals(string.substring(0, string.length()-1));
    }

    // Удаляется ли символ номера года с помощью Delete
    public static boolean canEditWithDeleteCardOwner(String string){
        cardOwnerInputField.setValue(string);
        cardOwnerInputField.sendKeys(Keys.HOME);
        cardOwnerInputField.sendKeys(Keys.DELETE);
        return cardOwnerInputField.getValue().equals(string.substring(1));
    }

    // Есть ли собщение о необходимости заполнения поля CVC/CVV
    public static boolean isEmptyCVC_CVVCode(){
        return cardCVC_CVVCodeErrorMessage.getText().equals(fillRequiredMessage);
    }

    // Есть ли собщение о слишком коротком коде CVC/CVV
    public static boolean isShortCVC_CVVCode(){
        return cardCVC_CVVCodeErrorMessage.getText().equals(invalidFormatMessage);
    }

    // Игнорируется ли ввод лишнего символа в коде CVC/CVV
    public static boolean canNotInputLongCVC_CVVCode(String string){
        cardCVC_CVVCodeInputField.setValue(string);
        return cardCVC_CVVCodeInputField.getValue().equals(string.substring(0, string.length()-1));
    }

    // Игнорируется ли ввод буквы в коде CVC/CVV
    public static boolean canNotInputLettersInCVC_CVVCode(String letter){
        val emptyString = "";

        cardCVC_CVVCodeInputField.sendKeys(letter);
        return cardCVC_CVVCodeInputField.getValue().equals(emptyString);
    }

    // Игнорируется ли ввод специального символа в коде CVC/CVV
    public static boolean canNotInputSpecialSymbolsInCVC_CVVCode(String symbol){
        val emptyString = "";

        cardCVC_CVVCodeInputField.sendKeys(symbol);
        return cardCVC_CVVCodeInputField.getValue().equals(emptyString);
    }

    // Удаляется ли символ кода CVC/CVV с помощью Backspace
    public static boolean canEditWithBackspaceCVC_CVVCode(String string){
        cardCVC_CVVCodeInputField.setValue(string);
        cardCVC_CVVCodeInputField.sendKeys(Keys.BACK_SPACE);
        return cardCVC_CVVCodeInputField.getValue().equals(string.substring(0, string.length()-1));
    }

    // Удаляется ли символ кода CVC/CVV с помощью Delete
    public static boolean canEditWithDeleteYCVC_CVVCode(String string){
        cardCVC_CVVCodeInputField.setValue(string);
        cardCVC_CVVCodeInputField.sendKeys(Keys.HOME);
        cardCVC_CVVCodeInputField.sendKeys(Keys.DELETE);
        return cardCVC_CVVCodeInputField.getValue().equals(string.substring(1));
    }
}
