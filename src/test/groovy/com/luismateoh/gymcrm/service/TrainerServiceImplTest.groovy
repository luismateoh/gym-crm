package com.luismateoh.gymcrm.service

import com.luismateoh.gymcrm.dao.TrainerDAO
import com.luismateoh.gymcrm.domain.Trainer
import com.luismateoh.gymcrm.dto.TrainerDTO
import com.luismateoh.gymcrm.utils.UsernameGenerator
import com.luismateoh.gymcrm.utils.Utils
import spock.lang.Specification

class TrainerServiceImplTest extends Specification {

    TrainerServiceImpl trainerService
    TrainerDAO trainerDAO = Mock()
    UsernameGenerator usernameGenerator = Mock()

    def setup() {
        trainerService = new TrainerServiceImpl(trainerDAO, usernameGenerator)
    }

    def "createTrainer creates a new trainer"() {
        given: "A trainer DTO"
        TrainerDTO trainerDTO = new TrainerDTO("John", "Doe", "Yoga")

        and: "A trainer object"
        Trainer trainer = new Trainer()
        trainer.setFirstName("John")
        trainer.setLastName("Doe")
        trainer.setIsActive(true)
        trainer.setUserName("john.doe")
        trainer.setPassword("password")

        when: "createTrainer is called"
        trainerDAO.save(_ as Trainer) >> trainer
        usernameGenerator.generateUsername(trainerDTO.firstName(), trainerDTO.lastName()) >> "johndoe"
        Trainer result = trainerService.createTrainer(trainerDTO)

        then: "The result should be the created trainer"
        result == trainer
        result.firstName == "John"
        result.lastName == "Doe"
        result.isActive == true
        result.userName == "john.doe"
        result.password != null
    }

    def "updateTrainer updates the details of an existing trainer"() {
        given: "A trainer object"
        Trainer trainer = new Trainer()
        trainer.setId(1L)
        trainer.setFirstName("John")
        trainer.setLastName("Doe")
        trainer.setIsActive(true)
        trainer.setUserName("john.doe")
        trainer.setPassword(Utils.generatePassword())

        when: "updateTrainer is called"
        trainerDAO.findById(1L) >> trainer
        trainerDAO.save(_ as Trainer) >> trainer
        Trainer result = trainerService.updateTrainer(trainer)

        then: "The result should be the updated trainer"
        result == trainer
        result.id == 1L
        result.firstName == "John"
        result.lastName == "Doe"
        result.isActive == true
        result.userName == "john.doe"
        result.password != null
    }

    def "updateTrainer throws NoSuchElementException when no trainer exists with the given id"() {
        given: "A trainer object"
        Trainer trainer = new Trainer()
        trainer.setId(1L)

        and: "TrainerDAO returns null"
        trainerDAO.findById(1L) >> null

        when: "updateTrainer is called with a non-existing trainer"
        trainerService.updateTrainer(trainer)

        then: "NoSuchElementException is thrown"
        thrown(NoSuchElementException)
    }

    def "findTrainerById returns the trainer with the given ID"() {
        given: "A trainer object"
        Trainer trainer = new Trainer()
        trainer.setId(1L)
        trainer.setFirstName("John")
        trainer.setLastName("Doe")
        trainer.setIsActive(true)
        trainer.setUserName("john.doe")
        trainer.setPassword("password")

        when: "findTrainerById is called"
        trainerDAO.findById(1L) >> trainer
        Trainer result = trainerService.findTrainerById(1L)

        then: "The result should be the trainer"
        result == trainer
    }

    def "findTrainerById throws NoSuchElementException when no trainer exists with the given id"() {
        given: "TrainerDAO returns null"
        trainerDAO.findById(1L) >> null

        when: "findTrainerById is called with a non-existing id"
        trainerService.findTrainerById(1L)

        then: "NoSuchElementException is thrown"
        thrown(NoSuchElementException)
    }
}