package org.mitre.pushee.hub.model.serializer;

import java.io.Writer;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mitre.pushee.hub.model.Publisher;
import org.mitre.pushee.hub.model.Subscriber;
import org.mitre.pushee.hub.model.Subscription;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.servlet.view.AbstractView;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Spring view which serializes Feed objects to JSON
 * 
 * @author AANGANES
 *
 */
public class JSONFeedView extends AbstractView {

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
					System.out.println("JSONFeedView checking field " + f.getName() + " from declaring class " + f.getDeclaringClass());
					if (f.getDeclaringClass().equals(Publisher.class)) {
						if (f.getName().equals("feeds")) {
							System.out.println("Skipping this field");
							return true;
						}
					} 
					else if (f.getDeclaringClass().equals(Subscription.class)) {
						if (f.getName().equals("feed")) {
							return true;
						}
					}
					else if (f.getDeclaringClass().equals(Subscriber.class)) {
						if (f.getName().equals("subscriptions")) {
							return true;
						}
					}
					System.out.println("Serializing this field");
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
