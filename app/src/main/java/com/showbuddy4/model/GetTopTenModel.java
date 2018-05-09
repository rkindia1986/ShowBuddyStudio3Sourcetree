package com.showbuddy4.model;

import java.io.Serializable;

/**
 * Created by Ashish on 2/18/2018.
 */

public class GetTopTenModel implements Serializable{


    /**
     * id : 1
     * name : abc
     */

    private String id;
    private String name;
private  String thumb_url;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumb_url() {
        return thumb_url;
    }

    public void setThumb_url(String thumb_url) {
        this.thumb_url = thumb_url;
    }
}
