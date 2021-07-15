package ru.netology.tests;

import lombok.val;
import org.junit.jupiter.api.*;
import ru.netology.api.BankServices;
import ru.netology.data.DataGenerator;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BankApiTest {

    @Test
    @DisplayName("Симулятор банковских сервисов определяет одобренную (по спецификации) карту")
    void shouldRecognizeApprovedCard() {
        val bankServices = new BankServices();

        assertTrue(bankServices.checkCardNumber("/payment", DataGenerator.generateValidApprovedCardInfo()), "API банка не распознаёт одобренную (по спецификации) карту");
    }

    @Test
    @DisplayName("Симулятор банковских сервисов определяет неодобренную (по спецификации) карту")
    void shouldRecognizeDeclinedCard() {
        val bankServices = new BankServices();

        assertTrue(bankServices.checkCardNumber("/payment", DataGenerator.generateValidDeclinedCardInfo()), "API банка не распознаёт неодобренную (по спецификации) карту");
    }
}
