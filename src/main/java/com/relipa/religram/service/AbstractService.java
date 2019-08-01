/*
 * Copyright (c) 2019. Relipa Software - 株式会社レリパ
 */

package com.relipa.religram.service;

import com.relipa.religram.entity.AbstractPersistableEntity;
import javassist.NotFoundException;
import org.hibernate.service.spi.ServiceException;

import java.io.Serializable;
import java.util.List;

public interface AbstractService<T extends AbstractPersistableEntity, ID extends Serializable> {

    T findById(Long id) throws ServiceException, NotFoundException;

    Iterable<T> findAll() throws ServiceException;

    T save(T entity) throws ServiceException;

    Iterable<T> saveAll(Iterable<T> entities) throws ServiceException;

    Long count();

    void deleteById(Long id) throws ServiceException, NotFoundException;

    void delete(T entity) throws ServiceException;

}
