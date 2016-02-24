package com.xiamu.riane.zhihuribao.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Xiamu on 2015/11/24.
 */
public class StoriesEntity implements Serializable{
    private String title;
   // private String ga_prefix;
   // private boolean multipic;
    private int type;
    private int id;
    private List<String> images;

    public void setTitle(String title) {
        this.title = title;
    }


    public void setType(int type) {
        this.type = type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getTitle() {
        return title;
    }

    public int getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public List<String> getImages() {
        return images;
    }
}
