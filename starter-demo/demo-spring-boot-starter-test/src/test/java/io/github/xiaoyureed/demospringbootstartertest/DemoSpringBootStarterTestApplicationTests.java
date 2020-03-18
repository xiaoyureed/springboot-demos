package io.github.xiaoyureed.demospringbootstartertest;

import io.github.xiaoyureed.demospringbootstarter.IService;
import io.github.xiaoyureed.demospringbootstartertest.conditional.RandomGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
// @RunWith(SpringRunner.class)
public class DemoSpringBootStarterTestApplicationTests {

    @Autowired(required = false) // 默认按照类型注入, 如果有多个, 则按照名称, 若按照名称没找到, 报错: 有多个同类型bean
    private IService iService;

    @Test
    public void testStarter() {
        if (iService == null) {
            System.out.println(">>> iService is null ");
            return;
        }
        iService.exec();
    }

    @Autowired
    private RandomGenerator randomGenerator;

    @Test
    public void testConditional() {
        System.out.println(">>> " + randomGenerator.get());
    }

}
