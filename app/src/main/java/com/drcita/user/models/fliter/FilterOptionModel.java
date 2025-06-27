package com.drcita.user.models.fliter;


public class FilterOptionModel {
    private int id;
    private String name;
    private String iconUrl; // Optional, if needed
    private boolean selected;

    public FilterOptionModel(int id, String name) {
        this.id = id;
        this.name = name;
        this.selected = false;
    }

    public FilterOptionModel(int id, String name, String iconUrl) {
        this.id = id;
        this.name = name;
        this.iconUrl = iconUrl;
        this.selected = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
}
