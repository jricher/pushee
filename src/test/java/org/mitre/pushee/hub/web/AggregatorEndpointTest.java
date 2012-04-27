package org.mitre.pushee.hub.web;

import static org.easymock.EasyMock.createNiceMock;

import org.junit.Before;
import org.junit.Test;
import org.mitre.pushee.hub.service.AggregatorService;
import org.mitre.pushee.hub.service.HubService;
import org.mitre.pushee.hub.service.impl.DefaultAggregatorService;

public class AggregatorEndpointTest {

	private HubService hubService;
    private ClientConnection http;
    private PuSHEndpoint endpoint;
    private AggregatorService aggService;
    private AggregatorEndpoint aggEndpoint;
    
    @Before
    public void setup() {
    	aggService = createNiceMock(DefaultAggregatorService.class);
    	
    	aggEndpoint = new AggregatorEndpoint(aggService, "http://localhost:8080.push/hub");
    }
    
    @Test
    public void subscriberCallbackTest() {
    	
    }
    
    @Test
    public void feedCallbackTest() {
    	
    }
	
}
