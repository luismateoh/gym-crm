package com.luismateoh.gymcrm.service

import com.luismateoh.gymcrm.dao.TraineeDao
import com.luismateoh.gymcrm.dao.UserDao
import com.luismateoh.gymcrm.domain.Trainee
import com.luismateoh.gymcrm.domain.User
import com.luismateoh.gymcrm.dto.TraineeDTO
import com.luismateoh.gymcrm.dto.UserDTO
import com.luismateoh.gymcrm.mapper.TraineeMapper
import com.luismateoh.gymcrm.mapper.UserMapper
import spock.lang.Specification

import java.time.LocalDate

class TraineeServiceTest extends Specification {

    TraineeService traineeService
    TraineeDao traineeDao = Mock()
    UserDao userDao = Mock()
    UserService userService = Mock()

    def setup() {
        traineeService = new TraineeService(traineeDao, userDao)
    }

    def "createTrainee returns created TraineeDTO"() {
        given: "User details and Trainee details"
        String firstName = "John"
        String lastName = "Doe"
        String dateOfBirth = "2000-01-01"
        String address = "123 Main St"
        String username = "john.doe"
        String password = "password"

        UserDTO userDTO = new UserDTO(id: 1, firstName: firstName, lastName: lastName, username: username, password: password, isActive: true)
        User user = UserMapper.INSTANCE.userDTOToUser(userDTO)
        Trainee trainee = new Trainee(id: 1, user: user, dateOfBirth: LocalDate.parse(dateOfBirth), address: address)
        TraineeDTO traineeDTO = TraineeMapper.INSTANCE.traineeToTraineeDTO(trainee)

        and: "Mocks are set up"
        userService.generateUsername(firstName, lastName) >> username
        userService.generatePassword() >> password
        userService.registerUser(firstName, lastName, username, password, true) >> userDTO
        traineeDao.saveOrUpdate(_ as Trainee) >> trainee

        when: "createTrainee is called"
        TraineeDTO result = traineeService.createTrainee(firstName, lastName, dateOfBirth, address)

        then: "The result is the created TraineeDTO"
        result == traineeDTO
    }

    def "findTraineeByUsername returns existing TraineeDTO"() {
        given: "An existing Trainee"
        String username = "john.doe"
        User user = new User(id: 1, firstName: "John", lastName: "Doe", username: username, password: "password", isActive: true)
        Trainee trainee = new Trainee(id: 1, user: user, dateOfBirth: LocalDate.parse("2000-01-01"), address: "123 Main St")
        TraineeDTO traineeDTO = TraineeMapper.INSTANCE.traineeToTraineeDTO(trainee)

        and: "TraineeDao returns the existing Trainee"
        traineeDao.findByUsername(username) >> trainee

        when: "findTraineeByUsername is called with the username of the existing Trainee"
        TraineeDTO result = traineeService.findTraineeByUsername(username)

        then: "The result is the existing TraineeDTO"
        result == traineeDTO
    }

    def "updateTraineeProfile updates the Trainee profile"() {
        given: "A TraineeDTO"
        TraineeDTO traineeDTO = new TraineeDTO(id: 1, user: new UserDTO(id: 1, firstName: "John", lastName: "Doe", username: "john.doe", password: "password", isActive: true), dateOfBirth: LocalDate.parse("2000-01-01"), address: "123 Main St")
        Trainee trainee = TraineeMapper.INSTANCE.traineeDTOToTrainee(traineeDTO)

        when: "updateTraineeProfile is called"
        traineeService.updateTraineeProfile(traineeDTO)

        then: "TraineeDao saveOrUpdate is called"
        1 * traineeDao.saveOrUpdate(trainee)
    }

    def "deleteTraineeByUsername deletes the Trainee by username"() {
        given: "A username"
        String username = "john.doe"

        when: "deleteTraineeByUsername is called with the username"
        traineeService.deleteTraineeByUsername(username)

        then: "TraineeDao deleteByUsername is called"
        1 * traineeDao.deleteByUsername(username)
    }

    def "findAllTrainees returns list of TraineeDTOs"() {
        given: "A list of Trainees"
        User user = new User(id: 1, firstName: "John", lastName: "Doe", username: "john.doe", password: "password", isActive: true)
        Trainee trainee = new Trainee(id: 1, user: user, dateOfBirth: LocalDate.parse("2000-01-01"), address: "123 Main St")
        List<Trainee> trainees = [trainee]
        List<TraineeDTO> traineeDTOs = trainees.stream().map(TraineeMapper.INSTANCE.&traineeToTraineeDTO).collect(Collectors.toList())

        and: "TraineeDao returns the list of Trainees"
        traineeDao.findAll() >> trainees

        when: "findAllTrainees is called"
        List<TraineeDTO> result = traineeService.findAllTrainees()

        then: "The result is the list of TraineeDTOs"
        result == traineeDTOs
    }
}
