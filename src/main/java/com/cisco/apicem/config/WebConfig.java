package com.cisco.apicem.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.web.config.SpringDataWebConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;

import com.cisco.apicem.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableWebMvc
@ComponentScan(basePackageClasses = { Constants.class }, useDefaultFilters = false, includeFilters = { @Filter(type = FilterType.ANNOTATION, value = {
		Controller.class, RestController.class, ControllerAdvice.class }) })
public class WebConfig extends SpringDataWebConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("css/", "images/", "lib/", "swagger-ui.js")//
				.addResourceLocations("classpath:META-INF/resources/")//
				.setCachePeriod(0);

		registry.addResourceHandler("webjars/**").addResourceLocations("classpath:META-INF/resources/webjars/");
	}

	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		registry.jsp("classpath:/resources/", ".jsp");
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("forward:/index.html");
	}

	/*
	 * @Override public void
	 * configureHandlerExceptionResolvers(List<HandlerExceptionResolver>
	 * exceptionResolvers) {
	 * exceptionResolvers.add(exceptionHandlerExceptionResolver()); }
	 */

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		configurer.favorParameter(false);
		configurer.favorPathExtension(false);
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		List<HttpMessageConverter<?>> messageConverters = messageConverters();
		converters.addAll(messageConverters);
	}

	/*
	 * @Bean public ExceptionHandlerExceptionResolver
	 * exceptionHandlerExceptionResolver() { ExceptionHandlerExceptionResolver
	 * exceptionHandlerExceptionResolver = new
	 * ExceptionHandlerExceptionResolver();
	 * exceptionHandlerExceptionResolver.setMessageConverters
	 * (messageConverters()); return exceptionHandlerExceptionResolver; }
	 */

	private List<HttpMessageConverter<?>> messageConverters() {
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();

		MappingJackson2HttpMessageConverter jackson2Converter = new MappingJackson2HttpMessageConverter();
		jackson2Converter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON));
		jackson2Converter.setObjectMapper(objectMapper);

		messageConverters.add(jackson2Converter);
		return messageConverters;
	}

}
