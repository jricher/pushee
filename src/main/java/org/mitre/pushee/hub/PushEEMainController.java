package org.mitre.pushee.hub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the PushEE base url
 */
@Controller
public class PushEEMainController {

	private static final Logger logger = LoggerFactory.getLogger(PushEEMainController.class);

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String home() {
		logger.info("Welcome to PushEE");
		return "root";
	}
	
}

