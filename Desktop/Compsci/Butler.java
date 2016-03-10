import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.nio.charset.StandardCharsets;
import java.nio.file.StandardCopyOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Scanner;


public class Butler
{
	public static void main(String[] args)
	{
		String command='';
		Scanner in = new Scanner(System.in);
		File file= new File(".Butler/");
		if(!file.exists())
		{
			System.out.println("Hello, I am your personal Butler. I shall be taking care of all of your Scheduling. Please type 'help' if you would like to see what commands I can comprehend.");
			System.out.println("Well young master, I will be needing your name and your gender.");
			System.out.println("What is your name? Please enter your name and press \'Enter\' ");
			System.out.println("What is your gender? Please enter your gender and press \' Enter \'");
			System.out.println("Now that is settled you are all ready to proceed with using me. It is a pleasure to meet you!");
		}
		else
		{
			// should add in a check to see if the person he is serving is female or male
			Date date= new Date();
			SimpelDateFormat formatter= new SimpelDateFormat("MM.dd.yyyy HH:mm:ss");
			String sdate= formatter.format(date);
			System.out.println("Welcome back young master. It is "+ sdate);
			System.out.println("A reminder that you can request a list of my commands using \'help\' ");

		}
		while(command.equals("Good Bye")== false)
		{
			System.out.print("Butler>> ");
			command= in.nextLine();
			if(command.equals("Set Schedule"))
			{
				[[['']]]
			}

		}
		Scanner in = new Scanner(System.in);
		System.out.println()
	}
}