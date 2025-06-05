package com.drcita.user.models.home;

public class Providers {
    private int id;
    private String image;
    private String providerName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    @Override
    public String toString() {
        return "Providers{" +
                "id=" + id +
                ", image='" + image + '\'' +
                ", providerName='" + providerName + '\'' +
                '}';
    }
}
