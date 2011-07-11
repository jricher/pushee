/**
 * 
 */
package org.mitre.pushee.oauth.web;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.jws.WebParam.Mode;

import org.mitre.pushee.oauth.exception.DuplicateClientIdException;
import org.mitre.pushee.oauth.model.ClientDetailsEntity;
import org.mitre.pushee.oauth.service.ClientDetailsEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.oauth2.provider.DefaultOAuth2GrantManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.base.Function;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
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

	private ClientDetailsEntityService clientService;
	
	@Autowired
	public OAuthClientController(ClientDetailsEntityService clientService) {
		this.clientService = clientService;
	}

	@RequestMapping(value = {"/", ""})
	public ModelAndView viewAllClients(ModelAndView modelAndView) {
		Collection<ClientDetailsEntity> clients = clientService.getAllClients();
		modelAndView.addObject("clients", clients);
		modelAndView.setViewName("/management/oauth/clientIndex");
		
		return modelAndView;
	}
	
	// TODO: the serializtion of this falls over
	@RequestMapping("/api/getAll")
	public ModelAndView apiGetAllClients(ModelAndView modelAndView) {
		
		Collection<ClientDetailsEntity> clients = clientService.getAllClients();
		modelAndView.addObject("clients", clients);
		modelAndView.setViewName("jsonOAuthClientView");
		
		return modelAndView;
	}
	
	@RequestMapping("/add")
	public ModelAndView addClientPage(ModelAndView modelAndView) {
		modelAndView.setViewName("/management/oauth/addClient");
		modelAndView.addObject("availableGrantTypes", DefaultOAuth2GrantManager.GrantType.values());
		modelAndView.addObject("client", new ClientDetailsEntity());
		return modelAndView;
	}
	
	// TODO: i think this needs a fancier binding than just strings on the way in
	@RequestMapping("/api/add")
	public ModelAndView apiAddClient(ModelAndView modelAndView,
			@RequestParam String clientId, @RequestParam String clientSecret, 
			@RequestParam String scope, // space delimited 
			@RequestParam String grantTypes, // space delimited
			@RequestParam(required=false) String redirectUri, 
			@RequestParam String authorities, // space delimited
			@RequestParam(required=false) String name, 
			@RequestParam(required=false) String description, 
			@RequestParam(required=false, defaultValue="false") boolean allowRefresh,
			@RequestParam(required=false) Long accessTokenTimeout, 
			@RequestParam(required=false) Long refreshTokenTimeout, 
			@RequestParam(required=false) String owner
			) {
		
		ClientDetailsEntity oldClient = clientService.loadClientByClientId(clientId);
		if (oldClient != null) {
			throw new DuplicateClientIdException(clientId);
		}
		
		Splitter spaceDelimited = Splitter.on(" ");
		// parse all of our space-delimited lists
		List<String> scopeList = Lists.newArrayList(spaceDelimited.split(scope));
		List<String> grantTypesList = Lists.newArrayList(spaceDelimited.split(grantTypes)); // TODO: make a stronger binding to GrantTypes
		List<GrantedAuthority> authoritiesList = Lists.newArrayList(
				Iterables.transform(spaceDelimited.split(authorities), new Function<String, GrantedAuthority>() {
					@Override
					public GrantedAuthority apply(String auth) {
						return new GrantedAuthorityImpl(auth);
					}
				}));
		
		ClientDetailsEntity client = clientService.createClient(clientId, clientSecret, scopeList, grantTypesList, redirectUri, authoritiesList, name, description, allowRefresh, accessTokenTimeout, refreshTokenTimeout, owner);
		
		modelAndView.addObject("entity", client);
		modelAndView.setViewName("jsonOAuthClientView");
		
		return modelAndView;
	}
	
	@RequestMapping("/delete/{clientId}")
	public ModelAndView deleteClientConfirmation(ModelAndView modelAndView,
			@PathVariable String clientId) {

		ClientDetailsEntity client = clientService.loadClientByClientId(clientId);
		modelAndView.addObject("client", client);
		modelAndView.setViewName("/management/oauth/deleteClientConfirm");
		
		return modelAndView;
	}

	// TODO: map this to a 202/204
	@RequestMapping("/api/delete")
	public ModelAndView apiDeleteClient(ModelAndView modelAndView,
			@RequestParam String clientId) {
		
		ClientDetailsEntity client = clientService.loadClientByClientId(clientId);
		
		clientService.deleteClient(client);
		
		return modelAndView;
	}
	
	@RequestMapping("/edit/{clientId}")
	public ModelAndView editClientPage(ModelAndView modelAndView,
			@PathVariable String clientId) {
		
		ClientDetailsEntity client = clientService.loadClientByClientId(clientId);
		modelAndView.addObject("availableGrantTypes", DefaultOAuth2GrantManager.GrantType.values());
		modelAndView.addObject("client", client);
		modelAndView.setViewName("/management/oauth/editClient");
		
		return modelAndView;
	}
	
	@RequestMapping("/api/update")
	public ModelAndView apiUpdateClient(ModelAndView modelAndView,
			@RequestParam String clientId, @RequestParam String clientSecret, 
			@RequestParam String scope, // space delimited 
			@RequestParam String grantTypes, // space delimited
			@RequestParam(required=false) String redirectUri, 
			@RequestParam String authorities, // space delimited
			@RequestParam(required=false) String name, 
			@RequestParam(required=false) String description, 
			@RequestParam(required=false, defaultValue="false") boolean allowRefresh,
			@RequestParam(required=false) Long accessTokenTimeout, 
			@RequestParam(required=false) Long refreshTokenTimeout, 
			@RequestParam(required=false) String owner			
	) {		
		ClientDetailsEntity client = clientService.loadClientByClientId(clientId);
		
		Splitter spaceDelimited = Splitter.on(" ");
		// parse all of our space-delimited lists
		List<String> scopeList = Lists.newArrayList(spaceDelimited.split(scope));
		List<String> grantTypesList = Lists.newArrayList(spaceDelimited.split(grantTypes)); // TODO: make a stronger binding to GrantTypes
		List<GrantedAuthority> authoritiesList = Lists.newArrayList(
				Iterables.transform(spaceDelimited.split(authorities), new Function<String, GrantedAuthority>() {
					@Override
					public GrantedAuthority apply(String auth) {
						return new GrantedAuthorityImpl(auth);
					}
				}));
		
		client.setClientSecret(clientSecret);
		client.setScope(scopeList);
		client.setAuthorizedGrantTypes(grantTypesList);
		client.setWebServerRedirectUri(redirectUri);
		client.setAuthorities(authoritiesList);
		client.setClientName(name);
		client.setClientDescription(description);
		client.setAllowRefresh(allowRefresh);
		client.setAccessTokenTimeout(accessTokenTimeout);
		client.setRefreshTokenTimeout(refreshTokenTimeout);
		client.setOwner(owner);		
		
		clientService.updateClient(client, client);
		
		modelAndView.addObject("entity", client);
		modelAndView.setViewName("jsonOAuthClientView");
		
		return modelAndView;
	}
	
	@RequestMapping("/view/{clientId}")
	public ModelAndView viewClientDetails(ModelAndView modelAndView,
			@PathVariable String clientId) {
		
		ClientDetailsEntity client = clientService.loadClientByClientId(clientId);
		modelAndView.addObject("client", client);
		modelAndView.setViewName("/management/oauth/viewClient");
		return modelAndView;
	}
	
	@RequestMapping("/api/getById")
	public ModelAndView getClientById(ModelAndView modelAndView,
			@RequestParam String clientId) {
		
		ClientDetailsEntity client = clientService.loadClientByClientId(clientId);
		modelAndView.addObject("entity", client);
		modelAndView.setViewName("jsonOAuthClientView");

		return modelAndView;
	}
}
