package com.pointingnews.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2015-12-17.
 */
public class ImgBean implements Serializable{
    private String pic;
    private String alt;
    private String kpic;

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getKpic() {
        return kpic;
    }

    public void setKpic(String kpic) {
        this.kpic = kpic;
    }
}
