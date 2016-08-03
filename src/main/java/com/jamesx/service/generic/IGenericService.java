package com.jamesx.service.generic;

import com.jamesx.util.PagingInfo;
import com.jamesx.util.SearchFilter;
import org.hibernate.criterion.Criterion;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
/**************************************************
 * By JamesXie 2016
 **************************************************/
public interface IGenericService<T> {
	EntityManager getEntityManager();

	<T> T findById(Integer id);

	<T> PagingInfo findByCriteriaPaging(PagingInfo pagingInfo,SearchFilter[] searchFilters);

	// search by example, entity contains data,
	// null or empty field will not partake in search
	<T> List<T> findByCriteriaList(List<Criterion> predicateList);

	<T> List<T> findByCriteria(T entity);

	<T> List<T> findAll();

	// Propagation.REQUIRED for returning generated id
	@Transactional(propagation = Propagation.REQUIRED)
	<T> T save(T entity);

	@Transactional(propagation = Propagation.REQUIRED)
	<T> void batchSave(List<T> entityList);

	<T> T update(T entity);

	<T> void delete(T entity);

	<T> void deleteById(Integer entityId);

	void setEntityType(Class<T> entityType);

//	//public void setClazz(Class<T> clazzToSet);
//
//	public <T> T findById(Integer id);
//
//	// search by example, entity contains data,
//	//null or empty field will not partake in search
//	public <T> List<T> findByCriteriaList(List<Criterion> predicateList);
//	public <T> List<T> findByCriteria(T entity);
//
//	public <T> List<T> findAll();
//
//	public <T> T save(T entity);
//
//	public <T> void batchSave(List<T> entityList);
//
//	public <T> T update(T entity);
//
//	public <T> void delete(T entity);
//
//	public <T> void deleteById(Integer entityId);
//	public EntityManager getEntityManager();

}
