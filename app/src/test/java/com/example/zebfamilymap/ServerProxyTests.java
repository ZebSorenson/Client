package com.example.zebfamilymap;

import org.junit.Test;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


import BackendLogic.ServerProxy;
import RequestResult.LoginRequest;
import RequestResult.LoginResult;

public class ServerProxyTests {

    private final ServerProxy serverProxy = new ServerProxy("localhost", "8080");

    @Test
    void testLoginSuccess() {
        LoginRequest loginRequest = new LoginRequest();

        loginRequest.setUsername("sheila");
        loginRequest.setPassword("parker");

        LoginResult loginResult = serverProxy.login(loginRequest);

        assertTrue(loginResult.getSuccess());
        assertNotNull(loginResult.getAuthtoken());
        assertNotNull(loginResult.getPersonID());
    }
}
