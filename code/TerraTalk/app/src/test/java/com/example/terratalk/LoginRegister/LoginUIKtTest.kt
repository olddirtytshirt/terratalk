package com.example.terratalk.LoginRegister

import org.junit.Assert.*
import org.junit.Test

class LoginUIKtTest {

    //email validation assertions
    @Test
    fun isValidEmailTest() {
        assertTrue(isValidEmail("name@email.com"))
        assertFalse(isValidEmail("name"))
        assertFalse(isValidEmail("name@email"))
        assertFalse(isValidEmail("@email"))
        assertFalse(isValidEmail("null"))

    }

    //password validation assertions
    @Test
    fun isValidPasswordTest() {
        assertTrue(isValidPassword("password"))
        assertTrue(isValidPassword("secret"))
        assertFalse(isValidPassword("test"))
    }
}