package org.rakhimov.starter;

public class HttpLoggingConfig {

    private final String annotationLogLevel;

    public HttpLoggingConfig(String annotationLogLevels) {
        this.annotationLogLevel = annotationLogLevels;
    }

    public String getAnnotationLogLevel() {
        return annotationLogLevel;
    }
}
