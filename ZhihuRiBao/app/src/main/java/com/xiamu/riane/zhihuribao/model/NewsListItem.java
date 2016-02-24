package com.xiamu.riane.zhihuribao.model;

/**
 * Created by Xiamu on 2015/11/13.
 */
public class NewsListItem {

    /**
     * color : 15007
     * thumbnail : http://pic3.zhimg.com/0e71e90fd6be47630399d63c58beebfc.jpg
     * description : 了解自己和别人，了解彼此的欲望和局限。
     * id : 13
     * name : 日常心理学
     */

    private String color;
    private String thumbnail;
    private String description;
    private String id;
    private String name;

    public void setColor(String color) {
        this.color = color;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
