package com.mynew.model;

import java.io.Serializable;

/**
 * Created by user on 2017/5/22.
 */
public class TabModel implements Serializable {
    private String id;
    private String name;

    public TabModel(){

    }

    public TabModel(String id,String name){
        this.id=id;
        this.name=name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object object){
        TabModel tabModel=(TabModel)object;
        if(this.getId().equals(tabModel.getId())&&this.getName().equals(tabModel.getName())){
            return true;
        }else {
            return false;
        }
    }
}
