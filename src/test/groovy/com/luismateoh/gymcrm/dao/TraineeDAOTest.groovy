package com.luismateoh.gymcrm.dao

import com.luismateoh.gymcrm.domain.Trainee
import com.luismateoh.gymcrm.utils.SerialNumberGenerator
import spock.lang.Specification

class TraineeDAOTest extends Specification {

    TraineeDAO traineeDAO
    Map<Long, Trainee> traineeStorage = Mock()
    SerialNumberGenerator serialNumberGenerator = Mock()

    def setup() {
        traineeDAO = new TraineeDAO()
        traineeDAO.traineeStorage = traineeStorage
        traineeDAO.serialNumberGenerator = serialNumberGenerator
    }

    def "save returns saved Trainee"() {
        given: "A Trainee"
        Trainee trainee = new Trainee()
        trainee.setUserName("Trainee")

        when: "save is called"
        serialNumberGenerator.getNextSerialNumber() >> 1L
        traineeStorage.put(1L, trainee) >> trainee
        Trainee result = traineeDAO.save(trainee)

        then: "The result is the saved Trainee"
        result == trainee
    }

    def "findById returns existing Trainee"() {
        given: "An existing Trainee"
        Trainee existingTrainee = new Trainee()
        existingTrainee.setUserName("ExistingTrainee")

        and: "TraineeStorage returns the existing Trainee"
        traineeStorage.get(1L) >> existingTrainee

        when: "findById is called with the id of the existing Trainee"
        Trainee result = traineeDAO.findById(1L)

        then: "The result is the existing Trainee"
        result == existingTrainee
    }

    def "delete removes Trainee"() {
        given: "An existing Trainee id"
        Long id = 1L

        when: "delete is called with the id of the existing Trainee"
        traineeDAO.delete(id)

        then: "The Trainee is removed from the storage"
        1 * traineeStorage.remove(id)
    }

    def "exists returns true when Trainee exists"() {
        given: "An existing Trainee"
        Trainee existingTrainee = new Trainee()
        existingTrainee.setUserName("ExistingTrainee")

        and: "TraineeStorage contains the existing Trainee"
        traineeStorage.values() >> [existingTrainee]

        when: "exists is called with the username of the existing Trainee"
        boolean result = traineeDAO.exists("ExistingTrainee")

        then: "The result is true"
        result == true
    }
}
