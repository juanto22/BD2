package com.org.security.identity.stereotype;

import static org.picketlink.idm.model.annotation.IdentityStereotype.Stereotype.USER;
import static org.picketlink.idm.model.annotation.StereotypeProperty.Property.IDENTITY_USER_NAME;

import java.util.Date;

import org.picketlink.idm.model.AbstractIdentityType;
import org.picketlink.idm.model.Account;
import org.picketlink.idm.model.annotation.AttributeProperty;
import org.picketlink.idm.model.annotation.IdentityStereotype;
import org.picketlink.idm.model.annotation.StereotypeProperty;
import org.picketlink.idm.model.annotation.Unique;
import org.picketlink.idm.query.QueryParameter;

import lombok.Getter;
import lombok.Setter;

@IdentityStereotype(USER)
@Getter
@Setter
public class User extends AbstractIdentityType implements Account {

	public static final QueryParameter USER_NAME = QUERY_ATTRIBUTE.byName("userName");

	@StereotypeProperty(IDENTITY_USER_NAME)
	@AttributeProperty
	@Unique
	private String userName;

	@AttributeProperty
	private String firstName;

	@AttributeProperty
	private String lastName;

	@AttributeProperty
	private String email;

	@AttributeProperty
	private String middleName;

	@AttributeProperty
	private String telephone;

	@AttributeProperty
	private String address;

	@AttributeProperty
	private int postIndex;

	@AttributeProperty
	private Date registerDate;

	@AttributeProperty
	private Date lastVisitDate;

	private User() {
		this(null);
	}
	
	public User(String userName) {
		this.userName = userName;
	}

}
