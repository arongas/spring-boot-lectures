package gr.rongasa.testing.service;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class DateTimeUtilsTest {

    @Spy
//    @Mock
    Preferences preferences;

    @Test
    public void addDays() {
        DateTimeUtils dateTimeUtils = new DateTimeUtils(preferences);
        when(preferences.getOrDefault(DateTimeUtils.DAYS, "0")).thenReturn("5");
//        when(preferences.getOrDefault(eq(DateTimeUtils.WEEKS), eq("0"))).thenReturn("0");
//        when(preferences.getOrDefault(eq(DateTimeUtils.MONTHS), eq("0"))).thenReturn("0");
//        when(preferences.getOrDefault(eq(DateTimeUtils.YEARS), anyString())).thenReturn("0");

        ZonedDateTime aDate = ZonedDateTime.of(2011, 2, 2, 13, 45, 11, 0, ZoneId.of("Europe/Athens"));
        ZonedDateTime dateTime = dateTimeUtils.addDays(aDate);
        Assertions.assertThat(dateTime).isEqualTo(aDate.plus(5, ChronoUnit.DAYS));
    }
}