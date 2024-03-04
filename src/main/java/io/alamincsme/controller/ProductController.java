package io.alamincsme.controller;


import io.alamincsme.model.Product;
import io.alamincsme.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ProductController {
	@Autowired
	private ProductService service;

	@GetMapping("/")
	public String showHome(Model model) {
		List<Product> productsSortedByName = service.findAllProductsSortedByName();
		System.out.println(productsSortedByName);
		model.addAttribute("products", productsSortedByName);
		return "home";
	}
}
