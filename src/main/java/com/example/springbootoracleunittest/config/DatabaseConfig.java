package com.example.springbootoracleunittest.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "com.example.springbootoracleunittest.repository")
@EnableTransactionManagement
public class DatabaseConfig {
    
    // Esta clase puede ser extendida para configuraciones específicas de base de datos
    // como configuraciones de procedimientos almacenados, conexiones personalizadas, etc.
    
}
