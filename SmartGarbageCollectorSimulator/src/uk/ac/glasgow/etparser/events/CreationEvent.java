package uk.ac.glasgow.etparser.events;
import java.util.Scanner;


public class CreationEvent extends Event{
	
	private int size;
	
	public CreationEvent(String line){
		super (line);
		Scanner scan=new Scanner(line);
		scan.next();
		scan.next();
		String s=scan.next();
		
		System.out.println(s+"  ssssssssssssssssssssssss");
		size = Integer.parseInt(s.trim(), 16); 
		System.out.println(size+"sizeeeeeeeeeeeee");
		
		status = "A";
		scan.close();
	}
	
	public CreationEvent(Event e){
		super(e);
		size=0;
	}
	
	public int getSize(){
		return size;
	}

}
