package com.drcita.user.models.departments;

public class Department {
    private int id;
    private String image;
    private String departmentName;
    private int position;

    // Getters
    public int getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public int getPosition() {
        return position;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
