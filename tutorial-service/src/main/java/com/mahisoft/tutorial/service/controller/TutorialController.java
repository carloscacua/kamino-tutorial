package com.mahisoft.tutorial.service.controller;

import com.mahisoft.tutorial.service.controller.dto.CreateProductRequest;
import com.mahisoft.tutorial.service.controller.dto.PartialUpdateProductRequest;
import com.mahisoft.tutorial.service.controller.dto.ProductItem;
import com.mahisoft.tutorial.service.controller.dto.UpdateProductRequest;
import com.mahisoft.tutorial.service.service.TutorialService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "vi/tutorial", description = "Tutorial API")
@RestController
@RequestMapping("v1/tutorial")
@RequiredArgsConstructor
public class TutorialController {

    private final TutorialService service;

    @ApiOperation("Returns the information of the given product id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping("/{id}")
    public ProductItem get(
            @ApiParam(value = "The id of the product", required = true)
            @PathVariable Long id) {
        return service.getProduct(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long createProduct(@RequestBody @Validated CreateProductRequest request) {
        return service.createProduct(request);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateProduct(@PathVariable Long id, @RequestBody @Validated UpdateProductRequest request) {
        service.updateProduct(id, request);
    }

    @PatchMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void partialUpdateProduct(@PathVariable Long id, @RequestBody @Validated PartialUpdateProductRequest request) {
        service.partialUpdateProduct(id, request);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id) {
        service.deleteProduct(id);
    }


    @ApiOperation("Returns the information of all  the products")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping()
    public List<ProductItem> getProducts() {
        return service.getProducts();
    }
}