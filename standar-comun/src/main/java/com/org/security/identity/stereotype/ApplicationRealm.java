package com.org.security.identity.stereotype;

import org.picketlink.idm.model.AbstractPartition;
import org.picketlink.idm.model.annotation.IdentityPartition;

@IdentityPartition(supportedTypes = { Role.class, Group.class })
public class ApplicationRealm extends AbstractPartition {

	private ApplicationRealm() {
		this(null);
		// PicketLink requires a default constructor to create and populate
		// instances using reflection
	}

	public ApplicationRealm(String name) {
		super(name);
	}
}
