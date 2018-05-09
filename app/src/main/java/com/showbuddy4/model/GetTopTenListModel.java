package com.showbuddy4.model;

import java.io.Serializable;

/**
 * Created by Ashish on 2/20/2018.
 */

public class GetTopTenListModel implements Serializable {


    /**
     * artistid : 1
     * textboxno : 10
     * name : abc
     */

    private String artistid;
    private String textboxno;
    private String name;

    public String getArtistid() {
        return artistid;
    }

    public void setArtistid(String artistid) {
        this.artistid = artistid;
    }

    public String getTextboxno() {
        return textboxno;
    }

    public void setTextboxno(String textboxno) {
        this.textboxno = textboxno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
