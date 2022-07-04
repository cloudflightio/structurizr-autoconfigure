package io.cloudflight.architecture.structurizr

import org.slf4j.LoggerFactory
import org.springframework.boot.SpringApplication
import org.springframework.context.ConfigurableApplicationContext

/**
 * Use this class as a starter method for your applications. It wraps SpringApplication and logs an error if there is one
 *
 * @author Klaus Lehner
 */
object SpringStructurizr {

    private val LOG = LoggerFactory.getLogger(SpringStructurizr::class.java)

    fun run(architectureClass: Class<*>): ConfigurableApplicationContext {
        try {
            return SpringApplication.run(architectureClass)
        } catch (ex: Exception) {
            LOG.error("Error at creating Structurizr model", ex)
            throw ex
        }
    }
}
