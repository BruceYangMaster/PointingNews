package com.pointingnews.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2015-12-17.
 */
public class PicsBean implements Serializable{
    private ArrayList<ImgBean> list;

    public ArrayList<ImgBean> getList() {
        return list;
    }

    public void setList(ArrayList<ImgBean> list) {
        this.list = list;
    }
}
