package com.drcita.user.models.language;

public class UpdateLanguageRequest {

    private int userId;
    private int language;

    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public int getUserId(){
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
