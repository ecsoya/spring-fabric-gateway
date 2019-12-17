package io.github.ecsoya.fabric;

public class FabricQueryRequest<T> extends FabricRequest {

	/**
	 * Extract queried object with this type.
	 */
	public Class<T> type;

	public FabricQueryRequest(Class<T> type, String function, String... arguments) {
		super(function, arguments);
		this.type = type;
	}

}
