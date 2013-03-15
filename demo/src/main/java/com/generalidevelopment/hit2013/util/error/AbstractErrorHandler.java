package com.generalidevelopment.hit2013.util.error;

import org.apache.log4j.Logger;

public class AbstractErrorHandler implements ErrorHandler {

	public final Logger logger = Logger.getLogger(getClass());

	@Override
	public void handleException(final Throwable t) {
	}

}
