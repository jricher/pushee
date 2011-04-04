package org.mitre.pushee.web;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mitre.pushee.hub.model.Feed;
import org.mitre.pushee.hub.model.Subscriber;
import org.mitre.pushee.hub.service.HubService;
import org.mitre.pushee.hub.web.ClientVerify;
import org.mitre.pushee.hub.web.PuSHEndpoint;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertThat;
import org.hamcrest.CoreMatchers;

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
        Model model = new ExtendedModelMap();

        //Define expected return objects from domain requests
        Feed feed = new Feed();
        feed.setId("UniqueIdShouldBeLong");

        List<Subscriber> subscriber = new ArrayList<Subscriber>();
        Subscriber s = new Subscriber();
        s.setId("THIS SHOULD BE A LONG");
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
        Model result = endpoint.subscribeRequest(callback,topic, verify, -1, null, null, model);

        //verify service was called properly
        verify(hubService);

        //Assert Result
        assertThat((Boolean)result.asMap().get("success"), CoreMatchers.equalTo(true));

    }
}
