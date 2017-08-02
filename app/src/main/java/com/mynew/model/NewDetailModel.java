package com.mynew.model;

import com.mynew.http.json.BaseJson;

import java.util.ArrayList;

/**
 * Created by user on 2017/4/20.
 */
public class NewDetailModel extends BaseModel {
    private String title=null;
    private String docid=null;
    private String source=null;
    private String ptime=null;
    private String body=null;
    private String url_mp4 = null;
    private String cover = null;
    private ArrayList<ImageDetailModel> imgList=null;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getPtime() {
        return ptime;
    }

    public void setPtime(String ptime) {
        this.ptime = ptime;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getUrl_mp4() {
        return url_mp4;
    }

    public void setUrl_mp4(String url_mp4) {
        this.url_mp4 = url_mp4;
    }

    public ArrayList<ImageDetailModel> getImgList() {
        return imgList;
    }

    public void setImgList(ArrayList<ImageDetailModel> imgList) {
        this.imgList = imgList;
    }
}
