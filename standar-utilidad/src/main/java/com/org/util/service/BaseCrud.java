package com.org.util.service;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.logging.Level;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.springframework.util.StringUtils;

import com.mysema.query.jpa.impl.JPADeleteClause;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.jpa.impl.JPAUpdateClause;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.PathMetadataFactory;
import com.mysema.query.types.path.EntityPathBase;
import com.mysema.query.types.path.NumberPath;
import com.org.util.domain.BaseModelEntity;
import com.rits.cloning.Cloner;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;

@Getter
@Setter
@Log
public class BaseCrud<T extends BaseModelEntity<ID>, ID extends Serializable> {

	public final static String ENTITY_VAR = "entity";
	public final static PropertyUtilsBean PROP_UTILS = new PropertyUtilsBean();
	protected final static int BATCH_SIZE = 100;

	protected Class clazz;
	protected Class idClazz;

	protected transient Cloner cloner = new Cloner();

	@Inject
	protected EntityManager em;

	protected JPAQuery query;

	public BaseCrud() {
		Type genericSuperclass = null;
		Type[] args = null;
		genericSuperclass = getClass().getGenericSuperclass();
		if (genericSuperclass instanceof ParameterizedType) {
			args = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments();
		}

		if (args != null && args.length >= 2) {
			clazz = (Class) args[0];
			idClazz = (Class) args[1];
		}
	}

	public Session getSession() {
		return em.unwrap(Session.class);
	}

	public JPAQuery newJpaQuery() {
		return new JPAQuery(em);
	}

	public long deleteOne(T entity) {
		EntityPathBase<T> qentity = getQEntity();
		return newJPADeleteClause()
				.where(new NumberPath(idClazz, PathMetadataFactory.forProperty(qentity, "id")).eq(entity.getId()))
				.execute();
	}

	public JPADeleteClause newJPADeleteClause() {
		return new JPADeleteClause(em, getQEntity());
	}

	public JPADeleteClause newJPADeleteClause(EntityPath<?> entity) {
		return new JPADeleteClause(em, entity);
	}

	protected JPAUpdateClause newJPAUpdateClause(EntityPath<?> entity) {
		return new JPAUpdateClause(em, entity);
	}

	public EntityPathBase<T> getQEntity() {
		EntityPathBase<T> ret = null;
		try {
			int lastpoint = getClazz().getName().lastIndexOf('.');
			String qname = getClazz().getName();
			String sname = qname.substring(0, lastpoint) + ".Q" + qname.substring(lastpoint + 1, qname.length());
			Class claxx = Class.forName(sname);
			Constructor<?> ctor = claxx.getConstructor(String.class);
			String ctorArgument = StringUtils.uncapitalize(getClazz().getSimpleName());
			ret = (EntityPathBase<T>) ctor.newInstance(new Object[] { ctorArgument });
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
				| IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
			log.log(Level.SEVERE, null, ex);
		}
		return ret;
	}

	public void doWork(Work work) throws Exception {
		getSession().doWork(work);
	}

}
