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
public class CreditTests {

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
    @DisplayName("Подтверждение банком кредита по данным карты и его обработка")
    void shouldConfirmCreditIfValidDataAndApprovedCard() {
        val shopStartPage = new ShopStartPage();
        val creditPage = shopStartPage.chooseCredit();
        val approved = "APPROVED";

        DataBaseOperations.clearDataBase();
        val sendingStatus = creditPage.requestCredit(DataGenerator.generateValidApprovedCardInfo().getNumber(),
                DataGenerator.generateMonthNumberWithShift(0),
                DataGenerator.generateYearNumberWithShift(0),
                DataGenerator.generateCardOwnerName(),
                DataGenerator.justDigits(3));

        assertEquals(creditPage.sendingSuccess, sendingStatus);
        assertEquals(creditPage.creditApproved, creditPage.bankResponse());
        assertTrue(DataBaseOperations.isRightDataBaseCreditOrderRecord(approved), "Запись в базу данных не внесена либо внесена неверно");
    }

    @Test
    @DisplayName("Отклонение банком кредита по данным карты и его обработка")
    void shouldDeclineCreditIfValidDataAndDeclinedCard() {
        val shopStartPage = new ShopStartPage();
        val creditPage = shopStartPage.chooseCredit();
        val declined = "DECLINED";

        DataBaseOperations.clearDataBase();
        val sendingStatus = creditPage.requestCredit(DataGenerator.generateValidDeclinedCardInfo().getNumber(),
                DataGenerator.generateMonthNumberWithShift(0),
                DataGenerator.generateYearNumberWithShift(0),
                DataGenerator.generateCardOwnerName(),
                DataGenerator.justDigits(3));

        assertEquals(creditPage.sendingSuccess, sendingStatus);
        assertEquals(creditPage.creditDeclined, creditPage.bankResponse());
        assertTrue(DataBaseOperations.isRightDataBaseCreditOrderRecord(declined), "Запись в базу данных не внесена либо внесена неверно");
    }

    @Test
    @DisplayName("Отклонение банком кредита по данным несуществующей карты и его обработка")
    void shouldDeclineCreditIfInvalidCard() {
        val shopStartPage = new ShopStartPage();
        val creditPage = shopStartPage.chooseCredit();

        DataBaseOperations.clearDataBase();
        val sendingStatus = creditPage.requestCredit(DataGenerator.generateInValidCardInfo().getNumber(),
                DataGenerator.generateMonthNumberWithShift(0),
                DataGenerator.generateYearNumberWithShift(0),
                DataGenerator.generateCardOwnerName(),
                DataGenerator.justDigits(3));

        assertEquals(creditPage.sendingSuccess, sendingStatus);
        assertEquals(creditPage.creditDeclined, creditPage.bankResponse());
        assertTrue(DataBaseOperations.isNoDataBaseCreditOrderRecord(), "В базу данных внесена запись об операции с несуществующей картой");
    }

    @Test
    @DisplayName("Ошибка при отсутствии номера карты в форме заявки на покупку в кредит")
    void shouldAlertIfEmptyCreditCardNumber(){
        val shopStartPage = new ShopStartPage();
        val creditPage = shopStartPage.chooseCredit();
        val sendingStatus = creditPage.requestCredit(DataGenerator.generateEmptyCardInfo().getNumber(),
                DataGenerator.generateMonthNumberWithShift(0),
                DataGenerator.generateYearNumberWithShift(0),
                DataGenerator.generateCardOwnerName(),
                DataGenerator.justDigits(3));

        assertEquals(creditPage.sendingError, sendingStatus);
        assertTrue(creditPage.isCreditFormEmptyCardNumberAlert(), "Нет собщения о необходимости заполнения поля Номер карты");
    }

    @Test
    @DisplayName("Ошибка при неполном номере карты в форме заявки на покупку в кредит")
    void shouldAlertIfShortCreditCardNumber(){
        val shopStartPage = new ShopStartPage();
        val creditPage = shopStartPage.chooseCredit();
        val sendingStatus = creditPage.requestCredit(DataGenerator.justDigits(3),
                DataGenerator.generateMonthNumberWithShift(0),
                DataGenerator.generateYearNumberWithShift(0),
                DataGenerator.generateCardOwnerName(),
                DataGenerator.justDigits(3));

        assertEquals(creditPage.sendingError, sendingStatus);
        assertTrue(creditPage.isCreditFormShortCardNumberAlert(), "Нет собщения о неполном номере карты");
    }

