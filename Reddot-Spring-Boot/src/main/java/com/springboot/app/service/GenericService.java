package com.springboot.app.service;

import com.springboot.app.dto.response.ServiceResponse;
import com.springboot.app.repository.GenericDAO;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Generic service class to handle CRUD operations for any entity
 */
@Service
public class GenericService {

    private final GenericDAO genericDAO;

    public GenericService(GenericDAO genericDAO) {
        this.genericDAO = genericDAO;
    }

    public <E> void saveEntity(E entity) {
        genericDAO.merge(entity);
    }

    public <E> ServiceResponse<E> findEntity(Class<E> entityClass, Object entityId) {

        ServiceResponse<E> response = new ServiceResponse<E>();

        response.setDataObject(genericDAO.find(entityClass, entityId));

        return response;
    }

    public <E> ServiceResponse<E> getEntity(Class<E> entityClass, Object entityId) {

        ServiceResponse<E> response = new ServiceResponse<E>();

        response.setDataObject(genericDAO.getEntity(entityClass, Collections.singletonMap("id", entityId)));

        return response;
    }

    public <E> ServiceResponse<E> updateEntity(E entity) {
        ServiceResponse<E> response = new ServiceResponse<>();
        E mergedEntity = genericDAO.merge(entity);
        response.setDataObject(mergedEntity);

        return response;
    }

    public <E> ServiceResponse<Void> deleteEntity(E entity) {

        ServiceResponse<Void> response = new ServiceResponse<Void>();

        genericDAO.remove(entity);

        return response;
    }

    public <E> ServiceResponse<List<E>> getAllEntities(Class<E> entityClass) {

        ServiceResponse<List<E>> response = new ServiceResponse<List<E>>();
        response.setDataObject(genericDAO.findAll(entityClass));

        return response;
    }

    public <E> ServiceResponse<List<E>> getEntities(Class<E> entityClass, Map<String, Object> filters) {

        ServiceResponse<List<E>> response = new ServiceResponse<List<E>>();

        response.setDataObject(genericDAO.getEntities(entityClass, filters));

        return response;
    }

}
