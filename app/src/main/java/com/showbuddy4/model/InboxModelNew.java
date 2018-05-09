package com.showbuddy4.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Admin on 3/10/2018.
 */

public class InboxModelNew {
    @Override
    public String toString() {
        return "InboxModelNew{" +
                "id='" + id + '\'' +
                ", FbProfileId='" + FbProfileId + '\'' +
                ", FbUserPhotoUrl1='" + FbUserPhotoUrl1 + '\'' +
                ", FbUserPhotoUrl2='" + FbUserPhotoUrl2 + '\'' +
                ", FbUserPhotoUrl3='" + FbUserPhotoUrl3 + '\'' +
                ", FbUserPhotoUrl4='" + FbUserPhotoUrl4 + '\'' +
                ", FbUserPhotoUrl5='" + FbUserPhotoUrl5 + '\'' +
                ", FbUserPhotoUrl6='" + FbUserPhotoUrl6 + '\'' +
                ", ProfileName='" + ProfileName + '\'' +
                ", UserEmail='" + UserEmail + '\'' +
                ", UserAddress='" + UserAddress + '\'' +
                ", UserDob='" + UserDob + '\'' +
                ", FirstName='" + FirstName + '\'' +
                ", LastName='" + LastName + '\'' +
                ", CollegeName='" + CollegeName + '\'' +
                ", Gender='" + Gender + '\'' +
                ", About='" + About + '\'' +
                ", createdDate='" + createdDate + '\'' +
                ", Age='" + Age + '\'' +
                ", Profession='" + Profession + '\'' +
                ", Location='" + Location + '\'' +
                ", qbuserid='" + qbuserid + '\'' +
                ", qbuserlogin='" + qbuserlogin + '\'' +
                ", qbpass='" + qbpass + '\'' +
                ", userActionBy='" + userActionBy + '\'' +
                ", userActionTo='" + userActionTo + '\'' +
                ", action='" + action + '\'' +
                ", actionDate='" + actionDate + '\'' +
                '}';
    }

    /**
     * id : 93
     * FbProfileId : 1764260830315704
     * FbUserPhotoUrl1 : https://scontent.xx.fbcdn.net/v/t1.0-1/p200x200/18921707_1527697193972070_5227115040866276025_n.jpg?oh=766f34d45497994c17b467ec4cac43e7&oe=5AF8AC21
     * FbUserPhotoUrl2 :
     * FbUserPhotoUrl3 :
     * FbUserPhotoUrl4 :
     * FbUserPhotoUrl5 : http://demo.workdesirewebsolutions.com/showbuddy/upload/1764260830315704850911.png
     * FbUserPhotoUrl6 : http://demo.workdesirewebsolutions.com/showbuddy/upload/1764260830315704946560.png
     * ProfileName :
     * UserEmail : mahesh2592@gmail.com
     * UserAddress :
     * UserDob :
     * FirstName : Mahesh
     * LastName : Prajapati
     * CollegeName : Gujarat University
     * Gender : male
     * About : Experience iOS Developer with excellent knowledge of swift & Objective-C
     * created_date : 2018-01-17 10:53:04
     * Age : 0
     * Profession : SR. iOS Developer
     * Location : 23.050049,72.5089523
     * qbuserid : 44768582
     * qbuserlogin : 1764260830315704
     * qbpass : showbuddy@123
     * user_action_by : 1764260830315704
     * user_action_to : 1539084196156827
     * action : like
     * action_date : 2018-02-20 12:23:30
     */

    @SerializedName("id")
    private String id;
    @SerializedName("FbProfileId")
    private String FbProfileId;
    @SerializedName("FbUserPhotoUrl1")
    private String FbUserPhotoUrl1;
    @SerializedName("FbUserPhotoUrl2")
    private String FbUserPhotoUrl2;
    @SerializedName("FbUserPhotoUrl3")
    private String FbUserPhotoUrl3;
    @SerializedName("FbUserPhotoUrl4")
    private String FbUserPhotoUrl4;
    @SerializedName("FbUserPhotoUrl5")
    private String FbUserPhotoUrl5;
    @SerializedName("FbUserPhotoUrl6")
    private String FbUserPhotoUrl6;
    @SerializedName("ProfileName")
    private String ProfileName;
    @SerializedName("UserEmail")
    private String UserEmail;
    @SerializedName("UserAddress")
    private String UserAddress;
    @SerializedName("UserDob")
    private String UserDob;
    @SerializedName("FirstName")
    private String FirstName;
    @SerializedName("LastName")
    private String LastName;
    @SerializedName("CollegeName")
    private String CollegeName;
    @SerializedName("Gender")
    private String Gender;
    @SerializedName("About")
    private String About;
    @SerializedName("created_date")
    private String createdDate;
    @SerializedName("Age")
    private String Age;
    @SerializedName("Profession")
    private String Profession;
    @SerializedName("Location")
    private String Location;
    @SerializedName("qbuserid")
    private String qbuserid;
    @SerializedName("qbuserlogin")
    private String qbuserlogin;
    @SerializedName("qbpass")
    private String qbpass;
    @SerializedName("user_action_by")
    private String userActionBy;
    @SerializedName("user_action_to")
    private String userActionTo;
    @SerializedName("action")
    private String action;
    @SerializedName("action_date")
    private String actionDate;
    @SerializedName("chatwith")
    private String chatwith;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFbProfileId() {
        return FbProfileId;
    }

