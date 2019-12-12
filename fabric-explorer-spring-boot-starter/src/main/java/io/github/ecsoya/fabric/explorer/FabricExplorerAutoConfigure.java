package io.github.ecsoya.fabric.explorer;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

import io.github.ecsoya.fabric.boot.SpringFabricGatewayAutoConfigure;

@Configuration
@ImportAutoConfiguration(SpringFabricGatewayAutoConfigure.class)
@EnableConfigurationProperties(FabricExplorerProperties.class)
public class FabricExplorerAutoConfigure {

	@Bean(name = "messageSource")
	public ResourceBundleMessageSource getMessageResource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("i18n/messages");
		return messageSource;
	}
}
