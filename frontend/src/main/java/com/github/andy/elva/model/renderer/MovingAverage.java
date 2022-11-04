package com.github.andy.elva.model.renderer;

import java.util.LinkedList;

public class MovingAverage {
    private double sum;
    private int size;
    private LinkedList<Integer> list;


    public MovingAverage(int frames) {
        this.list = new LinkedList<>();
        this.size = frames;
    }

    public double next(int val) {
        sum += val;
        list.offer(val);
        if (list.size() <= size) {
            return sum / list.size();
        }
        sum -= list.poll();
        return sum / size;
    }
}
