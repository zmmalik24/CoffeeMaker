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
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

/**
 * This is the controller that holds the REST endpoints that handle CRUD
 * operations for Recipes.
 *
 * Spring will automatically convert all of the ResponseEntity and List results
 * to JSON
 *
 * @author Kai Presler-Marshall
 * @author Michelle Lemons
 *
 */
@SuppressWarnings ( { "unchecked", "rawtypes" } )
@RestController
public class APIRecipeController extends APIController {

    /**
     * RecipeService object, to be autowired in by Spring to allow for
     * manipulating the Recipe model
     */
    @Autowired
    private RecipeService service;
    
    /**
     * RecipeService object, to be autowired in by Spring to allow for
     * manipulating the Recipe model
     */
    @Autowired
    private IngredientService iService;

    /**
     * REST API method to provide GET access to all recipes in the system
     *
     * @return JSON representation of all recipies
     */
    @GetMapping ( BASE_PATH + "/recipes" )
    public List<Recipe> getRecipes () {
        return service.findAll();
    }

    /**
     * REST API method to provide GET access to a specific recipe, as indicated
     * by the path variable provided (the name of the recipe desired)
     *
     * @param name
     *            recipe name
     * @return response to the request
     */
    @GetMapping ( BASE_PATH + "/recipes/{name}" )
    public ResponseEntity getRecipe ( @PathVariable ( "name" ) final String name ) {
        final Recipe recipe = service.findByName( name );
        return null == recipe
                ? new ResponseEntity( errorResponse( "No recipe found with name " + name ), HttpStatus.NOT_FOUND )
                : new ResponseEntity( recipe, HttpStatus.OK );
    }
    
    /**
     * REST API method to provide GET access to a specific recipe, as indicated
     * by the path variable provided (the name of the recipe desired)
     *
     * @param name
     *            recipe name
     * @return response to the request
     */
    @GetMapping ( BASE_PATH + "/recipes/{name}/ingredients" )
    public List<Ingredient> getRecipeIngredients ( @PathVariable ( "name" ) final String name ) {
        final Recipe recipe = service.findByName( name );
        return recipe.getIngredients();
    }

    /**
     * REST API method to provide POST access to the Recipe model. This is used
     * to create a new Recipe by automatically converting the JSON RequestBody
     * provided to a Recipe object. Invalid JSON will fail.
     *
     * @param recipe
     *            The valid Recipe to be saved.
     * @return ResponseEntity indicating success if the Recipe could be saved to
     *         the inventory, or an error if it could not be
     */
    @PostMapping ( BASE_PATH + "/recipes" )
    public ResponseEntity createRecipe ( @RequestBody final Recipe recipe ) {
        if ( null != service.findByName( recipe.getName() ) ) {
            return new ResponseEntity( errorResponse( "Recipe with the name " + recipe.getName() + " already exists" ),
                    HttpStatus.CONFLICT );
        }
        if ( service.findAll().size() < 3 ) {
            service.save( recipe );
            return new ResponseEntity( successResponse( recipe.getName() + " successfully created" ), HttpStatus.OK );
        }
        else {
            return new ResponseEntity(
                    errorResponse( "Insufficient space in recipe book for recipe " + recipe.getName() ),
                    HttpStatus.INSUFFICIENT_STORAGE );
        }

    }

    /**
     * REST API method to allow deleting a Recipe from the CoffeeMaker's
     * Inventory, by making a DELETE request to the API endpoint and indicating
     * the recipe to delete (as a path variable)
     *
     * @param name
     *            The name of the Recipe to delete
     * @return Success if the recipe could be deleted; an error if the recipe
     *         does not exist
     */
    @DeleteMapping ( BASE_PATH + "/recipes/{name}" )
    public ResponseEntity deleteRecipe ( @PathVariable final String name ) {
        final Recipe recipe = service.findByName( name );
        if ( null == recipe ) {
            return new ResponseEntity( errorResponse( "No recipe found for name " + name ), HttpStatus.NOT_FOUND );
        }
        service.delete( recipe );

        return new ResponseEntity( successResponse( name + " was deleted successfully" ), HttpStatus.OK );
    }
    
