package com.drcita.user.models.home;

public class SectionItem {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ITEM = 1;

    private int type;
    private String title;
    private String name;
    private int id;
    private boolean isPlaceholder;
    private String sectionType; // "hospital", "doctor", "specialist"

    // Constructor for header
    public SectionItem(int type, String title) {
        this.type = type;
        this.title = title;
    }

    // Constructor for item with ID and sectionType
    public SectionItem(int type, String name, int id, String sectionType) {
        this.type = type;
        this.name = name;
        this.id = id;
        this.sectionType = sectionType;
        this.isPlaceholder = false;
    }

    // Constructor for "No data" placeholder
    public SectionItem(int type, String name, boolean isPlaceholder) {
        this.type = type;
        this.name = name;
        this.isPlaceholder = isPlaceholder;
    }

    public int getType() { return type; }
    public String getTitle() { return title; }
    public String getName() { return name; }
    public int getId() { return id; }
    public String getSectionType() { return sectionType; }
    public boolean isPlaceholder() { return isPlaceholder; }
}
