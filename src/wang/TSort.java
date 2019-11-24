package wang;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
 
public class TSort 
{     
    public static int[][] graph;	//adjacency matrix
    public static int[]   indegree; // 这n个点的入度
    public static int n; 			// 顶点数
    public static int e;			// 边数
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
	    for (int i = 0; i < n; i++) 
        {
            indegree[i] = 0;
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
            	// 是否是重边
                graph[p][q] = 1;
                indegree[q]++;
            }
		}
    }
    
    public static void sort1()
    {
    	System.out.println("Sort result1:");
    	//from small to big to select the id(0) node
        for (int i=0;i<n;i++) 
        {
        	//先找到一个入度为0的点
            if (indegree[i] == 0) 
            {
                temp.addFirst(i);
                indegree[i] = -1;
            }
            while (!temp.isEmpty()) 
            {
                int p = temp.removeFirst();
                System.out.print(p + " ");
                for(int j=0;j<n;j++) 
                {
                    if(graph[p][j] == 1) 
                    {
                        graph[p][j] = 0;
                        indegree[j]--;
                        if(indegree[j] == 0) 
                        {
                            temp.addFirst(j);
                            indegree[j] = -1;
                        }
                    }
                }
            }
        }
    }
    
    public static void sort2() throws Exception
    {
    	buildgraph("infile.dat");
    	System.out.println("Sort result2:");
    	//from big to small to select the id(0) node
        for (int i=n-1;i>=0;i--) 
        {
        	//先找到一个入度为0的点
            if (indegree[i] == 0) 
            {
                temp.addFirst(i);
                indegree[i] = -1;
            }
            while (!temp.isEmpty()) 
            {
                int p = temp.removeFirst();
                System.out.print(p + " ");
                for(int j=0;j<n;j++) 
                {
                    if(graph[p][j] == 1) 
                    {
                        graph[p][j] = 0;
                        indegree[j]--;
                        if(indegree[j] == 0) 
                        {
                            temp.addFirst(j);
                            indegree[j] = -1;
                        }
                    }
                }
            }
        }
    }

    
    public static void main(String[] args) throws Exception{
    	buildgraph("infile.dat");
    	sort1();
    	System.out.println("");
    	sort2();
    }
}