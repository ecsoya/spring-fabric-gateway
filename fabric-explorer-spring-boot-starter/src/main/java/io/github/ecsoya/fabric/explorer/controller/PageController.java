package io.github.ecsoya.fabric.explorer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import io.github.ecsoya.fabric.explorer.FabricExplorerProperties;

@Controller
public class PageController {

	@Autowired
	private FabricExplorerProperties properties;

	@GetMapping("/")
	public String home(Model model) {
		model.addAllAttributes(properties.toMap());
		return "index";
	}

	@GetMapping("/block")
	public String block(Model model, long height) {
		model.addAllAttributes(properties.toMap());
		model.addAttribute("height", height);
		return "block";
	}

	@GetMapping("/tx")
	public String tx(Model model, String txid) {
		model.addAllAttributes(properties.toMap());
		model.addAttribute("txid", txid);
		return "tx";
	}

	@PostMapping("/history")
	public String history(Model model, String key, String type) {
		model.addAllAttributes(properties.toMap());
		model.addAttribute("key", key);
		model.addAttribute("type", type);
		return "history";
	}
}
