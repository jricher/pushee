package org.mitre.pushee.hub.model;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 * Feed representation. Feed objects contain a collection of the subscribers
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
	private Long id;
	
	@Basic
	private String url;
	
	@Basic
	private FeedType type;
	
	@ManyToOne
	private Publisher publisher;
	
	@OneToMany
	private Collection<Subscription> subscriptions;
	
	public Feed() {
		subscriptions = new ArrayList<Subscription>();
	}
	
	/**
	 * Add a subscriber to the Collection
	 * 
	 * @param s the subscriber to add
	 */
	public void addSubscription(Subscription s) {
		
		subscriptions.add(s); 
	
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
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
	 * @return the subscriptions Collection
	 */
	public Collection<Subscription> getSubscriptions() {
		return subscriptions;
	}
	
	/**
	 * @param subscriptions the subscriptions Collection to set
	 */
	public void setSubscriptions(Collection<Subscription> subscriptions) {
		this.subscriptions = subscriptions;
	}
	
}
