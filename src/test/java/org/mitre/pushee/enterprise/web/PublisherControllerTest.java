package org.mitre.pushee.enterprise.web;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.mitre.pushee.hub.exception.PublisherNotFoundException;
import org.mitre.pushee.hub.model.Publisher;
import org.mitre.pushee.hub.service.HubService;
import org.springframework.web.servlet.ModelAndView;

/**
 * Unit test for PublisherController
 * 
 * @author AANGANES
 *
 */
public class PublisherControllerTest {

	private HubService hubService;
	private PublisherController publisherController;
	
	@Before
	public void setup() {
		hubService = createNiceMock(HubService.class);
		publisherController = new PublisherController(hubService);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void viewAll() {
		
		Publisher pub1 = new Publisher();
		pub1.setCallbackURL("http://calbackURL/publisher1");
		pub1.setId(1L);
		
		Publisher pub3 = new Publisher();
		pub3.setCallbackURL("http://calbackURL/publisher3");
		pub3.setId(3L);
		
		Publisher pub2 = new Publisher();
		pub2.setCallbackURL("http://calbackURL/publisher2");
		pub2.setId(2L);
		
		List<Publisher> publishers = new ArrayList<Publisher>();
		publishers.add(pub1);
		publishers.add(pub2);
		publishers.add(pub3);
		
		expect(hubService.getAllPublishers()).andReturn(publishers).once();
		replay(hubService);
		
		ModelAndView result = publisherController.viewAllPublishers(new ModelAndView());
		
		verify(hubService);
		
		assertThat((List<Publisher>)(result.getModel().get("publishers")), CoreMatchers.equalTo(publishers));
		assertThat(result.getViewName(), CoreMatchers.equalTo("management/publisher/publisherIndex"));
		
	}
	
	@Test
	
	public void viewPublisher_valid() {
		
		Publisher publisher = new Publisher();
		publisher.setCallbackURL("http://calbackURL/publisher");
		publisher.setId(5L);
		
		expect(hubService.getExistingPublisher(5L)).andReturn(publisher).once();
		replay(hubService);
		
		ModelAndView result = publisherController.viewPublisher(5L, new ModelAndView());
		
		verify(hubService);
		
		assertThat((Publisher)result.getModel().get("publisher"), CoreMatchers.equalTo(publisher));
		assertThat(result.getViewName(), CoreMatchers.equalTo("management/publisher/viewPublisher"));
		
	}
	
	@Test(expected = PublisherNotFoundException.class)
	public void viewPublisher_invalid() {
		
		expect(hubService.getExistingPublisher(5L)).andThrow(new PublisherNotFoundException()).once();
		replay(hubService);
		
		publisherController.viewPublisher(5L, new ModelAndView());
		
		verify(hubService);
		
	}
	
	@Test
	public void addPublisher() {
		
		ModelAndView result = publisherController.addPublisher(new ModelAndView());
		
		assertThat((Publisher)result.getModel().get("publisher"), CoreMatchers.equalTo(new Publisher()));
		assertThat((String)result.getModel().get("mode"), CoreMatchers.equalTo("add"));
		assertThat(result.getViewName(), CoreMatchers.equalTo("management/publisher/editPublisher"));
		
	}
	
	@Test
	public void editPublisher_valid() {
		
		Publisher publisher = new Publisher();
		publisher.setCallbackURL("http://calbackURL/publisher");
		publisher.setId(5L);
		
		expect(hubService.getExistingPublisher(5L)).andReturn(publisher).once();
		replay(hubService);
		
		ModelAndView result = publisherController.editPublisher(5L, new ModelAndView());
		
		verify(hubService);
		
		assertThat((Publisher)result.getModel().get("publisher"), CoreMatchers.equalTo(publisher));
		assertThat((String)result.getModel().get("mode"), CoreMatchers.equalTo("edit"));
		assertThat(result.getViewName(), CoreMatchers.equalTo("management/publisher/editPublisher"));
		
	}
	
	@Test(expected = PublisherNotFoundException.class)
	public void editPublisher_invalid() {
		
		expect(hubService.getExistingPublisher(5L)).andThrow(new PublisherNotFoundException()).once();
		replay(hubService);
		
		publisherController.editPublisher(5L, new ModelAndView());
		
		verify(hubService);
		
	}
	
	@Test
	public void deletePublisher_valid() {
		
		Publisher publisher = new Publisher();
		publisher.setCallbackURL("http://calbackURL/publisher");
		publisher.setId(5L);
		
		expect(hubService.getExistingPublisher(5L)).andReturn(publisher).once();
		replay(hubService);
		
		ModelAndView result = publisherController.deletePublisherConfirmation(5L, new ModelAndView());
		
		verify(hubService);
		
		assertThat((Publisher)result.getModel().get("publisher"), CoreMatchers.equalTo(publisher));
		assertThat(result.getViewName(), CoreMatchers.equalTo("management/publisher/deletePublisherConfirm"));
		
	}
	
	@Test(expected = PublisherNotFoundException.class)
	public void deletePublisher_invalid() {

		expect(hubService.getExistingPublisher(5L)).andThrow(new PublisherNotFoundException()).once();
		replay(hubService);
		
		publisherController.deletePublisherConfirmation(5L, new ModelAndView());
		
		verify(hubService);
		
	}
	
}
