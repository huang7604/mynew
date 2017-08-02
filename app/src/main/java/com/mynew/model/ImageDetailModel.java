package com.mynew.model;

/**
 * Created by user on 2017/5/1.
 */
public class ImageDetailModel extends BaseModel {
    private String ref;
    private String pixel;
    private String src;
    private String alt;

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getPixel() {
        return pixel;
    }

    public void setPixel(String pixel) {
        this.pixel = pixel;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getSrc() {
        return src;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getAlt() {
        return alt;
    }
}
