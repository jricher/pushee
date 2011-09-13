package org.mitre.pushee.hub.service;

import static org.easymock.EasyMock.createNiceMock;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mitre.pushee.hub.exception.AggregatorNotFoundException;
import org.mitre.pushee.hub.repository.AggregatorRepository;
import org.mitre.pushee.hub.service.impl.DefaultAggregatorService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test of the DefaultAggregatorService class
 * 
 * @author AANGANES
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/spring/application-context.xml", "classpath:test-context.xml"})
public class AggregatorServiceTest {

	private AggregatorService aggService;
	private AggregatorRepository repository;
	
	@Before
	public void setup() {
		repository = createNiceMock(AggregatorRepository.class);
		aggService = new DefaultAggregatorService(repository);
	}
	
	@Test
	@Ignore
	public void getById_valid() {
		aggService.getById(1L);
	}
	
	@Test
	@Ignore
	public void getById_invalid() {
		
	}
	
	@Test
	@Ignore
	public void getAll() {
		
	}
	
	@Test
	@Ignore
	public void removeById_valid() {
		
	}
	
	@Test
	@Ignore
	public void removeById_invalid() {
	
	}
	
	@Test
	@Ignore
	public void remove_valid() {
		
	}
	
	@Test
	@Ignore
	public void getExistingAggregator_valid() {
		
	}
	
	@Test(expected = AggregatorNotFoundException.class)
	@Ignore
	public void getExistingAggregator_invalid() {
		
	}
}


