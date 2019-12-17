package io.github.ecsoya.fabric;

public class FabricRequest {

	/**
	 * Chaincode function name.
	 */
	public String function;

	/**
	 * Chaincode function arguments.
	 */
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
