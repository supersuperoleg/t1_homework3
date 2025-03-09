package org.rakhimov.starter;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "annotation-logger")
public class AnnotationLoggerProperties {

    private String annotationLogLevel;

    public String getAnnotationLogLevel() {
        return annotationLogLevel;
    }

    public void setAnnotationLogLevel(String annotationLogLevel) {
        this.annotationLogLevel = annotationLogLevel;
    }
}
