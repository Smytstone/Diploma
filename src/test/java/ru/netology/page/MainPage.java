package ru.netology.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class MainPage {
    private final SelenideElement paymentButton = $$(".button").find(exactText("Купить"));
    private final SelenideElement creditPay = $$(".button").find(exactText("Купить в кредит"));

    public MainPage() {
        SelenideElement headingMain = $("h2.heading");
        headingMain.should(visible);
    }

    public PaymentPage paymentPage() {
        paymentButton.click();
        return new PaymentPage();
    }

    public CreditPage creditPage() {
        creditPay.click();
        return new CreditPage();
    }
}
