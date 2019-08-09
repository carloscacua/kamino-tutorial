package com.mahisoft.tutorial.service.controller;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TutorialControllerTest {
    @LocalServerPort
    private int port;

    @Value("${server.contextPath}")
    private String contextPath;

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void getTest() {
        String person = "Peter Parker";
        String text = restTemplate.getForObject(String.format("http://localhost:%s%s/v1/tutorial/{person}", port, contextPath), String.class, person);

        Assert.assertEquals("Result doesn't match","Hello World Peter Parker!", text);
    }
}