import java.io.Serializable;
import java.util.ArrayList;
import java.util.Stack;

/*
 * Author: Cole Polyak
 * 11 May 2018
 * 
 * This class handles the implementation of various shell methods.
 */

public class FileSystem implements Serializable
{
	private static final long serialVersionUID = -2976471960930824706L;

	Node root; Node currentDirectory;
	
	// Basic constructor.
	public FileSystem()
	{
		root = new Node("C:", null, true);
		currentDirectory = root;
	}
	
	// Checks to ensure the file hasn't been created before.
	public void checkMakeFile(String name)
	{
		// Checks each of the names of the children.
		checkNames(name);
	}
	
	// Lists the files in the current directory.
	public void ls()
	{
		// Iterates and outputs.
		for(Node e : currentDirectory.children)
		{
			System.out.print(e.name + " ");
		}
		System.out.print("\n");
	}
	
	// Makes a new directory with name in current directory.
	public void mkdir(String name)
	{
		// Checks if it has been made before.
		checkMakeFile(name);
		currentDirectory.appendChild(name, true);
	}
	
	// Makes a new file with name in the current directory.
	public void touch(String name)
	{
		// Checks if the file has been made before.
		checkMakeFile(name);
		currentDirectory.appendChild(name, false);
	}
	
	// Outputs the present working directory.
	public void pwd()
	{
		// Stack for names of directories.
		Stack<String> nameStack = new Stack<>();
		
		// Duplicate of currentDirectory.
		Node current = currentDirectory;
		
		// Goes up to the root, adding each parent directory to the stack.
		while(current.parent != null)
		{
			nameStack.push(current.name);
			current = current.parent;
		}
		
		// Adds root to stack.
		nameStack.push(root.name);
		
		StringBuilder sb = new StringBuilder();
		
		// Pops each name off the stack and adds it to the sb.
		while(nameStack.size() != 0)
		{
			sb.append(nameStack.pop()+"\\");
		}
		
		// And output.
		System.out.println(sb);
	}
	
	// Changes directory to the one specified.
	public void cd(String name)
	{
		// Gives user option for bidirectional movement.
		if(name.trim().equals(".."))
		{
			// If they're at root already, do nothing.
			if(currentDirectory.parent == null) { return;}
			
			currentDirectory = currentDirectory.parent;
			return;
		}
		
		// Looking for target directory.
		for(Node e : currentDirectory.children)
		{
			// Checks if the directory exists and that it is a directory and not a file.
			if(e.name.equals(name) && e.isDirectory())
			{
				currentDirectory = e;
				return;
			}
		}
		
		throw new IllegalStateException("Target directory doesn't exist");
	}
	
	// Removes a file from Current Directory.
	public void rm(String name)
	{
		// Looks for file.
		for(int i = 0; i < currentDirectory.children.size(); ++i)
		{
			Node current = currentDirectory.children.get(i);
			
			// If names match
			if(current.name.equals(name))
			{
				// If target is a directory and not a file
				if(current.isDirectory)
				{
					throw new IllegalStateException("Target isn't a file!");
				}
				
				currentDirectory.children.remove(i);
				return;
			}
		}
		System.out.println("Target file doens't exist");
	}
	
	// Removes an empty directory from Current Directory.
	public void rmdir(String name) throws Exception
	{
		// Looks for directory.
		for(int i = 0; i < currentDirectory.children.size(); ++i)
		{
			Node current = currentDirectory.children.get(i);
			
			// If names match
			if(current.name.equals(name))
			{
				// If current node isn't a directory.
				if(!(current.isDirectory))
				{
					throw new IllegalStateException("Target isn't a directory");
				}
				
				// If target directory has children.
				if(current.children.size() != 0)
				{
					throw new Exception("Target directory isn't empty");
				}
				
				currentDirectory.children.remove(i);
				return;
			}
		}
		System.out.println("Target directory doesn't exist!");
	}
	
	// Recursively prints the tree rooted at the current directory.
	public void tree()
	{
		StringBuilder sb = new StringBuilder();
		toString(currentDirectory, sb, 0);
		System.out.println(sb.toString());
	}
	
	// Actually does the recursion.
	public void toString(Node r, StringBuilder sb, int level)
	{
		// Provides a base case.
		if(r != null)
		{
			// Adding appropriate number of spaces.
			for(int i = 0; i < 2 * level; ++i)
			{
				sb.append(" ");
			}
			
			// Adding name
			sb.append(r.name + " \n");
			
			// Recursively digging into each child.
			for(int i = 0; i < r.children.size(); ++i)
			{
				toString(r.children.get(i), sb, level+1);
			}
		}
	}
	
	// Checks to ensure that the file or directory to be created doesn't exist.
	public void checkNames(String name)
	{
		for(Node e : currentDirectory.children) 
		{
			if(e.name.equals(name))
			{
				throw new IllegalStateException("File with this name already exists!");
			}
		}
	}
	
	// The bones of the tree.
	private class Node implements Serializable
	{
		private static final long serialVersionUID = -1496886231511131385L;
		private String name;
		
		// All the children of the current node.
		private ArrayList<Node> children;
		
		private Node parent;
		private boolean isDirectory;
		
		// Constructor.
		public Node(String name, Node parent, Boolean isDirectory)
		{
			this.name = name;
			this.parent = parent;
			this.isDirectory = isDirectory;
			children = new ArrayList<>();
		}
		
		// If the Node in question is a directory.
		public boolean isDirectory()
		{
			return isDirectory;
		}
		
		// Returns children.
		public ArrayList<Node> children()
		{
			return children();
		}
		
		// Adds a child.
		public void appendChild(String name, boolean isDirectory)
		{
			children.add(new Node(name, currentDirectory, isDirectory));
		}
		
		// Checks if the Node in question is root.
		public boolean isRoot()
		{
			return this.parent == null;
		}
		
	}
	
}
