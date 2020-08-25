package wang;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;

public class BFS {
    	
	public static int[][] graph;	//adjacency matrix
	public static int[]   indegree; 
	public static int n; 			
	public static int e;			// 边数
	public static boolean[] visited;
	public static LinkedList<Integer> temp = new LinkedList<Integer>();
	
	public static void buildgraph(String filename) throws Exception
	{
	    File f = new File(filename);
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);
		String line = br.readLine();
		String[] ss = line.split("\\s+");
//		System.out.println(ss[0]);
//		System.out.println(ss[1]);
		int x = 0;
		for(int i=0;i<ss.length;i++)
		{
			if(!ss[i].equals(""))
			{
				x = i;
				break;
			}
		}
		n = Integer.parseInt(ss[x]);
		e = Integer.parseInt(ss[x+1]);
		graph = new int[n][n];
		indegree = new int[n];
		visited = new boolean[n];
		for (int i = 0; i < n; i++) 
	    {
			indegree[i] = 0;
			visited[i] = false;
	        for (int j = 0; j < n; j++) 
	        {
	         	graph[i][j] = 0;
	        }
	    }
		while( ( line = br.readLine() ) != null)
		{
			ss = line.split("\\s+");
			x = 0;
			for(int i=0;i<ss.length;i++)
			{
				if(!ss[i].equals(""))
				{
					x = i;
					break;
				}
			}
			int p = Integer.parseInt(ss[x]);
			int q = Integer.parseInt(ss[x+1]);
//			System.out.println(ss[0]);
//			System.out.println(ss[1]);
			if (graph[p][q] == 0) 
	        { 	
	         	
	            graph[p][q] = 1;
	            indegree[q]++;
	        }
		}
	}
	
	public static void search(int start)
	{
		int bfn = 1;
		ArrayList<Integer> S = new ArrayList<>();
		//start from node 0
		S.add(start);
		while(!S.isEmpty())
		{
			int m = S.remove(0);
			System.out.println(m + " " + bfn);
			bfn++;
			visited[m] = true;
			for(int i=0;i<n;i++)
			{
				if(graph[m][i] == 1 && !visited[i])
				{
					S.add(i);
				
					visited[i] = true;
				}
			}
			
		}
	}
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		buildgraph("infile.dat");
		search(0);
	}

}
