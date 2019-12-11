package io.github.ecsoya.fabric.config;

public class FabricChaincodeProperties {

	private String identify;

	private String version;

	private String name;

	public String getIdentify() {
		return identify;
	}

	public void setIdentify(String identify) {
		this.identify = identify;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "FabricChaincodeProperties [identify=" + identify + ", version=" + version + ", name=" + name + "]";
	}

}
