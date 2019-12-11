package io.github.ecsoya.fabric.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;

public class FabricProperties {

	private String channel;

	private String[] organizations;

	private String name;

	private int peers;

	private FabricGatewayProperties gateway;

	protected FabricNetworkProperties network;

	private FabricChaincodeProperties chaincode;

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

	public FabricChaincodeProperties getChaincode() {
		if (chaincode == null) {
			chaincode = new FabricChaincodeProperties();
		}
		return chaincode;
	}

	public void setChaincode(FabricChaincodeProperties chaincode) {
		this.chaincode = chaincode;
	}

	public int getPeers() {
		return peers;
	}

	public void setPeers(int peers) {
		this.peers = peers;
	}

	@Override
	public String toString() {
		return "FabricProperties [chaincode=" + chaincode + ", channel=" + channel + ", organizations="
				+ Arrays.toString(organizations) + ", name=" + name + ", peers=" + peers + ", gateway=" + gateway
				+ ", network=" + network + "]";
	}

}
