/**
 *
 */
package edu.ncsu.csc.CoffeeMaker.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

/**
 *
 */
@RestController
public class APIIngredientController extends APIController {

    /**
     * IngredientService object, to be autowired in by Spring to allow for
     * manipulating the Ingredient model
     */
    @Autowired
    private IngredientService service;

    /**
     * InventoryService object, to be autowired in by Spring to allow for
     * manipulating the Inventory model
     */
    @Autowired
    private InventoryService  iService;

    /**
     * RecipeService object, to be autowired in by Spring to allow for
     * manipulating the Recipe model
     */
    @Autowired
    private RecipeService     rService;

    /**
     * REST API method to provide GET access to all ingredients in the system
     *
     * @return JSON representation of all ingredients
     */
    @GetMapping ( BASE_PATH + "/ingredients" )
    public List<Ingredient> getIngredients () {
        return service.findAll();
    }

    /**
     *
     * @param name
     * @return
     */
    @SuppressWarnings ( { "rawtypes", "unchecked" } )
    @GetMapping ( BASE_PATH + "ingredients/{id}" )
    public ResponseEntity getIngredient ( @PathVariable final Long id ) {

        final Ingredient ingr = service.findById( id );

        if ( null == ingr ) {
            return new ResponseEntity( HttpStatus.NOT_FOUND );
        }

        return new ResponseEntity( ingr, HttpStatus.OK );
    }

    /**
     *
     * @param ingredient n
     * @return
     */
    @SuppressWarnings ( "rawtypes" )
    @PostMapping ( BASE_PATH + "/ingredients" )
    public ResponseEntity addIngredient ( @RequestBody final Ingredient ingredient ) {

        final Ingredient db = service.findById( ingredient.getId() );

        if ( null != db ) {
            return new ResponseEntity( HttpStatus.CONFLICT );
        } 

        try {

            Inventory inventory = iService.getInventory();
            List<Ingredient> ingList = inventory.getIngredients();
            
            for (int i = 0; i < ingList.size(); i++) {
            	if (ingList.get(i).getName().equals(ingredient.getName())) {
            		return new ResponseEntity( HttpStatus.BAD_REQUEST );
            	}
            }
            
            ingList.add( ingredient );
            iService.save( inventory );
            return new ResponseEntity( HttpStatus.CREATED );
        }
        catch ( final Exception e ) {
            return new ResponseEntity( HttpStatus.BAD_REQUEST ); // HttpStatus.FORBIDDEN
            // would be OK
            // too.
        }

    }

    /**
     * REST API method to allow deleting a ingredient from the CoffeeMaker's by
     * making a DELETE request to the API endpoint and indicating the ingredient
     * to delete (as a path variable)
     *
     * @param id
     *            The id of the ingredient to delete
     * @return Success if the ingredient could be deleted; an error if the
     *         ingredient does not exist
     */
    @DeleteMapping ( BASE_PATH + "/ingredients/{id}" )
    public ResponseEntity<String> deleteRecipe ( @PathVariable final Long id ) {
        final Ingredient ingredient = service.findById( id );
        if ( null == ingredient ) {
            return new ResponseEntity<String>( errorResponse( "No ingredient found for id " + id ),
                    HttpStatus.NOT_FOUND );
        }
        service.delete( ingredient );

        return new ResponseEntity<String>( successResponse( id + " was deleted successfully" ), HttpStatus.OK );
    }

    /**
     * REST API endpoint to update the Ingredient of the CoffeeMaker by adding
     * amounts from the to the Ingredient.
     *
     * @param ingredient
     *            amounts to add to inventory
     * @return response to the request
     */
    @PutMapping ( BASE_PATH + "ingredients/{id}" )
    public ResponseEntity<Ingredient> updateIngredient ( @PathVariable final Long id,
            @RequestBody final Ingredient ingredient ) {
        final Ingredient currIngredient = service.findById( id );
        if ( null == currIngredient ) {
            return new ResponseEntity<Ingredient>( HttpStatus.NOT_FOUND );
        }
        currIngredient.setAmount( ingredient.getAmount() );
        service.save( currIngredient );
        return new ResponseEntity<Ingredient>( currIngredient, HttpStatus.OK );
    }

    /**
     * Universal API endpoint for retrieving any object within the CoffeeMaker
     *
     * @param object
     *            used to determine what object we want to get
     * @param field
     *            used to determine the field of the object we are checking
     * @param value
     *            used to check the recipe value (name) or ingredient value (id)
     *
     * @return response to the request
     */
    @SuppressWarnings ( { "rawtypes", "unchecked" } )
    @GetMapping ( BASE_PATH + "{object}/{field}/{value}" )
    public ResponseEntity getObject ( @PathVariable final String object, @PathVariable final String field,
            @PathVariable final String value ) {
        try {
            Object obj = null;
            switch ( object ) {
                case "ingredients": // go do something
                    obj = service.findById( Long.parseLong( "value" ) );
                    break;

                case "Recipe": // go do something else
                    obj = rService.findByName( "value" );
                    break;
            }

            if ( null == obj ) {
                return new ResponseEntity( HttpStatus.NOT_FOUND );
            }

            return new ResponseEntity( obj, HttpStatus.OK );

        }
        catch ( final Exception e ) {
            return new ResponseEntity( "Something went wrong with your request :: " + e.getCause(),
                    HttpStatus.BAD_REQUEST );
        }

    }
}
