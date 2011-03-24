package org.mitre.pushee.hub.objectmodel;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Publisher {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private String id;
	
	@ManyToOne
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
}
