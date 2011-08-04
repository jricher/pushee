/**
 * 
 */
package org.mitre.pushee.oauth.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mitre.pushee.hub.exception.ClientNotFoundException;
import org.mitre.pushee.oauth.service.ClientDetailsEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.provider.ClientAuthenticationToken;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.verification.ClientAuthenticationCache;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author jricher
 *
 */
@Controller
public class OAuthConfirmationController {

	@Autowired
	private ClientDetailsEntityService clientService;
	
	@Autowired
	private ClientAuthenticationCache authenticationCache;
	
	public OAuthConfirmationController() {
		
	}
	
	public OAuthConfirmationController(ClientDetailsEntityService clientService, ClientAuthenticationCache authenticationCache) {
		this.clientService = clientService;
		this.authenticationCache = authenticationCache;
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping("/oauth/user/approve")
	public ModelAndView confimAccess(HttpServletRequest request, HttpServletResponse response, 
			ModelAndView modelAndView) {
		
		// grab our cached authentication from when the client asked for it
		ClientAuthenticationToken clientAuth = authenticationCache.getAuthentication(request, response);
		if (clientAuth == null) {
			throw new IllegalStateException("No client authentication request to authorize");
		}
		
		ClientDetails client = clientService.loadClientByClientId(clientAuth.getClientId());
		
		if (client == null) {
			throw new ClientNotFoundException("Client not found: " + clientAuth.getClientId());
		}
		
		modelAndView.addObject("auth_request", clientAuth);
	    modelAndView.addObject("client", client);
	    modelAndView.setViewName("oauth/approve");
	    
	    return modelAndView;
	}
}
