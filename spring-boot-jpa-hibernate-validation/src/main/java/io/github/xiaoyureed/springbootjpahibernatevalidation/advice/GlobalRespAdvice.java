package io.github.xiaoyureed.springbootjpahibernatevalidation.advice;

import io.github.xiaoyureed.springbootjpahibernatevalidation.Resp;
import io.github.xiaoyureed.springbootjpahibernatevalidation.anno.IgnoreRespAdvice;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/12/5
 */
@RestControllerAdvice
public class GlobalRespAdvice implements ResponseBodyAdvice<Object> {
    @Override
    @SuppressWarnings("all")
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        // if anno present at class
        if (methodParameter.getDeclaringClass().isAnnotationPresent(IgnoreRespAdvice.class)) {
            return false;
        }
        // if anno present at method
        if (methodParameter.getMethod().isAnnotationPresent(IgnoreRespAdvice.class)) {
            return false;
        }
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> aClass,
                                  ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (o == null) {
            return ResponseEntity.ok(Resp.ok());
        }
        if (o instanceof Resp) {
            return ResponseEntity.ok(o);
        }
        if (o instanceof ResponseEntity) {
            return o;
        }
        return ResponseEntity.ok(Resp.ok(o));
    }
}
