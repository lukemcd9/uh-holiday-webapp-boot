package edu.hawaii.its.holiday.configuration;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseConfig {

    private static final Log logger = LogFactory.getLog(DatabaseConfig.class);

    @Value("${spring.datasource.initialize:false}")
    private boolean springDatasourceInitialize;

    @Value("${spring.datasource.schema:}")
    private String springDatasourceSchema;

    @PostConstruct
    public void init() {
        logger.info("init starting");
        logger.info("init; 'spring.datasource.initialize': " + springDatasourceInitialize);
        logger.info("init; 'spring.datasource.schema'    : " + springDatasourceSchema);
        logger.info("init finished");
    }

}
