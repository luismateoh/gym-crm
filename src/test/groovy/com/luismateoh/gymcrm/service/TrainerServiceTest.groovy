package com.luismateoh.gymcrm.service

import com.luismateoh.gymcrm.dao.TrainerDao
import com.luismateoh.gymcrm.dao.UserDao
import com.luismateoh.gymcrm.domain.Trainer
import com.luismateoh.gymcrm.domain.TrainingType
import com.luismateoh.gymcrm.domain.User
import com.luismateoh.gymcrm.dto.TrainerDTO
import com.luismateoh.gymcrm.dto.TrainingTypeDTO
import com.luismateoh.gymcrm.dto.UserDTO
import com.luismateoh.gymcrm.mapper.TrainerMapper
import com.luismateoh.gymcrm.mapper.UserMapper
import spock.lang.Specification

class TrainerServiceTest extends Specification {

    TrainerService trainerService
    TrainerDao trainerDao = Mock()
    UserDao userDao = Mock()
    TrainingTypeService trainingTypeService = Mock()
    UserService userService = Mock()

    def setup() {
        trainerService = new TrainerService(trainerDao, userDao, trainingTypeService)
    }

    def "createTrainer returns created TrainerDTO"() {
        given: "User details and Trainer details"
        String firstName = "John"
        String lastName = "Doe"
        String specialization = "fitness"
        String username = "john.doe"
        String password = "password"

        UserDTO userDTO = new UserDTO(id: 1, firstName: firstName, lastName: lastName, username: username, password: password, isActive: true)
        User user = UserMapper.INSTANCE.userDTOToUser(userDTO)
        TrainingType trainingType = new TrainingType(id: 1, trainingTypeName: specialization)
        Trainer trainer = new Trainer(id: 1, user: user, specialization: trainingType)
        TrainerDTO trainerDTO = TrainerMapper.INSTANCE.trainerToTrainerDTO(trainer)

        and: "Mocks are set up"
        userService.generateUsername(firstName, lastName) >> username
        userService.generatePassword() >> password
        userService.registerUser(firstName, lastName, username, password, true) >> userDTO
        trainingTypeService.findByName(specialization) >> Optional.of(trainingType)
        trainerDao.saveOrUpdate(_ as Trainer) >> trainer

        when: "createTrainer is called"
        TrainerDTO result = trainerService.createTrainer(firstName, lastName, specialization)

        then: "The result is the created TrainerDTO"
        result == trainerDTO
    }

    def "findTrainerByUsername returns existing TrainerDTO"() {
        given: "An existing Trainer"
        String username = "john.doe"
        User user = new User(id: 1, firstName: "John", lastName: "Doe", username: username, password: "password", isActive: true)
        TrainingType trainingType = new TrainingType(id: 1, trainingTypeName: "fitness")
        Trainer trainer = new Trainer(id: 1, user: user, specialization: trainingType)
        TrainerDTO trainerDTO = TrainerMapper.INSTANCE.trainerToTrainerDTO(trainer)

        and: "TrainerDao returns the existing Trainer"
        trainerDao.findByUsername(username) >> trainer

        when: "findTrainerByUsername is called with the username of the existing Trainer"
        TrainerDTO result = trainerService.findTrainerByUsername(username)

        then: "The result is the existing TrainerDTO"
        result == trainerDTO
    }

    def "updateTrainerProfile updates the Trainer profile"() {
        given: "A TrainerDTO"
        TrainingType trainingType = new TrainingType(id: 1, trainingTypeName: "fitness")
        TrainerDTO trainerDTO = new TrainerDTO(id: 1, user: new UserDTO(id: 1, firstName: "John", lastName: "Doe", username: "john.doe", password: "password", isActive: true), specialization: new TrainingTypeDTO(trainingTypeName: "fitness"))
        Trainer trainer = TrainerMapper.INSTANCE.trainerDTOToTrainer(trainerDTO)

        and: "TrainingTypeService returns the training type"
        trainingTypeService.findByName("fitness") >> Optional.of(trainingType)

        when: "updateTrainerProfile is called"
        trainerService.updateTrainerProfile(trainerDTO)

        then: "TrainerDao saveOrUpdate is called"
        1 * trainerDao.saveOrUpdate(trainer)
    }

    def "findAllTrainers returns list of TrainerDTOs"() {
        given: "A list of Trainers"
        User user = new User(id: 1, firstName: "John", lastName: "Doe", username: "john.doe", password: "password", isActive: true)
        TrainingType trainingType = new TrainingType(id: 1, trainingTypeName: "fitness")
        Trainer trainer = new Trainer(id: 1, user: user, specialization: trainingType)
        List<Trainer> trainers = [trainer]
        List<TrainerDTO> trainerDTOs = trainers.stream().map(TrainerMapper.INSTANCE.&trainerToTrainerDTO).collect(Collectors.toList())

        and: "TrainerDao returns the list of Trainers"
        trainerDao.findAll() >> trainers

        when: "findAllTrainers is called"
        List<TrainerDTO> result = trainerService.findAllTrainers()

        then: "The result is the list of TrainerDTOs"
        result == trainerDTOs
    }
}
