package io.github.xiaoyureed.jetty;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.io.IOException;

/**
 * @auther: xiaoyu
 * @date: 2018/10/30 10:10
 * @description:
 */
public class BlockingServletTest extends Base {

    @Test
    public void testDoGet() throws IOException {
        String              url    = "http://localhost:8080/status";
        CloseableHttpClient client = HttpClientBuilder.create().build();

        HttpGet               reqGet  = new HttpGet(url);
        CloseableHttpResponse respGet = client.execute(reqGet);

        Assertions.assertThat(respGet.getStatusLine().getStatusCode()).isEqualTo(200);
    }
}
