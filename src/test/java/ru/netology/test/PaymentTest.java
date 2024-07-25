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

public class PaymentTest {

    @BeforeEach
    void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide().screenshots(true).savePageSource(false));
        open("http://localhost:8080");
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

//    @AfterEach
//    void tearDown() {
//        DataBase.clearTables();
//    }

    @Test
    void shouldPayWithApprovedDebitCard() {
        var mainPage = new MainPage();
        CardInfo card = new CardInfo(
                getFirstCard(), getValidMonth(1), plusYears(2), getValidHolderName(), getValidCVC());
        var paymentPage = mainPage.paymentPage();
        paymentPage.getFillCardDetails(card);
        paymentPage.successfulPaymentDebitCard();
        String actual = DataBase.getStatusPayment();
        assertEquals("APPROVED", actual);
    }


    @Test
    void shouldIgnorePaymentWithDeclinedDebitCard() {
        var mainPage = new MainPage();
        CardInfo card = new CardInfo(
                getSecondCard(), getCurrentMonth(), plusYears(5), getValidHolderName(), getValidCVC());
        var paymentPage = mainPage.paymentPage();
        paymentPage.getFillCardDetails(card);
        String actual = DataBase.getStatusPayment();
        paymentPage.invalidPaymentDebitCard();
        assertEquals("DECLINED", actual);
    }

    @Test
    void shouldIgnorePaymentWithDeclinedDebitCardExpired() {
        var mainPage = new MainPage();
        CardInfo card = new CardInfo(
                getSecondCard(), getValidMonth(1), getZeroYear(), getValidHolderName(), getValidCVC());
        var paymentPage = mainPage.paymentPage();
        paymentPage.getFillCardDetails(card);
        String actual = DataBase.getStatusPayment();
        paymentPage.checkCardExpired();
        assertEquals("DECLINED", actual);
    }

    @Test
    void shouldNotPayWithInvalidDebitCardNumber() {
        var mainPage = new MainPage();
        CardInfo card = new CardInfo(
                invalidCard(15), getValidMonth(2), plusYears(3), getValidHolderName(), getValidCVC());
        var paymentPage = mainPage.paymentPage();
        paymentPage.getFillCardDetails(card);
        paymentPage.invalidPaymentDebitCard();
    }

    @Test
    void shouldNotPayWithInvalidDebitCardWithFewNumbers() {
        var mainPage = new MainPage();
        CardInfo card = new CardInfo(
                invalidCard(7), getValidMonth(5), plusYears(3), getValidHolderName(), getValidCVC());
        var paymentPage = mainPage.paymentPage();
        paymentPage.getFillCardDetails(card);
        paymentPage.checkInvalidFormat();
    }

    @Test
    void shouldCheckInvalidDebitCardOneNumber() {
        var mainPage = new MainPage();
        CardInfo card = new CardInfo(
                invalidCard(1), getValidMonth(3), plusYears(1), getValidHolderName(), getValidCVC());
        var paymentPage = mainPage.paymentPage();
        paymentPage.getFillCardDetails(card);
        paymentPage.checkInvalidFormat();
    }

    @Test
    void shouldCheckPaymentWithExpiredDebitCard() {
        var mainPage = new MainPage();
        CardInfo card = new CardInfo(
                getFirstCard(), getValidMonth(10), plusYears(-1), getValidHolderName(), getValidCVC());
        var paymentPage = mainPage.paymentPage();
        paymentPage.getFillCardDetails(card);
        paymentPage.checkCardExpired();
    }

    @Test
    void shouldCheckPaymentIncorrectDebitCardExpired() {
        var mainPage = new MainPage();
        CardInfo card = new CardInfo(
                getFirstCard(), getValidMonth(-1), getCurrentYear(), getValidHolderName(), getValidCVC());
        var paymentPage = mainPage.paymentPage();
        paymentPage.getFillCardDetails(card);
        paymentPage.checkInvalidCardValidityPeriod();
    }


    @Test
    void shouldNotPayDebitCardValidMoreThanFiveYears() {
        var mainPage = new MainPage();
        CardInfo card = new CardInfo(
                getFirstCard(), getCurrentMonth(), plusYears(6), getValidHolderName(), getValidCVC());
        var paymentPage = mainPage.paymentPage();
        paymentPage.getFillCardDetails(card);
        paymentPage.checkInvalidCardValidityPeriod();
    }

    @Test
    void shouldCheckDebitCardInvalidYearOneDigit() {
        var mainPage = new MainPage();
        CardInfo card = new CardInfo(
                getFirstCard(), getValidMonth(2), oneDigitYear(), getValidHolderName(), getValidCVC());
        var paymentPage = mainPage.paymentPage();
        paymentPage.getFillCardDetails(card);
        paymentPage.checkInvalidFormat();

    }

    @Test
    void shouldCheckInvalidName() {
        var mainPage = new MainPage();
        CardInfo card = new CardInfo(
                getFirstCard(), getValidMonth(1), plusYears(3), getInvalidHolderName(), getValidCVC());
        var paymentPage = mainPage.paymentPage();
        paymentPage.getFillCardDetails(card);
        paymentPage.checkInvalidFormat();
    }

    @Test
    void shouldCheckInvalidLongName() {
        var mainPage = new MainPage();
        CardInfo card = new CardInfo(
                getFirstCard(), getValidMonth(1), plusYears(3), getLongName(), getValidCVC());
        var paymentPage = mainPage.paymentPage();
        paymentPage.getFillCardDetails(card);
        paymentPage.checkInvalidFormat();
    }

    @Test
    void shouldNotPayInvalidOwnerCardWithNumbers() {
        var mainPage = new MainPage();
        CardInfo card = new CardInfo(
                getFirstCard(), getValidMonth(4), plusYears(3),
                getHolderNamePlusDigits(), getValidCVC());
        var paymentPage = mainPage.paymentPage();
        paymentPage.getFillCardDetails(card);
        paymentPage.invalidPaymentDebitCard();
    }

    @Test
    void shouldNotPayInvalidOwnerCardOneLetterName() {
        var mainPage = new MainPage();
        CardInfo card = new CardInfo(
                getFirstCard(), getValidMonth(9), plusYears(3),
                getOneLetterName(), getValidCVC());
        var paymentPage = mainPage.paymentPage();
        paymentPage.getFillCardDetails(card);
        paymentPage.invalidPaymentDebitCard();
    }

    @Test
    void shouldCheckCardInvalidMonthInvalidPeriod() {
        var mainPage = new MainPage();
        CardInfo card = new CardInfo(
                getFirstCard(), getInvalidMonth(), plusYears(2), getValidHolderName(), getValidCVC());
        var paymentPage = mainPage.paymentPage();
        paymentPage.getFillCardDetails(card);
        paymentPage.checkInvalidCardValidityPeriod();
    }

    @Test
    void shouldNotPayCardInvalidZeroMonth() {
        var mainPage = new MainPage();
        CardInfo card = new CardInfo(
                getFirstCard(), getZeroMonth(), plusYears(2), getValidHolderName(), getValidCVC());
        var paymentPage = mainPage.paymentPage();
        paymentPage.getFillCardDetails(card);
        paymentPage.checkInvalidFormat();
    }

    @Test
    void shouldNotPayEmptyFieldNumberCard() {
        var mainPage = new MainPage();
        CardInfo card = new CardInfo(
                null, getValidMonth(1), plusYears(2), getValidHolderName(), getValidCVC());
        var paymentPage = mainPage.paymentPage();
        paymentPage.getFillCardDetails(card);
        paymentPage.checkInvalidFormat();
    }

    @Test
    void shouldNotPayEmptyFieldMonth() {
        var mainPage = new MainPage();
        CardInfo card = new CardInfo(
                getFirstCard(), null, plusYears(2), getValidHolderName(), getValidCVC());
        var paymentPage = mainPage.paymentPage();
        paymentPage.getFillCardDetails(card);
        paymentPage.checkInvalidFormat();
    }

    @Test
    void shouldNotPayEmptyFieldYears() {
        var mainPage = new MainPage();
        CardInfo card = new CardInfo(
                getFirstCard(), getValidMonth(2), null, getValidHolderName(), getValidCVC());
        var paymentPage = mainPage.paymentPage();
        paymentPage.getFillCardDetails(card);
        paymentPage.checkInvalidFormat();
    }

    @Test
    void shouldNotPayEmptyFieldOwner() {
        var mainPage = new MainPage();
        CardInfo card = new CardInfo(
                getFirstCard(), getValidMonth(2), plusYears(3), null, getValidCVC());
        var paymentPage = mainPage.paymentPage();
        paymentPage.getFillCardDetails(card);
        paymentPage.checkEmptyField();
    }

    @Test
    void shouldNotPayEmptyFieldCvc() {
        var mainPage = new MainPage();
        CardInfo card = new CardInfo(
                getFirstCard(), getValidMonth(2), plusYears(3), getValidHolderName(), null);
        var paymentPage = mainPage.paymentPage();
        paymentPage.getFillCardDetails(card);
        paymentPage.checkEmptyField();
    }

    @Test
    void shouldCheckInvalidCVCFormat() {
        var mainPage = new MainPage();
        CardInfo card = new CardInfo(
                getFirstCard(), getValidMonth(2), plusYears(3), getValidHolderName(), getInvalidCVC());
        var paymentPage = mainPage.paymentPage();
        paymentPage.getFillCardDetails(card);
        paymentPage.checkInvalidFormat();
    }

    @Test
    void shouldNotPayEmptyAllFields() {
        var mainPage = new MainPage();
        CardInfo card = new CardInfo(
                null, null, null, null, null);
        var paymentPage = mainPage.paymentPage();
        paymentPage.getFillCardDetails(card);
        paymentPage.checkAllFieldsAreRequired();
    }
}
