package com.luismateoh.gymcrm.utils

import spock.lang.Specification

class SerialNumberGeneratorTest extends Specification {

    SerialNumberGenerator serialNumberGenerator

    def setup() {
        serialNumberGenerator = new SerialNumberGenerator()
    }

    def "getNextSerialNumber"() {
        when: "getNextSerialNumber is called"
        long result = serialNumberGenerator.getNextSerialNumber()

        then: "The result should be 1"
        result == 1L

        when: "getNextSerialNumber is called again"
        result = serialNumberGenerator.getNextSerialNumber()

        then: "The result should be 2"
        result == 2L
    }

    def "getCurrentSerialNumber"() {
        when: "getCurrentSerialNumber is called"
        long result = serialNumberGenerator.getCurrentSerialNumber()

        then: "The result should be 1"
        result == 1L

        when: "getNextSerialNumber is called"
        serialNumberGenerator.getNextSerialNumber()

        and: "getCurrentSerialNumber is called again"
        result = serialNumberGenerator.getCurrentSerialNumber()

        then: "The result should be 2"
        result == 2L
    }

    def "getNextSerialNumber thread safety"() {
        given: "A set to store the generated serial numbers"
        Set<Long> serialNumbers = Collections.synchronizedSet(new HashSet<>())

        and: "A number of threads"
        int numThreads = 1000

        when: "Each thread calls getNextSerialNumber"
        List<Thread> threads = (0..<numThreads).collect {
            Thread.start {
                serialNumbers << serialNumberGenerator.getNextSerialNumber()
            }
        }
        threads*.join()

        then: "All generated serial numbers should be unique"
        serialNumbers.size() == numThreads
    }
}