package com.luismateoh.gymcrm.dao

import com.luismateoh.gymcrm.domain.Training
import spock.lang.Specification

import java.util.concurrent.atomic.AtomicLong

class TrainingDAOTest extends Specification {

    TrainingDAO trainingDAO
    Map<Long, Training> trainingStorage = Mock()
    AtomicLong serialNumber = Mock()

    def setup() {
        trainingDAO = new TrainingDAO()
        trainingDAO.trainingStorage = trainingStorage
        trainingDAO.serialNumber = serialNumber
    }

    def "save returns saved Training"() {
        given: "A Training"
        Training training = new Training()

        when: "save is called"
        serialNumber.getAndIncrement() >> 1L
        trainingStorage.put(1L, training) >> training
        Training result = trainingDAO.save(training)

        then: "The result is the saved Training"
        result == training
    }

    def "findById returns existing Training"() {
        given: "An existing Training"
        Training existingTraining = new Training()

        and: "TrainingStorage returns the existing Training"
        trainingStorage.get(1L) >> existingTraining

        when: "findById is called with the id of the existing Training"
        Training result = trainingDAO.findById(1L)

        then: "The result is the existing Training"
        result == existingTraining
    }

    def "findTrainingTypeById returns existing Training"() {
        given: "An existing Training"
        Training existingTraining = new Training()

        and: "TrainingStorage returns the existing Training"
        trainingStorage.get(1L) >> existingTraining

        when: "findTrainingTypeById is called with the id of the existing Training"
        Training result = trainingDAO.findTrainingTypeById(1L)

        then: "The result is the existing Training"
        result == existingTraining
    }
}