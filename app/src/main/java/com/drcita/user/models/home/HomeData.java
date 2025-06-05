package com.drcita.user.models.home;

import com.drcita.user.models.ads.AdResponse;
import com.drcita.user.models.dashboard.specilization.Specialization;

import java.util.List;

public class HomeData {

    private List<Specialization> specializations;
    private List<AdResponse.Ad> ads;
    private List<City> cities;
    private List<Providers> providers;

    public List<Specialization> getSpecializations() {
        return specializations;
    }

    public void setSpecializations(List<Specialization> specializations) {
        this.specializations = specializations;
    }

    public List<AdResponse.Ad> getAds() {
        return ads;
    }

    public void setAds(List<AdResponse.Ad> ads) {
        this.ads = ads;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    public List<Providers> getProviders() {
        return providers;
    }

    public void setProviders(List<Providers> providers) {
        this.providers = providers;
    }

    @Override
    public String toString() {
        return "HomeData{" +
                "specializations=" + specializations +
                ", ads=" + ads +
                ", cities=" + cities +
                ", providers=" + providers +
                '}';
    }
}
