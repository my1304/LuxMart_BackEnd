package luxmart_backend.config;
/*
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;

import javax.sql.DataSource;

@Configuration
public class LiquibaseConfig {

   @Bean
    public SpringLiquibase liquibase(@Qualifier("taskExecutor") TaskExecutor taskExecutor,
                                     DataSource dataSource, Environment env) {

        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog("classpath:db/changelog/master.xml");
        liquibase.setContexts(env.getProperty("liquibase.contexts"));
        liquibase.setDefaultSchema(env.getProperty("liquibase.default-schema"));
        liquibase.setDropFirst(true);
        liquibase.setShouldRun(Boolean.parseBoolean(env.getProperty("liquibase.enabled", "true")));
        return liquibase;
    }
}

*/