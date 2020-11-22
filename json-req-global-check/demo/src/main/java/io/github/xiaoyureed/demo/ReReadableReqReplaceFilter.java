package io.github.xiaoyureed.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/11/14
 */
//@Component
public class ReReadableReqReplaceFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(ReReadableReqReplaceFilter.class);

    {
        log.info(">>> load ReReadableReqReplaceFilter [ok]");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(new ReReadableRequest((HttpServletRequest) servletRequest), servletResponse);
    }

    private static class ReReadableInputStream extends ServletInputStream {

        private final ByteArrayInputStream inputStream;

        private ReReadableInputStream(byte[] body) {
            inputStream = new ByteArrayInputStream(body);
        }

        @Override
        public boolean isFinished() {
            return this.inputStream.available() == 0;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener readListener) {

        }

        @Override
        public int read() throws IOException {
            return this.inputStream.read();
        }
    }

    private static class ReReadableRequest extends HttpServletRequestWrapper {

        private final byte[] requestBody;

        private BufferedReader reader;

        private ServletInputStream inputStream;

        public ReReadableRequest(HttpServletRequest request) throws IOException {
            super(request);
            requestBody = StreamUtils.copyToByteArray(request.getInputStream());
        }

        @Override
        public BufferedReader getReader() throws IOException {
            if (reader != null) {
                return this.reader;
            }
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(getInputStream(), StandardCharsets.UTF_8));//getCharacterEncoding()
            this.reader = bufferedReader;
            return bufferedReader;
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            if (this.inputStream != null) {
                return inputStream;
            }
            if (this.requestBody != null) {
                ReReadableInputStream inputStream = new ReReadableInputStream(requestBody);
                this.inputStream = inputStream;
                return inputStream;
            }
            return super.getInputStream();

        }
    }
}
