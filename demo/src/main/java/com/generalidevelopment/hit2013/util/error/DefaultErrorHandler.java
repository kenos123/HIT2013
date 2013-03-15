package com.generalidevelopment.hit2013.util.error;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A default implementation of ErrorHandler interface. Sends an email notification containing exception's stack trace
 * for all exceptions specified in Configuration object.
 * 
 * @author T921647
 * 
 */
public class DefaultErrorHandler extends AbstractErrorHandler {

	private final Map<String, Log> loggerMap = new HashMap<String, Log>();

	@Override
	public void handleException(final Throwable t) {
		int deepest = Integer.MAX_VALUE;
		for (final Iterator<String> iterator = loggerMap.keySet().iterator(); iterator.hasNext();) {
			final String exceptionMapping = iterator.next();
			final int depth = ClassUtils.getDepth(exceptionMapping, t);
			if (depth >= 0 && depth < deepest) {
				deepest = depth;
				loggerMap.get(exceptionMapping).error(t.getMessage(), t);
			}
		}
	}

	public void setLoggerMap(final Map<String, String> loggerMap) {
		for (final Entry<String, String> entry : loggerMap.entrySet()) {
			this.loggerMap.put(entry.getKey(), LogFactory.getLog(entry.getValue()));
		}
	}

}
