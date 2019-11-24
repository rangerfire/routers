package wang;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

//最大值：Integer.MAX_VALUE

public class LinkStateRouting {

	public static ArrayList<router> routers = new ArrayList<>();
	public static int max = Integer.MAX_VALUE;
	
	public static void read() throws Exception  
	{
		String filename = "infile.dat";
		File f= new File(filename);
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);
		String line = "";
		
		int count = 0;
		router r;
		
		while( (line = br.readLine()) != null )
		{
			String[] ss = line.split("\\s++");

			for(int i=0;i<ss.length;i++)
			{
				//对于每个ss[i]
				String temp = ss[i];
				for(int j=0;j<temp.length();j++)
				{
					//如果发现不是数字的元素，则说明新开了一个路由器
					if(temp.charAt(j)<48 || temp.charAt(j)>57)
					{
						count = 1;
						r = new router();
						routers.add(r);
						break;
					}

				}
			}
			
			//如果这一行数据是路由器本身
			if(count == 1)
			{
				
					if(!ss[0].equals(""))
					{
						routers.get( routers.size()-1 ).id = Integer.parseInt( ss[0] );
						routers.get( routers.size()-1 ).network = ss[1];
					}
					else
					{
						routers.get( routers.size()-1 ).id = Integer.parseInt( ss[1] );
						routers.get( routers.size()-1 ).network = ss[2];
					}
								
			}
			//如果这一行数据是路由器的连接项，那么要记录直接相连的路由器，并且建立初步的graph
			else
			{
					if(!ss[0].equals(""))
					{
						routers.get( routers.size()-1 ).connected_id.add( Integer.parseInt( ss[0] ) );
						if(ss.length>1)
							routers.get( routers.size()-1 ).connected_cost.add( Integer.parseInt( ss[1] )  );
						else
							routers.get( routers.size()-1 ).connected_cost.add( 1  );
					}
					else
					{
						routers.get( routers.size()-1 ).connected_id.add( Integer.parseInt( ss[1] ) );
						if(ss.length>2)
							routers.get( routers.size()-1 ).connected_cost.add( Integer.parseInt( ss[2] ) );
						else
							routers.get( routers.size()-1 ).connected_cost.add( 1 );
					}
						
			}
			count = 0;
		}					
	}
	
	/*  在read之后，各个路由器的基本信息已经确定了， 下面建立每个路由器初始的网络图  */
	public static void build_first_graph()
	{
		//对于每一个router
		for(int i=0;i<routers.size();i++)
		{
			//先初始化graph数组
			routers.get(i).buildgraph(routers.size());
			//然后根据直接连接的信息建立图
			for(int j=0;j<routers.get(i).connected_id.size();j++)
			{
				//先找到这个Id所对应的序号
				int index = findIndex( routers.get(i).connected_id.get(j) );
				//两个方向都需要赋值
				routers.get(i).graph[i][index] = routers.get(i).connected_cost.get(j);
				routers.get(i).graph[index][i] = routers.get(i).connected_cost.get(j);
			}
		}
	}

	/*  在read之后，各个路由器的基本信息已经确定了， 下面初始化每个路由器的路由表  */
	public static void build_first_table()
	{
		//对于每一个router
		for(int i=0;i<routers.size();i++)
		{
			//根据当前的连接信息，建立table
			for(int j=0;j<routers.get(i).connected_id.size();j++)
			{
				int index = findIndex( routers.get(i).connected_id.get(j) );
				int cost = routers.get(i).connected_cost.get(j);
				String name = routers.get(index).network;
				ArrayList<Integer> arr = new ArrayList<>();
				arr.add( routers.get(i).connected_id.get(j) );
				routingtable rt = new routingtable(name, arr, cost);
				routers.get(i).table.add(rt);
			}
			
			
		}
	}
	
	
	/*  在read之后，各个路由器的基本信息已经确定了， 下面要通过互相发送包来完成最终的网络建立  */
	public static void startSend()
	{
		//对于每一个路由器
		for(int i=0;i<routers.size();i++)
		{

			//直接调用router类的方法，进行发送，开始循环
			routers.get(i).originatePacket();
			
		}
	}
	
	//通过路由器id找它的序号
	public static int findIndex(int id)
	{
		for(int i=0;i<routers.size();i++)
			if(routers.get(i).id == id)
				return i;
		return -1;						//	返回-1表示没找到
	}
	
	//通过路由器的序号找它的id
	public static int findId(int index)
	{
		return routers.get(index).id;
	}
	
	
	
	
	
	public static void startsystem()
	{
		try 
		{
			System.out.println("Please input your operation:");
			Scanner sc = new Scanner(System.in);
			String s = sc.nextLine();
			String first = s.charAt(0) + "";
			if( first.equals("C") )
			{
				//让每一个路由器新产生一个lsp包
				for(int i =0;i<routers.size();i++)
				{
					routers.get(i).originatePacket();
				}
			}
			else
				if( first.equals("P") )
				{
					//找到这个id
					String id_s = "";
					for(int i=1;i<s.length();i++)
					{
						id_s = id_s + s.charAt(i);
					}
					int id_num = Integer.parseInt(id_s);
					int index = findIndex(id_num);
					
					if(routers.get(index).tick == 1)
						routers.get(index).output();
					else
						System.out.println("This router has been shut down!");
				}
				else
					if( first.equals("S") )
					{
						//找到这个id
						String id_s = "";
						for(int i=1;i<s.length();i++)
						{
							id_s = id_s + s.charAt(i);
						}
						int id_num = Integer.parseInt(id_s);
						int index = findIndex(id_num);
						
						routers.get(index).shutdown();
						
					}
					else
						if( first.equals("T") )
						{
							//找到这个id
							String id_s = "";
							for(int i=1;i<s.length();i++)
							{
								id_s = id_s + s.charAt(i);
							}
							int id_num = Integer.parseInt(id_s);
							int index = findIndex(id_num);
							
							routers.get(index).turnon();
						}
						else
							if( first.equals("Q"))
							{
								System.out.println("Bye bye!");
								return;
							}
							else
							{
								System.out.println("Please input right operation!");
							}
			startsystem();
			
			
		}catch(Exception e)
		{
			System.out.println("Wrong operation!");
			startsystem();
		}
	}
	
	//测试：输出graph
	public static void showgraph(int[][] g)
	{
		for(int i=0;i<g.length;i++)
		{
			for(int j=0;j<g.length;j++)
			{
				System.out.print(g[i][j] + " ");
			}
			System.out.println("");
		}
	}
	
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		
	/*	
		for(int i=0;i<routers.size();i++) 
		{
			System.out.println(routers.get(i).id + ":" + routers.get(i).network);
			for(int j=0;j<routers.get(i).connected_id.size();j++)
			{
				System.out.print(routers.get(i).connected_id.get(j) + " ");
				System.out.println(routers.get(i).connected_cost.get(j));
			}
		}
	 */
		
	/*	
		for(int i=0;i<routers.size();i++) 
		{
			System.out.println("第" + i + "个, id为" + routers.get(i).id);
			showgraph(routers.get(i).graph);
		}
	*/	
		/*
		for(int i=0;i<routers.size();i++) 
		{
			System.out.println("第" + i + "个路由器的路由表为:");
			routers.get(i).output();
		}
	*/	
