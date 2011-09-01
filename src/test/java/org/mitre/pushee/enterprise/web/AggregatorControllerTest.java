package org.mitre.pushee.enterprise.web;

import static org.easymock.EasyMock.createNiceMock;

import org.junit.Before;
import org.mitre.pushee.hub.service.AggregatorService;


/**
 * Unit test for the AggregatorController class
 * 
 * @author AANGANES
 *
 */
public class AggregatorControllerTest {

	private AggregatorService aggService;
	private AggregatorController controller;
	
	@Before
	public void setup() {
		aggService = createNiceMock(AggregatorService.class);
		controller = new AggregatorController(aggService);
	}
	
	
}
