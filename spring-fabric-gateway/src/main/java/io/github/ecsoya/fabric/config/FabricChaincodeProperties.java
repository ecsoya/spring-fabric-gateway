package io.github.ecsoya.fabric.config;

import io.github.ecsoya.fabric.gateway.FabricContract;

/**
 * Chaincode configuration properties.
 * 
 * @author ecsoya
 *
 */
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

	public String getIdentify() {
		return identify;
	}

	public void setIdentify(String identify) {
		this.identify = identify;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "FabricChaincodeProperties [identify=" + identify + ", version=" + version + ", name=" + name + "]";
	}

}
