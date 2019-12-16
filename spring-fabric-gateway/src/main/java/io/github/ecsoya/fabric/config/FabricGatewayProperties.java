package io.github.ecsoya.fabric.config;

public class FabricGatewayProperties {

	private long commitTimeout = 10;

	private boolean discovery = false;

	private String commitHandler;

	private String queryHandler;

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

	public long getCommitTimeout() {
		return commitTimeout;
	}

	public void setCommitTimeout(long commitTimeout) {
		this.commitTimeout = commitTimeout;
	}

	public boolean isDiscovery() {
		return discovery;
	}

	public void setDiscovery(boolean discovery) {
		this.discovery = discovery;
	}

	public String getCommitHandler() {
		return commitHandler;
	}

	public void setCommitHandler(String commitHandler) {
		this.commitHandler = commitHandler;
	}

	public String getQueryHandler() {
		return queryHandler;
	}

	public void setQueryHandler(String queryHandler) {
		this.queryHandler = queryHandler;
	}

}
