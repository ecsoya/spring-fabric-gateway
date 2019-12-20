package io.github.ecsoya.fabric.config;

import io.github.ecsoya.fabric.gateway.FabricContract;
import lombok.Data;

/**
 * Chaincode configuration properties.
 * 
 * @author ecsoya
 *
 */
@Data
public class FabricChaincodeProperties {

	/**
	 * The identify (name) of the chaincode, required.
	 * 
	 * The org.hyperledger.fabric.gateway.Contract and {@link FabricContract} would
	 * use this property to create the chaincode instance.
	 */
	private String identify;

	/**
	 * The version of the chaincode, optionally. (This will be displayed at the
	 * explorer)
	 */
	private String version;

	/**
	 * The display name of the chaincode, optionally. (This will be displayed at the
	 * explorer)
	 */
	private String name;

	/**
	 * Custom chaincode function names.
	 */
	private FabricChaincodeFunctionProperties functions = new FabricChaincodeFunctionProperties();

}
