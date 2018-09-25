package com.potato.zhbj.bean;

import java.util.ArrayList;

/**
 * Created by li.zhirong on 2018/9/19/019 13:48
 */
public class NewsTabBean {
    public TabDataBean data;

    public class TabDataBean {
        public String title;
        public ArrayList<TabTopNewsBean> topnews;
        public ArrayList<TabTopicBean> topic;
        public ArrayList<TabNewsBean> news;
        public String countcommenturl;
        public String more;
    }

    private class TabNewsBean {

        /**
         * id : 35311
         * title : 中国
         * url : http://10.0.2.2:8080/zhbj/10006/724D6A55496A11726628.html
         * listimage : http://10.0.2.2:8080/zhbj/10006/1452327318UU91.jpg
         * pubdate : 2014-04-08 14:58
         * comment : true
         * commenturl : http://zhbj.qianlong.com/client/user/newComment/35319
         * type : news
         * commentlist : http://10.0.2.2:8080/zhbj/10006/comment_1.json
         */
        public int id;
        public String title;
        public String url;
        public String listimage;
        public String pubdate;
        public boolean comment;
        public String commenturl;
        public String type;
        public String commentlist;
    }

    public class TabTopicBean {

        /**
         * title : 中国
         * id : 10101
         * url : http://10.0.2.2:8080/zhbj/10006/list_1.json
         * listimage : http://10.0.2.2:8080/zhbj/10006/1452327318UU91.jpg
         * description : 11111111
         * sort : 1
         */

        public String title;
        public int id;
        public String url;
        public String listimage;
        public String description;
        public int sort;
    }

    public class TabTopNewsBean {
        /**
         * id : 35301
         * title : 中国1
         * topimage : http://10.0.2.2:8080/zhbj/10006/1452327318UU91.jpg
         * url : http://10.0.2.2:8080/zhbj/10006/724D6A55496A11726628.html
         * pubdate : 2014-04-08 14:24
         * comment : true
         * commenturl : http://zhbj.qianlong.com/client/user/newComment/35301
         * type : news
         * commentlist : http://10.0.2.2:8080/zhbj/10006/comment_1.json
         */

        public int id;
        public String title;
        public String topimage;
        public String url;
        public String pubdate;
        public boolean comment;
        public String commenturl;
        public String type;
        public String commentlist;
    }
}
