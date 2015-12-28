package com.pointingnews.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2015-12-17.
 */
public class PictureCommetsInfoBean implements Serializable{
    private int qreply;
    private int praise;
    private int dispraise;

    public int getQreply() {
        return qreply;
    }

    public void setQreply(int qreply) {
        this.qreply = qreply;
    }

    public int getPraise() {
        return praise;
    }

    public void setPraise(int praise) {
        this.praise = praise;
    }

    public int getDispraise() {
        return dispraise;
    }

    public void setDispraise(int dispraise) {
        this.dispraise = dispraise;
    }
}
