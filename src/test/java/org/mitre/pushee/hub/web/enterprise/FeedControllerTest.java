package org.mitre.pushee.hub.web.enterprise;


import org.junit.Before;
import org.junit.Test;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertThat;

import org.mitre.pushee.hub.service.HubService;

public class FeedControllerTest {

	private HubService hubService;
	
	@Before
	public void setUp() throws Exception {
		hubService = createNiceMock(HubService.class);
	}
	
	@Test
	public void getAllFeeds() {
		
	}
	
	@Test
	public void getFeed() {
		
	}
	
	@Test
	public void addFeed() {
		
	}
	
	@Test
	public void editFeed() {
		
	}
	
	@Test
	public void removeFeed() {
		
	}

}
