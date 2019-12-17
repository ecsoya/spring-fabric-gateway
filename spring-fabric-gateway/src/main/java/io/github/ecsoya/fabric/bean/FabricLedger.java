package io.github.ecsoya.fabric.bean;

import lombok.Data;

/**
 * The info of Fabric blockchain network.
 * 
 * @author ecsoya
 *
 */
@Data
public class FabricLedger {

	/**
	 * Total block height of current fabric blockchain network.
	 */
	private long height;

	/**
	 * The latest transaction Hash.
	 */
	private String currentHash;

	/**
	 * The previous transaction Hash.
	 */
	private String previousHash;

	/**
	 * The channel of current client. (@see FabricProperties)
	 */
	private String channel;

	/**
	 * The organizations of current fabric blockchain network. (@see
	 * FabricProperties)
	 */
	private String[] orgs = {};

	/**
	 * The name of current fabric blockchain network. (@see FabricProperties)
	 */
	private String name = "Fabric";

	/**
	 * The display name of the chaincode. (@see FabricProperties)
	 */
	private String chaincodeName;

	/**
	 * The identify of the chaincode. (@see FabricProperties)
	 */
	private String chaincode;

	/**
	 * The count of all peers. (@see FabricProperties)
	 */
	private int peers;

}
