package com.drcita.user.models.medicalrecords;

public class PhotosItem{
	private int photoId;
	private String recordPhoto;

	public void setPhotoId(int photoId){
		this.photoId = photoId;
	}

	public int getPhotoId(){
		return photoId;
	}

	public void setRecordPhoto(String recordPhoto){
		this.recordPhoto = recordPhoto;
	}

	public String getRecordPhoto(){
		return recordPhoto;
	}
}
