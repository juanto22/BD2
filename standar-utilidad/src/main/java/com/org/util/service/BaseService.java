package com.org.util.service;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.NonUniqueResultException;
import com.mysema.query.types.Predicate;
import com.org.util.domain.BaseModelEntity;
import com.org.util.safe.AbsSpec;
import com.org.util.safe.GroupedFilter;
import com.org.util.safe.ValueHolder;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;


@Getter
@Setter
@Log
public abstract class BaseService<T extends BaseModelEntity<ID>, ID extends Serializable> extends CrudService<T, ID> {

	public Page<T> findAll(int first, int pageSize, List<Sort.Order> orders, boolean any, boolean custom,
			Set<ValueHolder> conditions, Set<GroupedFilter> groupedFilters, String... raws) {
		Sort sort = new Sort(orders);
		return findAll(first, pageSize, sort, any, custom, conditions, groupedFilters, raws);
	}

	public Page<T> findAll(int first, int pageSize, Sort sort, boolean any, boolean custom, Set<ValueHolder> conditions,
			Set<GroupedFilter> groupedFilters, String... raws) {
		int page = Math.max(first / pageSize, 0);
		PageRequest pageable = new PageRequest(page, pageSize, sort);
		return findAll(pageable, any, custom, conditions, groupedFilters, raws);
	}

	public Page<T> findAll(Pageable pageable, boolean any, boolean custom, Set<ValueHolder> conditions,
			Set<GroupedFilter> groupedFilters, String... raws) {
		BooleanBuilder spec = AbsSpec.builder(vars(), any, custom, conditions, groupedFilters, raws);
		return getRepository().findAll(spec, pageable);
	}

	public Class<?> getType(Class cl, Object value) {
		return String.class;
	}

	public T findOne(Predicate predicate) {
        T result = null;
        try {
            try {
                result = getRepository().findOne(predicate);
            } catch (IllegalArgumentException | NonUniqueResultException e) {
            	log.log(Level.SEVERE, null, e);
            }
        } catch (Exception e) {
        	log.log(Level.SEVERE, null, e);
        }
        return result;
    }

}
