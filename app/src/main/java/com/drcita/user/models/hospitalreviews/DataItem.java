package com.drcita.user.models.hospitalreviews;

public class DataItem{
	private String comments;
	private String reviewDate;
	private int id;
	private String userName;
	private int rating;


	public void setComments(String comments) {
		this.comments = comments;
	}

	public void setReviewDate(String reviewDate) {
		this.reviewDate = reviewDate;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getComments(){
		return comments;
	}

	public String getReviewDate(){
		return reviewDate;
	}

	public int getId(){
		return id;
	}

	public String getUserName(){
		return userName;
	}
}
