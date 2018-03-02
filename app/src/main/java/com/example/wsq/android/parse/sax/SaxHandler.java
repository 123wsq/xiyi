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
    private final String NODE = "node";
    public  static final String NODE_QUIT = "quit";  //安全退出按钮
    private final String NODE_START = "start";  //启动页
    private final String NODE_WELCOME = "welcome"; //欢迎页
    private final String NODE_ADVERTISING = "advertising";  //广告页节点
    private final String NODE_SLIDESHOW = "slideshow";  //轮播图节点
    private final String NODE_CHILD_FONT_COLOR = "font_color";
    public static  final String NODE_CHILD_BACKGROUNG = "background";
    private final String NODE_CHILD_URL = "url";
    private final String NODE_CHILD_PATH = "path";
    private String cur_Node = "";
    private String node_name = "";
    private String desc = "";

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

        if (NODE.equals(localName)){
            node_name= attributes.getValue("name");
            if (attributes.getValue("name").equals(NODE_QUIT)){
                mNodeContent = new HashMap<>();
                desc = attributes.getValue("desc");
                mNodeContent.put("name", node_name);
                mNodeContent.put("desc", desc);

            }
        }else if(localName.equals(NODE_CHILD_FONT_COLOR)){
            cur_Node = NODE_CHILD_FONT_COLOR;
        }else if(localName.equals(NODE_CHILD_BACKGROUNG)){
            cur_Node = NODE_CHILD_BACKGROUNG;
        }else {
            node_name="";
        }


    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);

        if (NODE.equals(localName)){
            if (node_name.equals(NODE_QUIT)){
                mData.put(NODE_QUIT, mNodeContent);
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        String tempString = new String(ch, start, length);
        if (cur_Node.equals(NODE_CHILD_FONT_COLOR)){
            mNodeContent.put(NODE_CHILD_FONT_COLOR, tempString);
        }else if(cur_Node.equals(NODE_CHILD_BACKGROUNG)){
            mNodeContent.put(NODE_CHILD_BACKGROUNG, tempString);
        }
        this.cur_Node = "";
    }


    public Map<String, Object> getData(){
        return mData;
    }
}
