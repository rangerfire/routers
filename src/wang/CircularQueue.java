package wang;

import java.util.LinkedList;
import java.util.Scanner;

public class CircularQueue {
	
	private static LinkedList<String> cq = new LinkedList<String>();
	private static int maxlength = 12;
	private static int first = 0;
	private static int last = 0;
		
	//enqueue
	public static void enQueue(String data)
	{
		if(cq.size() < maxlength)
		{
			cq.add(data);
			last = (last + 1)%maxlength;
		}
		else
			if(!cq.get( (last)%maxlength ).equals(""))
			{
				cq.set(first, data);
				first = (first + 1)%maxlength;
				last = (last + 1)%maxlength;
			}
			else
			{
				cq.set(last, data);
				last = (last + 1)%maxlength;
				
//				System.out.println("first " + first);
//				System.out.println("last " + last);
			}
	}
	//dequeue
	public static void deQueue()
	{
		if(cq.size() < maxlength)
		{
			cq.removeFirst();
			last--;
		}
		else
		{
			cq.set(first, "");
			first = (first + 1)%maxlength;
		}
	}

	public static String getFirst()
	{
		return cq.get(first);
	}
	
	public static String gerLast()
	{
		return cq.get(last);
	}
	
	public static int getLength()
	{
		return cq.size();
	}
	
	public static void output()
	{
		System.out.println("Output by your insert order:");
		do {
			System.out.println(cq.get(first));
			
			deQueue();
		
		}while(first != last);
		cq.clear();
	}
	
	public static void start()
	{
		System.out.println("Please insert your data and 'quit' for quit:");
		Scanner sc = new Scanner(System.in);
		String temp = sc.nextLine();
		if(temp.equals("quit"))
		{
			output();
			System.out.println("Bye bye!");
			return;
		}
		else
			if(temp.equals(""))
			{
				System.out.println("Please insert something or just quit!");
				start();
			}
			else
			{
				enQueue(temp);
				start();
			}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Welcome to circluar queue!");
		start();
	}

}
