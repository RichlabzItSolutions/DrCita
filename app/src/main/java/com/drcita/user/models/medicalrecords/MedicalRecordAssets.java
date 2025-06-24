package com.drcita.user.models.medicalrecords;

import java.util.List;

public class MedicalRecordAssets {
    private List<MedicalAssetFile> images;
    private List<MedicalAssetFile> documents;
    private List<MedicalAssetFile> urls;

    public List<MedicalAssetFile> getImages() {
        return images;
    }

    public List<MedicalAssetFile> getDocuments() {
        return documents;
    }

    public List<MedicalAssetFile> getUrls() {
        return urls;
    }
}
