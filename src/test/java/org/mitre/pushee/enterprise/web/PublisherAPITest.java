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
 * Unit test for PublisherAPI class
 * 
 * @author AANGANES
 *
 */
public class PublisherAPITest {

	private HubService hubService;
	private PublisherAPI publisherApi;
	
	@Before
	public void setup() {
		hubService = createNiceMock(HubService.class);
		publisherApi = new PublisherAPI(hubService);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void getAll() {
		
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
		
		ModelAndView result = publisherApi.apiGetAllPublishers();
		
		verify(hubService);
		
		assertThat((List<Publisher>)(result.getModel().get("publishers")), CoreMatchers.equalTo(publishers));
	}
	
	@Test
	public void getPublisher_valid() {
		
		Publisher publisher = new Publisher();
		publisher.setCallbackURL("http://publisher.url");
		publisher.setId(5L);
		
		expect(hubService.getExistingPublisher(5L)).andReturn(publisher).once();
		replay(hubService);
		
		ModelAndView result = publisherApi.apiGetPublisher(5L);
		
		verify(hubService);
		
		assertThat((Publisher)(result.getModel().get("publisher")), CoreMatchers.equalTo(publisher));
		
	}
	
	@Test(expected=PublisherNotFoundException.class)
	public void getPublisher_invalid() {
		
		expect(hubService.getExistingPublisher(5L)).andThrow(new PublisherNotFoundException()).once();
		replay(hubService);
		
		publisherApi.apiGetPublisher(5L);
		
		verify(hubService);
		
	}
	
	@Test
	public void addPublisher() {
		
		Publisher publisher = new Publisher();
		publisher.setCallbackURL("http://publisher.url");
		
		expect(hubService.getPublisherByUrl("http://publisher.url")).andReturn(publisher).once();
		replay(hubService);
		
		ModelAndView result = publisherApi.apiAddPublisher("http://publisher.url");
		
		verify(hubService);
		
		assertThat((Publisher)(result.getModel().get("publisher")), CoreMatchers.equalTo(publisher));
		
	}
	
	@Test
	public void editPublisher_valid() {
		
		Publisher publisher = new Publisher();
		publisher.setCallbackURL("http://publisher.url");
		publisher.setId(5L);
		
		Publisher modified = new Publisher();
		modified.setCallbackURL("http://publisher.url/new");
		modified.setId(5L);
		
		expect(hubService.getExistingPublisher(5L)).andReturn(publisher).once();
		expect(hubService.getExistingPublisher(5L)).andReturn(modified).once();
		replay(hubService);
		
		ModelAndView result = publisherApi.apiEditPublisher(5L, "http://publisher.url/new");
		
		verify(hubService);
		
		assertThat((Publisher)(result.getModel().get("publisher")), CoreMatchers.equalTo(modified));
		
	}
	
	@Test(expected=PublisherNotFoundException.class)
	public void editPublisher_invalidPublisher() {
		
		expect(hubService.getExistingPublisher(5L)).andThrow(new PublisherNotFoundException()).once();
		replay(hubService);
		
		publisherApi.apiEditPublisher(5L, "http://publisher.url/new");
		
		verify(hubService);

	}
	
	@Test
	public void deletePublisher_valid() {
		
		Publisher publisher = new Publisher();
		publisher.setCallbackURL("http://publisher.url");
		publisher.setId(5L);
		
		expect(hubService.getExistingPublisher(5L)).andReturn(publisher).once();
		replay(hubService);
		
		ModelAndView result = publisherApi.apiRemovePublisher(5L, new ModelAndView());
		
		verify(hubService);
		
		assertThat(result.getViewName(), CoreMatchers.equalTo("management/successfullyRemoved"));
	}
	
	@Test(expected=PublisherNotFoundException.class)
	public void deletePublisher_invalid() {
		
		expect(hubService.getExistingPublisher(5L)).andThrow(new PublisherNotFoundException()).once();
		replay(hubService);
		
		publisherApi.apiRemovePublisher(5L, new ModelAndView());
		
		verify(hubService);
		
	}
	
}