package com.gen.stage6;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;
import org.apache.hadoop.mapred.lib.MultipleInputs;

import com.main.main;

public class Task6Conf {
	public static void deletedir(String outputpath){
		try{
			FileUtils.deleteDirectory(new File(outputpath));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static JobConf getConf(String inputpath,String inputpath2, String outputpath){
		Task6Conf.deletedir(outputpath);
		JobConf conf = new JobConf(main.class);

      conf.setOutputKeyClass(Text.class);
      conf.setOutputValueClass(Text.class);

      //conf.setMapperClass(Map.class);
      conf.setReducerClass(Reduce.class);

      //conf.setInputFormat(TextInputFormat.class);
      conf.setOutputFormat(TextOutputFormat.class);

      MultipleInputs.addInputPath(conf, new Path(inputpath), TextInputFormat.class,Map1.class);
      //FileInputFormat.setInputPaths(conf, new Path(inputpath));
      MultipleInputs.addInputPath(conf, new Path(inputpath2), TextInputFormat.class,Map2.class);
      FileOutputFormat.setOutputPath(conf, new Path(outputpath));
      try {
//		JobClient.runJob(conf);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
      return conf;
	}
}
