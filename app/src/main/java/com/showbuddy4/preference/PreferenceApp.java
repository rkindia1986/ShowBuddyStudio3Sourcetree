package com.showbuddy4.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.showbuddy4.R;


/**
 * Created by VR-46 on 1/27/2017.
 */

public class PreferenceApp {

    public static final String FIREBASE_CLOUD_MESSAGING = "fcm";
    public static final String SET_NOTIFY = "set_notify";
    private Context mContext;
    private SharedPreferences prefs;

    public PreferenceApp(Context mContext) {
        this.mContext = mContext;
        prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    public void clear() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }


    public String getFbId() {
        return prefs.getString(mContext.getResources().getString(R.string.pref_fb_id), "");
    }

    public void setFbId(String fbId) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(mContext.getResources().getString(R.string.pref_fb_id), fbId);
        editor.apply();
    }

    public String getEmailId() {
        return prefs.getString(mContext.getResources().getString(R.string.pref_email_id), "");
    }

    public void setemailId(String emailid) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(mContext.getResources().getString(R.string.pref_email_id), emailid);
        editor.apply();
    }

    public String getFirstName() {
        return prefs.getString(mContext.getResources().getString(R.string.pref_first_name), "");
    }

    public void setFirstName(String firstName) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(mContext.getResources().getString(R.string.pref_first_name), firstName);
        editor.apply();
    }

    public String getLastName() {
        return prefs.getString(mContext.getResources().getString(R.string.pref_last_name), "");
    }

    public void setLastName(String lastName) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(mContext.getResources().getString(R.string.pref_last_name), lastName);
        editor.apply();
    }

    public String getGender() {
        return prefs.getString(mContext.getResources().getString(R.string.pref_gender), "");
    }

    public void setGender(String gender) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(mContext.getResources().getString(R.string.pref_gender), gender);
        editor.apply();
    }

    public String getBirthday() {
        return prefs.getString(mContext.getResources().getString(R.string.pref_birthday), "");
    }

    public void setBirthday(String birthday) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(mContext.getResources().getString(R.string.pref_birthday), birthday);
        editor.apply();
    }

    public String getLocation() {
        return prefs.getString(mContext.getResources().getString(R.string.pref_location), "");
    }

    public void setLocation(String location) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(mContext.getResources().getString(R.string.pref_location), location);
        editor.apply();
    }

    public String getProfilePhoto() {
        return prefs.getString(mContext.getResources().getString(R.string.pref_profile_photo), "");
    }

    public void setProfilePhoto(String profilePhoto) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(mContext.getResources().getString(R.string.pref_profile_photo), profilePhoto);
        editor.apply();
    }

    public String getFbProfilePhoto2() {
        return prefs.getString(mContext.getResources().getString(R.string.pref_fb_profile_photo2), "");
    }

    public void setFbProfilePhoto2(String fbProfilePhoto2) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(mContext.getResources().getString(R.string.pref_fb_profile_photo2), fbProfilePhoto2);
        editor.apply();
    }

    public String getFbProfilePhoto3() {
        return prefs.getString(mContext.getResources().getString(R.string.pref_fb_profile_photo3), "");
    }

    public void setFbProfilePhoto3(String fbProfilePhoto3) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(mContext.getResources().getString(R.string.pref_fb_profile_photo3), fbProfilePhoto3);
        editor.apply();
    }

    public String getFbProfilePhoto4() {
        return prefs.getString(mContext.getResources().getString(R.string.pref_fb_profile_photo4), "");
    }

    public void setFbProfilePhoto4(String fbProfilePhoto4) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(mContext.getResources().getString(R.string.pref_fb_profile_photo4), fbProfilePhoto4);
        editor.apply();
    }

    public String getFbProfilePhoto5() {
        return prefs.getString(mContext.getResources().getString(R.string.pref_fb_profile_photo5), "");
    }

    public void setFbProfilePhoto5(String fbProfilePhoto5) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(mContext.getResources().getString(R.string.pref_fb_profile_photo5), fbProfilePhoto5);
        editor.apply();
    }

    public String getFbProfilePhoto6() {
        return prefs.getString(mContext.getResources().getString(R.string.pref_fb_profile_photo6), "");
    }

    public void setFbProfilePhoto6(String fbProfilePhoto6) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(mContext.getResources().getString(R.string.pref_fb_profile_photo6), fbProfilePhoto6);
        editor.apply();
    }

    public String getCollegeName() {
        return prefs.getString(mContext.getResources().getString(R.string.pref_college_name), "");
    }

    public void setCollegeName(String collegeName) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(mContext.getResources().getString(R.string.pref_college_name), collegeName);
        editor.apply();
    }


    public String getRelationshipStatus() {
        return prefs.getString(mContext.getResources().getString(R.string.pref_relationship_status), "");
    }

    public void setRelationshipStatus(String relationshipStatus) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(mContext.getResources().getString(R.string.pref_relationship_status), relationshipStatus);
        editor.apply();
    }

    public String getFilterSingleMan() {
        return prefs.getString(mContext.getResources().getString(R.string.pref_filter_single_man), "");
    }

    public void setFilterSingleMan(String relationshipStatus) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(mContext.getResources().getString(R.string.pref_filter_single_man), relationshipStatus);
        editor.apply();
    }


    public String getFilterSingleWoman() {
        return prefs.getString(mContext.getResources().getString(R.string.pref_filter_single_woman), "");
    }

    public void setFilterSingleWoman(String relationshipStatus) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(mContext.getResources().getString(R.string.pref_filter_single_woman), relationshipStatus);
        editor.apply();
    }

    public String getAbout() {
        return prefs.getString(mContext.getResources().getString(R.string.pref_about), "");
    }

    public void setAbout(String about) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(mContext.getResources().getString(R.string.pref_about), about);
        editor.apply();
    }

    public String getAge() {
        return prefs.getString(mContext.getResources().getString(R.string.pref_age), "");
    }

    public void setAge(String age) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(mContext.getResources().getString(R.string.pref_age), age);
        editor.apply();
    }

    public String getProfession() {
        return prefs.getString(mContext.getResources().getString(R.string.pref_profession), "");
    }

    public void setProfession(String profession) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(mContext.getResources().getString(R.string.pref_profession), profession);
        editor.apply();
    }

    public String getQbuserid() {
        return prefs.getString(mContext.getResources().getString(R.string.qbuserid), "");
    }

    public void setQbuserid(String qbuserid) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(mContext.getResources().getString(R.string.qbuserid), qbuserid);
        editor.apply();
    }

    public String getQbuserlogin() {
        return prefs.getString(mContext.getResources().getString(R.string.qbuserlogin), "");
    }

    public void setQbuserlogin(String qbuserlogin) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(mContext.getResources().getString(R.string.qbuserlogin), qbuserlogin);
        editor.apply();
    }

    public String getQbpass() {
        return prefs.getString(mContext.getResources().getString(R.string.qbpass), "");
    }

    public void setQbpass(String qbpass) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(mContext.getResources().getString(R.string.qbpass), qbpass);
        editor.apply();
    }
}
