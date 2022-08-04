package hs.example.entities;

import java.util.Date;

public class S3ObjectEntity {
	private String fileName;
	private String path;
	private String modifiedDate;
	private double size;
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public double getSize() {
		return size;
	}
	public void setSize(double size) {
		this.size = size;
	}
	@Override
	public String toString() {
		return "S3ObjectEntity [fileName=" + fileName + ", path=" + path + ", modifiedDate=" + modifiedDate + ", size="
				+ size + "]";
	}
	
	
	
}
