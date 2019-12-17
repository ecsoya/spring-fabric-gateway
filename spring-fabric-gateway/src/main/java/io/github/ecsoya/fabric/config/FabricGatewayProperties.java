package io.github.ecsoya.fabric.config;

import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.impl.GatewayImpl;

/**
 * The configuration properties of the {@link Gateway}.
 * 
 * @author ecsoya
 *
 */
public class FabricGatewayProperties {

	/**
	 * The timeout of committing to fabric, the unit is minutes.
	 * 
	 * The default value of fabric gateway is 5 minutes, here I increased it to 10
	 * minutes.
	 * 
	 * @see GatewayImpl
	 */
	private long commitTimeout = 10;

	/**
	 * Discovery of gateway builder.
	 * 
	 * @see GatewayImpl
	 */
	private boolean discovery = false;

	/**
	 * The commitHandler class name
	 * 
	 * @see GatewayImpl
	 */
	private String commitHandler;

	/**
	 * The queryHandler class name.
	 * 
	 * @see GatewayImpl
	 */
	private String queryHandler;

	/**
	 * The wallet configuration of gateway.
	 * 
	 * @see GatewayImpl
	 */
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
