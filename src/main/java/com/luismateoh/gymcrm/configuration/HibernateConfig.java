package com.luismateoh.gymcrm.configuration;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "com.luismateoh.gymcrm.domain")
public class HibernateConfig {

    @Bean
    public SessionFactory sessionFactory() {
        return new MetadataSources(new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build())
                .buildMetadata()
                .buildSessionFactory();
    }

}