    @Test
    @DisplayName("Контроль ввода слишком длинного номера карты в форме заявки на покупку в кредит")
    void shouldNotEnterLongCreditCardNumber() {
        val shopStartPage = new ShopStartPage();
        val creditPage = shopStartPage.chooseCredit();

        assertTrue(creditPage.canNotInputLongCreditCardNumber(DataGenerator.generateValidApprovedCardInfo().getNumber()
                + DataGenerator.justDigits(1)), "Допустим ввод слишком длинного номера карты");
    }

    @Test
    @DisplayName("Контроль ввода букв в номере карты в форме заявки на покупку в кредит")
    void shouldNotInputLettersInCreditCardNumber() {
        val shopStartPage = new ShopStartPage();
        val creditPage = shopStartPage.chooseCredit();

        assertTrue(creditPage.canNotInputLettersInCreditCardNumber(DataGenerator.justLetters(1)), "Допустим ввод буквы в номере карты");
    }

    @Test
    @DisplayName("Контроль ввода специальных символов в номере карты в форме заявки на покупку в кредит")
    void shouldNotInputSpecialSymbolsInCreditCardNumber() {
        val shopStartPage = new ShopStartPage();
        val creditPage = shopStartPage.chooseCredit();

        assertTrue(creditPage.canNotInputSpecialSymbolsInCreditCardNumber(DataGenerator.justOneSpecialSymbol()), "Допустим ввод специального символа в номере карты");
    }

    @Test
    @DisplayName("Возможность редактирования номера карты с Backspace в форме заявки на покупку в кредит")
    void shouldEditWithBackspaceCreditCardNumber() {
        val shopStartPage = new ShopStartPage();
        val creditPage = shopStartPage.chooseCredit();

        assertTrue(creditPage.canEditWithBackspaceCreditCardNumber(DataGenerator.justDigits(3)), "Невозможно редактирование номера карты с помощью Backspace");
    }

    @Test
    @DisplayName("Возможность редактирования номера карты с Delete в форме заявки на покупку в кредит")
    void shouldEditWithDeleteCreditCardNumber() {
        val shopStartPage = new ShopStartPage();
        val creditPage = shopStartPage.chooseCredit();

        assertTrue(creditPage.canEditWithDeleteCreditCardNumber(DataGenerator.justDigits(3)), "Невозможно редактирование номера карты с помощью Delete");
    }

    @Test
    @DisplayName("Ошибка при отсутствии номера месяца в форме заявки на покупку в кредит")
    void shouldAlertIfEmptyCreditMonthNumber(){
        val shopStartPage = new ShopStartPage();
        val creditPage = shopStartPage.chooseCredit();
        val sendingStatus = creditPage.requestCredit(DataGenerator.generateValidApprovedCardInfo().getNumber(),
                "",
                DataGenerator.generateYearNumberWithShift(0),
                DataGenerator.generateCardOwnerName(),
                DataGenerator.justDigits(3));

        assertEquals(creditPage.sendingError, sendingStatus);
        assertTrue(creditPage.isCreditFormEmptyMonthNumberAlert(), "Нет собщения о необходимости заполнения поля Месяц");
    }

    @Test
    @DisplayName("Ошибка при неполном номере месяца в форме заявки на покупку в кредит")
    void shouldAlertIfShortCreditMonthNumber(){
        val shopStartPage = new ShopStartPage();
        val creditPage = shopStartPage.chooseCredit();
        val sendingStatus = creditPage.requestCredit(DataGenerator.generateValidApprovedCardInfo().getNumber(),
                DataGenerator.justDigits(1),
                DataGenerator.generateYearNumberWithShift(0),
                DataGenerator.generateCardOwnerName(),
                DataGenerator.justDigits(3));

        assertEquals(creditPage.sendingError, sendingStatus);
        assertTrue(creditPage.isCreditFormShortMonthNumberAlert(), "Нет собщения о неполном номере месяца");
    }

