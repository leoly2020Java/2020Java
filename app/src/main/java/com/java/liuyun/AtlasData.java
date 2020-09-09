package com.java.liuyun;

//关系、属性的数据格式，[title,descr]的例子：["潜伏期", "1-14天，多为3-7天"]
public class AtlasData {
   public String title;
   public String descr;
   AtlasData(String title, String descr) {
       this.title = title;
       this.descr = descr;
   }
}
