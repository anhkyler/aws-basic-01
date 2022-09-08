package com.example.hs;

public class MappingObject {
	 public String contentlength;
     public String storageclass;
     public String lastmodified;
     public String objectkey;
	public MappingObject(String contentlength, String storageclass, String lastmodified, String objectkey) {
		super();
		this.contentlength = contentlength;
		this.storageclass = storageclass;
		this.lastmodified = lastmodified;
		this.objectkey = objectkey;
	}
	public String getContentlength() {
		return contentlength;
	}
	public void setContentlength(String contentlength) {
		this.contentlength = contentlength;
	}
	public String getStorageclass() {
		return storageclass;
	}
	public void setStorageclass(String storageclass) {
		this.storageclass = storageclass;
	}
	public String getLastmodified() {
		return lastmodified;
	}
	public void setLastmodified(String lastmodified) {
		this.lastmodified = lastmodified;
	}
	public String getObjectkey() {
		return objectkey;
	}
	public void setObjectkey(String objectkey) {
		this.objectkey = objectkey;
	}
	@Override
	public String toString() {
		return "MappingObject [contentlength=" + contentlength + ", storageclass=" + storageclass + ", lastmodified="
				+ lastmodified + ", objectkey=" + objectkey + "]";
	}
     
     
}
