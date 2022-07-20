package com.example.doctorscarespringbootapplication.dto;

public class EditUserDTO {
    private String pageNo;
    private String userId;

    public EditUserDTO(String pageNo, String userId) {
        this.pageNo = pageNo;
        this.userId = userId;
    }

    public EditUserDTO() {
    }

    public String getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
