package com.org.web.security.views;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.omnifaces.cdi.ViewScoped;

import com.org.security.enums.GroupsSecurityRolesNames;
import com.org.security.identity.stereotype.Group;
import com.org.security.service.SecurityManagedService;
import com.org.security.service.SecurityValidate;
import com.org.util.enumeration.ViewStatus;

import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
@Getter
@Setter
public class GroupView extends SecurityBaseView implements Serializable {

	private static final long serialVersionUID = -243534543L;

	/** CDI INJECTION POINT */
	@Inject
	private transient SecurityManagedService securityManagedService;

	@Inject
	private transient SecurityValidate securityValidate;

	@Inject
	private FacesContext facesContext;

	/** INSTANCE FIELD */

	private Group newGroup;
	private Group groupSelected;
	private List<Group> groups;
	private List<GroupsSecurityRolesNames> applicationGroups;

	@PostConstruct
	public void init() {
		groups = new ArrayList<Group>();
		groups = securityManagedService.findAllGroup();
		applicationGroups = Arrays.asList(GroupsSecurityRolesNames.values());
	}

	public void preparedCreatedGroup() {
		setStatus(ViewStatus.NEW);
		newGroup = new Group(StringUtils.EMPTY);
	}

	public void preparedUpdateGroup() {
		setStatus(ViewStatus.EDIT);
	}

	public void saveGroup() {
		boolean isNotExistGroup = !securityValidate.existGroup(newGroup.getName());
		if (isNotExistGroup) {
			securityManagedService.saveGroup(newGroup);
			init();
		} else {
			facesContext.addMessage(null, new FacesMessage("Grupo ya existe"));
		}
	}

	public void updateGroup() {
		securityManagedService.updateGroup(groupSelected);
		init();
	}

	public void deleteGroup() {
		securityManagedService.removeGroup(groupSelected);
		init();
	}

	public String getName(String codeGroup) {
		return GroupsSecurityRolesNames.getGroupsSecurityNames(codeGroup).getDescription();
	}
}
