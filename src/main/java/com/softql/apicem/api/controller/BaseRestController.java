package com.softql.apicem.api.controller;

import javax.inject.Inject;

import org.springframework.context.MessageSource;

public class BaseRestController {

	@Inject
	public MessageSource messageSource;
}
