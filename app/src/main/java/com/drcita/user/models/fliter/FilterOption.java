package com.drcita.user.models.fliter;



public class FilterOption {
    private String id;       // Optional - use if coming from API
    private String label;
    private boolean selected;
    private String min;
    private String max;

    public FilterOption(String id, String label, boolean selected) {
        this.id = id;
        this.label = label;
        this.selected = selected;
    }

    public FilterOption(String label, boolean selected) {
        this.label = label;
        this.selected = selected;
    }
    public String getMin() { return min; }
    public void setMin(String min) { this.min = min; }

    public String getMax() { return max; }
    public void setMax(String max) { this.max = max; }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
