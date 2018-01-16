package com.example.wsq.android.tools;

import android.text.TextUtils;

import com.example.wsq.android.bean.ContentType;
import com.example.wsq.android.bean.FileType;
import com.example.wsq.android.bean.WebContentBean;
import com.example.wsq.android.constant.WebKeys;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by wsq on 2018/1/12.
 */

public class HtmlContent {


    /**
     * 添加标题
     * @param param
     * @param title
     * @param color
     * @param fontSize
     */
    public static void onAddTitle(Map<String, WebContentBean> param, String title, String color, int fontSize){

        String mtitle = "<div style=\"text-align: center\">" +
                "<span style=\"font-size: "+fontSize+"px; color: "+color+"\">"+title+"</span>" +
                "</div>";
        WebContentBean bean = new WebContentBean();
        bean.setContent(title);
        bean.setHtmlContent(mtitle);
        bean.setType(ContentType.TITLE);
        bean.setFontColor(color);
        bean.setFontSize(fontSize);
        bean.setAddTime(1);
        param.put(WebKeys.TITLE, bean);

    }

    /**
     * 添加创建时间
     * @param param
     * @param time
     * @param color
     * @param fontSize
     */
    public static void onAddCreateTime(Map<String, WebContentBean>  param, String time, String color, int fontSize){

        WebContentBean bean = new WebContentBean();
        String createTime = "<div style=\"text-align: right; \">" +
                "<span style=\"font-size: "+fontSize+"px; color: "+color+"\">"+time+"</span>" +
                "<input type=\"button\" style=\"background-color: green; color: white;" +
                " border-radius: 5px; border: white; padding-left: 10px; padding-right: 10px;" +
                " padding-top: 2px; padding-bottom: 2px; margin-left: 10px;\"" +
                " value=\"收藏\" onclick=\"onCollectClickListener()\">" +"</div>" +
                "<div style=\"width: 100%; height: 1px; background-color: #AAAAAA;" +
                " margin-top: 5px;margin-bottom: 5px\"></div>";

        bean.setContent(time);
        bean.setHtmlContent(createTime);
        bean.setType(ContentType.CREATE_TIME);
        bean.setFontColor(color);
        bean.setFontSize(fontSize);
        bean.setAddTime(2);
        param.put(WebKeys.CREATE_TIME, bean);

    }

    /**
     * 添加文字内容
     * @param param
     * @param content
     * @param color
     * @param fontSize
     */
    public static void onAddContent(Map<String, WebContentBean> param, String content, String color, int fontSize){

        WebContentBean bean = new WebContentBean();
        String mContent = "<div>&nbsp;&nbsp;&nbsp;&nbsp;" +
                "<span style=\"font-size: "+fontSize+"px; color: "+color+"\">"+content+"</span>" +
                "</div>";

        bean.setContent(content);
        bean.setHtmlContent(mContent);
        bean.setType(ContentType.CONTENT_FONT);
        bean.setFontColor(color);
        bean.setFontSize(fontSize);
        bean.setAddTime(System.currentTimeMillis());
        param.put(WebKeys.CONTENT+"_"+System.currentTimeMillis()+"", bean);
    }

    /**
     * 添加内容图片
     * @param param
     * @param url
     */
    public static void onAddContentImgs(Map<String, WebContentBean> param, String url){

        WebContentBean bean = new WebContentBean();
        String mContent = "<div style=\"text-align: center\"><img style=\"width: 95%; height: 95%;\" src=\""+url+"\"></div>";

        bean.setContent(url);
        bean.setHtmlContent(mContent);
        bean.setType(ContentType.CONTENT_IMG);
        bean.setAddTime(System.currentTimeMillis());
//        bean.setFontColor(color);
//        bean.setFontSize(fontSize);

        param.put(WebKeys.IMG+"_"+System.currentTimeMillis()+"", bean);
    }


