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

import com.org.security.enums.RolesSecurityNames;
import com.org.security.identity.stereotype.Role;
import com.org.security.service.SecurityManagedService;
import com.org.security.service.SecurityValidate;
import com.org.util.enumeration.ViewStatus;

import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
@Getter
@Setter
public class RoleView extends SecurityBaseView implements Serializable {

	private static final long serialVersionUID = -643248768L;

	/** CDI INJECTION POINT */
	@Inject
	private transient SecurityManagedService securityManagedService;

	@Inject
	private transient SecurityValidate securityValidate;

	@Inject
	private FacesContext facesContext;

	/** INSTANCE FIELD */

	private Role newRole;
	private Role roleSelected;
	private List<Role> roles;
	private List<RolesSecurityNames> applicationRoles;

	@PostConstruct
	public void init() {
		roles = new ArrayList<Role>();
		roles = securityManagedService.findAllRole();
		applicationRoles = Arrays.asList(RolesSecurityNames.values());
	}

	public void preparedCreatedRole() {
		setStatus(ViewStatus.NEW);
		newRole = new Role(StringUtils.EMPTY);
	}

	public void preparedUpdateRole() {
		setStatus(ViewStatus.EDIT);
	}
	
	public void saveRole() {
		boolean isNotExistRole = !securityValidate.existRole(newRole.getName());
		if (isNotExistRole) {
			securityManagedService.saveRole(newRole);
			init();
		} else {
			facesContext.addMessage(null, new FacesMessage("Rol ya existe"));
		}
	}

	public void updateRole() {
		securityManagedService.updateRole(roleSelected);
		init();
	}
	
	public void deleteRole() {
		securityManagedService.removeRole(roleSelected);
		init();
	}

	public String getName(String codeRole) {
		return RolesSecurityNames.getRolesSecurityNames(codeRole).getDescription();
	}

}
