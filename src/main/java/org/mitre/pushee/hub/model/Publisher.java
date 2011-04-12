package org.mitre.pushee.hub.model;

import java.util.List;

import javax.persistence.*;

/**
 * Publisher representation. Since publishers may publish several feeds,
 * subscriber/subscription information is stored in Feed objects.
 * 
 * @author AANGANES
 *
 */
@Entity
public class Publisher {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private String id;
	
	@Basic
	private String callbackURL;
	
	@OneToMany
	private List<Feed> feeds;

	
	public Publisher() {
		
	}
	
	/**
	 * @param feed the feed to set
	 */
	public void setFeeds(List<Feed> feeds) {
		this.feeds = feeds;
	}

	/**
	 * @return the feed
	 */
	public List<Feed> getFeeds() {
		return feeds;
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
	 * @param callbackURL the callbackURL to set
	 */
	public void setCallbackURL(String callbackURL) {
		this.callbackURL = callbackURL;
	}

	/**
	 * @return the callbackURL
	 */
	public String getCallbackURL() {
		return callbackURL;
	}
}
