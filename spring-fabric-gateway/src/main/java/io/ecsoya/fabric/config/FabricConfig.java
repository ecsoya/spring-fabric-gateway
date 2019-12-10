package io.ecsoya.fabric.config;

import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class FabricConfig {

	private static final String CONFIG_FILE = "fabric.yml";

	public static final String WALLET_MEMORY = "fabric.wallet.memory"; // Use memory wallet, default true
	public static final String WALLET_FILE = "fabric.wallet.file"; // Wallet root file.
	public static final String WALLET_IDENTIFY = "fabric.wallet.identify";

	public static final String CHAINCODE = "fabric.chaincode";
	public static final String NETWORK_CONFIG_FILE = "fabric.network.config-file";
	public static final String CHANNEL = "fabric.channel";

	private Logger logger = LoggerFactory.getLogger(FabricConfig.class);
	private Properties properties;

	@PostConstruct
	public void initialize() {
		logger.debug("Initialize Fabric Config");

		properties = new Properties();
		properties.put(WALLET_IDENTIFY, "admin");
		properties.put(CHAINCODE, "traceable");
		properties.put(NETWORK_CONFIG_FILE, "network/connection.yml");
		properties.put(CHANNEL, "common");

		try {
			ClassPathResource file = new ClassPathResource(CONFIG_FILE);
			YamlPropertiesFactoryBean bean = new YamlPropertiesFactoryBean();
			bean.setResources(file);
			Properties object = bean.getObject();
			Set<Entry<Object, Object>> entrySet = object.entrySet();
			for (Entry<Object, Object> entry : entrySet) {
				properties.put(entry.getKey(), entry.getValue());
			}
		} catch (Exception e) {
			logger.debug("Initialize Fabric Config failed:", e);
		}
	}

	public String getProperty(String key) {
		return properties.getProperty(key);
	}

	public boolean getBoolean(String key) {
		String value = getProperty(key);
		return "true".equalsIgnoreCase(value);
	}
}
