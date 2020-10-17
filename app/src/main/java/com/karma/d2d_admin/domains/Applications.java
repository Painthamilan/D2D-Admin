package com.karma.d2d_admin.domains;

public class Applications {
    String Address,ApplicationId,Comments,CourseName,Email,FullName,Language,Mobile,Qualification;

    public Applications() {
    }

    public Applications(String address, String applicationId, String comments, String courseName, String email, String fullName, String language, String mobile, String qualification) {
        Address = address;
        ApplicationId = applicationId;
        Comments = comments;
        CourseName = courseName;
        Email = email;
        FullName = fullName;
        Language = language;
        Mobile = mobile;
        Qualification = qualification;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getApplicationId() {
        return ApplicationId;
    }

    public void setApplicationId(String applicationId) {
        ApplicationId = applicationId;
    }

    public String getComments() {
        return Comments;
    }

    public void setComments(String comments) {
        Comments = comments;
    }

    public String getCourseName() {
        return CourseName;
    }

    public void setCourseName(String courseName) {
        CourseName = courseName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getLanguage() {
        return Language;
    }

    public void setLanguage(String language) {
        Language = language;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getQualification() {
        return Qualification;
    }

    public void setQualification(String qualification) {
        Qualification = qualification;
    }
}
