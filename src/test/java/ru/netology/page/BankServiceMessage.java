package ru.netology.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class BankServiceMessage {

    private static SelenideElement approveBankService = $(".notification_status_ok .notification__title");
    private static SelenideElement declineBankService = $(".notification_status_error .notification__title");

    // Показано ли сообщение об успехе операции
    public static boolean isApproveMessage(){
        return approveBankService.isDisplayed();
    }

    // Показано ли сообщение об ошибке операции
    public static boolean isDeclineMessage(){
        return declineBankService.isDisplayed();
    }
}
