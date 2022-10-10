package com.example.androidassignments;

import junit.framework.TestCase;

public class LoginActivityTest extends TestCase {

    public void testIsEmailValid() {
        assertTrue(LoginActivity.isEmailValid("thisisatest@gmail.com"));
        assertFalse(LoginActivity.isEmailValid("thisisatestgmail.com"));
        assertFalse(LoginActivity.isEmailValid("thisisatest@gmailcom"));
        assertFalse(LoginActivity.isEmailValid("this  isatest@gmail.com"));
        assertFalse(LoginActivity.isEmailValid(""));
        assertFalse(LoginActivity.isEmailValid(null));

    }
}