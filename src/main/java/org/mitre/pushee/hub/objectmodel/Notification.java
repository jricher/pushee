package org.mitre.pushee.hub.objectmodel;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Notification {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private String id;
	
	@Basic
	private String hubMode = "publish";
	
	@Basic
	private String hubURL;
	
	public Notification() {
		
	}

	/**
	 * @param hubURL the hubURL to set
	 */
	public void setHubURL(String hubURL) {
		this.hubURL = hubURL;
	}

	/**
	 * @return the hubURL
	 */
	public String getHubURL() {
		return hubURL;
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
