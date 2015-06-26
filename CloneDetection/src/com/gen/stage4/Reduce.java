package com.gen.stage4;


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
      while(values.hasNext()){
    	  map.put(values.next().toString(), 0);
      }
      Iterator<Entry<String, Integer>> it = map.entrySet().iterator();
      while(it.hasNext())
      {
    	  String input=it.next().getKey();
          sb.append(delimiter);
          sb.append(input);
          delimiter = ",";
      }
      if(sb.indexOf(",")>0)
      {
          output.collect(key, new Text(sb.toString()));
      }
    }
  }
