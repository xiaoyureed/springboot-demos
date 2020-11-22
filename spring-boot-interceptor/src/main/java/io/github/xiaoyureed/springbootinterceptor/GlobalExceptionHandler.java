package io.github.xiaoyureed.springbootinterceptor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/11/12
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(AuthException.class)
    public ResponseEntity<String> handleAuthException() {
        return ResponseEntity.ok("auth failed");
    }
}
