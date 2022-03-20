package ru.netology.tour.pageObject;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.SelenideElement;

public class MainPageObject {
    private SelenideElement root;

    public MainPageObject() {
        root = $("#root");
    }

    public void clickRegularBuy() {
        root.$(byText("Купить")).click();
    }

    public void clickBuyInCredit() {
        root.$(byText("Купить в кредит")).click();
    }

    public int getTourPrice() {
        return Integer.parseInt(root.$(withText("Всего")).text().replaceAll("\\D+", ""));
    }

    public SelenideElement getTitle() {
        return root.$$("h3").get(1);
    }
}
