package edu.ncsu.csc.CoffeeMaker.unit;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class RecipeTest {

    @Autowired
    private RecipeService service;

    @BeforeEach
    public void setup () {
        service.deleteAll();
    }

    @Test
    @Transactional
    public void testAddRecipe () {

        final Recipe r1 = new Recipe();

        r1.setName( "Black Coffee" );
        r1.setPrice( 1 );
        final Ingredient i1 = new Ingredient( "Coffee", 1 );
        final Ingredient i2 = new Ingredient( "Milk", 0 );
        final Ingredient i3 = new Ingredient( "Pumpkin Spice", 0 );

        r1.addIngredient( i1 );
        r1.addIngredient( i2 );
        r1.addIngredient( i3 );
        service.save( r1 );

        final Recipe r2 = new Recipe();
        r2.setName( "Mocha" );
        r2.setPrice( 1 );
        final Ingredient i4 = new Ingredient( "Coffee", 1 );
        final Ingredient i5 = new Ingredient( "Milk", 1 );
        final Ingredient i6 = new Ingredient( "Pumpkin Spice", 1 );

        r2.addIngredient( i4 );
        r2.addIngredient( i5 );
        r2.addIngredient( i6 );
        service.save( r2 );

        final List<Recipe> recipes = service.findAll();
        Assertions.assertEquals( 2, recipes.size(),
                "Creating two recipes should result in two recipes in the database" );

        Assertions.assertEquals( r1, recipes.get( 0 ), "The retrieved recipe should match the created one" );
    }

    // @Test
    // @Transactional
    // public void testNoRecipes () {
    // Assertions.assertEquals( 0, service.findAll().size(), "There should be no
    // Recipes in the CoffeeMaker" );
    //
    // final Recipe r1 = new Recipe();
    // r1.setName( "Tasty Drink" );
    // r1.setPrice( 12 );
    // final Ingredient i1 = new Ingredient( IngredientType.COFFEE, -12 );
    // final Ingredient i2 = new Ingredient( IngredientType.MILK, 0 );
    // final Ingredient i3 = new Ingredient( IngredientType.PUMPKIN_SPICE, 0 );
    //
    // r1.addIngredient( i1 );
    // r1.addIngredient( i2 );
    // r1.addIngredient( i3 );
    // service.save( r1 );
    //
    // final Recipe r2 = new Recipe();
    // r2.setName( "Mocha" );
    // r2.setPrice( 1 );
    // final Ingredient i4 = new Ingredient( IngredientType.COFFEE, 1 );
    // final Ingredient i5 = new Ingredient( IngredientType.MILK, 1 );
    // final Ingredient i6 = new Ingredient( IngredientType.PUMPKIN_SPICE, 1 );
    //
    // r2.addIngredient( i4 );
    // r2.addIngredient( i5 );
    // r2.addIngredient( i6 );
    // service.save( r2 );
    //
    // final List<Recipe> recipes = List.of( r1, r2 );
    //
    // try {
    // service.saveAll( recipes );
    // Assertions.assertEquals( 0, service.count(),
    // "Trying to save a collection of elements where one is invalid should
    // result in neither getting saved" );
    // }
    // catch ( final Exception e ) {
    // Assertions.assertTrue( e instanceof ConstraintViolationException );
    // }
    //
    // }

    @Test
    @Transactional
    public void testAddRecipe1 () {

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = new Recipe();
        r1.setName( "Mocha" );
        r1.setPrice( 1 );
        final Ingredient i4 = new Ingredient( "Coffee", 1 );
        final Ingredient i5 = new Ingredient( "Milk", 1 );
        final Ingredient i6 = new Ingredient( "Pumpkin Spice", 1 );

        r1.addIngredient( i4 );
        r1.addIngredient( i5 );
        r1.addIngredient( i6 );

        service.save( r1 );

        Assertions.assertEquals( 1, service.findAll().size(), "There should only one recipe in the CoffeeMaker" );
        Assertions.assertNull( service.findByName( name ) );

    }

    /* Test2 is done via the API for different validation */

    @Test
    @Transactional
    public void testAddRecipe3 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe();
        r1.setName( "Coffee" );
        r1.setPrice( -1 );
        final Ingredient i4 = new Ingredient( "Coffee", 1 );
        final Ingredient i5 = new Ingredient( "Milk", 1 );
        final Ingredient i6 = new Ingredient( "Pumpkin Spice", 1 );

        r1.addIngredient( i4 );
        r1.addIngredient( i5 );
        r1.addIngredient( i6 );

        try {
            service.save( r1 );

            Assertions.assertNull( service.findByName( "Coffee" ),
                    "A recipe was able to be created with a negative price" );
        }
        catch ( final ConstraintViolationException cvee ) {
            // expected
        }

    }

    @Test
    @Transactional
    public void testAddRecipe4 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe();
        r1.setName( "Coffee" );
        r1.setPrice( 1 );
        Assertions.assertThrows( IllegalArgumentException.class, () -> new Ingredient( "Coffee", -3 ),
                "Amount cannot be negative." );
        Assertions.assertThrows( IllegalArgumentException.class, () -> new Ingredient( "Milk", -3 ),
                "Amount cannot be negative." );

        final Ingredient i6 = new Ingredient( "Pumpkin Spice", 1 );

        r1.addIngredient( i6 );

        service.save( r1 );

        Assertions.assertEquals( 1, service.count() );
        Assertions.assertEquals( 1, r1.getPrice() );
        Assertions.assertEquals("Coffee", r1.getName());

    }

    @Test
    @Transactional
    public void testAddRecipe13 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe();
        r1.setName( "Tasty Drink" );
        r1.setPrice( 12 );
        final Ingredient i1 = new Ingredient( "Coffee", 12 );
        final Ingredient i2 = new Ingredient( "Milk", 0 );
        final Ingredient i3 = new Ingredient( "Pumpkin Spice", 0 );

        r1.addIngredient( i1 );
        r1.addIngredient( i2 );
        r1.addIngredient( i3 );
        service.save( r1 );

        final Recipe r2 = new Recipe();
        r2.setName( "Mocha" );
        r2.setPrice( 1 );
        final Ingredient i4 = new Ingredient( "Coffee", 1 );
        final Ingredient i5 = new Ingredient( "Milk", 1 );
        final Ingredient i6 = new Ingredient( "Pumpkin Spice", 1 );

        r2.addIngredient( i4 );
        r2.addIngredient( i5 );
        r2.addIngredient( i6 );
        service.save( r2 );

        Assertions.assertEquals( 2, service.count(),
                "Creating two recipes should result in two recipes in the database" );

    }

    @Test
    @Transactional
    public void testAddRecipe14 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe();
        r1.setName( "Tasty Drink" );
        r1.setPrice( 12 );
        final Ingredient i1 = new Ingredient( "Coffee", 12 );
        final Ingredient i2 = new Ingredient( "Milk", 0 );
        final Ingredient i3 = new Ingredient( "Pumpkin Spice", 0 );

        r1.addIngredient( i1 );
        r1.addIngredient( i2 );
        r1.addIngredient( i3 );
        service.save( r1 );

        final Recipe r2 = new Recipe();
        r2.setName( "Mocha" );
        r2.setPrice( 1 );
        final Ingredient i4 = new Ingredient( "Coffee", 1 );
        final Ingredient i5 = new Ingredient( "Milk", 1 );
        final Ingredient i6 = new Ingredient( "Pumpkin Spice", 1 );

        r2.addIngredient( i4 );
        r2.addIngredient( i5 );
        r2.addIngredient( i6 );
        service.save( r2 );

        final Recipe r3 = new Recipe();
        r3.setName( "Latte" );
        r3.setPrice( 1 );
        final Ingredient i7 = new Ingredient( "Coffee", 1 );
        final Ingredient i8 = new Ingredient( "Milk", 1 );
        final Ingredient i9 = new Ingredient( "Pumpkin Spice", 1 );

        r3.addIngredient( i7 );
        r3.addIngredient( i8 );
        r3.addIngredient( i9 );
        service.save( r3 );

        Assertions.assertEquals( 3, service.count(),
                "Creating three recipes should result in three recipes in the database" );

    }

    @Test
    @Transactional
    public void testDeleteRecipe1 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe();
        r1.setName( "Tasty Drink" );
        r1.setPrice( 12 );
        final Ingredient i1 = new Ingredient( "Coffee", 12 );
        final Ingredient i2 = new Ingredient( "Milk", 0 );
        final Ingredient i3 = new Ingredient( "Pumpkin Spice", 0 );

        r1.addIngredient( i1 );
        r1.addIngredient( i2 );
        r1.addIngredient( i3 );

        service.save( r1 );

        Assertions.assertEquals( 1, service.count(), "There should be one recipe in the database" );

        service.delete( r1 );
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
    }

    @Test
    @Transactional
    public void testDeleteRecipe2 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe();
        r1.setName( "Tasty Drink" );
        r1.setPrice( 12 );
        final Ingredient i1 = new Ingredient( "Coffee", 12 );
        final Ingredient i2 = new Ingredient( "Milk", 0 );
        final Ingredient i3 = new Ingredient( "Pumpkin Spice", 0 );

        r1.addIngredient( i1 );
        r1.addIngredient( i2 );
        r1.addIngredient( i3 );
        service.save( r1 );

        final Recipe r2 = new Recipe();
        r2.setName( "Mocha" );
        r2.setPrice( 1 );
        final Ingredient i4 = new Ingredient( "Coffee", 1 );
        final Ingredient i5 = new Ingredient( "Milk", 1 );
        final Ingredient i6 = new Ingredient( "Pumpkin Spice", 1 );

        r2.addIngredient( i4 );
        r2.addIngredient( i5 );
        r2.addIngredient( i6 );
        service.save( r2 );

        final Recipe r3 = new Recipe();
        r3.setName( "Latte" );
        r3.setPrice( 1 );
        final Ingredient i7 = new Ingredient( "Coffee", 1 );
        final Ingredient i8 = new Ingredient( "Milk", 1 );
        final Ingredient i9 = new Ingredient( "Pumpkin Spice", 1 );

        r3.addIngredient( i7 );
        r3.addIngredient( i8 );
        r3.addIngredient( i9 );
        service.save( r3 );

        Assertions.assertEquals( 3, service.count(), "There should be three recipes in the database" );

        service.deleteAll();

        Assertions.assertEquals( 0, service.count(), "`service.deleteAll()` should remove everything" );

    }

    @Test
    @Transactional
    public void testDeleteRecipe3 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe();
        r1.setName( "Tasty Drink" );
        r1.setPrice( 12 );
        final Ingredient i1 = new Ingredient( "Coffee", 12 );
        final Ingredient i2 = new Ingredient( "Milk", 0 );
        final Ingredient i3 = new Ingredient( "Pumpkin Spice", 0 );

        r1.addIngredient( i1 );
        r1.addIngredient( i2 );
        r1.addIngredient( i3 );
        service.save( r1 );

        final Recipe r2 = new Recipe();
        r2.setName( "Mocha" );
        r2.setPrice( 1 );
        final Ingredient i4 = new Ingredient( "Coffee", 1 );
        final Ingredient i5 = new Ingredient( "Milk", 1 );
        final Ingredient i6 = new Ingredient( "Pumpkin Spice", 1 );

        r2.addIngredient( i4 );
        r2.addIngredient( i5 );
        r2.addIngredient( i6 );
        service.save( r2 );

        final Recipe r3 = new Recipe();
        r3.setName( "Latte" );
        r3.setPrice( 1 );
        final Ingredient i7 = new Ingredient( "Coffee", 1 );
        final Ingredient i8 = new Ingredient( "Milk", 1 );
        final Ingredient i9 = new Ingredient( "Pumpkin Spice", 1 );

        r3.addIngredient( i7 );
        r3.addIngredient( i8 );
        r3.addIngredient( i9 );
        service.save( r3 );

        final Recipe r4 = new Recipe();
        r3.setName( "Latte" );
        r3.setPrice( 1 );
        final Ingredient i10 = new Ingredient( "Coffee", 1 );
        final Ingredient i11 = new Ingredient( "Milk", 1 );
        final Ingredient i12 = new Ingredient( "Pumpkin Spice", 1 );

        r3.addIngredient( i10 );
        r3.addIngredient( i11 );
        r3.addIngredient( i12 );

        Assertions.assertEquals( 3, service.count(), "There should be three recipes in the database" );

        // Testing deletion of multiple recipes at once
        service.delete( r1 );
        service.delete( r2 );

        Assertions.assertEquals( 1, service.findAll().size(), "There should be one Recipe in the CoffeeMaker" );

        service.delete( r4 );

        Assertions.assertNull( service.findByName( "Water" ), "There is no recipe with that name" );
        Assertions.assertEquals( 1, service.findAll().size(), "There should be one Recipe in the CoffeeMaker" );

        // // Testing when trying to delete a recipe that is no in the system
        // try {
        // // r4 was never saved to the database, so shouldn't be in system
        // service.delete( r4 );
        //
        // Assertions.assertNull( service.findByName( name ), "There is no
        // recipe with that name." );
        // Assertions.assertEquals( 1, service.findAll().size(), "There should
        // be one Recipe in the CoffeeMaker" );
        // }
        // catch ( final ConstraintViolationException cvee ) {
        // // expected
        // }

    }

    // Written by jyu34
    @Test
    @Transactional
    public void testFindRecipe () {
        final Recipe r1 = new Recipe();
        r1.setName( "Tasty Drink" );
        r1.setPrice( 12 );
        final Ingredient i1 = new Ingredient( "Coffee", 12 );
        final Ingredient i2 = new Ingredient( "Milk", 0 );
        final Ingredient i3 = new Ingredient( "Pumpkin Spice", 0 );

        r1.addIngredient( i1 );
        r1.addIngredient( i2 );
        r1.addIngredient( i3 );
        service.save( r1 );

        final Recipe r2 = new Recipe();
        r2.setName( "Mocha" );
        r2.setPrice( 1 );
        final Ingredient i4 = new Ingredient( "Coffee", 1 );
        final Ingredient i5 = new Ingredient( "Milk", 1 );
        final Ingredient i6 = new Ingredient( "Pumpkin Spice", 1 );

        r2.addIngredient( i4 );
        r2.addIngredient( i5 );
        r2.addIngredient( i6 );
        service.save( r2 );

        final Recipe r3 = new Recipe();
        r3.setName( "Latte" );
        r3.setPrice( 1 );
        final Ingredient i7 = new Ingredient( "Coffee", 1 );
        final Ingredient i8 = new Ingredient( "Milk", 1 );
        final Ingredient i9 = new Ingredient( "Pumpkin Spice", 1 );

        r3.addIngredient( i7 );
        r3.addIngredient( i8 );
        r3.addIngredient( i9 );
        service.save( r3 );

        Assertions.assertNull( service.findById( null ) );

        Assertions.assertEquals( r1, service.findById( r1.getId() ) );

        Assertions.assertNull( service.findById( r1.getId() + 10 ) );

        Assertions.assertFalse( service.existsById( r1.getId() + 10 ) );
    }

    @Test
    @Transactional
    public void testRecipeEquals () {

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final Recipe r1 = new Recipe();
        r1.setName( "Mocha" );
        r1.setPrice( 1 );
        final Ingredient i4 = new Ingredient( "Coffee", 1 );
        final Ingredient i5 = new Ingredient( "Milk", 1 );
        final Ingredient i6 = new Ingredient( "Pumpkin Spice", 1 );

        r1.addIngredient( i4 );
        r1.addIngredient( i5 );
        r1.addIngredient( i6 );

        service.save( r1 );

        final Recipe r2 = new Recipe();
        r2.setName( "Mocha" );
        r2.setPrice( 1 );
        final Ingredient i1 = new Ingredient( "Coffee", 1 );
        final Ingredient i2 = new Ingredient( "Milk", 1 );
        final Ingredient i3 = new Ingredient( "Pumpkin Spice", 1 );

        r2.addIngredient( i1 );
        r2.addIngredient( i2 );
        r2.addIngredient( i3 );

        service.save( r2 );

        final Recipe r3 = new Recipe();
        r3.setName( "Coffee" );

        final Recipe r4 = new Recipe();
        r4.setName( null );

        final Recipe r5 = new Recipe();
        r5.setName( "" );

        Assertions.assertTrue( r1.equals( r2 ) );
        Assertions.assertFalse( r1.equals( r3 ) );
        Assertions.assertFalse( r2.equals( r3 ) );
        Assertions.assertFalse( r4.equals( r5 ) );
        Assertions.assertFalse( r1.equals( r4 ) );


    }

}
