package io.ecsoya.fabric;

public class FabricException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6755525873084547174L;

	public FabricException() {
	}

	public FabricException(String message) {
		super(message);
	}

	public FabricException(Throwable cause) {
		super(cause);
	}

	public FabricException(String message, Throwable cause) {
		super(message, cause);
	}

	public FabricException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
