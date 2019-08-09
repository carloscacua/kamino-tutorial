package com.mahisoft.tutorial.service.controller;

import com.mahisoft.tutorial.service.service.TutorialService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "vi/tutorial", description = "Tutorial API")
@RestController
@RequestMapping("v1/tutorial")
@RequiredArgsConstructor
public class TutorialController {

    @Autowired
    private TutorialService service;

    @ApiOperation("Returns a greeting for a given person")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping("/{person}")
    public String get(
            @ApiParam(value = "Name of the person to greet", required = true)
            @PathVariable String person) {
        return service.getGreeting(person);
    }
}