package com.springboot.app.repository;

import com.springboot.app.search.SortSpec;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Qualifier("genericDAO")
@Repository
public class GenericDAO {
    private static final Logger log = LoggerFactory.getLogger(GenericDAO.class);
    @PersistenceContext
    protected EntityManager entityManager;

    public void persist(Object entity) {
        entityManager.persist(entity);
    }

    public void remove(Object entity) {
        Object toBeRemoved = entityManager.contains(entity) ? entity : entityManager.merge(entity);
        entityManager.remove(toBeRemoved);
    }

    public <E> E merge(E entity) {
        return entityManager.merge(entity);
    }

    public void refresh(Object entity) {
        entityManager.refresh(entity);
    }

    public <E> E find(Class<E> entityClass, Object id) {
        return entityManager.find(entityClass, id);
    }

    public <E> List<E> findAll(Class<E> entityClass) {
        TypedQuery<E> query = entityManager.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e",
                entityClass);
        return query.getResultList();
    }

    public <E> List<E> findAll(Class<E> entityClass, List<String> joinFetchProperties) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<E> criteriaQuery = criteriaBuilder.createQuery(entityClass);

        Root<E> entity = criteriaQuery.from(entityClass);

        // fetch specified properties
        for (String property : joinFetchProperties) {
            entity.fetch(property, JoinType.LEFT);
        }

        TypedQuery<E> typedQuery = entityManager.createQuery(criteriaQuery);

        return typedQuery.getResultList();
    }

    public <E> Number countEntities(Class<E> entityClass) {
        TypedQuery<Number> typedQuery = entityManager
                .createQuery("SELECT count(e) FROM " + entityClass.getSimpleName() + " e", Number.class);
        return typedQuery.getSingleResult();
    }

    public <E> E getEntity(Class<E> entityClass, Map<String, Object> filters) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<E> query = builder.createQuery(entityClass);

        Root<E> root = query.from(entityClass);
        query.select(root);

        Predicate[] predicates = buildPredicates(builder, root, filters);
        query.where(predicates);

        TypedQuery<E> typedQuery = entityManager.createQuery(query);
        typedQuery.setMaxResults(1);

        List<E> resultList = typedQuery.getResultList();

        if (resultList.isEmpty()) {
            return null;
        }

        return resultList.get(0);
    }

    private <E> Predicate[] buildPredicates(CriteriaBuilder builder, Root<E> root, Map<String, Object> filters) {
        List<Predicate> predicateList = new ArrayList<>();

        for (String paramName : filters.keySet()) {
            Object value = filters.get(paramName);

            Predicate predicate = null;

            if (value instanceof Map.Entry) {
                @SuppressWarnings("rawtypes")
                Map.Entry<Comparable, Comparable> valuePair = (Map.Entry<Comparable, Comparable>) value;
                @SuppressWarnings("rawtypes")
                Comparable value1 = valuePair.getKey();
                @SuppressWarnings("rawtypes")
                Comparable value2 = valuePair.getValue();

                predicate = builder.between(getPathGeneric(root, paramName), value1, value2);
            } else if (value != null) {
                predicate = builder.equal(getPathGeneric(root, paramName), value);
            }

            predicateList.add(predicate);
        }

        Predicate[] predicates = new Predicate[predicateList.size()];
        predicateList.toArray(predicates);

        return predicates;
    }

    private <E> Path<E> getPathGeneric(Root<?> root, String pathExpression) {

        String[] paths = pathExpression.split("\\.");
        if (paths.length > 1) {
            Join<?, ?> join = root.join(paths[0], JoinType.LEFT);
            for (int i = 1; i < paths.length - 1; i++) {
                join = join.join(paths[i], JoinType.LEFT);
            }

            return join.<E>get(paths[paths.length - 1]); // return last path entry
        }

        return root.<E>get(pathExpression);
    }

    public <T, R> List<T> getEntities(Class<T> targetClass, String targetPath, Class<R> entityClass,
                                      Map<String, Object> filters) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(targetClass);

        Root<R> root = query.from(entityClass);
        query.select(getPathGeneric(root, targetPath));

        Predicate[] predicates = buildPredicates(builder, root, filters);
        query.where(predicates);

        return entityManager.createQuery(query).getResultList();
    }

    public <E> List<E> getEntities(Class<E> entityClass, Map<String, Object> filters) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<E> query = builder.createQuery(entityClass);

        Root<E> root = query.from(entityClass);
        query.select(root);

        Predicate[] predicates = buildPredicates(builder, root, filters);
        query.where(predicates);

        return entityManager.createQuery(query).getResultList();
    }

    public <E> List<E> getEntities(Class<E> entityClass, Map<String, Object> filters, SortSpec sortSpec) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<E> query = builder.createQuery(entityClass);

        Root<E> root = query.from(entityClass);
        query.select(root);

        Predicate[] predicates = buildPredicates(builder, root, filters);
        query.where(predicates);

        query.orderBy(sortSpec.dir == SortSpec.Direction.ASC ? builder.asc(getPathGeneric(root, sortSpec.field))
                : builder.desc(getPathGeneric(root, sortSpec.field)));

        TypedQuery<E> typedQuery = entityManager.createQuery(query);

        return typedQuery.getResultList();
    }

    public <E> List<E> getEntities(Class<E> entityClass, Map<String, Object> filters, int startPosition, int maxResult,
                                   SortSpec sortSpec) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<E> query = builder.createQuery(entityClass);

        Root<E> root = query.from(entityClass);
        query.select(root);

        Predicate[] predicates = buildPredicates(builder, root, filters);
        query.where(predicates);

        query.orderBy(sortSpec.dir == SortSpec.Direction.ASC ? builder.asc(getPathGeneric(root, sortSpec.field))
                : builder.desc(getPathGeneric(root, sortSpec.field)));

        TypedQuery<E> typedQuery = entityManager.createQuery(query);

        typedQuery.setFirstResult(startPosition);
        typedQuery.setMaxResults(maxResult);

        return typedQuery.getResultList();
    }

    public <E> Number getMaxNumber(Class<E> entityClass, String targetPath, Map<String, Object> filters) {
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Number> query = builder.createQuery(Number.class);

            Root<E> root = query.from(entityClass);

            CriteriaBuilder.Coalesce<Number> coalesceExp = builder.coalesce();
            coalesceExp.value(builder.max(getPathGeneric(root, targetPath)));
            coalesceExp.value(0);

            query.select(coalesceExp);

            Predicate[] predicates = buildPredicates(builder, root, filters);
            query.where(predicates);
            var result = entityManager.createQuery(query).getSingleResult();
            if (result == null) {
                return 0;
            }
            return result;
        } catch (Exception e) {
            log.error("Error in getMaxNumber", e);
            return 0;
        }
    }


}
