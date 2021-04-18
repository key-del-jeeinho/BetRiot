package com.xylope.betriot;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class ApplicationBooter {
    public static final ApplicationContext CONTEXT = new GenericXmlApplicationContext(
            "/applicationContext.xml",
            "/secretContext.xml",
            "/betRefactoringContext.xml",
            "/userRefactoringContext.xml");

    public static void main(String[] args) throws InterruptedException {
        CONTEXT.getBean(BetRiotApplication.class).start();
    }
}
