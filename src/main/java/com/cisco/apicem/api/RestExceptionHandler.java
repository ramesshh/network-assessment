package com.cisco.apicem.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Called when an exception occurs during request processing. Transforms
 * exception message into JSON format.
 */
@ControllerAdvice(annotations = RestController.class)
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(RestExceptionHandler.class);

	@Autowired
	private MessageSource messageSource;

	/*
	 * @ExceptionHandler(value = {Exception.class})
	 * 
	 * @ResponseBody public ResponseEntity<ResponseMessage>
	 * handleAuthenticationException(Exception ex, WebRequest request) { if
	 * (log.isDebugEnabled()) { log.debug("handling exception..."); } return new
	 * ResponseEntity<>(new ResponseMessage(ResponseMessage.Type.danger,
	 * ex.getMessage()), HttpStatus.BAD_REQUEST); }
	 * 
	 * @ExceptionHandler(value = {ResourceNotFoundException.class})
	 * 
	 * @ResponseBody public ResponseEntity<ResponseMessage>
	 * handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest
	 * request) { if (log.isDebugEnabled()) {
	 * log.debug("handling ResourceNotFoundException..."); } return new
	 * ResponseEntity<>(HttpStatus.NOT_FOUND); }
	 * 
	 * @ExceptionHandler(value = {ResourceNotFoundException.class})
	 * 
	 * @ResponseBody public ResponseEntity<ResponseMessage>
	 * handleResourceNotFoundException(UsernameExistedException ex, WebRequest
	 * request) { if (log.isDebugEnabled()) {
	 * log.debug("handling UsernameExistedException..."); }
	 * 
	 * ResponseMessage error = ResponseMessage.danger("username existed.");
	 * return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST); }
	 * 
	 * @ExceptionHandler(value = {InvalidRequestException.class}) public
	 * ResponseEntity<ResponseMessage>
	 * handleInvalidRequestException(InvalidRequestException ex, WebRequest req)
	 * { if (log.isDebugEnabled()) {
	 * log.debug("handling InvalidRequestException..."); }
	 * 
	 * ResponseMessage alert = new ResponseMessage( ResponseMessage.Type.danger,
	 * ApiErrors.INVALID_REQUEST,
	 * messageSource.getMessage(ApiErrors.INVALID_REQUEST, new String[]{},
	 * null));
	 * 
	 * BindingResult result = ex.getErrors();
	 * 
	 * List<FieldError> fieldErrors = result.getFieldErrors();
	 * 
	 * if (!fieldErrors.isEmpty()) { fieldErrors.stream().forEach((e) -> {
	 * alert.addError(e.getField(), e.getCode(), e.getDefaultMessage()); }); }
	 * 
	 * return new ResponseEntity<>(alert, HttpStatus.UNPROCESSABLE_ENTITY); }
	 */

}
