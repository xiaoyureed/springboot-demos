package io.github.xiaoyureed.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/11/14
 */
@Component
@Aspect
public class ReqCheckAop {

    @Autowired
    private ObjectMapper objectMapper;

    @Pointcut("execution(org.springframework.http.ResponseEntity<Resp> io.github.xiaoyureed.demo..*.*(..))")
    private void pointcut() {}

    @Around("pointcut()")
    public Object reqCheck(ProceedingJoinPoint jp) throws Throwable {
        Object[] args = jp.getArgs();
        System.out.println(">>> req = " + objectMapper.writeValueAsString(args[0]));
        return jp.proceed();
    }
}
