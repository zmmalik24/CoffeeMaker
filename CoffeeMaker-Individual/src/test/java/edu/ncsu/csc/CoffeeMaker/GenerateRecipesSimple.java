package edu.ncsu.csc.CoffeeMaker;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@RunWith ( SpringRunner.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class GenerateRecipesSimple {

    @Autowired
    private RecipeService recipeService;

    @Test
    public void testCreateRecipes () {

        recipeService.deleteAll();

        final Recipe r1 = new Recipe();
        final Ingredient i1 = new Ingredient( "Coffee", 1 );
        final Ingredient i2 = new Ingredient( "Pumpkin Spice", 3 );
        final Ingredient i3 = new Ingredient( "Milk", 6 );
        r1.addIngredient( i1 );
        r1.addIngredient( i2 );
        r1.addIngredient( i3 );

        r1.setName( "Custom Coffee" );
        r1.setPrice( 1 );

        final Recipe r2 = new Recipe();
        final Ingredient i4 = new Ingredient( "Coffee", 5 );
        final Ingredient i5 = new Ingredient( "Milk", 2 );
        r2.addIngredient( i4 );
        r2.addIngredient( i5 );
        r2.setName( "Mocha" );
        r2.setPrice( 3 );

        recipeService.save( r1 );
        recipeService.save( r2 );

        Assert.assertEquals( "Creating two recipes should results in two recipes in the database", 2,
                recipeService.count() );

    }

}
