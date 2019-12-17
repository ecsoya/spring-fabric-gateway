package io.github.ecsoya.fabric.config;

import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.impl.FileSystemWallet;
import org.hyperledger.fabric.gateway.impl.InMemoryWallet;
import org.hyperledger.fabric.gateway.impl.WalletIdentity;

/**
 * The wallet configuration of fabric gateway.
 * 
 * @author Jin Liu (jin.liu@soyatec.com)
 * @see Wallet
 * @see WalletIdentity
 */
public class FabricWalletProperties {

	/**
	 * Using memory wallet or not.
	 * 
	 * @see InMemoryWallet
	 * @see FileSystemWallet
	 */
	private boolean memory = true;

	/**
	 * The wallet file path for using {@link FileSystemWallet}.
	 */
	private String file;

	/**
	 * The default identify of wallet.
	 * 
	 * @see WalletIdentity
	 */
	private String identify = "admin";

	public boolean isMemory() {
		return memory;
	}

	public void setMemory(boolean memory) {
		this.memory = memory;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getIdentify() {
		return identify;
	}

	public void setIdentify(String identify) {
		this.identify = identify;
	}

	@Override
	public String toString() {
		return "FabricWalletProperties [memory=" + memory + ", file=" + file + ", identify=" + identify + "]";
	}

}
