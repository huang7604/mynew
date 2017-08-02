package com.mynew.util;

import com.mynew.model.ImageDetailModel;

import java.util.ArrayList;

/**
 * Created by user on 2017/5/1.
 */
public class NewDetailReplace {
    public String replace(String content, ArrayList<ImageDetailModel> imageList){
        if(imageList!=null){
            if(imageList.size()>0){
                for(int i=0;i<imageList.size();i++){
                    ImageDetailModel imageDetailModel=imageList.get(i);
                    content=content.replace(imageDetailModel.getRef(),"<p><img src=\""+imageDetailModel.getSrc()+
                            "?pixel="+imageDetailModel.getPixel()+"\"/><span>"+imageDetailModel.getAlt()+"</span></p>");
                }
            }
        }
        return content;
    }
}
