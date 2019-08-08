package com.mahisoft.tutorial.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

// This two annotation enable test for SpringBoot, any test within this class will be executed
// using a Spring Application Context that allows you to use the configured beans, inject Mocks to
// the application, load files and any resource you need for your test in a Spring way.
@RunWith(SpringRunner.class)
@SpringBootTest
public class TutorialServiceApplicationTest {


    // For this particular test we don't need an assert. This test will
    // fail if the application can't load, so this empty method is more than
    // enough.
    @Test
    public void contextLoads() {
        // Application Loaded
    }
}