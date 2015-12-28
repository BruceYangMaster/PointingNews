package com.pointingnews.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015-12-15.
 */
public class NewsContent {
    private String body;
    private String title;
    private String ptime;
    private ArrayList<NewsContentImg> img;
    private String source_url;
    private NewsSourceInfo sourceinfo;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPtime() {
        return ptime;
    }

    public void setPtime(String ptime) {
        this.ptime = ptime;
    }

    public ArrayList<NewsContentImg> getImg() {
        return img;
    }

    public void setImg(ArrayList<NewsContentImg> img) {
        this.img = img;
    }

    public String getSource_url() {
        return source_url;
    }

    public void setSource_url(String source_url) {
        this.source_url = source_url;
    }

    public NewsSourceInfo getSourceinfo() {
        return sourceinfo;
    }

    public void setSourceinfo(NewsSourceInfo sourceinfo) {
        this.sourceinfo = sourceinfo;
    }
}
