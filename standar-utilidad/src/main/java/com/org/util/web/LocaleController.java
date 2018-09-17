/*
 * Copyright 2014 - 2015
 * Ministerio de Hacienda
 *
 * Este programa es de uso exclusivo para el Ministerio de Hacienda,
 * y está protegido por las leyes de derechos de autor de la
 * República de El Salvador, se prohibe la copia y distribución
 * total o parcial del mismo.
 */
package com.org.util.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;



@Named
@SessionScoped
@Setter
@Getter
public class LocaleController implements Serializable {

	/**
	 * Serial de la clase.
	 */
	private static final long serialVersionUID = -3129840528588351859L;

	/** Los lenguajes disponibles. */
	private List<Language> languages;

	/** El lenguaje actual seleccionado. */
	private Language language;

	/**
	 * Inicializa El Controlador.
	 */
	@PostConstruct
	public void init() {
		Locale locale = new Locale.Builder().setLanguage("es").build();
		languages = new ArrayList<Language>();
		languages.add(new Language(0, "Español", locale, "es.png"));
		languages.add(new Language(1, "Ingles", Locale.ENGLISH, "en.png"));
		language = languages.get(0);
	}

	/**
	 * Language changed.
	 *
	 * @param e
	 *            the e
	 */
	public void languageChanged(ValueChangeEvent e) {

		Language newLocaleValue = (Language) e.getNewValue();

		for (Language language : languages) {

			if (language.equals(newLocaleValue)) {
				FacesContext.getCurrentInstance().getViewRoot()
						.setLocale(language.getLocale());
			}
		}
	}

	/**
	 * Retorna el valor del Campo locale.
	 *
	 * @return El valor del Campo locale
	 */
	public Locale getLocale() {
		if(language==null){
			return languages.get(0).getLocale();
		}
		return language.getLocale();
	}

}