package com.luismateoh.gymcrm.dao

import com.luismateoh.gymcrm.domain.Trainer
import com.luismateoh.gymcrm.utils.SerialNumberGenerator
import spock.lang.Specification

class TrainerDAOTest extends Specification {

    TrainerDAO trainerDAO
    Map<Long, Trainer> trainerStorage = Mock()
    SerialNumberGenerator serialNumberGenerator = Mock()

    def setup() {
        trainerDAO = new TrainerDAO()
        trainerDAO.trainerStorage = trainerStorage
        trainerDAO.serialNumberGenerator = serialNumberGenerator
    }

    def "save returns saved Trainer"() {
        given: "A Trainer"
        Trainer trainer = new Trainer()
        trainer.setUserName("Trainer")

        when: "save is called"
        serialNumberGenerator.getNextSerialNumber() >> 1L
        trainerStorage.put(1L, trainer) >> trainer
        Trainer result = trainerDAO.save(trainer)

        then: "The result is the saved Trainer"
        result == trainer
    }

    def "findById returns existing Trainer"() {
        given: "An existing Trainer"
        Trainer existingTrainer = new Trainer()
        existingTrainer.setUserName("ExistingTrainer")

        and: "TrainerStorage returns the existing Trainer"
        trainerStorage.get(1L) >> existingTrainer

        when: "findById is called with the id of the existing Trainer"
        Trainer result = trainerDAO.findById(1L)

        then: "The result is the existing Trainer"
        result == existingTrainer
    }

    def "exists returns true when Trainer exists"() {
        given: "An existing Trainer"
        Trainer existingTrainer = new Trainer()
        existingTrainer.setUserName("ExistingTrainer")

        and: "TrainerStorage contains the existing Trainer"
        trainerStorage.values() >> [existingTrainer]

        when: "exists is called with the username of the existing Trainer"
        boolean result = trainerDAO.exists("ExistingTrainer")

        then: "The result is true"
        result
    }

    def "findByUserName returns Optional of existing Trainer"() {
        given: "An existing Trainer"
        Trainer existingTrainer = new Trainer()
        existingTrainer.setUserName("ExistingTrainer")

        and: "TrainerStorage contains the existing Trainer"
        trainerStorage.values() >> [existingTrainer]

        when: "findByUserName is called with the username of the existing Trainer"
        Optional<Optional<Trainer>> result = trainerDAO.findByUserName("ExistingTrainer")

        then: "The result is Optional of existing Trainer"
        result.get().get() == existingTrainer
    }
}
