package com.pointingnews.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2015-12-17.
 */
public class PictureRootBean implements Serializable{
    private int status;
    private PictureDataBean data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public PictureDataBean getData() {
        return data;
    }

    public void setData(PictureDataBean data) {
        this.data = data;
    }
}
