package org.mitre.pushee.enterprise.web;

import org.mitre.pushee.hub.service.AggregatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for Aggregator management console
 * 
 * @author AANGANES
 *
 */
@Controller
@RequestMapping("/management/aggregators")
public class AggregatorController {

	@Autowired
	private AggregatorService service;
	
	/**
	 * Default constructor
	 */
	public AggregatorController() {
		
	}
	
	/**
	 * Constructor for use in test harnesses
	 * 
	 * @param service
	 */
	public AggregatorController(AggregatorService service) {
		this.service = service;
	}
	
}
