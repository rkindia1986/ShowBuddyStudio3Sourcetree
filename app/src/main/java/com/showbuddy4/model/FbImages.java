package com.showbuddy4.model;

/**
 * Created by User on 27-04-2018.
 */

public class FbImages {
    public FbImages(String height, String source, String width) {
        this.height = height;
        this.source = source;
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    String height,source,width;
}
