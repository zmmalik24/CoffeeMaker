/**
 *
 */
package edu.ncsu.csc.CoffeeMaker.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 *
 */
@Entity
public class Ingredient extends DomainObject {

    @Id
    @GeneratedValue
    private long           id;

    private String name;

    private int            amount;

    public Ingredient ( final String name, final int amount ) {
        super();
        setName( name );
        setAmount( amount );
    }

    public Ingredient () {

    }

    public String getName () {
        return name;
    }

    public void setName ( final String name ) {
        this.name = name;
    }

    public int getAmount () {
        return amount;
    }

    public void setAmount ( final int amount ) {
        if ( amount < 0 ) {
            throw new IllegalArgumentException( "Amount cannot be negative." );
        }
        this.amount = amount;
    }

    public void setId ( final long id ) {
        this.id = id;
    }

    @Override
    public String toString () {
        return "Ingredient [id=" + id + ", ingredient=" + name + ", amount=" + amount + "]";
    }

    @Override
    public Long getId () {
        return id;
    }

}
