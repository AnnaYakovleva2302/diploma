package ru.netology.tour.test;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;

import com.codeborne.selenide.logevents.SelenideLogger;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.qameta.allure.Description;
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

    @Description("Compare real tour price with written in DB")
    public void expectMatchedTourPrice() {
        int tourPrice = page.getTourPrice();
        int savedTourPrice = db.getLastPaymentAmount();
        assertEquals(tourPrice, savedTourPrice);
    }

    @Description("Compare title of the form based on the card type")
    public void expectTitleMatchedCardType(CardInfo card) {
        String title = page.getTitle();
        if (card.isCredit()) {
            assertEquals(formPayByCreditText, title);
        }
        else {
            assertEquals(formPayByCardText, title);
        }
    }

    @Description("Check if successful result is shown correctly in the UI")
    public void expectAcceptedResult(CardInfo card) {
        page.launchFormBasedOnCard(card);
        expectTitleMatchedCardType(card);
        form.fillAndSubmit(card);
        notifications.getSuccess().shouldBe(visible, Duration.ofSeconds(processingTime));
        notifications.getError().shouldNotBe(visible);
    }

    @Description("Check if failed result is shown correctly in the UI")
    public void expectDeclinedResult(CardInfo card) {
        page.launchFormBasedOnCard(card);
        expectTitleMatchedCardType(card);
        form.fillAndSubmit(card);
        notifications.getError().shouldBe(visible, Duration.ofSeconds(processingTime));
        notifications.getSuccess().shouldNotBe(visible);
    }

    @Test
    @Description(
        "Test creating an order with valid accepted debit card," +
        " check if orders count increased in DB," +
        " order has corresponding payment," +
        " status of payment is APPROVED," +
        " announced tour price matched written in DB"
    )
    void shouldAcceptWithValidAcceptedDebitCard() {
        CardInfo card = CardDataGenerator.generateAcceptedCard(false);
        int before = db.getOrdersCount();
        expectAcceptedResult(card);
        int after = db.getOrdersCount();
        assertEquals(before + 1, after);
        assertEquals(db.getLastPaymentID(), db.getLastTransactionID());
        assertEquals("APPROVED", db.getLastPaymentStatus());
        expectMatchedTourPrice();
    }

    @Test
    @Description(
        "Test creating an order with valid declined debit card," +
        " check if orders count increased in DB," +
        " order has corresponding payment," +
        " status of payment is DECLINED," +
        " announced tour price matched written in DB"
    )
    void shouldDeclineWithValidDeclinedDebitCard() {
        CardInfo card = CardDataGenerator.generateDeclinedCard(false);
        int before = db.getOrdersCount();
        expectDeclinedResult(card);
        int after = db.getOrdersCount();
        assertEquals(before + 1, after);
        assertEquals(db.getLastPaymentID(), db.getLastTransactionID());
        assertEquals("DECLINED", db.getLastPaymentStatus());
        expectMatchedTourPrice();
    }

    @Test
    @Description(
        "Test creating an order with valid but random (not listed) debit card," +
        " check if orders count has not been changed in DB"
    )
    void shouldDeclineWithValidRandomDebitCard() {
        CardInfo card = CardDataGenerator.generateRandomValidCard(false);
        int before = db.getOrdersCount();
        expectDeclinedResult(card);
        int after = db.getOrdersCount();
        assertEquals(before, after);
    }

    @Test
    @Description(
        "Test creating an order with valid accepted credit card," +
        " check if orders count increased in DB," +
        " order has corresponding credit record," +
        " status of credit record is APPROVED"
    )
    void shouldAcceptWithValidAcceptedCreditCard() {
        CardInfo card = CardDataGenerator.generateAcceptedCard(true);
        int before = db.getOrdersCount();
        expectAcceptedResult(card);
        int after = db.getOrdersCount();
        assertEquals(before + 1, after);
        assertEquals(db.getLastCreditID(), db.getLastTransactionID());
        assertEquals("APPROVED", db.getLastCreditStatus());
    }

    @Test
    @Description(
        "Test creating an order with valid declined credit card," +
        " check if orders count increased in DB," +
        " order has corresponding credit record," +
        " status of credit record is DECLINED"
    )
    void shouldDeclineWithValidDeclinedCreditCard() {
        CardInfo card = CardDataGenerator.generateDeclinedCard(true);
        int before = db.getOrdersCount();
        expectDeclinedResult(card);
        int after = db.getOrdersCount();
        assertEquals(before + 1, after);
        assertEquals(db.getLastCreditID(), db.getLastTransactionID());
        assertEquals("DECLINED", db.getLastCreditStatus());
    }

    @Test
    @Description(
        "Test creating an order with valid but random (not listed) credit card," +
        " check if orders count has not been changed in DB"
    )
    void shouldDeclineWithValidRandomCreditCard() {
        CardInfo card = CardDataGenerator.generateRandomValidCard(true);
        int before = db.getOrdersCount();
        expectDeclinedResult(card);
        int after = db.getOrdersCount();
        assertEquals(before, after);
    }

    @Test
    @Description(
        "Test validating of the debit card form," +
        " check if proper validation messages are shown on form submitted if form fields were empty," +
        " check if validation messages disappear on form submit if form fields were fixed"
    )
    void shouldShowValidationsWithEmptyAndExpiredCards() {
        CardInfo card = CardDataGenerator.generateEmptyCard(false);
        page.launchFormBasedOnCard(card);
        expectTitleMatchedCardType(card);
        form.fillAndSubmit(card);
        assertEquals(validationFormatText, form.getCardValidation());
        assertEquals(validationFormatText, form.getMonthValidation());
        assertEquals(validationFormatText, form.getYearValidation());
        assertEquals(validationEmptyText, form.getOwnerValidation());
        assertEquals(validationFormatText, form.getCVVValidation());
        notifications.getError().shouldNotBe(visible, Duration.ofSeconds(processingTime));
        notifications.getSuccess().shouldNotBe(visible);
        card = CardDataGenerator.generateExpiredRandomCard(false);
        form.fillAndSubmit(card);
        assertEquals("", form.getCardValidation());
        assertEquals("", form.getMonthValidation());
        assertEquals("", form.getYearValidation());
        assertEquals("", form.getOwnerValidation());
        assertEquals("", form.getCVVValidation());
        notifications.getError().shouldNotBe(visible, Duration.ofSeconds(processingTime));
        notifications.getSuccess().shouldNotBe(visible);
    }
}
