import java.util.*;
import java.lang.*;
import java.io.*;

public class WordAssassin
{
	public ArrayList<String> nicelist;
	public HashMap<String, String> pair;
	public String destinationname;
	public ArrayList<Integer> added;
	public HashMap<String, String> wishlists;
	public WordAssassin()
	{
		nicelist= new ArrayList<String>();
		pair= new HashMap<String, String>();
		destinationname="";
		added= new ArrayList<Integer>();
		wishlists= new HashMap<String, String>();
	}
	//Have to account for people typing in same names, create an error message and skip over the rest of that iteration of the loop. 
	public void makeList()
	{
		Scanner in= new Scanner(System.in);
		System.out.println("Please name the group.");
		System.out.print("$$ ");
		destinationname= in.nextLine();
		System.out.println("Please input the name of the participant.");
		String recentString="";
		while(recentString.equals("End")==false)
		{
			System.out.println("To end list, type \'End \' ");
			System.out.print("$$ ");
			recentString= in.nextLine();
			if(recentString.equals("End"))
			{
				break;
			}
			nicelist.add(recentString);
            System.out.println("Now type in what is their taboo word.");
            System.out.print("$$ ");
            wishlists.put(recentString, in.nextLine());
		}
	}
	public void makePairs()
	{
		pair.clear();
		added.clear();
		int i= nicelist.size();
		int k=0;
		int m=0;
		boolean valid=true;
		for(int j=0; j<i; j++)
		{
			k= (int) (Math.random()*i);
			while(added.contains(k))
			{	
				k= (int) (Math.random()*i);
			}
			pair.put(nicelist.get(j), nicelist.get(k));
			added.add(k);
		}
		for(String x: pair.keySet())
		{
			if(x.equals(pair.get(x)))
				valid=false;
		}
		if(!valid)
		{
			makePairs();
		}

	}
	public static void main(String[] args)
	{
		WordAssassin santa= new WordAssassin();
		santa.makeList();
		santa.makePairs();
		File file= new File(santa.destinationname);
		File file1;
		FileWriter fileWriter;
		BufferedWriter bufferedWriter;
		file.mkdir();
		for(String x: santa.pair.keySet())
		{
			String s= santa.destinationname+ "/"+ x +".txt";
			file1=new File(s);
			try{
				fileWriter = new FileWriter(s);
            	bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(x + " your target is " + santa.pair.get(x)+ "\n");
                bufferedWriter.write("they cannot say "+ santa.wishlists.get(santa.pair.get(x)));
				bufferedWriter.close();
			}
			catch(IOException ex) {
            System.out.println(
                "Error writing to file '"
                + s + "'");

        	}
		}

		System.out.println("May the Odds be Ever in Your Favor.");
	}
}