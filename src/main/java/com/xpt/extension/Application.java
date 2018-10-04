package com.xpt.extension;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
@EnableTransactionManagement(proxyTargetClass = true)
@ComponentScan(basePackages = "com.xpt.extension")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}