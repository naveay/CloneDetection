package com.gen.stage1;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;
import com.main.main;
public class Map extends MapReduceBase implements Mapper<LongWritable, Text, Text, Text> {
	
	class kv{
		public String key=null,value=null;
		
		public kv(String key, String value){
			this.key = key;
			this.value = value;
		}
		
		public kv(String val){
			//T1:1
			String temp[]=val.split(":");
			this.key = temp[0];
			this.value = temp[1];
		}
	}
	
	Text txt = new Text();
  public void configure(JobConf conf){
	  //System.out.println("Job1 started");
	  //main.log+="Job1 started\n";
  }
  
  public void close(){
	  //System.out.println("Job1 Ended");
	  //main.log+="Job1 Ended\n";
  }

  public void map(LongWritable key, Text value, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {
    String line = value.toString();
    String _key=null,_value=null;
    int firstSpace = line.indexOf(' ');
    _key = line.substring(0, firstSpace);
    _value = line.substring(firstSpace+1);
    
    
    kv mapkey = new kv(_key), mapvalue = null;
    String nodes[] = _value.split(" ");
    for(String node:nodes){
    	mapvalue = new kv(node);
    	_key = mapkey.key+":"+mapvalue.key;
    	_value = mapkey.value +":"+ mapvalue.value;
    	output.collect(new Text(_key), new Text(_value));
    }
  }
}