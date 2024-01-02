package edu.ncsu.csc.CoffeeMaker.unit;

import java.util.ArrayList;
import java.util.List;

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
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class InventoryTest {

    @Autowired
    private InventoryService inventoryService;

    @BeforeEach
    public void setup () {
        inventoryService.deleteAll();
    }

    @Test
    @Transactional
    public void testConsumeInventory () {

        final Inventory i = inventoryService.getInventory();

        final Ingredient coffeeStock = new Ingredient("Coffee", 100);
        final Ingredient milkStock = new Ingredient("Milk", 100);
        final Ingredient pumpkinStock = new Ingredient("Pumpkin Spice", 100);

        final List<Ingredient> stock = new ArrayList<Ingredient>();
        stock.add(coffeeStock);
        stock.add(milkStock);
        stock.add(pumpkinStock);

        i.addIngredient(coffeeStock);


        final Recipe recipe = new Recipe();
        final Ingredient i1 = new Ingredient("Coffee", 1);
        final Ingredient i2 = new Ingredient("Milk", 20);
        final Ingredient i3 = new Ingredient("Pumpkin Spice", 5);

        recipe.setName( "Delicious Not-Coffee" );

        recipe.addIngredient( i1 );
        recipe.addIngredient( i2 );
        recipe.addIngredient( i3 );

        recipe.setPrice( 5 );



        i.useIngredients( recipe );

        /*
         * Make sure that all of the inventory fields are now properly updated
         */

        Assertions.assertEquals( 99, i.getInventory().get( 0 ).getAmount());

    }

    @Test
    @Transactional
    public void testAddInventory1 () {

        final Inventory i = inventoryService.getInventory();

        final Ingredient coffeeStock = new Ingredient("Coffee", 100);
        final Ingredient milkStock = new Ingredient("Milk", 100);
        final Ingredient pumpkinStock = new Ingredient("Pumpkin Spice", 100);

        final List<Ingredient> stock = new ArrayList<Ingredient>();
        stock.add(coffeeStock);
        stock.add(milkStock);
        stock.add(pumpkinStock);

        i.addIngredient(coffeeStock);

        i.updateAmount(coffeeStock);

        /* Save and retrieve again to update with DB */
        inventoryService.save( i );



        Assertions.assertEquals( 200, i.getIngredient("Coffee"),
                "Adding to the inventory should result in correctly-updated values for coffee" );
       


    }

    @Test
    @Transactional
    public void testAddInventory2 () {
        final Inventory i = inventoryService.getInventory();

        try {
            final Ingredient coffeeStock = new Ingredient("Coffee", -100);
            final Ingredient milkStock = new Ingredient("Milk", 100);
            final Ingredient pumpkinStock = new Ingredient("Pumpkin Spice", 100);

            final List<Ingredient> stock = new ArrayList<Ingredient>();
            stock.add(coffeeStock);
            stock.add(milkStock);
            stock.add(pumpkinStock);

            i.addIngredient(coffeeStock);

            i.updateAmount(coffeeStock);
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( 0, i.getIngredient("Coffee"),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- coffee" );

        }
    }

    @Test
    @Transactional
    public void testAddInventory3 () {
        final Inventory i = inventoryService.getInventory();

        try {
            final Ingredient coffeeStock = new Ingredient("Coffee", 100);
            final Ingredient milkStock = new Ingredient("Milk", -100);
            final Ingredient pumpkinStock = new Ingredient("Pumpkin Spice", 100);

            final List<Ingredient> stock = new ArrayList<Ingredient>();
            stock.add(coffeeStock);
            stock.add(milkStock);
            stock.add(pumpkinStock);

            i.addIngredient(coffeeStock);

            i.updateAmount(coffeeStock);
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( 0, i.getIngredient("Milk"),
                    "Trying to update the Inventory with an invalid value for milk should result in no changes -- coffee" );
        }
    }

    @Test
    @Transactional
    public void testAddInventory4 () {
        final Inventory i = inventoryService.getInventory();

        try {
            final Ingredient coffeeStock = new Ingredient("Coffee", 100);
            final Ingredient milkStock = new Ingredient("Milk", 100);
            final Ingredient pumpkinStock = new Ingredient("Pumpkin Spice", -100);

            final List<Ingredient> stock = new ArrayList<Ingredient>();
            stock.add(coffeeStock);
            stock.add(milkStock);
            stock.add(pumpkinStock);

            i.addIngredient(coffeeStock);

            i.updateAmount(coffeeStock);
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( 0, i.getIngredient("Pumpkin Spice"),
                    "Trying to update the Inventory with an invalid value for pumpkin spice should result in no changes -- coffee" );
        }
    }

    //Written by jqdo
    //@Test
    //    @Transactional
    //    public void testEnoughIngredients () {
    //        Inventory ivt = inventoryService.getInventory();
    //        final Recipe recipe = new Recipe();
    //        recipe.setName( "Delicious Not-Coffee" );
    //        recipe.setChocolate( 10 );
    //        recipe.setMilk( 20 );
    //        recipe.setSugar( 5 );
    //        recipe.setCoffee( 1 );
    //        ivt.addIngredients( 5, 3, 7, 2 );
    //
    //        /* Save and retrieve again to update with DB */
    //        inventoryService.save( ivt );
    //
    //        ivt = inventoryService.getInventory();
    //
    //        Assertions.assertEquals( 505, (int) ivt.getCoffee(),
    //                "Adding to the inventory should result in correctly-updated values for coffee" );
    //        Assertions.assertEquals( 503, (int) ivt.getMilk(),
    //                "Adding to the inventory should result in correctly-updated values for milk" );
    //        Assertions.assertEquals( 507, (int) ivt.getSugar(),
    //                "Adding to the inventory should result in correctly-updated values sugar" );
    //        Assertions.assertEquals( 502, (int) ivt.getChocolate(),
    //                "Adding to the inventory should result in correctly-updated values chocolate" );
    //
    //        Assertions.assertTrue( ivt.enoughIngredients( recipe ) );
    //
    //        ivt.setChocolate( 0 );
    //        ivt.setCoffee( 0 );
    //        ivt.setMilk( 0 );
    //        ivt.setSugar( 0 );
    //
    //        /* Save and retrieve again to update with DB */
    //        inventoryService.save( ivt );
    //
    //        ivt = inventoryService.getInventory();
    //
    //        Assertions.assertEquals( 0, (int) ivt.getCoffee(),
    //                "Setting a value to the inventory should result in correctly-updated values for coffee" );
    //        Assertions.assertEquals( 0, (int) ivt.getMilk(),
    //                "Setting a value to the inventory should result in correctly-updated values for milk" );
    //        Assertions.assertEquals( 0, (int) ivt.getSugar(),
    //                "Setting a value to the inventory should result in correctly-updated values for sugar" );
    //        Assertions.assertEquals( 0, (int) ivt.getChocolate(),
    //                "Setting a value to the inventory should result in correctly-updated values for chocolate" );
    //
    //        Assertions.assertFalse( ivt.enoughIngredients( recipe ) );
    //    }
    //
    //    // Written by jqdo
    //    @Test
    //    @Transactional
    //    public void testCheckChocolate () {
    //        Inventory ivt = inventoryService.getInventory();
    //        ivt.setChocolate( 5 );
    //
    //        inventoryService.save( ivt );
    //
    //        ivt = inventoryService.getInventory();
    //
    //        Assertions.assertEquals( 5, (int) ivt.getChocolate(),
    //                "Setting a value to the inventory should result in correctly-updated values for chocolate" );
    //
    //        ivt.checkChocolate( "50" );
    //        inventoryService.save( ivt );
    //
    //        ivt = inventoryService.getInventory();
    //        Assertions.assertEquals( 50, ivt.checkChocolate( "50" ),
    //                "Checking for a string amount should correctly parse to be the same amount" );
    //
    //        try {
    //            ivt.checkChocolate( "-50" );
    //        }
    //        catch ( final IllegalArgumentException iae ) {
    //            Assertions.assertEquals( 5, (int) ivt.getChocolate(),
    //                    "Setting a value to the inventory should result in correctly-updated values for chocolate" );
    //        }
    //    }
    //
    //    // Written by jqdo
    //    @Test
    //    @Transactional
    //    public void testToString () {
    //        Inventory ivt = inventoryService.getInventory();
    //        ivt.addIngredients( 5, 3, 7, 2 );
    //        inventoryService.save( ivt );
    //
    //        ivt = inventoryService.getInventory();
    //        Assertions.assertEquals( "Coffee: 505\nMilk: 503\nSugar: 507\nChocolate: 502\n", ivt.toString() );
    //    }

}