    /**
     * 添加附件
     * @param param
     * @param path
     * @param fileName
     */
    public static void onAddAddachment(Map<String, WebContentBean> param, String path, String fileName){
        WebContentBean bean = new WebContentBean();
        String icon = "";
        if (fileName.toLowerCase().endsWith(FileType.DOC.getName())
                || fileName.toLowerCase().endsWith(FileType.DOCX.getName())){
            icon = "../image/word.png";
        }else if (fileName.toLowerCase().endsWith(FileType.PDF.getName())){
            icon = "../image/pdf.png";
        }else if (fileName.toLowerCase().endsWith(FileType.TXT.getName())){
            icon = "../image/txt.png";
        }else if (fileName.toLowerCase().endsWith(FileType.EXCEL.getName())){
            icon = "../image/excel.png";
        }else if (fileName.toLowerCase().endsWith(FileType.PNG.getName())
                || fileName.toLowerCase().endsWith(FileType.PNG.getName())){
            icon = "../image/png.png";
        }

        String mContent = "<div><img src=\""+icon+"\" style=\"width: 20px; height: 20px;\"><a href=\""+path+"\" style=\"margin-left: 10px\">"+fileName+"</a></div>";
//        bean.setContent(mContent);
        bean.setHtmlContent(mContent);
        bean.setType(ContentType.ADDACHMENT);
        bean.setAddTime(System.currentTimeMillis());
        param.put(WebKeys.ADDACHMENT+"_"+System.currentTimeMillis()+"", bean);
    }


    /**
     * 开始生成html代码
     * @param param
     * @return
     * @throws Exception
     */
    public static String onCreateHtml(Map<String, WebContentBean> param) throws Exception{
        String content = "";
        if (TextUtils.isEmpty(param.get(WebKeys.TITLE).getContent()+"")){
            throw new Exception("标题不能为空");
        }
        content = param.get(WebKeys.TITLE).getHtmlContent()+"";

        if(TextUtils.isEmpty(param.get(WebKeys.CREATE_TIME).getContent()+"")){
            throw new Exception("创建时间为空了");
        }

        content += param.get(WebKeys.CREATE_TIME).getHtmlContent()+"";
        //排序
        List<Map.Entry<String, WebContentBean>> newMap = new ArrayList<Map.Entry<String, WebContentBean>>(param.entrySet());
        Collections.sort(newMap, new Comparator<Map.Entry<String, WebContentBean>>() {
            public int compare(Map.Entry<String, WebContentBean> o1, Map.Entry<String, WebContentBean> o2) {
                return (o1.getValue().getAddTime()+"").toString().compareTo(o2.getValue().getAddTime()+"");
            }
        });

        for (Map.Entry<String, WebContentBean> entry : newMap){

            if (entry.getValue().getType()== ContentType.CONTENT_FONT|| entry.getValue().getType()==ContentType.CONTENT_IMG){
                content += entry.getValue().getHtmlContent();
            }
        }

        return content;
    }


    /**
     * 创建附件内容
     * @param param
     * @return
     * @throws Exception
     */
    public static String onCreateAddachment(Map<String, WebContentBean> param){
        String content = "";
        //排序
        List<Map.Entry<String, WebContentBean>> newMap = new ArrayList<Map.Entry<String, WebContentBean>>(param.entrySet());
        Collections.sort(newMap, new Comparator<Map.Entry<String, WebContentBean>>() {
            public int compare(Map.Entry<String, WebContentBean> o1, Map.Entry<String, WebContentBean> o2) {
                return (o1.getValue().getAddTime()+"").toString().compareTo(o2.getValue().getAddTime()+"");
            }
        });
        int num = 0;
        for (Map.Entry<String, WebContentBean> entry : newMap){

            if (entry.getValue().getType()== ContentType.ADDACHMENT){
                content += entry.getValue().getHtmlContent();
                num++;
            }
        }
        Logger.d("========"+num);
        return  content;
    }


}
