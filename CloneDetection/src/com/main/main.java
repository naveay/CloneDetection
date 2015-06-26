package com.main;

import java.io.IOException;

import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.BufferedWriter;
import java.io.FileWriter;

import com.gen.stage1.Task1Conf;
import com.gen.stage2.Task2Conf;
import com.gen.stage3.Task3Conf;
import com.gen.stage4.Task4Conf;
import com.gen.stage5.Task5Conf;
import com.gen.stage6.Task6Conf;
import com.gen.stage7.Task7Conf;
import com.gen.stage8.Task8Conf;
import com.gen.stage3.LOOPDETECT;
import com.gen.test.GenerateData;
import com.gen.IO.IOcontroller;
import com.gen.IO.Logfile;
public class main {
	public static String log="";
	private static int test=10;
	private static String path="testdata/";
	public static boolean nextiter=false;
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		int mode=1;
		/*
		 * 0 pure clone detection without any other function.
		 * 1 full functional parts excludes loops deletion.
		 */
		int io_mode=0;
		String filename="part-00000";
		/*
		 * 0 MR to write file
		 * 1 HDFS IO input
		 */
		/*GenerateData gen=new GenerateData();
		try {
			//gen.generatedata(path,test);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

		Logfile logfile=new Logfile();
		for(int i=2;i<=2;i++)
		{
			log="";
			JobConf conf = null;
			boolean initial=false;
			
			try {
				
				conf = Task1Conf.getConf(path+i+"/input/", "tempout");
				JobClient.runJob(conf);
				
				conf = Task3Conf.getConf("tempout", "out");
				JobClient.runJob(conf);
				
				if(mode==0)
				{
					conf = Task8Conf.getConf("out", "clone/global");
					JobClient.runJob(conf);
					
					conf = Task8Conf.getConf("clone/global", "clone/prev");
					JobClient.runJob(conf);
				}
				else
				{

					conf = Task8Conf.getConf("out", "clone/prev");
					JobClient.runJob(conf);

				}
				while(nextiter)
				{
				    nextiter=false;
					conf = Task2Conf.getConf("clone/prev", "tempout");
					JobClient.runJob(conf);
					
					conf = Task3Conf.getConf("tempout", "out");
					JobClient.runJob(conf);
					
					conf = Task8Conf.getConf("out", "clone/current");
					JobClient.runJob(conf);
					
					if(mode==0)
					{
						if(io_mode==0)
						{
							conf = Task5Conf.getConf("clone/global","clone/current","clone/tmp");
							JobClient.runJob(conf);
							conf = Task4Conf.getConf("clone/tmp", "clone/global");
							JobClient.runJob(conf);
						}
						else
						{
							conf = Task5Conf.getConf("clone/global","clone/current","clone/tmp");
							JobClient.runJob(conf);
							logfile.copyfile("clone/tmp/"+filename, "clone/global/"+filename);
						}
					}
					else
					{
						conf = Task6Conf.getConf("clone/prev","clone/current","clone/result");
						JobClient.runJob(conf);
						
						conf = Task8Conf.getConf("out", "clone/prev");
						JobClient.runJob(conf);
						
						if(!initial)
						{
							conf = Task4Conf.getConf("clone/result", "clone/global");
							JobClient.runJob(conf);
							initial=true;
						}
						else
						{
							//conf = Task7Conf.getConf("clone/result","clone/tmp_2");

							//JobClient.runJob(conf);
							
							if(io_mode==0)
							{
								conf = Task5Conf.getConf("clone/global","clone/result","clone/tmp");
								JobClient.runJob(conf);
								conf = Task4Conf.getConf("clone/tmp", "clone/global");
								JobClient.runJob(conf);
							}
							else
							{
								conf = Task5Conf.getConf("clone/global","clone/result","clone/tmp");
								JobClient.runJob(conf);
								logfile.copyfile("clone/tmp/"+filename, "clone/global/"+filename);
							}
						}
					}
				}
				
				//output.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(log);
		}
	}

}
