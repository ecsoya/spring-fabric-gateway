package io.github.ecsoya.fabric.config;

public class FabricGatewayProperties {

	private FabricWalletProperties wallet;

	public FabricWalletProperties getWallet() {
		if (wallet == null) {
			wallet = new FabricWalletProperties();
		}
		return wallet;
	}

	public void setWallet(FabricWalletProperties wallet) {
		this.wallet = wallet;
	}

	@Override
	public String toString() {
		return "FabricGatewayProperties [wallet=" + wallet + "]";
	}

}
