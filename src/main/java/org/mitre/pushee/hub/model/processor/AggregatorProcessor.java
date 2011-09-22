package org.mitre.pushee.hub.model.processor;

import org.springframework.http.HttpEntity;


/**
 * Interface for aggregator processors
 * 
 * @author AANGANES
 *
 */
public interface AggregatorProcessor {

	/**
	 * Process some new content.
	 * 
	 * @param input the input HTTP Entity
	 * @return the processed output
	 */
	public HttpEntity<String> process(HttpEntity<String> input);
	
	/**
	 * Get the latest content from this aggregator.
	 * 
	 * @return the new content
	 */
	public HttpEntity<String> getContent();
	
}
