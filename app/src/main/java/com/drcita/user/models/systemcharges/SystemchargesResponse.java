package com.drcita.user.models.systemcharges;

public class SystemchargesResponse{
	private int sLSH;
	private String message;
	private String status;
	private int dollar;

	public void setSLSH(int sLSH){
		this.sLSH = sLSH;
	}

	public int getSLSH(){
		return sLSH;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	public void setDollar(int dollar){
		this.dollar = dollar;
	}

	public int getDollar(){
		return dollar;
	}

	@Override
 	public String toString(){
		return 
			"SystemchargesResponse{" + 
			"sLSH = '" + sLSH + '\'' + 
			",message = '" + message + '\'' + 
			",status = '" + status + '\'' + 
			",dollar = '" + dollar + '\'' + 
			"}";
		}
}
