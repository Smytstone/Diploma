package ru.netology.data;


import com.github.javafaker.Faker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static java.lang.Integer.parseInt;

public class DataHelper {
    public static Faker faker = new Faker(new Locale("RU"));

    private DataHelper() {
    }

    public static String plusMonth(int plusMonth) {
        return LocalDate.now().plusMonths(plusMonth).format(DateTimeFormatter.ofPattern("MM"));
    }

    public static String getCurrentMonth() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("MM"));
    }

    public static String getCurrentYear() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yy"));
    }

    public static String getFirstCard() {
        return "4444 4444 4444 4441";
    }

    public static String getSecondCard() {
        return "4444 4444 4444 4442";
    }

    public static String getRandomNumbers(int number) {
        return faker.number().digits(number);
    }

    public static String invalidCard(int numbers) {
        return getRandomNumbers(numbers);
    }

    public static String getValidMonth(int plusMonth) {
        return plusMonth(plusMonth);
    }

    public static String getZeroMonth() {
        return "00";
    }


    public static String getInvalidMonth() {
        int month = parseInt(getCurrentMonth());
        return "2" + month;
    }

    public static String plusYears(int years) {
        return LocalDate.now().plusYears(years).format(DateTimeFormatter.ofPattern("yy"));
    }

    public static String getZeroYear() {
        return "00";
    }

    public static String oneDigitYear() {
        return getRandomNumbers(1);
    }

    public static String getValidHolderName() {
        return faker.name().name();
    }

    public static String getHolderNamePlusDigits() {
        return faker.name().firstName() + getRandomNumbers(5);
    }

    public static String getInvalidHolderName() {

        return ",АЗУАЬАПЗ;)Е(Л";
    }

    public static String getOneLetterName() {
        return faker.letterify("П");
    }

    public static String getLongName() {
        return String.valueOf(faker.lorem());
    }

    public static String getValidCVC() {
        return getRandomNumbers(3);
    }

    public static String getInvalidCVC() {
        return getRandomNumbers(2);
    }

}
