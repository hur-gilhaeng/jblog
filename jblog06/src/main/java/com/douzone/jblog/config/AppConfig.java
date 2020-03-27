package com.douzone.jblog.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.douzone.jblog.config.app.DBConfig;
import com.douzone.jblog.config.app.MyBatisConfig;

@Configuration
@ComponentScan({"com.douzone.jblog.service",
				"com.douzone.jblog.repository"})
@Import({DBConfig.class, MyBatisConfig.class})
public class AppConfig {
}
