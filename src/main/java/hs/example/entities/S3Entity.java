package hs.example.entities;

import java.util.Date;

public class S3Entity {
	String name;
	String key;
	String type;
	double size;
	Date modifiedDate;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public double getSize() {
		return size;
	}
	public void setSize(double size) {
		this.size = size;
	}
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	@Override
	public String toString() {
		return "S3Entity [Name=" + name + ", key=" + key + ", type=" + type + ", size=" + size + ", modifiedDate="
				+ modifiedDate + "]";
	}
	
}
