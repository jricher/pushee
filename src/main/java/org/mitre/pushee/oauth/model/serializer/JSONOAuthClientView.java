package org.mitre.pushee.oauth.model.serializer;

import java.io.Writer;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mitre.pushee.hub.model.Publisher;
import org.mitre.pushee.hub.model.Subscriber;
import org.mitre.pushee.hub.model.Subscription;
import org.mitre.pushee.oauth.model.ClientDetailsEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.servlet.view.AbstractView;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JSONOAuthClientView extends AbstractView {

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Gson gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {

			@Override
			public boolean shouldSkipField(FieldAttributes f) {
				/**/
				if (f.getDeclaringClass().equals(ClientDetailsEntity.class)) {
					if (f.getName().equals("authorities")) {
						// TODO: find a way to push this through as a list of strings
						return true;
					}
				}
				/**/
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
