package com.cisco.apicem;

import java.net.UnknownHostException;

import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;

@SpringBootApplication
public class Application extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
	}

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
		return jongo().getCollection("users");
	}

}
