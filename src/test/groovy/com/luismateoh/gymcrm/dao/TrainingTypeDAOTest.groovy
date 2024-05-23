package com.luismateoh.gymcrm.dao

import com.luismateoh.gymcrm.domain.TrainingType
import spock.lang.Specification

import java.util.concurrent.atomic.AtomicLong

class TrainingTypeDAOTest extends Specification {

    TrainingTypeDAO trainingTypeDAO
    Map<Long, TrainingType> trainingTypeStorage = Mock()
    AtomicLong serialNumber = new AtomicLong(1)

    def setup() {
        trainingTypeDAO = new TrainingTypeDAO()
        trainingTypeDAO.trainingTypeStorage = trainingTypeStorage
        trainingTypeDAO.serialNumber = serialNumber
    }

    def "save returns saved TrainingType"() {
        given: "A TrainingType"
        TrainingType trainingType = new TrainingType()
        trainingType.setName("Type")

        when: "save is called"
        trainingTypeStorage.put(serialNumber.get(), trainingType) >> trainingType
        TrainingType result = trainingTypeDAO.save(trainingType)

        then: "The result is the saved TrainingType"
        result == trainingType
    }

    def "findByNameIgnoreCase returns existing TrainingType"() {
        given: "An existing TrainingType"
        TrainingType existingTrainingType = new TrainingType()
        existingTrainingType.setName("ExistingType")

        and: "TrainingTypeStorage returns the existing TrainingType"
        trainingTypeStorage.values() >> [existingTrainingType]

        when: "findByNameIgnoreCase is called with the name of the existing TrainingType"
        TrainingType result = trainingTypeDAO.findByNameIgnoreCase("ExistingType")

        then: "The result is the existing TrainingType"
        result == existingTrainingType
    }
}