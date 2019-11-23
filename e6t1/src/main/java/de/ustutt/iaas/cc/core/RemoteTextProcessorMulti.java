package de.ustutt.iaas.cc.core;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

/**
 * A text processor that sends the text to one of a set of remote REST API for
 * processing (and balances the load between them round-robin).
 * 
 * @author hauptfn
 *
 */
public class RemoteTextProcessorMulti implements ITextProcessor {

	private List<WebTarget> targets = new ArrayList<>();
	int nextProcessor = 0;

	public RemoteTextProcessorMulti(List<String> textProcessorResources, Client client) {
		super();
		textProcessorResources.forEach(item -> {
			targets.add(client.target(item));
		});
	}

	@Override
	public String process(String text) {
		System.out.println("INDEX " + nextProcessor + " " + targets.size());
		if (targets.size() == nextProcessor) {
			nextProcessor = 0;
		}

		String processedText = targets.get(nextProcessor).request(MediaType.TEXT_PLAIN).post(Entity.entity(text, MediaType.TEXT_PLAIN),
				String.class);

		nextProcessor = nextProcessor + 1;

		return processedText;
	}

}
