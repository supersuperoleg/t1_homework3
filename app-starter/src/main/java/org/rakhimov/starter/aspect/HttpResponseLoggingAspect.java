package org.rakhimov.starter.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.rakhimov.starter.HttpLoggingConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class HttpResponseLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(HttpResponseLoggingAspect.class);

    private final HttpLoggingConfig httpLoggingConfig;

    public HttpResponseLoggingAspect(HttpLoggingConfig httpLoggingConfig) {
        this.httpLoggingConfig = httpLoggingConfig;
    }

    @AfterReturning(
            pointcut = "@annotation(org.rakhimov.starter.aspect.annotation.LogHttpResponse)",
            returning = "result"
    )
    public void logHttpResponse(JoinPoint joinPoint, Object result) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        Object[] args = joinPoint.getArgs();

        String logMessage = "REST метод: {}.{} выполнился успешно с аргументами: {}, результат: {}";

        switch (httpLoggingConfig.getAnnotationLogLevel().toUpperCase()) {
            case "DEBUG":
                logger.debug(logMessage, className, methodName, args, result);
                break;
            case "WARN":
                logger.warn(logMessage, className, methodName, args, result);
                break;
            case "ERROR":
                logger.error(logMessage, className, methodName, args, result);
                break;
            case "INFO":
                logger.info(logMessage, className, methodName, args, result);
                break;
            // default не нужен, так как конфигурация гарантирует валидный уровень логгирования
        }
    }
}
