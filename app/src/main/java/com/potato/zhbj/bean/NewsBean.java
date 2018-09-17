package com.potato.zhbj.bean;

import java.util.ArrayList;

/**
 * Created by li.zhirong on 2018/9/13/013 16:48
 */
public class NewsBean {
   public int retcode;
   public ArrayList<Integer> extend;
   public ArrayList<NewsBeanData> data;

    @Override
    public String toString() {
        return "NewsBean{" +
                "retcode=" + retcode +
                ", extend=" + extend +
                ", data=" + data +
                '}';
    }

    public class NewsBeanData {
        public int id;
        public String title;
        public int type;
        public ArrayList<TabData> children;

        @Override
        public String toString() {
            return "NewsBeanData{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    ", type=" + type +
                    ", children=" + children +
                    '}';
        }
    }

    public class TabData {
        public int id;
        public String title;
        public int type;
        public String url;

        @Override
        public String toString() {
            return "TabData{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    ", type=" + type +
                    ", url='" + url + '\'' +
                    '}';
        }
    }
}
