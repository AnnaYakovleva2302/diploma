package ru.netology.tour.pageObject;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.SelenideElement;

public class CardFormPageObject {
    private SelenideElement form;

    public CardFormPageObject() {
        form = $("form");
    }

    public void clickBuy() {
        $(byText("Купить")).click();
    }

    public void enterCard(String value) {
        form.$(byText("Номер карты")).parent().$("input").setValue(value);
    }

    public void enterMonth(String value) {
        form.$(byText("Месяц")).parent().$("input").setValue(value);
    }

    public void enterYear(String value) {
        form.$(byText("Год")).parent().$("input").setValue(value);
    }

    public void enterOwner(String value) {
        form.$(byText("Владелец")).parent().$("input").setValue(value);
    }

    public void enterCVV(String value) {
        form.$(byText("CVC/CVV")).parent().$("input").setValue(value);
    }

    public SelenideElement getCardValidation() {
        return form.$(byText("Номер карты")).parent().$(".input__sub");
    }

    public SelenideElement getMonthValidation() {
        return form.$(byText("Месяц")).parent().$(".input__sub");
    }

    public SelenideElement getYearValidation() {
        return form.$(byText("Год")).parent().$(".input__sub");
    }

    public SelenideElement getOwnerValidation() {
        return form.$(byText("Владелец")).parent().$(".input__sub");
    }

    public SelenideElement getCVVValidation() {
        return form.$(byText("CVC/CVV")).parent().$(".input__sub");
    }

    public void submitForm() {
        form.$(byText("Продолжить")).click();
    }
}
