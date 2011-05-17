package org.mitre.pushee.hub.web;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertThat;

import java.util.HashSet;
import java.util.Set;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mitre.pushee.hub.model.Feed;
import org.mitre.pushee.hub.model.Subscriber;
import org.mitre.pushee.hub.model.Subscription;
import org.mitre.pushee.hub.service.HubService;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author mfranklin
 *         Date: 4/4/11
 *         Time: 8:19 AM
 */
public class PuSHEndpointTest {

    private HubService hubService;
    private ClientConnection http;
    private PuSHEndpoint endpoint;

    @Before
    public void setup() {
        //Create mock dependency instances
        hubService = createNiceMock(HubService.class);
        http = createNiceMock(ClientConnection.class);
        //Create controller instance and inject dependencies
        endpoint = new PuSHEndpoint(hubService, http);
    }


    @Test
    public void subscriberRequest_valid() {
        //Define Parameter Values to Pass to controller action (Can be done in setup method if common)
        String callback = "http://callback.url";
        String topic = "http://topic.url";
        ClientVerify verify = ClientVerify.ASYNC;
        String mode = "subscribe";
        String verifyToken = "verify";
        String secret = "secret";
        int leaseSeconds = 0;
        ModelAndView mav = new ModelAndView();

        //Define expected return objects from domain requests
        Feed feed = new Feed();
        feed.setId(1L);
        feed.setUrl(topic);

        Set<Subscriber> subscribers = new HashSet<Subscriber>();
        Subscriber s = new Subscriber();
        s.setId(1L);
        subscribers.add(s);

        //Define expected dependency calls
        /*Behavior syntax for easymock calls with returns other than void:

         *    expect specified method call with specified parameters
         *    and return to the caller the given object # of times
         *
         */
        expect(hubService.getFeedByUrl(topic)).andReturn(feed).once();
        //expect(hubService.getSubscribersByFeed(feed)).andReturn(subscribers).once();
        expect(hubService.getSubscriberByCallbackURL(callback)).andReturn(s).once();
        
		expect(http.verifyCallback(callback, mode, topic, leaseSeconds, verifyToken)).andReturn(Boolean.TRUE).once();

        /* Behavior syntax for easymock calls to void methods
         *   make call with parameters
         *   match the last call to the mock object as an expected void
         */
        hubService.saveSubscriber(s);
        expectLastCall();

        //Finalize the mock object and tell it to begin matching expectations
        replay(hubService);
        replay(http);

        //Make call to controller
        ModelAndView result = endpoint.subscribeRequest(mode, callback,topic, verify, leaseSeconds, secret, verifyToken, mav);
        
        //verify service was called properly
        verify(hubService);
        verify(http);

        //Assert Result
        //assertThat(result.getModelMap().get("success"), CoreMatchers.equalTo(true));
        assertThat(result.getViewName(), CoreMatchers.equalTo(PuSHEndpoint.SUBSCRIPTION_SUCCESS_VIEW));

    }
    
    @Test
    public void unsubscribeRequest_valid() {
    	//Define Parameter Values to Pass to controller action (Can be done in setup method if common)
        String callback = "http://callback.url";
        String topic = "http://topic.url";
        ClientVerify verify = ClientVerify.ASYNC;
        String mode = "unsubscribe";
        String verifyToken = "verify";
        String secret = "secret";
        int leaseSeconds = 0;
        ModelAndView mav = new ModelAndView();

        //Define expected return objects from domain requests
        Feed feed = new Feed();
        feed.setId(1L);
        feed.setUrl(topic);

        //hubService.saveFeed(feed);
        //expectLastCall();
        
        Set<Subscriber> subscribers = new HashSet<Subscriber>();
        Subscriber s = new Subscriber();
        s.setId(1L);       

        Subscription subscript = new Subscription();
        subscript.setFeed(feed);
        subscript.setSubscriber(s);
        subscript.setId(1L);
        
        s.addSubscription(subscript);

        subscribers.add(s);
        
        //Define expected dependency calls
        /*Behavior syntax for easymock calls with returns other than void:

         *    expect specified method call with specified parameters
         *    and return to the caller the given object # of times
         *
         */
        expect(hubService.getFeedByUrl(topic)).andReturn(feed).once();
        //expect(hubService.getSubscribersByFeed(feed)).andReturn(subscriber).once();
        
        expect(hubService.getSubscriberByCallbackURL(callback)).andReturn(s).once();
        
		expect(http.verifyCallback(callback, mode, topic, leaseSeconds, verifyToken)).andReturn(Boolean.TRUE).once();

        /* Behavior syntax for easymock calls to void methods
         *   make call with parameters
         *   match the last call to the mock object as an expected void
         */
        hubService.saveSubscriber(s);
        expectLastCall();

        //Finalize the mock object and tell it to begin matching expectations
        replay(hubService);
        replay(http);

        //Make call to controller
		ModelAndView result = endpoint.unsubscribeRequest(mode, callback, topic, verify, leaseSeconds, secret, verifyToken, mav);

        //verify service was called properly
        verify(hubService);
        verify(http);

        //Assert Result
        assertThat(result.getViewName(), CoreMatchers.equalTo(PuSHEndpoint.UNSUBSCRIPTION_SUCCESS_VIEW));
        //assertThat((Boolean)result.getModelMap().get("success"), CoreMatchers.equalTo(true));
    }
    
    @Test 
    public void publish() {
        String callback = "http://callback.url";
        String topic = "http://topic.url";
        ClientVerify verify = ClientVerify.ASYNC;
        String mode = "unsubscribe";
        String verifyToken = "verify";
        String secret = "secret";
        int leaseSeconds = 0;
        ModelAndView mav = new ModelAndView();

        //Define expected return objects from domain requests
        Feed feed = new Feed();
        feed.setId(1L);
        feed.setUrl(topic);

        //hubService.saveFeed(feed);
        //expectLastCall();
        
        Set<Subscriber> subscribers = new HashSet<Subscriber>();
        Subscriber s = new Subscriber();
        s.setId(1L);       

        Subscription subscript = new Subscription();
        subscript.setFeed(feed);
        subscript.setSubscriber(s);
        subscript.setId(1L);
        
        s.addSubscription(subscript);

        subscribers.add(s);
        
        expect(hubService.getFeedByUrl(topic)).andReturn(feed).once();
        expect(hubService.getSubscribersByFeed(feed)).andReturn(subscribers).once();
        
		http.fetchAndRepublishFeedToSubscribers(feed, subscribers);
		expectLastCall();
		
		replay(hubService);
		replay(http);
		
		ModelAndView result = endpoint.publish(topic, mav);
		
		verify(hubService);
		verify(http);
		
        assertThat(result.getViewName(), CoreMatchers.equalTo(PuSHEndpoint.PUBLISH_SUCCESS_VIEW));
    	
    }
}
