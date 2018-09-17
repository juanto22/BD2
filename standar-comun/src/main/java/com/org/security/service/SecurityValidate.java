package com.org.security.service;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.RelationshipManager;
import org.picketlink.idm.query.IdentityQueryBuilder;

import com.org.security.identity.stereotype.Group;
import com.org.security.identity.stereotype.Role;
import com.org.security.identity.stereotype.User;

@Stateless
public class SecurityValidate {

	@Inject
	private IdentityManager identityManager;

	@Inject
	private RelationshipManager relationshipManager;

	private IdentityQueryBuilder queryBuilder;

	@PostConstruct
	public void init() {
		queryBuilder = identityManager.getQueryBuilder();
	}

	public boolean existGroup(String groupName) {

		List<Group> groups = queryBuilder.createIdentityQuery(Group.class)
				.where(queryBuilder.equal(Group.NAME, groupName)).getResultList();
		if (groups.size() == 0) {
			return false;
		}
		return true;
	}

	public boolean existRole(String roleName) {

		List<Role> roles = queryBuilder.createIdentityQuery(Role.class).where(queryBuilder.equal(Role.NAME, roleName))
				.getResultList();

		if (roles.size() == 0) {
			return false;
		}
		return true;
	}
	
	public boolean existUser(String userName) {

		List<User> user = queryBuilder.createIdentityQuery(User.class)
				.where(queryBuilder.equal(User.USER_NAME, userName)).getResultList();
		if (user.size() == 0) {
			return false;
		}
		return true;
	}

}
