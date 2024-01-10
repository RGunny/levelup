package me.rgunny.levelup.common.aspect;


import com.google.common.base.Joiner;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Map;
import java.util.stream.Collectors;

@Component
@Aspect
@Slf4j
public class LoggingAspect {

    @Pointcut("execution(* me.rgunny.levelup..*Controller.*(..))")
    public void loggingPointcut() {
    }

    @Pointcut("execution(* me.rgunny.levelup.common.exception.ExceptionAdvice.*(..))")
    public void exceptionHandlerPointcut() {
    }

    @Around("loggingPointcut()")
    public Object loggingRequest(ProceedingJoinPoint pjp) throws Throwable {
        HttpServletRequest request = getCurrentRequest();

        Map<String, String[]> parameterMap = request.getParameterMap();

        String parameters = "";
        if (!parameterMap.isEmpty())
            parameters = " [" + parameterMapToString(parameterMap) + "]";

        long start = System.currentTimeMillis();
        try {
            return pjp.proceed(pjp.getArgs());
        } finally {
            long end = System.currentTimeMillis();
            log.info("Request: {} {}{} < {} ({}ms)", request.getMethod(), request.getRequestURI(),
                    parameters, request.getRemoteHost(), end - start);
        }
    }

    private HttpServletRequest getCurrentRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    private String parameterMapToString(Map<String, String[]> paramMap) {
        return paramMap.entrySet().stream()
                .map(entry -> String.format("%s: (%s)",
                        entry.getKey(),
                        Joiner.on(",").join(entry.getValue())))
                .collect(Collectors.joining(", "));
    }
}
