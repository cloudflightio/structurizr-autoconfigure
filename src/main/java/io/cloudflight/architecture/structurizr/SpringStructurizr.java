package io.cloudflight.architecture.structurizr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Use this class as a starter method for your applications. It wraps SpringApplication and logs an error if there is one
 *
 * @author Klaus Lehner
 */
public final class SpringStructurizr {

    private static final Logger LOG = LoggerFactory.getLogger(SpringStructurizr.class);

    private SpringStructurizr() {
    }

    public static ConfigurableApplicationContext run(Class<?> architectureClass) {
        try {
            return SpringApplication.run(architectureClass);
        } catch (Exception ex) {
            LOG.error("Error at creating Structurizr model", ex);
            throw ex;
        }
    }
}