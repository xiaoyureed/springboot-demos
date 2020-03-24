package io.github.xiaoyureed.weixinlogindemo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@SpringBootApplication
public class WeixinLoginDemoApplication {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(WeixinLoginDemoApplication.class, args);
    }

}

@Controller
class LoginController {

    private final WechatProps wechatProps;

    private final RestTemplate restTemplate;

    @Autowired
    public LoginController(WechatProps wechatProps, RestTemplate restTemplate) {
        this.wechatProps = wechatProps;
        this.restTemplate = restTemplate;
    }

    @RequestMapping("/wechat/login")
    public void wechatLogin(HttpServletResponse resp) throws IOException {
        // 向微信发送请求, 设置回调接口
        //todo 回调地址部署上线后需要更改为真实互联网地址
        // 这里先用 花生壳 获取临时域名
        resp.sendRedirect("https://open.weixin.qq.com/connect/qrconnect"
                + "&appid=" + wechatProps.getAppid()
                + "&redirect_uri=" + URLEncoder.encode("http://xxx:8080", StandardCharsets.UTF_8.toString())
                + "&response_type=code&scope=snsapi_login&state=STATE#wechat_redirect");
    }

    @RequestMapping("/wechat/callback")
    public void wechatCallback(@RequestParam String code) {
        // 通过 code 拿到 access_token
        StringBuilder tokenUrl = new StringBuilder("https://api.weixin.qq.com/sns/oauth2/access_token");
        tokenUrl.append("&appid=" + wechatProps.getAppid());
        tokenUrl.append("&secret=" + wechatProps.getSecret());
        tokenUrl.append("&code=" + code);
        tokenUrl.append("&grant_type=authorization_code");
        WechatTokenRespDto wechatTokenRespDto = restTemplate.getForObject(tokenUrl.toString(), WechatTokenRespDto.class);

        // 通过 openid, token 获取用户信息
        HashMap<String, Object> userInfoArgs = new HashMap<>(2);
        userInfoArgs.put("openid", wechatTokenRespDto.getOpenid());
        userInfoArgs.put("access_token", wechatTokenRespDto.getAccess_token());
        WechatUserInfoRespDto wechatUserInfoRespDto = restTemplate.getForObject("", WechatUserInfoRespDto.class, userInfoArgs);
        // todo
    }
}

@Component
@ConfigurationProperties("wechat")
@Data
@AllArgsConstructor
@NoArgsConstructor
class WechatProps {

    private String appid;

    private String secret;
}

@Data
class WechatTokenRespDto {

    private String access_token;
    private String expire_in;
    private String refresh_toke;
    private String openid;
    private String scope;
    private String unionid;
}

@Data
class WechatUserInfoRespDto {
    private String name;
}
