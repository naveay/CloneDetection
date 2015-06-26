package com.gen.IO;
import java.io.File;
import java.io.IOException;

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

import com.main.main;

public class IOcontroller {

	public static void createNewHDFSFile(String toCreateFilePath, String content) throws IOException
    {
        Configuration config = new Configuration();
        FileSystem hdfs = FileSystem.get(config);
        
        FSDataOutputStream os = hdfs.create(new Path(toCreateFilePath));

        os.write(content.getBytes("UTF-8"));
        
        os.close();
        
        hdfs.close();
    }
    public static boolean deleteHDFSFile(String dst) throws IOException
    {
        Configuration config = new Configuration();
        FileSystem hdfs = FileSystem.get(config);
        Path path = new Path(dst);
        boolean isDeleted = hdfs.delete(path);
        hdfs.close();
        return isDeleted;
    }
    public static byte[] readHDFSFile(String dst) throws Exception
    {
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(conf);
        
        // check if the file exists
        Path path = new Path(dst);
        if ( fs.exists(path) )
        {
            FSDataInputStream is = fs.open(path);
            // get the file info to create the buffer
            FileStatus stat = fs.getFileStatus(path);
            
            // create the buffer
            byte[] buffer = new byte[Integer.parseInt(String.valueOf(stat.getLen()))];
            is.readFully(0, buffer);
            
            is.close();
            fs.close();
            
            return buffer;
        }
        else
        {
            throw new Exception("the file is not found .");
        }
    }
}
