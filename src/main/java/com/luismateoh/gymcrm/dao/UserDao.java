package com.luismateoh.gymcrm.dao;

import com.luismateoh.gymcrm.domain.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao extends GenericDaoImpl<User, Long> {

    public UserDao(SessionFactory sessionFactory) {
        super(User.class, sessionFactory);
    }

    public User findByUsername(String username) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("FROM User WHERE username = :username", User.class);
            query.setParameter("username", username);
            return query.uniqueResult();
        }
    }
}

