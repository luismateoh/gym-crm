package com.luismateoh.gymcrm.dao;

import com.luismateoh.gymcrm.domain.Trainer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class TrainerDao extends GenericDaoImpl<Trainer, Long> {

    public TrainerDao(SessionFactory sessionFactory) {
        super(Trainer.class, sessionFactory);
    }

    public Trainer findByUsername(String username) {
        try (Session session = sessionFactory.openSession()) {
            Query<Trainer> query = session.createQuery("SELECT t FROM Trainer t JOIN t.user u WHERE u.username = :username", Trainer.class);
            query.setParameter("username", username);
            return query.uniqueResult();
        }
    }

}
