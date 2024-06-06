package com.luismateoh.gymcrm.service

import com.luismateoh.gymcrm.dao.TrainingTypeDao
import com.luismateoh.gymcrm.domain.TrainingType
import spock.lang.Specification

class TrainingTypeServiceTest extends Specification {

    TrainingTypeService trainingTypeService
    TrainingTypeDao trainingTypeDao = Mock()

    def setup() {
        trainingTypeService = new TrainingTypeService(trainingTypeDao)
    }

    def "findByName returns existing TrainingType"() {
        given: "An existing TrainingType"
        String name = "fitness"
        TrainingType trainingType = new TrainingType(id: 1, trainingTypeName: name)

        and: "TrainingTypeDao returns the list of TrainingTypes"
        trainingTypeDao.findAll() >> [trainingType]

        when: "findByName is called with the name of the existing TrainingType"
        Optional<TrainingType> result = trainingTypeService.findByName(name)

        then: "The result is the existing TrainingType"
        result.isPresent()
        result.get() == trainingType
    }

    def "findByName returns empty when TrainingType does not exist"() {
        given: "A non-existing TrainingType name"
        String name = "non-existing"

        and: "TrainingTypeDao returns an empty list"
        trainingTypeDao.findAll() >> []

        when: "findByName is called with the non-existing TrainingType name"
        Optional<TrainingType> result = trainingTypeService.findByName(name)

        then: "The result is empty"
        !result.isPresent()
    }
}
