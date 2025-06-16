package com.drcita.user.models.specilization;

import java.util.List;

public class DoctorSearchRequest {
    private int cityId;
    private String searchStr;
    private List<Integer> hospitalId;
    private List<Integer>  doctorId;
    private List<Integer> specializationId;
    private List<Integer> consultationMode;
    private String area;
    private String gender;
    private List<Integer> languageIds;
    private Experience experience;
    private String availability;
    private String sort;

    public List<Integer> getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(List<Integer> doctorId) {
        this.doctorId = doctorId;
    }

    // Constructor


    public DoctorSearchRequest(int cityId, String searchStr, List<Integer> hospitalId,
                               List<Integer> doctorId, List<Integer> specializationId,
                               List<Integer> consultationMode, String area, String gender,
                               List<Integer> languageIds,
                               Experience experience, String availability, String sort) {
        this.cityId = cityId;
        this.searchStr = searchStr;
        this.hospitalId = hospitalId;
        this.doctorId = doctorId;
        this.specializationId = specializationId;
        this.consultationMode = consultationMode;
        this.area = area;
        this.gender = gender;
        this.languageIds = languageIds;
        this.experience = experience;
        this.availability = availability;
        this.sort = sort;
    }

    // Empty constructor
    public DoctorSearchRequest() {}

    // Getters and Setters
    public int getCityId() { return cityId; }
    public void setCityId(int cityId) { this.cityId = cityId; }

    public String getSearchStr() { return searchStr; }
    public void setSearchStr(String searchStr) { this.searchStr = searchStr; }

    public List<Integer> getHospitalId() { return hospitalId; }
    public void setHospitalId(List<Integer> hospitalId) { this.hospitalId = hospitalId; }

    public List<Integer> getSpecializationId() { return specializationId; }
    public void setSpecializationId(List<Integer> specializationId) { this.specializationId = specializationId; }

    public List<Integer> getConsultationMode() { return consultationMode; }
    public void setConsultationMode(List<Integer> consultationMode) { this.consultationMode = consultationMode; }

    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public List<Integer> getLanguageIds() { return languageIds; }
    public void setLanguageIds(List<Integer> languageIds) { this.languageIds = languageIds; }

    public Experience getExperience() { return experience; }
    public void setExperience(Experience experience) { this.experience = experience; }

    public String getAvailability() { return availability; }
    public void setAvailability(String availability) { this.availability = availability; }

    public String getSort() { return sort; }
    public void setSort(String sort) { this.sort = sort; }

    // Nested class for experience
    public static class Experience {
        private String min;
        private String max;

        public Experience() {}

        public Experience(String min, String max) {
            this.min = min;
            this.max = max;
        }

        public String getMin() { return min; }
        public void setMin(String min) { this.min = min; }

        public String getMax() { return max; }
        public void setMax(String max) { this.max = max; }
    }
}

