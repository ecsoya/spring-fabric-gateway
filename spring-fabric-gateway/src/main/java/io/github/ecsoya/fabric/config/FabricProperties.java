package io.github.ecsoya.fabric.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class FabricProperties {

	private String chaincode;

	private String channel;

	private String[] organizations;

	private String name;

	private FabricGatewayProperties gateway;

	protected FabricNetworkProperties network;

	public String getChaincode() {
		return chaincode;
	}

	public void setChaincode(String chaincode) {
		this.chaincode = chaincode;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String[] getOrganizations() {
		return organizations;
	}

	public void setOrganizations(String[] organizations) {
		this.organizations = organizations;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public FabricNetworkProperties getNetwork() {
		if (network == null) {
			network = new FabricNetworkProperties();
		}
		return network;
	}

	public void setNetwork(FabricNetworkProperties network) {
		this.network = network;
	}

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

	public FabricGatewayProperties getGateway() {
		if (gateway == null) {
			gateway = new FabricGatewayProperties();
		}
		return gateway;
	}

	public void setGateway(FabricGatewayProperties gateway) {
		this.gateway = gateway;
	}
}
