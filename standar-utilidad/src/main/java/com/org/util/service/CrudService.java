package com.org.util.service;

import static com.mysema.query.group.GroupBy.groupBy;
import static com.mysema.query.group.GroupBy.list;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.mysema.query.jpa.impl.JPADeleteClause;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadataFactory;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.path.EntityPathBase;
import com.mysema.query.types.path.NumberPath;
import com.org.util.domain.BaseModelEntity;
import com.org.util.repository.BaseRepository;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class CrudService<T extends BaseModelEntity<ID>, ID extends Serializable> extends BaseCrud<T, ID> {

	public abstract BaseRepository<T, ID> getRepository();

	public T findOne(ID id) {
		return getRepository().findOne(id);
	}

	public List<T> findAll() {
		return getRepository().findAll();
	}

	public List<T> findAll(Predicate predicate) {
		return (List<T>) getRepository().findAll(predicate);
	}

	public List<T> findAll(Predicate predicate, Sort sort) {
		return (List<T>) getRepository().findAll(predicate, sort);
	}

	public Page<T> findAll(Predicate predicate, Pageable pageable) {
		return getRepository().findAll(predicate, pageable);
	}

	public <S extends T> S save(S entity) {
		return getRepository().save(entity);
	}

	public <S extends T> List<S> save(Iterable<S> entities) {
		return getRepository().save(entities);
	}

	public void delete(T entity) {
		getRepository().delete(entity);
	}

	public void delete(Iterable<T> iterable) {
		getRepository().delete(iterable);
	}

	public long removeOne(T entity) {
		EntityPathBase<T> qentity = getQEntity();
		return newJPADeleteClause()
				.where((new NumberPath(idClazz, PathMetadataFactory.forProperty(qentity, "id")).eq(entity.getId())))
				.execute();
	}

	public long remove(EntityPath<?> qentity, BooleanExpression bexp) {
		JPADeleteClause jpadelete = new JPADeleteClause(em, qentity);
		return jpadelete.where(bexp).execute();
	}

	public Map<String, Object> vars() {
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put(ENTITY_VAR, getQEntity());
		return vars;
	}

	public Map<ID, T> findAllAsMap() {
		EntityPathBase<T> entity = getQEntity();
		return (Map<ID, T>) newJpaQuery().from(entity)
				.transform(groupBy(new NumberPath(idClazz, PathMetadataFactory.forProperty(entity, "id"))).as(entity));
	}

	public <A extends Serializable> Map<A, T> findAllAsMapWithPathAsKey(Path<? super A> path) {
		EntityPathBase<T> entity = getQEntity();
		return (Map<A, T>) newJpaQuery().from(entity).transform(groupBy(path).as(entity));
	}

	public <A extends Serializable> Map<A, List<T>> findAllasMapGroupedByPath(Path<? super A> path,
			BooleanExpression criteria) {
		Map<A, List<T>> ret = null;
		EntityPathBase<T> entity = getQEntity();
		JPAQuery oq;
		if (criteria != null) {
			oq = newJpaQuery().from(entity).where(criteria);
		} else {
			oq = newJpaQuery().from(entity);
		}
		ret = (Map<A, List<T>>) oq.transform(groupBy(path).as(list(entity)));
		return ret;
	}

	public <A extends Serializable> Map<A, List<T>> findAllasMapGroupedByPath(Path<? super A> path) {
		return findAllasMapGroupedByPath(path, null);
	}

	public void detach(T... entities) {
		for (T t : entities) {
			em.detach(t);
		}
	}

	public void detach(Collection<T> entities) {
		for (T t : entities) {
			em.detach(t);
		}
	}

	protected <T extends BaseModelEntity<ID>> Collection<T> bulkSave(Collection<T> entities) {
		final List<T> savedEntities = new ArrayList<T>(entities.size());
		int count = 0;
		for (T t : entities) {
			savedEntities.add(persistOrMerge(t));
			count++;
			if (count % BATCH_SIZE == 0) {
				em.flush();
				em.clear();
			}
		}

		em.flush();
		em.clear();
		return savedEntities;
	}

	private <T extends BaseModelEntity<ID>> T persistOrMerge(T t) {
		if (t.getId() == null) {
			em.persist(t);
			return t;
		} else {
			return em.merge(t);
		}
	}

}
