package com.gen.stage2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

import com.gen.stage2.Reduce.CPair;
import com.gen.stage3.LOOPDETECT;

public class Reduce extends MapReduceBase implements Reducer<Text, Text, Text, Text> {
	
	public class kv{
		
		public String first=null,last=null,firstHalf = null, secondHalf = null;
		
		public kv(String val){
			String temp[] = val.split(":"),middle=null;
			first = temp[0];
			last = temp[temp.length-1];
			StringBuffer sb = new StringBuffer();
			String delimiter="";
			for(int i=1;i<temp.length-1;i++){
				sb.append(delimiter);
				sb.append(temp[i]);
				delimiter=":";
			}
			middle = sb.toString();
			if(middle.equalsIgnoreCase("")){
				firstHalf = first;
				secondHalf = last;
			}else{
				firstHalf = first+":"+sb.toString();
				secondHalf = sb.toString()+":"+last;
			}
			
		}
		
		public boolean isCompatible(kv other){
			if(this.firstHalf.equalsIgnoreCase(other.secondHalf))
			{
	    		return true;
			}
			else if(this.secondHalf.equalsIgnoreCase(other.firstHalf))
			{
				return true;
			}else{
				return false;
			}
		}
		
		public String fuze(kv other){
			StringBuffer sb = new StringBuffer();
			if(this.firstHalf.equalsIgnoreCase(other.secondHalf))
			{
	    		sb.append(other.first);
	    		sb.append(":");
	    		sb.append(this.firstHalf);
	    		sb.append(":");
	    		sb.append(this.last);
			}
			else if(this.secondHalf.equalsIgnoreCase(other.firstHalf))
			{
				sb.append(this.first);
	    		sb.append(":");
	    		sb.append(other.firstHalf);
	    		sb.append(":");
	    		sb.append(other.last);
			}else{
				return "";
			}
			return sb.toString();
		}
	}
	
	public class CPair{
		public String key; //T1:....:Tn
		public String mapKey; // T2:..:Tn-1
		
		public String firstHalf; //T1:..:Tn-1
		public String secondHalf; // T2:...:Tn
		public List<String> values;
		
		public CPair(String key,String cKey,List<String> values){
			this.key = key;
			this.mapKey = cKey;
			this.values = values;
		}
		
		public CPair(String str){
			String elems[] = str.split(",");
			this.key = elems[0];
			this.values = new ArrayList<String>();
			
			for(int i =1;i<elems.length;i++){
				this.values.add(elems[i]);
			}
			/*
			String temp[] = key.split(":");
			StringBuffer sb = new StringBuffer();
			String delimiter="";
			for(int i=1;i<temp.length-1;i++){
				sb.append(delimiter);
				sb.append(temp[i]);
				delimiter=":";
			}
			this.mapKey="";
			this.mapKey = sb.toString();
			*/
		}
		
		public CPair(){
			
		}
		
	}
	
	
    public void reduce(Text key, Iterator<Text> values, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {
    	
    	HashMap<String,List<CPair>> map = new HashMap<String,List<CPair>>();
    	List<CPair> list = null;
    	HashMap<String, StringBuffer> out=new HashMap<String, StringBuffer>();
    	while(values.hasNext()){
    		CPair cpair= new CPair(values.next().toString());
    		if(map.containsKey(cpair.mapKey)){
    			list = map.get(cpair.mapKey);
    		}else{
    			list = new ArrayList<CPair>();
    			map.put(cpair.mapKey, list);
    		}
    		list.add(cpair);
    	}
    	
    	Iterator<Entry<String, List<CPair>>> it = map.entrySet().iterator();
    	Entry<String, List<CPair>> entry = null;
    	CPair result = null;
    	while(it.hasNext()){
    		entry = it.next();
    		List<CPair> listPairs = entry.getValue();
    		for(int i=0;i<listPairs.size();i++){
    			CPair pair1 = listPairs.get(i);
    			for(int j=0;j<listPairs.size();j++){
    				CPair pair2 = listPairs.get(j);
    				
    				if(isCompatible(pair1, pair2)){
    					result = fuze(pair1,pair2);
    				}else if(isCompatible(pair2, pair1)){
    					result = fuze(pair2,pair1);
    				}
    				
    				if(result!=null){
    					for(String value:result.values){
    						//System.out.println(value);
    						/*
    						if(out.containsKey(result.key.toString()))
    						{
    							out.get(result.key.toString()).append(",");
    							out.get(result.key.toString()).append(value);
    						}
    						else
    							out.put(result.key.toString(), new StringBuffer().append(value));
    						*/
    						output.collect(new Text(result.key), new Text(value));
    					
    					}
    					result = null;
    				}else{}
    			}
    		}
    		/*
    		Iterator<Entry<String, StringBuffer>> it_out = out.entrySet().iterator();
        	Entry<String, StringBuffer> entryout = null;
        	while(it_out.hasNext()){
        		entryout = it_out.next();
        		output.collect(new Text(entryout.getKey()), new Text(entryout.getValue().toString()));
        	}
        	*/
    	}	
    }
    
    public boolean isCompatible(CPair pair1,CPair pair2){
    	return isCompatible(pair1.key, pair2.key);
	}
    
    public boolean isCompatible(String str1,String str2){
    	String secondHalf = str1.substring(str1.indexOf(":")+1);
    	String firstHalf = str2.substring(0,str2.lastIndexOf(":"));
		return secondHalf.equals(firstHalf);
    }
    
    public CPair fuze(CPair pair1,CPair pair2){
    	CPair result = new CPair();
    	
    	result.key = fuze(pair1.key,pair2.key);
    	result.values = new ArrayList<String>();
    	
    	for(int i =0;i<pair1.values.size();i++){
    		String str1 = pair1.values.get(i);
    		for(int j =0;j<pair2.values.size();j++){
    			String str2 = pair2.values.get(j);
        		if(isCompatible(str1, str2)){
        			result.values.add(fuze(str1,str2));
        		}
        	}
    	}
    	return result;
    }
    
    public String fuze(String str1,String str2){
    	String secondHalf = str2.substring(str2.lastIndexOf(":")+1);
		return str1+":"+secondHalf;
    }
    
  }
