package com.pointingnews.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2015-12-17.
 */
public class PictureTopicBean implements Serializable{
    private String topic;
    private String url;
    private int id;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
