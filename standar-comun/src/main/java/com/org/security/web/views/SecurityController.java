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
		
		DefaultSubMenu advanceOptionsMenu = new DefaultSubMenu("Opciones Avanzadas");
		
		item = new DefaultMenuItem();
		item.setValue("Exportar Informacion");
		item.setTarget("/security/role/list.xhtml");
		item.setIcon("icon-hyperlink");
		item.setCommand("#{securityController.onMenuSelect}");
		advanceOptionsMenu.addElement(item);
		
		item = new DefaultMenuItem();
		item.setValue("Importar Informacion");
		item.setTarget("/security/role/list.xhtml");
		item.setIcon("icon-hyperlink");
		item.setCommand("#{securityController.onMenuSelect}");
		advanceOptionsMenu.addElement(item);
		
		item = new DefaultMenuItem();
		item.setValue("Respaldo Automatico");
		item.setTarget("/security/role/list.xhtml");
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

		item.setValue("Asistencia y conducta");
		item.setTarget("/students/consultaAsistencia.xhtml");
		item.setIcon("icon-search");
		item.setCommand("#{securityController.onMenuSelect}");
		firstSubmenu.addElement(item);
		
		item = new DefaultMenuItem();
		item.setValue("Entrega de tareas ex-aula");
		item.setTarget("/students/subirtareas.xhtml");
		item.setIcon("icon-hyperlink");
		item.setCommand("#{securityController.onMenuSelect}");
		firstSubmenu.addElement(item);

		item = new DefaultMenuItem();
		item.setValue("Calificaciones");
		item.setTarget("/students/consultaNotas.xhtml");
		item.setIcon("fa fa-list-alt");
		item.setCommand("#{securityController.onMenuSelect}");
		firstSubmenu.addElement(item);


		model.addElement(firstSubmenu);
	}

	private void guestMenu() {

		DefaultSubMenu firstSubmenu = new DefaultSubMenu("Menú");
		firstSubmenu.setIcon("icon-menu");

		DefaultMenuItem item = new DefaultMenuItem();
		item.setValue("Asistencia");
		item.setTarget("/teachers/asistencia.xhtml");
		item.setIcon("icon-feather");
		item.setCommand("#{securityController.onMenuSelect}");
		firstSubmenu.addElement(item);

		item = new DefaultMenuItem();
		item.setValue("Actividades ex-aula");
		item.setTarget("/teachers/exaula.xhtml");
		item.setIcon("fa fa-folder-open-o");
		item.setCommand("#{securityController.onMenuSelect}");
		firstSubmenu.addElement(item);
		
		item = new DefaultMenuItem();
		item.setValue("Descargar tareas");
		item.setTarget("/teachers/exaulaDownload.xhtml");
		item.setIcon("fa fa-folder-open-o");
		item.setCommand("#{securityController.onMenuSelect}");
		firstSubmenu.addElement(item);

		item = new DefaultMenuItem();
		item.setValue("Control de conducta");
		item.setTarget("/teachers/conducta.xhtml");
		item.setIcon("fa fa-folder-open-o");
		item.setCommand("#{securityController.onMenuSelect}");
		firstSubmenu.addElement(item);
		
		item = new DefaultMenuItem();
		item.setValue("Calificaciones de asignaturas");
		item.setTarget("/teachers/calificaciones.xhtml");
		item.setIcon("icon-book");
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
