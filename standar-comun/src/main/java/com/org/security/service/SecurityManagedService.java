package com.org.security.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.RelationshipManager;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.query.IdentityQueryBuilder;
import org.picketlink.idm.query.RelationshipQuery;

import com.org.security.identity.stereotype.Grant;
import com.org.security.identity.stereotype.Group;
import com.org.security.identity.stereotype.GroupMembership;
import com.org.security.identity.stereotype.Role;
import com.org.security.identity.stereotype.User;

@Stateless
public class SecurityManagedService {

	@Inject
	private IdentityManager identityManager;

	@Inject
	private RelationshipManager relationshipManager;

	private IdentityQueryBuilder queryBuilder;

	@PostConstruct
	public void init() {
		queryBuilder = identityManager.getQueryBuilder();
	}

	public List<User> findAllUser() {

		List<User> users = queryBuilder.createIdentityQuery(User.class).getResultList();
		if (users != null && !users.isEmpty()) {
			return users;
		} else {
			return new ArrayList<User>();
		}
	}

	public List<GroupMembership> findAllUserGroup(User user) {
		RelationshipQuery<GroupMembership> query = relationshipManager.createRelationshipQuery(GroupMembership.class);
		query.setParameter(GroupMembership.MEMBER, user);

		List<GroupMembership> userGroups = query.getResultList();

		if (userGroups != null && !userGroups.isEmpty()) {
			return userGroups;
		} else {
			return new ArrayList<GroupMembership>();
		}
	}

	public List<Grant> findAllUserGrant(User user) {
		RelationshipQuery<Grant> query = relationshipManager.createRelationshipQuery(Grant.class);
		query.setParameter(Grant.ASSIGNEE, user);

		List<Grant> userGrats = query.getResultList();

		if (userGrats != null && !userGrats.isEmpty()) {
			return userGrats;
		} else {
			return new ArrayList<Grant>();
		}
	}

	public void saveUser(User user, Password password, Group group, Role role) {
		identityManager.add(user);
		identityManager.updateCredential(user, password);
		relationshipManager.add(new GroupMembership(user, group));
		relationshipManager.add(new Grant(user, role));
	}

	public void updateUser(User user, GroupMembership group, Grant grant) {
		identityManager.update(user);
		boolean isNewGroupRelations = group.getId() == null;
		if (isNewGroupRelations) {
			relationshipManager.add(group);
		} else {
			relationshipManager.update(group);
		}

		boolean isNewRoleRelations = grant.getId() == null;
		if (isNewRoleRelations) {
			relationshipManager.add(grant);
		} else {
			relationshipManager.update(grant);
		}

	}

	public void updatePassword(User user, Password password) {
		identityManager.updateCredential(user, password);
	}

	public void removeUser(User user, GroupMembership group, Grant grant) {
		relationshipManager.remove(group);
		relationshipManager.remove(grant);
		identityManager.remove(user);
	}

	public void removeGroupFromUser(GroupMembership group) {
		relationshipManager.remove(group);
	}

	public void removeGrantFromUser(Grant grant) {
		relationshipManager.remove(grant);
	}

	public List<Group> findAllGroup() {

		List<Group> groups = queryBuilder.createIdentityQuery(Group.class).getResultList();
		if (groups != null && !groups.isEmpty()) {
			return groups;
		} else {
			return new ArrayList<Group>();
		}
	}

	public Group findGroupByName(String groupName) {

		List<Group> groups = queryBuilder.createIdentityQuery(Group.class)
				.where(queryBuilder.equal(Group.NAME, groupName)).getResultList();

		if (groups.size() == 0) {
			return null;
		}
		return groups.get(0);
	}

	public void saveGroup(Group group) {
		identityManager.add(group);
	}

	public void updateGroup(Group group) {
		identityManager.update(group);
	}

	public void removeGroup(Group group) {
		identityManager.remove(group);
	}

	public List<Role> findAllRole() {

		List<Role> roles = queryBuilder.createIdentityQuery(Role.class).getResultList();
		if (roles != null && !roles.isEmpty()) {
			return roles;
		} else {
			return new ArrayList<Role>();
		}
	}

	public Role findRoleByName(String roleName) {

		List<Role> roles = queryBuilder.createIdentityQuery(Role.class).where(queryBuilder.equal(Role.NAME, roleName))
				.getResultList();

		if (roles.size() == 0) {
			return null;
		}
		return roles.get(0);
	}

	public void saveRole(Role role) {
		identityManager.add(role);
	}

	public void updateRole(Role role) {
		identityManager.update(role);
	}

	public void removeRole(Role role) {
		identityManager.remove(role);
	}

}
