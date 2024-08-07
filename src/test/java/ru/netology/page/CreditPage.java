package ru.netology.page;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.data.CardInfo;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class CreditPage {
    private final SelenideElement cardNumberInput = $("input[placeholder='0000 0000 0000 0000']");
    private final SelenideElement monthInput = $("input[placeholder='08']");
    private final SelenideElement yearInput = $("input[placeholder='22']");
    private final SelenideElement cvcInput = $("input[placeholder='999']");
    private final SelenideElement ownerInput = $$(".input__control").get(3);
    private final SelenideElement continueButton = $$(".button").find(exactText("Продолжить"));

    public CreditPage() {
        SelenideElement headingPayment = $$("h3.heading").find(exactText("Кредит по данным карты"));
        headingPayment.shouldBe(visible);
    }

    public void getCardFieldsFilled(CardInfo cardInfo) {
        cardNumberInput.setValue(cardInfo.getCardNumber());
        monthInput.setValue(cardInfo.getMonth());
        yearInput.setValue(cardInfo.getYear());
        ownerInput.setValue(cardInfo.getCardHolder());
        cvcInput.setValue(cardInfo.getCvc());
        continueButton.click();
    }

    public void successfulPaymentCreditCard() {
        $(".notification_status_ok")
                .shouldHave(text("Успешно Операция одобрена Банком."), Duration.ofSeconds(15)).shouldBe(visible);
    }

    public void invalidPaymentCreditCard() {
        $(".notification_status_error .notification__content")
                .shouldHave(text("Ошибка! Банк отказал в проведении операции."), Duration.ofSeconds(20)).shouldBe(visible);
    }

    public void checkInvalidFormat() {
        $(".input__sub").shouldBe(visible).shouldHave(text("Неверный формат"));
    }

    public void checkInvalidCardValidityPeriod() {
        $(".input__sub").shouldBe(visible)
                .shouldHave(text("Неверно указан срок действия карты"));
    }

    public void checkCardExpired() {
        $(".input__sub").shouldBe(visible)
                .shouldHave(text("Истёк срок действия карты"));
    }

    public void checkEmptyField() {
        $(".input__sub").shouldBe(visible)
                .shouldHave(text("Поле обязательно для заполнения"));
    }

    public void checkAllFieldsAreRequired() {
        $$(".input__sub").shouldHave(CollectionCondition.size(5));
    }
}
