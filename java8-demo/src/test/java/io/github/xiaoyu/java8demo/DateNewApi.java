package io.github.xiaoyu.java8demo;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * @author xiaoyu
 * @since 1.0
 */
public class DateNewApi {

    /**
     * Instant 是新旧api的中介
     */
    @Test
    public void testInstant() {
        Clock clock  = Clock.systemDefaultZone();
        Clock clock1 = Clock.systemUTC();// 等价

        Instant instant  = clock.instant();
        Instant instant1 = clock1.instant();
        assertThat(instant.toString().equals(instant1.toString())).isEqualTo(true);// 2019-05-04T04:58:43.456Z
    }

    @Test
    public void testConvert() {
        Clock   clock   = Clock.systemUTC();
        Instant instant = clock.instant();
        Date    date    = Date.from(instant);
        System.out.println(date);// Sat May 25 11:10:47 CST 2019

        Date    nowDate    = new Date();
        Instant nowInstant = nowDate.toInstant();
        System.out.println(nowInstant.toString());// 2019-05-25T03:20:18.379Z

        ZoneId        zone          = clock.getZone();
        ZoneId        zoneId        = ZoneId.systemDefault();// 等价
        LocalDateTime localDateTime = LocalDateTime.ofInstant(nowInstant, zone);
        System.out.println(localDateTime);// 2019-05-25T03:23:28.016
    }

    @Test
    public void testMills() throws InterruptedException {
        Clock clock = Clock.systemUTC();
        long  start = clock.millis();

        TimeUnit.SECONDS.sleep(1);

        long end   = clock.millis();
        long spend = end - start;
        System.out.println(spend); // 1001

    }

    @Test
    public void testZoneId() {
        ZoneId        zone1 = ZoneId.systemDefault();
        ZoneId        zone2 = ZoneId.of("Europe/Berlin");
        LocalDateTime now1  = LocalDateTime.now(zone1);
        LocalDateTime now2  = LocalDateTime.now(zone2);

        long between = ChronoUnit.HOURS.between(now1, now2);
        System.out.println(between); // -6
    }


    @Test
    public void testFormatter() {
        LocalTime late = LocalTime.of(23, 59, 59);
        System.out.println(late);       // 23:59:59

        LocalDateTime localDateTime = LocalDateTime.of(2019, 1, 1, 1, 30, 10);
        System.out.println(localDateTime);// 2019-01-01T01:30:10

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        String format = localDateTime.format(dateTimeFormatter);
        System.out.println(format); // 2019-30-01 01:30:10

        // todo
        // LocalDateTime parse = LocalDateTime.parse("2019-30-01 01:30:10", dateTimeFormatter);
        // System.out.println(parse);
    }

    @Test
    public void testLocalDateTime() {
        LocalDateTime sylvester = LocalDateTime.of(2014, Month.DECEMBER, 31, 23, 59, 59);

        DayOfWeek dayOfWeek = sylvester.getDayOfWeek();
        System.out.println(dayOfWeek);      // WEDNESDAY

        Month month = sylvester.getMonth();
        System.out.println(month);          // DECEMBER

        long minuteOfDay = sylvester.getLong(ChronoField.MINUTE_OF_DAY);
        System.out.println(minuteOfDay);    // 1439

        /////////////////////////
        // 转为 instant
        Instant instant = sylvester
                .atZone(ZoneId.systemDefault())
                .toInstant();

        Date legacyDate = Date.from(instant);
        System.out.println(legacyDate);     // Wed Dec 31 23:59:59 CET 2014

        DateTimeFormatter formatter =
                DateTimeFormatter
                        .ofPattern("MMM dd, yyyy - HH:mm");

        LocalDateTime parsed = LocalDateTime.parse("Nov 03, 2014 - 07:13", formatter);
        String        string = formatter.format(parsed);
        System.out.println(string);     // Nov 03, 2014 - 07:13
    }
}
