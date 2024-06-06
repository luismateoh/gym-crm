package com.luismateoh.gymcrm.dao;

import com.luismateoh.gymcrm.domain.TrainingType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class TrainingTypeDao extends GenericDaoImpl<TrainingType, Long> {

    public TrainingTypeDao(SessionFactory sessionFactory) {
        super(TrainingType.class, sessionFactory);
    }

    public TrainingType findByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            Query<TrainingType> query = session.createQuery("FROM TrainingType WHERE trainingTypeName = :name", TrainingType.class);
            query.setParameter("name", name);
            return query.uniqueResult();
        }
    }

}
