package com.org.security.web.views;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;

import org.picketlink.Identity;
import org.picketlink.credential.DefaultLoginCredentials;
import org.primefaces.event.MenuActionEvent;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuItem;
import org.primefaces.model.menu.MenuModel;

import com.org.security.enums.GroupsSecurityRolesNames;
import com.org.security.utils.AuthorizationChecker;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;

@Named
@SessionScoped
@Setter
@Getter
@Log
public class SecurityController implements Serializable {

	private static final long serialVersionUID = -17312982688234323L;

	@Inject
	private Identity identity;

	@Inject
	private FacesContext facesContext;

	@Inject
	private DefaultLoginCredentials loginCredentials;

	@Inject
	private AuthorizationChecker authorizationChecker;

	private MenuModel model;

	@PostConstruct
	public void init() {
		model = new DefaultMenuModel();

		boolean isAdminUser = authorizationChecker.hasGroup(GroupsSecurityRolesNames.ADMINS.getCode());
		boolean isTecnicoUser = authorizationChecker.hasGroup(GroupsSecurityRolesNames.TEC.getCode());
		boolean isInvitadoUser = authorizationChecker.hasGroup(GroupsSecurityRolesNames.GUEST.getCode());
		
		if (isAdminUser) {
			adminMenu();
		}

		if (isTecnicoUser) {
			tecnicoMenu();
		}

		if (isInvitadoUser) {
			guestMenu();
		}

	}

	private void adminMenu() {
		DefaultSubMenu firstSubmenu = new DefaultSubMenu("Administración de seguridad");
		firstSubmenu.setIcon("icon-menu");

		DefaultMenuItem item = new DefaultMenuItem();
		item.setValue("Lista de Usuarios");
		item.setTarget("/security/list.xhtml");
		item.setIcon("icon-hyperlink");
		item.setCommand("#{securityController.onMenuSelect}");
		firstSubmenu.addElement(item);

		item = new DefaultMenuItem();
		item.setValue("Lista de grupos");
		item.setTarget("/security/group/list.xhtml");
		item.setIcon("icon-hyperlink");
		item.setCommand("#{securityController.onMenuSelect}");
		firstSubmenu.addElement(item);

		item = new DefaultMenuItem();
		item.setValue("Lista de roles");
		item.setTarget("/security/role/list.xhtml");
		item.setIcon("icon-hyperlink");
		item.setCommand("#{securityController.onMenuSelect}");
		firstSubmenu.addElement(item);
	
		DefaultSubMenu crudMenu = new DefaultSubMenu("Mantenimiento");
		item = new DefaultMenuItem();
		item.setValue("Empleados");
		item.setTarget("/administration/crud/index.xhtml");
		item.setIcon("icon-hyperlink");
		item.setCommand("#{securityController.onMenuSelect}");
		crudMenu.addElement(item);
		
		firstSubmenu.addElement(crudMenu);
		
		DefaultSubMenu advanceOptionsMenu = new DefaultSubMenu("Opciones Avanzadas");
		
		item = new DefaultMenuItem();
		item.setValue("Exportar/Importar Informacion");
		item.setTarget("/administration/advancedOptions/expimpInformacion.xhtml");
		item.setIcon("icon-hyperlink");
		item.setCommand("#{securityController.onMenuSelect}");
		advanceOptionsMenu.addElement(item);
		
		item = new DefaultMenuItem();
		item.setValue("Respaldo Automatico");
		item.setTarget("/administration/advancedOptions/respaldo.xhtml");
		item.setIcon("icon-hyperlink");
		item.setCommand("#{securityController.onMenuSelect}");
		advanceOptionsMenu.addElement(item);
		
		firstSubmenu.addElement(advanceOptionsMenu);

		model.addElement(firstSubmenu);

	}

	private void tecnicoMenu() {
		DefaultSubMenu firstSubmenu = new DefaultSubMenu("Menú");
		firstSubmenu.setIcon("icon-menu");

		DefaultMenuItem item = new DefaultMenuItem();

		item.setValue("Empleados");
		item.setTarget("/tec/crud/index.xhtml");
		item.setIcon("icon-hyperlink");
		item.setCommand("#{securityController.onMenuSelect}");
		firstSubmenu.addElement(item);

		model.addElement(firstSubmenu);
	}

	private void guestMenu() {
		
		DefaultSubMenu firstSubmenu = new DefaultSubMenu("Menú");
		firstSubmenu.setIcon("icon-menu");

		DefaultMenuItem item = new DefaultMenuItem();
		
		item.setValue("Empleados");
		item.setTarget("/guest/crud/index.xhtml");
		item.setIcon("icon-hyperlink");
		item.setCommand("#{securityController.onMenuSelect}");
		firstSubmenu.addElement(item);

		model.addElement(firstSubmenu);
	}

	public MenuModel getMenuModel() {
		try {
			return model;
		} catch (Exception e) {
			return new DefaultMenuModel();
		}
	}

	public void onMenuSelect(ActionEvent actionEvent) throws IOException {
		MenuItem menu = ((MenuActionEvent) actionEvent).getMenuItem();
		HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();

		String urlWithSessionID = response.encodeRedirectURL(menu.getTarget());
		String uri = facesContext.getExternalContext().getRequestContextPath() + urlWithSessionID;
		facesContext.getExternalContext().redirect(uri);
	}

}
