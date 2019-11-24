package wang;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;

public class Huffman {
	
	public static ArrayList<Character> carr = new ArrayList<Character>();

	public static void start() throws Exception
	{
		File f = new File("infile.dat");
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);
		String line = "";
		while( ( line = br.readLine() ) != null)
		{
			for(int i=0;i<line.length();i++)
			{
				char c = line.charAt(i);
				//0~9: 48-57, A~Z: 65~90, a~z: 97~122 
				if(  (c>=48 && c<=57) || (c>=65 &c<=90) || (c>=97 && c<=122) )
					carr.add(c);
			}
		}
//		System.out.println("total char:" + carr.size());
		Map<Character, Integer> map = CountF(carr);
		ArrayList< Entry<Character, Integer> > earr = new ArrayList< Map.Entry<Character, Integer> >(map.entrySet());
		//override sort
		Collections.sort(earr,new Comparator< Entry<Character,Integer> >()
	    {
	    	public int compare(Entry<Character, Integer> E1, Entry<Character, Integer> E2) 
	    	{  
	        	int flag = E2.getValue().compareTo(E1.getValue());  
	        	if(flag==0)
	        	{
	        		return E2.getKey().compareTo(E1.getKey());  
	            }
	        		return flag;  
	          }
	    });
		//already get fequency
		write(earr);	
	}
	
	public static void write(ArrayList< Entry<Character, Integer> > earr) throws Exception
	{
		File f = new File("outfile.dat");
		FileWriter fw = new FileWriter(f);
		PrintWriter pw = new PrintWriter(fw);
		//step1
		pw.println("   Symbol Frequency");
		for(int i=0;i<earr.size();i++)
		{
			Entry<Character,Integer> e = earr.get(i);
			double freq = 100 * (double)e.getValue()/(double)carr.size();
			pw.println("     " + e.getKey() + ",      " + String.format("%.2f", freq) + "%");
		}
		pw.println();
		//step2
		pw.println("   Symbol Huffman Codes");
		int count = 0;
		ArrayList<Node<String>> arr = new ArrayList<Node<String>>();
		for(int i=0;i<earr.size();i++)
		{
			Entry<Character,Integer> e = earr.get(i);
			arr.add(new Node<String>(e.getKey()+"", e.getValue()));
		}
		Node<String> rnode = createHuffmanTree(arr);
		HuffmanCode(rnode);
		ArrayList<Node<String>> al = generate(rnode);
		//CodeFinish
		for(int i=0;i<earr.size();i++)
		{
			Entry<Character,Integer> e = earr.get(i);
			String temp = e.getKey() + "";
			for(int j=0;j<al.size();j++)
			{
				if(temp.equals(al.get(j).data))
				{
					pw.println("     " + al.get(j).data + ",      " + al.get(j).code);
					int l = al.get(j).code.length();
					count = count + l*e.getValue();
				}
			}
		}
		pw.print("   Total Bits: " + count);
		fw.flush();
		fw.close();
	}
	
	public static Map<Character, Integer> CountF(ArrayList<Character> carr)
	{
		Map<Character, Integer> map = new HashMap<Character, Integer>();
		for(int i=0;i<carr.size();i++)
		{
			Character ch = new Character(carr.get(i));
			if(map.containsKey(ch))
				map.put(ch, map.get(ch) + 1);
			else
				map.put(ch, 1);
		}		
		return map;
	}
	
    public static <T> Node<T> createHuffmanTree(List<Node<T>> nodeList)
    {    
        while(nodeList.size() > 1)
        {
        	Collections.sort(nodeList);   
            Node<T> left = nodeList.get(nodeList.size()-1);    
            Node<T> right = nodeList.get(nodeList.size()-2);    
            Node<T> parent = new Node<T>(null, left.weight+right.weight);    
            parent.setLeft(left);    
            parent.setRight(right);    
            nodeList.remove(left);    
            nodeList.remove(right);    
            nodeList.add(parent);    
        }    
        return nodeList.get(0);    
    }    
    public  static<T> void HuffmanCode(Node<T> rnode)
    {  
    	if (rnode == null) 
    		return;  
        if(rnode.left != null)   
            rnode.left.setCoding(rnode.code + "0");  
        if(rnode.right != null)   
           rnode.right.setCoding(rnode.code + "1");    
        HuffmanCode(rnode.left);  
        HuffmanCode(rnode.right);  
	}  
      
        
    public static <T> ArrayList<Node<T>> generate(Node<T> root)
    {
        ArrayList<Node<T>> arr = new ArrayList<Node<T>>();    
        Queue<Node<T>> queue = new ArrayDeque<Node<T>>();    
        if(root != null)
            queue.add(root);    
        while(!queue.isEmpty())
        {    
            arr.add(queue.peek());    
            Node<T> node = queue.poll();        
            if(node.left != null)
            {    
                queue.offer(node.left);    
            }    
            if(node.right != null)
            {    
                queue.offer(node.right);    
            }    
        }    
        return arr;    
    }    
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		start();
	}

}


class Node<T> implements Comparable<Node<T>> 
{ 
	T data;    
	double weight;     
	Node<T> left;    
	Node<T> right;    
	String code = "";   
    public Node(T data, double weight)
    {    
    	this.data = data;    
    	this.weight = weight;    
    }

    public void setData(T data)
    {
    	this.data = data;    
    }    

    public void setWeight(double weight)
    {    
        this.weight = weight;    
    }    

    public void setLeft(Node<T> left)
    {    
        this.left = left;    
    }
    
    public void setRight(Node<T> right) 
    {    
        this.right = right;    
    }      
    
    public void setCoding(String code)
    {
    	this.code = code;
    }      
    //override
    public int compareTo(Node<T> other) 
    {    
        if(other.weight > this.weight)
        {    
            return 1;    
        }    
        if(other.weight < this.weight)
        {    
            return -1;    
        }    
        return 0;    
    }
}

