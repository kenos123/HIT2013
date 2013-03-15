package com.generalidevelopment.hit2013.util.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

/**
 * Class used for returning all object from contrller layer to client in JSON format contains two parts - messages
 * List<JSONMessage> and data - cargo object. If exception occurs, data object is always null. Otherwise it depends on
 * implementation or actual data passed to the object.
 * 
 * @author t911552
 * 
 */
public class JSONResponseEnvelope {

	private JSONResponseStatus responseStatus;
	private int statusCode;
	private List<JSONMessage> messages = new ArrayList<JSONMessage>();
	private Map<String, Object> additionalInfo = new HashMap<String, Object>();

	private Object data;

	public JSONResponseStatus getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(final JSONResponseStatus msgStatus) {
		responseStatus = msgStatus;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(final int statusCode) {
		this.statusCode = statusCode;
	}

	public List<JSONMessage> getMessages() {
		return messages;
	}

	public void setMessages(final List<JSONMessage> messages) {
		this.messages = messages;
	}

	public Object getData() {
		return data;
	}

	public void setData(final Object data) {
		this.data = data;
	}

	public void addAdditionalInfo(final String key, final Object value) {
		additionalInfo.put(key, value);
	}

	public Map<String, Object> getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(final Map<String, Object> additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	/**
	 * Creates simple response of JSONResponseType.SUCCESS, with one JSONMessageType.INFO and optional user message text
	 * 
	 * @param userMessage
	 */
	public static JSONResponseEnvelope createSuccessResponse() {
		final JSONResponseEnvelope envelope = new JSONResponseEnvelope();
		envelope.data = null;
		envelope.statusCode = 200;
		envelope.responseStatus = JSONResponseStatus.SUCCESS;
		envelope.messages.add(new JSONMessage(1, JSONMessageType.INFO, "", JSONMessageFormat.TEXT, "Success", null));
		return envelope;
	}

	/**
	 * Generates simple response of JSONResponseType.SUCCESS, with one JSONMessageType.INFO and text Success
	 * 
	 * @param data
	 */
	public static JSONResponseEnvelope createSuccessResponse(final Object data) {
		final JSONResponseEnvelope envelope = new JSONResponseEnvelope();
		envelope.data = data;
		envelope.statusCode = 200;
		envelope.responseStatus = JSONResponseStatus.SUCCESS;
		envelope.messages.add(new JSONMessage(1, JSONMessageType.INFO, "", JSONMessageFormat.TEXT, "Success", null));
		return envelope;
	}

	/**
	 * Creates simple response of JSONResponseType.SUCCESS, with one JSONMessageType.INFO and optional user message text
	 * 
	 * @param data
	 * @param userMessage
	 */
	public static JSONResponseEnvelope createSuccessResponse(final Object data, final String userMessage) {
		final JSONResponseEnvelope envelope = new JSONResponseEnvelope();
		envelope.data = data;
		envelope.statusCode = 200;
		envelope.responseStatus = JSONResponseStatus.SUCCESS;
		envelope.messages.add(new JSONMessage(1, JSONMessageType.INFO, "", JSONMessageFormat.TEXT, "Success",
				userMessage));
		return envelope;
	}

	/**
	 * Creates simple response of JSONResponseType.SUCCESS, with one JSONMessageType.INFO and optional user message text
	 * 
	 * @param userMessage
	 */
	public static JSONResponseEnvelope createSuccessResponseMsg(final String... userMessages) {
		final JSONResponseEnvelope envelope = new JSONResponseEnvelope();
		envelope.data = null;
		envelope.statusCode = 200;
		envelope.responseStatus = JSONResponseStatus.SUCCESS;
		int i = 0;
		for (final String userMessage : userMessages) {
			i++;
			envelope.messages.add(new JSONMessage(i, JSONMessageType.INFO, "", JSONMessageFormat.TEXT, "Success",
					userMessage));
		}
		return envelope;
	}

	/**
	 * Creates simple response of JSONResponseType.ERROR, with one JSONMessageType.ERROR and optional user meessage text
	 * 
	 * @param data
	 * @param userMessage
	 */
	public static JSONResponseEnvelope createFailureResponse(final Object data, final String userMessage) {
		final JSONResponseEnvelope envelope = new JSONResponseEnvelope();
		envelope.data = data;
		envelope.statusCode = 500;
		envelope.responseStatus = JSONResponseStatus.ERROR;
		envelope.messages.add(new JSONMessage(1, JSONMessageType.ERROR, "", JSONMessageFormat.TEXT, "Failure",
				userMessage));
		return envelope;
	}

	/**
	 * Creates simple response of JSONResponseType.ERROR, with one JSONMessageType.ERROR and text Failure text
	 * 
	 * @param data
	 */
	public static JSONResponseEnvelope createFailureResponse(final Object data) {
		final JSONResponseEnvelope envelope = new JSONResponseEnvelope();
		envelope.data = data;
		envelope.statusCode = 500;
		envelope.responseStatus = JSONResponseStatus.ERROR;
		envelope.messages.add(new JSONMessage(1, JSONMessageType.ERROR, "", JSONMessageFormat.TEXT, "Failure",
				"Failure"));
		return envelope;
	}

	/**
	 * Creates response of JSONResponseType.ERROR, with one JSONMessageType.ERROR and text Failure text
	 * 
	 * @param bindingResult
	 */
	public static JSONResponseEnvelope createFailureResponse(final BindingResult bindingResult) {
		final JSONResponseEnvelope envelope = new JSONResponseEnvelope();
		envelope.statusCode = 400;
		envelope.responseStatus = JSONResponseStatus.ERROR;

		final List<FieldError> fieldErrors = bindingResult.getFieldErrors();
		int id = 1;
		for (final FieldError fieldError : fieldErrors) {
			final String userMessage = fieldError.getDefaultMessage();
			envelope.messages.add(new JSONMessage(id, JSONMessageType.ERROR, "", JSONMessageFormat.TEXT,
					"Binding error", userMessage));
			id++;
		}
		return envelope;
	}

	/**
	 * Creates response of JSONResponseType.ERROR, with one JSONMessageType.ERROR and text Failure text
	 * 
	 * @param List<String>
	 */
	public static JSONResponseEnvelope createFailureResponse(final List<String> errorMessages) {
		final JSONResponseEnvelope envelope = new JSONResponseEnvelope();
		envelope.statusCode = 400;
		envelope.responseStatus = JSONResponseStatus.ERROR;
		int id = 1;
		for (final String errorMessage : errorMessages) {
			envelope.messages.add(new JSONMessage(id, JSONMessageType.ERROR, "", JSONMessageFormat.TEXT,
					"Binding error", errorMessage));
			id++;
		}
		return envelope;
	}

}