//		for(int i=0;i<routers.size();i++) 
//			{
//				System.out.println("第" + i + "个路由器的路由表为:");
//				routers.get(i).output();
//			}	
		read();
		build_first_graph();
		build_first_table();
		startSend();
	
		startsystem();

	}
	

}

//路由器类
class router
{
	int id;
	String network;
	//---------------------这2个值应该是默认不变的
	ArrayList<Integer> connected_id;				//id 
	ArrayList<Integer> connected_cost;				//cost 
	
	//在这个路由器眼中的整个网络
	int[][] graph;
	
	ArrayList<LSP> received;		//记录之前收到的LSP
	ArrayList<routingtable> table;
	
	int tick;			//用于开关,1为开，其他值为关
	
	//每个router应该有一个序列号，记录它产生了几个lsp包
	int sequencenumber;
	
	//构造函数
	public router()
	{
		this.id = 0;
		this.network = "";
		this.connected_id = new ArrayList<>();
		this.connected_cost = new ArrayList<>();
		
		received = new ArrayList<>();
		table = new ArrayList<>();			//可能出问题！
		
		tick = 1;
		
		//初次创建时，序列号为0
		this.sequencenumber = 0;
	}
	
	public router(int id, String network)
	{
		this.id = id;
		this.network = network;
		this.connected_id = new ArrayList<>();
		this.connected_cost = new ArrayList<>();
//		graph = new ArrayList<>();						//这里的初始化可能有问题！！！！
		
		received = new ArrayList<>();
		table = new ArrayList<>();			//可能出问题！
		
		tick = 1;
		
		//初次创建时，序列号为0
		this.sequencenumber = 0;
		
	}
	
