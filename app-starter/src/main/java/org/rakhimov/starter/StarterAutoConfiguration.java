package org.rakhimov.starter;

import org.rakhimov.starter.aspect.HttpRequestLoggingAspect;
import org.rakhimov.starter.aspect.HttpResponseLoggingAspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableConfigurationProperties(AnnotationLoggerProperties.class)
public class StarterAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(StarterAutoConfiguration.class);

    private final AnnotationLoggerProperties annotationLoggerProperties;

    public StarterAutoConfiguration(AnnotationLoggerProperties annotationLoggerProperties) {
        this.annotationLoggerProperties = annotationLoggerProperties;
    }

    @Bean
    @ConditionalOnProperty(name = "annotation-logger.enabled", havingValue = "true", matchIfMissing = true)
    public HttpRequestLoggingAspect httpRequestLoggingAspect(HttpLoggingConfig httpLoggingConfig) {
        return new HttpRequestLoggingAspect(httpLoggingConfig);
    }

    @Bean
    @ConditionalOnProperty(name = "annotation-logger.enabled", havingValue = "true", matchIfMissing = true)
    public HttpResponseLoggingAspect httpResponseLoggingAspect(HttpLoggingConfig httpLoggingConfig) {
        return new HttpResponseLoggingAspect(httpLoggingConfig);
    }

    @Bean
    public HttpLoggingConfig httpLoggingConfig() {
        String annotationLogLevel = annotationLoggerProperties.getAnnotationLogLevel();
        String defaultLogLevel = "INFO";
        if (annotationLogLevel == null || annotationLogLevel.isEmpty()) {
            logger.warn("Не указано значение параметра annotation-logger.annotation-log-level! " +
                    "Будет использоваться значение по умолчанию - {}.", defaultLogLevel);
            annotationLogLevel = defaultLogLevel;
        }
        List<String> validLogLevels = Arrays.asList("DEBUG", "INFO", "WARN", "ERROR");
        String finalLogLevel = annotationLogLevel.toUpperCase();

        if (!validLogLevels.contains(finalLogLevel)) {
            logger.warn("Неизвестный уровень логирования: {}. Допустимые значения: {}. " +
                            "Будет использоваться значение по умолчанию - {}.",
                    annotationLogLevel, validLogLevels, defaultLogLevel);
            annotationLogLevel = defaultLogLevel;
        }

        return new HttpLoggingConfig(annotationLogLevel);
    }


}
