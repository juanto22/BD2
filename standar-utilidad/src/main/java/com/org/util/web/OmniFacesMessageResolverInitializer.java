package com.org.util.web;

import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;

import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

public class OmniFacesMessageResolverInitializer implements ServletContainerInitializer {
	public void onStartup(Set<Class<?>> c, ServletContext cx) {

		Messages.setResolver(new Messages.Resolver() {
			private static final String BASE_COMMON_NAME = "i18n.safiCommonBundle";
			private static final String BASE_NAME = "i18n.applicationBundle";
			private static final String BASE_PROGRAMATICO = "i18n.programaticaBundle";
			private static final String ERROR_BASE_NAME = "i18n.safiErrorMessage";
			private static final String SECURITY_ERROR_BASE_NAME = "i18n.securityErrorMessage";

			public String getMessage(String message, Object... params) {

				ResourceBundle baseNameBundle = null;
				
				try {
					baseNameBundle = ResourceBundle.getBundle(BASE_COMMON_NAME, Faces.getLocale());
					if (baseNameBundle != null && baseNameBundle.containsKey(message)) {
						message= baseNameBundle.getString(message);
						return MessageFormat.format(message, params);
					}
				} catch (Exception e) {
				}

				try {
					baseNameBundle = ResourceBundle.getBundle(BASE_NAME, Faces.getLocale());
					if (baseNameBundle != null && baseNameBundle.containsKey(message)) {
						message= baseNameBundle.getString(message);
						return MessageFormat.format(message, params);
					}
				} catch (Exception e) {
				}
				
				try {
					baseNameBundle = ResourceBundle.getBundle(BASE_PROGRAMATICO, Faces.getLocale());
					if (baseNameBundle != null && baseNameBundle.containsKey(message)) {
						message= baseNameBundle.getString(message);
						return MessageFormat.format(message, params);
					}
				} catch (Exception e) {
				}

				try {
					baseNameBundle = ResourceBundle.getBundle(SECURITY_ERROR_BASE_NAME, Faces.getLocale());
					if (baseNameBundle != null && baseNameBundle.containsKey(message)) {
						message= baseNameBundle.getString(message);
						return MessageFormat.format(message, params);
					}
				} catch (Exception e) {
				}

				try {
					baseNameBundle = ResourceBundle.getBundle(ERROR_BASE_NAME, Faces.getLocale());
					if (baseNameBundle != null && baseNameBundle.containsKey(message)) {
						message= baseNameBundle.getString(message);
						return MessageFormat.format(message, params);
					}
				} catch (Exception e) {
				}
				return message;
			}
		});
	}
}
