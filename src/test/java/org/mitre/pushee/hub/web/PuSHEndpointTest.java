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
    private PuSHEndpoint endpoint;

    @Before
    public void setup() {
        //Create mock dependency instances
        hubService = createNiceMock(HubService.class);
        //Create controller instance and inject dependencies
        endpoint = new PuSHEndpoint(hubService);
    }


    @Test @Ignore
    public void subscriberRequest_valid() {
        //Define Parameter Values to Pass to controller action (Can be done in setup method if common)
        String callback = "";
        String topic = "http://topic.url";
        ClientVerify verify = ClientVerify.ASYNC;
        ModelAndView mav = new ModelAndView();

        //Define expected return objects from domain requests
        Feed feed = new Feed();
        feed.setId(1L);

        Set<Subscriber> subscriber = new HashSet<Subscriber>();
        Subscriber s = new Subscriber();
        s.setId(1L);
        subscriber.add(s);

        //Define expected dependency calls
        /*Behavior syntax for easymock calls with returns other than void:

         *    expect specified method call with specified parameters
         *    and return to the caller the given object # of times
         *
         */
        expect(hubService.getFeedByUrl(callback)).andReturn(feed).once();
        expect(hubService.getSubscribersByFeed(feed)).andReturn(subscriber).once();
        /* Behavior syntax for easymock calls to void methods
         *   make call with parameters
         *   match the last call to the mock object as an expected void
         */
        hubService.saveSubscriber(s);
        expectLastCall();

        //Finalize the mock object and tell it to begin matching expectations
        replay(hubService);

        //Make call to controller
        ModelAndView result = endpoint.subscribeRequest("subscribe", callback,topic, verify, -1, null, null, mav);

        //verify service was called properly
        verify(hubService);

        //Assert Result
        assertThat((Boolean)result.getModelMap().get("success"), CoreMatchers.equalTo(true));

    }
    
    @Test @Ignore
    public void unsubscribeRequest_valid() {
    	//Define Parameter Values to Pass to controller action (Can be done in setup method if common)
        String callback = "";
        String topic = "http://topic.url";
        ClientVerify verify = ClientVerify.ASYNC;
        ModelAndView mav = new ModelAndView();

        //Define expected return objects from domain requests
        Feed feed = new Feed();
        feed.setId(1L);

        hubService.saveFeed(feed);
        expectLastCall();
        
        Set<Subscriber> subscriber = new HashSet<Subscriber>();
        Subscriber s = new Subscriber();
        s.setId(1L);       

        Subscription subscript = new Subscription();
        subscript.setFeed(feed);
        subscript.setSubscriber(s);
        subscript.setId(1L);
        
        s.addSubscription(subscript);
        
        hubService.saveSubscriber(s);
        expectLastCall();
        
        subscriber.add(s);
        expectLastCall();
        
        //Define expected dependency calls
        /*Behavior syntax for easymock calls with returns other than void:

         *    expect specified method call with specified parameters
         *    and return to the caller the given object # of times
         *
         */
        expect(hubService.getFeedByUrl(callback)).andReturn(feed).once();
        expect(hubService.getSubscribersByFeed(feed)).andReturn(subscriber).once();
        /* Behavior syntax for easymock calls to void methods
         *   make call with parameters
         *   match the last call to the mock object as an expected void
         */
        hubService.saveSubscriber(s);
        expectLastCall();

        //Finalize the mock object and tell it to begin matching expectations
        replay(hubService);

        //Make call to controller
        ModelAndView result = endpoint.unsubscribeRequest("unsubscribe", callback, topic, verify, -1, null, null, mav);

        //verify service was called properly
        verify(hubService);

        //Assert Result
        assertThat((Boolean)result.getModelMap().get("success"), CoreMatchers.equalTo(true));
    }
}
