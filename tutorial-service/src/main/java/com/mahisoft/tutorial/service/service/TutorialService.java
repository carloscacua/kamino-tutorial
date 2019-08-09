package com.mahisoft.tutorial.service.service;

import org.springframework.stereotype.Service;

@Service
public class TutorialService {

    public String getGreeting(String person) {
        return String.format("Hello World %s!", person);
    }
}