    @Test
    @DisplayName("Контроль ввода слишком длинного номера месяца в форме заявки на покупку в кредит")
    void shouldNotEnterLongCreditMonthNumber(){
        val shopStartPage = new ShopStartPage();
        val creditPage = shopStartPage.chooseCredit();

        assertTrue(creditPage.canNotInputLongCreditMonthNumber(DataGenerator.justDigits(3)), "Допустим ввод слишком длинного номера месяца");
    }

    @Test
    @DisplayName("Контроль ввода несуществующего (00) номера месяца в форме заявки на покупку в кредит")
    void shouldAlertIfZeroCreditMonthNumber(){
        val shopStartPage = new ShopStartPage();
        val creditPage = shopStartPage.chooseCredit();
        val sendingStatus = creditPage.requestCredit(DataGenerator.generateValidApprovedCardInfo().getNumber(),
                "00",
                DataGenerator.generateYearNumberWithShift(1),
                DataGenerator.generateCardOwnerName(),
                DataGenerator.justDigits(3));

        assertEquals(creditPage.sendingError, sendingStatus);
        assertTrue(creditPage.isCreditFormZeroMonthNumberAlert(), "Допустим ввод номера месяца 00");
    }

    @Test
    @DisplayName("Ввод номера месяца 01 в форме заявки на оплату по карт")
    void shouldAcceptFirstCreditMonthNumber(){
        val shopStartPage = new ShopStartPage();
        val creditPage = shopStartPage.chooseCredit();
        val sendingStatus = creditPage.requestCredit(DataGenerator.generateValidApprovedCardInfo().getNumber(),
                "00",
                DataGenerator.generateYearNumberWithShift(1),
                DataGenerator.generateCardOwnerName(),
                DataGenerator.justDigits(3));

        assertEquals(creditPage.sendingSuccess, sendingStatus);
        assertEquals(creditPage.creditApproved, creditPage.bankResponse());
    }

    @Test
    @DisplayName("Ввод номера месяца 12 в форме заявки на оплату по карт")
    void shouldAcceptLastCreditMonthNumber(){
        val shopStartPage = new ShopStartPage();
        val creditPage = shopStartPage.chooseCredit();
        val sendingStatus = creditPage.requestCredit(DataGenerator.generateValidApprovedCardInfo().getNumber(),
                "12",
                DataGenerator.generateYearNumberWithShift(1),
                DataGenerator.generateCardOwnerName(),
                DataGenerator.justDigits(3));

        assertEquals(creditPage.sendingSuccess, sendingStatus);
        assertEquals(creditPage.creditApproved, creditPage.bankResponse());
    }

    @Test
    @DisplayName("Контроль ввода несуществующего (13) номера месяца в форме заявки на оплату по карт")
    void shouAlertIfTooBigCreditMonthNumber(){
        val shopStartPage = new ShopStartPage();
        val creditPage = shopStartPage.chooseCredit();
        val sendingStatus = creditPage.requestCredit(DataGenerator.generateValidApprovedCardInfo().getNumber(),
                "13",
                DataGenerator.generateYearNumberWithShift(1),
                DataGenerator.generateCardOwnerName(),
                DataGenerator.justDigits(3));

        assertEquals(creditPage.sendingError, sendingStatus);
        assertTrue(creditPage.isCreditFormTooBigMonthNumberAlert(), "Допустим ввод номера месяца 13");
    }

    @Test
    @DisplayName("Контроль ввода букв в номере месяца в форме заявки на покупку в кредит")
    void shouldNotInputLettersInCreditMonthNumber(){
        val shopStartPage = new ShopStartPage();
        val creditPage = shopStartPage.chooseCredit();

        assertTrue(creditPage.canNotInputLettersInCreditMonthNumber(DataGenerator.justLetters(1)), "Допускается ввод букв в номере месяц");
    }

    @Test
    @DisplayName("Контроль ввода специальных символов в номере месяца в форме заявки на покупку в кредит")
    void shouldNotInputSpecialSymbolsInCreditMonthNumber(){
        val shopStartPage = new ShopStartPage();
        val creditPage = shopStartPage.chooseCredit();

        assertTrue(creditPage.canNotInputSpecialSymbolsInCreditMonthNumber(DataGenerator.justOneSpecialSymbol()), "Допускается ввод спецсимволов в номере месяца");
    }

