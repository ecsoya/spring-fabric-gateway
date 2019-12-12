package io.github.ecsoya.fabric.explorer;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
public class FabricExplorerAutoConfiguration {

	private Logger logger = LoggerFactory.getLogger(FabricExplorerAutoConfiguration.class);

	@Autowired
	private FabricExplorerProperties properties;

	@PostConstruct
	private void initialize() {
		logger.info("Init FabricExplorerAutoConfiguration: " + properties);
	}

	@Bean
	@ConditionalOnMissingBean(value = { IndexController.class })
	public IndexController indexController() {
		return new IndexController();
	}

	@Bean
	@ConditionalOnMissingBean(value = { PageController.class })
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
