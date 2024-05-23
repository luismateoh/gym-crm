package com.luismateoh.gymcrm.service

import com.luismateoh.gymcrm.dao.TraineeDAO
import com.luismateoh.gymcrm.domain.Trainee
import com.luismateoh.gymcrm.dto.TraineeDTO
import com.luismateoh.gymcrm.utils.UsernameGenerator
import com.luismateoh.gymcrm.utils.Utils
import spock.lang.Specification

import java.time.LocalDate

class TraineeServiceImplTest extends Specification {

    TraineeServiceImpl traineeService
    TraineeDAO traineeDAO = Mock()
    UsernameGenerator usernameGenerator = Mock()

    def setup() {
        traineeService = new TraineeServiceImpl(traineeDAO, usernameGenerator)
    }

    def "createTrainee creates a new trainee"() {
        given: "A trainee DTO"
        TraineeDTO traineeDTO = new TraineeDTO("John", "Doe", "2000-01-01", "123 Street")

        and: "A trainee object"
        Trainee trainee = new Trainee()
        trainee.setFirstName(traineeDTO.firstName())
        trainee.setLastName(traineeDTO.lastName())
        trainee.setDateOfBirth(LocalDate.parse(traineeDTO.dateOfBirth()))
        trainee.setAddress(traineeDTO.address())
        trainee.setIsActive(true)
        trainee.setUserName("john.doe")
        trainee.setPassword(Utils.generatePassword())

        when: "createTrainee is called"
        traineeDAO.save(_ as Trainee) >> trainee
        usernameGenerator.generateUsername(traineeDTO.firstName(), traineeDTO.lastName()) >> "john.doe"
        Trainee result = traineeService.createTrainee(traineeDTO)

        then: "The result should be the created trainee"
        result == trainee
        result.userName == "john.doe"
        result.firstName == traineeDTO.firstName()
        result.lastName == traineeDTO.lastName()
        result.dateOfBirth == LocalDate.parse(traineeDTO.dateOfBirth())
        result.address == traineeDTO.address()
        result.isActive == true
        result.password != null
    }

    def "updateTrainee updates the details of an existing trainee"() {
        given: "A trainee object"
        Trainee trainee = new Trainee()
        trainee.setId(1L)
        trainee.setFirstName("John")
        trainee.setLastName("Doe")
        trainee.setDateOfBirth(LocalDate.parse("2000-01-01"))
        trainee.setAddress("123 Street")
        trainee.setIsActive(true)
        trainee.setUserName("john.doe")
        trainee.setPassword("password")

        when: "updateTrainee is called"
        traineeDAO.findById(1L) >> trainee
        traineeDAO.save(_ as Trainee) >> trainee
        Trainee result = traineeService.updateTrainee(trainee)

        then: "The result should be the updated trainee"
        result == trainee
        result.userName == "john.doe"
        result.firstName == trainee.firstName
        result.lastName == trainee.lastName
        result.dateOfBirth == LocalDate.parse(trainee.dateOfBirth.toString())
        result.address == trainee.address
        result.isActive == true
        result.password != null
    }

    def "updateTrainee throws NoSuchElementException when trainee does not exist"() {
        given: "A trainee object"
        Trainee trainee = new Trainee()
        trainee.setId(1L)

        and: "TraineeDAO returns null"
        traineeDAO.findById(1L) >> null

        when: "updateTrainee is called with a non-existing trainee"
        traineeService.updateTrainee(trainee)

        then: "NoSuchElementException is thrown"
        thrown(NoSuchElementException)
    }

    def "deleteTrainee deletes a trainee by ID"() {
        given: "A trainee id"
        Long traineeId = 1L

        when: "deleteTrainee is called"
        traineeService.deleteTrainee(traineeId)

        then: "The traineeDAO delete method should be called"
        1 * traineeDAO.delete(traineeId)
    }

    def "findTraineeById returns the trainee with the given ID"() {
        given: "A trainee id"
        Long traineeId = 1L

        and: "A trainee object"
        Trainee trainee = new Trainee()
        trainee.setId(1L)
        trainee.setFirstName("John")
        trainee.setLastName("Doe")
        trainee.setDateOfBirth(LocalDate.parse("2000-01-01"))
        trainee.setAddress("123 Street")
        trainee.setIsActive(true)
        trainee.setUserName("john.doe")
        trainee.setPassword("password")

        when: "findTraineeById is called"
        traineeDAO.findById(1L) >> trainee
        Trainee result = traineeService.findTraineeById(traineeId)

        then: "The result should be the found trainee"
        result == trainee
    }

    def "findTraineeById throws NoSuchElementException when trainee does not exist"() {
        given: "TraineeDAO returns null"
        traineeDAO.findById(1L) >> null

        when: "findTraineeById is called with a non-existing id"
        traineeService.findTraineeById(1L)

        then: "NoSuchElementException is thrown"
        thrown(NoSuchElementException)
    }
}