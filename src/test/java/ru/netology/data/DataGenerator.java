package ru.netology.data;

import com.github.javafaker.Faker;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import lombok.Value;
import lombok.val;
import ru.netology.api.BankServices;

public class DataGenerator {

    private DataGenerator(){
    }

// Буквы и цифры

    public static String justLetters(int quantity){
        return new Faker().name().firstName().substring(0, quantity);
    }

    public static String justDigits(int quantity){
        Random rnd = new Random();

        return new BigInteger(32, rnd).toString().substring(1, quantity+1);
    }

    public static String justOneSpecialSymbol(){
        String symbols = "!@#$%^&*()_+|}{><~:;'";
        Random rnd = new Random();
        int symbolNumber = new Random().nextInt(symbols.length());

        return symbols.substring(symbolNumber, symbolNumber+1);
    }

// Данные для поля "Номер карты"

    public static CardInfo generateValidApprovedCardInfo(){
        val approvedCardNumber = "4444 4444 4444 4441";
        val cardStatus = "APPROVED";

        return new DataGenerator.CardInfo(approvedCardNumber, cardStatus);
    }

    public static CardInfo generateValidDeclinedCardInfo(){
        val bankServices = new BankServices();
        val declinedCardNumber = "4444 4444 4444 4442";
        val cardStatus = "DECLINED";

        return new DataGenerator.CardInfo(declinedCardNumber, cardStatus);
    }

    public static CardInfo generateInValidCardInfo(){
        DataGenerator.CardInfo cardInfo = new DataGenerator.CardInfo((justDigits(4)+ " " +
                justDigits(4) + " " + justDigits(4) + " " + justDigits(4)).substring(0, 18) + "0", "");

        return cardInfo;
    }

    public static CardInfo generateEmptyCardInfo(){
        DataGenerator.CardInfo cardInfo = new DataGenerator.CardInfo("", "");

        return cardInfo;
    }

    @Value
    public static class CardInfo{
        String number;
        String status;
    }

// Данные для поля "Месяц"

    public static String generateMonthNumberWithShift(int shift){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM");

        return LocalDate.now().plusMonths(shift).format(formatter);
    }

// Данные для поля "Год"

    public static String generateYearNumberWithShift(int shift){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy");

        return LocalDate.now().plusYears(shift).format(formatter);
    }

// Данные для поля "Владелец"

    public static String generateCardOwnerName(){
        return new Faker().name().fullName();
    }
}
