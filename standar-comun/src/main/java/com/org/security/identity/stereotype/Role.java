package com.org.security.identity.stereotype;

import static org.picketlink.idm.model.annotation.IdentityStereotype.Stereotype.ROLE;
import static org.picketlink.idm.model.annotation.StereotypeProperty.Property.IDENTITY_ROLE_NAME;

import org.picketlink.idm.model.AbstractIdentityType;
import org.picketlink.idm.model.annotation.AttributeProperty;
import org.picketlink.idm.model.annotation.IdentityStereotype;
import org.picketlink.idm.model.annotation.StereotypeProperty;
import org.picketlink.idm.model.annotation.Unique;
import org.picketlink.idm.query.QueryParameter;

import lombok.Getter;
import lombok.Setter;

@IdentityStereotype(ROLE)
@Getter
@Setter
public class Role extends AbstractIdentityType {

	/**
     * A query parameter used to set the name value.
     */
    public static final QueryParameter NAME = QUERY_ATTRIBUTE.byName("name");

    @StereotypeProperty(IDENTITY_ROLE_NAME)
    @AttributeProperty
    @Unique
    private String name;
    
    // PicketLink requires a default constructor to create and populate instances using reflection
    private Role() {
    	this(null);
    }
    
    public Role(String name) {
        this.name = name;
    }
	
}
