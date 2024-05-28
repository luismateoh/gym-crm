package com.luismateoh.gymcrm.service

import com.luismateoh.gymcrm.dao.TrainingDao
import com.luismateoh.gymcrm.domain.Training
import com.luismateoh.gymcrm.domain.TrainingType
import com.luismateoh.gymcrm.dto.TrainingDTO
import com.luismateoh.gymcrm.mapper.TrainingMapper
import spock.lang.Specification

import java.time.LocalDate

import static com.luismateoh.gymcrm.utils.Utils.convertToLocalDate

class TrainingServiceTest extends Specification {

    TrainingService trainingService
    TrainingDao trainingDao = Mock()
    TrainingTypeService trainingTypeService = Mock()
    TrainingMapper trainingMapper = TrainingMapper.INSTANCE

    def setup() {
        trainingService = new TrainingService(trainingDao, trainingTypeService)
    }

    def "addTraining returns created TrainingDTO"() {
        given: "Training details"
        TrainingType trainingType = new TrainingType(id: 1, trainingTypeName: "fitness")
        TrainingDTO trainingDTO = new TrainingDTO(id: 1, trainingName: "Morning Training", trainingType: trainingType, trainingDate: LocalDate.now(), trainingDuration: 60)
        Training training = TrainingMapper.INSTANCE.trainingDTOToTraining(trainingDTO)
        Training savedTraining = new Training(id: 1, trainingName: "Morning Training", trainingType: trainingType, trainingDate: LocalDate.now(), trainingDuration: 60)

        and: "Mocks are set up"
        trainingTypeService.findByName("fitness") >> Optional.of(trainingType)
        trainingDao.saveOrUpdate(_ as Training) >> savedTraining

        when: "addTraining is called"
        TrainingDTO result = trainingService.addTraining(trainingDTO)

        then: "The result is the created TrainingDTO"
        result == TrainingMapper.INSTANCE.trainingToTrainingDTO(savedTraining)
    }

    def "getTraineeTrainings returns list of TrainingDTOs"() {
        given: "Criteria and existing trainings"
        String username = "john.doe"
        Date fromDate = new Date()
        Date toDate = new Date()
        String trainerName = "Trainer Name"
        String trainingType = "fitness"
        TrainingType type = new TrainingType(id: 1, trainingTypeName: trainingType)
        Training training = new Training(id: 1, trainingName: "Training", trainingType: type, trainingDate: LocalDate.now(), trainingDuration: 60)
        List<Training> trainings = [training]

        and: "Mocks are set up"
        trainingDao.findByTraineeUsernameAndCriteria(username, convertToLocalDate(fromDate), convertToLocalDate(toDate), trainerName, trainingType) >> trainings

        when: "getTraineeTrainings is called"
        List<TrainingDTO> result = trainingService.getTraineeTrainings(username, fromDate, toDate, trainerName, trainingType)

        then: "The result is the list of TrainingDTOs"
        result == trainings.stream().map(trainingMapper::trainingToTrainingDTO).toList()
    }

    def "getTrainerTrainings returns list of TrainingDTOs"() {
        given: "Criteria and existing trainings"
        String username = "trainer.doe"
        Date fromDate = new Date()
        Date toDate = new Date()
        String traineeName = "Trainee Name"
        TrainingType type = new TrainingType(id: 1, trainingTypeName: "fitness")
        Training training = new Training(id: 1, trainingName: "Training", trainingType: type, trainingDate: LocalDate.now(), trainingDuration: 60)
        List<Training> trainings = [training]

        and: "Mocks are set up"
        trainingDao.findByTrainerUsernameAndCriteria(username, convertToLocalDate(fromDate), convertToLocalDate(toDate), traineeName) >> trainings

        when: "getTrainerTrainings is called"
        List<TrainingDTO> result = trainingService.getTrainerTrainings(username, fromDate, toDate, traineeName)

        then: "The result is the list of TrainingDTOs"
        result == trainings.stream().map(trainingMapper::trainingToTrainingDTO).toList()
    }

    def "findAllTrainings returns list of TrainingDTOs"() {
        given: "A list of Trainings"
        TrainingType type = new TrainingType(id: 1, trainingTypeName: "fitness")
        Training training = new Training(id: 1, trainingName: "Training", trainingType: type, trainingDate: LocalDate.now(), trainingDuration: 60)
        List<Training> trainings = [training]
        List<TrainingDTO> trainingDTOs = trainings.stream().map(trainingMapper::trainingToTrainingDTO).collect(Collectors.toList())

        and: "TrainingDao returns the list of Trainings"
        trainingDao.findAll() >> trainings

        when: "findAllTrainings is called"
        List<TrainingDTO> result = trainingService.findAllTrainings()

        then: "The result is the list of TrainingDTOs"
        result == trainingDTOs
    }
}
