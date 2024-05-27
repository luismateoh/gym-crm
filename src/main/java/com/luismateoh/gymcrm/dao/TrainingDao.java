package com.luismateoh.gymcrm.dao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.luismateoh.gymcrm.domain.Training;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class TrainingDao extends GenericDaoImpl<Training, Long> {
    public TrainingDao(SessionFactory sessionFactory) {
        super(Training.class, sessionFactory);
    }

    public List<Training> findByTraineeUsernameAndCriteria(String username, LocalDate fromDate, LocalDate toDate,
                                                           String trainerName, String trainingType) {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Training> query = cb.createQuery(Training.class);
            Root<Training> training = query.from(Training.class);

            List<Predicate> predicates = new ArrayList<>();

            if (username != null && !username.isEmpty()) {
                predicates.add(cb.equal(training.get("trainee").get("user").get("username"), username));
            }
            if (fromDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(training.get("trainingDate"), fromDate));
            }
            if (toDate != null) {
                predicates.add(cb.lessThanOrEqualTo(training.get("trainingDate"), toDate));
            }
            if (trainerName != null && !trainerName.isEmpty()) {
                predicates.add(cb.like(cb.concat(training.get("trainer").get("user").get("firstName"),
                        cb.concat(" ", training.get("trainer").get("user").get("lastName"))), "%" + trainerName + "%"));
            }
            if (trainingType != null && !trainingType.isEmpty()) {
                predicates.add(cb.equal(cb.lower(training.get("trainingType").get("trainingTypeName")),
                        trainingType.toLowerCase()));
            }

            query.where(predicates.toArray(new Predicate[0]));

            return session.createQuery(query).getResultList();
        }
    }

    public List<Training> findByTrainerUsernameAndCriteria(String username, LocalDate fromDate, LocalDate toDate,
                                                           String traineeName) {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Training> query = cb.createQuery(Training.class);
            Root<Training> training = query.from(Training.class);

            List<Predicate> predicates = new ArrayList<>();

            if (username != null && !username.isEmpty()) {
                predicates.add(cb.equal(training.get("trainer").get("user").get("username"), username));
            }
            if (fromDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(training.get("trainingDate"), fromDate));
            }
            if (toDate != null) {
                predicates.add(cb.lessThanOrEqualTo(training.get("trainingDate"), toDate));
            }
            if (traineeName != null && !traineeName.isEmpty()) {
                predicates.add(cb.like(cb.concat(training.get("trainee").get("user").get("firstName"),
                        cb.concat(" ", training.get("trainee").get("user").get("lastName"))), "%" + traineeName + "%"));
            }

            query.where(predicates.toArray(new Predicate[0]));

            return session.createQuery(query).getResultList();
        }
    }
}

