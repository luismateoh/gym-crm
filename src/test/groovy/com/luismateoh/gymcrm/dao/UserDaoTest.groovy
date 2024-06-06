package com.luismateoh.gymcrm.dao

import com.luismateoh.gymcrm.domain.User
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.Transaction
import org.hibernate.query.Query
import spock.lang.Specification

class UserDaoTest extends Specification {

    UserDao userDao
    SessionFactory sessionFactory = Mock()
    Session session = Mock()
    Transaction transaction = Mock()

    def setup() {
        userDao = new UserDao(sessionFactory)
        sessionFactory.openSession() >> session
        session.beginTransaction() >> transaction
    }

    def "saveOrUpdate returns saved User"() {
        given: "A User"
        User user = new User()
        user.setUsername("username")
        user.setPassword("password")
        user.setFirstName("First")
        user.setLastName("Last")
        user.setIsActive(true)

        when: "saveOrUpdate is called"
        userDao.saveOrUpdate(user)

        then: "The transaction is committed and session is called with saveOrUpdate"
        1 * session.saveOrUpdate(user)
        1 * transaction.commit()
    }

    def "findById returns existing User"() {
        given: "An existing User"
        User existingUser = new User()
        existingUser.setUsername("existingUser")
        existingUser.setPassword("password")
        existingUser.setFirstName("First")
        existingUser.setLastName("Last")
        existingUser.setIsActive(true)

        and: "Session returns the existing User"
        session.get(User.class, 1L) >> existingUser

        when: "findById is called with the id of the existing User"
        User result = userDao.findById(1L)

        then: "The result is the existing User"
        result == existingUser
    }

    def "delete removes User"() {
        given: "An existing User"
        User user = new User()
        user.setUsername("username")
        user.setPassword("password")
        user.setFirstName("First")
        user.setLastName("Last")
        user.setIsActive(true)

        when: "delete is called with the existing User"
        userDao.delete(user)

        then: "The transaction is committed and session is called with remove"
        1 * session.remove(user)
        1 * transaction.commit()
    }

    def "findByUsername returns existing User"() {
        given: "An existing User"
        User existingUser = new User()
        existingUser.setUsername("existingUser")
        existingUser.setPassword("password")
        existingUser.setFirstName("First")
        existingUser.setLastName("Last")
        existingUser.setIsActive(true)
        Query query = Mock()

        and: "Session and query return the existing User"
        session.createQuery(_, User.class) >> query
        query.setParameter("username", "existingUser") >> query
        query.uniqueResult() >> existingUser

        when: "findByUsername is called with the username of the existing User"
        User result = userDao.findByUsername("existingUser")

        then: "The result is the existing User"
        result == existingUser
    }

    def "exists returns true when User exists"() {
        given: "An existing User"
        User existingUser = new User()
        existingUser.setUsername("existingUser")
        existingUser.setPassword("password")
        existingUser.setFirstName("First")
        existingUser.setLastName("Last")
        existingUser.setIsActive(true)
        Query query = Mock()

        and: "Session and query return the existing User"
        session.createQuery(_, User.class) >> query
        query.setParameter("username", "existingUser") >> query
        query.uniqueResult() >> existingUser

        when: "exists is called with the username of the existing User"
        boolean result = userDao.findByUsername("existingUser") != null

        then: "The result is true"
        result == true
    }
}
