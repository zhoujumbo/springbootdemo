package com.pattern.prototype.simple;

/**
 * Created by Tom.
 */
public class Client {

    public Prototype startClone(Prototype concretePrototype){
        return (Prototype)concretePrototype.clone();
    }

}