	void buildgraph(int num)
	{
		this.graph = new int[num][num];
	}
	
	//关掉这个路由器
	void shutdown()
	{
		//先将开关关掉
		this.tick = Integer.MAX_VALUE;	
		
//		//改一下自己的图
		int index = LinkStateRouting.findIndex(this.id);
		
		//关掉之后，由于网络有变化，所以要让每个路由器重新发一遍自己的lsp包
		for(int i=0;i<LinkStateRouting.routers.size();i++)
		{
			for(int j=0;j<LinkStateRouting.routers.get(i).graph.length;j++)
				LinkStateRouting.routers.get(i).graph[index][j] = 0;
			for(int j=0;j<LinkStateRouting.routers.get(i).graph.length;j++)
				LinkStateRouting.routers.get(i).graph[j][index] = 0;
			LinkStateRouting.routers.get(i).originatePacket();
		}
	}
	
	//开启这个路由器
	void turnon()
	{
		this.tick = 1;
		int index = LinkStateRouting.findIndex(this.id);
	
		//由于现在每个路由器都有一个完整的图了，所以每个路由器的图都得改
		for(int i=0;i<LinkStateRouting.routers.size();i++)
		{
			//对于每一个路由器的每个直接连接项
			for(int j=0;j<LinkStateRouting.routers.get(i).connected_id.size();j++)
			{
				int index2 = LinkStateRouting.findIndex( LinkStateRouting.routers.get(i).connected_id.get(j) );
				//判断,与该路由器直接相连的路由器是否被shut down
				if( LinkStateRouting.routers.get(index2).tick == 1 )
				{
					int cost_h = LinkStateRouting.routers.get(i).connected_cost.get(j);
					LinkStateRouting.routers.get(i).graph[i][index2] = cost_h;
					LinkStateRouting.routers.get(i).graph[index2][i] = cost_h;
					
				
				}
			}
			
			//打开之后，由于网络有变化，所以要让每个路由器重新发一遍自己的lsp包
			LinkStateRouting.routers.get(i).originatePacket();
		}
	}
	
