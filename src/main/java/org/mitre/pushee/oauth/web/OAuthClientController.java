/**
 * 
 */
package org.mitre.pushee.oauth.web;

import java.util.Collection;
import java.util.List;

import org.mitre.pushee.oauth.model.ClientDetailsEntity;
import org.mitre.pushee.oauth.model.OAuth2AccessTokenEntity;
import org.mitre.pushee.oauth.model.OAuth2RefreshTokenEntity;
import org.mitre.pushee.oauth.service.ClientDetailsEntityService;
import org.mitre.pushee.oauth.service.OAuth2TokenEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.oauth2.provider.DefaultOAuth2GrantManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Lists;


/**
 * 
 * Endpoint for managing OAuth2 clients
 * 
 * @author jricher
 *
 */
@Controller
@RequestMapping("/manager/oauth/clients")
public class OAuthClientController {

	@Autowired
	private ClientDetailsEntityService clientService;

	@Autowired
	private OAuth2TokenEntityService tokenService;
	
	/**
	 * Redirect to the "/" version of the root
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("")
	public ModelAndView redirectRoot(ModelAndView modelAndView) {
		modelAndView.setViewName("redirect:/manager/oauth/clients/");
		return modelAndView;
	}
	
	/**
	 * View all clients
	 * @param modelAndView
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping("/")
	public ModelAndView viewAllClients(ModelAndView modelAndView) {
		Collection<ClientDetailsEntity> clients = clientService.getAllClients();
		modelAndView.addObject("clients", clients);
		modelAndView.setViewName("/management/oauth/clientIndex");
		
		return modelAndView;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping("/add")
	public ModelAndView redirectAdd(ModelAndView modelAndView) {
		modelAndView.setViewName("redirect:/manager/oauth/clients/add/");
		return modelAndView;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping("/add/")
	public ModelAndView addClientPage(ModelAndView modelAndView) {
		
		List<GrantedAuthority> auth = Lists.newArrayList();
		auth.add(new GrantedAuthorityImpl("ROLE_CLIENT"));
		
		ClientDetailsEntity client = ClientDetailsEntity.makeBuilder()
				.setScope(Lists.newArrayList("scope"))
				.setAuthorities(auth) // why do we have to pull this into a separate list?
				.setAuthorizedGrantTypes(Lists.newArrayList(DefaultOAuth2GrantManager.GrantType.authorization_code.toString()))
				.finish();
		modelAndView.addObject("availableGrantTypes", DefaultOAuth2GrantManager.GrantType.values());
		modelAndView.addObject("client", client);
		
		modelAndView.setViewName("/management/oauth/editClient");
		return modelAndView;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping("/delete/{clientId}")
	public ModelAndView deleteClientConfirmation(ModelAndView modelAndView,
			@PathVariable String clientId) {

		ClientDetailsEntity client = clientService.loadClientByClientId(clientId);
		modelAndView.addObject("client", client);
		modelAndView.setViewName("/management/oauth/deleteClientConfirm");
		
		return modelAndView;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping("/edit/{clientId}")
	public ModelAndView editClientPage(ModelAndView modelAndView,
			@PathVariable String clientId) {
		
		ClientDetailsEntity client = clientService.loadClientByClientId(clientId);
		
		modelAndView.addObject("availableGrantTypes", DefaultOAuth2GrantManager.GrantType.values());
		modelAndView.addObject("client", client);
		modelAndView.setViewName("/management/oauth/editClient");
		
		return modelAndView;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping("/view/{clientId}")
	public ModelAndView viewClientDetails(ModelAndView modelAndView,
			@PathVariable String clientId) {
		
		ClientDetailsEntity client = clientService.loadClientByClientId(clientId);
		
		List<OAuth2AccessTokenEntity> accessTokens = tokenService.getAccessTokensForClient(client);
		List<OAuth2RefreshTokenEntity> refreshTokens = tokenService.getRefreshTokensForClient(client);
		
		modelAndView.addObject("client", client);
		modelAndView.addObject("accessTokens", accessTokens);
		modelAndView.addObject("refreshTokens", refreshTokens);
		
		modelAndView.setViewName("/management/oauth/viewClient");
		return modelAndView;
	}
}
