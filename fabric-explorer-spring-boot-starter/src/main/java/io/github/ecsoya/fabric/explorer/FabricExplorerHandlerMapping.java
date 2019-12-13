package io.github.ecsoya.fabric.explorer;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import io.github.ecsoya.fabric.explorer.controller.FabricExplorerController;

public class FabricExplorerHandlerMapping extends RequestMappingHandlerMapping {

	private Logger logger = LoggerFactory.getLogger(FabricExplorerHandlerMapping.class);

	@Autowired
	private FabricExplorerProperties properties;

	@Override
	protected void registerHandlerMethod(Object handler, Method method, RequestMappingInfo mapping) {
		String path = properties.getPath();
		PatternsRequestCondition oldPatternCondition = mapping.getPatternsCondition();
		Set<String> patterns = oldPatternCondition.getPatterns();
		if (patterns != null && !patterns.isEmpty() && !"".equals(path)) {
			String[] newPatterns = patterns.stream().map(p -> {
				if (!p.startsWith("/")) {
					p = "/" + p;
				}
				String value = "/" + path + p;
				if (value.endsWith("/")) {
					value = value.substring(0, value.length() - 1);
				}
				return value;
			}).collect(Collectors.toSet()).toArray(new String[0]);

			mapping = new RequestMappingInfo(new PatternsRequestCondition(newPatterns), mapping.getMethodsCondition(),
					mapping.getParamsCondition(), mapping.getHeadersCondition(), mapping.getConsumesCondition(),
					mapping.getProducesCondition(), mapping.getCustomCondition());
		}
		logger.info("Fabric Explorer Registered Mapping: " + mapping);
		super.registerHandlerMethod(handler, method, mapping);
	}

	@Override
	public void registerMapping(RequestMappingInfo mapping, Object handler, Method method) {
		super.registerMapping(mapping, handler, method);
	}

	@Override
	protected boolean isHandler(Class<?> beanType) {
		return FabricExplorerController.class == beanType;
	}

	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();

	}

}
