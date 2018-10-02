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
import org.primefaces.model.UploadedFile;

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
public class AdvanceOptionsView implements Serializable {

	private static final long serialVersionUID = 43568786343L;

	@Inject
	private Identity identity;
	
	@Inject
	private transient EmpleadoService empleadoService;

	@Inject
	private FacesContext facesContext;

	private ViewStatus statusView;
	
	private List<SelectItem> tablesList;
	
	private UploadedFile file;
	
	
	/**
	 * Para exportar/importar
	 */
	
	private String type;
	private String fileName;
	private String tableName;

	@PostConstruct
	public void init() {
		type = "IMP";
		statusView = ViewStatus.NONE;
		tablesList = empleadoService.getTablesName();
	}
	
	public void exportTable() {
		try {
			empleadoService.doTxtTable(tableName, fileName);
			Messages.create("EXPORTACION").detail("Se exporto a .txt exitosamente").add();
		} catch (Exception e) {
			Messages.create("EXPORTACION").detail("Ocurrio un error al realizar la exportacion de la tabla").error().add();
		}
	}
	
	public void impTable() {
		try {
			empleadoService.doImpTXT(file.getFileName());
			Messages.create("IMPORTACION").detail("Se importo el .txt exitosamente").add();
		} catch (Exception e) {
			Messages.create("IMPORTACION").detail("Ocurrio un error al realizar la importacion del archivo").error().add();
		}
	}

}
