package com.generalidevelopment.hit2013.util.error;

public class ClassUtils {

	public static int getDepth(final String exception, final Throwable t) {
		return getDepth(exception, t.getClass(), 0);
	}

	public static int getDepth(final String exceptionMapping, final Class<?> exceptionClass, final int depth) {
		if (exceptionClass.getName().indexOf(exceptionMapping) != -1) {
			return depth;
		}
		if (exceptionClass.equals(Throwable.class)) {
			return -1;
		}
		return getDepth(exceptionMapping, exceptionClass.getSuperclass(), depth + 1);
	}

}
