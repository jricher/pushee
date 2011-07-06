package org.mitre.pushee.hub.model.serializer;

import java.io.Writer;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mitre.pushee.hub.model.Feed;
import org.mitre.pushee.hub.model.Subscription;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.servlet.view.AbstractView;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author AANGANES
 *
 */
public class JSONSubscriberView extends AbstractView {

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.view.AbstractView#renderMergedOutputModel(java.util.Map, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void renderMergedOutputModel(Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Gson gson = new GsonBuilder()
			.setExclusionStrategies(new ExclusionStrategy() {
				
				@Override
				public boolean shouldSkipField(FieldAttributes f) {
					if (f.getDeclaringClass().equals(Subscription.class)) {
						//When serializing subscriptions, ignore the subscriber 
						//(since it refers to THIS)
						if (f.getName().equals("subscriber")) {
							return true;
						}
					}
					else if (f.getDeclaringClass().equals(Feed.class)) {
						//When serializing the feeds this subscriber has subscriptions 
						//to, only show ID, URL, and type
						if (f.getName().equals("publisher")) {
							return true;
						}
						else if (f.getName().equals("subscriptions")) {
							return true;
						}
					}
					return false;
				}
				
				@Override
				public boolean shouldSkipClass(Class<?> clazz) {
					// skip the JPA binding wrapper
					if (clazz.equals(BeanPropertyBindingResult.class)) {
						return true;
					}
					return false;
				}
	
			}).create();
		
		response.setContentType("application/json");
		
		Writer out = response.getWriter();
		
		Object obj = model.get("entity");
		if (obj == null) {
			obj = model;
		}
		
		gson.toJson(obj, out);

	}

}
