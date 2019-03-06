package edu.osumc.bmi.oauth2.core;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import edu.osumc.bmi.oauth2.core.properties.BMIOAuth2Properties;

// @Configuration
// @EnableTransactionManagement
public class PersistenceJPAConfig {

  @Autowired
  private BMIOAuth2Properties properties;

  @Bean
  @ConditionalOnMissingBean
  public DataSource dataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName(properties.getPersistence().getDriver());
    dataSource.setUrl(properties.getPersistence().getUrl());
    dataSource.setUsername(properties.getPersistence().getUsername());
    dataSource.setPassword(properties.getPersistence().getPassword());

    return dataSource;
  }

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnBean(name = "dataSource")
  public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
    LocalContainerEntityManagerFactoryBean entityManager =
        new LocalContainerEntityManagerFactoryBean();

    entityManager.setDataSource(dataSource());
    entityManager.setPackagesToScan("edu.osumc.bmi.oauth2.core.domain");
    entityManager.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
    entityManager.setJpaProperties(additionalProperties());
    return entityManager;
  }

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnBean(name = "entityManagerFactory")
  public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {

    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(emf);
    return transactionManager;
  }

  @Bean
  public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
    return new PersistenceExceptionTranslationPostProcessor();
  }

  private Properties additionalProperties() {
    Properties properties = new Properties();
    properties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
    properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");

    return properties;
  }
}
