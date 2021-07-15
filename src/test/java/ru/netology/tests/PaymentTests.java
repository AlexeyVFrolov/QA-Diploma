package ru.netology.tests;

import com.codeborne.selenide.junit5.ScreenShooterExtension;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.val;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import ru.netology.data.DataGenerator;
import ru.netology.dbops.DataBaseOperations;
import ru.netology.page.ShopStartPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith({ScreenShooterExtension.class})
public class PaymentTests {

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
    @DisplayName("Подтверждение банком платежа по карте и его обработка")
    void shouldConfirmPaymentIfValidDataAndApprovedCard() {
        val shopStartPage = new ShopStartPage();
        val paymentPage = shopStartPage.choosePayment();
        val approved = "APPROVED";

        DataBaseOperations.clearDataBase();
        val sendingStatus = paymentPage.requestPayment(DataGenerator.generateValidApprovedCardInfo().getNumber(),
                DataGenerator.generateMonthNumberWithShift(0),
                DataGenerator.generateYearNumberWithShift(0),
                DataGenerator.generateCardOwnerName(),
                DataGenerator.justDigits(3));

        assertEquals(paymentPage.sendingSuccess, sendingStatus);
        assertEquals(paymentPage.paymentApproved, paymentPage.bankResponse());
        assertTrue(DataBaseOperations.isRightDataBasePaymentOrderRecord(approved), "Запись в базу данных не внесена либо внесена неверно");
    }

    @Test
    @DisplayName("Отклонение банком платежа по карте и его обработка")
    void shouldDeclinePaymentIfValidDataAndDeclinedCard() {
        val shopStartPage = new ShopStartPage();
        val paymentPage = shopStartPage.choosePayment();
        val declined = "DECLINED";

        DataBaseOperations.clearDataBase();
        val sendingStatus =  paymentPage.requestPayment(DataGenerator.generateValidDeclinedCardInfo().getNumber(),
                DataGenerator.generateMonthNumberWithShift(0),
                DataGenerator.generateYearNumberWithShift(0),
                DataGenerator.generateCardOwnerName(),
                DataGenerator.justDigits(3));

        assertEquals(paymentPage.sendingSuccess, sendingStatus);
        assertEquals(paymentPage.paymentDeclined, paymentPage.bankResponse());
        assertTrue(DataBaseOperations.isRightDataBasePaymentOrderRecord(declined), "Запись в базу данных не внесена либо внесена неверно");
    }

    @Test
    @DisplayName("Отклонение банком платежа по несуществующей карте и его обработка")
    void shouldDeclinePaymentIfInvalidCard() {
        val shopStartPage = new ShopStartPage();
        val paymentPage = shopStartPage.choosePayment();

        DataBaseOperations.clearDataBase();
        val sendingStatus = paymentPage.requestPayment(DataGenerator.generateInValidCardInfo().getNumber(),
                DataGenerator.generateMonthNumberWithShift(0),
                DataGenerator.generateYearNumberWithShift(0),
                DataGenerator.generateCardOwnerName(),
                DataGenerator.justDigits(3));

        assertEquals(paymentPage.sendingSuccess, sendingStatus);
        assertEquals(paymentPage.paymentDeclined, paymentPage.bankResponse());
        assertTrue(DataBaseOperations.isNoDataBasePaymentOrderRecord(), "В базу данных внесена запись об операции с несуществующей картой");
    }

    @Test
    @DisplayName("Ошибка при отсутствии номера карты в форме заявки на оплату по карте")
    void shouldAlertIfEmptyPaymentCardNumber(){
        val shopStartPage = new ShopStartPage();
        val paymentPage = shopStartPage.choosePayment();
        val sendingStatus = paymentPage.requestPayment(DataGenerator.generateEmptyCardInfo().getNumber(),
                DataGenerator.generateMonthNumberWithShift(0),
                DataGenerator.generateYearNumberWithShift(0),
                DataGenerator.generateCardOwnerName(),
                DataGenerator.justDigits(3));

        assertEquals(paymentPage.sendingError, sendingStatus);
        assertTrue(paymentPage.isPaymentFormEmptyCardNumberAlert(), "Нет собщения о необходимости заполнения поля Номер карты");
    }

