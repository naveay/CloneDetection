package com.gen.stage7;


import java.io.IOException;
import java.util.Iterator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

import com.main.main;

public class Reduce extends MapReduceBase implements Reducer<Text, Text, Text, Text> {
    public void reduce(Text key, Iterator<Text> values, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {
      StringBuffer sb = new StringBuffer();
      String delimiter = "";
      HashMap<String,Integer> map = new HashMap<String,Integer>();
      HashMap<String,Integer> map2 = new HashMap<String,Integer>();
      while(values.hasNext()){
    	  map.put(values.next().toString(), 0);
      }
      Iterator<Entry<String, Integer>> it = map.entrySet().iterator();
      while(it.hasNext())
      {
    	  String input=it.next().getKey();

    	  int firstSpace = input.indexOf(" "); 
    	  String _key = input.substring(0, firstSpace); // T1:T2
    	  String _value = input.substring(firstSpace+1);// 1:2,...
    	  String value=_value.split(",")[0];
    	  HashMap<String,Integer> map3 = new HashMap<String,Integer>();
  		  String temp[] = value.split(":");
  		  int num=0;
  		  boolean skip=false;
  		  for(int i=0;i<temp.length;i++)
  		  {
  			if(!map3.containsKey(temp[i]))
  				map3.put(temp[i], i);
  			else
  			{
  				if(i==temp.length-1||map3.get(temp[i])==0)
  				{
  					if(i==temp.length-1)
  					{
  						int firstSpace1 = value.lastIndexOf(temp[i]);
  						value = value.substring(0,firstSpace1-1);// 1:2
  					}
  					else
  					{
  						int firstSpace1 = value.indexOf(temp[i]);
  						value = value.substring(firstSpace1+temp[i].length()+1);// 1:2
  					}
  				}
  				else
  				{
  					skip=true;
  				}
  			}
  		  }
  		  if(skip)
  		  {
  			output.collect(new Text(_key), new Text(input.substring(firstSpace+1)));
  			map.remove(input);
  			it = map.entrySet().iterator();
  			continue;
  		  }
    	  String double_value=value+":"+value;
    	  if(map.size()==1)
    	  {
    		  output.collect(new Text(_key), new Text(input.substring(firstSpace+1)));
    	  }
    	  else
    	  {
    		  boolean dup=false;
	    	  while(it.hasNext())
	          {
	    		  String input2=it.next().getKey();
	    		  int firstSpace2 = input2.indexOf(" "); 
	        	  String _value2 = input2.substring(firstSpace2+1);// 1:2
	        	  //---------
	        	  String [] array=_value2.split(",");
	        	  //for(int i =0;i<)
	        	  if(double_value.contains(_value2))
	        	  {
	        		  dup=true;
	        	  }
	        	  else
	        	  {
	        		  
	        	  }
	          }
	    	  if(!dup)
	    		  output.collect(new Text(_key), new Text(input.substring(firstSpace+1)));
    	  }
    	  map.remove(input);
		  it = map.entrySet().iterator();
      }
    }
  }
