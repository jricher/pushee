package org.mitre.pushee.hub.model.processor.impl;

import java.util.ArrayList;

import org.mitre.pushee.hub.model.processor.AggregatorProcessor;

/**
 * Basic, "dumb" implementation of the AggregatorProcessor interface.
 * This class just concatenates the bodies of the HttpEntities it receives.
 * 
 * @author AANGANES
 *
 */
public class BasicAggregatorProcessor implements AggregatorProcessor<ArrayList<String>, String> {

	private ArrayList<String> content;
	
	public BasicAggregatorProcessor() {
		content = new ArrayList<String>();
	}
	
	public void process(String input) {
		content.add(input);
	}
	
	public ArrayList<String> getContent() {
		return content;
	}

	
}
