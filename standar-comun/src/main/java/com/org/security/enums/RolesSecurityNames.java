package com.org.security.enums;

public enum RolesSecurityNames {

	ADMINS("admins", "Administradores"),

	TEC("tec", "Tecnico"),

	GUEST("guest", "Invitado");

	String code;

	String description;

	private RolesSecurityNames(final String code, final String description) {
		this.code = code;
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

	public static RolesSecurityNames getRolesSecurityNames(final String code) {
		RolesSecurityNames ret = null;
		for (RolesSecurityNames activeEnum : values()) {
			if (activeEnum.getCode().equals(code)) {
				ret = activeEnum;
				break;
			}
		}
		return ret;
	}

}
