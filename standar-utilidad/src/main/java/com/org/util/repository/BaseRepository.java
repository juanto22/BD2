package com.org.util.repository;

import java.io.Serializable;

import javax.enterprise.inject.Alternative;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import com.org.util.domain.BaseModelEntity;

@NoRepositoryBean
@Alternative
public interface BaseRepository<T extends BaseModelEntity<ID>, ID extends Serializable> 
				 extends JpaRepository<T, ID>, JpaSpecificationExecutor<T>, 
				 QueryDslPredicateExecutor<T>, Serializable{

}
