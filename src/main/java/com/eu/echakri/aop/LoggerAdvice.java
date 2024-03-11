package com.eu.echakri.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
//@Component
public class LoggerAdvice {

    Logger log = LoggerFactory.getLogger(LoggerAdvice.class);

    @Pointcut(value = "execution(* com.eu.echakri.*..*(..) )")
    public void myPointCut() {

    }

    @Around("myPointCut()")
    public Object applicationLogger(ProceedingJoinPoint joinPoint) throws Throwable {

        ObjectMapper mapper = new ObjectMapper();
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().toString();
        Object[] args = joinPoint.getArgs();
        log.info("Method Invoked " + className + " " + methodName + "()" + "Arguments: " + mapper.writeValueAsString(args));

        Object object = joinPoint.proceed();
        log.info("Method Invoked " + className + " " + methodName + "()" + "Response: " + mapper.writeValueAsString(object));

        return object;

    }
}
