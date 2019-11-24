package wang;

import java.util.ArrayList;
import java.util.Scanner;

public class MaxHeap {

	public static ArrayList<Integer> maxHeap = new ArrayList<>();
	
	public static void init()
	{
		maxHeap = new ArrayList<>();
		maxHeap.add(null);
	}
	
	public static void fUp(int index)
	{
		while(index >1 && maxHeap.get(index/2) < maxHeap.get(index))
		{
			//change&&swap
			int temp = maxHeap.get(index);
			maxHeap.set(index, maxHeap.get(index/2));
			maxHeap.set(index/2, temp);
			
			index = index/2;
		}
	}
	
	public static void fDown(int index)
	{
		while(index*2 <= maxHeap.size() - 1)
		{
			int left = index*2;
			if( (left+1)<=(maxHeap.size()-1) && maxHeap.get(left) < maxHeap.get(left+1))
				left = left + 1;
			if(maxHeap.get(left) <= maxHeap.get(index))
				break;
			//swap
			int temp = maxHeap.get(index);
			maxHeap.set(index, maxHeap.get(left));
			maxHeap.set(left, temp);
			
			index = left;
		}
	}
	
	public static void store(int n)
	{
		maxHeap.add(n);
		fUp(maxHeap.size()-1);
	}
	
	public static int deleteMax()
	{
		int length = maxHeap.size()-1;
		int max = maxHeap.get(1);
		maxHeap.set(1, maxHeap.get(length));
		maxHeap.remove(length);
		fDown(1);
//		for(int i=1;i<maxHeap.size();i++)
//			System.out.print(maxHeap.get(i) + " ");
//		System.out.println("");
		return max;
	}
	
	public static void output()
	{
		while(maxHeap.size()>1)
			System.out.print(deleteMax() + " ");
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Welcome!");
		init();
		System.out.println("Please type in 10 numbers:");
		Scanner sc = new Scanner(System.in);
		String line = "";
		while( ( line = sc.nextLine() ) != null)
		{
			if(line.equals("quit"))
				return;
			String[] ss = line.split("\\s++");
			for(int i=0;i<ss.length;i++)
			{
				if(i>9)	break;
				int temp = Integer.parseInt(ss[i]);
				
				store(temp);
			}
			System.out.println("Delete the largest:");
			System.out.println( deleteMax() );
			System.out.println("Output:");
			output();
		
		}
	}

}
