package com.udb.service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.persistence.Query;

import org.hibernate.jdbc.Work;

import com.org.util.repository.BaseRepository;
import com.org.util.service.BaseService;
import com.udb.model.Empleado;
import com.udb.repository.EmpleadoRepository;

@Stateless
public class EmpleadoService extends BaseService<Empleado, Long> {

	@Inject
	private EmpleadoRepository departamentoRepository;

	@Override
	public BaseRepository<Empleado, Long> getRepository() {
		return departamentoRepository;
	}
	
	public List<Object> insertarEmpleado(Empleado empleado) throws Exception {
        List<Object> response = new ArrayList<>();

       
			doWork(new Work() {
			    @Override
			    public void execute(Connection connection) throws SQLException {
			        String procedureName = "{call  insertar_empleado(?,?,?,?,?,?)}";
			        try (CallableStatement callable = connection.prepareCall(procedureName)) {
			            callable.setString(1, empleado.getCargo());
			            callable.setString(2, empleado.getCodigo());
			            callable.setDate(3, new java.sql.Date(empleado.getFechaContratacion().getTime()));
			            callable.setString(4, empleado.getName());
			            callable.setDouble(5, empleado.getSalario());
			            callable.setString(6, empleado.getLastUser().getId());
			            
			            boolean ret = callable.execute();
			        }
			    }

			});
		

        return response;
    }
	
	public List<Object> updateEmpleado(Empleado empleado) throws Exception {
		List<Object> response = new ArrayList<>();
		
		
		doWork(new Work() {
			@Override
			public void execute(Connection connection) throws SQLException {
				String procedureName = "{call  actualizar_empleado(?,?,?,?,?,?,?)}";
				try (CallableStatement callable = connection.prepareCall(procedureName)) {
					callable.setLong(1, empleado.getId());
					callable.setString(2, empleado.getCargo());
					callable.setString(3, empleado.getCodigo());
					callable.setDate(4, new java.sql.Date(empleado.getFechaContratacion().getTime()));
					callable.setString(5, empleado.getName());
					callable.setDouble(6, empleado.getSalario());
					callable.setString(7, empleado.getLastUser().getId());
					
					boolean ret = callable.execute();
				}
			}
			
		});
		
		
		return response;
	}
	
	public List<Object> eliminarEmpleado(Empleado empleado) throws Exception {
		List<Object> response = new ArrayList<>();
		
		
		doWork(new Work() {
			@Override
			public void execute(Connection connection) throws SQLException {
				String procedureName = "{call  eliminar_empleado(?)}";
				try (CallableStatement callable = connection.prepareCall(procedureName)) {
					callable.setLong(1, empleado.getId());
					
					boolean ret = callable.execute();
				}
			}
			
		});
		
		
		return response;
	}
	
	public List<SelectItem> getTablesName() {
        
		List<SelectItem> items = new ArrayList<SelectItem>();
        
        Query query = em.createNativeQuery("SELECT table_name FROM user_tables");
        List<String> list = query.getResultList();
        
        for (String tableName : list) {
            SelectItem item = new SelectItem(tableName, tableName);
            items.add(item);
        }
        return items;
    }
	
	public List<SelectItem> getDIRSNames() {
		
		List<SelectItem> items = new ArrayList<SelectItem>();
		
		Query query = em.createNativeQuery("SELECT DIRECTORY_NAME, DIRECTORY_PATH FROM ALL_DIRECTORIES");
		List<Object[]> list = query.getResultList();
		
		for (Object[] dir : list) {
			SelectItem item = new SelectItem(dir[0].toString(), dir[1].toString());
			items.add(item);
		}
		return items;
	}

	public List<Object> doBackUpDataBase(String jobMode, String expName, 
			String schemaTableEtc, String dondeGuardar) throws Exception {
        List<Object> response = new ArrayList<>();

       
			doWork(new Work() {
			    @Override
			    public void execute(Connection connection) throws SQLException {
			        String procedureName = "{call  BACK_UP_DB(?,?,?,?)}";
			        try (CallableStatement callable = connection.prepareCall(procedureName)) {
			            callable.setString(1, jobMode);
			            callable.setString(2, expName);
			            callable.setString(3, schemaTableEtc);
			            callable.setString(4, dondeGuardar);

			            boolean ret = callable.execute();
			        }
			    }

			});
		

        return response;
    }
	
	public List<Object> doTxtTable(String tableName, String fileName) throws Exception {
		List<Object> response = new ArrayList<>();
		
		doWork(new Work() {
			@Override
			public void execute(Connection connection) throws SQLException {
				String procedureName = "{call  EMP_TXT(?,?)}";
				try (CallableStatement callable = connection.prepareCall(procedureName)) {
					callable.setString(1, tableName);
					callable.setString(2, fileName);
					
					boolean ret = callable.execute();
				}
			}
			
		});
		
		
		return response;
	}
	
	public List<Object> doImpTXT(String fileName) throws Exception {
		List<Object> response = new ArrayList<>();
		
		doWork(new Work() {
			@Override
			public void execute(Connection connection) throws SQLException {
				String procedureName = "{call  IMP_TXT(?,?)}";
				try (CallableStatement callable = connection.prepareCall(procedureName)) {
					callable.setString(1, "EXPORT_PLSQLP");
					callable.setString(2, fileName);
					
					boolean ret = callable.execute();
				}
			}
			
		});
		
		
		return response;
	}

}
