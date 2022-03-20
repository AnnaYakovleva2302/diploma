package ru.netology.tour.test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;

import com.codeborne.selenide.logevents.SelenideLogger;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.qameta.allure.selenide.AllureSelenide;
import lombok.SneakyThrows;
import ru.netology.tour.data.CardDataGenerator;
import ru.netology.tour.data.CardInfo;
import ru.netology.tour.data.DataBaseManager;
import ru.netology.tour.pageObject.CardFormPageObject;
import ru.netology.tour.pageObject.MainPageObject;
import ru.netology.tour.pageObject.NotificationsPageObject;

public class BuyTourTest {
    MainPageObject page;
    CardFormPageObject form;
    NotificationsPageObject notifications;
    DataBaseManager db;
    String successText;
    String errorText;
    String formPayByCardText;
    String formPayByCreditText;
    String validationEmptyText;
    String validationFormatText;
    String validationExpiredYearText;
    String validationExpiredMonthText;
    int processingTime;
    
    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    @SneakyThrows
    void openBrowser() {
        open("http://localhost:8080");
        page = new MainPageObject();
        form = new CardFormPageObject();
        notifications = new NotificationsPageObject();
        db = new DataBaseManager();
    }

    public BuyTourTest() {
        processingTime = 15;
        successText = "Успешно!";
        errorText = "Необходимо подтверждение";
        formPayByCardText = "Оплата по карте";
        formPayByCreditText = "Кредит по данным карты";
        validationEmptyText = "Поле обязательно для заполнения";
        validationFormatText = "Неверный формат";
        validationExpiredYearText = "Истёк срок действия карты";
        validationExpiredMonthText = "Неверно указан срок действия карты";
    }

    public void fillAndSubmitTheForm(CardFormPageObject form, CardInfo card) {
        // fill the form of card
        form.enterCard(card.getNumber());
        form.enterMonth(card.getMonth());
        form.enterYear(card.getYear());
        form.enterOwner(card.getOwner());
        form.enterCVV(card.getCvv());
        // submit it
        form.submitForm();
    }

    public void launchAndCheckFormBasedOnCard(CardInfo card) {
        String titleText;
        if (card.isCredit()) {
            // launch form with credit card
            page.clickBuyInCredit();
            titleText = formPayByCreditText;
        }
        else {
            // launch form with debit card
            page.clickRegularBuy();
            titleText = formPayByCardText;
        }
        // check if form switched to regular payment mode
        page.getTitle().shouldHave(text(titleText));
    }

    public void expectMatchedTourPrice() {
        // get real tour price from written in form
        int tourPrice = page.getTourPrice();
        // get latest payment amount from db
        int savedTourPrice = db.getLastPaymentAmount();
        // check if saved and announced prices are equal
        assertEquals(tourPrice, savedTourPrice);
    }

    public void expectAcceptedResult(CardInfo card) {
        launchAndCheckFormBasedOnCard(card);
        // fill and submit the form with card data
        this.fillAndSubmitTheForm(form, card);
        // check proper notifications
        notifications.getSuccess().shouldBe(visible, Duration.ofSeconds(processingTime));
        notifications.getError().shouldNotBe(visible);
    }

    public void expectDeclinedResult(CardInfo card) {
        launchAndCheckFormBasedOnCard(card);
        // fill and submit the form with card data
        this.fillAndSubmitTheForm(form, card);
        // check proper notifications
        notifications.getError().shouldBe(visible, Duration.ofSeconds(processingTime));
        notifications.getSuccess().shouldNotBe(visible);
    }

    @Test
    void shouldAcceptWithValidAcceptedDebitCard() {
        CardInfo card = CardDataGenerator.generateAcceptedCard(false);
        // store orders count before test launched
        int before = db.getOrdersCount();
        // launch tests and verify accepted form results
        expectAcceptedResult(card);
        // get orders count after test launched
        int after = db.getOrdersCount();
        // DB check if after count increased in 1
        assertEquals(before + 1, after);
        // DB check if payment id is reflected in orders list
        assertEquals(db.getLastPaymentID(), db.getLastTransactionID());
        // DB check if payment was accepted
        assertEquals("APPROVED", db.getLastPaymentStatus());
        // check if saved and announced prices are equal
        expectMatchedTourPrice();
    }

