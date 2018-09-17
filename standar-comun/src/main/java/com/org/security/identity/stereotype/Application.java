package com.org.security.identity.stereotype;

import org.picketlink.idm.model.AbstractIdentityType;
import org.picketlink.idm.model.annotation.AttributeProperty;
import org.picketlink.idm.model.annotation.IdentityPartition;
import org.picketlink.idm.model.annotation.Unique;

import lombok.Getter;
import lombok.Setter;

@IdentityPartition(supportedTypes = { Role.class, Group.class })
@Getter
@Setter
public class Application extends AbstractIdentityType {

	@AttributeProperty
	@Unique
	private String name;

	private Application() {
		this(null);
		// PicketLink requires a default constructor to create and populate
		// instances using reflection
	}

	public Application(String name) {
		this.name = name;
	}

}
