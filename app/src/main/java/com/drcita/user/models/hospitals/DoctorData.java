package com.drcita.user.models.hospitals;

import com.drcita.user.models.specilization.ProviderModel;

import java.util.List;

public class DoctorData {
    public int doctorId;
    public String picture;
    public String doctorName;
    public int experience;
    public double rating;
    public String gender;
    public List<String> specializations;
    public List<String> languages;
    public List<String> qualifications;
    public int providerCount;
    public List<ProviderModel> providers;
}

