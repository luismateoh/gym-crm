package com.luismateoh.gymcrm.dao;

import com.luismateoh.gymcrm.domain.Trainee;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class TraineeDao extends GenericDaoImpl<Trainee, Long> {

    public TraineeDao(SessionFactory sessionFactory) {
        super(Trainee.class, sessionFactory);
    }

    public Trainee findByUsername(String username) {
        try (Session session = sessionFactory.openSession()) {
            Query<Trainee> query = session.createQuery("SELECT t FROM Trainee t JOIN t.user u WHERE u.username = :username", Trainee.class);
            query.setParameter("username", username);
            return query.uniqueResult();
        }
    }

    public void deleteByUsername(String username) {
        try (Session session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();
            Trainee trainee = findByUsername(username);
            if (trainee != null) {
                session.remove(trainee);
            }
            transaction.commit();
        }
    }
}
