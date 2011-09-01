package org.mitre.pushee.hub.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test of the JpaAggregatorRepository class
 * 
 * @author AANGANES
 *
 */

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/spring/application-context.xml", "classpath:test-context.xml"})
public class AggregatorRepositoryTest {

	@Autowired
	private AggregatorRepository repository;
	
	@PersistenceContext
	private EntityManager sharedManager;
	
	@Test
	@Rollback
	@Ignore
	public void getAll() {
		
	}
	
	@Test
	@Ignore
	public void getById_valid() {
		
	}
	
	@Test
	@Ignore
	public void getById_invalid() {
		//should return null
	}
	
	@Test
	@Ignore
	public void getByUrl_valid() {
		
	}
	
	@Test
	@Ignore
	public void getByUrl_invalid() {
		//should return null
	}
	
	@Test
	@Ignore
	public void save() {
		
	}
	
	@Test
	@Ignore
	public void remove_valid() {
		
	}
	
	@Test(expected = IllegalArgumentException.class)
	@Ignore
	public void remove_invalid() {
		
	}
	
}