    @Test
    @DisplayName("Возможность редактирования номера месяца с Backspace в форме заявки на покупку в кредит")
    void shouldEditWithBackspaceCreditMonthNumber() {
        val shopStartPage = new ShopStartPage();
        val creditPage = shopStartPage.chooseCredit();

        assertTrue(creditPage.canEditWithBackspaceCreditMonthNumber(DataGenerator.justDigits(2)), "Невозможно редактирование номера месяца с помощью Backspace");
    }

    @Test
    @DisplayName("Возможность редактирования номера месяца с Delete в форме заявки на покупку в кредит")
    void shouldEditWithDeleteCreditMonthNumber() {
        val shopStartPage = new ShopStartPage();
        val creditPage = shopStartPage.chooseCredit();

        assertTrue(creditPage.canEditWithDeleteCreditMonthNumber(DataGenerator.justDigits(2)), "Невозможно редактирование номера месяца с помощью Delete");
    }

    @Test
    @DisplayName("Ошибка при отсутствии номера года в форме заявки на покупку в кредит")
    void shouldAlertIfEmptyCreditYearNumber(){
        val shopStartPage = new ShopStartPage();
        val creditPage = shopStartPage.chooseCredit();
        val sendingStatus = creditPage.requestCredit(DataGenerator.generateValidApprovedCardInfo().getNumber(),
                DataGenerator.generateMonthNumberWithShift(0),
                "",
                DataGenerator.generateCardOwnerName(),
                DataGenerator.justDigits(3));

        assertEquals(creditPage.sendingError, sendingStatus);
        assertTrue(creditPage.isCreditFormEmptyYearNumberAlert(), "Нет собщения о необходимости заполнения поля Год");
    }

    @Test
    @DisplayName("Ошибка при неполном номере года в форме заявки на покупку в кредит")
    void shouldAlertIfShortCreditYearNumber(){
        val shopStartPage = new ShopStartPage();
        val creditPage = shopStartPage.chooseCredit();
        val sendingStatus =  creditPage.requestCredit(DataGenerator.generateValidApprovedCardInfo().getNumber(),
                DataGenerator.generateMonthNumberWithShift(0),
                DataGenerator.justDigits(1),
                DataGenerator.generateCardOwnerName(),
                DataGenerator.justDigits(3));

        assertEquals(creditPage.sendingError, sendingStatus);
        assertTrue(creditPage.isCreditFormShortYearNumberAlert(), "Нет собщения о неполном номере года");
    }

    @Test
    @DisplayName("Контроль ввода слишком длинного номера года в форме заявки на покупку в кредит")
    void shouldNotEnterLongCreditYearNumber(){
        val shopStartPage = new ShopStartPage();
        val creditPage = shopStartPage.chooseCredit();

        assertTrue(creditPage.canNotInputLongCreditYearNumber(DataGenerator.justDigits(3)), "Допустим ввод слишком длинного номера года");
    }

    @Test
    @DisplayName("Ошибка при вводе истекшего года срока действия карты в форме заявки на покупку в кредит")
    void shouldAlertIfExpiredCreditYearNumber(){
        val shopStartPage = new ShopStartPage();
        val creditPage = shopStartPage.chooseCredit();
        val sendingStatus = creditPage.requestCredit(DataGenerator.generateValidApprovedCardInfo().getNumber(),
                DataGenerator.generateMonthNumberWithShift(0),
                DataGenerator.generateYearNumberWithShift(-1),
                DataGenerator.generateCardOwnerName(),
                DataGenerator.justDigits(3));

        assertEquals(creditPage.sendingError, sendingStatus);
        assertTrue(creditPage.isCreditFormExpiredYearNumberAlert(), "Допустим ввод года истекшего срока действия карты");
    }

