package com.springboot.app.service;

import com.springboot.app.dto.response.ServiceResponse;
import com.springboot.app.repository.GenericDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class GenericService {

    @Autowired
    private GenericDAO genericDAO;

    /**
     * Save new instance of the given entity
     *
     * @param <E>:    entity type must be a subclass of BaseEntity
     * @param entity: object to be saved
     * @return
     */

    @Transactional(readOnly = false)
    public <E> ServiceResponse<Long> saveEntity(E entity) {

        ServiceResponse<Long> response = new ServiceResponse<Long>();
        genericDAO.persist(entity);

        return response;
    }

    @Transactional(readOnly = true)
    public <E> ServiceResponse<E> findEntity(Class<E> entityClass, Object entityId) {

        ServiceResponse<E> response = new ServiceResponse<E>();

        response.setDataObject(genericDAO.find(entityClass, entityId));

        return response;
    }

    @Transactional(readOnly = true)
    public <E> ServiceResponse<E> getEntity(Class<E> entityClass, Object entityId) {

        ServiceResponse<E> response = new ServiceResponse<E>();

        response.setDataObject(genericDAO.getEntity(entityClass, Collections.singletonMap("id", entityId)));

        return response;
    }

    @Transactional(readOnly = false)
    public <E> ServiceResponse<E> updateEntity(E entity) {
        ServiceResponse<E> response = new ServiceResponse<>();
        E mergedEntity = genericDAO.merge(entity);
        response.setDataObject(mergedEntity);

        return response;
    }

    @Transactional(readOnly = false)
    public <E> ServiceResponse<Void> deleteEntity(E entity) {

        ServiceResponse<Void> response = new ServiceResponse<Void>();

        genericDAO.remove(entity);

        return response;
    }

    @Transactional(readOnly = true)
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
