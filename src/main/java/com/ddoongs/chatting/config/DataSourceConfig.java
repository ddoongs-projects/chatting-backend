package com.ddoongs.chatting.config;

import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfig {

  @ConfigurationProperties(prefix = "spring.datasource.hikari")
  @Bean(name = "dataSource")
  public DataSource getDataSource() {
    return DataSourceBuilder.create()
        .type(HikariDataSource.class)
        .build();
  }
}
