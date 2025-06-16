package com.drcita.user.models.profile;

import java.util.List;

public class ProfileData {
    private Profile profile;
    private List<Disease> diseases;

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public List<Disease> getDiseases() {
        return diseases;
    }

    public void setDiseases(List<Disease> diseases) {
        this.diseases = diseases;
    }
}
