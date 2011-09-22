package org.mitre.pushee.hub.model;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.mitre.pushee.hub.model.processor.AggregatorProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;

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
	@NamedQuery(name = "Aggregator.getAll", query = "select a from Aggregator a"),
	@NamedQuery(name = "Aggregator.getByFeedUrl", query = "select a from Aggregator a where a.aggregatorFeed.url =:feedUrl"),
	@NamedQuery(name = "Aggregator.getBySubscriberUrl", query = "select a from Aggregator a where a.sourceSubscriber.postbackURL =:subscriberUrl")
})
public class Aggregator {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Basic
	private String displayName;
	
	@JoinColumn(name = "feed_id")
	private Feed aggregatorFeed;
	
	@JoinColumn(name = "subscriber_id")
	private Subscriber sourceSubscriber;
	
	@Transient
	private AggregatorProcessor processor;
	
	private static final Logger logger = LoggerFactory.getLogger(Aggregator.class);
	
	/**
	 * Constructor
	 */
	public Aggregator() {
		logger.info("Aggregator constructor");
	}

	public void process(HttpEntity<String> input) {
		processor.process(input);
	}
	
	public HttpEntity<String> getContent() {
		return processor.getContent();
	}
	
	@Override
	public String toString() {
		return "Aggregator [id=" + id + ", displayName=" + displayName
				+ ", aggregatorFeed=" + aggregatorFeed + ", sourceSubscriber="
				+ sourceSubscriber + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((aggregatorFeed == null) ? 0 : aggregatorFeed.hashCode());
		result = prime * result
				+ ((displayName == null) ? 0 : displayName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime
				* result
				+ ((sourceSubscriber == null) ? 0 : sourceSubscriber.hashCode());
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
		if (aggregatorFeed == null) {
			if (other.aggregatorFeed != null) {
				return false;
			}
		} else if (!aggregatorFeed.equals(other.aggregatorFeed)) {
			return false;
		}
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
		if (sourceSubscriber == null) {
			if (other.sourceSubscriber != null) {
				return false;
			}
		} else if (!sourceSubscriber.equals(other.sourceSubscriber)) {
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
	 * @return the aggregatorFeed
	 */
	public Feed getAggregatorFeed() {
		return aggregatorFeed;
	}
	
	/**
	 * @param feed the aggregatorFeed
	 */
	public void setAggregatorFeed(Feed feed) {
		this.aggregatorFeed = feed;
	}

	/**
	 * @return the sourceSubscriber
	 */
	public Subscriber getSourceSubscriber() {
		return sourceSubscriber;
	}
	
	/**
	 * @param subscriber the sourceSubscriber
	 */
	public void setSourceSubscriber(Subscriber subscriber) {
		this.sourceSubscriber = subscriber;
	}

	public AggregatorProcessor getProcessor() {
		return processor;
	}

	public void setProcessor(AggregatorProcessor processor) {
		this.processor = processor;
	}
}
