package edu.ncsu.csc.CoffeeMaker.unit;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class IngredientTest {

    @Autowired
    private IngredientService ingredientService;

    @BeforeEach
    public void setup () {
        ingredientService.deleteAll();
    }

    @Test
    public void createIngredient () {
        final Ingredient i1 = new Ingredient( "Coffee", 20 );
        ingredientService.save( i1 );
        Assert.assertEquals( 1, ingredientService.count() );
        Assert.assertEquals( "Coffee", i1.getName() );
        Assert.assertEquals( 20, i1.getAmount() );

        final Ingredient i2 = new Ingredient( "Milk", 17 );
        ingredientService.save( i2 );
        Assert.assertEquals( "Milk", i2.getName() );
        Assert.assertEquals( 2, ingredientService.count() );
        Assert.assertEquals( 17, i2.getAmount() );

        final Ingredient i3 = new Ingredient( "Pumpkin Spice", 12 );
        ingredientService.save( i3 );
        Assert.assertEquals( "Pumpkin Spice", i3.getName() );
        Assert.assertEquals( 3, ingredientService.count() );
        Assert.assertEquals( 12, i3.getAmount() );

    }

    @Test
    public void setIngredient () {
        final Ingredient i1 = new Ingredient( "Coffee", 20 );
        ingredientService.save( i1 );
        Assert.assertEquals( 1, ingredientService.count() );
        Assert.assertEquals( "Coffee", i1.getName() );
        Assert.assertEquals( 20, i1.getAmount() );

        i1.setName( "Milk" );
        Assert.assertEquals( "Milk", i1.getName() );

    }

    @Test
    public void ingredientAmount () {
        final Ingredient i1 = new Ingredient( "Coffee", 20 );
        ingredientService.save( i1 );
        Assert.assertEquals( 1, ingredientService.count() );
        Assert.assertEquals( "Coffee", i1.getName() );
        Assert.assertEquals( 20, i1.getAmount() );

        i1.setAmount( 50 );
        Assert.assertEquals( 50, i1.getAmount() );

    }

    @Test
    public void testToString () {
        final Ingredient i1 = new Ingredient( "Coffee", 20 );
        ingredientService.save( i1 );
        Assert.assertEquals( 1, ingredientService.count() );
        Assert.assertEquals( "Coffee", i1.getName() );
        Assert.assertEquals( 20, i1.getAmount() );

        Assert.assertEquals( "Ingredient [id=" + i1.getId() + ", ingredient=" + i1.getName() + ", amount="
                + i1.getAmount() + "]", i1.toString() );

    }

}