	//接收函数
	void receivePacket(LSP lsp, int forward_id)
	{
		lsp.TTL --;
		for(int i=0;i<received.size();i++)
			if(lsp.sequence_number <= received.get(i).sequence_number && received.get(i).originate_id == lsp.originate_id)
				return;
		if(lsp.TTL == 0)
			return;
		//如果出现以上两种情况，忽略这个LSP
		
		//否则
		received.add(lsp);
		
		//compare to the receiving router's connectivity graph--------------------------这一步如何实现?
		
		//根据包中的内容，更新这个路由器的graph
		for(int i=0;i<lsp.dconnected_id.size();i++)
		{
			int index1 = LinkStateRouting.findIndex(lsp.originate_id);
			int index2 = LinkStateRouting.findIndex(lsp.dconnected_id.get(i));
			int cost_here = lsp.dconnected_cost.get(i);
			this.graph[index1][index2] = cost_here;
			this.graph[index2][index1] = cost_here;
		}

		//更新完graph之后， 要更新路由表！！！！！！！！！！！！！！！！！！！！
		dijkstra(this.id);
		
		//然后发送给所有的connected的路由器
		for(int i=0;i<connected_id.size();i++)
		{
			//要找到那个对象
			for(int j=0;j<LinkStateRouting.routers.size();j++)
			{
				//如果id相同，则找到了
				if(LinkStateRouting.routers.get(j).id == connected_id.get(i))
				{
					LinkStateRouting.routers.get(j).receivePacket(lsp, this.id);
					break;
				}
			}
		}
		
		
		
	}
	
	
	//创建LSP包函数
	void originatePacket()
	{	
		//档开关打开时，正常
		if(this.tick == 1)
		{
			//首先产生一个当前状态下的LSP包,全部为初始化的值：ID=id、sequence_number=1、TTL=10、可达网络：需要初始化
			LSP l = new LSP(this.id);
			
			//------------------------------------------------可达性的表示是否正确？------------------------------------------------
			//先找到本路由器的序号（不是Id）
			int index = LinkStateRouting.findIndex( this.id );
			
			//根据当前的graph,建立直接可达的3个数组，完善lsp包（要注意是否有被停止的router!）
			for(int j=0;j<this.graph.length;j++)
			{
				if(this.graph[index][j] != 0 && this.graph[index][j] != Integer.MAX_VALUE && LinkStateRouting.routers.get(j).tick == 1)
				{	
					//说明第j个路由器可达,找到它的网络名
					int id_here = LinkStateRouting.routers.get(j).id;
					String net = LinkStateRouting.routers.get(j).network;
					int cost_here = this.graph[index][j];
				
					l.dconnected_id.add(id_here);
					l.dconnected_network.add(net);
					l.dconnected_cost.add(cost_here);
				
				}
			}
		
		
			/*--------------------------测试使用！-----------------------------------------
			System.out.println("id为" + this.id + "的路由器产生了一个LSP包，信息如下：");
			System.out.println("发出者id：" + l.originate_id);
			System.out.println("序列号" + l.sequence_number);
			System.out.println("TTL:" + l.TTL);
			for(int k=0;k<l.dconnected_id.size();k++)
			{
				System.out.println("连接信息：   与id为" + l.dconnected_id.get(k) + "相连,   网络名：" + l.dconnected_network.get(k) + ",  cost为" + l.dconnected_cost.get(k));
			}
			System.out.println("");
			----------------------------测试使用-------------------------------------------*/
				
				
			//然后把包发给所有与他直接相连的路由器
			for(int i=0;i<connected_id.size();i++)
			{
				//要找到那个对象的序号为j
				int j = LinkStateRouting.findIndex(connected_id.get(i));
				
				//在发之前，要看一下是否在2跳之内可以到达这个router
				if( (this.tick + LinkStateRouting.routers.get(j).tick) == 2)
				{
					//如果等于2， 才继续发送
					//但是等于2的时候可能是关掉之后重新开启的路由器，所以要改变一下graph, 所以要找到这个想要发送去的路由器的cost(本来的值)
					
					LinkStateRouting.routers.get(j).receivePacket(l, this.id);
					break;
				}					
				else
				{
					//否则，更新graph！！！---------------------------------------------这里可能有问题
							
					//不等于2说明那个路由器断了，所以要去掉那条边,改完之后，要更新路由表
					for(int k=0;k<this.graph.length;k++)
						this.graph[index][k] = 0;
					for(int k=0;k<this.graph.length;k++)
						this.graph[k][index] = 0;
					this.dijkstra(this.id);
				}
	
			}
		}//endif
		//如果开关没开
		else
		{
			return;
		}
	
	}
	
