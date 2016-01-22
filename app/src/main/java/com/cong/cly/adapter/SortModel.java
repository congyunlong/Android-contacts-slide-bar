package com.cong.cly.adapter;

/**
 * Created by cong on 16/1/21.
 */
public class SortModel {
    boolean isLeaf = true;
    public String name;
    public String az;

    public SortModel(String name) {
        this.name = name;
    }

    public SortModel(String az, boolean isLeaf) {
        this.az = az;
        this.isLeaf = isLeaf;
    }
}
