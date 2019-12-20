package io.github.ecsoya.fabric.config;

import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.impl.FileSystemWallet;
import org.hyperledger.fabric.gateway.impl.InMemoryWallet;
import org.hyperledger.fabric.gateway.impl.WalletIdentity;

import lombok.Data;

/**
 * The wallet configuration of fabric gateway.
 * 
 * @author Jin Liu (jin.liu@soyatec.com)
 * @see Wallet
 * @see WalletIdentity
 */
@Data
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
	private String identity = "admin";

}
