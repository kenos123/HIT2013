package com.generalidevelopment.hit2013.util.error;

/**
 * An interface for handling exceptions occurring in an application.
 * 
 * 
 */
public interface ErrorHandler {

	/**
	 * Method handles a specific exception according to rules defined in Configuration object.
	 * 
	 * @param exception
	 */
	public void handleException(Throwable t);

}
