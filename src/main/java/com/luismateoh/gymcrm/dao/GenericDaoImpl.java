package com.luismateoh.gymcrm.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class GenericDaoImpl<T, ID extends Serializable> implements GenericDao<T, ID> {

    private final Class<T> entityClass;
    protected final SessionFactory sessionFactory;

    public GenericDaoImpl(Class<T> entityClass, SessionFactory sessionFactory) {
        this.entityClass = entityClass;
        this.sessionFactory = sessionFactory;
    }

    @Override
    public T findById(ID id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(entityClass, id);
        }
    }

    @Override
    public List<T> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<T> query = session.createQuery("FROM " + entityClass.getName(), entityClass);
            return query.list();
        }
    }

    @Override
    public T saveOrUpdate(T entity) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.saveOrUpdate(entity);
            tx.commit();
            return entity;
        }
    }

    @Override
    public void delete(T entity) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.remove(entity);
            tx.commit();
        }
    }
}
