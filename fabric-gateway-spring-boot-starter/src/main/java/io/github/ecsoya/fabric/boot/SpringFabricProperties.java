package io.github.ecsoya.fabric.boot;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import io.github.ecsoya.fabric.config.FabricProperties;

/**
 * Configure {@link FabricProperties} with spring boot application.yml.
 * 
 * @author Jin Liu (jin.liu@soyatec.com)
 */
@Configuration
@ConfigurationProperties("spring.fabric")
public class SpringFabricProperties extends FabricProperties {

	/**
	 * Load the network contents from classpath.
	 */
	@Override
	public InputStream getNetworkContents() {
		InputStream contents = super.getNetworkContents();
		if (contents == null && network != null) {
			String file = network.getFile();
			if (file != null && !file.equals("")) {
				ClassPathResource resource = new ClassPathResource(file);
				try {
					return resource.getInputStream();
				} catch (IOException e) {
					return null;
				}
			}
		}
		return contents;
	}
}
