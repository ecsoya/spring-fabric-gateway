package io.ecsoya.fabric.boot;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.ClassPathResource;

import io.ecsoya.fabric.config.FabricProperties;

@ConfigurationProperties("spring.fabric")
public class SpringFabricProperties extends FabricProperties {

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
