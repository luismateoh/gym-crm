package com.luismateoh.gymcrm.utils

import spock.lang.Specification

class UtilsTest extends Specification {

    def "generatePassword"() {
        when: "generatePassword is called"
        String result = Utils.generatePassword()

        then: "The result should be a string of length 10"
        result.size() == 10

        and: "The result should only contain alphanumeric characters"
        result.matches("[A-Za-z0-9]+")
    }
}