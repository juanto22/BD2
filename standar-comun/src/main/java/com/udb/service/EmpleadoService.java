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

	public List<Object> doBackUpDataBase(Long idDocumento) throws Exception {
        List<Object> response = new ArrayList<>();

       
			doWork(new Work() {
			    @Override
			    public void execute(Connection connection) throws SQLException {
			        String procedureName = "{call  detalleinformes(?)}";
			        try (CallableStatement callable = connection.prepareCall(procedureName)) {
			            callable.setLong(1, idDocumento);

			            boolean ret = callable.execute();
			        }
			    }

			});
		

        return response;
    }

}
