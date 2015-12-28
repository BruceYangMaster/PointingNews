package com.pointingnews.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2015-12-10.
 */
public class TopicBean implements Serializable{
    private int id;       //自己为title topic设置id
    private String topic; //title topic
    private String ur;  //title topic的基本ur
    private String news_type_id;  //新闻分类的id

    public String getNews_type_id() {
        return news_type_id;
    }

    public void setNews_type_id(String news_type_id) {
        this.news_type_id = news_type_id;
    }

    public String getUr() {
        return ur;
    }

    public void setUr(String ur) {
        this.ur = ur;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
