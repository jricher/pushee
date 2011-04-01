package org.mitre.pushee.hub.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 * Feed representation. Feed objects contain a list of the subscribers
 * subscribed to this feed, as well as a reference back to the feed's 
 * Publisher.
 * 
 * @author AANGANES
 *
 */
@Entity
public class Feed {

	public enum FeedType {
		ATOM, RSS
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private String id;
	
	@Basic
	private String url;
	
	@Basic
	private FeedType type;
	
	@OneToMany
	private Publisher publisher;
	
	@ManyToOne
	private List<Subscriber> subscribers;
	
	public Feed() {
		
	}
	
	/**
	 * Add a subscriber to the list
	 * 
	 * @param s the subscriber to add
	 */
	public void addSubsciber(Subscriber s) {
		
		subscribers.add(s); 
	
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(FeedType type) {
		this.type = type;
	}

	/**
	 * @return the type
	 */
	public FeedType getType() {
		return type;
	}

	/**
	 * @param publisher the publisher to set
	 */
	public void setPublisher(Publisher publisher) {
		this.publisher = publisher;
	}

	/**
	 * @return the publisher
	 */
	public Publisher getPublisher() {
		return publisher;
	}
	
	/**
	 * @return the subscribers list
	 */
	public List<Subscriber> getSubscribers() {
		return subscribers;
	}
	
	/**
	 * @param subscribers the subscribers list to set
	 */
	public void setSubscribers(List<Subscriber> subscribers) {
		this.subscribers = subscribers;
	}
	
}
