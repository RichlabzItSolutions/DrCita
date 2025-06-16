package com.drcita.user.models.userprofile;

import com.drcita.user.models.profile.Disease;
import com.drcita.user.models.profile.Profile;

import java.util.List;

public class ProfileDiseaseData {
    private MemberProfile profile;
    private List<Disease> diseases;

    public MemberProfile getProfile() { return profile; }
    public List<Disease> getDiseases() { return diseases; }
}