    @Test
    @DisplayName("Ошибка при неполном номере карты в форме заявки на оплату по карте")
    void shouldAlertIfShortPaymentCardNumber(){
        val shopStartPage = new ShopStartPage();
        val paymentPage = shopStartPage.choosePayment();
        val sendingStatus = paymentPage.requestPayment(DataGenerator.justDigits(3),
                DataGenerator.generateMonthNumberWithShift(0),
                DataGenerator.generateYearNumberWithShift(0),
                DataGenerator.generateCardOwnerName(),
                DataGenerator.justDigits(3));

        assertEquals(paymentPage.sendingError, sendingStatus);
        assertTrue(paymentPage.isPaymentFormShortCardNumberAlert(), "Нет собщения о неполном номере карты");
    }

    @Test
    @DisplayName("Контроль ввода слишком длинного номера карты в форме заявки на оплату по карте")
    void shouldNotEnterLongPaymentCardNumber() {
        val shopStartPage = new ShopStartPage();
        val paymentPage = shopStartPage.choosePayment();

        assertTrue(paymentPage.canNotInputLongPaymentCardNumber(DataGenerator.generateValidApprovedCardInfo().getNumber()
                + DataGenerator.justDigits(1)), "Допустим ввод слишком длинного номера карты");
    }

    @Test
    @DisplayName("Контроль ввода букв в номере карты в форме заявки на оплату по карте")
    void shouldNotInputLettersInPaymentCardNumber() {
        val shopStartPage = new ShopStartPage();
        val paymentPage = shopStartPage.choosePayment();

        assertTrue(paymentPage.canNotInputLettersInPaymentCardNumber(DataGenerator.justLetters(1)), "Допустим ввод буквы в номере карты");
    }

    @Test
    @DisplayName("Контроль ввода специальных символов в номере карты в форме заявки на оплату по карте")
    void shouldNotInputSpecialSymbolsInPaymentCardNumber() {
        val shopStartPage = new ShopStartPage();
        val paymentPage = shopStartPage.choosePayment();

        assertTrue(paymentPage.canNotInputSpecialSymbolsInPaymentCardNumber(DataGenerator.justOneSpecialSymbol()), "Допустим ввод специального символа в номере карты");
    }

    @Test
    @DisplayName("Возможность редактирования номера карты с Backspace в форме заявки на оплату по карте")
    void shouldEditWithBackspacePaymentCardNumber() {
        val shopStartPage = new ShopStartPage();
        val paymentPage = shopStartPage.choosePayment();

        assertTrue(paymentPage.canEditWithBackspacePaymentCardNumber(DataGenerator.justDigits(3)), "Невозможно редактирование номера карты с помощью Backspace");
    }

    @Test
    @DisplayName("Возможность редактирования номера карты с Delete в форме заявки на оплату по карте")
    void shouldEditWithDeletePaymentCardNumber() {
        val shopStartPage = new ShopStartPage();
        val paymentPage = shopStartPage.choosePayment();

        assertTrue(paymentPage.canEditWithDeletePaymentCardNumber(DataGenerator.justDigits(3)), "Невозможно редактирование номера карты с помощью Delete");
    }

    @Test
    @DisplayName("Ошибка при отсутствии номера месяца в форме заявки на оплату по карте")
    void shouldAlertIfEmptyPaymentMonthNumber(){
        val shopStartPage = new ShopStartPage();
        val paymentPage = shopStartPage.choosePayment();
        val sendingStatus = paymentPage.requestPayment(DataGenerator.generateValidApprovedCardInfo().getNumber(),
                "",
                DataGenerator.generateMonthNumberWithShift(0),
                DataGenerator.generateCardOwnerName(),
                DataGenerator.justDigits(3));

        assertEquals(paymentPage.sendingError, sendingStatus);
        assertTrue(paymentPage.isPaymentFormEmptyMonthNumberAlert(), "Нет собщения о необходимости заполнения поля Месяц");
    }

    @Test
    @DisplayName("Ошибка при неполном номере месяца в форме заявки на оплату по карте")
    void shouldAlertIfShortPaymentMonthNumber(){
        val shopStartPage = new ShopStartPage();
        val paymentPage = shopStartPage.choosePayment();
        val sendingStatus = paymentPage.requestPayment(DataGenerator.generateValidApprovedCardInfo().getNumber(),
                DataGenerator.justDigits(1),
                DataGenerator.generateMonthNumberWithShift(0),
                DataGenerator.generateCardOwnerName(),
                DataGenerator.justDigits(3));

        assertEquals(paymentPage.sendingError, sendingStatus);
        assertTrue(paymentPage.isPaymentFormShortMonthNumberAlert(), "Нет собщения о неполном номере месяца");
    }

