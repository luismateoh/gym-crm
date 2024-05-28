package com.luismateoh.gymcrm.dao

import com.luismateoh.gymcrm.domain.TrainingType
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.Transaction
import org.hibernate.query.Query
import spock.lang.Specification

class TrainingTypeDaoTest extends Specification {

    TrainingTypeDao trainingTypeDao
    SessionFactory sessionFactory = Mock()
    Session session = Mock()
    Transaction transaction = Mock()

    def setup() {
        trainingTypeDao = new TrainingTypeDao(sessionFactory)
        sessionFactory.openSession() >> session
        session.beginTransaction() >> transaction
    }

    def "saveOrUpdate returns saved TrainingType"() {
        given: "A TrainingType"
        TrainingType trainingType = new TrainingType()
        trainingType.setTrainingTypeName("Type")

        when: "saveOrUpdate is called"
        trainingTypeDao.saveOrUpdate(trainingType)

        then: "The transaction is committed and session is called with saveOrUpdate"
        1 * session.saveOrUpdate(trainingType)
        1 * transaction.commit()
    }

    def "findById returns existing TrainingType"() {
        given: "An existing TrainingType"
        TrainingType existingTrainingType = new TrainingType()
        existingTrainingType.setTrainingTypeName("ExistingType")

        and: "Session returns the existing TrainingType"
        session.get(TrainingType.class, 1L) >> existingTrainingType

        when: "findById is called with the id of the existing TrainingType"
        TrainingType result = trainingTypeDao.findById(1L)

        then: "The result is the existing TrainingType"
        result == existingTrainingType
    }

    def "delete removes TrainingType"() {
        given: "An existing TrainingType"
        TrainingType trainingType = new TrainingType()
        trainingType.setTrainingTypeName("Type")

        when: "delete is called with the existing TrainingType"
        trainingTypeDao.delete(trainingType)

        then: "The transaction is committed and session is called with remove"
        1 * session.remove(trainingType)
        1 * transaction.commit()
    }

    def "findByName returns existing TrainingType"() {
        given: "An existing TrainingType"
        TrainingType existingTrainingType = new TrainingType()
        existingTrainingType.setTrainingTypeName("ExistingType")
        Query query = Mock()

        and: "Session and query return the existing TrainingType"
        session.createQuery(_, TrainingType.class) >> query
        query.setParameter("name", "ExistingType") >> query
        query.uniqueResult() >> existingTrainingType

        when: "findByName is called with the name of the existing TrainingType"
        TrainingType result = trainingTypeDao.findByName("ExistingType")

        then: "The result is the existing TrainingType"
        result == existingTrainingType
    }

    def "exists returns true when TrainingType exists"() {
        given: "An existing TrainingType"
        TrainingType existingTrainingType = new TrainingType()
        existingTrainingType.setTrainingTypeName("ExistingType")
        Query query = Mock()

        and: "Session and query return the existing TrainingType"
        session.createQuery(_, TrainingType.class) >> query
        query.setParameter("name", "ExistingType") >> query
        query.uniqueResult() >> existingTrainingType

        when: "exists is called with the name of the existing TrainingType"
        boolean result = trainingTypeDao.findByName("ExistingType") != null

        then: "The result is true"
        result == true
    }
}
