package com.softql.apicem.api;

import java.rmi.server.ExportException;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.softql.apicem.ApiErrors;
import com.softql.apicem.exception.InvalidIpAddressException;
import com.softql.apicem.exception.InvalidRequestException;
import com.softql.apicem.exception.ResourceNotFoundException;
import com.softql.apicem.exception.SearchException;
import com.softql.apicem.exception.UsernameExistedException;
import com.softql.apicem.model.ResponseMessage;

/**
 * Called when an exception occurs during request processing. Transforms
 * exception message into JSON format.
 */
@ControllerAdvice(annotations = RestController.class)
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(RestExceptionHandler.class);

	@Inject
	private MessageSource messageSource;

	@ExceptionHandler(value = { Exception.class })
	@ResponseBody
	public ResponseEntity<ResponseMessage> handleAuthenticationException(Exception ex, WebRequest request) {
		if (log.isDebugEnabled()) {
			log.debug("handling exception...");
		}
		ResponseMessage alert = new ResponseMessage(ResponseMessage.Type.danger, ApiErrors.GENERIC_ERROR,
				messageSource.getMessage(ApiErrors.GENERIC_ERROR, new String[] {}, null));

		return new ResponseEntity<>(alert, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(value = { ResourceNotFoundException.class })
	@ResponseBody
	public ResponseEntity<ResponseMessage> handleResourceNotFoundException(ResourceNotFoundException ex,
			WebRequest request) {
		if (log.isDebugEnabled()) {
			log.debug("handling ResourceNotFoundException...");
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = { UsernameExistedException.class })
	@ResponseBody
	public ResponseEntity<ResponseMessage> handleUsernameExistedException(UsernameExistedException ex,
			WebRequest request) {
		if (log.isDebugEnabled()) {
			log.debug("handling UsernameExistedException...");
		}

		ResponseMessage error = ResponseMessage.danger("username existed.");
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = { InvalidIpAddressException.class })
	@ResponseBody
	public ResponseEntity<ResponseMessage> handleInvalidIpAddressException(InvalidIpAddressException ex,
			WebRequest request) {
		if (log.isDebugEnabled()) {
			log.debug("handling InvalidIpAddressException...");
		}

		ResponseMessage alert = new ResponseMessage(ResponseMessage.Type.danger, ApiErrors.INVALID_IP,
				messageSource.getMessage(ApiErrors.INVALID_IP, new String[] {}, null));

		return new ResponseEntity<>(alert, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = { SearchException.class })
	@ResponseBody
	public ResponseEntity<ResponseMessage> handleSearchException(SearchException ex, WebRequest request) {
		if (log.isDebugEnabled()) {
			log.debug("handling InvalidSearchException...");
		}

		ResponseMessage alert = new ResponseMessage(ResponseMessage.Type.danger, ApiErrors.CONNECT_ERROR,
				messageSource.getMessage(ApiErrors.CONNECT_ERROR, new String[] {}, null));

		return new ResponseEntity<>(alert, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(value = { ExportException.class })
	@ResponseBody
	public ResponseEntity<ResponseMessage> handleExportException(ExportException ex, WebRequest request) {
		if (log.isDebugEnabled()) {
			log.debug("handling ExportException...");
		}

		ResponseMessage alert = new ResponseMessage(ResponseMessage.Type.danger, ApiErrors.EXPORT_ERROR,
				messageSource.getMessage(ApiErrors.EXPORT_ERROR, new String[] {}, null));

		return new ResponseEntity<>(alert, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(value = { InvalidRequestException.class })
	public ResponseEntity<ResponseMessage> handleInvalidRequestException(InvalidRequestException ex, WebRequest req) {
		if (log.isDebugEnabled()) {
			log.debug("handling InvalidRequestException...");
		}

		ResponseMessage alert = new ResponseMessage(ResponseMessage.Type.danger, ApiErrors.INVALID_REQUEST,
				messageSource.getMessage(ApiErrors.INVALID_REQUEST, new String[] {}, null));

		BindingResult result = ex.getErrors();

		List<FieldError> fieldErrors = result.getFieldErrors();

		if (!fieldErrors.isEmpty()) {
			fieldErrors.stream().forEach((e) -> {
				alert.addError(e.getField(), e.getCode(), e.getDefaultMessage());
			});
		}

		return new ResponseEntity<>(alert, HttpStatus.UNPROCESSABLE_ENTITY);
	}

}