package com.org.security.utils;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;

import org.picketlink.Identity;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.RelationshipManager;
import org.picketlink.idm.model.Account;
import org.picketlink.idm.query.IdentityQueryBuilder;
import org.picketlink.idm.query.RelationshipQuery;

import com.org.security.identity.stereotype.Grant;
import com.org.security.identity.stereotype.Group;
import com.org.security.identity.stereotype.GroupMembership;
import com.org.security.identity.stereotype.Role;

@Named
@Stateless
public class AuthorizationChecker {

	@Inject
	private Identity identity;

	@Inject
	private PartitionManager partitionManager;

	@Inject
	private IdentityManager identityManager;

	private IdentityQueryBuilder queryBuilder;

	@PostConstruct
	public void init() {
		queryBuilder = identityManager.getQueryBuilder();
	}

	public boolean hasGroup(String groupName) {
		
		List<Group> groups = queryBuilder.createIdentityQuery(Group.class)
				.where(queryBuilder.equal(Group.NAME, groupName)).getResultList();

		if (groups.size() == 1) {
			Group group = groups.get(0);
			Account user = identity.getAccount();

			RelationshipManager relationshipManager = partitionManager.createRelationshipManager();
			RelationshipQuery<GroupMembership> query = relationshipManager
					.createRelationshipQuery(GroupMembership.class);
			query.setParameter(GroupMembership.GROUP, group);
			query.setParameter(GroupMembership.MEMBER, user);

			// user is assigned with two groups
			List<GroupMembership> resultList = query.getResultList();

			return resultList.size() > 0;
		}
		return false;
	}

	public boolean hasRole(String roleName) {

		List<Role> roles = queryBuilder.createIdentityQuery(Role.class).where(queryBuilder.equal(Role.NAME, roleName))
				.getResultList();

		if (roles.size() == 1) {
			Role role = roles.get(0);
			Account user = identity.getAccount();

			RelationshipManager relationshipManager = partitionManager.createRelationshipManager();
			RelationshipQuery<Grant> query = relationshipManager.createRelationshipQuery(Grant.class);
			query.setParameter(Grant.ASSIGNEE, user);
			query.setParameter(Grant.ROLE, role);

			// user is assigned with two groups
			List<Grant> resultList = query.getResultList();

			return resultList.size() > 0;
		}
		return false;
	}

}
