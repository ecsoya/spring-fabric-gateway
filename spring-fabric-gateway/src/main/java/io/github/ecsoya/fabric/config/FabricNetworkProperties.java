package io.github.ecsoya.fabric.config;

public class FabricNetworkProperties {

	private String name;

	private String file;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	@Override
	public String toString() {
		return "FabricNetworkProperties [name=" + name + ", file=" + file + "]";
	}

}
