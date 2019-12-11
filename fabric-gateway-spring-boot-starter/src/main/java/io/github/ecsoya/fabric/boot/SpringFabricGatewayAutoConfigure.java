package io.github.ecsoya.fabric.boot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.ecsoya.fabric.config.FabricContext;
import io.github.ecsoya.fabric.service.IFabricInfoService;
import io.github.ecsoya.fabric.service.IFabricService;
import io.github.ecsoya.fabric.service.impl.FabricInfoServiceImpl;
import io.github.ecsoya.fabric.service.impl.FabricServiceImpl;

@Configuration
@EnableConfigurationProperties(SpringFabricProperties.class)
public class SpringFabricGatewayAutoConfigure {

	@Autowired
	private SpringFabricProperties properties;

	@Bean
	public FabricContext fabricContext() {
		return new FabricContext(properties);
	}

	@Bean
	public IFabricService fabricService(FabricContext fabricContext) {
		return new FabricServiceImpl(fabricContext);
	}

	@Bean
	public IFabricInfoService fabricInfoService(FabricContext fabricContext) {
		return new FabricInfoServiceImpl(fabricContext);
	}
}
