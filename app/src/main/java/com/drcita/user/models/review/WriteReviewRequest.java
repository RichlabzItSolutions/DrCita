package com.drcita.user.models.review;

public class WriteReviewRequest{
	private String comments;
	private int providerId;
	private int rating;
	private int userId;

	public String getComments(){
		return comments;
	}

	public int getProviderId(){
		return providerId;
	}

	public int getRating(){
		return rating;
	}

	public int getUserId(){
		return userId;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public void setProviderId(int providerId) {
		this.providerId = providerId;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
}
