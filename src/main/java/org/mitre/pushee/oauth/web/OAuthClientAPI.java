package org.mitre.pushee.oauth.web;

import java.util.Collection;
import java.util.List;

import org.mitre.pushee.hub.exception.ClientNotFoundException;
import org.mitre.pushee.oauth.exception.DuplicateClientIdException;
import org.mitre.pushee.oauth.model.ClientDetailsEntity;
import org.mitre.pushee.oauth.service.ClientDetailsEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.base.Function;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

@Controller
@RequestMapping("/manager/oauth/clients/api")
public class OAuthClientAPI {

	@Autowired
	private ClientDetailsEntityService clientService;
	
	// TODO: i think this needs a fancier binding than just strings on the way in
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping("/add")
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

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping("/delete")
    public ModelAndView apiDeleteClient(ModelAndView modelAndView,
    		@RequestParam String clientId) {
    	
    	ClientDetailsEntity client = clientService.loadClientByClientId(clientId);
    	
    	if (client == null) {
    		throw new ClientNotFoundException("Client not found: " + clientId);
    	}
    	
    	clientService.deleteClient(client);
    	
    	modelAndView.setViewName("management/successfullyRemoved");
    	return modelAndView;
    }

	// TODO: the serializtion of this falls over, don't know why
	@PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping("/getAll")
    public ModelAndView apiGetAllClients(ModelAndView modelAndView) {
    	
    	Collection<ClientDetailsEntity> clients = clientService.getAllClients();
    	modelAndView.addObject("clients", clients);
    	modelAndView.setViewName("jsonOAuthClientView");
    	
    	return modelAndView;
    }

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping("/update")
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
    	
    	if (client == null) {
    		throw new ClientNotFoundException("Client not found: " + clientId);
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

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping("/getById")
    public ModelAndView getClientById(ModelAndView modelAndView,
    		@RequestParam String clientId) {
    	
    	ClientDetailsEntity client = clientService.loadClientByClientId(clientId);
    	
    	if (client == null) {
    		throw new ClientNotFoundException("Client not found: " + clientId);
    	}
    	
    	modelAndView.addObject("entity", client);
    	modelAndView.setViewName("jsonOAuthClientView");
    
    	return modelAndView;
    }

}
