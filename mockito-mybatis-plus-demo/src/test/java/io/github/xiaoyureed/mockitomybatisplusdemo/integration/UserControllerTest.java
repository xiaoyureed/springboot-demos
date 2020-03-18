package io.github.xiaoyureed.mockitomybatisplusdemo.integration;

import io.github.xiaoyureed.mockitomybatisplusdemo.pojo.po.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import java.net.Inet4Address;
import java.net.URI;
import java.net.UnknownHostException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 集成测试, 请求真实数据
 *
 * @author xiaoyu
 * date: 2020/1/24
 */
@SpringBootTest
@Slf4j
public class UserControllerTest {
    private TestRestTemplate testRestTemplate;

    private String url;

    @Autowired
    private Environment env;

    private TestRestTemplate testRestTemplate() {
        return new TestRestTemplate();
    }

    private String host() {
        try {
            return Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return "";
        }
    }

    private String port() {
        String port = env.getProperty("local.server.port");
        if (StringUtils.isEmpty(port)) {
            return "8080";
        }
        return port;
    }

    @BeforeEach
    public void init() {
        testRestTemplate = testRestTemplate();
        url = "http://" + host() + ":" + port();
    }

    @Test
    public void testLogin() {
        User reqUser = new User();
        reqUser.setName("Jack");
        reqUser.setPwd("abd");
        ResponseEntity<String> resp = testRestTemplate.exchange(RequestEntity
                .post(URI.create(url + "/user/session"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(reqUser), String.class);
        log.info(">>> token = {}", resp.getBody());
        assertThat(resp.getBody()).isNotBlank();
    }
}
