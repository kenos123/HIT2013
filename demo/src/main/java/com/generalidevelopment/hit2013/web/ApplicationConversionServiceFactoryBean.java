package com.generalidevelopment.hit2013.web;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;

import com.generalidevelopment.hit2013.Item;

@Configurable
/**
 * A central place to register application converters and formatters. 
 */
public class ApplicationConversionServiceFactoryBean extends FormattingConversionServiceFactoryBean {

	@Override
	protected void installFormatters(final FormatterRegistry registry) {
		super.installFormatters(registry);
		// Register application converters and formatters
	}

	public Converter<Item, String> getItemToStringConverter() {
		return new org.springframework.core.convert.converter.Converter<com.generalidevelopment.hit2013.Item, java.lang.String>() {
			@Override
			public String convert(final Item item) {
				return new StringBuilder().append(item.getName()).append(' ').append(item.getPrice()).toString();
			}
		};
	}

	public Converter<Long, Item> getIdToItemConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.generalidevelopment.hit2013.Item>() {
			@Override
			public com.generalidevelopment.hit2013.Item convert(final java.lang.Long id) {
				return Item.findItem(id);
			}
		};
	}

	public Converter<String, Item> getStringToItemConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.String, com.generalidevelopment.hit2013.Item>() {
			@Override
			public com.generalidevelopment.hit2013.Item convert(final String id) {
				return getObject().convert(getObject().convert(id, Long.class), Item.class);
			}
		};
	}

	public void installLabelConverters(final FormatterRegistry registry) {
		registry.addConverter(getItemToStringConverter());
		registry.addConverter(getIdToItemConverter());
		registry.addConverter(getStringToItemConverter());
	}

	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		installLabelConverters(getObject());
	}
}
