package io.github.ecsoya.fabric.explorer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "io.github.ecsoya.fabric.explorer.*")
public class FabricExplorerApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(FabricExplorerApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(FabricExplorerApplication.class, args);
	}

}
