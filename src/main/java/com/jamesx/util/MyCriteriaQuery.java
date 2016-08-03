package com.jamesx.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.hibernate.jpa.criteria.predicate.ComparisonPredicate;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;
/**************************************************
 * By JamesXie 2016
 **************************************************/
public class MyCriteriaQuery<T> {

    public CriteriaBuilder criteriaBuilder;// = entityManager.getCriteriaBuilder();
    public CriteriaQuery query;//= cBuilder.createQuery();
    public Root<T> root;
    public Predicate whereClause;
    public EntityManager entityManager;
    public Class<T> entityClass;

     /*******************************************************************
     * Methods to create where clause ...
     * @param searchFilters: contains "searchFilters"
     *******************************************************************/
    public void createWhereClause(SearchFilter[] searchFilters) {
        Predicate predicate = null;
        Gson gson = new GsonBuilder().setDateFormat(HelperUtil.DATE_FORMAT).create();
        for (SearchFilter filter : searchFilters) {
            // convert Json date string to Date for Date/TimeStamp field
            if (filter.type.equals("Date")) filter.value = gson.fromJson(gson.toJson(filter.value), Date.class);
            // create a predicate for WhereClause
            predicate = (Predicate) createPredicate(criteriaBuilder, root, filter);
            // if whereClause is null, repalce with predicate, else and predicate with AND operatation
            whereClause = whereClause == null ? predicate : criteriaBuilder.and(whereClause, predicate);
        }

    }

    /*******************************************************************
     * Create a search Predicate
     * @param cBuilder
     * @param root
     * @param filter
     * @return: a predicate of a search criterion
     * @throws Exception
     *******************************************************************/
    public Predicate createPredicate(CriteriaBuilder cBuilder, Root root, SearchFilter filter) {
        Predicate predicate = null;
        String op = filter.operator;
        switch (op){
            case "equal": predicate = (ComparisonPredicate) cBuilder.equal(root.get(filter.name), (Comparable) filter.value);
                break;
            case "notEqual": predicate = (ComparisonPredicate) cBuilder.notEqual(root.get(filter.name), (Comparable) filter.value);
                break;
            case "gt":predicate = (ComparisonPredicate) cBuilder.greaterThan( root.get(filter.name), (Comparable) filter.value);
                break;
            case "ge":predicate = (ComparisonPredicate) cBuilder.greaterThanOrEqualTo(root.get(filter.name), (Comparable) filter.value);
                break;
            case "lt":predicate = (ComparisonPredicate) cBuilder.lessThan(root.get(filter.name), (Comparable) filter.value);
                break;
            case "le":predicate = (ComparisonPredicate) cBuilder.lessThanOrEqualTo(root.get(filter.name), (Comparable) filter.value);
                break;
            case "like":predicate = cBuilder.like(root.get(filter.name), filter.value+"%");
                break;
            case "$":predicate = cBuilder.like(root.get(filter.name), "%"+filter.value+"%");
                break;
            default:
                predicate = (ComparisonPredicate) cBuilder.equal(root.get(filter.name), (Comparable) filter.value);
                break;
        }
        return predicate;
    }
    /*******************************************************************
     * Construction method for MyCriteriaQuery
     * @param entityManager: entitymanager
     * @param entityClass: Class of Root Entity
     *******************************************************************/
    public MyCriteriaQuery(EntityManager entityManager, Class<T> entityClass) {
        this.entityManager = entityManager;
        this.entityClass = entityClass;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
        this.query = criteriaBuilder.createQuery();
        this.root = query.from(entityClass);
    }

    /*******************************************************************
     * return CriteriaQuery with WhereClause
     *******************************************************************/
    public CriteriaQuery<T> getQuery() {
        query.select(root);
        if (whereClause != null) query.where(whereClause);
        return query;
    }

    /*******************************************************************
     * return Query for count
     *******************************************************************/
    public CriteriaQuery<Long> getQueryForCount() {
        query.select(criteriaBuilder.count(root));
        if (whereClause != null) query.where(whereClause);
        return query;
    }

    /*******************************************************************
     * return search results
     *******************************************************************/
    public List<T> list() {
        return pagedList(null, null, null, null);
    }

    /*******************************************************************
     * return a paged list of entity
     * @param pagingInfo current page number to start query
     *******************************************************************/
    public List<T> pagedList(PagingInfo pagingInfo) {
        //ascending = ascending == null || ascending.trim().length()==0 ? "ASC" : ascending;
        //process sorting, ignore sorting if sortField is empty
        if (!(pagingInfo.orderBy == null || pagingInfo.orderBy.trim().length() == 0)) {
            if (pagingInfo.asc.toUpperCase().equals("ASC")) {
                query.orderBy(criteriaBuilder.asc(root.get(pagingInfo.orderBy)));
            } else {
                query.orderBy(criteriaBuilder.desc(root.get(pagingInfo.orderBy)));
            }
        }
        TypedQuery<T> myTypeQuery = this.entityManager.createQuery(this.getQuery());
        if (pagingInfo.start != null) myTypeQuery.setFirstResult(pagingInfo.start);
        if (pagingInfo.pageSize != null) myTypeQuery.setMaxResults(pagingInfo.pageSize);
        return myTypeQuery.getResultList();
    }

    /*******************************************************************
     * return a paged list of entity
     * @param pageNumber
     * @param pageSize: number of items per page
     * @param sortField: orderBy field
     * @param ascending: "ASC" -> asscending order, else descending
     * @return
     *******************************************************************/
    public List<T> pagedList(Integer pageNumber, Integer pageSize, String sortField, String ascending) {
        ascending = ascending == null || ascending.trim().length() == 0 ? "ASC" : ascending;
        //process sorting, ignore sorting if sortField is empty
        if (!(sortField == null || sortField.trim().length() == 0)) {
            if (ascending.toUpperCase().equals("ASC")) {
                query.orderBy(criteriaBuilder.asc(root.get(sortField)));
            } else {
                query.orderBy(criteriaBuilder.desc(root.get(sortField)));
            }
        }
        TypedQuery<T> myTypeQuery = this.entityManager.createQuery(this.getQuery());
        if (pageNumber != null) myTypeQuery.setFirstResult(pageNumber - 1);
        if (pageSize != null) myTypeQuery.setMaxResults(pageSize);
        return myTypeQuery.getResultList();
    }

    /*******************************************************************
     * return number of records found by specified search criteria/filter
     *******************************************************************/
    public Long count() {
        TypedQuery<Long> q = this.entityManager.createQuery(this.getQueryForCount());
        return q.getSingleResult();
    }
}