    @Test
    @DisplayName("Максимально возможный год срока действия карты в форме заявки на покупку в кредит")
    void shouldAcceptMaximumCardExpiryCreditYearNumber(){
        val shopStartPage = new ShopStartPage();
        val creditPage = shopStartPage.chooseCredit();
        int maxCardValidityPeriod = 5;
        val sendingStatus = creditPage.requestCredit(DataGenerator.generateValidApprovedCardInfo().getNumber(),
                DataGenerator.generateMonthNumberWithShift(0),
                DataGenerator.generateYearNumberWithShift(maxCardValidityPeriod),
                DataGenerator.generateCardOwnerName(),
                DataGenerator.justDigits(3));

        assertEquals(creditPage.sendingSuccess, sendingStatus);
        assertEquals(creditPage.creditApproved, creditPage.bankResponse());
    }

    @Test
    @DisplayName("Ошибка при вводе года, превышающего возможный срок действия карты в форме заявки на покупку в кредит")
    void shouldAlertIfTooBigCreditYearNumber(){
        val shopStartPage = new ShopStartPage();
        val creditPage = shopStartPage.chooseCredit();
        int overMaxCardValidityPeriod = 6;
        val sendingStatus = creditPage.requestCredit(DataGenerator.generateValidApprovedCardInfo().getNumber(),
                DataGenerator.generateMonthNumberWithShift(0),
                DataGenerator.generateYearNumberWithShift(overMaxCardValidityPeriod),
                DataGenerator.generateCardOwnerName(),
                DataGenerator.justDigits(3));

        assertEquals(creditPage.sendingError, sendingStatus);
        assertTrue(creditPage.isCreditFormTooBigYearNumberAlert(), "Допустим ввод номера года более возможного срока действия карты");
    }

    @Test
    @DisplayName("Контроль ввода букв в номере года в форме заявки на покупку в кредит")
    void shouldNotInputLettersInCreditYearNumber(){
        val shopStartPage = new ShopStartPage();
        val creditPage = shopStartPage.chooseCredit();

        assertTrue(creditPage.canNotInputLettersInCreditYearNumber(DataGenerator.justLetters(1)), "Допускается ввод букв в номере года");
    }

    @Test
    @DisplayName("Контроль ввода специальных символов в номере года в форме заявки на покупку в кредит")
    void shouldNotInputSpecialSymbolsInCreditYearNumber(){
        val shopStartPage = new ShopStartPage();
        val creditPage = shopStartPage.chooseCredit();

        assertTrue(creditPage.canNotInputSpecialSymbolsInCreditYearNumber(DataGenerator.justOneSpecialSymbol()), "Допускается ввод спецсимволов в номере года");
    }

    @Test
    @DisplayName("Возможность редактирования номера года с Backspace в форме заявки на покупку в кредит")
    void shouldEditWithBackspaceCreditYearNumber() {
        val shopStartPage = new ShopStartPage();
        val creditPage = shopStartPage.chooseCredit();

        assertTrue(creditPage.canEditWithBackspaceCreditYearNumber(DataGenerator.justDigits(2)), "Невозможно редактирование номера года с помощью Backspace");
    }

    @Test
    @DisplayName("Возможность редактирования номера года с Delete в форме заявки на покупку в кредит")
    void shouldEditWithDeleteCreditYearNumber() {
        val shopStartPage = new ShopStartPage();
        val creditPage = shopStartPage.chooseCredit();

        assertTrue(creditPage.canEditWithDeleteCreditYearNumber(DataGenerator.justDigits(2)), "Невозможно редактирование номера года с помощью Delete");
    }

    @Test
    @DisplayName("Контроль ввода истекшего срока действия карты в текущем году в форме заявки на покупку в кредит")
    void shouldAlertIfExpiredDateInCurrentYearCredit(){
        val shopStartPage = new ShopStartPage();
        val creditPage = shopStartPage.chooseCredit();
        val sendingStatus = creditPage.requestCredit(DataGenerator.generateValidApprovedCardInfo().getNumber(),
                DataGenerator.generateMonthNumberWithShift(-1),
                DataGenerator.generateYearNumberWithShift(0),
                DataGenerator.generateCardOwnerName(),
                DataGenerator.justDigits(3));

        assertEquals(creditPage.sendingError, sendingStatus);
        assertTrue(creditPage.isCreditFormExpiredMonthNumberAlert(), "Допустим ввод истекшего срока действия карты");
    }

