package org.mitre.pushee.hub.model;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.*;

/**
 * Subscriber representation. Subscribers are referenced from
 * the Feeds they are subscribed to.
 * @author AANGANES
 *
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "Subscriber.getByUrl", query = "select s from Subscriber s where s.postbackURL = :url")
        })
public class Subscriber {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Basic
	private String postbackURL;
	
    @OneToMany(mappedBy = "subscriber")
    private Collection<Subscription> subscriptions;
    
	public Subscriber() {
		subscriptions = new ArrayList<Subscription>();
	}

	/**
	 * Add a subscription, overwriting old subscriptions if an equivalent
	 * one is found.
	 * 
	 * @param s the subscription to add
	 */
	public void addSubscription(Subscription s) {
		if (!subscriptions.contains(s)) {
			subscriptions.add(s);
		} else {
			subscriptions.remove(s);
			subscriptions.add(s);
		}
	}
	
	/**
	 * Remove a subscription, if it exists.
	 * 
	 * @param f the feed this subscription this is for
	 */
	public void removeSubscription(Feed f) {
		Subscription s = new Subscription();
		s.setFeed(f);
		s.setSubscriber(this);
		
		if (subscriptions.contains(s)) {
			subscriptions.remove(s);
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((postbackURL == null) ? 0 : postbackURL.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Subscriber other = (Subscriber) obj;
		if (postbackURL == null) {
			if (other.postbackURL != null) {
				return false;
			}
		} else if (!postbackURL.equals(other.postbackURL)) {
			return false;
		}
		return true;
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
	 * @param postbackURL the postbackURL to set
	 */
	public void setPostbackURL(String postbackURL) {
		this.postbackURL = postbackURL;
	}

	/**
	 * @return the postbackURL
	 */
	public String getPostbackURL() {
		return postbackURL;
	}

	/**
	 * @param subscriptions the subscriptions to set
	 */
	public void setSubscriptions(Collection<Subscription> subscriptions) {
		this.subscriptions = subscriptions;
	}

	/**
	 * @return the subscriptions
	 */
	public Collection<Subscription> getSubscriptions() {
		return subscriptions;
	}
	
}
