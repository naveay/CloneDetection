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

import com.gen.stage2.Reduce.kv;
import com.main.main;
public class Map2 extends MapReduceBase implements Mapper<LongWritable, Text, Text, Text> {
	
class Pair{
	
	public String key, value;
	
	public Pair(String k,String v){
		this.key = k;
		this.value = v;
	}
}
	
class keyValue{
	
	public String restOfArray(String str[]){
		StringBuffer sb = new StringBuffer();
		String delimiter="";
		for(int i=1;i<str.length-1;i++){
			sb.append(delimiter);
			sb.append(str[i]);
			delimiter=":";
		}
		return sb.toString();
	}
	
	public Pair[] generateKV(String key,String value){
		Pair[] pairs = new Pair[2];
		
		String temp[] = key.split(":"),middle=null; // {T1,T2,..,Tn}
		String key1 = key.substring(key.indexOf(":")+1);
		String key2 = key.substring(0,key.lastIndexOf(":"));
		
		pairs[0] = new Pair(key1, value);
		pairs[1] = new Pair(key2, value);
		return pairs;
	}
	
}
	
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
    Pair[] pairs = (new keyValue()).generateKV(_key,_value);
    for(Pair kv: pairs){
    	output.collect(new Text(kv.key), new Text(line.replace('\t', ',')));
    }
  }
}