//package edu.online.messenger.configuration;
//
//import liquibase.integration.spring.SpringLiquibase;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import javax.sql.DataSource;
//
//@Configuration
//public class LiquibaseConfiguration {
//
//    private static final String changelogString = "classpath:changelog-master.xml";
//    @Bean
//    public SpringLiquibase liquibase(DataSource dataSource) {
//        SpringLiquibase liquibase = new SpringLiquibase();
//        liquibase.setDropFirst(true);
//        liquibase.setDataSource(dataSource);
//        liquibase.setChangeLog(changelogString);
//        return liquibase;
//    }
//}
