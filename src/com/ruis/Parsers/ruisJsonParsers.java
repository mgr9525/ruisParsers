package com.ruis.Parsers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class ruisJsonParsers<T> {
	 private T result=null;
	    private Class<?> Tclass;
	    private Field[] fields=null;
	    private String xmlStr="";

	    private  JSONObject  parser;
	    public ruisJsonParsers(T t){
	        result=t;
	        fields=t.getClass().getDeclaredFields();
	        try {
	            Tclass = Class.forName(t.getClass().getName());
	        }catch (Exception e){Tclass=null;}
	    }

	    public void parse(String strs) throws Exception
	    {
	        if(fields==null)
	            throw new Exception("错误的类");
	        parser=new JSONObject(strs.trim());
	        if(parser==null)
	            throw new Exception("错误的json");
	        	
	        parses(fields,parser,result);
	    }
	    
	    private void parses(Field[] flds, JSONObject  par,Object obj) throws Exception
	    {
		        	Iterator<String> keys=par.keys();
		        	while(keys.hasNext()) {
		    	        try {
			        		String key=keys.next();
			        		if(!par.isNull(key))
			        		{
			        			Object o=par.get(key);
			        			parseNeirong(flds,obj,key,o);
			        		}
		    	        }catch (Exception e){
		    	        	if(e.getMessage()!=null)
		    	        	System.err.println("ruis_err:ruisJsonParsers->parses : "+e.getMessage());
		    	        }
		        	} 
	    }
	    private void parseNeirong(Field[] flds,Object obj,String key,Object objs) throws Exception
	    {
	            //Field[] flds=objNode.getCls().getClass().getDeclaredFields();

	            for (int i = 0; i < flds.length; i++) {
	            	Field fld=flds[i];
	                fld.setAccessible(true);
	                String fname = fld.getName();
	                String ftype = fld.getType().getName();
	            	if(fname.equals(key))
	            	{
	            		if(objs instanceof JSONObject||objs instanceof JSONArray)
	        			{
	        				if(ftype.equals("java.util.List"))
	        				{
	        					Field[] fls=null;
	        					Object inst=null;
	        					JSONObject jsonObj=null;
	        					String name=result.getClass().getName();
                                String oname=fname;//+"_cls";
                                Class<?>[] cls=Tclass.getClasses();
                                for(Class<?> cl : cls) {
                                    if(cl.getName().equals(name+"$"+oname)) {
                                    	inst=cl.newInstance();
                                    	fls=cl.getDeclaredFields();
                                    	
                                    	List<Object> flist=(List)fld.get(obj);
                                        if(flist==null)
                                            flist=new ArrayList<Object>();
                                        flist.add(inst);
                                    	fld.set(obj, flist);
                                    	break;
                                    }
                                }
                                
                                if(objs instanceof JSONObject){
                                	jsonObj=(JSONObject)objs;
                                    if(fls!=null&&jsonObj!=null&&inst!=null)
                                    	parses(fls,jsonObj,inst);
                                }else{
                                	JSONArray ja=(JSONArray)objs;
                                	int size=ja.length();
                                	for(int j=0;j<size;j++)
                                	{
                                		jsonObj = (JSONObject) ja.get(j);
                                        if(fls!=null&&jsonObj!=null&&inst!=null)
                                        	parses(fls,jsonObj,inst);
                                	}
                                }
	        				}
	        			}else{
		                    try {
		                         fld.set(obj, objs);
		                    } catch (Exception e) {
		                    }
	        			}
	            	}
	            }
	    }
}
