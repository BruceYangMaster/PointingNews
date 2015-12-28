package com.pointingnews.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2015-12-17.
 */
public class PictureDataBean implements Serializable{
    private String is_intro;
    private ArrayList<PictureBean> list;

    public String getIs_intro() {
        return is_intro;
    }

    public void setIs_intro(String is_intro) {
        this.is_intro = is_intro;
    }

    public ArrayList<PictureBean> getList() {
        return list;
    }

    public void setList(ArrayList<PictureBean> list) {
        this.list = list;
    }
}
