package com.org.util.enumeration;

import lombok.Getter;

@Getter
public enum ViewStatus {

	/**
	 * Constante para cuando no se sabe en que estado esta la pagina actual.
	 */
	NONE(0, "NONE"),
	/**
	 * Constante para el estado de la pagina Agregando.
	 */
	NEW(1, "NEW"),
	/**
	 * Constante para el estado de la pagina Editando.
	 */
	EDIT(2, "EDIT"),

	/**
	 * Constante para el estado de la pagina Eliminando.
	 */
	REMOVE(3, "REMOVE"),

	/**
	 * Constante para el estado de la pagina Consultando.
	 */
	VIEW(4, "VIEW");

	/**
	 * Código de la Enumaración.
	 */
	private int code;

	/**
	 * Descripción de la Enumaración.
	 */
	String description;

	/**
	 * Constructor por defecto.
	 *
	 * @param code
	 *            Código para la nueva Enumeración
	 */
	private ViewStatus(final int code, final String description) {
		this.code = code;
		this.description = description;
	}

	/**
	 * Método que transforma los valores a una Enumeración
	 * 
	 * @param code
	 *            Código que se convertira en la Enumeración
	 * @return Devuelve la Enumeración
	 */
	public static ViewStatus getViewStatus(final int code) {
		ViewStatus ret = null;
		for (ViewStatus ienum : values()) {
			if (ienum.getCode() == code) {
				ret = ienum;
				break;
			}
		}
		return ret;
	}

}
