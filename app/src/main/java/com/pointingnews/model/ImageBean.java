package com.pointingnews.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2015-12-14.
 */
public class ImageBean implements Serializable{
    private String imgsrc;
    private String alt;

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getImgsrc() {
        return imgsrc;
    }

    public void setImgsrc(String imgsrc) {
        this.imgsrc = imgsrc;
    }
}
