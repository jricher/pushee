package org.mitre.pushee.enterprise.web;

import org.mitre.pushee.hub.service.AggregatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * Controller for Aggregator API
 * 
 * @author AANGANES
 *
 */
@Controller
@RequestMapping("/management/aggregators/api")
public class AggregatorAPI {

	@Autowired
	AggregatorService service;
	
	/**
	 * Default constructor
	 */
	public AggregatorAPI() {
		
	}
	
	/**
	 * Constructor for use in test harnesses
	 * 
	 * @param service
	 */
	public AggregatorAPI(AggregatorService service) {
		this.service = service;
	}
	
	
}
