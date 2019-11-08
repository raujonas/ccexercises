package de.unistuttgart.iaas.cc.exfour;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
public class AddNewItemToDatabaseCartController {


	private final static Logger logger = LoggerFactory.getLogger(IndexController.class);
	private static final String awsCredentialsFile = "aws.properties";

	@RequestMapping("/set")
	public String hello(@RequestParam(value = "item") String newItem) {

		// TODO: Task4 - Add newItem to the database
		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
				.build();

		DynamoDB dynamoDB = new DynamoDB(client);

		Table table = dynamoDB.getTable("ShoppingCart");

		try {
			System.out.println("Adding a new item... (" + newItem + ")");

			table.putItem(new Item().withPrimaryKey("itemUuid", UUID.randomUUID().toString()).withString("item", newItem));

			System.out.println("PutItem succeeded");
		}
		catch (Exception e) {
			System.err.println(e.getMessage());
		}

		return "set-cookie";
	}
}
