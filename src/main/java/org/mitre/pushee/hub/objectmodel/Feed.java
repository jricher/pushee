package org.mitre.pushee.hub.objectmodel;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Basic;

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
	
	public Feed() {
		
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
	
}
