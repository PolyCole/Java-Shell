import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

/*
 * Author: Cole Polyak
 * 11 May 2018
 * 
 * This class implements and activates the shell. 
 */

public class Driver {

	public static void main(String[] args) 
	{
		FileSystem f = read();
		run(f);
		write(f);
	}

	// Reads from a binary file. If file doesn't exist, creates a new system.
	public static FileSystem read()
	{
		// Input for binary file.
		ObjectInputStream binIn = null;

		try
		{
			// Trying to open a line to the existing file.
			binIn = new ObjectInputStream(
					new FileInputStream("fs.data"));

			FileSystem f = (FileSystem) binIn.readObject();

			binIn.close();

			return f;
		}
		catch (FileNotFoundException e) 
		{
			return new FileSystem();
		} 
		catch (IOException e) 
		{
			System.err.println("IOException arose... Creating new filesystem.");
			return new FileSystem();
		} 
		catch (ClassNotFoundException e)
		{
			System.err.println("ClassNotFoundException arose... Creating new filesystem.");
			return new FileSystem();
		} 
	}

	// The meat and bones of the driver, reads in commands and processes them.
	public static void run(FileSystem f)
	{

		Scanner keyboard = new Scanner(System.in);

		// Infinite loop until the user types quit.
		loop:
			while(true)
			{
				// For aesthetic :)
				System.out.print("C:\\");

				String input = keyboard.nextLine().toLowerCase();
				
				// Helps for processing the two separate words.
				String[] command = input.split(" ");

				// Directs the input to the appropriate command.
				switch(command[0])
				{
				case "ls":
					f.ls();
					break;
				case "cd":
					try { f.cd(command[1]); }
					catch(IllegalStateException e)
					{ System.out.println("Target directory doesn't exist");}
					break;
				case "mkdir":
					f.mkdir(command[1]);
					break;
				case "touch":
					f.touch(command[1]);
					break;
				case "rm":
					try { f.rm(command[1]); }
					catch(IllegalStateException e)
					{ System.out.println("Target isn't a file");}
					break;
				case "pwd":
					f.pwd();
					break;
				case "rmdir":
					try { f.rmdir(command[1]); }
					catch(IllegalStateException e)
					{ System.out.println("Target isn't a directory");}
					catch(Exception e)
					{ System.out.println("Target directory isn't empty");}
					break;
				case "tree":
					f.tree();
					break;
				case "quit":
					break loop;
				
				// If a command that doesn't exist is inputted, the program will do nothing.
				default:
					continue;
				}
			}

		keyboard.close();
	}

	// Writes the File System to the binary file.
	public static void write(FileSystem f)
	{
		// Binary output.
		ObjectOutputStream binout = null;
		
		try
		{
			// Tries to open a line to the existing file
			binout = new ObjectOutputStream(
					new FileOutputStream("fs.data"));

			// And writes object.
			binout.writeObject(f);

			binout.close();

		}
		catch (FileNotFoundException e)
		{
			// If file isn't found, one is created.
			File newFile = new File("fs.data");
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}
}
