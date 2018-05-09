package com.showbuddy4.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Jainam on 22-12-2017.
 */

public class Data implements Serializable {

    private String description;

    private String imagePath;

    private String profile_id;
    private String proffesion,age,collage;

    public ArrayList<String> images_list;

    public Data(String profile_id, String imagePath, String description,ArrayList<String> images_list,String proffesion,String age,String collage) {
        this.profile_id = profile_id;
        this.imagePath = imagePath;
        this.description = description;
        this.images_list = images_list;
        this.proffesion=proffesion;
        this.age=age;
        this.collage=collage;
    }

    public String getProfile_id() {
        return profile_id;
    }

    public String getDescription() {
        return description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public ArrayList<String> getImages_list() {
        return images_list;
    }

    public void setImages_list(ArrayList<String> images_list) {
        this.images_list = images_list;
    }

    public String getProffesion() {
        return proffesion;
    }

    public void setProffesion(String proffesion) {
        this.proffesion = proffesion;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getCollage() {
        return collage;
    }

    public void setCollage(String collage) {
        this.collage = collage;
    }
}
