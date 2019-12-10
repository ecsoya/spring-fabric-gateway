package io.ecsoya.fabric.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class FabricProperties {

	private String chaincode;

	private String channel;

	private String[] organizations;

	private String name;

	private FabricWalletProperties wallet;

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

	public FabricWalletProperties getWallet() {
		if (wallet == null) {
			wallet = new FabricWalletProperties();
			wallet.setIdentify("admin");
			wallet.setMemory(true);
		}
		return wallet;
	}

	public void setWallet(FabricWalletProperties wallet) {
		this.wallet = wallet;
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
}
