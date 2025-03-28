package com.drcita.user.models.contact;

public class ContactRequest{
	private String subject;
	private String description;
	private int userId;

	public void setSubject(String subject){
		this.subject = subject;
	}

	public String getSubject(){
		return subject;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setUserId(int userId){
		this.userId = userId;
	}

	public int getUserId(){
		return userId;
	}

	@Override
 	public String toString(){
		return 
			"ContactRequest{" + 
			"subject = '" + subject + '\'' + 
			",description = '" + description + '\'' + 
			",userId = '" + userId + '\'' + 
			"}";
		}
}
