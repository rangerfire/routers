package wang;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class SortedSet {
	
	public static ArrayList<Integer> data = new ArrayList<Integer>();
	public static stNode root = null;
	
	public static void read() throws Exception
	{
		try
		{
		File f = new File("infile.dat");
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);
		String line = "";
		while( ( line = br.readLine() ) != null)
		{
			String[] str = line.split(",");
			for(int i=0;i<str.length;i++)
			{
				String t = str[i];
				int temp = Integer.parseInt(t);
				data.add(temp);
			}
		}
		System.out.print("Sorted Set A Contains ");
		for(int i=0;i<data.size();i++)
		{
			System.out.print(data.get(i));
			if(i != (data.size()-1))
				System.out.print(",");
		}
		System.out.println("");
		}
		catch(Exception e)
		{
			System.out.println("Pleas do not type blank between your number and comma!");
		}
	}
	
	public static void build()
	{
		if(data.size() > 0)
		{
			int rootn = data.get(0);
			root = new stNode(rootn,null,null,null);
			if(data.size() > 1)
			{
				for(int i=1;i<data.size();i++)
				{
					int a = data.get(i);
					add(root,new stNode(a, null, null, null));
				}
			}
		}
	}
	
	public static boolean isEmpty()
	{
		if(root == null)
			return true;
		return false;
			
	}
	
	public static stNode add(stNode root, stNode n)
	{
		if(root == null)
			root = n;
		else
			if(n.value < root.value)
				root.leftchild = add(root.leftchild, n);
			else
				if(n.value > root.value)
					root.rightchild = add(root.rightchild, n);
		return root;
				
	}
	
	public static String remove(stNode root, stNode n)
	{
		if(root == null)
			return "false";
		stNode temp = null;
		while(root != null)
		{
			if(n.value < root.value)
			{
				temp = root;
				root = root.leftchild;
			}
			else
				if(n.value > root.value)
				{
					temp = root;
					root = root.rightchild;
				}
				else
				{
					//leaf
					if(root.leftchild == null && root.leftchild == null)
					{
						if(temp == null)
							root = null;
						else
						{
							if(temp.leftchild == root)
								temp.leftchild = null;
							else
								if(temp.rightchild == root)
									temp.rightchild = null;
						}
					}
					//only left
					else if(root.leftchild != null && root.leftchild == null)
					{
						if(temp == null)
							root = root.leftchild;
						else
							if(temp.leftchild == root)
								temp.leftchild = root.leftchild;
							else if(temp.rightchild == root)
								temp.rightchild = root.leftchild;		
					}
					//right only
					else if(root.leftchild == null && root.leftchild != null)
					{
						if(temp == null)
							root = root.rightchild;
						else
							if(temp.leftchild == root)
								temp.leftchild = root.rightchild;
							else if(temp.rightchild == root)
								temp.rightchild = root.rightchild;	
					}
					//both
					else 
					{
						stNode min = root.rightchild;
						stNode minfather = null;
						while(min != null)
						{
							minfather = min;
							min = min.leftchild;
						}
						int tvalue = root.value;
						root.value = min.value;
						min.value = tvalue;
						if(minfather.leftchild == min)
							minfather.leftchild = min.rightchild;
						else if(minfather.rightchild == min)
							minfather.rightchild = min.rightchild;
					}
				}			
			break;
		}
		return "finish";
		
	}
	
	public static stNode contains(stNode root, int value)
	{
		if(root == null)
			return root;
		if(value < root.value)
			return contains(root.leftchild, value);
		else if(value > root.value)
			return contains(root.rightchild, value);
		return root;
	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		read();
		build();
		System.out.println("Please input a number:");
		Scanner sc = new Scanner(System.in);
		String s = sc.nextLine();
		int t = Integer.parseInt(s);
		if(contains(root, t) != null)
			System.out.println("Yes");
		else
			System.out.println("No");
	}

}


class stNode
{
	int value;
	stNode parent;   		
	stNode leftchild;		
	stNode rightchild;		
	//constructor
	public stNode(int data, stNode p, stNode l, stNode r)
	{
		this.value = data;
		this.parent = p;
		this.leftchild = l;
		this.rightchild = r;
	}
}
