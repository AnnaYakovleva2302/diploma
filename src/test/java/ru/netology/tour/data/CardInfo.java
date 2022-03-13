package ru.netology.tour.data;

import lombok.Value;

@Value
public class CardInfo {
    String number;
    String month;
    String year;
    String owner;
    String cvv;
    boolean isCredit;
}