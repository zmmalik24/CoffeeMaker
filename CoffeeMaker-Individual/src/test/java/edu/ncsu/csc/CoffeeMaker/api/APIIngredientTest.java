package edu.ncsu.csc.CoffeeMaker.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith ( SpringExtension.class )
public class APIIngredientTest {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private IngredientService         service;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {
        final IngredientService ingredientService;

        mvc = MockMvcBuilders.webAppContextSetup( context ).build();

        service.deleteAll();
    }

    @Test
    @Transactional
    public void ensureIngredient () throws Exception {
        service.deleteAll();

        final Ingredient coffee = new Ingredient("Coffee", 5);
        Ingredient milk = new Ingredient("Milk", 5);


        service.save(coffee);
        service.save(milk);

        Assert.assertEquals( 2, service.count() );

        final List<Ingredient> list = service.findAll();

        final Long longArray[] = new Long[2];

        Long id = 0L;

        int counter = 0;
        for (final Ingredient i: list) {
            id = i.getId();
            longArray[counter] = id;
            counter++;

        }

        final String longString = Long.toString(id);


        mvc.perform( delete( "/api/v1/ingredients/1109"  ) );

        mvc.perform( get( "/api/v1/ingredients/" + longString  ) );




        milk = new Ingredient("Milk", 6);
        mvc.perform( put( "/api/v1/ingredients/" + longString  ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( milk ) )  );

        mvc.perform( delete( "/api/v1/ingredients/" + longString  ) );

        mvc.perform( get( "/api/v1/ingredient/id/longString" ));



        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( milk ) ) ).andExpect( status().is(201) );



    }


}

