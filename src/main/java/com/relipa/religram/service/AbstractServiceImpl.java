package com.relipa.religram.service;

import com.relipa.religram.entity.AbstractPersistableEntity;
import javassist.NotFoundException;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.io.Serializable;

public class AbstractServiceImpl<T extends AbstractPersistableEntity, ID extends Serializable> implements AbstractService<T, ID> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractServiceImpl.class);

    @Inject
    private CrudRepository<T, ID> repository;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public T findById(Long id) throws ServiceException, NotFoundException {
        LOG.debug("AbstractServiceImpl::findOneById({})", id);
        return repository.findById((ID) id).orElseThrow(() -> new NotFoundException("Could not find entity"));
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<T> findAll() throws ServiceException {
        LOG.debug("AbstractServiceImpl::findAll()");
        try {
            Iterable<T> entities = repository.findAll();
            return entities;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public T save(T entity) throws ServiceException {
        LOG.debug("AbstractServiceImpl::save()");
        try {
            return repository.save(entity);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public Iterable<T> saveAll(Iterable<T> entities) throws ServiceException {
        LOG.debug("AbstractServiceImpl::saveAll()");
        try {
            Iterable<T> results = repository.saveAll(entities);
            return results;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Long count() {
        LOG.debug("AbstractServiceImpl::count()");
        return repository.count();
    }

    @Override
    @Transactional
    public void deleteById(Long id) throws ServiceException, NotFoundException {
        LOG.debug("AbstractServiceImpl::delete by id: {}", id);
        try {
            repository.deleteById((ID) id);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void delete(T entity) throws ServiceException {
        LOG.debug("AbstractServiceImpl::delete()");
        try {
            repository.delete(entity);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage(), e);
        }
    }
}