    @Test
    void shouldDeclineWithValidDeclinedDebitCard() {
        CardInfo card = CardDataGenerator.generateDeclinedCard(false);
        // store orders count before test launched
        int before = db.getOrdersCount();
        // launch tests and verify declined form results
        expectDeclinedResult(card);
        // get orders count after test launched
        int after = db.getOrdersCount();
        // DB check if after count increased in 1
        assertEquals(before + 1, after);
        // DB check if payment id is reflected in orders list
        assertEquals(db.getLastPaymentID(), db.getLastTransactionID());
        // DB check if payment was declined
        assertEquals("DECLINED", db.getLastPaymentStatus());
        // check if saved and announced prices are equal
        expectMatchedTourPrice();
    }

    @Test
    void shouldDeclineWithValidRandomDebitCard() {
        CardInfo card = CardDataGenerator.generateRandomValidCard(false);
        // store orders count before test launched
        int before = db.getOrdersCount();
        // launch tests and verify declined form results
        expectDeclinedResult(card);
        // get orders count after test launched
        int after = db.getOrdersCount();
        // DB check if after count is the same as before
        assertEquals(before, after);
    }

    @Test
    void shouldAcceptWithValidAcceptedCreditCard() {
        CardInfo card = CardDataGenerator.generateAcceptedCard(true);
        // store orders count before test launched
        int before = db.getOrdersCount();
        // launch tests and verify accepted form results
        expectAcceptedResult(card);
        // get orders count after test launched
        int after = db.getOrdersCount();
        // DB check if after count increased in 1
        assertEquals(before + 1, after);
        // DB check if credit id is reflected in orders list
        assertEquals(db.getLastCreditID(), db.getLastTransactionID());
        // DB check if payment was accepted
        assertEquals("APPROVED", db.getLastCreditStatus());
    }

    @Test
    void shouldDeclineWithValidDeclinedCreditCard() {
        CardInfo card = CardDataGenerator.generateDeclinedCard(true);
        // store orders count before test launched
        int before = db.getOrdersCount();
        // launch tests and verify declined form results
        expectDeclinedResult(card);
        // get orders count after test launched
        int after = db.getOrdersCount();
        // DB check if after count increased in 1
        assertEquals(before + 1, after);
        // DB check if credit id is reflected in orders list
        assertEquals(db.getLastCreditID(), db.getLastTransactionID());
        // DB check if credit was declined
        assertEquals("DECLINED", db.getLastCreditStatus());
    }

    @Test
    void shouldDeclineWithValidRandomCreditCard() {
        CardInfo card = CardDataGenerator.generateRandomValidCard(true);
        // store orders count before test launched
        int before = db.getOrdersCount();
        // launch tests and verify declined form results
        expectDeclinedResult(card);
        // get orders count after test launched
        int after = db.getOrdersCount();
        // DB check if after count is the same as before
        assertEquals(before, after);
    }

    @Test
    void shouldShowValidationsWithEmptyAndExpiredCards() {
        CardInfo card = CardDataGenerator.generateEmptyCard(false);
        // launch form with debit card
        page.clickRegularBuy();
        // check if form switched to regular payment mode
        page.getTitle().shouldHave(text(formPayByCardText));
        // fill and submit the form with card data
        this.fillAndSubmitTheForm(form, card);
        // check validations of fields
        form.getCardValidation().shouldHave(text(validationFormatText));
        form.getMonthValidation().shouldHave(text(validationFormatText));
        form.getYearValidation().shouldHave(text(validationFormatText));
        form.getOwnerValidation().shouldHave(text(validationEmptyText));
        form.getCVVValidation().shouldHave(text(validationFormatText));
        // check proper notifications
        notifications.getError().shouldNotBe(visible, Duration.ofSeconds(processingTime));
        notifications.getSuccess().shouldNotBe(visible);
        // fill form fields but with invalid card
        card = CardDataGenerator.generateExpiredRandomCard(false);
        // fill and submit the form with card data
        this.fillAndSubmitTheForm(form, card);
        // check validations of fields
        form.getCardValidation().shouldNotHave(text(validationFormatText));
        form.getMonthValidation().shouldHave(text(validationExpiredMonthText));
        form.getYearValidation().shouldNotHave(text(validationEmptyText));
        form.getOwnerValidation().shouldNotHave(text(validationEmptyText));
        form.getCVVValidation().shouldNotHave(text(validationFormatText));
        // check proper notifications
        notifications.getError().shouldNotBe(visible, Duration.ofSeconds(processingTime));
        notifications.getSuccess().shouldNotBe(visible);
    }
}
