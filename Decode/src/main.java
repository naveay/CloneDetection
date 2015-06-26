import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
public class main {
	public static void main(String[]args) throws IOException
	{
		BufferedReader br=new BufferedReader(new FileReader("map_id_knnd_syntax_charname.txt"));
		BufferedReader map_br=new BufferedReader(new FileReader("map.txt"));
		BufferedReader result=new BufferedReader(new FileReader("part-00000"));
		BufferedWriter output=new BufferedWriter(new FileWriter("out.txt"));
		HashMap<String,String> map = new HashMap<String,String>();
		HashMap<String,String> map_word = new HashMap<String,String>();
		String sCurrentLine;
		while ((sCurrentLine = map_br.readLine()) != null) {
			map.put(sCurrentLine.split("\t")[1], sCurrentLine.split("\t")[0]);
		}
		map_br.close();
		
		while ((sCurrentLine = br.readLine()) != null) {
			String key=sCurrentLine.split(":")[1];
			sCurrentLine = br.readLine();
			while(sCurrentLine!=null&&sCurrentLine.split(":").length>1&&sCurrentLine.split(":")[0]=="ID")
			{
				map_word.put(key, "");
				key=sCurrentLine.split(":")[1];
				sCurrentLine = br.readLine();
			}

			if(sCurrentLine==null)
			{
				map_word.put(key, "");
				break;
			}
			else
			{
				map_word.put(key, sCurrentLine);
			}
		}
		br.close();
		/*
		int initial=1;
		String sCurrentLine;
		while ((sCurrentLine = br.readLine()) != null) {
			String nodes[] = sCurrentLine.split(" ");
			String node1[] = nodes[0].split(":");
			String node2[] = nodes[1].split(":");
		    if(!map.containsKey(node1[1]))
		    {
		    	map.put(node1[1], initial);
		    	mapfile.write(node1[1]+"\t"+ initial+"\n");
		    	initial++;
		    }
		    if(!map.containsKey(node2[1]))
		    {
		    	map.put(node2[1], initial);
		    	mapfile.write(node2[1]+"\t"+ initial+"\n");
		    	initial++;
		    }
		    output.write("T"+node1[0]+":"+map.get(node1[1])+" "+"T"+node2[0]+":"+map.get(node2[1])+"\n");
		}
		mapfile.close();
		output.close();
		br.close()
		*/
	}
}
