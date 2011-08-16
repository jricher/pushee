package org.mitre.pushee.oauth.web;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertThat;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mitre.pushee.oauth.model.OAuth2AccessTokenEntity;
import org.mitre.pushee.oauth.service.OAuth2TokenEntityService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.ModelAndView;

/**
 * Unit test for OAuth IntrospectionEndpoint
 * 
 * @author AANGANES
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/spring/application-context.xml", "classpath:test-context.xml"})
public class IntrospectionEndpointTest {

	IntrospectionEndpoint endpoint;
	OAuth2TokenEntityService tokenServices;
	OAuth2AccessTokenEntity token;
	
	@Before
	public void setup() {
		tokenServices = createNiceMock(OAuth2TokenEntityService.class);
		endpoint = new IntrospectionEndpoint(tokenServices);
		token = new OAuth2AccessTokenEntity();
		token.setValue("tokenValue");
	}
	
	@Test
	public void verify_valid() {
		expect(tokenServices.getAccessToken("tokenValue")).andReturn(token).once();
		replay(tokenServices);
		
		ModelAndView result = endpoint.verify("tokenValue", new ModelAndView());
		verify(tokenServices);
		
		assertThat(result.getViewName(), CoreMatchers.equalTo("tokenIntrospection"));
		assertThat((OAuth2AccessTokenEntity)result.getModel().get("entity"), CoreMatchers.equalTo(token));
	}
	
	@Test
	public void verify_invalid() {
		expect(tokenServices.getAccessToken("tokenValue")).andReturn(null).once();
		replay(tokenServices);
		
		ModelAndView result = endpoint.verify("tokenValue", new ModelAndView());
		verify(tokenServices);
		
		assertThat(result.getViewName(), CoreMatchers.equalTo("tokenNotFound"));
	}
	
}
