package org.mitre.pushee.hub.model;

import java.util.ArrayList;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Publisher representation. Since publishers may publish several feeds,
 * subscriber/subscription information is stored in Feed objects.
 * 
 * @author AANGANES
 *
 */
@Entity
@Table(name="publisher")
@NamedQueries({
        @NamedQuery(name="Publisher.getByUrl", query = "select p from Publisher p where p.callbackURL = :url"),
        @NamedQuery(name="Publisher.getAll", query = "select p from Publisher p")
})
public class Publisher {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Basic
	private String callbackURL;
	
	@OneToMany(mappedBy = "publisher")
	private List<Feed> feeds;

	
	private static final Logger logger = LoggerFactory.getLogger(Publisher.class);
	
	public Publisher() {
		feeds = new ArrayList<Feed>();
	}

	
	


	@Override
	public int hashCode() {
		logger.info("Publisher - hashcode");
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((callbackURL == null) ? 0 : callbackURL.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}





	@Override
	public String toString() {
		return "Publisher [id=" + id + ", callbackURL=" + callbackURL
				+ ", feeds=" + feeds + "]";
	}





	@Override
	public boolean equals(Object obj) {
		logger.info("Publisher - equals");
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Publisher other = (Publisher) obj;
		if (callbackURL == null) {
			if (other.callbackURL != null)
				return false;
		} else if (!callbackURL.equals(other.callbackURL))
			return false;
		if (feeds == null) {
			if (other.feeds != null)
				return false;
		} else if (!feeds.equals(other.feeds))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}





	/**
	 * TODO: does this method need to be smarter; ie function like a Set?
	 * @param f
	 */
	public void addFeed(Feed f) {
		feeds.add(f);
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
