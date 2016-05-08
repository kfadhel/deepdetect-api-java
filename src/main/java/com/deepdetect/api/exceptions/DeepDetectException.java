package com.deepdetect.api.exceptions;

/** class for exceptions thrown by DeepDetect API. */
public class DeepDetectException extends Exception {

	private static final long serialVersionUID = 8989203948485235452L;

	public DeepDetectException(String message) {
	  	super(message, null);
	  }

	  public DeepDetectException(String message, Throwable e) {
	  	super(message, e);
	  }
}
