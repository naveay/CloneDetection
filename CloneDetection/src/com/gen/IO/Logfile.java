package com.gen.IO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;

import com.gen.stage2.*;
import com.gen.stage2.Reduce.CPair;
import com.main.main;
public class Logfile {
	private String log;
	private String tmp_log;
	public void deletefile(String name)throws IOException
	{
		IOcontroller io=new IOcontroller();
		io.deleteHDFSFile(name);
	}
	public void copyfile(String source,String dest)  throws Exception
	{
		IOcontroller io=new IOcontroller();
		String value=new String(io.readHDFSFile(source),"UTF-8");
		io.createNewHDFSFile(dest, value);
	}
	public void mergefile(String source,String dest)  throws Exception
	{
		IOcontroller io=new IOcontroller();
		String value=new String(io.readHDFSFile(source),"UTF-8");
		String value2=new String(io.readHDFSFile(dest),"UTF-8");
		deletefile(dest);
		io.createNewHDFSFile(dest, value2+value);
	}
	public Logfile() throws IOException
	{
	}
	public void addLog(String content) throws Exception
	{	Configuration conf = new Configuration();
    	FileSystem fs = FileSystem.get(conf);
		Path path = new Path(tmp_log);
        if ( fs.exists(path) )
        {
        	IOcontroller io=new IOcontroller();
        	
        	
        	HashMap<String,List<String>> map = new HashMap<String,List<String>>();
        	List<String> list = null;
        	String value=new String(io.readHDFSFile(tmp_log),"UTF-8");
        	String[] line=value.split("\n");
        	for(int m=0;m<line.length;m++)
        	{
        		String[] array=line[m].split(" ");
            	String[] values=array[1].split(",");
            	for(int i=0;i<values.length;i++)
            	{
            		if(map.containsKey(array[0])){
            			list = map.get(array[0]);
            		}else{
            			list = new ArrayList<String>();
            			map.put(array[0], list);
            		}
            		list.add(values[i]);
            	}
        	}
        	if(content.isEmpty())
        	{
        		
        	}
        	else
        	{
        		line=content.split("\n");
            	for(int m=0;m<line.length;m++)
            	{
            		String[] array=line[m].split(" ");
                	String[] values=array[1].split(",");
                	for(int i=0;i<values.length;i++)
                	{
                		String key1 = array[0].substring(array[0].indexOf(":")+1);
                		if(map.containsKey(key1))
                		{
                			list = map.get(key1);
                			if(list.contains(values[i].substring(values[i].indexOf(":")+1)));
                				list.remove(values[i].substring(values[i].indexOf(":")+1));
                		}
                		String key2 = array[0].substring(0,array[0].lastIndexOf(":"));
                		if(map.containsKey(key2))
                		{
                			list = map.get(key2);
                			if(list.contains(values[i].substring(0,values[i].lastIndexOf(":"))));
                				list.remove(values[i].substring(0,values[i].lastIndexOf(":")));
                		}
                	}
            	}
        	}
        	
        	Iterator<Entry<String, List<String>>> it = map.entrySet().iterator();
        	Entry<String, List<String>> entry = null;
        	CPair result = null;
        	String output="";
        	while(it.hasNext()){
        		entry = it.next();
        		List<String> listPairs = entry.getValue();
        		if(listPairs.size()>0)
        		{
	        		output+=entry.getKey()+" ";
	        		for(int i=0;i<listPairs.size()-1;i++){
	        			output+=listPairs.get(i)+",";
	        		}
	        		output+=listPairs.get(listPairs.size()-1)+"\n";
        		}
        	}
        	String cur=new String(io.readHDFSFile(log),"UTF-8");
        	io.createNewHDFSFile(log, cur+output);
        	io.createNewHDFSFile(tmp_log, content);
        }
        else
        {
        	IOcontroller io=new IOcontroller();
        	io.createNewHDFSFile(tmp_log, content);
        	io.createNewHDFSFile(log, "");
        }
	}
	//public 
}
