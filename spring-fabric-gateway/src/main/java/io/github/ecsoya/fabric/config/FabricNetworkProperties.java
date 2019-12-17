package io.github.ecsoya.fabric.config;

import org.hyperledger.fabric.sdk.NetworkConfig;

/**
 * Fabric network configurations.
 * 
 * @author ecsoya
 * @see NetworkConfig
 */
public class FabricNetworkProperties {

	/**
	 * The name of the network.
	 */
	private String name;

	/**
	 * The network config file, required.
	 * 
	 * @see NetworkConfig
	 */
	private String file;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	@Override
	public String toString() {
		return "FabricNetworkProperties [name=" + name + ", file=" + file + "]";
	}

}