    @Test
    @DisplayName("Контроль ввода слишком длинного номера месяца в форме заявки на оплату по карте")
    void shouldNotEnterLongPaymentMonthNumber(){
        val shopStartPage = new ShopStartPage();
        val paymentPage = shopStartPage.choosePayment();

        assertTrue(paymentPage.canNotInputLongPaymentMonthNumber(DataGenerator.justDigits(3)), "Допустим ввод слишком длинного номера месяца");
    }

    @Test
    @DisplayName("Контроль ввода несуществующего (00) номера месяца в форме заявки на оплату по карте")
    void shouldAlertIfZeroPaymentMonthNumber(){
        val shopStartPage = new ShopStartPage();
        val paymentPage = shopStartPage.choosePayment();
        val sendingStatus =  paymentPage.requestPayment(DataGenerator.generateValidApprovedCardInfo().getNumber(),
                "00",
                DataGenerator.generateYearNumberWithShift(1),
                DataGenerator.generateCardOwnerName(),
                DataGenerator.justDigits(3));

        assertEquals(paymentPage.sendingError, sendingStatus);
        assertTrue(paymentPage.isPaymentFormZeroMonthNumberAlert(), "Допустим ввод номера месяца 00");
    }

    @Test
    @DisplayName("Ввод номера месяца 01 в форме заявки на оплату по карте")
    void shouldAcceptFirstPaymentMonthNumber(){
        val shopStartPage = new ShopStartPage();
        val paymentPage = shopStartPage.choosePayment();
        val sendingStatus =  paymentPage.requestPayment(DataGenerator.generateValidApprovedCardInfo().getNumber(),
                "01",
                DataGenerator.generateYearNumberWithShift(1),
                DataGenerator.generateCardOwnerName(),
                DataGenerator.justDigits(3));

        assertEquals(paymentPage.sendingSuccess, sendingStatus);
        assertEquals(paymentPage.paymentApproved, paymentPage.bankResponse());
    }

    @Test
    @DisplayName("Ввод номера месяца 12 в форме заявки на оплату по карт")
    void shouldAcceptLastPaymentMonthNumber(){
        val shopStartPage = new ShopStartPage();
        val paymentPage = shopStartPage.choosePayment();
        val sendingStatus = paymentPage.requestPayment(DataGenerator.generateValidApprovedCardInfo().getNumber(),
                "12",
                DataGenerator.generateYearNumberWithShift(1),
                DataGenerator.generateCardOwnerName(),
                DataGenerator.justDigits(3));

        assertEquals(paymentPage.sendingSuccess, sendingStatus);
        assertEquals(paymentPage.paymentApproved, paymentPage.bankResponse());
    }

    @Test
    @DisplayName("Контроль ввода несуществующего (13) номера месяца в форме заявки на оплату по карт")
    void shouAlertIfTooBigPaymentMonthNumber(){
        val shopStartPage = new ShopStartPage();
        val paymentPage = shopStartPage.choosePayment();
        val sendingStatus = paymentPage.requestPayment(DataGenerator.generateValidApprovedCardInfo().getNumber(),
                "13",
                DataGenerator.generateYearNumberWithShift(1),
                DataGenerator.generateCardOwnerName(),
                DataGenerator.justDigits(3));

        assertEquals(paymentPage.sendingError, sendingStatus);
        assertTrue(paymentPage.isPaymentFormTooBigMonthNumberAlert(), "Допустим ввод номера месяца 13");
    }

    @Test
    @DisplayName("Контроль ввода букв в номере месяца в форме заявки на оплату по карте")
    void shouldNotInputLettersInPaymentMonthNumber(){
        val shopStartPage = new ShopStartPage();
        val paymentPage = shopStartPage.choosePayment();

        assertTrue(paymentPage.canNotInputLettersInPaymentMonthNumber(DataGenerator.justLetters(1)), "Допускается ввод букв в номере месяц");
    }

