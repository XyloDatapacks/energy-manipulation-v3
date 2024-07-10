package com.xylo_datapacks.energy_manipulation.api;

import com.sun.jdi.IntegerValue;

public class Counter {
    private int value = 0;
    
    public int getValue() {
        return value;
    }
    
    public Counter increment() {
        value++;
        return this;
    }
}
