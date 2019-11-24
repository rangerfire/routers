package wang;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Stack;

public class ForTest {
	
	public static int[][] graph = new int[7][7];
	
	static void init()
	{
		graph[0][1] = 1;
		graph[1][0] = 1;
		
		graph[0][3] = 1;
		graph[3][0] = 1;
		
		graph[1][2] = 1;
		graph[2][1] = 1;
		
		graph[1][4] = 1;
		graph[4][1] = 1;
		
		graph[2][3] = 1;
		graph[3][2] = 1;
		
		graph[2][5] = 1;
		graph[5][2] = 1;
		
		graph[3][4] = 1;
		graph[4][3] = 1;
		
		graph[3][6] = 1;
		graph[6][3] = 1;
		
		graph[4][5] = 1;
		graph[5][4] = 1;
		
		graph[5][6] = 1;
		graph[6][5] = 1;

	}
	//每个路由器都有一个最短路径算法，来更新当前的路由表(参数值为该路由器的id)
	static void dijkstra(int start)
		{
			int len = graph.length;
			
			int index = start;
			
			int[] s = new int[len];			//记录所有未找到最短路径的点
			int[][] distance = graph;		//记录最短路径
			for(int i=0;i<len;i++)
				for(int j=0;j<len;j++)
					distance[i][j] = graph[i][j];
			int[] path = new int[len];		//记录前一个节点的序号（不是ID）
			
			for(int i=0;i<len;i++)
			{
				if(graph[index][i] > 0 )
					path[i] = index;
				else
					path[i] = -1;
			}
			
			s[index] = 1;		//先把开始节点加入s数组
			
			for(int i=0;i<len;i++)
			{
				int min = Integer.MAX_VALUE;
				int v = 0;
				for(int j=0;j<len;j++)
				{
					if( s[j]!=1 && index!=j && distance[index][j] != 0 && distance[index][j]<min )
					{
						min = distance[index][j];
						v = j;
					}
				}
				//目前v是到start最短的节点的序号
				s[v] = 1;
				for(int j=0;j<len;j++)
				{
					if( s[j] != 1 && distance[v][j]!=0 && (min + distance[v][j] < distance[index][j]  ||  distance[index][j] == 0) )
					{
						//说明加入这个节点之后找到了更短的路径
						distance[index][j] = min + distance[v][j];
						path[j] = v;
					}
				}
			}//endfor
			
			Stack<Integer> stack = new Stack<>();
			for(int i=0;i<len;i++)
			{
				if(distance[index][i] != 0)
				{
					System.out.println(start + "-->" + i + " 最短路径长度：" + distance[index][i]);
					System.out.print("逆序：");
					int ii = i;
					while(ii != -1)
					{
						System.out.print( ii + " ");
						stack.push(ii);
						ii  = path[ii];
						
					}
					System.out.println("");
					System.out.print("正序：");
					while(!stack.isEmpty())
					{
						System.out.print(stack.pop() + " ");
					}
					System.out.println("");
				}
			}
		}//end void
	
	
	public static void main(String[] args) throws ParseException
	{
//		 ArrayList<Character> carr = new ArrayList<Character>();
//		carr.add('a');
//		carr.add('b');
//		carr.add('c');
//		carr.add('d');
//		carr.add('e');
//		carr.add('f');
//		carr.add('a');
//		carr.add('b');
//		carr.add('1');
//		carr.add('2');
//		
//		Map<Character, Integer> map = new HashMap<Character, Integer>();
//		for(int i=0;i<carr.size();i++)
//		{
//			Character ch = new Character(carr.get(i));
//			if(map.containsKey(ch))
//				map.put(ch, map.get(ch) + 1);
//			else
//				map.put(ch, 1);
//		}	
//		
//		System.out.println(map.size());
//		System.out.println(map.get('a'));
//		System.out.println(map.entrySet());
//		System.out.println(map.entrySet());
//		int order = (int)(Math.log((double)2047)/Math.log((double)2));
//		int size = (int) Math.pow(2, (int)(Math.log((double)2048)/Math.log((double)2)) - order);
//		System.out.println(size);
//		System.out.println(16%2);
//		
//		
//		String s = "";
//		if(s.equals(""))
//			System.out.println("no");
//		else
////			System.out.println("yes");
//		char m = ' ';
//		
//		System.out.println( (m >=97 && m<=122) || (m >=65 && m<=90) || (m >=48 && m<=57) || m == 39 || m == 32);
//		
//		double temp = 97;
//		String ts = String.valueOf(temp);
//		String rev = "";
//		for(int i=0;i<5;i++)
//		{
//			if(i >= ts.length())
//				rev = rev + "0";
//			else
//				rev = rev + ts.charAt(i);
//		}
//		System.out.println(rev);
//		
//		String s = "MIC Cor;, Inc.";
//		String ss = s.replaceAll("\\,", "");
//		System.out.println(ss);
//		
//		String s = "   3 3333 2   ";
//		String[] ss = s.split("\\s+");
//		for(int i=0;i<ss.length;i++)
//		{
//			if(!ss[i].equals(""))
//				System.out.println(ss[i]);
//		}
//		
//		int[][] a = new int[10][3];
//		System.out.println(a.length);
		
//		init();
//		dijkstra(5);
		
		String day = "1";
		String month = "2";
		String year = "2008";
		String s = month + "/" + day + "/" + year;
		System.out.println(s);
	

		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		Date d = format.parse(s);
		String newd = format.format(d);
		System.out.println(newd);
		
	}
}
