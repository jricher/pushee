package org.mitre.pushee.enterprise.web;

import static org.easymock.EasyMock.createNiceMock;

import org.junit.Before;
import org.mitre.pushee.hub.service.AggregatorService;


/**
 * Unit test for AggregatorAPI controller
 * 
 * @author AANGANES
 *
 */
public class AggregatorAPITest {

	private AggregatorAPI aggregatorApi;
	private AggregatorService aggService;
	
	@Before
	public void setup() {
		aggService = createNiceMock(AggregatorService.class);
		aggregatorApi = new AggregatorAPI(aggService);
	}
	
}
