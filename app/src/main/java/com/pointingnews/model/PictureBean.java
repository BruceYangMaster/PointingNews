package com.pointingnews.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2015-12-17.
 */
public class PictureBean implements Serializable{
    private String id;
    private String title;
    private String long_title;
    private String source;
    private String pic;
    private String kpic;
    private String intro;
    private PicsBean pics;
    private PictureCommetsInfoBean comment_count_info;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLong_title() {
        return long_title;
    }

    public void setLong_title(String long_title) {
        this.long_title = long_title;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getKpic() {
        return kpic;
    }

    public void setKpic(String kpic) {
        this.kpic = kpic;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public PicsBean getPics() {
        return pics;
    }

    public void setPics(PicsBean pics) {
        this.pics = pics;
    }

    public PictureCommetsInfoBean getComment_count_info() {
        return comment_count_info;
    }

    public void setComment_count_info(PictureCommetsInfoBean comment_count_info) {
        this.comment_count_info = comment_count_info;
    }
}
