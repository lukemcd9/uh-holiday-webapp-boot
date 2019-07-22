package edu.hawaii.its.holiday.configuration;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Profile(value = { "test" })
@Configuration
@ComponentScan(basePackages = "edu.hawaii.its.holiday")
@EnableJpaRepositories(basePackages = { "edu.hawaii.its.holiday.repository" })
@PropertySources({
        @PropertySource(value = "classpath:custom.properties"),
        @PropertySource(value = "file:${user.home}/.${user.name}-conf/holidays-overrides.properties",
                ignoreResourceNotFound = true)
})
@EntityScan(basePackages = { "edu.hawaii.its.holiday.type" })
@EnableTransactionManagement
public class AppConfigTest {

    private static Log logger = LogFactory.getLog(AppConfigTest.class);

    @PostConstruct
    public void init() {
        logger.info("AppConfigTest init");
    }
}
