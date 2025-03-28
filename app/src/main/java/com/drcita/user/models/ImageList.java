package com.drcita.user.models;

public class ImageList {
    private String image;
    private int imageID; // your R.drawable.image

    public ImageList(String image, int imageID) {
        this.image = image;
        this.imageID = imageID;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public String getImage() {
        return image;
    }

    public int getImageID() {
        return imageID;
    }
}
