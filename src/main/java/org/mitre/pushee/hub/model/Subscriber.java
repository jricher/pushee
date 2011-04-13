package org.mitre.pushee.hub.model;

import javax.persistence.*;

/**
 * Subscriber representation. Subscribers are referenced from
 * the Feeds they are subscribed to.
 * @author AANGANES
 *
 */
@Entity
@NamedQueries({
        @NamedQuery(name="Subscriber.getByFeedId", query="SELECT s FROM Subscriber s WHERE s.feed.id = :feedId")
        })
public class Subscriber {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Basic
	private String postbackURL;

    @ManyToOne
    private Feed feed;
	
	public Subscriber() {
		
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

    public Feed getFeed() {
        return feed;
    }

    public void setFeed(Feed feed) {
        this.feed = feed;
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
	
}
