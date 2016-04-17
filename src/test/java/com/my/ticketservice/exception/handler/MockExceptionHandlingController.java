package com.my.ticketservice.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.my.ticketservice.exception.TicketServiceException;

@Controller
@RequestMapping("/api")
public class MockExceptionHandlingController {
	static final String EXCEPTION_MESSAGE = "some exception message";

	@RequestMapping("/runtimeException")
	@ResponseStatus(value = HttpStatus.OK)
	public void throwRuntimeException() {
		throw new RuntimeException();
	}

	@RequestMapping("/ticketServiceException")
	@ResponseStatus(value = HttpStatus.OK)
	public void throwMediaStorageServiceException() {
		throw new TicketServiceException("new exception");
	}

}
