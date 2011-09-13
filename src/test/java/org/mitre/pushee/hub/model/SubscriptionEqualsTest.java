package org.mitre.pushee.hub.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test for the Subscription .equals method
 * 
 * @author AANGANES
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/spring/application-context.xml", "classpath:test-context.xml"})
public class SubscriptionEqualsTest {

	//SubA and subB should be equal.
	private Subscription subA;
	private Subscription subB;
	//SubC should not be equal to A or B
	private Subscription subC;
	
	private Feed theFeed;
	private Feed theOtherFeed;
	private Subscriber theSubscriber;
	
	@Before
	public void setup()
	{
		//Set up Feeds and the subscriber
		theFeed = new Feed();
		theFeed.setId(1L);
		theFeed.setUrl("http://example.com/feed");
		
		theOtherFeed = new Feed();
		theOtherFeed.setId(2L);
		theOtherFeed.setUrl("http://example.com/theOtherFeed");
		
		theSubscriber = new Subscriber();
		theSubscriber.setId(1L);
		theSubscriber.setPostbackURL("http://example.com/subscriber");
		
		subA = new Subscription();
		subA.setFeed(theFeed);
		subA.setSubscriber(theSubscriber);
		
		subB = new Subscription();
		subB.setFeed(theFeed);
		subB.setSubscriber(theSubscriber);
		
		subC = new Subscription();
		subC.setFeed(theOtherFeed);
		subC.setSubscriber(theSubscriber);
				
	}
	
	@Test
	@Ignore
	public void testReflexive() 
	{
		assertEquals(subA, subA);
		assertEquals(subB, subB);
		assertEquals(subC, subC);
	}
	
	@Test
	@Ignore
	public void testSymmetric() 
	{
		assertEquals(subA, subB);
		assertEquals(subB, subA);
		
		assertFalse(subA.equals(subC));
		assertFalse(subC.equals(subA));
	}
	
	@Test
	@Ignore
	public void testNotEqualToNull() 
	{
		assertFalse(subA.equals(null));
		assertFalse(subC.equals(null));
	}
	
}
