package io.endeavour.stocks.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    /**
     * Method to create a DataSource representing the stocks DB
     * The prefix given spring.datasource will tell Spring to pickup for corresponding parameters from the applications.properties file
     * @return DataSource
     */
    @Bean(name = "dataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource getDataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "dataSourceCrud")
    @ConfigurationProperties(prefix = "spring.datasource-crudjpa")
    public DataSource getDataSourceCrud(){
        return DataSourceBuilder.create().build();
    }

}