    @Test
    @DisplayName("Контроль ввода специальных символов в номере месяца в форме заявки на оплату по карте")
    void shouldNotInputSpecialSymbolsInPaymentMonthNumber(){
        val shopStartPage = new ShopStartPage();
        val paymentPage = shopStartPage.choosePayment();

        assertTrue(paymentPage.canNotInputSpecialSymbolsInPaymentMonthNumber(DataGenerator.justOneSpecialSymbol()), "Допускается ввод спецсимволов в номере месяца");
    }

    @Test
    @DisplayName("Возможность редактирования номера месяца с Backspace в форме заявки на оплату по карте")
    void shouldEditWithBackspacePaymentMonthNumber() {
        val shopStartPage = new ShopStartPage();
        val paymentPage = shopStartPage.choosePayment();

        assertTrue(paymentPage.canEditWithBackspacePaymentMonthNumber(DataGenerator.justDigits(2)), "Невозможно редактирование номера месяца с помощью Backspace");
    }

    @Test
    @DisplayName("Возможность редактирования номера месяца с Delete в форме заявки на оплату по карте")
    void shouldEditWithDeletePaymentMonthNumber() {
        val shopStartPage = new ShopStartPage();
        val paymentPage = shopStartPage.choosePayment();

        assertTrue(paymentPage.canEditWithDeletePaymentMonthNumber(DataGenerator.justDigits(2)), "Невозможно редактирование номера месяца с помощью Delete");
    }

    @Test
    @DisplayName("Ошибка при отсутствии номера года в форме заявки на оплату по карте")
    void shouldAlertIfEmptyPaymentYearNumber(){
        val shopStartPage = new ShopStartPage();
        val paymentPage = shopStartPage.choosePayment();
        val sendingStatus = paymentPage.requestPayment(DataGenerator.generateValidApprovedCardInfo().getNumber(),
                DataGenerator.generateMonthNumberWithShift(0),
                "",
                DataGenerator.generateCardOwnerName(),
                DataGenerator.justDigits(3));

        assertEquals(paymentPage.sendingError, sendingStatus);
        assertTrue(paymentPage.isPaymentFormEmptyYearNumberAlert(), "Нет собщения о необходимости заполнения поля Год");
    }

    @Test
    @DisplayName("Ошибка при неполном номере года в форме заявки на оплату по карте")
    void shouldAlertIfShortPaymentYearNumber(){
        val shopStartPage = new ShopStartPage();
        val paymentPage = shopStartPage.choosePayment();
        val sendingStatus = paymentPage.requestPayment(DataGenerator.generateValidApprovedCardInfo().getNumber(),
                DataGenerator.generateMonthNumberWithShift(0),
                DataGenerator.justDigits(1),
                DataGenerator.generateCardOwnerName(),
                DataGenerator.justDigits(3));

        assertEquals(paymentPage.sendingError, sendingStatus);
        assertTrue(paymentPage.isPaymentFormShortYearNumberAlert(), "Нет собщения о неполном номере года");
    }

    @Test
    @DisplayName("Контроль ввода слишком длинного номера года в форме заявки на оплату по карте")
    void shouldNotEnterLongPaymentYearNumber(){
        val shopStartPage = new ShopStartPage();
        val paymentPage = shopStartPage.choosePayment();

        assertTrue(paymentPage.canNotInputLongPaymentYearNumber(DataGenerator.justDigits(3)), "Допустим ввод слишком длинного номера года");
    }

    @Test
    @DisplayName("Ошибка при вводе истекшего года срока действия карты в форме заявки на оплату по карте")
    void shouldAlertIfExpiredPaymentYearNumber(){
        val shopStartPage = new ShopStartPage();
        val paymentPage = shopStartPage.choosePayment();
        val sendingStatus =  paymentPage.requestPayment(DataGenerator.generateValidApprovedCardInfo().getNumber(),
                DataGenerator.generateMonthNumberWithShift(0),
                DataGenerator.generateYearNumberWithShift(-1),
                DataGenerator.generateCardOwnerName(),
                DataGenerator.justDigits(3));

        assertEquals(paymentPage.sendingError, sendingStatus);
        assertTrue(paymentPage.isPaymentFormExpiredYearNumberAlert(), "Допустим ввод года истекшего срока действия карты");
    }

