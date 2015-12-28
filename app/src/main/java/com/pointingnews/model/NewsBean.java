package com.pointingnews.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2015-12-11.
 */
public class NewsBean implements Serializable{

    private int replyCount;
    private int votecount;
    private String docid;
    private String title;
    private int hasAD;
    private int priority;
    private String lmodify;
    private String imgsrc;
    private String url;
    private ArrayList<AdBean> ads;
    private String digest;
    private String ptime;
    private String source;
    private ArrayList<ImageBean> imgextra;



    public String getPtime() {
        return ptime;
    }

    public void setPtime(String ptime) {
        this.ptime = ptime;
    }

    public ArrayList<ImageBean> getImgextra() {
        return imgextra;
    }

    public void setImgextra(ArrayList<ImageBean> imgextra) {
        this.imgextra = imgextra;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public int getVotecount() {
        return votecount;
    }

    public void setVotecount(int votecount) {
        this.votecount = votecount;
    }

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getHasAD() {
        return hasAD;
    }

    public void setHasAD(int hasAD) {
        this.hasAD = hasAD;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getLmodify() {
        return lmodify;
    }

    public void setLmodify(String lmodify) {
        this.lmodify = lmodify;
    }

    public String getImgsrc() {
        return imgsrc;
    }

    public void setImgsrc(String imgsrc) {
        this.imgsrc = imgsrc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayList<AdBean> getAds() {
        return ads;
    }

    public void setAds(ArrayList<AdBean> ads) {
        this.ads = ads;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }
}
