
package com.drcita.user.models.login;

import com.google.gson.annotations.Expose;

import org.parceler.Parcel;

@Parcel
public class Data {

    @Expose
    private String name;
    @Expose
    private String userId;
    @Expose
    private String mobile;
    @Expose
    private int profile_status;
    @Expose
    private int region;
    @Expose
    private int language;

    public int getRegion() {
        return region;
    }

    public void setRegion(int region) {
        this.region = region;
    }

    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getProfile_status() {
        return profile_status;
    }

    public void setProfile_status(int profile_status) {
        this.profile_status = profile_status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String  getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
