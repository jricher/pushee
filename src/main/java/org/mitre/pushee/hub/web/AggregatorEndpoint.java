package org.mitre.pushee.hub.web;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.mitre.pushee.hub.model.Aggregator;
import org.mitre.pushee.hub.service.AggregatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/aggregator")
public class AggregatorEndpoint {
	
	@Autowired
	private AggregatorService aggService;
	
	private String hubUrl;
	
	/**
	 * Default constructor
	 */
	public AggregatorEndpoint() {
		
	}

	/**
	 * Constructor for use with testing.
	 * 
	 * @param aggService the AggregatorService to use
	 */
	public AggregatorEndpoint(AggregatorService aggService, String hubUrl) {
		this.aggService = aggService;
		this.hubUrl = hubUrl;
	}
	
	/**
	 * Handle callbacks to an Aggregator's Feed URL
	 * 
	 * @param aggId
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("/feed/{aggId}")
	public ModelAndView handleFeedCalls(@PathVariable Long aggId, ModelAndView modelAndView) {
		
		Aggregator agg = aggService.getById(aggId);
		
		Object content = agg.getContent();
		
		//TODO: is this the right way to return the content?
		modelAndView.addObject(content);
		
		modelAndView.setViewName("accepted");
		return modelAndView;
	}
	
	/**
	 * Handle callbacks to an Aggregator's Source Subscriber URL, which will occur whenever
	 * one of the feeds the Aggregator is subscribed to publishes new content.
	 * 
	 * @param aggId
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("/subscriber/{aggId}")
	public ModelAndView handleSourceSubscriberCalls(@PathVariable Long aggId, @RequestBody String body, ModelAndView modelAndView) {
		
		Aggregator agg = aggService.getById(aggId);
		
		agg.process(body);
		
		//Post to hub URL that the Feed associated with this Aggregator has new content
		String feedUrl = agg.getAggregatorFeed().getUrl();
		
		//autowire hub URL 
		//use httpclient to post to it, as PuSHEndpoint does
		HttpClient hc = new DefaultHttpClient();
		HttpPost post = new HttpPost(hubUrl +"?hub.mode=publish&hub.url=" + feedUrl);

		try {
			HttpResponse response = hc.execute(post);
			int sc = response.getStatusLine().getStatusCode();
			
			//TODO: handle response based on code?
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		modelAndView.setViewName("accepted");
		return modelAndView;
	}
	
	
	/**
	 * @return the service
	 */
	public AggregatorService getAggService() {
		return aggService;
	}

	/**
	 * @param service the service to set
	 */
	public void setAggService(AggregatorService aggService) {
		this.aggService = aggService;
	}

	public String getHubUrl() {
		return hubUrl;
	}

	public void setHubUrl(String hubUrl) {
		this.hubUrl = hubUrl;
	}

}
