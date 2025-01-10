package com.springboot.app.search;

public class SortSpec {
    public final String field;
    public final Direction dir;
    public SortSpec(String field, Direction dir) {
        this.dir = dir;
        this.field = field;
    }

    public enum Direction {
        ASC, DESC
    }
}
