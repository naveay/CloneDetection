package com.gen.stage8;


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
        HashMap<String,Integer> map_out = new HashMap<String,Integer>();
        while(values.hasNext()){
      	  map.put(values.next().toString(), 0);
        }
        Iterator<Entry<String, Integer>> it = map.entrySet().iterator();
        while(it.hasNext())
        {
      	    String input=it.next().getKey();
      	    Iterator<Entry<String, Integer>> it_1 = map.entrySet().iterator();
	      	while(it_1.hasNext())
	        {
	      		String input_2=it_1.next().getKey();
	      		if(input_2.compareTo(input)==0)
	      			continue;
	      		String[]array1=input.split(":");
	      		String[]array2=input_2.split(":");
	      		int num=0;
	      		for(int i=1;i<array1.length-1;i++)
	      		{
	      			if(array1[i].compareTo(array2[i])==0)	
	      			{
	      				num++;
	      			}
	      			else
	      			{
	      				//num=0;
	      			}
	      			
	      			if(num>=1)
		      		{
		      			break;
		      		}
	      		}
	      		if(num>=1)
	      		{
	      			map.put(input, 1);
	      			break;
	      		}
	        }
	      	if(map.get(input)==0)
	      	{
	            sb.append(delimiter);
	            sb.append(input);
	            delimiter = ",";
	      	}
        }
        if(sb.indexOf(",")>0)
        {
            output.collect(key, new Text(sb.toString()));
        }
    }
  }
