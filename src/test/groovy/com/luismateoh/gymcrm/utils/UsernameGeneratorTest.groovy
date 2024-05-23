package com.luismateoh.gymcrm.utils

import com.luismateoh.gymcrm.dao.TraineeDAO
import com.luismateoh.gymcrm.dao.TrainerDAO
import spock.lang.Specification

class UsernameGeneratorTest extends Specification {

    UsernameGenerator usernameGenerator
    TraineeDAO traineeDAO = Mock()
    TrainerDAO trainerDAO = Mock()
    SerialNumberGenerator serialNumberGenerator = Mock()

    def setup() {
        usernameGenerator = new UsernameGenerator()
        usernameGenerator.traineeDAO = traineeDAO
        usernameGenerator.trainerDAO = trainerDAO
        usernameGenerator.serialNumberGenerator = serialNumberGenerator
    }

    def "generateUsername"() {
        given: "A first name and a last name"
        String firstName = "John"
        String lastName = "Doe"

        and: "The base username"
        String baseUsername = firstName.toLowerCase() + "." + lastName.toLowerCase()

        and: "The traineeDAO and trainerDAO do not contain the base username"
        traineeDAO.exists(baseUsername) >> false
        trainerDAO.exists(baseUsername) >> false

        when: "generateUsername is called"
        String result = usernameGenerator.generateUsername(firstName, lastName)

        then: "The result should be the base username"
        result == baseUsername

        when: "The traineeDAO contains the base username"
        traineeDAO.exists(baseUsername) >> true
        trainerDAO.exists(baseUsername) >> false
        serialNumberGenerator.getCurrentSerialNumber() >> 2L

        and: "generateUsername is called again"
        result = usernameGenerator.generateUsername(firstName, lastName)

        then: "The result should be the base username with the current serial number appended"
        result == baseUsername + ""
    }
}