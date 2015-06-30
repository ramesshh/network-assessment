package com.cisco.apicem.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;

import com.cisco.apicem.Constants;

@Configuration
@ComponentScan(basePackageClasses = { Constants.class }, excludeFilters = { @Filter(type = FilterType.ANNOTATION, value = {
		RestController.class, ControllerAdvice.class, Configuration.class }) })
public class AppConfig {

}
