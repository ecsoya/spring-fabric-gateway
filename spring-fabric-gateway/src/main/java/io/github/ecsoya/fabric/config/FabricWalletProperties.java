package io.github.ecsoya.fabric.config;

public class FabricWalletProperties {

	private boolean memory = true;

	private String file;

	private String identify = "admin";

	public boolean isMemory() {
		return memory;
	}

	public void setMemory(boolean memory) {
		this.memory = memory;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getIdentify() {
		return identify;
	}

	public void setIdentify(String identify) {
		this.identify = identify;
	}
}
