package io.github.ecsoya.fabric.explorer;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

import io.github.ecsoya.fabric.boot.SpringFabricGatewayAutoConfigure;
import io.github.ecsoya.fabric.explorer.controller.IndexController;
import io.github.ecsoya.fabric.explorer.controller.PageController;

@Configuration
@ImportAutoConfiguration(SpringFabricGatewayAutoConfigure.class)
@EnableConfigurationProperties(FabricExplorerProperties.class)
public class FabricExplorerAutoConfigure {

	@Bean
	public IndexController indexController() {
		return new IndexController();
	}

	@Bean
	public PageController pageController() {
		return new PageController();
	}

	@Bean(name = "messageSource")
	public ResourceBundleMessageSource getMessageResource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("i18n/messages");
		return messageSource;
	}
}
