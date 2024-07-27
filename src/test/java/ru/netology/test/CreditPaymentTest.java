package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.CardInfo;
import ru.netology.data.DataBase;
import ru.netology.page.MainPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DataHelper.*;

public class CreditPaymentTest {


    @BeforeEach
    void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide().screenshots(true).savePageSource(false));
    }

    @BeforeEach
    public void openPage() {
        open("http://localhost:8080");
    }

    @AfterEach
    void tearDown() {
        DataBase.clearTables();
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    void shouldPayWithApprovedCreditCard() {
        var mainPage = new MainPage();
        CardInfo card = new CardInfo(
                getFirstCard(), getValidMonth(1), plusYears(4), getValidHolderName(), getValidCVC());
        var creditPage = mainPage.creditPage();
        creditPage.getCardFieldsFilled(card);
        creditPage.successfulPaymentCreditCard();
        String actual = DataBase.getStatusCredit();
        assertEquals("APPROVED", actual);
    }

    @Test
    void shouldIgnorePaymentWithDeclinedCreditCard() {
        var mainPage = new MainPage();
        CardInfo card = new CardInfo(
                getSecondCard(), getValidMonth(1), plusYears(4), getValidHolderName(), getValidCVC());
        var creditPage = mainPage.creditPage();
        creditPage.getCardFieldsFilled(card);
        String actual = DataBase.getStatusCredit();
        creditPage.invalidPaymentCreditCard();
        assertEquals("DECLINED", actual);
    }

    @Test
    void shouldIgnorePaymentWithDeclinedCreditCardExpired() {
        var mainPage = new MainPage();
        CardInfo card = new CardInfo(
                getSecondCard(), getCurrentMonth(), getCurrentYear(), getValidHolderName(), getValidCVC());
        var creditPage = mainPage.creditPage();
        creditPage.getCardFieldsFilled(card);
        String actual = DataBase.getStatusCredit();
        creditPage.invalidPaymentCreditCard();
        assertEquals("DECLINED", actual);
    }

    @Test
    void shouldNotPayWithInvalidCreditCardNumber() {
        var mainPage = new MainPage();
        CardInfo card = new CardInfo(
                invalidCard(14), getValidMonth(2), plusYears(1), getValidHolderName(), getValidCVC());
        var creditPage = mainPage.creditPage();
        creditPage.getCardFieldsFilled(card);
        creditPage.checkInvalidFormat();
    }

    @Test
    void shouldNotPayWithInvalidCreditCardWithFewNumbers() {
        var mainPage = new MainPage();
        CardInfo card = new CardInfo(
                invalidCard(5), getValidMonth(5), plusYears(3), getValidHolderName(), getValidCVC());
        var creditPage = mainPage.creditPage();
        creditPage.getCardFieldsFilled(card);
        creditPage.checkInvalidFormat();
    }

    @Test
    void shouldCheckInvalidCreditCardOneNumber() {
        var mainPage = new MainPage();
        CardInfo card = new CardInfo(
                invalidCard(1), getValidMonth(3), plusYears(1), getValidHolderName(), getValidCVC());
        var creditPage = mainPage.creditPage();
        creditPage.getCardFieldsFilled(card);
        creditPage.checkInvalidFormat();
    }

    @Test
    void shouldCheckPaymentWithExpiredCreditCard() {
        var mainPage = new MainPage();
        CardInfo card = new CardInfo(
                getFirstCard(), getValidMonth(-1), plusYears(-1), getValidHolderName(), getValidCVC());
        var creditPage = mainPage.creditPage();
        creditPage.getCardFieldsFilled(card);
        creditPage.checkCardExpired();
    }

    @Test
    void shouldCheckPaymentIncorrectCreditCardExpired() {
        var mainPage = new MainPage();
        CardInfo card = new CardInfo(
                getFirstCard(), getValidMonth(-1), getCurrentYear(), getValidHolderName(), getValidCVC());
        var creditPage = mainPage.creditPage();
        creditPage.getCardFieldsFilled(card);
        creditPage.checkInvalidCardValidityPeriod();
    }

    @Test
    void shouldCheckCreditCardInvalidYearOneDigit() {
        var mainPage = new MainPage();
        CardInfo card = new CardInfo(
                getFirstCard(), getValidMonth(2), oneDigitYear(), getValidHolderName(), getValidCVC());
        var creditPage = mainPage.creditPage();
        creditPage.getCardFieldsFilled(card);
        creditPage.checkInvalidFormat();
    }

    @Test
    void shouldCheckInvalidNameCredit() {
        var mainPage = new MainPage();
        CardInfo card = new CardInfo(
                getFirstCard(), getValidMonth(1), plusYears(3), getInvalidHolderName(), getValidCVC());
        var creditPage = mainPage.creditPage();
        creditPage.getCardFieldsFilled(card);
        creditPage.checkInvalidFormat();
    }

    @Test
    void shouldCheckCreditCardInvalidMonthInvalidPeriod() {
        var mainPage = new MainPage();
        CardInfo card = new CardInfo(
                getFirstCard(), getInvalidMonth(), plusYears(2), getValidHolderName(), getValidCVC());
        var creditPage = mainPage.creditPage();
        creditPage.getCardFieldsFilled(card);
        creditPage.checkInvalidCardValidityPeriod();
    }

    @Test
    void shouldNotPayEmptyFieldCvcCredit() {
        var mainPage = new MainPage();
        CardInfo card = new CardInfo(
                getFirstCard(), getValidMonth(2), plusYears(3), getValidHolderName(), null);
        var creditPage = mainPage.creditPage();
        creditPage.getCardFieldsFilled(card);
        creditPage.checkEmptyField();
    }

    @Test
    void shouldCheckInvalidCvcCredit() {
        var mainPage = new MainPage();
        CardInfo card = new CardInfo(
                getFirstCard(), getValidMonth(2), plusYears(3), getValidHolderName(), getInvalidCVC());
        var creditPage = mainPage.creditPage();
        creditPage.getCardFieldsFilled(card);
        creditPage.checkInvalidFormat();
    }

    @Test
    void shouldNotPayEmptyAllFieldCredit() {
        var mainPage = new MainPage();
        CardInfo card = new CardInfo(
                null, null, null, null, null);
        var creditPage = mainPage.creditPage();
        creditPage.getCardFieldsFilled(card);
        creditPage.checkAllFieldsAreRequired();
    }
}
