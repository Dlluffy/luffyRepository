package com.elderlycare.model;

public class ElderPhoto {
    private Long photoId;
    private Long elderId;
    private String photoPath;
    private java.util.Date uploadTime;
    private String description;

    // Getters and Setters
    public Long getPhotoId() {
        return photoId;
    }

    public void setPhotoId(Long photoId) {
        this.photoId = photoId;
    }

    public Long getElderId() {
        return elderId;
    }

    public void setElderId(Long elderId) {
        this.elderId = elderId;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public java.util.Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(java.util.Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
