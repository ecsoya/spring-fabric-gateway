package io.github.ecsoya.fabric.config;

import org.hyperledger.fabric.gateway.Identity;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.impl.identity.FileSystemWalletStore;
import org.hyperledger.fabric.gateway.impl.identity.InMemoryWalletStore;

import lombok.Data;

/**
 * The wallet configuration of fabric gateway.
 * 
 * @author Jin Liu (jin.liu@soyatec.com)
 * @see Wallet
 * @see Identity
 */
@Data
public class FabricWalletProperties {

	/**
	 * Using memory wallet or not.
	 * 
	 * @see InMemoryWalletStore
	 * @see FileSystemWalletStore
	 */
	private boolean memory = true;

	/**
	 * The wallet file path for using {@link FileSystemWalletStore}.
	 */
	private String file;

	/**
	 * The default identify of wallet.
	 * 
	 * @see Identity
	 */
	private String identity = "admin";

}
