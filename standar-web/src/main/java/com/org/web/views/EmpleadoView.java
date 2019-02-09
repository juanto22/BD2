package com.org.web.views;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

import org.hibernate.exception.GenericJDBCException;
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
	
	private List<SelectItem> dirsList;
	
	/**
	 * Para respaldo
	 */
	
	private String jobMode;
	private String expName;
	private String schemaTableEtc;
	private String dondeGuardar;

	@PostConstruct
	public void init() {
		loadLazyData();
		statusView = ViewStatus.NONE;
		tablesList = empleadoService.getTablesName();
		
		dirsList = empleadoService.getDIRSNames();
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
		try {
			empleadoService.insertarEmpleado(selectedItem);
			Messages.create("Información").detail("Registro ingresado exitosamente").add();
		} catch (Exception e) {
			if(e.getCause() instanceof GenericJDBCException) {
				GenericJDBCException generic = (GenericJDBCException) e.getCause();
				if(generic.getCause() instanceof SQLException) {
					SQLException sql = (SQLException) generic.getCause();
					Messages.create("ERROR").detail("No se pudo ingresar: " + sql.getMessage()).error().add();
				}
				e.getCause().getCause().getMessage();
			}else {
				Messages.create("ERROR").detail("No se pudo ingresar").error().add();
			}
			
			
		}
		

	}

	public void update() {
		handleUser();
		try {
			empleadoService.updateEmpleado(selectedItem);
			Messages.create("Información").detail("Registro actualizado exitosamente").add();
		} catch (Exception e) {
			Messages.create("ERROR").detail("No se pudo actualizar").error().add();
		}
		

	}
	
	public void handleUser() {
		User user = (User) identity.getAccount();
		UserTypeEntity userType = new UserTypeEntity();
		userType.setId(user.getId());
		selectedItem.setLastUser(userType);
	}

	public void delete(Empleado selectedItem) {
		try {
			empleadoService.eliminarEmpleado(selectedItem);
			Messages.create("Información").detail("Registro eliminado exitosamente").add();
		} catch (Exception e) {
			Messages.create("ERROR").detail("No se pudo eliminar").error().add();
		}
		

	}
	
	public void respaldoBD() {
		try {
			if("SCHEMA".equals(jobMode)) {
				schemaTableEtc = "SIG_LOCAL";
			}
			empleadoService.doBackUpDataBase(jobMode, expName, schemaTableEtc, dondeGuardar);
			Messages.create("RESPALDO").detail("Respaldo realizado correctamente").add();
		} catch (Exception e) {
			Messages.create("RESPALDO").detail("Ocurrio un error al realizar el respaldo").error().add();
		}
	}

}
