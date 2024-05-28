package com.luismateoh.gymcrm.dao

import com.luismateoh.gymcrm.domain.Training
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.Transaction
import org.hibernate.query.Query
import spock.lang.Specification

import java.time.LocalDate

class TrainingDaoTest extends Specification {

    TrainingDao trainingDao
    SessionFactory sessionFactory = Mock()
    Session session = Mock()
    Transaction transaction = Mock()

    def setup() {
        trainingDao = new TrainingDao(sessionFactory)
        sessionFactory.openSession() >> session
        session.beginTransaction() >> transaction
    }

    def "saveOrUpdate returns saved Training"() {
        given: "A Training"
        Training training = new Training()
        training.setTrainingName("Training")

        when: "saveOrUpdate is called"
        trainingDao.saveOrUpdate(training)

        then: "The transaction is committed and session is called with saveOrUpdate"
        1 * session.saveOrUpdate(training)
        1 * transaction.commit()
    }

    def "findById returns existing Training"() {
        given: "An existing Training"
        Training existingTraining = new Training()
        existingTraining.setTrainingName("ExistingTraining")

        and: "Session returns the existing Training"
        session.get(Training.class, 1L) >> existingTraining

        when: "findById is called with the id of the existing Training"
        Training result = trainingDao.findById(1L)

        then: "The result is the existing Training"
        result == existingTraining
    }

    def "delete removes Training"() {
        given: "An existing Training"
        Training training = new Training()
        training.setTrainingName("Training")

        when: "delete is called with the existing Training"
        trainingDao.delete(training)

        then: "The transaction is committed and session is called with remove"
        1 * session.remove(training)
        1 * transaction.commit()
    }

    def "findByTraineeUsernameAndCriteria returns list of trainings"() {
        given: "An existing Training"
        Training training = new Training()
        training.setTrainingName("Training")
        Query query = Mock()

        and: "Session and query return the list of Training"
        session.createQuery(_, Training.class) >> query
        query.getResultList() >> [training]

        when: "findByTraineeUsernameAndCriteria is called with criteria"
        List<Training> result = trainingDao.findByTraineeUsernameAndCriteria("username", LocalDate.now(), LocalDate.now().plusDays(1), "trainerName", "trainingType")

        then: "The result is the list of Training"
        result == [training]
    }

    def "findByTrainerUsernameAndCriteria returns list of trainings"() {
        given: "An existing Training"
        Training training = new Training()
        training.setTrainingName("Training")
        Query query = Mock()

        and: "Session and query return the list of Training"
        session.createQuery(_, Training.class) >> query
        query.getResultList() >> [training]

        when: "findByTrainerUsernameAndCriteria is called with criteria"
        List<Training> result = trainingDao.findByTrainerUsernameAndCriteria("username", LocalDate.now(), LocalDate.now().plusDays(1), "traineeName")

        then: "The result is the list of Training"
        result == [training]
    }
}