	//每个路由器都有一个最短路径算法，来更新当前的路由表(参数值为该路由器的id)，但是不改变本身的graph!!!! distance记录距离，graph不变！
	void dijkstra(int start)
	{
		//每次使用算法前，先初始化table表
		this.table = new ArrayList<>();
		
		int len = this.graph.length;
		
		int index = LinkStateRouting.findIndex(start);
		
		int[] s = new int[len];			//记录所有未找到最短路径的点
		int[][] distance = new int[len][len];	//记录最短路径
		for(int i=0;i<len;i++)
			for(int j=0;j<len;j++)
				distance[i][j] = this.graph[i][j];
		int[] path = new int[len];		//记录前一个节点的序号（不是ID）
		
		for(int i=0;i<len;i++)
		{
			if(graph[index][i] > 0)
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
		
		//stack用于保存link的路径
		Stack<Integer> stack = new Stack<>();
	
		for(int i=0;i<len;i++)
		{
			
			routingtable rtt = new routingtable();
			if(distance[index][i] != 0)
			{
				rtt.network = LinkStateRouting.routers.get(i).network;
				rtt.cost = distance[index][i];
				
				
				// System.out.print("逆序：");
				int ii = i;
				while(ii != -1)
				{
					stack.push(LinkStateRouting.findId(ii));			//把id压入栈！！！
					ii  = path[ii];
					
				}
				while(!stack.isEmpty())
				{
					int here = stack.pop();
					if(this.id != here)
						rtt.outgoing_link.add(here);
				}
				
				
		//		for(int k=0;k<table.size();k++)
		//		{
		//			//如果当前路由表中没有这样的记录，才添加
	    //			if( !isSame(rtt,table.get(k)) )
		//			{
				this.table.add(rtt);
		//				break;
		//			}
		//		}
					
//				//去重		
//				int size = 0;
//				int n1 = 0;
//				do 
//				{
//					size = table.size();
//					routingtable rt1 = table.get(n1);
//					if( size <= 1 )
//						break;
//					for(int k=0;k<size;k++)
//					{
//						//如果找到不是他自己，并且和他一样的表项，删除它
//						if( k != n1 && isSame(rt1, table.get(k)) )
//						{
//							table.remove(k);
//							if(k > n1)
//								n1--;
//						}
//					}
//					n1++;
//					
//				}while(table.size() >= 1 && n1<table.size());		
//						
//				//去重之后，去掉不符合要求的
//				size = 0;
//				n1 = 0;
//				do 
//				{
//					size = table.size();
//					routingtable rt1 = table.get(n1);
//					if( size <= 1 )
//						break;
//					for(int k=0;k<size;k++)
//					{
//						//如果找到不是他自己，并且和他名字一样的表项，如果代价没有它小，就删除它
//						if( k != n1 && issmall(rt1, table.get(k)) )
//						{
//							table.remove(k);
//							if(k > n1)
//								n1--;
//						}
//					}
//					n1++;
//					
//				}while(table.size() >= 1 && n1<table.size());		
						
			}
			
		}

	}//end void
	
	//判断2个routing_table是否相等
	boolean isSame(routingtable a, routingtable b)
	{
		//逐条比较
		if( !a.network.equals(b.network) )
		{
			//System.out.println(a.network + "," + b.network);
			return false;
		}
		//在名字相等的情况下，如果a比b小，才叫不同
		if( a.cost != b.cost)
		{
		//	System.out.println(a.cost + "," + b.cost);
			return false;
		}
	
		if( a.outgoing_link.size() != b.outgoing_link.size() )
		{
		//	System.out.println(a.outgoing_link.size() + "," + b.outgoing_link.size());
			return false;
		}
		for(int i=0;i<a.outgoing_link.size();i++)
		{
			if(  a.outgoing_link.get(i) != b.outgoing_link.get(i)  )
				return false;
		}
		return true;
	}
	
	//如果2个表项的目的地一样，去掉cost大的(a是否比b短)
	boolean issmall(routingtable a, routingtable b)
	{
		if( a.network.equals(b.network) )
		{
			if( a.cost <= b.cost)
				return true;
			else	
				return false;
		}
		return false;
	}
	
	//输出路由表函数
	void output()
	{
//		System.out.println("大小：" + table.size());
		for(int i=0;i<table.size();i++)
		{
			System.out.print( table.get(i).network + ", " + this.id);
			for(int j=0;j<table.get(i).outgoing_link.size();j++)
			{
				System.out.print("->" + table.get(i).outgoing_link.get(j));
			}
			System.out.println("");
//			System.out.println( ",  " +table.get(i).cost);
		}
		System.out.println("");
	}
	
	
};

//LSP包类
class LSP
{
	int originate_id;
	int sequence_number;				//表示这个初始路由器发送了多少个LSP，初值为1，每个新的LSP都应该比之前的大
	int TTL;							//LSP的生存时间，初值为10， 每次转发减1
	
	ArrayList<Integer> dconnected_id;		//每个相连的网络的 id
	ArrayList<String> dconnected_network;	//每个相连的网络的 名字
	ArrayList<Integer> dconnected_cost;		//每个相连的网络的 cost
	
	public LSP(int id)
	{
		this.originate_id = id;
		//根据Id 找到序号，从而找到sequence_number,并让他加一
		int index = LinkStateRouting.findIndex(id);
		LinkStateRouting.routers.get(index).sequencenumber =  LinkStateRouting.routers.get(index).sequencenumber + 1;
		this.sequence_number = LinkStateRouting.routers.get(index).sequencenumber;
		this.TTL = 10;
		
		this.dconnected_id = new ArrayList<>();
		this.dconnected_network = new ArrayList<>();
		this.dconnected_cost = new ArrayList<>();
	}
};

class routingtable
{
	String network;
	ArrayList<Integer> outgoing_link;			//一个id的序列，表示最短路径
	int cost;
	public routingtable()
	{
		network = "";
		outgoing_link = new ArrayList<>();
		cost = 0;	
	}
	public routingtable(String a, ArrayList<Integer> b, int c)
	{
		// TODO Auto-generated constructor stub
		network = a;
		outgoing_link = b;
		cost = c;
	}
}

