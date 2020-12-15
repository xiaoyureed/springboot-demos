package io.github.xiaoyureed.springbootjpahibernatevalidation.advice;

import io.github.xiaoyureed.springbootjpahibernatevalidation.Resp;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/12/5
 */
@ControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<Resp<?>> handleMethodArgumentNotValidException(
            HttpServletRequest req, MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        List<FieldError> fieldErrors   = bindingResult.getFieldErrors();

        List<String> fes = fieldErrors.stream().map(
                fe -> fe.getDefaultMessage()).collect(Collectors.toList());
        return ResponseEntity.ok(Resp.error(String.join(", ", fes)));
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<Resp<?>> handleException(HttpServletRequest req, Exception ex) {
        return ResponseEntity.ok(Resp.error(ex.getMessage()));
    }
}
