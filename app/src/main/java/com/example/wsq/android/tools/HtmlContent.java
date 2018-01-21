package com.example.wsq.android.tools;

import android.content.Context;
import android.text.TextUtils;

import com.example.wsq.android.bean.AuthType;
import com.example.wsq.android.bean.ContentType;
import com.example.wsq.android.bean.FileType;
import com.example.wsq.android.bean.WebContentBean;
import com.example.wsq.android.constant.WebKeys;
import com.example.wsq.android.db.dao.impl.MaterialImpl;
import com.example.wsq.android.db.dao.inter.DbConInter;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wsq on 2018/1/12.
 */

public class HtmlContent {

    private static DbConInter conInter;
    /**
     * 添加标题
     * @param param
     * @param title
     * @param color
     * @param fontSize
     */
    public static void onAddTitle(Context context, Map<String, WebContentBean> param, String title, String color, int fontSize, AuthType authType){

        conInter = new MaterialImpl();

        WebContentBean bean = new WebContentBean();
        bean.setContent(title);
        bean.setHtmlContent(onTitleHtml(title, color, fontSize));
        bean.setType(ContentType.TITLE);
        bean.setFontColor(color);
        bean.setFontSize(fontSize);
        bean.setAddTime(1);
        bean.setAuthType(authType);
        param.put(WebKeys.TITLE, bean);
        conInter.insertData(context, WebKeys.TITLE, bean);
    }

    public static String onTitleHtml(String title, String color, int fontSize){
        String mtitle = "<div style=\"text-align: center\">" +
                "<span style=\"font-size: "+fontSize+"px; color: "+color+"\">"+title+"</span>" +
                "</div>";
        return mtitle;
    }


    /**
     * 将ICON保存到数据库中
     * @param context
     * @param iconPath
     */
    public static void onAddIcon(Context context, String iconPath, AuthType authType){
        conInter = new MaterialImpl();

        WebContentBean bean = new WebContentBean();
        bean.setContent(iconPath);
//        bean.setHtmlContent(onTitleHtml(title, color, fontSize));
        bean.setType(ContentType.ICON);
        bean.setFontColor("");
        bean.setFontSize(0);
        bean.setAuthType(authType);
        bean.setAddTime(System.currentTimeMillis());
        conInter.insertData(context, WebKeys.ICON, bean);

    }


    /**
     * 添加资料描述
     * @param context
     * @param content
     */
    public static void onAddInteroduction(Context context, String content, AuthType authType){

        conInter = new MaterialImpl();

        WebContentBean bean = new WebContentBean();
        bean.setContent(content);
//        bean.setHtmlContent(onTitleHtml(title, color, fontSize));
        bean.setType(ContentType.INTRODUCTION);
        bean.setFontColor("");
        bean.setFontSize(0);
        bean.setAuthType(authType);
        bean.setAddTime(System.currentTimeMillis());
        conInter.insertData(context, WebKeys.INTRODUCTION, bean);
    }
    /**
     * 添加文章类型
     * @param context
     * @param content
     */
    public static void onAddArticlesType(Context context, String articles, AuthType authType){

        conInter = new MaterialImpl();

        WebContentBean bean = new WebContentBean();
        bean.setContent(articles);
//        bean.setHtmlContent(onTitleHtml(title, color, fontSize));
        bean.setType(ContentType.ARTICLES);
        bean.setFontColor("");
        bean.setFontSize(0);
        bean.setAuthType(authType);
        bean.setAddTime(System.currentTimeMillis());
        conInter.insertData(context, WebKeys.ARTICLES, bean);
    }

    /**
     * 添加创建时间
     * @param param
     * @param time
     * @param color
     * @param fontSize
     */
    public static void onAddCreateTime(Context context, Map<String, WebContentBean>  param, String time, String color, int fontSize, AuthType authType){

        WebContentBean bean = new WebContentBean();

        bean.setContent(time);
        bean.setHtmlContent(onCreateTimeHtml(time, color, fontSize));
        bean.setType(ContentType.CREATE_TIME);
        bean.setFontColor(color);
        bean.setFontSize(fontSize);
        bean.setAddTime(2);
        bean.setAuthType(authType);
        param.put(WebKeys.CREATE_TIME, bean);

        conInter = new MaterialImpl();

        conInter.insertData(context, WebKeys.CREATE_TIME, bean);

    }

    public static String onCreateTimeHtml(String time, String color, int fontSize){

        String createTime = "<div style=\"text-align: right; \">" +
                "<span style=\"font-size: "+fontSize+"px; color: "+color+"\">"+time+"</span>" +
                "<input type=\"button\" style=\"background-color: #5cb85c; border-color: #4cae4c; color: white;" +
                " border-radius: 5px; border: white; padding-left: 10px; padding-right: 10px;" +
                " padding-top: 2px; padding-bottom: 2px; margin-left: 10px;\"" +
                " value=\"收藏\" onclick=\"onCollectClickListener()\">" +"</div>" +
                "<div style=\"width: 100%; height: 1px; background-color: #AAAAAA;" +
                " margin-top: 5px;margin-bottom: 5px\"></div>";

        return createTime;
    }

