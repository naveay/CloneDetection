package com.gen.stage3;
import java.util.HashMap;
public class LOOPDETECT {
	public static boolean loopcheck(String str)
	{
		HashMap<String,Integer> map = new HashMap<String,Integer>();
		String temp[] = str.split(":");
		int num=0;
		for(int i=0;i<temp.length;i++)
		{
			if(!map.containsKey(temp[i]))
				map.put(temp[i], 1);
			else
			{
				num++;
				if(num>=2)
					return false;
			}
		}
		return true;
	}
}
