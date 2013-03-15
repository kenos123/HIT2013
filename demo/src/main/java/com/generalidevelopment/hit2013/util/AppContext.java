package com.generalidevelopment.hit2013.util;

import java.util.Locale;

import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;

public class AppContext {

	private static ApplicationContext ctx;

	/**
	 * Injected from the class "ApplicationContextProvider" which is automatically loaded during Spring-Initialization.
	 */

	public static void setApplicationContext(final ApplicationContext applicationContext) {
		ctx = applicationContext;
	}

	/**
	 * Get access to the Spring ApplicationContext from everywhere in your Application
	 * 
	 * @return
	 */

	public static ApplicationContext getApplicationContext() {
		return ctx;
	}

	public static String getMsg(final String string, final Object... args) {
		final Locale locale = LocaleContextHolder.getLocale();
		if (getApplicationContext() == null) {
			return string;
		} else {
			return getApplicationContext().getMessage(string, args, locale);
		}
	}
}