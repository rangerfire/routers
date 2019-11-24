package wang;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class TrieArticle {

	public static int index;							//index in the child nodes
	public static TNode root;
	public static ArrayList<ArrayList<String>> names;
	public static ArrayList<Integer> times;				//hit time
	public static ArrayList<Integer> Wtimes;			//word time
	public static int wholenumber;
	public static int index2;							//index of the main string
	public static int count;
	public static String temp;
	
	public static void init()
	{
		index = 0;
		root = new TNode();
		names = new ArrayList<>();
		times = new ArrayList<>();
		Wtimes = new ArrayList<>();
		wholenumber = 0;
		index2 = 0;
		count = 0;
		temp = "";
	}
	
	public static void readArt(String fname) throws Exception
	{
		File f = new File(fname);
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);
		String line = "";
		String whole = "";
		while( ( line = br.readLine() ) != null)
		{
			whole = whole + line;
		}
		
		//把whole中的特殊符号都去掉
		ArrayList<Character> tc = new ArrayList<>();
		for(int j=0;j<whole.length();j++)
		{
			char cc = whole.charAt(j); 
			if(!isAva(cc))
				tc.add(cc);
		}
		for(int k=0;k<tc.size();k++)
		{
			String rex = "\\" + tc.get(k);
//			System.out.println(rex);
			whole = whole.replaceAll(rex, "");
		}
		whole = whole + ".";
//		System.out.println(whole);
		statistics(whole);
		
//		System.out.println(whole);
	}
	
	public static void statistics(String s)
	{
		index2 = 0;
		char[] chs = s.toCharArray();
		while(index2 < chs.length)
		{
//			System.out.println(chs[index2] + " ," + index2);
			//注意每次从开头查
			Search(chs[index2], s);
			temp = "";
		}	
		
	}
	
	public static void Search(char c, String s)
	{
		Search1(root,c, s);
	}
	
	//一个字符是不是字母或数字或'     是=true
	public static boolean isDC(char m)
	{
		if((m >=97 && m<=122) || (m >=65 && m<=90) || (m >=48 && m<=57) || m==39 || m ==95)
			return true;
		else
			return false;
	}
	
	public static void Search1(TNode n, char c, String s)
	{
		//if n has this child
		if(isExist(n, c))
		{
			temp = temp + c;
			n = n.child.get(index);
			index2++;
			char nextc = s.charAt(index2);
			//如果是叶子节点并且本单词结束，才能算找到(下一个单词不是字母或数字)
			if(n.isLeaf && !isDC(nextc) )
			{
				int idn = n.id;
				times.set(idn, times.get(idn) + 1);
//				System.out.println(temp);
				wholenumber++;
				//end of Search1, find next pre char
				if(index2 >= s.length())
					return;
				else
				{
					for(int i=index2;i<s.length();i++)
						if( isDC( s.charAt(i) )   )
						{
							index2 = i;
							break;
						}
					return;
				}
				
			}
			Search1(n, nextc,s);
		}
		//if do not have this child
		else 
		{
			for(int i=index2;i<s.length();i++)
			{
				
				if( isDC( s.charAt(i) ) )
				{
					temp = temp + s.charAt(i);
				}
				else
				{
					index2 = i;
					break;
				}
			}
			//如果temp的长度大于0，那说明又找到一个单词，要判断这个单词是否是那几个词，是的话忽略，不是的话总词数+1
			if(temp.length()>0)
			{
				
				if(temp.equals("a") || temp.equals("an") || temp.equals("the") || temp.equals("and") || temp.equals("or") || temp.equals("but")
						|| temp.equals("A") || temp.equals("An") || temp.equals("The") || temp.equals("And") || temp.equals("Or") || temp.equals("But"))
					wholenumber = wholenumber + 0;
				else
				{
//					System.out.println(temp);
					wholenumber++;
				}
			}
			//find the next pre char
			index2++;
			if(index2 >= s.length())
				return;
			else
			{
				for(int i=index2; i<s.length();i++)
					if( isDC( s.charAt(i) )   )
					{
						index2 = i;
						break;
					}
				return;
			}
		}
		
	}
	
	public static void readComp(String fname) throws IOException
	{
		File f = new File(fname);
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);
		String line = "";
		int idnumber = 0;
		while( ( line = br.readLine() ) != null)
		{
			//id number is the same
			ArrayList<String> nn = new ArrayList<>();
			String[] ss = line.split("\t");
			for(int i=0;i<ss.length;i++)
			{
				nn.add(ss[i]);
				//去掉符号
//				String t1 = ss[i];
//				String t2 = t1.replaceAll(",", "");
//				String t3 = t2.replaceAll("\\.", "");
				
				ArrayList<Character> tc = new ArrayList<>();
				for(int j=0;j<ss[i].length();j++)
				{
					char cc = ss[i].charAt(j); 
					if(!isAva(cc))
						tc.add(cc);
				}
				for(int k=0;k<tc.size();k++)
				{
					String rex = "\\" + tc.get(k);
//					System.out.println(rex);
					ss[i] = ss[i].replaceAll(rex, "");
				}
		
				//insert each
				insert(ss[i],idnumber);	
			}
			names.add(nn);
			times.add(0);
			idnumber ++ ;
		}
	}
	
	public static boolean isAva(char m)
	{
		if((m >=97 && m<=122) || (m >=65 && m<=90) || (m >=48 && m<=57) || m==39 || m == 32 || m == 95)
			return true;
		else
			return false;
	}
	
	public static void insert(String s, int id)
	{
		insert(root, s, id);
	}
	
	public static void insert(TNode n, String s, int id)
	{
		char[] c = s.toCharArray();
		for(int i=0;i<c.length;i++)
		{
			//if node n has this child
			if(isExist(n, c[i]))
			{
				n.child.get(index).prenum++;
				if(i == c.length-1)
				{
					n.child.get(index).isLeaf = true;
					n.child.get(index).cfnum++;
					n.child.get(index).id = id;
					
				}
				n = n.child.get(index);
			}
			else
			{
				TNode nc = new TNode();
				nc.c = c[i];
				n.child.add(nc);
				n.child.get(n.child.size()-1).prenum++;
				if(i == c.length-1)
				{
					n.child.get(n.child.size()-1).isLeaf = true;
					n.child.get(n.child.size()-1).cfnum++;
					n.child.get(n.child.size()-1).id = id;
			
				}
				n = n.child.get(n.child.size()-1);
			}
		}
	}
	
	//search if nr has ch child
	public static boolean isExist(TNode nr, char ch)
	{
		for(int i=0;i<nr.child.size();i++)
		{
			if(ch == nr.child.get(i).c)
			{
				index = i;
				return true;
			}
		}
		return false;
	}
	
	//for test(not use)
	public static boolean isExist(String ss)
	{
		return search(root, ss);
	}
	public static boolean search(TNode n, String ss)
	{
		char[] cc = ss.toCharArray();
		for(int i=0;i<cc.length;i++)
		{
			if(!isExist(n, cc[i]))
				return false;
			else
				if(i == cc.length-1 && !n.child.get(index).isLeaf)
					return false;
			n = n.child.get(index);
		}
		return true;
	}
	
	public static int output_pre()
	{
		int maxl = 15;
		for(int i=0;i<times.size();i++)
		{
			//find the longest name
			if(maxl < names.get(i).get(0).length())
				maxl = names.get(i).get(0).length();
		}
		return maxl;
	}
	
	public static void output()
	{
		System.out.println("Output:");
		int maxlen = output_pre();
		int hlen = 1 + maxlen + 1 + maxlen + 1 + maxlen + 1;
		//top
		for(int i=0;i<hlen;i++)
			System.out.print("-");
		System.out.println("");
		//first line
		System.out.print("|Company");
		for(int i=0;i<maxlen-7;i++)
			System.out.print(" ");
		System.out.print("|Hit Count");
		for(int i=0;i<maxlen-9;i++)
			System.out.print(" ");
		System.out.print("|Relevance");
		for(int i=0;i<maxlen-9;i++)
			System.out.print(" ");
		System.out.println("|");
		for(int i=0;i<hlen;i++)
			System.out.print("-");
		System.out.println("");
		
		int totalhc = 0;
		double totalre = 0;
		for(int i=0;i<times.size();i++)
		{		
			System.out.print("|" + names.get(i).get(0));
			for(int q=0;q<maxlen-names.get(i).get(0).length();q++)
				System.out.print(" ");
			
			System.out.print( "|" + times.get(i));
			for(int q=0;q<maxlen-String.valueOf(times.get(i)).length();q++)
				System.out.print(" ");
			
			totalhc = totalhc + times.get(i);
			
			//relevance
			double temp =100.0 * (double)times.get(i)/(double)wholenumber;
			totalre = totalre + temp;
			String ts = String.valueOf(temp);
			String rev = "";
			for(int j=0;j<5;j++)
			{
				if(j >= ts.length())
					rev = rev + "0";
				else
					rev = rev + ts.charAt(j);
			}
			rev = rev + "%";
			System.out.print("|" + rev);
			for(int q=0;q<maxlen-rev.length();q++)
				System.out.print(" ");
			System.out.println("|");
			
			for(int q=0;q<hlen;q++)
				System.out.print("-");
			System.out.println("");
		}
		
		//relevance
		double temp =100.0 * (double)totalhc/(double)wholenumber;
		String ts = String.valueOf(temp);
		String rev = "";
		for(int j=0;j<5;j++)
		{
			if(j >= ts.length())
				rev = rev + "0";
			else
				rev = rev + ts.charAt(j);
		}	
		rev = rev + "%";
		System.out.print("|Total");
		for(int q=0;q<maxlen-5;q++)
			System.out.print(" ");
		
		System.out.print("|" + totalhc);
		for(int q=0;q<maxlen-String.valueOf(totalhc).length();q++)
			System.out.print(" ");
		
		System.out.print("|" + rev);
		for(int q=0;q<maxlen-rev.length();q++)
			System.out.print(" ");
		System.out.println("|");
		
		for(int i=0;i<hlen;i++)
			System.out.print("-");
		System.out.println("");
		
		int cou = 12;
		System.out.print("|Total Words");
		for(int i=0;i<=hlen/2-11-2;i++)
		{
			System.out.print(" ");
			cou++;
		}
		System.out.print("|" + wholenumber);
		cou = cou + 1 + String.valueOf(wholenumber).length();
		for(int i=0;i<=hlen;i++)
		{
			if(cou<hlen-1)
			{
				System.out.print(" ");
				cou++;
			}
			else
				break;
		}
			
		System.out.println("|");
		
		//bottle
		for(int i=0;i<hlen;i++)
			System.out.print("-");
	}
	
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		init();
		
		System.out.println("Please input company names's file name:");
		Scanner sc = new Scanner(System.in);
		String line = sc.nextLine();
		readComp(line);
		
		System.out.println("Please input news article's file name:");
		sc = new Scanner(System.in);
		line = sc.nextLine();
		readArt(line);
		
		output();
		
//		System.out.println(isExist("Microsoft"));
//		System.out.println(isExist("Mic Cor Inc"));
//		System.out.println(isExist("Microso"));
//		System.out.println(isExist("Microsof"));
//		System.out.println(isExist("Microsoft1"));
//		System.out.println(isExist("Microsoft1123"));
	}

}
 

class TNode
{
	char c;
	int cfnum;
	int prenum;
	ArrayList<TNode> child;
	boolean isLeaf;
	//用于统计频率
	int id;
	public TNode()
	{
		char c = 0;
		this.cfnum = 0;
		this.prenum = 0;
		this.child = new ArrayList<>();
		this.isLeaf = false;
		this.id = 0;
	}
	
}