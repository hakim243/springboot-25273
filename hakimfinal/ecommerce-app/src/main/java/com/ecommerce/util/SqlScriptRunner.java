package com.ecommerce.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

// Temporarily disable this component
//@Component
public class SqlScriptRunner {

    private static final Logger logger = LoggerFactory.getLogger(SqlScriptRunner.class);
    
    private final JdbcTemplate jdbcTemplate;
    
    public SqlScriptRunner(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    @PostConstruct
    public void runSqlScripts() {
        // Do nothing - we're using AdminUserCreator instead
        logger.info("SQL scripts execution disabled. Using AdminUserCreator instead.");
    }
    
    private void executeSqlScript(String scriptPath) {
        try {
            String sqlScript = readSqlScript(scriptPath);
            
            if (sqlScript != null && !sqlScript.isEmpty()) {
                // Split the script by semicolons to execute each statement separately
                String[] statements = sqlScript.split(";");
                
                for (String statement : statements) {
                    if (!statement.trim().isEmpty()) {
                        try {
                            jdbcTemplate.execute(statement);
                        } catch (Exception e) {
                            logger.error("Error executing statement in script {}: {}", scriptPath, e.getMessage());
                            // Continue with the next statement
                        }
                    }
                }
                
                logger.info("Successfully executed SQL script: {}", scriptPath);
            }
        } catch (Exception e) {
            logger.error("Error processing SQL script {}: {}", scriptPath, e.getMessage(), e);
        }
    }
    
    private String readSqlScript(String scriptPath) {
        try (InputStream is = new ClassPathResource(scriptPath).getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            logger.error("Error reading SQL script {}: {}", scriptPath, e.getMessage(), e);
            return null;
        }
    }
} 