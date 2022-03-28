package ru.netology.tour.pageObject;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.SelenideElement;

import io.qameta.allure.Description;
import ru.netology.tour.data.CardInfo;

public class MainPageObject {
    private SelenideElement root;

    public MainPageObject() {
        root = $("#root");
    }

    @Description("Launch debit or credit card form")
    public void launchFormBasedOnCard(CardInfo card) {
        if (card.isCredit()) {
            this.clickBuyInCredit();
        }
        else {
            this.clickRegularBuy();
        }
    }

    public int getTourPrice() {
        return Integer.parseInt(root.$(withText("Всего")).text().replaceAll("\\D+", "")) * 100;
    }

    public String getTitle() {
        return root.$$("h3").get(1).text();
    }    

    private void clickRegularBuy() {
        root.$(byText("Купить")).click();
    }

    private void clickBuyInCredit() {
        root.$(byText("Купить в кредит")).click();
    }
}
