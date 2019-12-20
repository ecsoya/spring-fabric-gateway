package io.github.ecsoya.fabric.config;

import lombok.Data;

@Data
public class FabricChaincodeFunctionProperties {

	private String create = "create";

	private String get = "get";

	private String delete = "delete";

	private String update = "update";

	private String query = "query";

	private String history = "history";

	private String count = "count";

	private String exists = "exists";
}
