package pl.parser.nbp;

import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class UserInputTest {

    @Test(expected = IllegalArgumentException.class)
    public void argsAmountTest1tooMany() {
        UserInput userInput = new UserInput(new String[]{"EUR", "2013-01-28", "2013-01-31", "terefere", "rampampam"});
    }

    @Test(expected = IllegalArgumentException.class)
    public void argsAmountTest2tooFew() {
        UserInput userInput = new UserInput(new String[]{"EUR"});
    }

    @Test
    public void argsAmountTest3ok() {
        UserInput userInputOK = new UserInput(new String[]{"EUR", "2013-01-28", "2013-01-31"});
        assertNotNull(userInputOK);
    }

    @Test
    public void currencyValidationTest1LowerUpper() {
        UserInput userInput1 = new UserInput(new String[]{"eur", "2013-01-28", "2013-01-31"});
        assertEquals(CurrencySupported.EUR, userInput1.getCurrency());
        UserInput userInput2 = new UserInput(new String[]{"EUR", "2013-01-28", "2013-01-31"});
        assertEquals(CurrencySupported.EUR, userInput2.getCurrency());
        UserInput userInput3 = new UserInput(new String[]{"gBp", "2013-01-28", "2013-01-31"});
        assertEquals(CurrencySupported.GBP, userInput3.getCurrency());
        UserInput userInput4 = new UserInput(new String[]{"cHF", "2013-01-28", "2013-01-31"});
        assertEquals(CurrencySupported.CHF, userInput4.getCurrency());
    }

    @Test(expected = IllegalArgumentException.class)
    public void currencyValidationTest1notSupported() {
        UserInput userInput = new UserInput(new String[]{"EURO", "2013-01-28", "2013-01-31"});
    }

    @Test(expected = IllegalArgumentException.class)
    public void currencyValidationTest2notSupported() {
        UserInput userInput = new UserInput(new String[]{"PLN", "2013-01-28", "2013-01-31"});
    }

    @Test(expected = IllegalArgumentException.class)
    public void currencyValidationTest3notSupported() {
        UserInput userInput = new UserInput(new String[]{"zlotowka", "2013-01-28", "2013-01-31"});
    }

    @Test(expected = IllegalArgumentException.class)
    public void startDateTest1parseInput() {
        UserInput userInput = new UserInput(new String[]{"EUR", "20130-01-28", "2013-01-31"});
    }

    @Test(expected = IllegalArgumentException.class)
    public void startDateTest2parseInput() {
        UserInput userInput = new UserInput(new String[]{"EUR", "2013-101-28", "2013-01-31"});
    }

    @Test(expected = IllegalArgumentException.class)
    public void startDateTest3parseInput() {
        UserInput userInput = new UserInput(new String[]{"EUR", "dawnodawnotemu", "2013-01-31"});
    }

    @Test(expected = IllegalArgumentException.class)
    public void endDateTest1parseInput() {
        UserInput userInput = new UserInput(new String[]{"EUR", "2013-01-31", "20130-01-28"});
    }

    @Test(expected = IllegalArgumentException.class)
    public void endDateTest2parseInput() {
        UserInput userInput = new UserInput(new String[]{"EUR", "2013-01-31", "2013-101-28"});
    }

    @Test(expected = IllegalArgumentException.class)
    public void endDateTest3parseInput() {
        UserInput userInput = new UserInput(new String[]{"EUR", "2013-01-31", "rokswietlnypozniej"});
    }

    @Test(expected = IllegalArgumentException.class)
    public void startDateBeforeDbBegin() {
        UserInput userInput = new UserInput(new String[]{"EUR", UserInput.START_DATE_OF_THE_DATABASE.minusDays(1).toString(), "2013-01-31"});
    }

    @Test(expected = IllegalArgumentException.class)
    public void startDateAfterCurrentDate() {
        UserInput userInput = new UserInput(new String[]{"EUR", LocalDate.now().plusDays(1).toString(), LocalDate.now().toString()});
    }

    @Test(expected = IllegalArgumentException.class)
    public void endDateAfterCurrentDate() {
        UserInput userInput = new UserInput(new String[]{"EUR", "2013-01-31", LocalDate.now().plusDays(1).toString()});
    }

}