package com.xiamu.riane.zhihuribao.model;

import java.util.List;

/**
 * Created by Xiamu on 2015/11/25.
 */
public class Before {
    private List<StoriesEntity> stories;
    private String date;

    public List<StoriesEntity> getStories() {
        return stories;
    }

    public void setStories(List<StoriesEntity> stories) {
        this.stories = stories;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
