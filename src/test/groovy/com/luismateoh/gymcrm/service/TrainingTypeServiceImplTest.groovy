package com.luismateoh.gymcrm.service

import com.luismateoh.gymcrm.dao.TrainingTypeDAO
import com.luismateoh.gymcrm.domain.TrainingType
import spock.lang.Specification

class TrainingTypeServiceImplTest extends Specification {

    TrainingTypeServiceImpl trainingTypeService
    TrainingTypeDAO trainingTypeDAO = Mock()

    def setup() {
        trainingTypeService = new TrainingTypeServiceImpl(trainingTypeDAO)
    }

    def "getOrCreateTrainingType returns existing TrainingType"() {
        given: "An existing TrainingType"
        TrainingType existingTrainingType = new TrainingType()
        existingTrainingType.setName("ExistingType")

        and: "TrainingTypeDAO returns the existing TrainingType"
        trainingTypeDAO.findByNameIgnoreCase("ExistingType") >> existingTrainingType

        when: "getOrCreateTrainingType is called with the name of the existing TrainingType"
        TrainingType result = trainingTypeService.getOrCreateTrainingType("ExistingType")

        then: "The result is the existing TrainingType"
        result == existingTrainingType
    }

    def "getOrCreateTrainingType creates new TrainingType when it does not exist"() {
        given: "TrainingTypeDAO returns null"
        trainingTypeDAO.findByNameIgnoreCase("NewType") >> null

        and: "A new TrainingType"
        TrainingType newTrainingType = new TrainingType()
        newTrainingType.setName("NewType")

        and: "TrainingTypeDAO saves the new TrainingType"
        trainingTypeDAO.save(_ as TrainingType) >> newTrainingType

        when: "getOrCreateTrainingType is called with a new name"
        TrainingType result = trainingTypeService.getOrCreateTrainingType("NewType")

        then: "The result is the new TrainingType"
        result == newTrainingType
    }
}