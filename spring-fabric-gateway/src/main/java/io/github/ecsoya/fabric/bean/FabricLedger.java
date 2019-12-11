package io.github.ecsoya.fabric.bean;

import lombok.Data;

/**
 * Fabric blockchain info.
 * 
 * @author ecsoya
 *
 */
@Data
public class FabricLedger {

	private long height;

	private String currentHash;

	private String previousHash;

	private String channel;

	private String[] orgs = {};

	private String name = "Fabric";

}
