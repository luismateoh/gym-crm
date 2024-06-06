package com.luismateoh.gymcrm.dao

import com.luismateoh.gymcrm.domain.Trainee
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.Transaction
import org.hibernate.query.Query
import spock.lang.Specification

class TraineeDaoTest extends Specification {

    TraineeDao traineeDao
    SessionFactory sessionFactory = Mock()
    Session session = Mock()
    Transaction transaction = Mock()

    def setup() {
        traineeDao = new TraineeDao(sessionFactory)
        sessionFactory.openSession() >> session
        session.beginTransaction() >> transaction
    }

    def "saveOrUpdate returns saved Trainee"() {
        given: "A Trainee"
        Trainee trainee = new Trainee()
        trainee.setUserName("Trainee")

        when: "saveOrUpdate is called"
        traineeDao.saveOrUpdate(trainee)

        then: "The transaction is committed and session is called with saveOrUpdate"
        1 * session.saveOrUpdate(trainee)
        1 * transaction.commit()
    }

    def "findById returns existing Trainee"() {
        given: "An existing Trainee"
        Trainee existingTrainee = new Trainee()
        existingTrainee.setUserName("ExistingTrainee")

        and: "Session returns the existing Trainee"
        session.get(Trainee.class, 1L) >> existingTrainee

        when: "findById is called with the id of the existing Trainee"
        Trainee result = traineeDao.findById(1L)

        then: "The result is the existing Trainee"
        result == existingTrainee
    }

    def "delete removes Trainee"() {
        given: "An existing Trainee"
        Trainee trainee = new Trainee()
        trainee.setUserName("Trainee")

        when: "delete is called with the existing Trainee"
        traineeDao.delete(trainee)

        then: "The transaction is committed and session is called with remove"
        1 * session.remove(trainee)
        1 * transaction.commit()
    }

    def "findByUsername returns existing Trainee"() {
        given: "An existing Trainee"
        Trainee existingTrainee = new Trainee()
        existingTrainee.setUserName("ExistingTrainee")
        Query query = Mock()

        and: "Session and query return the existing Trainee"
        session.createQuery(_, Trainee.class) >> query
        query.setParameter("username", "ExistingTrainee") >> query
        query.uniqueResult() >> existingTrainee

        when: "findByUsername is called with the username of the existing Trainee"
        Trainee result = traineeDao.findByUsername("ExistingTrainee")

        then: "The result is the existing Trainee"
        result == existingTrainee
    }

    def "deleteByUsername removes Trainee"() {
        given: "An existing Trainee"
        Trainee existingTrainee = new Trainee()
        existingTrainee.setUserName("ExistingTrainee")
        Query query = Mock()

        and: "Session and query return the existing Trainee"
        session.createQuery(_, Trainee.class) >> query
        query.setParameter("username", "ExistingTrainee") >> query
        query.uniqueResult() >> existingTrainee

        when: "deleteByUsername is called with the username of the existing Trainee"
        traineeDao.deleteByUsername("ExistingTrainee")

        then: "The transaction is committed and session is called with remove"
        1 * session.remove(existingTrainee)
        1 * transaction.commit()
    }

    def "exists returns true when Trainee exists"() {
        given: "An existing Trainee"
        Trainee existingTrainee = new Trainee()
        existingTrainee.setUserName("ExistingTrainee")
        Query query = Mock()

        and: "Session and query return the existing Trainee"
        session.createQuery(_, Trainee.class) >> query
        query.setParameter("username", "ExistingTrainee") >> query
        query.uniqueResult() >> existingTrainee

        when: "exists is called with the username of the existing Trainee"
        boolean result = traineeDao.findByUsername("ExistingTrainee") != null

        then: "The result is true"
        result == true
    }
}