    @Test
    @DisplayName("Максимально возможный год срока действия карты в форме заявки на оплату по карте")
    void shouldAcceptMaximumCardExpiryPaymentYearNumber(){
        val shopStartPage = new ShopStartPage();
        val paymentPage = shopStartPage.choosePayment();
        int maxCardValidityPeriod = 5;
        val sendingStatus = paymentPage.requestPayment(DataGenerator.generateValidApprovedCardInfo().getNumber(),
                DataGenerator.generateMonthNumberWithShift(0),
                DataGenerator.generateYearNumberWithShift(maxCardValidityPeriod),
                DataGenerator.generateCardOwnerName(),
                DataGenerator.justDigits(3));

        assertEquals(paymentPage.sendingSuccess, sendingStatus);
        assertEquals(paymentPage.paymentApproved, paymentPage.bankResponse());
    }

    @Test
    @DisplayName("Ошибка при вводе года, превышающего возможный срок действия карты в форме заявки на оплату по карте")
    void shouldAlertIfTooBigPaymentYearNumber(){
        val shopStartPage = new ShopStartPage();
        val paymentPage = shopStartPage.choosePayment();
        int overMaxCardValidityPeriod = 6;
        val sendingStatus =  paymentPage.requestPayment(DataGenerator.generateValidApprovedCardInfo().getNumber(),
                DataGenerator.generateMonthNumberWithShift(0),
                DataGenerator.generateYearNumberWithShift(overMaxCardValidityPeriod),
                DataGenerator.generateCardOwnerName(),
                DataGenerator.justDigits(3));

        assertEquals(paymentPage.sendingError, sendingStatus);
        assertTrue(paymentPage.isPaymentFormTooBigYearNumberAlert(), "Допустим ввод номера года более возможного срока действия карты");
    }

    @Test
    @DisplayName("Контроль ввода букв в номере года в форме заявки на оплату по карте")
    void shouldNotInputLettersInPaymentYearNumber(){
        val shopStartPage = new ShopStartPage();
        val paymentPage = shopStartPage.choosePayment();

        assertTrue(paymentPage.canNotInputLettersInPaymentYearNumber(DataGenerator.justLetters(1)), "Допускается ввод букв в номере года");
    }

    @Test
    @DisplayName("Контроль ввода специальных символов в номере года в форме заявки на оплату по карте")
    void shouldNotInputSpecialSymbolsInPaymentYearNumber(){
        val shopStartPage = new ShopStartPage();
        val paymentPage = shopStartPage.choosePayment();

        assertTrue(paymentPage.canNotInputSpecialSymbolsInPaymentYearNumber(DataGenerator.justOneSpecialSymbol()), "Допускается ввод спецсимволов в номере года");
    }

    @Test
    @DisplayName("Возможность редактирования номера года с Backspace в форме заявки на оплату по карте")
    void shouldEditWithBackspacePaymentYearNumber() {
        val shopStartPage = new ShopStartPage();
        val paymentPage = shopStartPage.choosePayment();

        assertTrue(paymentPage.canEditWithBackspacePaymentYearNumber(DataGenerator.justDigits(2)), "Невозможно редактирование номера года с помощью Backspace");
    }

    @Test
    @DisplayName("Возможность редактирования номера года с Delete в форме заявки на оплату по карте")
    void shouldEditWithDeletePaymentYearNumber() {
        val shopStartPage = new ShopStartPage();
        val paymentPage = shopStartPage.choosePayment();

        assertTrue(paymentPage.canEditWithDeletePaymentYearNumber(DataGenerator.justDigits(2)), "Невозможно редактирование номера года с помощью Delete");
    }

    @Test
    @DisplayName("Контроль ввода истекшего срока действия карты в текущем году в форме заявки на оплату по карте")
    void shouldAlertIfExpiredDateInCurrentYearPayment(){
        val shopStartPage = new ShopStartPage();
        val paymentPage = shopStartPage.choosePayment();
        val sendingStatus = paymentPage.requestPayment(DataGenerator.generateValidApprovedCardInfo().getNumber(),
                DataGenerator.generateMonthNumberWithShift(-1),
                DataGenerator.generateYearNumberWithShift(0),
                DataGenerator.generateCardOwnerName(),
                DataGenerator.justDigits(3));

        assertEquals(paymentPage.sendingError, sendingStatus);
        assertTrue(paymentPage.isPaymentFormExpiredMonthNumberAlert(), "Допустим ввод истекшего срока действия карты");
    }

