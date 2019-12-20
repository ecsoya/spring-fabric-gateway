package io.github.ecsoya.fabric.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import lombok.Data;

/**
 * The root configuration of the fabric and network.
 * 
 * @author ecsoya
 *
 * @see FabricContext
 */
@Data
public class FabricProperties {

	/**
	 * The channel name of fabric, required.
	 */
	private String channel;

	/**
	 * The organizations of fabric.
	 */
	private String[] organizations;

	/**
	 * The name of the fabric.
	 */
	private String name;

	/**
	 * The count of all peers.
	 */
	private int peers;

	/**
	 * The gateway configuration of fabric.
	 */
	private FabricGatewayProperties gateway = new FabricGatewayProperties();

	/**
	 * The network configuration of fabric.
	 */
	protected FabricNetworkProperties network = new FabricNetworkProperties();

	/**
	 * The chaincode configuration of fabric.
	 */
	private FabricChaincodeProperties chaincode = new FabricChaincodeProperties();

	/**
	 * @return Load the contents of the network.
	 */
	public InputStream getNetworkContents() {
		if (network == null) {
			return null;
		}
		String file = network.getFile();
		if (file == null || file.equals("")) {
			return null;
		}
		File localFile = new File(file);
		if (!localFile.exists()) {
			return null;
		}
		try {
			return new FileInputStream(localFile);
		} catch (FileNotFoundException e) {
			return null;
		}
	}

}
