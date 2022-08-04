package hs.example.entities;


public class S3ObjectEntity {
	private String fileName;
	private String path;
	private String modifiedDate;
	private double size;
	private String key;
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
	
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	@Override
	public String toString() {
		return "S3ObjectEntity [fileName=" + fileName + ", path=" + path + ", modifiedDate=" + modifiedDate + ", size="
				+ size + "]";
	}
	
	
	
}
