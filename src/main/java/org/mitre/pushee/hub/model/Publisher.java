package org.mitre.pushee.hub.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

/**
 * Publisher representation. Since publishers may publish several feeds,
 * subscriber/subscription information is stored in Feed objects.
 * 
 * @author AANGANES
 *
 */
@Entity
@NamedQueries({
        @NamedQuery(name="Publisher.getByUrl", query = "select p from Publisher p where p.callbackURL = :url")
})
public class Publisher {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Basic
	private String callbackURL;
	
	@OneToMany(mappedBy = "publisher")
	private List<Feed> feeds;

	
	public Publisher() {
		feeds = new ArrayList<Feed>();
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