    public void setFbProfileId(String FbProfileId) {
        this.FbProfileId = FbProfileId;
    }

    public String getFbUserPhotoUrl1() {
        return FbUserPhotoUrl1;
    }

    public void setFbUserPhotoUrl1(String FbUserPhotoUrl1) {
        this.FbUserPhotoUrl1 = FbUserPhotoUrl1;
    }

    public String getFbUserPhotoUrl2() {
        return FbUserPhotoUrl2;
    }

    public void setFbUserPhotoUrl2(String FbUserPhotoUrl2) {
        this.FbUserPhotoUrl2 = FbUserPhotoUrl2;
    }

    public String getFbUserPhotoUrl3() {
        return FbUserPhotoUrl3;
    }

    public void setFbUserPhotoUrl3(String FbUserPhotoUrl3) {
        this.FbUserPhotoUrl3 = FbUserPhotoUrl3;
    }

    public String getFbUserPhotoUrl4() {
        return FbUserPhotoUrl4;
    }

    public void setFbUserPhotoUrl4(String FbUserPhotoUrl4) {
        this.FbUserPhotoUrl4 = FbUserPhotoUrl4;
    }

    public String getFbUserPhotoUrl5() {
        return FbUserPhotoUrl5;
    }

    public void setFbUserPhotoUrl5(String FbUserPhotoUrl5) {
        this.FbUserPhotoUrl5 = FbUserPhotoUrl5;
    }

    public String getFbUserPhotoUrl6() {
        return FbUserPhotoUrl6;
    }

    public void setFbUserPhotoUrl6(String FbUserPhotoUrl6) {
        this.FbUserPhotoUrl6 = FbUserPhotoUrl6;
    }

    public String getProfileName() {
        return ProfileName;
    }

    public void setProfileName(String ProfileName) {
        this.ProfileName = ProfileName;
    }

    public String getUserEmail() {
        return UserEmail;
    }

    public void setUserEmail(String UserEmail) {
        this.UserEmail = UserEmail;
    }

    public String getUserAddress() {
        return UserAddress;
    }

    public void setUserAddress(String UserAddress) {
        this.UserAddress = UserAddress;
    }

    public String getUserDob() {
        return UserDob;
    }

    public void setUserDob(String UserDob) {
        this.UserDob = UserDob;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String FirstName) {
        this.FirstName = FirstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String LastName) {
        this.LastName = LastName;
    }

    public String getCollegeName() {
        return CollegeName;
    }

    public void setCollegeName(String CollegeName) {
        this.CollegeName = CollegeName;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String Gender) {
        this.Gender = Gender;
    }

    public String getAbout() {
        return About;
    }

    public void setAbout(String About) {
        this.About = About;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String Age) {
        this.Age = Age;
    }

    public String getProfession() {
        return Profession;
    }

    public void setProfession(String Profession) {
        this.Profession = Profession;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String Location) {
        this.Location = Location;
    }

    public String getQbuserid() {
        return qbuserid;
    }

    public void setQbuserid(String qbuserid) {
        this.qbuserid = qbuserid;
    }

    public String getQbuserlogin() {
        return qbuserlogin;
    }

    public void setQbuserlogin(String qbuserlogin) {
        this.qbuserlogin = qbuserlogin;
    }

    public String getQbpass() {
        return qbpass;
    }

    public void setQbpass(String qbpass) {
        this.qbpass = qbpass;
    }

    public String getUserActionBy() {
        return userActionBy;
    }

    public void setUserActionBy(String userActionBy) {
        this.userActionBy = userActionBy;
    }

    public String getUserActionTo() {
        return userActionTo;
    }

    public void setUserActionTo(String userActionTo) {
        this.userActionTo = userActionTo;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getActionDate() {
        return actionDate;
    }

    public void setActionDate(String actionDate) {
        this.actionDate = actionDate;
    }

    public String getChatwith() {
        return chatwith;
    }

    public void setChatwith(String chatwith) {
        this.chatwith = chatwith;
    }
}
