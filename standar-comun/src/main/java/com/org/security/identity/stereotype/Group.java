package com.org.security.identity.stereotype;

import static org.picketlink.idm.model.annotation.IdentityStereotype.Stereotype.GROUP;
import static org.picketlink.idm.model.annotation.StereotypeProperty.Property.IDENTITY_GROUP_NAME;

import org.picketlink.idm.model.AbstractIdentityType;
import org.picketlink.idm.model.annotation.AttributeProperty;
import org.picketlink.idm.model.annotation.IdentityStereotype;
import org.picketlink.idm.model.annotation.InheritsPrivileges;
import org.picketlink.idm.model.annotation.StereotypeProperty;
import org.picketlink.idm.model.annotation.Unique;
import org.picketlink.idm.query.QueryParameter;

import lombok.Getter;
import lombok.Setter;

@IdentityStereotype(GROUP)
@Getter
@Setter
public class Group extends AbstractIdentityType {

	/**
	 * A query parameter used to query groups by name.
	 */
	public static final QueryParameter NAME = QUERY_ATTRIBUTE.byName("name");

	/**
	 * A query parameter used to query groups by parent.
	 */
	public static final QueryParameter PARENT = QUERY_ATTRIBUTE.byName("parent");

	@StereotypeProperty(IDENTITY_GROUP_NAME)
	@AttributeProperty
	@Unique
	private String name;

	/**
	 * The parent group.
	 */
	@InheritsPrivileges
	@AttributeProperty
	private Group parent;

	// PicketLink requires a default constructor to create and populate
	// instances using reflection
	private Group() {
		this(null);
	}

	public Group(String name) {
		this.name = name;
	}

	public Group(String name, Group parent) {
		this.name = name;
		this.parent = parent;
	}

}
