package com.github.andy.elva.model.renderer;

import java.util.LinkedList;

public class Accumulative {
    Integer accumulator;

    public Accumulative(int firstValue) {
        accumulator = firstValue;
    }

    public double next(int val) {
        accumulator = accumulator + val;
        return  accumulator;
    }
}
