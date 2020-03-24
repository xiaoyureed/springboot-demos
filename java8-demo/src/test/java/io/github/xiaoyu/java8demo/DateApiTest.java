package io.github.xiaoyu.java8demo;

import org.junit.Test;

import java.time.*;
import java.time.temporal.TemporalAdjusters;

/**
 * @author xiaoyu
 * date: 2020/3/20
 */
public class DateApiTest {
    @Test
    public void t1() {
        System.out.println(LocalDate.now());
        System.out.println(LocalTime.now());
        System.out.println(LocalDateTime.now());

        DayOfWeek thursday = LocalDate.parse("2017-07-20").getDayOfWeek();
        System.out.println("周四: " + thursday);
        int twenty = LocalDate.parse("2017-07-20").getDayOfMonth();
        System.out.println("twenty: " + twenty);

        LocalDate firstDayOfMonth = LocalDate.parse("2017-07-20")
                .with(TemporalAdjusters.firstDayOfMonth());
        System.out.println("这个月的第一天: " + firstDayOfMonth);
        firstDayOfMonth = firstDayOfMonth.withDayOfMonth(1);
        System.out.println("这个月的第一天: " + firstDayOfMonth);

        // LocalDate birthday   = LocalDate.of(2009, 07, 20);
        LocalDate birthday        = LocalDate.now();
        MonthDay  birthdayMd = MonthDay.of(birthday.getMonth(), birthday.getDayOfMonth());
        MonthDay  today      = MonthDay.from(LocalDate.now());
        System.out.println("今天是否是我的生日: " + today.equals(birthdayMd));
    }

}
