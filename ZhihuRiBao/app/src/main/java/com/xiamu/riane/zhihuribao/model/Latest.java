package com.xiamu.riane.zhihuribao.model;

import java.util.List;

/**
 * Created by Xiamu on 2015/11/14.
 */
public class Latest {

    /**
     * date : 20151114
     * stories : [{"title":"一些玩物不丧志，能学到知识的游戏","ga_prefix":"111411","images":["http://pic3.zhimg.com/3562767f8d1f2655c3ab76620a2e6daa.jpg"],"multipic":true,"type":"0","id":"7428446"},{"images":["http://pic2.zhimg.com/7a15bdde0fa80cd972d6b8436b94bd25.jpg"],"type":"0","id":"7435359","ga_prefix":"111410","title":"明明吃了止疼片，咬自己的手还是会疼"},{"images":["http://pic2.zhimg.com/98094ac56becc992b041084a3b195d79.jpg"],"type":"0","id":"7428599","ga_prefix":"111409","title":"冬天跑步，南北方分别怎么穿"},{"images":["http://pic1.zhimg.com/c1ff12ae510784eca0a86a16a02d4698.jpg"],"type":"0","id":"7435411","ga_prefix":"111408","title":"我们随手就能保护海洋，这是件挺让人骄傲的事儿"},{"title":"如果你是懂深情和浪漫的人，别错过这两幅画","ga_prefix":"111407","images":["http://pic4.zhimg.com/d6cdff9eba3cbdd4cfeb937202429bd3.jpg"],"multipic":true,"type":"0","id":"7436240"},{"images":["http://pic4.zhimg.com/9df4f5137f9f99be34e5c0604d987b9b.jpg"],"type":"0","id":"7435607","ga_prefix":"111407","title":"这部据说超越《甄嬛传》的剧还没播出，编剧却起诉了制片方"},{"images":["http://pic3.zhimg.com/ed9eead958a77a75b7fceffdeeb825ce.jpg"],"type":"0","id":"7436000","ga_prefix":"111407","title":"留在大城市还是回到小城市，列个式子算一下"},{"images":["http://pic2.zhimg.com/ab6b9c50d67e06510ee36925e8a10abd.jpg"],"type":"0","id":"7429223","ga_prefix":"111406","title":"瞎扯 · 如何正确地吐槽"}]
     * top_stories : [{"image":"http://pic1.zhimg.com/11493515956113db4bfb689155eb8bfc.jpg","type":"0","id":"7436000","ga_prefix":"111407","title":"留在大城市还是回到小城市，列个式子算一下"},{"image":"http://pic3.zhimg.com/96ef40a5c96583f7cdfdd01fe8c277b6.jpg","type":"0","id":"7435607","ga_prefix":"111407","title":"这部据说超越《甄嬛传》的剧还没播出，编剧却起诉了制片方"},{"image":"http://pic1.zhimg.com/929900b6d4e7b3489b64f15761664ee4.jpg","type":"0","id":"7373553","ga_prefix":"111321","title":"这里上演了一场密码战争，而我解读了密码"},{"image":"http://pic1.zhimg.com/6a467f9f4555cd4ff63c3800b8dab3d0.jpg","type":"0","id":"7429671","ga_prefix":"111316","title":"比尔 · 盖茨能用手机控制整个家，全靠智能开关"},{"image":"http://pic3.zhimg.com/c8bba9f72da3637df2e0bfc5a1f0c59a.jpg","type":"0","id":"7111306","ga_prefix":"111319","title":"作为职业美食摄影师，我眼中专业的美食照是这样子的"}]
     */

    private String date;
    /**
     * title : 一些玩物不丧志，能学到知识的游戏
     * ga_prefix : 111411
     * images : ["http://pic3.zhimg.com/3562767f8d1f2655c3ab76620a2e6daa.jpg"]
     * multipic : true
     * type : 0
     * id : 7428446
     */

    private List<StoriesEntity> stories;
    /**
     * image : http://pic1.zhimg.com/11493515956113db4bfb689155eb8bfc.jpg
     * type : 0
     * id : 7436000
     * ga_prefix : 111407
     * title : 留在大城市还是回到小城市，列个式子算一下
     */

    private List<TopStoriesEntity> top_stories;

    public void setDate(String date) {
        this.date = date;
    }

    public void setStories(List<StoriesEntity> stories) {
        this.stories = stories;
    }

    public void setTop_stories(List<TopStoriesEntity> top_stories) {
        this.top_stories = top_stories;
    }

    public String getDate() {
        return date;
    }

    public List<StoriesEntity> getStories() {
        return stories;
    }

    public List<TopStoriesEntity> getTop_stories() {
        return top_stories;
    }


    public static class TopStoriesEntity {
        private String image;
        private int type;
        private int id;
        private String ga_prefix;
        private String title;

        public void setImage(String image) {
            this.image = image;
        }

        public void setType(int type) {
            this.type = type;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setGa_prefix(String ga_prefix) {
            this.ga_prefix = ga_prefix;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImage() {
            return image;
        }

        public int getType() {
            return type;
        }

        public int getId() {
            return id;
        }

        public String getGa_prefix() {
            return ga_prefix;
        }

        public String getTitle() {
            return title;
        }
    }
}
