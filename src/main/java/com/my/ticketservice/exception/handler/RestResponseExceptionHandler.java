package com.my.ticketservice.exception.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.my.ticketservice.exception.TicketServiceException;

import org.springframework.http.HttpStatus;

/**
 * Exception Handler. It handles exception coming from rest controllers.
 *
 */
@ControllerAdvice
public class RestResponseExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger(RestResponseExceptionHandler.class);

	static final String SERVER_ERROR = "Server Error";

	/**
	 * Handle TicketServiceException. It Should go before runtime exception
	 * handler.
	 * 
	 * @param ex
	 *            ticket service exception that was thrown by application
	 * @return message that will be sent to a service client. Message is being
	 *         taken from the exception. returns status 400 (Bad request)
	 */
	@ExceptionHandler(value = TicketServiceException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	protected String handleException(TicketServiceException ex) {
		logger.error("TicketServiceException  " + ex.getMessage(), ex);
		return ex.getMessage();
	}

	/**
	 * Handle RuntimeException. It Should go the last.
	 * 
	 * @param ex
	 *            runtime exception (not app custom exception) that was thrown
	 *            by application
	 * @return message that will be sent to a service client. Usually it is
	 *         something like: Server Error. returns status 500 (Internal Server Error)
	 */
	@ExceptionHandler(value = RuntimeException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	protected String handleException(RuntimeException ex) {
		logger.error("RuntimeException " + SERVER_ERROR, ex);
		return SERVER_ERROR;
	}

}
