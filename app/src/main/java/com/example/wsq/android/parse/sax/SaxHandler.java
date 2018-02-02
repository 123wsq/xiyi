package com.example.wsq.android.parse.sax;

import com.orhanobut.logger.Logger;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/2/2 0002.
 */

public class SaxHandler extends DefaultHandler{

    private Map<String, Object> mData;
    private Map<String, Object> mNodeContent;
    private List<String> mUrls;
    private final String NODE_QUIT = "quit";  //安全退出按钮
    private final String NODE_START = "start";  //启动页
    private final String NODE_WELCOME = "welcome"; //欢迎页
    private final String NODE_ADVERTISING = "advertising";  //广告页节点
    private final String NODE_SLIDESHOW = "slideshow";  //轮播图节点
    private final String NODE_NAME = "name";
    private final String NODE_CHILD_FONT_COLOR = "font_color";
    private final String NODE_CHILD_BACKGROUNG = "background";
    private final String NODE_CHILD_URL = "url";
    private final String NODE_CHILD_PATH = "path";
    @Override
    public void startDocument() throws SAXException {
        mData = new HashMap<>();
        super.startDocument();
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if (qName.equals(NODE_QUIT)){
            mNodeContent = new HashMap<>();
        }else if(qName.equals(NODE_START)){
        }else if(qName.equals(NODE_WELCOME)){
        }else if(qName.equals(NODE_ADVERTISING)){

        }else if(qName.equals(NODE_SLIDESHOW)){

        }else if(qName.equals(NODE_CHILD_URL)){
            mUrls = new ArrayList<>();
        }else if(qName.equals(NODE_CHILD_PATH)){

        }else if(qName.equals(NODE_CHILD_FONT_COLOR)){

        }else if(qName.equals(NODE_CHILD_FONT_COLOR)){

        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);

        if (qName.equals(NODE_QUIT)){

        }else if(qName.equals(NODE_START)){

        }else if(qName.equals(NODE_WELCOME)){

        }else if(qName.equals(NODE_ADVERTISING)){

        }else if(qName.equals(NODE_SLIDESHOW)){

        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        String tempString = new String(ch, start, length);
        Logger.d(tempString);
    }


    public Map<String, Object> getData(){
        return mData;
    }
}