    /**
     * 添加文字内容
     * @param param
     * @param content
     * @param color
     * @param fontSize
     */
    public static void onAddContent(Context context, Map<String, WebContentBean> param,String key, long insertTime,  String content, String color, int fontSize, AuthType authType){


        //验证是否存在换行
        Pattern p = Pattern.compile("\\n");
        Matcher m = p.matcher(content);
        content = m.replaceAll("");

        //
        if (TextUtils.isEmpty(content)){
            return;
        }


        conInter = new MaterialImpl();

        if (TextUtils.isEmpty(key)){  //表示新增的数据
            WebContentBean bean = new WebContentBean();
            bean.setContent(content);
            bean.setHtmlContent(onContentHtml(content, color, fontSize));
            bean.setType(ContentType.CONTENT_FONT);
            bean.setFontColor(color);
            bean.setFontSize(fontSize);
            bean.setAuthType(authType);
            Logger.d("插入的时间   "+insertTime);
            bean.setAddTime(insertTime ==-1 ? System.currentTimeMillis() : insertTime);
            String newKey = WebKeys.CONTENT+"_"+System.currentTimeMillis()+"";
            param.put(newKey, bean);

            conInter.insertData(context, newKey, bean);

        }else{   //表示修改数据，所以在此处只需要修改编辑的内容和html 别的都不需要修改
            WebContentBean bean = param.get(key);
            bean.setContent(content);
            bean.setHtmlContent(onContentHtml(content, color, fontSize));
            bean.setAuthType(authType);
            param.put(key, bean);
            conInter.updateData(context, key, bean);
        }

    }

    public static String onContentHtml(String content, String color, int fontSize){
        String mContent = "<span style=\"font-size: "+fontSize+"px; color: "+color+"\">"+content+"</span>";

        return mContent;
    }

    /**
     * 添加内容图片
     * @param param
     * @param url
     */
    public static void onAddContentImgs(Context context, Map<String, WebContentBean> param, String url, long insertTime, AuthType authType){

        WebContentBean bean = new WebContentBean();

        bean.setContent(url);
        bean.setHtmlContent(onImageHtml(url));
        bean.setType(ContentType.CONTENT_IMG);
        bean.setAddTime(insertTime==-1 ? System.currentTimeMillis() : insertTime);
        bean.setAuthType(authType);
        String key = WebKeys.IMG+"_"+System.currentTimeMillis()+"";
        param.put(key, bean);

        conInter = new MaterialImpl();
        conInter.insertData(context, key, bean);

    }

    public static String onImageHtml( String url){
        String mContent = "<div style=\"text-align: center\"><img style=\"max-width: 95%; height: auto !important;\" src=\""+url+"\"></div>";

        return mContent;
    }


    /**
     * 添加附件
     * @param param
     * @param path
     * @param fileName
     */
    public static void onAddAddachment(Context context, Map<String, WebContentBean> param, String path, String fileName, AuthType authType){

        WebContentBean bean = new WebContentBean();
        bean.setContent(path);
        bean.setFileName(fileName);
        bean.setHtmlContent(onAddachmentHtml(path, fileName));
        bean.setType(ContentType.ADDACHMENT);
        bean.setAddTime(System.currentTimeMillis());
        bean.setAuthType(authType);
        bean.setFileName(fileName);
        String key = WebKeys.ADDACHMENT+"_"+System.currentTimeMillis()+"";
        param.put( key, bean);

        conInter = new MaterialImpl();
        conInter.insertData(context, key, bean);

    }

    public static String onAddachmentHtml(String path, String fileName){
        String iconPath = "http://xiyicontrol.com/vendor/ueditor/dialogs/attachment/fileTypeImages/";
        String icon = "";
        if (fileName.toLowerCase().endsWith(FileType.DOC.getName())
                || fileName.toLowerCase().endsWith(FileType.DOCX.getName())
                || fileName.toLowerCase().endsWith(FileType.PSD.getName())){
            icon = iconPath+ "icon_doc.gif";
        }else if (fileName.toLowerCase().endsWith(FileType.PDF.getName())){
            icon = iconPath+ "icon_pdf.gif";
        }else if (fileName.toLowerCase().endsWith(FileType.EXCEL.getName())){
            icon = iconPath+ "icon_xls.gif";
        }else if (fileName.toLowerCase().endsWith(FileType.PNG.getName())
                || fileName.toLowerCase().endsWith(FileType.PNG.getName())){
            icon = iconPath+ "icon_jpg.gif";
        }else if (fileName.toLowerCase().endsWith(FileType.TXT.getName())){
            icon = iconPath+ "icon_txt.gif";
        }else if (fileName.toLowerCase().endsWith(FileType.EXE.getName())){
            icon = iconPath+ "icon_exe.gif";
        }else if (fileName.toLowerCase().endsWith(FileType.MP3.getName())){
            icon = iconPath+ "icon_mp3.gif";
        }else if (fileName.toLowerCase().endsWith(FileType.MP4.getName())){
            icon = iconPath+ "icon_mv.gif";
        }else if (fileName.toLowerCase().endsWith(FileType.RAR.getName())
                || fileName.toLowerCase().endsWith(FileType.RAR.getName())){
            icon = iconPath+ "icon_rar.gif";
        }else if (fileName.toLowerCase().endsWith(FileType.PPT.getName())){
            icon = iconPath+ "icon_ppt.gif";
        }else{
            icon = iconPath+ "icon_default.png";
        }

        String mContent = "<div><img src=\""+icon+"\" style=\"width: 20px; height: 20px;\"><a href=\""+path+"\" style=\"margin-left: 10px\">"+fileName+"</a></div>";
        return mContent;
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
//                Logger.d(entry.getValue().toString());
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
        return  content;
    }


}
