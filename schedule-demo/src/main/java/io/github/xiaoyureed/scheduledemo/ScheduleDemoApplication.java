package io.github.xiaoyureed.scheduledemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
@EnableScheduling // 开启自带的定时任务
public class ScheduleDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScheduleDemoApplication.class);
    }
}

@Component
@Slf4j
class PrintTask {

    /**
     * cron = "seconds minutes hours day month week [year]"
     *
     * 星号(*)：可用在所有字段中，表示对应时间域的每一个时刻，例如，*在分钟字段时，表示“每分钟”；
     * 问号（?）：该字符只在日期和星期字段中使用，它通常指定为“无意义的值”，相当于占位符；
     * 减号(-)：表达一个范围，如在小时字段中使用“10-12”，则表示从10到12点，即10,11,12；
     * 逗号(,)：表达一个列表值，如在星期字段中使用“MON,WED,FRI”，则表示星期一，星期三和星期五；
     * 斜杠(/)：x/y表达一个等步长序列，x为起始值，y为增量步长值。如在秒数字段中使用0/15，则表示为0,15,30和45秒，而5/15在分钟字段中表示5,20,35,50；
     * x 也可以用 * 代替, 等同 0/y
     */
    @Scheduled(cron = "0/2 * * * * ?")
    public void exec() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        String nowFormatted = now.format(DateTimeFormatter.ISO_DATE_TIME);
        log.info(">>> exec scheduled task, current time: {}", nowFormatted);
    }
}

