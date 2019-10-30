package edu.hawaii.its.holiday.configuration;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Profile(value = { "dev", "default", "localhost" })
@Configuration
@ComponentScan(basePackages = { "edu.hawaii.its.holiday" },
        excludeFilters = { @Filter(type = FilterType.ANNOTATION, value = Configuration.class) })
@EntityScan(basePackages = { "edu.hawaii.its.holiday.type" })
//@EnableJpaRepositories(basePackages = { "edu.hawaii.its.holiday.repository" })
@MapperScan(basePackages = "edu.hawaii.its.holiday.mapper")
@EnableTransactionManagement
@PropertySource("classpath:custom.properties")
public class WebConfig {

    private static final Log logger = LogFactory.getLog(WebConfig.class);

    @PostConstruct
    public void init() {
        logger.info("AppConfig init");

    }
}