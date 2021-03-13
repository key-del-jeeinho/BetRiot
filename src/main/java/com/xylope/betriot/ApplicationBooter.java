package com.xylope.betriot;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import javax.security.auth.login.LoginException;

public class ApplicationBooter {
    public static final ApplicationContext CONTEXT = new GenericXmlApplicationContext("/applicationContext.xml", "/secretContext.xml");

    public static void main(String[] args) throws LoginException {
        CONTEXT.getBean(BetRiotApplication.class).start();
    }
}
