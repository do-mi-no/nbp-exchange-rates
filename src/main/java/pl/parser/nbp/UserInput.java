package pl.parser.nbp;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Arrays;

public class UserInput {

    private final CurrencySupported currency;
    private final LocalDate dateStart;
    private final LocalDate dateEnd;

    private static DateTimeFormatter userDateFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
    static final LocalDate START_DATE_OF_THE_DATABASE = LocalDate.from(userDateFormatter.parse("2002-01-02"));
    static final LocalTime HOUR_OF_DATABASE_UPDATE = LocalTime.of(8, 15);

    UserInput(String[] args) {

        checkRightAmountOfInputArguments(args);

        String inputCurrency = args[0];
        String inputDateStart = args[1];
        String inputDateEnd = args[2];

        CurrencySupported currSupp = convertInputCurrencyToEnumValue(inputCurrency);
        LocalDate ldStart = parseUserInputToLocalDate(inputDateStart, "start");
        LocalDate ldEnd = parseUserInputToLocalDate(inputDateEnd, "end");

        validateStartAndEndDate(ldStart, ldEnd);

        this.currency = currSupp;
        this.dateStart = ldStart;
        this.dateEnd = ldEnd;
    }

    CurrencySupported getCurrency() {
        return currency;
    }

    LocalDate getDateStart() {
        return dateStart;
    }

    LocalDate getDateEnd() {
        return dateEnd;
    }

    private static void checkRightAmountOfInputArguments(String[] args) throws IllegalArgumentException {
        if (args.length != 3) {
            throw new IllegalArgumentException("\nInvalid number of input arguments: " + args.length + ".\n" +
                    "The right format of input data: 'CUR RRRR-MM-DD RRRR-MM-DD', where:\n" +
                    "CUR - one of supported currencies " + Arrays.toString(CurrencySupported.values()) +
                    ", plus START/END date,\nE.g: EUR 2013-01-28 2013-01-31");
        }
    }

    private static CurrencySupported convertInputCurrencyToEnumValue(String inputCurrency) throws IllegalArgumentException {
        CurrencySupported cs;
        try {
            cs = CurrencySupported.valueOf(inputCurrency.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("\nCurrency '" + inputCurrency + "' is not valid." +
                    "\nSupported currencies: " + Arrays.toString(CurrencySupported.values()) + ".");
        }
        return cs;
    }

    private static LocalDate parseUserInputToLocalDate(String inputDate, String dateType) throws DateTimeParseException {
        userDateFormatter = userDateFormatter.withResolverStyle(ResolverStyle.LENIENT);
        //todo: ResolverStyle should be STRICT, but if it is, id does not work...
        LocalDate ld;
        try {
            ld = LocalDate.parse(inputDate, userDateFormatter);
        } catch (Exception e) {
            throw new IllegalArgumentException("\nInvalid " + dateType + " date: " + inputDate + ".");
        }
        return ld;
    }

    private static void validateStartAndEndDate(LocalDate startDate, LocalDate endDate) throws IllegalArgumentException {
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        if (startDate.isBefore(START_DATE_OF_THE_DATABASE)) {
            throw new IllegalArgumentException("\nStart date is not valid. " +
                    "\nIt can not be earlier than: " + START_DATE_OF_THE_DATABASE + " (date of the first database record).");
        }

        if (startDate.isAfter(currentDate)) {
            throw new IllegalArgumentException("\nStart date is not valid. \n" +
                    "It can not be later than: " + currentDate + ".");
        }

        if (startDate.equals(currentDate) && currentTime.isBefore(HOUR_OF_DATABASE_UPDATE)) {
            throw new IllegalArgumentException(
                    "\nTable C of buying and selling foreign currencies is published (updated) on business days " +
                            "between " + HOUR_OF_DATABASE_UPDATE.minusMinutes(30L) + " and " + HOUR_OF_DATABASE_UPDATE + ".\n" +
                            "If you want to check exchange rates with start date = " + currentDate +
                            ", run the app after " + HOUR_OF_DATABASE_UPDATE + ".");
        }

        if (endDate.isAfter(currentDate)) {
            throw new IllegalArgumentException("\nEnd date is not valid. \n" +
                    "It can not be later than: " + currentDate + ".");
        }

        if (endDate.equals(currentDate) && currentTime.isBefore(HOUR_OF_DATABASE_UPDATE)) {
            throw new IllegalArgumentException(
                    "\nTable C of buying and selling foreign currencies is published (updated) on business days " +
                            "between " + HOUR_OF_DATABASE_UPDATE.minusMinutes(30L) + " and " + HOUR_OF_DATABASE_UPDATE + ".\n" +
                            "If you want to check exchange rates with end date = " + currentDate +
                            ", run the app after " + HOUR_OF_DATABASE_UPDATE + ".");
        }

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("\nStart date or End date is not valid." +
                    "\nMake sure, that: START date <= END date");
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserInput{");
        sb.append("currency=").append(currency);
        sb.append(", dateStart=").append(dateStart);
        sb.append(", dateEnd=").append(dateEnd);
        sb.append('}');
        return sb.toString();
    }
}
