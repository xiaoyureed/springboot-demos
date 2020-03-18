package io.github.xiaoyu.h2jpademo;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Optional;

/**
 * @author xiaoyu
 * @since 1.0
 */
@Slf4j
public class Other {
    @Test
    public void test() {
        Optional<String> opt = Optional.of("nihao");
        boolean          present = opt.isPresent();// true
        log.info(opt.get());  // nihao
    }
}
