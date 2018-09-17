package com.udb.service;

import javax.ejb.Stateless;
import javax.inject.Inject;

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


}
