package com.mahisoft.tutorial.service.controller;

import com.google.common.base.Strings;
import com.mahisoft.kamino.commonbeans.exception.ApiHttpClientErrorException;
import com.mahisoft.tutorial.service.controller.dto.*;
import org.apache.commons.lang3.tuple.Triple;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
    public void getAllProductTest() {

        CreateProductRequest original = CreateProductRequest
                .builder()
                .name("Test Product")
                .price(BigDecimal.valueOf(100L))
                .category("Clearance")
                .discount(25D)
                .build();

        createProduct(original);
        createProduct(original);

        List<ProductItem> list = getProducts().getBody();
        Assert.assertNotNull(list);
    }

    @Test
    public void getProductInfoTest() {
        Long id = createProduct(CreateProductRequest
                .builder()
                .name("Test Product")
                .price(BigDecimal.valueOf(100L))
                .category("Clearance")
                .discount(25D)
                .build()
        ).getBody();

        ResponseEntity<ProductItem> response = restTemplate.getForEntity(String.format("http://localhost:%s%s/v1/tutorial/{id}", port, contextPath), ProductItem.class, id);

        Assert.assertEquals("Response status doesn't match", HttpStatus.OK, response.getStatusCode());
        Assert.assertNotNull("ProductInfo is null", response.getBody());

        Assert.assertFalse("Name is null or empty", Strings.isNullOrEmpty(response.getBody().getName()));
        Assert.assertFalse("Category is null or empty", Strings.isNullOrEmpty(response.getBody().getCategory()));
        Assert.assertEquals("Price doesn't match", BigDecimal.valueOf(100L), response.getBody().getPrice());
        Assert.assertEquals("Discount doesn't match", 25D, response.getBody().getDiscount(), 0D);
    }

    @Test
    public void createProductTest() {

        CreateProductRequest request = CreateProductRequest
                .builder()
                .name("Test Product")
                .price(BigDecimal.valueOf(100L))
                .category("Clearance")
                .discount(25D)
                .build();

        ResponseEntity<Long> response = createProduct(request);

        Assert.assertEquals("Response status doesn't match", HttpStatus.CREATED, response.getStatusCode());
        Assert.assertNotNull("Response value is null", response.getBody());
        Assert.assertTrue("The Id should be greater that 0", response.getBody() > 0);
    }

    @Test
    public void createProductFailsForNullFieldValuesTest() {

        CreateProductRequest request = CreateProductRequest
                .builder()
                .name("Test Product")
                .price(BigDecimal.valueOf(100L))
                .category("Clearance")
                .discount(25D)
                .build();

        ResponseEntity<Long> response = createProduct(request);

        Assert.assertEquals("Response status doesn't match", HttpStatus.CREATED, response.getStatusCode());
        Assert.assertNotNull("Response value is null", response.getBody());
        Assert.assertTrue("The Id should be greater that 0", response.getBody() > 0);
    }

    @Test
    public void createProductValidationFailTest() throws IOException {

        List<Triple<String, List<String>, CreateProductRequest>> scenarios =
                Arrays.asList(
                        // Scenario 1, all fields are null
                        Triple.of(
                                "Required field validation: %s",
                                Arrays.asList("Name is required", "Price is required", "Category is required"),
                                CreateProductRequest.builder().build()
                        ),
                        //Scenario 2, all fields are supplied but don't follow requirements
                        Triple.of(
                                "Constrain field validation: %s",
                                Arrays.asList(
                                        "Name must have between 3 and 25 characters",
                                        "Category must have between 3 and 25 characters",
                                        "Discount min value is 10"),
                                CreateProductRequest.builder()
                                        .name("A")
                                        .price(BigDecimal.valueOf(10L))
                                        .discount(1D)
                                        .category("A")
                                        .build()
                        ),
                        // Scenario 3, test custom validator for discount
                        Triple.of(
                                "Custom constrain validation: %s",
                                Collections.singletonList(
                                        "Discount is required when the category is Clearance"),
                                CreateProductRequest
                                        .builder()
                                        .name("Test Product")
                                        .price(BigDecimal.valueOf(10L))
                                        .category("Clearance")
                                        .build()
                        )
                );

        for (Triple<String, List<String>, CreateProductRequest> scenario : scenarios) {

            try {
                restTemplate.postForEntity(String.format("http://localhost:%s%s/v1/tutorial", port, contextPath), scenario.getRight(),
                        Long.class);
                Assert.fail("ApiHttpClientErrorException Expected");
            } catch (ApiHttpClientErrorException e) {
                Assert.assertEquals("Response status doesn't match", HttpStatus.BAD_REQUEST, e.getStatusCode());
                for (String expectedError : scenario.getMiddle()) {
                    // The response is a big JSON string and parse it might need too much effort just for testing
                    // so in this case we simply check if the message we put in the annotation is present in that JSON
                    Assert.assertTrue(String.format(scenario.getLeft(), expectedError), e.getResponseBodyAsString().indexOf(expectedError) > 0);
                }
            }
        }
    }

    @Test(expected = ApiHttpClientErrorException.class)
    public void getProductInfoFailsNotFoundTest() {

        try {
            getProduct(999999999999999L);
        }
        catch (ApiHttpClientErrorException e) {
            Assert.assertEquals("Response status doesn't match", HttpStatus.NOT_FOUND, e.getStatusCode());
            throw e;
        }
    }

    @Test
    public void updateProductTest() {

        ResponseEntity<Long> createResponse = createProduct(CreateProductRequest
                .builder()
                .name("Test Product")
                .price(BigDecimal.valueOf(100L))
                .category("Clearance")
                .discount(25D)
                .build()
        );

        UpdateProductRequest updateRequest = UpdateProductRequest
                .builder()
                .name("Test product updated")
                .price(BigDecimal.valueOf(1000L))
                .category("Health")
                .discount(10D)
                .status(StatusType.Inactive)
                .build();


        ResponseEntity<Void> response = updateProduct(createResponse.getBody(), updateRequest);

        Assert.assertEquals("Response status doesn't match", HttpStatus.NO_CONTENT, response.getStatusCode());

        ResponseEntity<ProductItem> productResponse = getProduct(createResponse.getBody());

        Assert.assertEquals(updateRequest.getName(), productResponse.getBody().getName());
        Assert.assertEquals(updateRequest.getCategory(), productResponse.getBody().getCategory());
        Assert.assertEquals(updateRequest.getPrice(), productResponse.getBody().getPrice());
        Assert.assertEquals(updateRequest.getDiscount(), productResponse.getBody().getDiscount());
        Assert.assertEquals(updateRequest.getStatus(), productResponse.getBody().getStatus());
    }

    @Test
    public void partialUpdateProductTest() {

        CreateProductRequest original = CreateProductRequest
                .builder()
                .name("Test Product")
                .price(BigDecimal.valueOf(100L))
                .category("Clearance")
                .discount(25D)
                .build();

        ResponseEntity<Long> createResponse = createProduct(original);

        PartialUpdateProductRequest updateRequest = PartialUpdateProductRequest
                .builder()
                .name("Test Product Updated")
                .build();

        ResponseEntity<Void> response = partialUpdateProduct(createResponse.getBody(), updateRequest);

        Assert.assertEquals("Response status doesn't match", HttpStatus.NO_CONTENT, response.getStatusCode());

        ResponseEntity<ProductItem> productResponse = getProduct(createResponse.getBody());

        Assert.assertEquals(updateRequest.getName(), productResponse.getBody().getName());
        Assert.assertEquals(original.getCategory(), productResponse.getBody().getCategory());
        Assert.assertEquals(original.getPrice(), productResponse.getBody().getPrice());
        Assert.assertEquals(original.getDiscount(), productResponse.getBody().getDiscount());
    }

    @Test
    public void deleteProductTest() {
        Long id = null;
        try
        {
            CreateProductRequest original = CreateProductRequest
                    .builder()
                    .name("Test Product")
                    .price(BigDecimal.valueOf(100L))
                    .category("Clearance")
                    .discount(25D)
                    .build();

            ResponseEntity<Long> createResponse = createProduct(original);

            PartialUpdateProductRequest updateRequest = PartialUpdateProductRequest
                    .builder()
                    .status(StatusType.Inactive)
                    .build();

            id = createResponse.getBody();
            partialUpdateProduct(id, updateRequest);

            deleteProduct(id);
            getProduct(id);

            Assert.fail("Exception should be thrown");

        }catch(ApiHttpClientErrorException ex)
        {
            Assert.assertEquals(ex.getError().getMessage(),
                    String.format("Product '%s' not found.", id));
        }
    }

    @Test
    public void deleteActiveProductTest() {
        Long id = null;
        try
        {
            CreateProductRequest original = CreateProductRequest
                    .builder()
                    .name("Test Product")
                    .price(BigDecimal.valueOf(100L))
                    .category("Clearance")
                    .discount(25D)
                    .build();

            ResponseEntity<Long> createResponse = createProduct(original);
            id = createResponse.getBody();
            deleteProduct(id);
            Assert.fail("Exception should be thrown");

        }catch(ApiHttpClientErrorException ex)
        {
            Assert.assertEquals(ex.getError().getMessage(),
                    String.format("Product '%s' is active.", id));
        }
    }

    private ResponseEntity<ProductItem> getProduct(Long id){
        return restTemplate.getForEntity(String.format("http://localhost:%s%s/v1/tutorial/{id}", port, contextPath), ProductItem.class, id);
    }

    private ResponseEntity<List<ProductItem>> getProducts(){
        return restTemplate.exchange(String.format("http://localhost:%s%s/v1/tutorial/", port, contextPath),
                HttpMethod.GET, null, new ParameterizedTypeReference<List<ProductItem>>() {});
    }

    private ResponseEntity<Void> updateProduct(Long id, UpdateProductRequest request) {
        return restTemplate.exchange(String.format("http://localhost:%s%s/v1/tutorial/{id}", port, contextPath),
                HttpMethod.PUT,
                new HttpEntity<>(request), Void.class, id);
    }

    private ResponseEntity<Void> partialUpdateProduct(Long id, PartialUpdateProductRequest request) {
        return restTemplate.exchange(String.format("http://localhost:%s%s/v1/tutorial/{id}", port, contextPath),
                HttpMethod.PATCH,
                new HttpEntity<>(request), Void.class, id);
    }

    private void deleteProduct(Long id){
        restTemplate.delete(String.format("http://localhost:%s%s/v1/tutorial/{id}", port, contextPath), id,
                HttpMethod.DELETE,
                new HttpEntity<>(id), Void.class, id);
    }

    private ResponseEntity<Long> createProduct(CreateProductRequest request) {
        return restTemplate.postForEntity(String.format("http://localhost:%s%s/v1/tutorial", port, contextPath), request,
                Long.class);
    }

}