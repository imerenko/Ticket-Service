package com.my.ticketservice.exception;

/**
 * Ticket Service Exception. It extends Runtime Exception so it is an unchecked exception.
 * I personally prefer do not use checked exceptions for clean code sake.
 * But I am not against checked exception. 
 *
 */
public class TicketServiceException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	/**
     * Creates a new <code>TicketServiceException</code> instance.
     * 
     * @param message
     *            the message to display
     */
    public TicketServiceException(String message) {
        super(message);
    }

    /**
     * Creates a new <code>TicketServiceException</code> instance.
     * 
     * @param message
     *            the message to display
     * @param ex
     *            the chained exception
     */
    public TicketServiceException(String message, Throwable ex) {
        super(message, ex);
    }

}
