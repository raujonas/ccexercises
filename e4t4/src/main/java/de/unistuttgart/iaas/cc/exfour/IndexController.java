package de.unistuttgart.iaas.cc.exfour;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.UUID;

@Controller
public class IndexController {


	private final static Logger logger = LoggerFactory.getLogger(IndexController.class);
	private static final String awsCredentialsFile = "aws.properties";

	@RequestMapping("/")
	public String hello(Model model) {

	    // TODO: Task 4 - Request database for content of cart

		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
				.build();

		DynamoDB dynamoDB = new DynamoDB(client);

		Table table = dynamoDB.getTable("ShoppingCart");

		ScanSpec scanSpec = new ScanSpec();

		try {
			ItemCollection<ScanOutcome> items = table.scan(scanSpec);

			String content = "";

			Iterator<Item> iter = items.iterator();
			while (iter.hasNext()) {
				Item item = iter.next();
				System.out.println(item.toString());
				content = content + " " + item.getJSON("item").replace("\"", "") + ", ";
			}

			model.addAttribute("cart", content);
		}
		catch (Exception e) {
			System.err.println("Unable to scan the table:");
			System.err.println(e.getMessage());
		}

		return "index";
	}
}
