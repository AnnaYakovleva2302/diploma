package ru.netology.tour.pageObject;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.SelenideElement;

import io.qameta.allure.Description;
import ru.netology.tour.data.CardInfo;

public class CardFormPageObject {
    private SelenideElement form;

    public CardFormPageObject() {
        form = $("form");
    }

    @Description("Fill the card form and submit it")
    public void fillAndSubmit(CardInfo card) {
        this.enterCard(card.getNumber());
        this.enterMonth(card.getMonth());
        this.enterYear(card.getYear());
        this.enterOwner(card.getOwner());
        this.enterCVV(card.getCvv());
        this.submitForm();
    }

    public String getCardValidation() {
        return form.$(byText("Номер карты")).parent().$(".input__sub").text();
    }

    public String getMonthValidation() {
        return form.$(byText("Месяц")).parent().$(".input__sub").text();
    }

    public String getYearValidation() {
        return form.$(byText("Год")).parent().$(".input__sub").text();
    }

    public String getOwnerValidation() {
        return form.$(byText("Владелец")).parent().$(".input__sub").text();
    }

    public String getCVVValidation() {
        return form.$(byText("CVC/CVV")).parent().$(".input__sub").text();
    }

    private void enterCard(String value) {
        form.$(byText("Номер карты")).parent().$("input").setValue(value);
    }

    private void enterMonth(String value) {
        form.$(byText("Месяц")).parent().$("input").setValue(value);
    }

    private void enterYear(String value) {
        form.$(byText("Год")).parent().$("input").setValue(value);
    }

    private void enterOwner(String value) {
        form.$(byText("Владелец")).parent().$("input").setValue(value);
    }

    private void enterCVV(String value) {
        form.$(byText("CVC/CVV")).parent().$("input").setValue(value);
    }

    private void submitForm() {
        form.$(byText("Продолжить")).click();
    }
}
