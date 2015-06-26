package com.gen.stage1;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

public class Reduce extends MapReduceBase implements Reducer<Text, Text, Text, Text> {
    public void reduce(Text key, Iterator<Text> values, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {
      StringBuffer sb = new StringBuffer();
      String delimiter = "";
      while(values.hasNext()){
    	  sb.append(delimiter);
    	  sb.append(values.next());
    	  delimiter = ",";
      }
      //System.out.println(key+" "+sb.toString());
      output.collect(key, new Text(sb.toString()));
    }
  }
