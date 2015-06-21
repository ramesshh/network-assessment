package com.softql.apicem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.Mongo;
import com.softql.apicem.Constants;

@Configuration
@ComponentScan(basePackageClasses = { Constants.class }, excludeFilters = { @Filter(type = FilterType.ANNOTATION, value = {
		RestController.class, ControllerAdvice.class, Configuration.class }) })
@PropertySource("classpath:/app.properties")
@PropertySource(value = "classpath:/database.properties", ignoreResourceNotFound = true)
public class AppConfig {

	public @Bean Mongo mongo() throws Exception {
		return new Mongo("localhost");
	}

	public @Bean MongoTemplate mongoTemplate() throws Exception {
		return new MongoTemplate(mongo(), "apicDB");
	}

}
