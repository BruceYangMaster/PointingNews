package com.pointingnews.utils;

/**
 * Created by Administrator on 2015-12-11.
 */
public class UrlUtil {

    /**
     * 获取title topic url
     * @param pageNo     请求第几页
     * @param pageSize   一次请求多少条数据
     * @param ur         请求数据的基本ur
     * @return            请求目标url
     */
   public static String getUrlByUr(int pageNo,int pageSize,String ur){
       String url = ur + "/" + pageNo*pageSize + "-"  + pageSize + ".html";
       return url;
   }



}
