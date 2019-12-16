package io.github.ecsoya.fabric.explorer;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("spring.fabric.explorer")
public class FabricExplorerProperties {

	private String title = "Spring Fabric Explorer";

	private String logo = "img/explorer/camel.png";

	private String copyright = "Ecsoya (jin.liu@soyatec.com)";

	private String hyperledgerExplorerUrl = "";

	private String path = "/explorer";

	private Map<String, Object> map;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getCopyright() {
		return copyright;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	public Map<String, Object> toMap() {
		if (map == null) {
			map = new HashMap<>();
			map.put("title", title);
			map.put("logo", logo);
			map.put("copyright", copyright);
			map.put("hyperledgerExplorerUrl", hyperledgerExplorerUrl);
			map.put("path", getPath());
		}
		return map;
	}

	public String getHyperledgerExplorerUrl() {
		return hyperledgerExplorerUrl;
	}

	public void setHyperledgerExplorerUrl(String hyperledgerExplorerUrl) {
		this.hyperledgerExplorerUrl = hyperledgerExplorerUrl;
	}

	public String getPath() {
		if (path == null || path.equals("")) {
			return "";
		}
		if (path.startsWith("/")) {
			path = path.substring(1);
		}
		if (path.endsWith("/")) {
			path = path.substring(0, path.length() - 1);
		}
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public String toString() {
		return "FabricExplorerProperties [" + toMap() + "]";
	}

}
