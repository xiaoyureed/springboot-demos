package io.github.xiaoyureed.springbootdemo;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

public class T {
    @Test
    public void te() {
        Path path = Paths.get(System.getProperty("user.home") + "/tmp");
        String s = path.toAbsolutePath().toString();
        System.out.println(path.toString());
    }
}
