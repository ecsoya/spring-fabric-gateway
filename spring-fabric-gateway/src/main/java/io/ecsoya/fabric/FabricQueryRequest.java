package io.ecsoya.fabric;

public class FabricQueryRequest<T> extends FabricRequest {

	public Class<T> type;

	public FabricQueryRequest(Class<T> type, String function, String... arguments) {
		super(function, arguments);
		this.type = type;
	}

}
