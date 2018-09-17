package com.org.web.security.views;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Messages;
import org.picketlink.idm.credential.Password;

import com.org.security.identity.stereotype.Grant;
import com.org.security.identity.stereotype.Group;
import com.org.security.identity.stereotype.GroupMembership;
import com.org.security.identity.stereotype.Role;
import com.org.security.identity.stereotype.User;
import com.org.security.service.SecurityManagedService;
import com.org.security.service.SecurityValidate;
import com.org.util.enumeration.ViewStatus;

import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
@Getter
@Setter
public class UserView extends SecurityBaseView implements Serializable {

	private static final long serialVersionUID = -745345098098L;

	/** CDI INJECTION POINT */
	@Inject
	private transient SecurityManagedService securityManagedService;

	@Inject
	private transient SecurityValidate securityValidate;

	@Inject
	private FacesContext facesContext;

	/** INSTANCE FIELD */

	private User newUser;
	private User userSelected;

	private Password userPassword;
	private String password;
	private String passwordConfirm;

	private Group userGroup;
	private Role userRole;

	private GroupMembership userMembership;
	private Grant userGrant;

	private List<User> users;
	private List<Group> groups;
	private List<Role> roles;

	@PostConstruct
	public void init() {
		users = new ArrayList<User>();
		users = securityManagedService.findAllUser();

		groups = new ArrayList<Group>();
		groups = securityManagedService.findAllGroup();

		roles = new ArrayList<Role>();
		roles = securityManagedService.findAllRole();
	}

	public void preparedCreatedUser() {
		setStatus(ViewStatus.NEW);
		newUser = new User(StringUtils.EMPTY);
	}

	public void preparedUpdateUser() {
		setStatus(ViewStatus.EDIT);
		List<GroupMembership> groupUser = securityManagedService.findAllUserGroup(userSelected);

		if (groupUser != null && !groupUser.isEmpty()) {
			userMembership = groupUser.get(0);
			userGroup = userMembership.getGroup();
		}

		List<Grant> grantsUser = securityManagedService.findAllUserGrant(userSelected);

		if (grantsUser != null && !grantsUser.isEmpty()) {
			userGrant = grantsUser.get(0);
			userRole = userGrant.getRole();
		}
	}

	public void saveUser() {
		boolean isNotExistGroup = !securityValidate.existUser(newUser.getUserName());
		if (isNotExistGroup) {
			boolean equalsPassword = password.equals(passwordConfirm);
			if (equalsPassword) {
				userPassword = new Password(password);
				securityManagedService.saveUser(newUser, userPassword, userGroup, userRole);
				init();
			} else {
				Messages.create("Error").detail("Password no coinciden!!").error().add();
			}
		} else {
			Messages.create("Error").detail("Usuario ya existe!!").error().add();
		}
	}

	public void updateUser() {
		if (isNotNullSelectedUser()) {

			if (userMembership == null) {
				userMembership = new GroupMembership(userSelected, userGroup);
			}

			if (userGrant == null) {
				userGrant = new Grant(userSelected, userRole);
			}

			securityManagedService.updateUser(userSelected, userMembership, userGrant);
			init();
		} else {
			Messages.create("Error").detail("seleccionar usuario!!").error().add();
		}
	}

	public void deleteUser() {
		if (isNotNullSelectedUser()) {
			GroupMembership userGroup = securityManagedService.findAllUserGroup(userSelected).get(0);
			Grant userGrant = securityManagedService.findAllUserGrant(userSelected).get(0);
			boolean isNotNullObjects = userGrant != null && userGrant != null;
			if (isNotNullObjects) {
				securityManagedService.removeUser(userSelected, userGroup, userGrant);
				init();
			}
		} else {
			Messages.create("Error").detail("seleccionar usuario!!").error().add();
		}
	}

	private boolean isNotNullSelectedUser() {
		return userSelected != null;
	}
}
