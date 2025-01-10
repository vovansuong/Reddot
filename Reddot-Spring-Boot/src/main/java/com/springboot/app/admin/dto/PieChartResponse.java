package com.springboot.app.admin.dto;

public class PieChartResponse {
    private String name; // forum group name"
    private Long value; // number of discussions

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }
}
