package com.org.security.identity.stereotype;

import org.picketlink.idm.model.AbstractPartition;
import org.picketlink.idm.model.annotation.AttributeProperty;
import org.picketlink.idm.model.annotation.IdentityPartition;

import lombok.Getter;
import lombok.Setter;

@IdentityPartition(supportedTypes = {Application.class, User.class, Role.class, Group.class})
@Getter
@Setter
public class Realm extends AbstractPartition {

	@AttributeProperty
	private boolean enforceSSL;

	@AttributeProperty
	private int numberFailedLoginAttempts;

	@AttributeProperty
	private byte[] publickKey;

	@AttributeProperty
	private byte[] privateKey;

	// PicketLink requires a default constructor to create and populate
	// instances using reflection
	private Realm() {
		this(null);
	}

	public Realm(String name) {
		super(name);
	}
}
