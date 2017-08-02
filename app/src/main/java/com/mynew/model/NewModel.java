package com.mynew.model;

import java.io.Serializable;

/**
 * Created by user on 2017/4/18.
 */
public class NewModel extends BaseModel implements Serializable{

    private String title=null;      //标题
    private String digest=null;    //描述
    private String docid=null;     //内容ID
    private String ltitle=null;   //原标题
    private String source=null;   //来源
    private String imgsrc=null;   //图片路径
    private ImageModel imageModel=null;   //图片模型
    private String ptime=null;     //时间

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public String getLtitle() {
        return ltitle;
    }

    public void setLtitle(String ltitle) {
        this.ltitle = ltitle;
    }

    public String getImgsrc() {
        return imgsrc;
    }

    public void setImgsrc(String imgsrc) {
        this.imgsrc = imgsrc;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public ImageModel getImageModel() {
        return imageModel;
    }

    public void setImageModel(ImageModel imageModel) {
        this.imageModel = imageModel;
    }

    public String getPtime() {
        return ptime;
    }

    public void setPtime(String ptime) {
        this.ptime = ptime;
    }

    @Override
    public boolean equals(Object o) {
        NewModel newModel=(NewModel)o;
        return docid.equals(newModel.getDocid());
    }

    @Override
    public int hashCode() {
        return docid.hashCode();
    }
}