    @Test
    @DisplayName("Ошибка при отсутствии имени владельца карты в форме заявки на оплату по карте")
    void shouldAlertIfEmptyPaymentCardOwner(){
        val shopStartPage = new ShopStartPage();
        val paymentPage = shopStartPage.choosePayment();
        val sendingStatus = paymentPage.requestPayment(DataGenerator.generateValidApprovedCardInfo().getNumber(),
                DataGenerator.generateMonthNumberWithShift(0),
                DataGenerator.generateYearNumberWithShift(0),
                "",
                DataGenerator.justDigits(3));

        assertEquals(paymentPage.sendingError, sendingStatus);
        assertTrue(paymentPage.isPaymentFormEmptyCardOwnerAlert(), "Нет собщения о необходимости заполнения поля Владелец");
    }

    @Test
    @DisplayName("Ошибка при однобуквенном имени владельца карты в форме заявки на оплату по карте")
    void shouldAlertIfShortPaymentCardOwner(){
        val shopStartPage = new ShopStartPage();
        val paymentPage = shopStartPage.choosePayment();
        val sendingStatus = paymentPage.requestPayment(DataGenerator.generateValidApprovedCardInfo().getNumber(),
                DataGenerator.generateMonthNumberWithShift(0),
                DataGenerator.generateYearNumberWithShift(0),
                DataGenerator.justLetters(1),
                DataGenerator.justDigits(3));

        assertEquals(paymentPage.sendingError, sendingStatus);
        assertTrue(paymentPage.isPaymentFormShortCardOwnerAlert(), "Допустим ввод имени владельца, состоящего из одной буквы");
    }

    @Test
    @DisplayName("Контроль ввода цифр в имени владельца карты в форме заявки на оплату по карте")
    void shouldNotInputDigitsInPaymentCardOwner(){
        val shopStartPage = new ShopStartPage();
        val paymentPage = shopStartPage.choosePayment();

        assertTrue(paymentPage.canNotInputDigitsInPaymentCardOwner(DataGenerator.justDigits(1)), "Допускается ввод цифр в имени владельца карты");
    }

    @Test
    @DisplayName("Контроль ввода специальных символов в имени владельца карты в форме заявки на оплату по карте")
    void shouldNotInputSpecialSymbolsInPaymentCardOwner(){
        val shopStartPage = new ShopStartPage();
        val paymentPage = shopStartPage.choosePayment();

        assertTrue(paymentPage.canNotInputSpecialSymbolsInPaymentCardOwner(DataGenerator.justOneSpecialSymbol()), "Допускается ввод спецсимволов в имени владельца карты");
    }

    @Test
    @DisplayName("Контроль ввода SQL injection в поле Владелец в форме заявки на оплату по карте")
    void shouldAlertIfSQLInjectionPaymentCardOwner(){
        val shopStartPage = new ShopStartPage();
        val paymentPage = shopStartPage.choosePayment();
        val sendingStatus = paymentPage.requestPayment(DataGenerator.generateValidApprovedCardInfo().getNumber(),
                DataGenerator.generateMonthNumberWithShift(0),
                DataGenerator.generateYearNumberWithShift(0),
                "or 1 = 1; -",
                DataGenerator.justDigits(3));

        assertEquals(paymentPage.sendingError, sendingStatus);
        assertTrue(paymentPage.isPaymentFormSQLInjectionCardOwnerAlert(), "Допускается ввод последовательности SQL Injection в имени владельца карты");
    }

    @Test
    @DisplayName("Возможность редактирования имени владельца с Backspace в форме заявки на оплату по карте")
    void shouldEditWithBackspacePaymentCardOwner() {
        val shopStartPage = new ShopStartPage();
        val paymentPage = shopStartPage.choosePayment();

        assertTrue(paymentPage.canEditWithBackspacePaymentCardOwner(DataGenerator.justLetters(2)), "Невозможно редактирование имени владельца с помощью Backspace");
    }

    @Test
    @DisplayName("Возможность редактирования имени владельца с Delete в форме заявки на оплату по карте")
    void shouldEditWithDeletePaymentCardOwner() {
        val shopStartPage = new ShopStartPage();
        val paymentPage = shopStartPage.choosePayment();

        assertTrue(paymentPage.canEditWithDeletePaymentCardOwner(DataGenerator.justLetters(2)), "Невозможно редактирование имени владельца с помощью Delete");
    }

