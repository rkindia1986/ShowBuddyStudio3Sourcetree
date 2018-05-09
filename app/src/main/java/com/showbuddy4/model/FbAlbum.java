package com.showbuddy4.model;

/**
 * Created by User on 26-04-2018.
 */

public class FbAlbum {
    public FbAlbum(String created_time, String name, String id) {
        this.created_time = created_time;
        this.name = name;
        this.id = id;
    }

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    //getAlbum: {"created_time":"2013-01-22T08:59:35+0000","name":"Mobile Uploads","id":"379066858856671"}
    String created_time,name,id;
}
