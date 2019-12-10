package io.ecsoya.fabric.boot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.ecsoya.fabric.config.FabricContext;
import io.ecsoya.fabric.service.IFabricInfoService;
import io.ecsoya.fabric.service.IFabricService;
import io.ecsoya.fabric.service.impl.FabricInfoServiceImpl;
import io.ecsoya.fabric.service.impl.FabricServiceImpl;

@Configuration
@EnableConfigurationProperties(SpringFabricProperties.class)
public class SpringFabricGatewayAutoConfigure {

	@Autowired
	private SpringFabricProperties properties;

	@Bean
	public FabricContext fabricContext() {
		System.out.println(properties);
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