    /**
     * REST API method to provide POST access to the Recipe model. This is used
     * to create a new Recipe by automatically converting the JSON RequestBody
     * provided to a Recipe object. Invalid JSON will fail.
     *
     * @param recipe
     *            The valid Recipe to be saved.
     * @return ResponseEntity indicating success if the Recipe could be saved to
     *         the inventory, or an error if it could not be
     */
    @PutMapping ( BASE_PATH + "/recipes/{name}/{ingName}/{amount}" )
    public ResponseEntity addRecipeIngredient ( @PathVariable final String name, @PathVariable final String ingName, @PathVariable final int amount) {
        final Recipe recipe = service.findByName(name);
        
        if (amount < 0) {
        	return new ResponseEntity( errorResponse( "Amount is not valid" ), HttpStatus.NOT_FOUND );
        }
        
        Ingredient newIngredient = new Ingredient(ingName, amount);
        
        iService.save(newIngredient);
        
    	recipe.addIngredient(newIngredient);
    	service.save(recipe); 
    	
    	return new ResponseEntity( successResponse( "Ingredient was added successfully " + " to " + name), HttpStatus.OK );
    }
    
    /**
     * REST API method to allow deleting an ingredient from a Recipe from the CoffeeMaker's
     * Inventory, by making a DELETE request to the API endpoint and indicating
     * the ingredient to delete (as a path variable)
     *
     * @param name
     *            The name of the Recipe
     * @param ingredient 
     * 			  The name of the ingredient to be deleted
     * @return Success if the recipe could be deleted; an error if the recipe
     *         does not exist
     */
    @DeleteMapping ( BASE_PATH + "/recipes/{name}/{ingredient}" )
    public ResponseEntity deleteRecipeIngredient (@PathVariable final String name, @PathVariable final String ingredient) {
        final Recipe recipe = service.findByName( name );
        if ( null == recipe ) {
            return new ResponseEntity( errorResponse( "No recipe found for name " + name ), HttpStatus.NOT_FOUND );
        }
        
        final List<Ingredient> ingredientList = recipe.getIngredients(); 
        
        Ingredient finalIngredient = null;
        for (Ingredient i: ingredientList) {
        	if (i.getName().equals(ingredient)) {
        		finalIngredient = i;
        		break;
        	}
        }
        
        recipe.removeIngredient(finalIngredient);
        service.save(recipe);
        

        return new ResponseEntity( successResponse( "Ingredient " + " was deleted successfully " + " from " + name), HttpStatus.OK );
    }
    
    /**
     * REST API method to allow editing the amount of an ingredient from a Recipe from the CoffeeMaker's
     * Inventory, by making a PUT request to the API endpoint and indicating
     * the ingredient to edit amount (as a path variable)
     *
     * @param name
     *            The name of the Recipe
     * @param ingredient 
     * 			  The name of the ingredient to be adjusted
     * @param amount 
     * 			  Representing the new amount of the ingredient
     * @return Success if the recipe could be deleted; an error if the recipe
     *         does not exist
     */
    @PostMapping ( BASE_PATH + "/recipes/{name}/{ingredient}/{amount}" )
    public ResponseEntity editRecipeIngredientAmount (@PathVariable final String name, @PathVariable final int amount,  @PathVariable final String ingredient) {
        final Recipe recipe = service.findByName( name );
        if ( null == recipe ) {
            return new ResponseEntity( errorResponse( "No recipe found for name " + name ), HttpStatus.NOT_FOUND );
        }
        
        if (amount < 0) {
        	return new ResponseEntity( errorResponse( "Amount is not valid" ), HttpStatus.NOT_FOUND );
        }
        
        final List<Ingredient> ingredientList = recipe.getIngredients();
        
        Ingredient finalIngredient = null;
        for (Ingredient i: ingredientList) {
        	if (i.getName().equals(ingredient)) {
        		finalIngredient = i;
        		break;
        	}
        }
        
        recipe.editIngredientAmount(finalIngredient, amount);
        service.save(recipe);
        

        return new ResponseEntity( successResponse( "Ingredient amount " + " was edited successfully " + " from " + name), HttpStatus.OK );
    }
    
    /**
     * REST API method to allow editing the amount of an ingredient from a Recipe from the CoffeeMaker's
     * Inventory, by making a PUT request to the API endpoint and indicating
     * the ingredient to edit amount (as a path variable)
     *
     * @param name
     *            The name of the Recipe
     * @param ingredient 
     * 			  The name of the ingredient to be adjusted
     * @param amount 
     * 			  Representing the new amount of the ingredient
     * @return Success if the recipe could be deleted; an error if the recipe
     *         does not exist
     */
    @PutMapping ( BASE_PATH + "/recipes/{name}/{price}/price" )
    public ResponseEntity editRecipePrice ( @PathVariable final String name, @PathVariable final int price) {
        final Recipe recipe = service.findByName( name );
        if ( null == recipe ) {
            return new ResponseEntity( errorResponse( "No recipe found for name " + name ), HttpStatus.NOT_FOUND );
        }
        recipe.editPrice(price);
        service.save(recipe);
        

        return new ResponseEntity( successResponse( "Recipe " + "name " + "was edited successfully"), HttpStatus.OK );
    }
}

