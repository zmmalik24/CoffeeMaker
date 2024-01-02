package edu.ncsu.csc.CoffeeMaker.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@ExtendWith ( SpringExtension.class )
@SpringBootTest
@AutoConfigureMockMvc
public class APICoffeeTest {

    @Autowired
    private MockMvc          mvc;

    @Autowired
    private RecipeService    service;

    @Autowired
    private InventoryService iService;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {
        service.deleteAll();
        iService.deleteAll();

        final Inventory ivt = iService.getInventory();

        //        ivt.setChocolate( 15 );
        //        ivt.setCoffee( 15 );
        //        ivt.setMilk( 15 );
        //        ivt.setSugar( 15 );

        iService.save( ivt );

        final Recipe recipe = new Recipe();
        recipe.setName( "Coffee" );
        recipe.setPrice( 50 );
        //        recipe.setCoffee( 3 );
        //        recipe.setMilk( 1 );
        //        recipe.setSugar( 1 );
        //        recipe.setChocolate( 0 );
        service.save( recipe );
    }

    @Test
    @Transactional
    public void testNullCoffeeRecipe () throws Exception {
        /* Insufficient amount paid */

        final String name = " ";

        mvc.perform( post( String.format( "/api/v1/makecoffee/%s", name ) ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( 40 ) ) ).andExpect( status().is4xxClientError() )
        .andExpect( jsonPath( "$.message" ).value( "No recipe selected" ) );

    }

    @Test
    @Transactional
    public void testPurchaseBeverage1 () throws Exception {

        final String name = "Coffee";

        mvc.perform( post( String.format( "/api/v1/makecoffee/%s", name ) ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( 60 ) ) ).andExpect( status().isOk() )
        .andExpect( jsonPath( "$.message" ).value( 10 ) );

    }

    @Test
    @Transactional
    public void testPurchaseBeverage2 () throws Exception {
        /* Insufficient amount paid */

        final String name = "Coffee";

        mvc.perform( post( String.format( "/api/v1/makecoffee/%s", name ) ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( 40 ) ) ).andExpect( status().is4xxClientError() )
        .andExpect( jsonPath( "$.message" ).value( "Not enough money paid" ) );

    }

    //    @Test
    //    @Transactional
    //    public void testPurchaseBeverage3 () throws Exception {
    //        final String name = "Coffee";
    //
    //        /* Insufficient inventory */
    //
    //        final Inventory ivt = iService.getInventory();
    //        final List<Ingredient> ingredientList = new ArrayList<Ingredient>();
    //        service.deleteAll();
    //
    //        final Ingredient coffee = new Ingredient(IngredientType.COFFEE, 0);
    //        final Ingredient milk = new Ingredient(IngredientType.MILK, 0);
    //        ingredientList.add( coffee );
    //        ingredientList.add( milk );
    //        ivt.addIngredients( ingredientList );
    //
    //        iService.save( ivt );
    //
    //        mvc.perform( post( String.format( "/api/v1/makecoffee/%s", name ) ).contentType( MediaType.APPLICATION_JSON )
    //                .content( TestUtils.asJsonString( 50 ) ) ).andExpect( status().is4xxClientError() )
    //        .andExpect( jsonPath( "$.message" ).value( "Not enough inventory" ) );
    //
    //    }

}
