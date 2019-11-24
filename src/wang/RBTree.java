package wang;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.TreeMap;
//The implementation of TreeMap is the red-black tree data structure	

public class RBTree {
	
	public static TreeMap<String, String> sorted_dic;
	
	public static void startBuild(String fname) throws Exception
	{
		sorted_dic = new TreeMap<>();
		File f = new File(fname);
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);
		String line = "";
		line = br.readLine(); 	//SKIP THE FIRST LINE
		while( ( line = br.readLine() ) != null)
		{
			int index = 0;
			for(int i=0;i<line.length();i++)
				if(line.charAt(i) != ' ')
				{
					index = i;
					break;
				}
			String temp = line.substring(index, line.length());
			String ss[] = temp.split("\\s++");
			String key = ss[0];
			String value = ss[1];
//			System.out.println(key);
//			System.out.println(value);
			insert(key,value);
		}
	}
	//insert key/value pairs
	public static void insert(String key, String value)
	{
		sorted_dic.put(key, value);
	}
	//retrieve values by keys
	public static String retrieve(String key)
	{
		return sorted_dic.get(key);
	}
	//delete keys
	public static void delete(String key)
	{
		sorted_dic.remove(key);
	}
	//inform whether or not a key exists in the dictionary
	public static boolean isExist(String key)
	{
		return sorted_dic.containsKey(key);
	}
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		//System.out.println(isExist("222"));
		//System.out.println(isExist("hello"));
		startBuild("key_value_data.dat");
		System.out.println(retrieve("gpa"));
		System.out.println(retrieve("department"));
	
		
	}

}
