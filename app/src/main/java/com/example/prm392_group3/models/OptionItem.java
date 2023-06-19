package com.example.prm392_group3.models;

public class OptionItem {
    private int startIconResId;
    private String description;
    private Integer endIconResId;

    public OptionItem(int startIconResId, String description, Integer endIconResId) {
        this.startIconResId = startIconResId;
        this.description = description;
        this.endIconResId = endIconResId;
    }

    public int getStartIconResId() {
        return startIconResId;
    }

    public String getDescription() {
        return description;
    }

    public Integer getEndIconResId() {
        return endIconResId;
    }
}
