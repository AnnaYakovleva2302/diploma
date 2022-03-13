package ru.netology.tour.pageObject;

import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.SelenideElement;

import lombok.Data;

@Data
public class NotificationsPageObject {
    private SelenideElement error;
    private SelenideElement success;

    public NotificationsPageObject() {
        success = $(".notification_status_ok");
        error = $(".notification_status_error");
    }
}