    @Test
    @DisplayName("Ошибка при отсутствии имени владельца карты в форме заявки на покупку в кредит")
    void shouldAlertIfEmptyCreditCardOwner(){
        val shopStartPage = new ShopStartPage();
        val creditPage = shopStartPage.chooseCredit();
        val sendingStatus = creditPage.requestCredit(DataGenerator.generateValidApprovedCardInfo().getNumber(),
                DataGenerator.generateMonthNumberWithShift(0),
                DataGenerator.generateYearNumberWithShift(0),
                "",
                DataGenerator.justDigits(3));

        assertEquals(creditPage.sendingError, sendingStatus);
        assertTrue(creditPage.isCreditFormEmptyCardOwnerAlert(), "Нет собщения о необходимости заполнения поля Владелец");
    }

    @Test
    @DisplayName("Ошибка при однобуквенном имени владельца карты в форме заявки на покупку в кредит")
    void shouldAlertIfShortCreditCardOwner(){
        val shopStartPage = new ShopStartPage();
        val creditPage = shopStartPage.chooseCredit();
        val sendingStatus = creditPage.requestCredit(DataGenerator.generateValidApprovedCardInfo().getNumber(),
                DataGenerator.generateMonthNumberWithShift(0),
                DataGenerator.generateYearNumberWithShift(0),
                DataGenerator.justLetters(1),
                DataGenerator.justDigits(3));

        assertEquals(creditPage.sendingError, sendingStatus);
        assertTrue(creditPage.isCreditFormShortCardOwnerAlert(), "Допустим ввод имени владельца, состоящего из одной буквы");
    }

    @Test
    @DisplayName("Контроль ввода цифр в имени владельца карты в форме заявки на покупку в кредит")
    void shouldNotInputDigitsInCreditCardOwner(){
        val shopStartPage = new ShopStartPage();
        val creditPage = shopStartPage.chooseCredit();

        assertTrue(creditPage.canNotInputDigitsInCreditCardOwner(DataGenerator.justDigits(1)), "Допускается ввод цифр в имени владельца карты");
    }

    @Test
    @DisplayName("Контроль ввода специальных символов в имени владельца карты в форме заявки на покупку в кредит")
    void shouldNotInputSpecialSymbolsInCreditCardOwner(){
        val shopStartPage = new ShopStartPage();
        val creditPage = shopStartPage.chooseCredit();

        assertTrue(creditPage.canNotInputSpecialSymbolsInCreditCardOwner(DataGenerator.justOneSpecialSymbol()), "Допускается ввод спецсимволов в имени владельца карты");
    }

    @Test
    @DisplayName("Контроль ввода SQL injection в поле Владелец в форме заявки на покупку в кредит")
    void shouldAlertIfSQLInjectionCreditCardOwner(){
        val shopStartPage = new ShopStartPage();
        val creditPage = shopStartPage.chooseCredit();
        val sendingStatus = creditPage.requestCredit(DataGenerator.generateValidApprovedCardInfo().getNumber(),
                DataGenerator.generateMonthNumberWithShift(0),
                DataGenerator.generateYearNumberWithShift(0),
                "or 1 = 1; -",
                DataGenerator.justDigits(3));

        assertEquals(creditPage.sendingError, sendingStatus);
        assertTrue(creditPage.isCreditFormSQLInjectionCardOwnerAlert(), "Допускается ввод последовательности SQL Injection в имени владельца карты");
    }

    @Test
    @DisplayName("Возможность редактирования имени владельца с Backspace в форме заявки на покупку в кредит")
    void shouldEditWithBackspaceCreditCardOwner() {
        val shopStartPage = new ShopStartPage();
        val creditPage = shopStartPage.chooseCredit();

        assertTrue(creditPage.canEditWithBackspaceCreditCardOwner(DataGenerator.justLetters(2)), "Невозможно редактирование имени владельца с помощью Backspace");
    }

    @Test
    @DisplayName("Возможность редактирования имени владельца с Delete в форме заявки на покупку в кредит")
    void shouldEditWithDeleteCreditCardOwner() {
        val shopStartPage = new ShopStartPage();
        val creditPage = shopStartPage.chooseCredit();

        assertTrue(creditPage.canEditWithDeleteCreditCardOwner(DataGenerator.justLetters(2)), "Невозможно редактирование имени владельца с помощью Delete");
    }

