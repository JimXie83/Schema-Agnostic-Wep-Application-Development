package com.jamesx.service.generic;

import com.jamesx.util.*;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jamesx.util.HelperUtil.getEagerLoadingEntityGraphName;

/**************************************************
 * By JamesXie 2016
 *****************************************************/
@Service
@Repository
@Transactional
public class GenericServiceImpl<T> implements IGenericService<T> {
	public Class<T> entityType;
	@PersistenceContext
	public EntityManager entityManager;

	public GenericServiceImpl() {}

	public GenericServiceImpl(Class<T> entityType) { this.entityType = entityType; }

	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	/*********************************************
	 * Eager-Loading to execute findById()
	 ********************************************/
	@Override
	public <T> T findById(Integer id) {
		// get default eager-loading Entity Graph Name, e.g. "graph.EntityCalssName.eagerLoad"
		EntityGraph graph = entityManager.getEntityGraph(getEagerLoadingEntityGraphName(entityType));
		Map hints = new HashMap();
		hints.put("javax.persistence.fetchgraph", graph);
		T entity = (T) entityManager.find((Class<T>) entityType, id, hints);
		return entity;
	}

	/********************************************************************************************
	 *  retrieve records by specified criteria
	 * @param pagingInfo
	 * @param searchFilters
	 * @param <T>
	 * @return: paged result list
	 ********************************************************************************************/
	@Override
	public <T> PagingInfo findByCriteriaPaging(PagingInfo pagingInfo, SearchFilter[] searchFilters) {
		List<T> resultList=new ArrayList<>();
		MyCriteriaQuery myQuery = new MyCriteriaQuery(entityManager, this.entityType);
		myQuery.createWhereClause(searchFilters);
		pagingInfo.totalCount = myQuery.count().intValue();
		// if not record foudn, no need to execute the query
		if (pagingInfo.totalCount>0) resultList = myQuery.pagedList(pagingInfo);
		pagingInfo.serverData = resultList;
		return pagingInfo;
	}

	/********************************************************************************************
	 *  search by criteria list
	 *
	 * @param predicateList
	 * @param <T>
	 * @return: paged result list
	 ********************************************************************************************/
	@Override
	public <T> List<T> findByCriteriaList(List<Criterion> predicateList) {
		Session session = entityManager.unwrap(Session.class);
		// create a new Criteria object for the current session
		Criteria sessionCriteria = session.createCriteria((Class) this.entityType);
		for (Criterion predicate : predicateList) sessionCriteria.add(predicate);
		List<T> list = sessionCriteria.list();
		return list;
	}

	/********************************************************************************************
	 *  search by example, entity contains data,
	 *  null or empty field will not partake in search
	 * @param entity
	 * @param <T>
	 * @return: result list
	 ********************************************************************************************/
	@Override
	public <T> List<T> findByCriteria(T entity) {
		Session session = entityManager.unwrap(Session.class);
		// create a new Criteria object for the current session
		Criteria sessionCriteria = session.createCriteria((Class) this.entityType);
		// convert entity into search filters, and add to the created Criteria
		List<Criterion> searchCriteria = HelperUtil.generateCriteria(entity);
		//add all search conditions into a Criterion Object
		for (Criterion crit : searchCriteria) sessionCriteria.add(crit);
		List<T> list = sessionCriteria.list();
		return list;
	}

	/********************************************************************************************
	 *  find all-deprecated
	 ********************************************************************************************/
	@Override
	public <T> List<T> findAll() {
		return entityManager.createQuery("from " + ((Class) entityType).getName()).getResultList();
	}

	/********************************************************************************************
	 * save the entity to DB
	 * @param entity
	 * @param <T>
	 * @return
	 ********************************************************************************************/
	@Override
	@Transactional(propagation = Propagation.REQUIRED) //Propagation.REQUIRED for returning generated id
	public <T> T save(T entity) {
		//entityManager.persist(entity);
		return entityManager.merge(entity);
	}

	/********************************************************************************************
	 * update a list of entties to DB
	 * @param entityList
	 * @param <T>
	 ********************************************************************************************/
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public <T> void batchSave(List<T> entityList) {
		for (T obj : entityList) {
			// if entity is not changed, no need to merge it
			if (((IObjectWithState) obj).getObjectState() == ObjectState.Unchanged) {
				continue;
			}
			T managedEntity = entityManager.merge(obj);
			// if ObjectState is Deleted, use manage entity to delete
			if (((IObjectWithState) obj).getObjectState() == ObjectState.Deleted) {
				entityManager.remove(managedEntity);
			}
		}
		entityManager.flush();
		entityManager.clear();
	}

	@Override
	public <T> T update(T entity) {
		entityManager.merge(entity);
		return entity;
	}

	@Override
	public <T> void delete(T entity) {
		entityManager.remove(entity);
	}

	@Override
	public <T> void deleteById(Integer entityId) {
		T entity = findById(entityId);
		delete((T) entity);
	}

	@Override
	public void setEntityType(Class<T> entityType) {
		this.entityType = entityType;
	}

}
