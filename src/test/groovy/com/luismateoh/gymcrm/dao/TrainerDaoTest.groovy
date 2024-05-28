package com.luismateoh.gymcrm.dao

import com.luismateoh.gymcrm.domain.Trainer
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.Transaction
import org.hibernate.query.Query
import spock.lang.Specification

class TrainerDaoTest extends Specification {

    TrainerDao trainerDao
    SessionFactory sessionFactory = Mock()
    Session session = Mock()
    Transaction transaction = Mock()

    def setup() {
        trainerDao = new TrainerDao(sessionFactory)
        sessionFactory.openSession() >> session
        session.beginTransaction() >> transaction
    }

    def "saveOrUpdate returns saved Trainer"() {
        given: "A Trainer"
        Trainer trainer = new Trainer()
        trainer.setUserName("Trainer")

        when: "saveOrUpdate is called"
        trainerDao.saveOrUpdate(trainer)

        then: "The transaction is committed and session is called with saveOrUpdate"
        1 * session.saveOrUpdate(trainer)
        1 * transaction.commit()
    }

    def "findById returns existing Trainer"() {
        given: "An existing Trainer"
        Trainer existingTrainer = new Trainer()
        existingTrainer.setUserName("ExistingTrainer")

        and: "Session returns the existing Trainer"
        session.get(Trainer.class, 1L) >> existingTrainer

        when: "findById is called with the id of the existing Trainer"
        Trainer result = trainerDao.findById(1L)

        then: "The result is the existing Trainer"
        result == existingTrainer
    }

    def "delete removes Trainer"() {
        given: "An existing Trainer"
        Trainer trainer = new Trainer()
        trainer.setUserName("Trainer")

        when: "delete is called with the existing Trainer"
        trainerDao.delete(trainer)

        then: "The transaction is committed and session is called with remove"
        1 * session.remove(trainer)
        1 * transaction.commit()
    }

    def "findByUsername returns existing Trainer"() {
        given: "An existing Trainer"
        Trainer existingTrainer = new Trainer()
        existingTrainer.setUserName("ExistingTrainer")
        Query query = Mock()

        and: "Session and query return the existing Trainer"
        session.createQuery(_, Trainer.class) >> query
        query.setParameter("username", "ExistingTrainer") >> query
        query.uniqueResult() >> existingTrainer

        when: "findByUsername is called with the username of the existing Trainer"
        Trainer result = trainerDao.findByUsername("ExistingTrainer")

        then: "The result is the existing Trainer"
        result == existingTrainer
    }

    def "exists returns true when Trainer exists"() {
        given: "An existing Trainer"
        Trainer existingTrainer = new Trainer()
        existingTrainer.setUserName("ExistingTrainer")
        Query query = Mock()

        and: "Session and query return the existing Trainer"
        session.createQuery(_, Trainer.class) >> query
        query.setParameter("username", "ExistingTrainer") >> query
        query.uniqueResult() >> existingTrainer

        when: "exists is called with the username of the existing Trainer"
        boolean result = trainerDao.findByUsername("ExistingTrainer") != null

        then: "The result is true"
        result == true
    }
}
