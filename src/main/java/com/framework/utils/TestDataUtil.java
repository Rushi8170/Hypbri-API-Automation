package com.framework.utils;

import com.github.javafaker.Faker;

import java.util.Locale;

/**
 * TestDataUtil — generates random realistic test data using JavaFaker.
 *
 * Usage: String email = TestDataUtil.randomEmail();
 */
public final class TestDataUtil {

    private static final Faker faker = new Faker(Locale.ENGLISH);

    private TestDataUtil() {}

    public static String randomFirstName()   { return faker.name().firstName(); }
    public static String randomLastName()    { return faker.name().lastName(); }
    public static String randomFullName()    { return faker.name().fullName(); }
    public static String randomEmail()       { return faker.internet().emailAddress(); }
    public static String randomPhone()       { return faker.phoneNumber().cellPhone(); }
    public static String randomAddress()     { return faker.address().streetAddress(); }
    public static String randomCity()        { return faker.address().city(); }
    public static String randomZipCode()     { return faker.address().zipCode(); }
    public static String randomCountry()     { return faker.address().country(); }
    public static String randomPassword()    { return faker.internet().password(8, 16, true, true); }
    public static String randomUsername()    { return faker.name().username(); }
    public static String randomUUID()        { return java.util.UUID.randomUUID().toString(); }
    public static int    randomNumber(int min, int max) { return faker.number().numberBetween(min, max); }
    public static String randomCompanyName() { return faker.company().name(); }
}
