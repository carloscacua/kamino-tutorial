package com.mahisoft.tutorial.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

// This annotation allows the service to be auto-configurable
// and allow to scan components for this application
@SpringBootApplication
// This annotation allows the application to scan for components (Beans) in the specified packages.
// By default we want to scan for components in this application and the beans defined in our commons library.
// If it is needed we could add more packages here. The scan is done recursively, so do not put too lower packages like 'com'.
@ComponentScan({"com.mahisoft.tutorial.service", "com.mahisoft.kamino.commonbeans"})
// By default PMD will detect this class as Utility because its has only static methods,
// but in this case we can't add a private constructor to fix the warning, so the way to fix
// this issue is by removing this specific warning
@SuppressWarnings("PMD.UseUtilityClass")
public class TutorialServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TutorialServiceApplication.class, args);
    }
}