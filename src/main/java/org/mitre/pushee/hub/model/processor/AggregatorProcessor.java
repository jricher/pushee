package org.mitre.pushee.hub.model.processor;




/**
 * Interface for aggregator processors
 * 
 * @author AANGANES
 *
 */
public interface AggregatorProcessor<T, K> {

	/**
	 * Process some new content.
	 * 
	 * @param input the input HTTP Entity
	 */
	public void process(K input);
	
	/**
	 * Get the latest content from this aggregator.
	 * 
	 * @return the new content
	 */
	public T getContent();
	
}
