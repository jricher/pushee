package org.mitre.pushee.hub.model.processor.impl;

import org.mitre.pushee.hub.model.processor.AggregatorProcessor;
import org.springframework.http.HttpEntity;

/**
 * Basic, "dumb" implementation of the AggregatorProcessor interface.
 * This class just concatenates the bodies of the HttpEntities it receives.
 * 
 * @author AANGANES
 *
 */
public class BasicAggregatorProcessor implements AggregatorProcessor {

	private HttpEntity<String> content;
	
	public HttpEntity<String> process(HttpEntity<String> input) {
		
		String newContentString = content.getBody().concat(input.getBody());
		HttpEntity<String> newContent = new HttpEntity<String>(newContentString, content.getHeaders());
		
		this.content = newContent;
		return newContent;
	}
	
	public HttpEntity<String> getContent() {
		return content;
	}
	
}
