package edu.gatech.cs2340.gtrational.rational.model;

/**
 * Created by shyamal on 10/2/17.
 */

public class Model {

    private static Model instance = new Model();

    private User user;


    private Model() {

    }

    public static Model getInstance(){
        return instance;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
