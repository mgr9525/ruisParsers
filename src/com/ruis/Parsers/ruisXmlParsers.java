package com.ruis.Parsers;

import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

/**
 * Created by mgr9525 on 2016/1/14.
 */
public class ruisXmlParsers<T> {
    private T result=null;
    private Class<?> Tclass;
    private Field[] fields=null;
    private String xmlStr="";
    private ListNode objNode=null;
    private List<ListNode> objList=null;

    private XmlPullParser parser;
    private XmlPullParserFactory pullParserFactory;

    public ruisXmlParsers(T t){
        result=t;
        objList=new ArrayList<ListNode>();
        fields=t.getClass().getDeclaredFields();
        try {
            pullParserFactory = XmlPullParserFactory.newInstance();
            parser = pullParserFactory.newPullParser(); //由android.util.Xml创建一个XmlPullParser实例
            Tclass = Class.forName(t.getClass().getName());
        }catch (Exception e){Tclass=null;e.printStackTrace();}
    }

    public void parse(String strs) throws Exception
    {
        if(fields==null)
            throw new Exception("错误的类");
        parser.setInput(new StringReader(strs.trim()));
        XmlPullParser tper=null;
        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    //result = new T();
                    break;
                case XmlPullParser.START_TAG:
                    Field[] flds=fields;
                    if(objNode!=null)
                        flds=objNode.getCls().getDeclaredFields();
                    startTagNeirong(flds);
                    break;
                case XmlPullParser.END_TAG:
                    endTagNeirong();
                /*if (parser.getName().equals("VideoBoxs")) {
                	ivideo.getvbList().add(video);
                    video = null;
                }*/
                    break;
            }
            eventType = parser.next();
        }
            //return result;
    }
    private void startTagNeirong(Field[] flds)
    {
        try {
            //Field[] flds=objNode.getCls().getClass().getDeclaredFields();
			System.out.println("startTagNeirong tag name:"+parser.getName());

            for (int i = 0; i < flds.length; i++) {
                flds[i].setAccessible(true);
                String fname = flds[i].getName();
                String type = flds[i].getType().getName();
                if (parser.getName().equals(fname)) {
                    Object val = null;
                    try {
                    	int evtp=0;
                        switch (flds[i].getType().getName()) {
                            case "int":
                            	evtp=parser.next();
                            	if(evtp==XmlPullParser.TEXT)
                            		val = Integer.parseInt(parser.getText());
                                break;
                            case "java.lang.String":
                            	evtp=parser.next();
                            	if(evtp==XmlPullParser.TEXT)
                            		val = parser.getText();
                                break;
                            case "java.util.List":
                                String name=result.getClass().getName();
                                String oname=fname.substring(0, fname.length() - 1);
                                Object parent=result;
                                if(objNode!=null) {
                                    parent=objNode.getParent();
                                    objList.add(0, objNode);
                                }
                                Class<?>[] cls=Tclass.getClasses();
                                for(Class<?> cl : cls) {
                                    if(cl.getName().equals(name+"$"+oname)) {
                                        objNode = new ListNode();
                                        objNode.setName(fname);
                                        objNode.setField(flds[i]);
                                        objNode.setCls(cl);
                                        objNode.setParent(parent);
                                        objNode.setObj(cl.newInstance());
                                    }
                                }
                                break;
                        }
                    } catch (Exception e) {
                    }
                    if (val != null) {
                        if(objNode==null)
                            flds[i].set(result, val);
                        else
                            flds[i].set(objNode.getObj(), val);
                    }
                }
            }
        }catch (Exception e){}
    }

    private void endTagNeirong()
    {
        if(objNode==null)return;
        try {
            if (parser.getName().equals(objNode.getName())) {
                List<Object> flist=(List)objNode.getField().get(objNode.getParent());
                if(flist==null)
                    flist=new ArrayList<Object>();
                flist.add(objNode.getObj());
                objNode.getField().set(objNode.getParent(),flist);
                if(objList.size()>0) {
                    objNode = objList.get(0);
                    objList.remove(0);
                }else
                    objNode=null;
            }
        }catch (Exception e){
        }
    }

    protected class ListNode{
        private Field field;
        private Class<?> cls;
        private Object obj;
        private Object parent;
        private String name;

        public Field getField() {
            return field;
        }

        public void setField(Field field) {
            this.field = field;
        }

        public Class<?> getCls() {
            return cls;
        }

        public void setCls(Class<?> cls) {
            this.cls = cls;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getObj() {
            return obj;
        }

        public void setObj(Object obj) {
            this.obj = obj;
        }

        public Object getParent() {
            return parent;
        }

        public void setParent(Object parent) {
            this.parent = parent;
        }
    }
}
