package org.mitre.pushee.enterprise.web;

import java.util.List;

import org.mitre.pushee.hub.model.Aggregator;
import org.mitre.pushee.hub.model.processor.AggregatorProcessor;
import org.mitre.pushee.hub.service.AggregatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


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
	
	private final String jsonView = "jsonAggregatorView";
	
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
	
	/**
	 * API access to get the list of all aggregators.
	 * 
	 * @param  modelAndView  MAV object
	 * @return JSON representation of aggregator list
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value="/getAll")
	public ModelAndView apiGetAllFeeds() {	
		List<Aggregator> aggregators = (List<Aggregator>)service.getAll();

		return new ModelAndView(jsonView, "aggregators", aggregators);
	}

	/**
	 * API access to get a single aggregator by ID
	 * 
	 * @param  aggregatorId  ID of the aggregator to get
	 * @param  modelAndView  MAV object
	 * @return JSON representation of the aggregator
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value="/get")
	public ModelAndView apiGetAggregator(@RequestParam("aggregatorId") Long aggregatorId) {
		
		Aggregator agg = service.getExistingAggregator(aggregatorId);
		
		return new ModelAndView(jsonView, "aggregator", agg);
	}
	
	/**
	 * API access to create a new aggregator.
	 * 
	 * @param aggregatorId
	 * @param displayName
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value="/add")
	public ModelAndView apiAddAggregator(@RequestParam("displayName") String displayName,
			@RequestParam("processorClass") String processorClassName) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		
		Aggregator newAgg = service.createNew(displayName);
		
		Class<?> clazz = Class.forName(processorClassName);
		AggregatorProcessor proc = (AggregatorProcessor)clazz.newInstance();
		newAgg.setProcessor(proc);
		
		return new ModelAndView(jsonView, "newAgg", newAgg);
	}
	
	/**
	 * API access to edit an existing aggregator
	 * 
	 * @param aggregatorId
	 * @param displayName
	 * @param processorClassName
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value="/edit")
	public ModelAndView apiEditAggregator(@RequestParam("aggregatorId") Long aggregatorId, 
			@RequestParam("displayName") String displayName) {
		
		Aggregator agg = service.getExistingAggregator(aggregatorId);
		agg.setDisplayName(displayName);
		service.save(agg);
		
		Aggregator retrieved = service.getExistingAggregator(aggregatorId);
		
		return new ModelAndView(jsonView, "aggregator", retrieved);
	}
	
	/**
	 * API access to remove an aggregator
	 * 
	 * @param aggregatorId
	 * @param modelAndView
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value="/delete")
	public ModelAndView apiDeleteAggregator(@RequestParam Long aggregatorId, ModelAndView modelAndView) {
		
		//First verify that the aggregator exists
		Aggregator agg = service.getExistingAggregator(aggregatorId);
		
		service.remove(agg);
		
		modelAndView.setViewName("management/successfullyRemoved");
		return modelAndView;
	}
	
}
