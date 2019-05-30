package gr.rongasa.testing.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Period;
import java.time.ZonedDateTime;


@RequiredArgsConstructor
@Service
public class DateTimeUtils {
    public static final String DAYS = "DAYS";
    public static final String WEEKS = "WEEKS";
    public static final String YEARS = "YEARS";
    public static final String MONTHS = "MONTHS";

    public final Preferences preferences;

    public ZonedDateTime addDays(ZonedDateTime dateTime) {
        dateTime = dateTime.plus(Period.ofDays(Integer.parseInt(preferences.getOrDefault(DAYS, "0"))));
        dateTime = dateTime.plus(Period.ofWeeks(Integer.parseInt(preferences.getOrDefault(WEEKS, "0"))));
        dateTime = dateTime.plus(Period.ofMonths(Integer.parseInt(preferences.getOrDefault(MONTHS, "0"))));
        dateTime = dateTime.plus(Period.ofYears(Integer.parseInt(preferences.getOrDefault(YEARS, "0"))));
        return dateTime;
    }
}
