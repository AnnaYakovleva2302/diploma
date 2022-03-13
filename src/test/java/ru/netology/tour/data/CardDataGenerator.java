package ru.netology.tour.data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.github.javafaker.Faker;

public class CardDataGenerator {
    public static CardInfo generateAcceptedCard(boolean isCreditCard) {
        Faker faker = new Faker(new Locale("en"));
        String month = CardDataGenerator.generateMonth(1);
        String year = CardDataGenerator.generateYear(1);

        return new CardInfo(
            "4444444444444441",
            month,
            year,
            faker.artist().name(),
            faker.random().nextInt(111, 999).toString(),
            isCreditCard
        );
    }

    public static CardInfo generateDeclinedCard(boolean isCreditCard) {
        Faker faker = new Faker(new Locale("en"));
        String month = CardDataGenerator.generateMonth(1);
        String year = CardDataGenerator.generateYear(1);

        return new CardInfo(
            "4444444444444442",
            month,
            year,
            faker.artist().name(),
            faker.random().nextInt(111, 999).toString(),
            isCreditCard
        );
    }

    public static CardInfo generateRandomValidCard(boolean isCreditCard) {
        Faker faker = new Faker(new Locale("en"));
        String month = CardDataGenerator.generateMonth(1);
        String year = CardDataGenerator.generateYear(1);

        return new CardInfo(
            faker.finance().creditCard(),
            month,
            year,
            faker.artist().name(),
            faker.random().nextInt(111, 999).toString(),
            isCreditCard
        );
    }

    public static CardInfo generateExpiredRandomCard(boolean isCreditCard) {
        Faker faker = new Faker(new Locale("en"));
        String month = CardDataGenerator.generateMonth(-1);
        String year = CardDataGenerator.generateYear(-1);

        return new CardInfo(
            faker.finance().creditCard(),
            month,
            year,
            faker.artist().name(),
            faker.random().nextInt(111, 999).toString(),
            isCreditCard
        );
    }

    public static CardInfo generateEmptyCard(boolean isCreditCard) {
        return new CardInfo(
            "",
            "",
            "",
            "",
            "",
            isCreditCard
        );
    }

    private static String generateMonth(int increment) {
        LocalDate local = LocalDate.now();
        local = local.plusMonths(increment);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM");
        return local.format(formatter);
    }

    private static String generateYear(int increment) {
        LocalDate local = LocalDate.now();
        local = local.plusMonths(increment);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YY");
        return local.format(formatter);
    }
}
