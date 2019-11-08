package de.unistuttgart.iaas.cc.exfour;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class IndexController {


	private final static Logger logger = LoggerFactory.getLogger(IndexController.class);
	private static final String awsCredentialsFile = "aws.properties";

	@RequestMapping("/")
	public String hello(Model model) {

	    // TODO: Task 4 - Request database for content of cart

		model.addAttribute("cart", "this is the content");
		return "index";
	}
}
