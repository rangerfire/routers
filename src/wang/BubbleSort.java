package wang;

import java.util.ArrayList;

public class BubbleSort {
	
	public static ArrayList<Integer> arr = new ArrayList<Integer>();
	
	public static void bubblesort()
	{
		int first = 0;
		int last = arr.size()-1;
		
		while(first < last)
		{
			int count = 0;
			//first->last
			for(int i=first;i<last;i++)
			{
				if(arr.get(i) > arr.get(i+1))
				{
					//swap
					int temp = arr.get(i);
					arr.set(i, arr.get(i+1));
					arr.set(i+1, temp);
					count++;
				}
			}
			if(count == 0)
				break;
			last--;
			//change to last->first
			for(int i=last;i>first;i--)
			{
				if(arr.get(i)<arr.get(i-1))
				{
					int temp = arr.get(i);
					arr.set(i, arr.get(i-1));
					arr.set(i-1, temp);
				}
			}
			first++;
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//get 10 random numbers
		for(int i=0;i<10;i++)
			arr.add( (int)(1+Math.random()*10) );
		System.out.println("Original list: ");
		for(int i=0;i<10;i++)
			System.out.print(arr.get(i) + " ");
		System.out.println("");
		System.out.println("After bouncy bubble sort: ");
		bubblesort();
		for(int i=0;i<10;i++)
			System.out.print(arr.get(i) + " ");		
	}

}
