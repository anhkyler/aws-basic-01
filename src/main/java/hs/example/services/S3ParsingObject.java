package hs.example.services;

import java.util.Date;
import java.util.List;

public class S3ParsingObject {
	private int id;
	private String filePath;
	private List<String> parsePath;
	public List<String> getParsePath() {
		return parsePath;
	}
	public void setParsePath(List<String> parsePath) {
		this.parsePath = parsePath;
	}
	private Date dateModified;
	private double size;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
	
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public Date getDateModified() {
		return dateModified;
	}
	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}
	public double getSize() {
		return size;
	}
	public void setSize(double size) {
		this.size = size;
	}
	@Override
	public String toString() {
		return "S3ParsingObject [id=" + id + ", filePath=" + filePath + ", parsePath=" + parsePath + ", dateModified="
				+ dateModified + ", size=" + size + "]";
	}
	
		
}