    @Test
    @DisplayName("Ошибка при отсутствии кода CVC/CVV в форме заявки на покупку в кредит")
    void shouldAlertIfEmptyCreditCVC_CVVCode(){
        val shopStartPage = new ShopStartPage();
        val creditPage = shopStartPage.chooseCredit();
        val sendingStatus = creditPage.requestCredit(DataGenerator.generateValidApprovedCardInfo().getNumber(),
                DataGenerator.generateMonthNumberWithShift(0),
                DataGenerator.generateYearNumberWithShift(0),
                DataGenerator.generateCardOwnerName(),
                "");

        assertEquals(creditPage.sendingError, sendingStatus);
        assertTrue(creditPage.isCreditFormEmptyCVC_CVVCodeAlert(), "Нет собщения о необходимости заполнения поля CVC/CVV");
    }

    @Test
    @DisplayName("Ошибка при неполном коде CVC/CVV в форме заявки на оплату по карт")
    void shouldAlertIfShortCreditCVC_CVVCode(){
        val shopStartPage = new ShopStartPage();
        val creditPage = shopStartPage.chooseCredit();
        val sendingStatus = creditPage.requestCredit(DataGenerator.generateValidApprovedCardInfo().getNumber(),
                DataGenerator.generateMonthNumberWithShift(0),
                DataGenerator.generateYearNumberWithShift(0),
                DataGenerator.generateCardOwnerName(),
                DataGenerator.justDigits(1));

        assertEquals(creditPage.sendingError, sendingStatus);
        assertTrue(creditPage.isCreditFormShortCVC_CVVCodeAlert(), "Нет собщения о неполном коде CVC/CVV");
    }

    @Test
    @DisplayName("Контроль ввода слишком длинного кода CVC/CVV в форме заявки на покупку в кредит")
    void shouldNotEnterLongCreditCVC_CVVCode(){
        val shopStartPage = new ShopStartPage();
        val creditPage = shopStartPage.chooseCredit();

        assertTrue(creditPage.canNotInputLongCreditCVC_CVVCode(DataGenerator.justDigits(4)), "Допустим ввод слишком длинного кода CVC/CVV");
    }

    @Test
    @DisplayName("Контроль ввода букв в коде CVC/CVV в форме заявки на покупку в кредит")
    void shouldNotInputLettersInCreditCVC_CVVCode(){
        val shopStartPage = new ShopStartPage();
        val creditPage = shopStartPage.chooseCredit();

        assertTrue(creditPage.canNotInputLettersInCreditCVC_CVVCode(DataGenerator.justLetters(1)));
    }

    @Test
    @DisplayName("Контроль ввода специальных символов в коде CVC/CVV в форме заявки на покупку в кредит")
    void shouldNotInputSpecialSymbolsInCreditCVC_CVVCode(){
        val shopStartPage = new ShopStartPage();
        val creditPage = shopStartPage.chooseCredit();

        assertTrue(creditPage.canNotInputSpecialSymbolsInCreditCVC_CVVCode(DataGenerator.justOneSpecialSymbol()), "Допускается ввод спецсимволов в коде CVC/CVV");
    }

    @Test
    @DisplayName("Возможность редактирования кода CVC/CVV с Backspace в форме заявки на покупку в кредит")
    void shouldEditWithBackspaceCreditCVC_CVVCode() {
        val shopStartPage = new ShopStartPage();
        val creditPage = shopStartPage.chooseCredit();

        assertTrue(creditPage.canEditWithBackspaceCreditCVC_CVVCode(DataGenerator.justDigits(2)), "Невозможно редактирование кода CVC/CVV с помощью Backspace");
    }

    @Test
    @DisplayName("Возможность редактирования кода CVC/CVV с Delete в форме заявки на покупку в кредит")
    void shouldEditWithDeleteCreditCVC_CVVCode() {
        val shopStartPage = new ShopStartPage();
        val creditPage = shopStartPage.chooseCredit();

        assertTrue(creditPage.canEditWithDeleteCreditCVC_CVVCode(DataGenerator.justDigits(2)), "Невозможно редактирование кода CVC/CVV с помощью Delete");
    }
}
