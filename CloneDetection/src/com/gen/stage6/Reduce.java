package com.gen.stage6;


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
    	HashMap<String,List<CPair>> map_2 = new HashMap<String,List<CPair>>();
    	while(values.hasNext()){
    		CPair cpair= new CPair(values.next().toString());
    		if(key.toString().equals(cpair.key))
    		{
        		if(map.containsKey(cpair.mapKey)){
        			list = map.get(cpair.mapKey);
        		}else{
        			list = new ArrayList<CPair>();
        			map.put(cpair.mapKey, list);
        		}
        		list.add(cpair);
    		}
    		else
    		{	
        		if(map_2.containsKey(cpair.mapKey)){
        			list = map_2.get(cpair.mapKey);
        		}else{
        			list = new ArrayList<CPair>();
        			map_2.put(cpair.mapKey, list);
        		}
        		list.add(cpair);
    		}
    	}
    	
    	Iterator<Entry<String, List<CPair>>> it = map.entrySet().iterator();
    	Entry<String, List<CPair>> entry = null;
    	CPair result = null;
    	HashMap<String,Integer> duplica=new HashMap<String,Integer>();
    	while(it.hasNext()){
    		entry = it.next();
    		List<CPair> listPairs = entry.getValue();
    		for(int i=0;i<listPairs.size();i++){
    			Iterator<Entry<String, List<CPair>>> it_2 = map_2.entrySet().iterator();
    	    	Entry<String, List<CPair>> entry_2 = null;
    	    	while(it_2.hasNext()){
        			HashMap<String,Integer> index_1=new HashMap<String,Integer>();
        			boolean check=true;
    	    		entry_2 = it_2.next();
    	    		List<CPair> listPairs_2 = entry_2.getValue();
    	    		for(int z=0;z<listPairs_2.size();z++){
    	    			String tmp=listPairs_2.get(z).key;
    	    			if(key.toString().equals(tmp.substring(tmp.indexOf(":")+1)))
    	    			{
    	    				for(int mm=0;mm<listPairs_2.get(z).values.size();mm++)
    	    				{
    	    					tmp=listPairs_2.get(z).values.get(mm);
        	    				index_1.put(tmp.substring(tmp.indexOf(":")+1),1);
    	    				}
    	    			}
    	    			else if(key.toString().equals(tmp.substring(0,tmp.lastIndexOf(":"))))
    	    			{
    	    				for(int mm=0;mm<listPairs_2.get(z).values.size();mm++)
    	    				{
    	    					tmp=listPairs_2.get(z).values.get(mm);
        	    				index_1.put(tmp.substring(0,tmp.lastIndexOf(":")),1);
    	    				}
    	    			}
    	    		}
    	    		for(int zz=0;zz<listPairs.get(i).values.size();zz++)
    				{
    					if(index_1.containsKey(listPairs.get(i).values.get(zz)))
    					{
    						
    					}
    					else
    					{
    						check=false;
    					}
    				}
    	    		if(check)
    	    		{
    	    			duplica.put(key.toString(), 1);
    	    		}
    	    		else
    	    		{}
    	    	}	
    		}
    	}
    	
    	
    	it = map.entrySet().iterator();
    	entry = null;
    	result = null;
    	while(it.hasNext()){
    		entry = it.next();
    		List<CPair> listPairs = entry.getValue();
    		for(int i=0;i<listPairs.size();i++){
    			for(int zz=0;zz<listPairs.get(i).values.size();zz++)
				{
    				if(duplica.containsKey(key.toString()))
        			{}
        			else
        			{
        				output.collect(new Text(key.toString()), new Text(listPairs.get(i).values.get(zz)));
        			}
				}
    		}
    				
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
