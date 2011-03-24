package org.mitre.pushee.hub.objectmodel;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Entity;
import javax.persistence.Basic;

@Entity
public class Subscriber {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private String id;
	
	@ManyToOne
	private List<Subscription> subscriptions;
	
	@Basic
	private String postbackURL;
	
	public Subscriber() {
		
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
	 * @return the subscriptions list
	 */
	public List<Subscription> getSubscriptions() {
		return subsriptions;
	}
	
	/**
	 * @param subscriptions the subscriptions list to set
	 */
	public void setSubscriptions(List<Subscription> subscriptions) {
		this.subscriptions = subscriptions;
	}
	
}
