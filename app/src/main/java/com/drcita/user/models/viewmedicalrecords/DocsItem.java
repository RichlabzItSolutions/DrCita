package com.drcita.user.models.viewmedicalrecords;

public class DocsItem{
	private String recordDoc;
	private int docId;

	public void setRecordDoc(String recordDoc){
		this.recordDoc = recordDoc;
	}

	public String getRecordDoc(){
		return recordDoc;
	}

	public void setDocId(int docId){
		this.docId = docId;
	}

	public int getDocId(){
		return docId;
	}
}
