package edu.ncsu.csc.CoffeeMaker.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * Inventory for the coffee maker. Inventory is tied to the database using
 * Hibernate libraries. See InventoryRepository and InventoryService for the
 * other two pieces used for database support.
 *
 * @author Kai Presler-Marshall
 */
@Entity
public class Inventory extends DomainObject {

    /** id for inventory entry */
    @Id
    @GeneratedValue
    private Long                   id;

    /** list of ingredients */
    @OneToMany ( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    private final List<Ingredient> ingredients;

    /**
     * Empty constructor for Hibernate
     */
    public Inventory () {
        // Intentionally empty so that Hibernate can instantiate
        // Inventory object.

        ingredients = new ArrayList<Ingredient>();
    }

    /**
     * Use this to create inventory with specified amts.
     *
     * @param ingredient
     *            the initial ingredient in inventory
     */
    public Inventory ( final Ingredient ingredient ) {
        ingredients = new ArrayList<Ingredient>();
        ingredients.add( ingredient );
    }

    /**
     * Returns the ID of the entry in the DB
     *
     * @return long
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Set the ID of the Inventory (Used by Hibernate)
     *
     * @param id
     *            the ID
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Returns true if there are enough ingredients to make the beverage.
     *
     * @param r
     *            recipe to check if there are enough ingredients
     * @return true if enough ingredients to make the beverage
     */
    public boolean enoughIngredients ( final Recipe r ) {

        final List<Ingredient> recipeIngredients = r.getIngredients();

        for ( final Ingredient i : ingredients ) {
            for ( final Ingredient j : recipeIngredients ) {
                if ( i.getId() == j.getId() ) {
                    if ( i.getAmount() < j.getAmount() ) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public int getIngredient ( final String ingredient ) {
        for ( final Ingredient i : ingredients ) {
            if ( i.getName() == ingredient ) {
                return i.getAmount();
            }
        }
        return 0;
    }

    public List<Ingredient> getIngredients () {
        return ingredients;
    }

    /**
     * Removes the ingredients used to make the specified recipe. Assumes that
     * the user has checked that there are enough ingredients to make
     *
     * @param r
     *            recipe to make
     * @return true if recipe is made.
     */
    public boolean useIngredients ( final Recipe r ) {

        final List<Ingredient> recipeIngredients = r.getIngredients();

        if ( enoughIngredients( r ) ) {
            for ( final Ingredient i : ingredients ) {
                for ( final Ingredient j : recipeIngredients ) {
                    if ( i.getName() == j.getName() ) {
                        i.setAmount( i.getAmount() - j.getAmount() );
                        break;
                    }
                }
            }
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Adds ingredients to the inventory
     *
     * @param ingredientList
     *            list of ingredients that we want to add to current inventory
     * @return true if successful, false if not
     */
    public boolean addIngredient ( Ingredient ingredient ) {

        for (int i = 0; i < ingredients.size(); i++) {
        	if (ingredients.get(i).getName().equals(ingredient.getName())) {
        		ingredients.remove(i);
        		ingredients.add(ingredient);
        		return true;
        	}
        }
        ingredients.add(ingredient);

        return true;
    }

    /**
     * Adds ingredients to the inventory
     *
     * @param ingredientList
     *            list of ingredients that we want to add to current inventory
     * @return true if successful, false if not
     */
    public boolean updateAmount ( Ingredient ingredient ) {

    	for (int i = 0; i < ingredients.size(); i++) {
        	if (ingredients.get(i).getName().equals(ingredient.getName())) {
        		int origAmount = ingredients.get(i).getAmount();
        		
        		int newAmount = origAmount + ingredient.getAmount();
        		
        		ingredient.setAmount(newAmount);
        		
        		ingredients.remove(i);
        		ingredients.add(ingredient);
        		return true;
        	}
        }

        return false;

    }

    /**
     * Method used to get the ingredients list
     *
     * @return list of ingredients
     */
    public final List<Ingredient> getInventory () {
        return ingredients;
    }

    /**
     * Returns a string describing the current contents of the inventory.
     *
     * @return String
     */
    @Override
    public String toString () {
        final StringBuffer buf = new StringBuffer();
        for ( int i = 0; i < ingredients.size(); i++ ) {
            buf.append( ingredients.get( i ).getName() );
            buf.append( ingredients.get( i ).getAmount() );
            buf.append( "\n" );
        }
        return buf.toString();
    }

}
