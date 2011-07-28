package org.mitre.pushee.hub.model;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Subscriber representation. Subscribers are referenced from
 * the Feeds they are subscribed to.
 * @author AANGANES
 *
 */
@Entity
@Table(name="subscriber")
@NamedQueries({
        @NamedQuery(name = "Subscriber.getByUrl", query = "select s from Subscriber s where s.postbackURL = :url"),
        @NamedQuery(name = "Subscriber.getAll", query = "select s from Subscriber s")
        })
public class Subscriber {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Basic
	private String postbackURL;
	
    @OneToMany(mappedBy = "subscriber", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<Subscription> subscriptions;
    
    
    private static final Logger logger = LoggerFactory.getLogger(Subscriber.class);
    
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
	
	@Override
	public String toString() {
		return "Subscriber [id=" + id + ", postbackURL=" + postbackURL
				+ ", subscriptions=" + subscriptions + "]";
	}

	/**
	 * Remove a subscription, if it exists.
	 * 
	 * @param f the feed this subscription this is for
	 */
	public void removeSubscription(Feed f) {

		if (subscriptions.contains(f)) {
			subscriptions.remove(f);
		}
	}
	
	

	@Override
	public int hashCode() {
		logger.info("Subscriber - hashcode");
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((postbackURL == null) ? 0 : postbackURL.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		logger.info("Subscriber - equals");
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Subscriber other = (Subscriber) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (postbackURL == null) {
			if (other.postbackURL != null)
				return false;
		} else if (!postbackURL.equals(other.postbackURL))
			return false;
		if (subscriptions == null) {
			if (other.subscriptions != null)
				return false;
		} else if (!subscriptions.equals(other.subscriptions))
			return false;
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
