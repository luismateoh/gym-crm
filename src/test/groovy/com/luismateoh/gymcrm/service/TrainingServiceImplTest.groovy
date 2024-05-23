package com.luismateoh.gymcrm.service

import com.luismateoh.gymcrm.dao.TrainingDAO
import com.luismateoh.gymcrm.domain.Training
import com.luismateoh.gymcrm.domain.TrainingType
import com.luismateoh.gymcrm.dto.TrainingDTO
import spock.lang.Specification

import java.time.LocalDate

class TrainingServiceImplTest extends Specification {

    TrainingServiceImpl trainingService
    TrainingDAO trainingDAO = Mock()
    TrainingTypeService trainingTypeService = Mock()

    def setup() {
        trainingService = new TrainingServiceImpl(trainingDAO, trainingTypeService)
    }


    def "createTraining creates a new training"() {
        given: "A training DTO"
        TrainingDTO trainingDTO = new TrainingDTO("1", "1", "Cardio", "2021-01-01", 60, "1")

        and: "A training object"
        Training training = new Training()
        training.setTrainingName("Cardio")
        training.setTrainingType(new TrainingType().setName("Aerobic"))
        training.setTrainingDuration(60)
        training.setTrainingDate(LocalDate.parse(trainingDTO.trainingDate))

        and: "A training type object"
        TrainingType trainingType = new TrainingType()
        trainingType.setName("Aerobic")

        when: "createTraining is called"
        trainingDAO.save(_ as Training) >> training
        trainingTypeService.getOrCreateTrainingType(_ as String) >> trainingType // Add this line
        Training result = trainingService.createTraining(trainingDTO)

        then: "The result should be the created training"
        result == training
        result.trainingName == "Cardio"
        result.trainingDuration == 60
        result.trainingDate == LocalDate.parse("2021-01-01")
    }


    def "findTrainingById returns the training with the given ID"() {
        given: "A training object"
        Training training = new Training()
        training.setId(1L)
        training.setTrainingName("Cardio")
        training.setTrainingType(new TrainingType().setName("Aerobic"))
        training.setTrainingDuration(60)
        training.setTrainingDate(LocalDate.now())

        when: "findTrainingById is called"
        trainingDAO.findById(1L) >> training
        Training result = trainingService.findTrainingById(1L)

        then: "The result should be the training"
        result == training
        result.id == 1L
        result.trainingName == "Cardio"
        result.trainingDuration == 60
        result.trainingDate == LocalDate.now()
    }

    def "findTrainingById throws NoSuchElementException when no training exists with the given id"() {
        given: "TrainingDAO returns null"
        trainingDAO.findById(1L) >> null

        when: "findTrainingById is called with a non-existing id"
        trainingService.findTrainingById(1L)

        then: "NoSuchElementException is thrown"
        thrown(NoSuchElementException)
    }
}