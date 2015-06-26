package com.gen.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class GenerateData {
	private String path="testdata/";
	private int[][] graph;
	private int[] typegraph;
	private void initialGraph(int num)
	{
		graph=new int[num][num];
		typegraph=new int[num];
	}
	public void generatedata(String path,int num) throws IOException
	{
		for(int i=1;i<=num;i++)
		{
			generateGraph(i*3,i+1,3+num/5,3+num/5);
			outputdata(path+i+"/input/",i+".txt");
			graph=null;
			typegraph=null;
		}
	}
	public void outputdata(String path,String name) throws IOException
	{
		File dir=new File(path);
		dir.mkdirs();
		dir=new File(path+name);
		dir.createNewFile();
		BufferedWriter output=new BufferedWriter(new FileWriter(dir));
		for(int i=0;i<graph[0].length;i++)
		{
			for(int m=0;m<graph[0].length;m++)
			{
				if(graph[i][m]==1)
				{
					String str="T"+typegraph[i]+":"+(i+1)+" "+"T"+typegraph[m]+":"+(m+1);
					output.write(str+"\n");
				}
			}
		}
		output.close();
	}
	public void generateGraph(int nodenum,int typenum, int nodeindegree, int nodeoutdegree)
	{
		initialGraph(nodenum);
		Random randoutdegree=new Random();
		Random column=new Random();
		Random type=new Random();
		for(int i=0;i<nodenum;i++)
		{
			int type_num=type.nextInt(typenum)+1;
			typegraph[i]=type_num;
			int out=randoutdegree.nextInt(nodeoutdegree)+1;
			for(;out>0;out--)
			{
				graph[i][column.nextInt(nodenum)]=1;
			}
		}
		for(int i=0;i<nodenum;i++)
		{
			int sum=0;
			for(int m=0;m<nodenum;m++)
			{
				if(graph[m][i]==1)
					sum++;
				if(sum>nodeindegree)
					graph[m][i]=0;
			}
		}
	}
}
