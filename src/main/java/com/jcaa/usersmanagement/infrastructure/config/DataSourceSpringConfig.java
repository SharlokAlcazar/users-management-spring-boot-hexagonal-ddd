package com.jcaa.usersmanagement.infrastructure.config;

import com.jcaa.usersmanagement.infrastructure.adapter.persistence.config.DatabaseConfig;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.config.PostgresDatabaseConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration(proxyBeanMethods = false)
public class DataSourceSpringConfig {

  private static final String ENGINE_POSTGRES = "postgres";

  @Value("${db.engine:mysql}")     private String dbEngine;
  @Value("${db.host}")             private String dbHost;
  @Value("${db.port}")             private int dbPort;
  @Value("${db.name}")             private String dbName;
  @Value("${db.username}")         private String dbUsername;
  @Value("${db.password}")         private String dbPassword;
  @Value("${db.sslmode:disable}")  private String dbSslMode;

  @Bean
  public DataSource dataSource() {
    final HikariConfig hikariConfig = new HikariConfig();
    hikariConfig.setJdbcUrl(buildJdbcUrl());
    hikariConfig.setUsername(dbUsername);
    hikariConfig.setPassword(dbPassword);
    hikariConfig.setMaximumPoolSize(5);
    hikariConfig.setMinimumIdle(1);
    hikariConfig.setConnectionTimeout(30_000);

    log.info("[DataSourceSpringConfig] engine={} host={} port={}", dbEngine, dbHost, dbPort);
    return new HikariDataSource(hikariConfig);
  }

  private String buildJdbcUrl() {
    if (ENGINE_POSTGRES.equalsIgnoreCase(dbEngine)) {
      return new PostgresDatabaseConfig(dbHost, dbPort, dbName, dbUsername, dbPassword, dbSslMode)
          .buildJdbcUrl();
    }
    return new DatabaseConfig(dbHost, dbPort, dbName, dbUsername, dbPassword).buildJdbcUrl();
  }
}