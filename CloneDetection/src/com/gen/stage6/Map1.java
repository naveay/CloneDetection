package com.gen.stage6;

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
public class Map1 extends MapReduceBase implements Mapper<LongWritable, Text, Text, Text> {
	
	Text txt = new Text();
  public void configure(JobConf conf){
	  //System.out.println("Job2 started");
	  //main.log+="Job2 started\n";
  }
  
  public void close(){
	  //System.out.println("Job2 Ended");
	  //main.log+="Job2 Ended\n";
  }

  public void map(LongWritable key, Text value, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {
	  
    String line = value.toString(); //T1:T2	1:2,5:6
    String _key=null,_value=null;
    int firstSpace = line.indexOf("\t"); 
    _key = line.substring(0, firstSpace);// T1:T2
    _value = line.substring(firstSpace+1);// 1:2,5:6
    output.collect(new Text(_key), new Text(line.replace('\t', ',')));
  }
}