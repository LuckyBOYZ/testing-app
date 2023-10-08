package com.sumalukasz.config;

import org.springframework.test.context.jdbc.Sql;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Sql(scripts = "classpath:sql/default_data_for_all_tables.sql")
public @interface MigrateDatabase {
}
