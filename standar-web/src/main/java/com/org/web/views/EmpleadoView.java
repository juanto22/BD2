package com.org.web.views;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Messages;
import org.picketlink.Identity;

import com.org.security.identity.model.UserTypeEntity;
import com.org.security.identity.stereotype.User;
import com.org.util.enumeration.ViewStatus;
import com.org.util.web.BaseLazyModel;
import com.udb.model.Empleado;
import com.udb.service.EmpleadoService;

import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
@Getter
@Setter
public class EmpleadoView implements Serializable {

	private static final long serialVersionUID = 43568786343L;

	@Inject
	private Identity identity;
	
	@Inject
	private transient EmpleadoService empleadoService;

	@Inject
	private FacesContext facesContext;

	private transient BaseLazyModel<Empleado, Long> empleadosLazy;
	private Empleado selectedItem;

	private ViewStatus statusView;
	
	private List<SelectItem> tablesList;

	@PostConstruct
	public void init() {
		loadLazyData();
		statusView = ViewStatus.NONE;
		tablesList = empleadoService.getTablesName();
	}

	private void loadLazyData() {
		empleadosLazy = new BaseLazyModel<Empleado, Long>(empleadoService);
	}

	public void preparedCreate() {
		statusView = ViewStatus.NEW;
		selectedItem = new Empleado();
	}

	public void preparedUpdate() {
		statusView = ViewStatus.EDIT;
	}
	
	public void preparedUpdate(Empleado empleado) {
		statusView = ViewStatus.EDIT;
		selectedItem = empleado;
	}

	public void save() {
		handleUser();
		empleadoService.save(selectedItem);
		Messages.create("Información").detail("Registro ingresado exitosamente").add();

	}

	public void update() {
		handleUser();
		empleadoService.save(selectedItem);
		Messages.create("Información").detail("Registro actualizado exitosamente").add();

	}
	
	public void handleUser() {
		User user = (User) identity.getAccount();
		UserTypeEntity userType = new UserTypeEntity();
		userType.setId(user.getId());
		selectedItem.setLastUser(userType);
	}

	public void delete(Empleado selectedItem) {
		empleadoService.delete(selectedItem);
		Messages.create("Información").detail("Registro eliminado exitosamente").add();

	}

}
