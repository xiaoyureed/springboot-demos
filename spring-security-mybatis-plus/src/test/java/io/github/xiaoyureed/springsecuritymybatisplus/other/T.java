package io.github.xiaoyureed.springsecuritymybatisplus.other;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author xiaoyu
 * date: 2020/3/22
 */
public class T {

    @Test
    public void t() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String                encode11                = bCryptPasswordEncoder.encode("11");
        String                encode22 = bCryptPasswordEncoder.encode("22");
        System.out.println(encode11);
        System.out.println(encode22);
    }
}
