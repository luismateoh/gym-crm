package com.luismateoh.gymcrm.service

import com.luismateoh.gymcrm.dao.UserDao
import com.luismateoh.gymcrm.domain.User
import com.luismateoh.gymcrm.dto.UserDTO
import com.luismateoh.gymcrm.mapper.UserMapper
import org.apache.commons.lang3.RandomStringUtils
import spock.lang.Specification

class UserServiceTest extends Specification {

    UserService userService
    UserDao userDao = Mock()

    def setup() {
        userService = new UserService(userDao)
    }

    def "registerUser returns created UserDTO"() {
        given: "User details"
        String firstName = "John"
        String lastName = "Doe"
        String username = "john.doe"
        String password = "password"
        boolean isActive = true

        User user = new User(id: 1, firstName: firstName, lastName: lastName, username: username, password: password, isActive: isActive)
        UserDTO userDTO = UserMapper.INSTANCE.userToUserDTO(user)

        and: "Mocks are set up"
        userDao.saveOrUpdate(_ as User) >> user

        when: "registerUser is called"
        UserDTO result = userService.registerUser(firstName, lastName, username, password, isActive)

        then: "The result is the created UserDTO"
        result == userDTO
    }

    def "login returns UserDTO if credentials are correct"() {
        given: "A User"
        String username = "john.doe"
        String password = "password"
        User user = new User(id: 1, firstName: "John", lastName: "Doe", username: username, password: password, isActive: true)
        UserDTO userDTO = UserMapper.INSTANCE.userToUserDTO(user)

        and: "UserDao returns the User"
        userDao.findByUsername(username) >> user

        when: "login is called with correct credentials"
        Optional<UserDTO> result = userService.login(username, password)

        then: "The result is the UserDTO"
        result.isPresent()
        result.get() == userDTO
    }

    def "login returns empty if credentials are incorrect"() {
        given: "A User"
        String username = "john.doe"
        String password = "password"
        User user = new User(id: 1, firstName: "John", lastName: "Doe", username: username, password: password, isActive: true)

        and: "UserDao returns the User"
        userDao.findByUsername(username) >> user

        when: "login is called with incorrect password"
        Optional<UserDTO> result = userService.login(username, "wrongPassword")

        then: "The result is empty"
        !result.isPresent()
    }

    def "generateUsername returns unique username"() {
        given: "A first name and last name"
        String firstName = "John"
        String lastName = "Doe"
        String baseUsername = "john.doe"

        and: "UserDao returns null for the base username"
        userDao.findByUsername(baseUsername) >> null

        when: "generateUsername is called"
        String result = userService.generateUsername(firstName, lastName)

        then: "The result is the base username"
        result == baseUsername
    }

    def "generateUsername returns unique username with counter"() {
        given: "A first name and last name"
        String firstName = "John"
        String lastName = "Doe"
        String baseUsername = "john.doe"

        and: "UserDao returns existing user for the base username"
        userDao.findByUsername(baseUsername) >> new User()
        userDao.findByUsername(baseUsername + "1") >> null

        when: "generateUsername is called"
        String result = userService.generateUsername(firstName, lastName)

        then: "The result is the base username with counter"
        result == baseUsername + "1"
    }

    def "generatePassword returns random password"() {
        when: "generatePassword is called"
        String result = userService.generatePassword()

        then: "The result is a random alphanumeric string of length 10"
        result.length() == 10
        result.each { char c ->
            assert c in RandomStringUtils.randomAlphanumeric(10).toCharArray()
        }
    }

    def "changePassword updates the User password"() {
        given: "A UserDTO"
        UserDTO userDTO = new UserDTO(id: 1, firstName: "John", lastName: "Doe", username: "john.doe", password: "password", isActive: true)
        User user = UserMapper.INSTANCE.userDTOToUser(userDTO)

        when: "changePassword is called"
        userService.changePassword(userDTO, "newPassword")

        then: "UserDao saveOrUpdate is called with the updated User"
        1 * userDao.saveOrUpdate(_ as User) >> { User updatedUser ->
            assert updatedUser.password == "newPassword"
        }
    }

    def "setActiveStatus sets the User active status"() {
        given: "A username"
        String username = "john.doe"
        User user = new User(id: 1, firstName: "John", lastName: "Doe", username: username, password: "password", isActive: true)

        and: "UserDao returns the User"
        userDao.findByUsername(username) >> user

        when: "setActiveStatus is called"
        userService.setActiveStatus(username, false)

        then: "UserDao saveOrUpdate is called with the updated User"
        1 * userDao.saveOrUpdate(_ as User) >> { User updatedUser ->
            assert updatedUser.isActive == false
        }
    }

    def "findByUsername returns UserDTO if user exists"() {
        given: "A username"
        String username = "john.doe"
        User user = new User(id: 1, firstName: "John", lastName: "Doe", username: username, password: "password", isActive: true)
        UserDTO userDTO = UserMapper.INSTANCE.userToUserDTO(user)

        and: "UserDao returns the User"
        userDao.findByUsername(username) >> user

        when: "findByUsername is called"
        Optional<UserDTO> result = userService.findByUsername(username)

        then: "The result is the UserDTO"
        result.isPresent()
        result.get() == userDTO
    }

    def "findByUsername returns empty if user does not exist"() {
        given: "A username"
        String username = "john.doe"

        and: "UserDao returns null"
        userDao.findByUsername(username) >> null

        when: "findByUsername is called"
        Optional<UserDTO> result = userService.findByUsername(username)

        then: "The result is empty"
        !result.isPresent()
    }

    def "validatePassword returns true if password matches"() {
        given: "A UserDTO with a password"
        UserDTO userDTO = new UserDTO(id: 1, firstName: "John", lastName: "Doe", username: "john.doe", password: "password", isActive: true)

        when: "validatePassword is called with the correct password"
        boolean result = userService.validatePassword(userDTO, "password")

        then: "The result is true"
        result == true
    }

    def "validatePassword returns false if password does not match"() {
        given: "A UserDTO with a password"
        UserDTO userDTO = new UserDTO(id: 1, firstName: "John", lastName: "Doe", username: "john.doe", password: "password", isActive: true)

        when: "validatePassword is called with an incorrect password"
        boolean result = userService.validatePassword(userDTO, "wrongPassword")

        then: "The result is false"
        result == false
    }
}