    @Test
    @DisplayName("Ошибка при отсутствии кода CVC/CVV в форме заявки на оплату по карте")
    void shouldAlertIfEmptyPaymentCVC_CVVCode(){
        val shopStartPage = new ShopStartPage();
        val paymentPage = shopStartPage.choosePayment();
        val sendingStatus = paymentPage.requestPayment(DataGenerator.generateValidApprovedCardInfo().getNumber(),
                DataGenerator.generateMonthNumberWithShift(0),
                DataGenerator.generateYearNumberWithShift(0),
                DataGenerator.generateCardOwnerName(),
                "");

        assertEquals(paymentPage.sendingError, sendingStatus);
        assertTrue(paymentPage.isPaymentFormEmptyCVC_CVVCodeAlert(), "Нет собщения о необходимости заполнения поля CVC/CVV");
    }

    @Test
    @DisplayName("Ошибка при неполном коде CVC/CVV в форме заявки на оплату по карт")
    void shouldAlertIfShortPaymentCVC_CVVCode(){
        val shopStartPage = new ShopStartPage();
        val paymentPage = shopStartPage.choosePayment();
        val sendingStatus = paymentPage.requestPayment(DataGenerator.generateValidApprovedCardInfo().getNumber(),
                DataGenerator.generateMonthNumberWithShift(0),
                DataGenerator.generateYearNumberWithShift(0),
                DataGenerator.generateCardOwnerName(),
                DataGenerator.justDigits(1));

        assertEquals(paymentPage.sendingError, sendingStatus);
        assertTrue(paymentPage.isPaymentFormShortCVC_CVVCodeAlert(), "Нет собщения о неполном коде CVC/CVV");
    }

    @Test
    @DisplayName("Контроль ввода слишком длинного кода CVC/CVV в форме заявки на оплату по карте")
    void shouldNotEnterLongPaymentCVC_CVVCode(){
        val shopStartPage = new ShopStartPage();
        val paymentPage = shopStartPage.choosePayment();

        assertTrue(paymentPage.canNotInputLongPaymentCVC_CVVCode(DataGenerator.justDigits(4)), "Допустим ввод слишком длинного кода CVC/CVV");
    }

    @Test
    @DisplayName("Контроль ввода букв в коде CVC/CVV в форме заявки на оплату по карте")
    void shouldNotInputLettersInPaymentCVC_CVVCode(){
        val shopStartPage = new ShopStartPage();
        val paymentPage = shopStartPage.choosePayment();

        assertTrue(paymentPage.canNotInputLettersInPaymentCVC_CVVCode(DataGenerator.justLetters(1)), "Допускается ввод букв в коде CVC/CVV");
    }

    @Test
    @DisplayName("Контроль ввода специальных символов в коде CVC/CVV в форме заявки на оплату по карте")
    void shouldNotInputSpecialSymbolsInPaymentCVC_CVVCode(){
        val shopStartPage = new ShopStartPage();
        val paymentPage = shopStartPage.choosePayment();

        assertTrue(paymentPage.canNotInputSpecialSymbolsInPaymentCVC_CVVCode(DataGenerator.justOneSpecialSymbol()), "Допускается ввод спецсимволов в коде CVC/CVV");
    }

    @Test
    @DisplayName("Возможность редактирования кода CVC/CVV с Backspace в форме заявки на оплату по карте")
    void shouldEditWithBackspacePaymentCVC_CVVCode() {
        val shopStartPage = new ShopStartPage();
        val paymentPage = shopStartPage.choosePayment();

        assertTrue(paymentPage.canEditWithBackspacePaymentCVC_CVVCode(DataGenerator.justDigits(2)), "Невозможно редактирование кода CVC/CVV с помощью Backspace");
    }

    @Test
    @DisplayName("Возможность редактирования кода CVC/CVV с Delete в форме заявки на оплату по карте")
    void shouldEditWithDeletePaymentCVC_CVVCode() {
        val shopStartPage = new ShopStartPage();
        val paymentPage = shopStartPage.choosePayment();

        assertTrue(paymentPage.canEditWithDeletePaymentCVC_CVVCode(DataGenerator.justDigits(2)), "Невозможно редактирование кода CVC/CVV с помощью Delete");
    }
}
