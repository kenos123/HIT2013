package com.generalidevelopment.hit2013.util.json;

/**
 * Class for storing messages in <JSONResponseEnvelope>
 * 
 * @author t911552
 * 
 */
public class JSONMessage {
	private final int messageOrder;
	private final JSONMessageType messageType;
	private final JSONMessageFormat messageFormat;
	private final String message;
	private final String userMessage;
	private final String errorClassName;

	/**
	 * 
	 * @param messageOrder order of the message in messages stack
	 * @param messageType type of the message <JSONMessageType>
	 * @param errorClassName java class name of the occured error/exception
	 * @param messageFormat format of the message <JSONMessageFormat>
	 * @param message message text
	 * @param userMessage message text in user friendly format
	 */
	public JSONMessage(final int messageOrder, final JSONMessageType messageType, final String errorClassName,
			final JSONMessageFormat messageFormat, final String message, final String userMessage) {
		this.messageOrder = messageOrder;
		this.messageType = messageType;
		this.messageFormat = messageFormat;
		this.message = message;
		this.userMessage = userMessage;
		this.errorClassName = errorClassName;
	}

	public int getMessageOrder() {
		return messageOrder;
	}

	public JSONMessageType getMessageType() {
		return messageType;
	}

	public JSONMessageFormat getMessageFormat() {
		return messageFormat;
	}

	public String getMessage() {
		return message;
	}

	public String getUserMessage() {
		return userMessage;
	}

	public String getErrorClassName() {
		return errorClassName;
	}

}
