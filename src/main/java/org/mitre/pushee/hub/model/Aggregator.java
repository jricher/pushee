package org.mitre.pushee.hub.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.mitre.pushee.hub.model.Feed.FeedType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An aggregator is a special class of Feed that acts as both a Feed and a Subscriber -
 * it subscribes to a set of source, and serves the combined content as a single new feed.
 * 
 * @author AANGANES
 *
 */
@Entity
@Table(name="aggregator")
@NamedQueries({
	@NamedQuery(name = "Aggregator.getByUrl", query = "select a from Aggregator a where a.url  = :aggregatorUrl"),
	@NamedQuery(name = "Aggregator.getAll", query = "select a from Aggregator a")
})
public class Aggregator {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Basic
	private String displayName;
	
	@Basic
	private String url;

	@Basic
	private FeedType type;
	
	//Feeds that I subscribe to
	@OneToMany
	private List<Feed> source;
	
	//Subscribers that are subscribed to me
	@OneToMany
	private List<Subscriber> subscribers;
	
	@OneToMany(mappedBy = "feed")
	private Collection<Subscription> subscriptions;
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(Subscriber.class);
	
	/**
	 * Constructor
	 */
	public Aggregator() {
		subscribers = new ArrayList<Subscriber>();
	}
	
	public void addSubscriber(Subscriber subscriber) {
		this.subscribers.add(subscriber);
	}
	
	public void addSourceFeed(Feed feed) {
		this.source.add(feed);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((displayName == null) ? 0 : displayName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

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
		Aggregator other = (Aggregator) obj;
		if (displayName == null) {
			if (other.displayName != null) {
				return false;
			}
		} else if (!displayName.equals(other.displayName)) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (type != other.type) {
			return false;
		}
		if (url == null) {
			if (other.url != null) {
				return false;
			}
		} else if (!url.equals(other.url)) {
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
	 * @param source the source to set
	 */
	public void setFeeds(List<Feed> feeds) {
		this.source = feeds;
	}

	/**
	 * @return the source
	 */
	public List<Feed> getFeeds() {
		return source;
	}



	/**
	 * @param subscribers the subscribers to set
	 */
	public void setSubscribers(List<Subscriber> subscribers) {
		this.subscribers = subscribers;
	}



	/**
	 * @return the subscribers
	 */
	public List<Subscriber> getSubscribers() {
		return subscribers;
	}

	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
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
