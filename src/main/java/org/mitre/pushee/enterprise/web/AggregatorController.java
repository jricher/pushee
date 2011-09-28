package org.mitre.pushee.enterprise.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import org.mitre.pushee.hub.model.Aggregator;
import org.mitre.pushee.hub.model.processor.AggregatorProcessor;
import org.mitre.pushee.hub.service.AggregatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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
	
	@RequestMapping("")
	public ModelAndView redirectRoot(ModelAndView modelAndView) {
		modelAndView.setViewName("redirect:/manager/aggregators/");
		return modelAndView;
	}
	
	/**
	 * Root page. This page should display the list of current aggregators.
	 * 
	 * @param  modelAndView  MAV object
	 * @return aggregatorIndex page
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value="/")
	public ModelAndView viewAllAggregators(ModelAndView modelAndView) {
		
		Collection<Aggregator> aggs = service.getAll();
		
		modelAndView.addObject("aggregators", aggs);
		modelAndView.setViewName("management/aggregator/aggIndex");
		
		return modelAndView;
	}
	
	/**
	 * View details of a particular aggregator.
	 * 
	 * @param  aggregatorId        ID of the aggregator to view
	 * @param  modelAndView  MAV object
	 * @return viewAggregator page
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value="/view/{aggregatorId}")
	public ModelAndView viewAggregator(@PathVariable Long aggregatorId, ModelAndView modelAndView) {
		
		Aggregator agg = service.getExistingAggregator(aggregatorId);
		
		modelAndView.addObject("aggregator", agg);
		modelAndView.setViewName("management/aggregator/viewAggregator");
		return modelAndView;
	}
	
	@RequestMapping("/add")
	public ModelAndView redirectAdd(ModelAndView modelAndView) {
		modelAndView.setViewName("redirect:/manager/aggregator/add/");
		return modelAndView;
	}
	
	/**
	 * Add an aggregator. 
	 * 
	 * @param  modelAndView  MAV object
	 * @return editAggregator page with mode set to "add"
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value="/add/")
	public ModelAndView addAggregator(ModelAndView modelAndView) throws ClassNotFoundException {
		
		Aggregator agg = new Aggregator();

		/*
		 * The following snippet comes from http://stackoverflow.com/questions/492184/how-do-you-find-all-subclasses-of-a-given-class-in-java/495851#495851
		 * The snippet looks in the "org/mitre/pushee/hub/model/processor/impl" package for 
		 * classes that imlement the AggregatorProcessor interface. Those classes are then presented
		 * in a drop-down selector on the "Add New Aggregator" page as options for the processor type 
		 * on this new aggregator.
		 */
		ArrayList<Class<AggregatorProcessor>> classes = new ArrayList<Class<AggregatorProcessor>>();
		ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(true);
		provider.addIncludeFilter(new AssignableTypeFilter(AggregatorProcessor.class));

		// scan in org.example.package
		Set<BeanDefinition> components = provider.findCandidateComponents("org/mitre/pushee/hub/model/processor/impl");
		for (BeanDefinition component : components)
		{
		    @SuppressWarnings("unchecked")
			Class<AggregatorProcessor> cls = (Class<AggregatorProcessor>) Class.forName(component.getBeanClassName());
		    classes.add(cls);
		}
		
		modelAndView.addObject("mode", "add");
		modelAndView.addObject("aggregator", agg);
		modelAndView.addObject("processors", classes);
		modelAndView.setViewName("management/aggregator/editAggregator");
		
		return modelAndView;
	}
	
	/**
	 * Edit an aggregator.
	 * 
	 * @param  aggregatorId  ID of the aggregator to edit
	 * @param  modelAndView  MAV object
	 * @return editAggregator page with mode set to "edit"
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value="/edit/{aggregatorId}")
	public ModelAndView editAggregator(@PathVariable Long aggregatorId, ModelAndView modelAndView) {
		
		Aggregator agg = service.getExistingAggregator(aggregatorId);
		
		modelAndView.addObject("mode", "edit");
		modelAndView.addObject("aggregator", agg);
		modelAndView.setViewName("management/aggregator/editAggregator");
		
		return modelAndView;
	}
	
	/**
	 * Prompt the user to confirm deletion before committing the action
	 * 
	 * @param  aggregatorId
	 * @param  modelAndView
	 * @return delete confirmation page
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value="/delete/{aggregatorId}")
	public ModelAndView deleteAggregatorConfirmation(@PathVariable Long aggregatorId, ModelAndView modelAndView) {
		
		Aggregator agg = service.getExistingAggregator(aggregatorId);
		
		modelAndView.addObject("aggregator", agg);
		modelAndView.setViewName("management/aggregator/deleteAggregatorConfirm");
		
		return modelAndView;
	}
	
}
