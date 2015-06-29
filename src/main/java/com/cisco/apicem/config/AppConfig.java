package com.cisco.apicem.config;

import java.net.UnknownHostException;

import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;

import com.cisco.apicem.Constants;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;

@Configuration
@ComponentScan(basePackageClasses = { Constants.class }, excludeFilters = { @Filter(type = FilterType.ANNOTATION, value = {
		RestController.class, ControllerAdvice.class, Configuration.class }) })
public class AppConfig {

	@Bean
	public Jongo jongo() {
		DB db;
		try {
			db = new MongoClient("localhost", 27017).getDB("apicDB");
		} catch (UnknownHostException e) {
			throw new MongoException("Connection error : ", e);
		}
		return new Jongo(db);
	}

	@Bean
	public MongoCollection users() {
		return jongo().getCollection("user");
	}
}
