package io.ecsoya.fabric;

public class FabricRequest {

	public String function;
	public String[] arguments;

	public FabricRequest(String function, String... arguments) {
		this.function = function;
		this.arguments = arguments;
	}

	public void checkValidate() throws FabricException {
		if (function == null || function.equals("")) {
			throw new FabricException("The executable function name is empty.");
		}
	}
}
