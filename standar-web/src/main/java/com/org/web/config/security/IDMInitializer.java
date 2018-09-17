package com.org.web.config.security;

import java.util.List;
import java.util.logging.Level;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.RelationshipManager;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.query.IdentityQueryBuilder;

import com.org.security.enums.ApplicationRealmNames;
import com.org.security.enums.GroupsSecurityRolesNames;
import com.org.security.enums.RolesSecurityNames;
import com.org.security.identity.stereotype.Grant;
import com.org.security.identity.stereotype.Group;
import com.org.security.identity.stereotype.GroupMembership;
import com.org.security.identity.stereotype.Realm;
import com.org.security.identity.stereotype.Role;
import com.org.security.identity.stereotype.User;

import lombok.extern.java.Log;

@Singleton
@Startup
@Log
public class IDMInitializer {

	@Inject
	private PartitionManager partitionManager;

	private String adminLoginName = "admin";
	private String adminEmail = "admin@admin.admin";
	private String password = "admin";
	private String adminFirstName = "Admin";
	private String adminLastName = "Admin";

	@PostConstruct
	public void create() throws Exception {
		Realm realm = partitionManager.getPartition(Realm.class, ApplicationRealmNames.SCHOOL_APPLICATION.getCode());

		if (realm == null) {
			log.log(Level.SEVERE, null, "Couldn't find default partition, creating default partition");
			realm = new Realm(ApplicationRealmNames.SCHOOL_APPLICATION.getCode());
			this.partitionManager.add(realm);
		} else {
			log.log(Level.WARNING, "Found default partition!!!!!!!!!!!");
		}

		IdentityManager identityManager = partitionManager.createIdentityManager(realm);
		IdentityQueryBuilder queryBuilder = identityManager.getQueryBuilder();

		List<User> users = queryBuilder.createIdentityQuery(User.class)
				.where(queryBuilder.equal(User.USER_NAME, adminLoginName)).getResultList();

		if (users.size() == 0) {
			// if admin doesn't exist create
			User adminUser = new User(adminLoginName);
			adminUser.setEmail(adminEmail);
			adminUser.setFirstName(adminFirstName);
			adminUser.setLastName(adminLastName);

			identityManager.add(adminUser);
			identityManager.updateCredential(adminUser, new Password(password));

			// create admins group
			Group adminsGroup = addGroup(identityManager, GroupsSecurityRolesNames.ADMINS.getCode());
			
			// create admin role
			Role adminRole = addRole(identityManager, RolesSecurityNames.ADMIN.getCode());
			
			RelationshipManager relationshipManager = partitionManager.createRelationshipManager();

			// add admin as member of admins group
			relationshipManager.add(new GroupMembership(adminUser, adminsGroup));
			
			// Grant the admin role to the user
			relationshipManager.add(new Grant(adminUser, adminRole));
		}

	}

	public Group addGroup(IdentityManager identityManager, String groupName) {
		IdentityQueryBuilder queryBuilder = identityManager.getQueryBuilder();

		List<Group> groups = queryBuilder.createIdentityQuery(Group.class)
				.where(queryBuilder.equal(Group.NAME, groupName)).getResultList();

		if (groups.size() == 0) {
			// if doesn't exist group {{ groupName }} create it
			Group group = new Group(groupName);
			identityManager.add(group);
			return group;
		}
		return groups.get(0);
	}

	public Role addRole(IdentityManager identityManager, String roleName) {
		IdentityQueryBuilder queryBuilder = identityManager.getQueryBuilder();

		List<Role> roles = queryBuilder.createIdentityQuery(Role.class).where(queryBuilder.equal(Role.NAME, roleName))
				.getResultList();

		if (roles.size() == 0) {
			// if doesn't exist role {{ roleName }} create it
			Role role = new Role(roleName);
			identityManager.add(role);
			return role;
		}
		return roles.get(0);
	}

}
