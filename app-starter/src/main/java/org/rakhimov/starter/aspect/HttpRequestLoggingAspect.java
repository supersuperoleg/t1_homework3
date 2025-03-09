package org.rakhimov.starter.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.rakhimov.starter.HttpLoggingConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class HttpRequestLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequestLoggingAspect.class);

    private final HttpLoggingConfig httpLoggingConfig;

    public HttpRequestLoggingAspect(HttpLoggingConfig httpLoggingConfig) {
        this.httpLoggingConfig = httpLoggingConfig;
    }

    @Before("@annotation(org.rakhimov.starter.aspect.annotation.LogHttpRequest)")
    public void logHttpRequests(JoinPoint joinPoint) {
        String annotationLogLevel = httpLoggingConfig.getAnnotationLogLevel();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        Object[] args = joinPoint.getArgs();

        String logMessage = "Вызван REST метод: {}.{} с аргументами: {}";

        switch (annotationLogLevel.toUpperCase()) {
            case "DEBUG":
                logger.debug(logMessage, className, methodName, args);
                break;
            case "WARN":
                logger.warn(logMessage, className, methodName, args);
                break;
            case "ERROR":
                logger.error(logMessage, className, methodName, args);
                break;
            case "INFO":
                logger.info(logMessage, className, methodName, args);
                break;
            // default не нужен, так как конфигурация гарантирует валидный уровень логгирования
        }
    }
}
