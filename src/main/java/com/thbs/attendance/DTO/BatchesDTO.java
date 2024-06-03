package com.thbs.attendance.DTO;

public class BatchesDTO {
	
    private long batchId;
    private String batchName;
	
    public BatchesDTO(long batchId, String batchName) {
		super();
		this.batchId = batchId;
		this.batchName = batchName;
	}
    
	public BatchesDTO() {
		super();
	}
	
	public long getBatchId() {
		return batchId;
	}
	
	public void setBatchId(long batchId) {
		this.batchId = batchId;
	}
	
	public String getBatchName() {
		return batchName;
	}
	
	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}
	
	
}
