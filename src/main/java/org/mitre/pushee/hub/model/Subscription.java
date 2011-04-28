package org.mitre.pushee.hub.model;

import java.util.Date;

import javax.persistence.*;


/**
 * Subscription representation. A single subscription links a single subscriber to
 * a single feed. Subscribers and feeds can have many subscriptions.
 * 
 * @author AANGANES
 * 		Created on 4/13/2011
 */
@Entity
@NamedQueries({
    @NamedQuery(name="Subscription.getByFeedId", query="SELECT s FROM Subscription s WHERE s.feed.id = :feedId"),
    @NamedQuery(name="Subscription.getSubscribersByFeedId", query="SELECT s FROM Subscription sub join sub.subscriber s WHERE sub.feed.id = :feedId")
    })
public class Subscription {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Temporal(TemporalType.DATE)
	private Date timeout;
	
	@Basic
	private String secret;
	
	@ManyToOne
    @JoinColumn(name = "feed_id")
	private Feed feed;
	
	@ManyToOne
    @JoinColumn(name = "subscriber_id")
	private Subscriber subscriber;
	
	public Subscription() {
		
	}

	/**
	 * Override Object's .equals method so that we can manipulate collections
	 * of Subscriptions with .contains.
	 * Two subscriptions are considered equivalent if they connect the same feed (URL)
	 * with the same subscriber (URL).
	 */
	@Override
	public boolean equals(Object o) {
		
		if (o != null && o.getClass() == this.getClass()) {
			
			Subscription s = (Subscription)o;
			if ( (s.getFeed().getUrl().equals(this.getFeed().getUrl())) &&
				 (s.getSubscriber().getPostbackURL().equals(this.getSubscriber().getPostbackURL())) ) {
				return true;
			}
			
		}
		
		return false;
	}
	
	/**
	 * In order to override equals correctly, must override hashCode too.
	 */
	@Override
	public int hashCode() {
		return feed.getUrl().length();
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param timeout the timeout to set
	 */
	public void setTimeout(Date timeout) {
		this.timeout = timeout;
	}

	/**
	 * @return the timeout
	 */
	public Date getTimeout() {
		return timeout;
	}

	/**
	 * @param secret the secret to set
	 */
	public void setSecret(String secret) {
		this.secret = secret;
	}

	/**
	 * @return the secret
	 */
	public String getSecret() {
		return secret;
	}

	/**
	 * @param feed the feed to set
	 */
	public void setFeed(Feed feed) {
		this.feed = feed;
	}

	/**
	 * @return the feed
	 */
	public Feed getFeed() {
		return feed;
	}

	/**
	 * @param subscriber the subscriber to set
	 */
	public void setSubscriber(Subscriber subscriber) {
		this.subscriber = subscriber;
	}

	/**
	 * @return the subscriber
	 */
	public Subscriber getSubscriber() {
		return subscriber;
	}
	
	
}
