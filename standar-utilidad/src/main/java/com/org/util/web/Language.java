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
import java.util.Locale;

/**
 * Clase encargada de guardar los temas de estilos de prime.
 *
 * @author Luis Felipe Sosa Alvarez <luisfsosa@gmail.com>
 */
public class Language implements Serializable {

    /**
     * Serial de La Clase.
     */
    private static final long serialVersionUID = -664718116078675394L;

    /** La id. */
    private Integer id;

    /** La display name. */
    private String displayName;

    /** La locale. */
    private Locale locale;

    /** La image. */
    private String image;

    /**
     * Constructor por defecto de la clase language.
     */
    public Language() {
    }

    /**
     * Constructor por defecto de la clase language.
     *
     * @param id
     *            the id
     * @param displayName
     *            the display name
     * @param locale
     *            the locale
     * @param image
     *            the image
     */
    public Language(final Integer id, final String displayName,
            final Locale locale, final String image) {
        this.id = id;
        this.displayName = displayName;
        this.locale = locale;
        this.image = image;
    }

    /**
     * Retorna el valor del Campo id.
     *
     * @return El valor del Campo id
     */
    public final Integer getId() {
        return id;
    }

    /**
     * Modifica el valor del Campo id.
     *
     * @param id
     *            El nuevo valor que tendra el Campo id
     */
    public final void setId(final Integer id) {
        this.id = id;
    }

    /**
     * Retorna el valor del Campo display name.
     *
     * @return El valor del Campo display name
     */
    public final String getDisplayName() {
        return displayName;
    }

    /**
     * Modifica el valor del Campo display name.
     *
     * @param displayName
     *            El nuevo valor que tendra el Campo display name
     */
    public final void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }

    /**
     * Retorna el valor del Campo locale.
     *
     * @return El valor del Campo locale
     */
    public final Locale getLocale() {
        return locale;
    }

    /**
     * Modifica el valor del Campo locale.
     *
     * @param locale
     *            El nuevo valor que tendra el Campo locale
     */
    public final void setLocale(final Locale locale) {
        this.locale = locale;
    }

    /**
     * Retorna el valor del Campo image.
     *
     * @return El valor del Campo image
     */
    public final String getImage() {
        return image;
    }

    /**
     * Modifica el valor del Campo image.
     *
     * @param image
     *            El nuevo valor que tendra el Campo image
     */
    public final void setImage(final String image) {
        this.image = image;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals(final Object obj) {

        if (null == obj) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        if (!getClass().equals(obj.getClass())) {
            return false;
        }

        Language that = (Language) obj;

        return null == this.getId() ? false : this.getId().equals(that.getId());
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode() {

        int hashCode = 17;

        hashCode += null == getId() ? 0 : getId().hashCode() * 31;

        return hashCode;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString() {
        return displayName;
    }
}