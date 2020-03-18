package io.github.xiaoyureed;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @auther: xiaoyu
 * @date: 2018/10/30 11:41
 * @description:
 */
public class AsyncServletTest extends Base {

    @Test
    public void testDoGet() throws IOException {
        String       url    = "http://localhost:8080/heavy/async";
        HttpClient   client = HttpClientBuilder.create().build();
        HttpGet      req    = new HttpGet(url);
        HttpResponse resp   = client.execute(req);

        Assertions.assertThat(resp.getStatusLine().getStatusCode()).isEqualTo(200);

        String content = IOUtils.toString(resp.getEntity().getContent(), StandardCharsets.UTF_8);
        Assertions.assertThat(content).isEqualTo(
                "This is some heavy resource that will be served in an async way");
    }
}
