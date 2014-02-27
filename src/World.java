import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

import java.awt.Image;

public class World
{
	private int WORLD_HEIGHT = 25;
	private int WORLD_WIDTH = 25;
	private int x, y;
	
	private final int GRIDS = 3;
	private final int TYPEGRID = 0;
	private final int WAITGRID = 1;
	private final int POSGRID = 2;
	
	private Block[][] blocks = new Block[WORLD_HEIGHT][WORLD_WIDTH];
	private int[][][] grid = new int[GRIDS][WORLD_HEIGHT][WORLD_WIDTH];
	
	private String[] fileContents = new String[WORLD_HEIGHT];
	private String typeFileName = "world.txt";
	private String waitFileName = "wait.txt";
	private String posFileName = "position.txt";
	
	public static final int NUM_TREES = 20;
	private Block[] trees = new Block[NUM_TREES];
	private int[][] treeLocations = new int[NUM_TREES][2];

	public World()
	{
		load();
	}
	
	public void load()
	{
		loadFile(typeFileName);
		loadContents(TYPEGRID);
		
		loadFile(waitFileName);
		loadContents(WAITGRID);
		
		loadFile(posFileName);
		loadContents(POSGRID);
		
		loadXY();
		loadBlocks();
	}

	
	public void loadFile(String fileName)
	{
		int index = 0;
		
		try
		{
			Scanner in = new Scanner(new File(fileName));
			
			while(in.hasNextLine() && index < WORLD_HEIGHT)
			{
				fileContents[index] = in.nextLine();
				index++;
			}
			
			in.close();
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}
	
	public void loadContents(int h)
	{
		for(int i = 0; i < WORLD_HEIGHT; i++) //do for each line of the string array
		{
			String line = fileContents[i]; //store the current operand
			
			for(int j = 0; j < WORLD_WIDTH; j++) //do for each number in line
			{
				int from, to;
				
				if(j == 0) //if first time through, grab first number
				{
					from = 0;
					to = line.indexOf(' ');
				}
				else //if not first time through, grab from the character(s) after the x to the next space
				{
					from = line.lastIndexOf('x') + 1;
					to = line.indexOf(' ');
				}

				
				grid[h][i][j] = Integer.valueOf(line.substring(from, to)); //store the number now that it is located
				
				line = line.replaceFirst(" ", "x"); //change the space after number last read to an x

			}
		}
	}
	
	public void loadXY()
	{
		String contents = "";
		try
		{
			Scanner in = new Scanner(new File("settings.txt"));
			contents = in.nextLine();
			in.close();
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
		
		x = Integer.valueOf(contents.substring(0, 1));
		y = Integer.valueOf(contents.substring(2, 3));
	}

	public void loadBlocks()
	{
		int woodblockCount = 0;
		
		for(int i = 0; i < WORLD_HEIGHT; i++)
			for(int j = 0; j < WORLD_WIDTH; j++)
			{
				switch(grid[TYPEGRID][j][i])
				{
					case 0: blocks[i][j] = new Block(i, j);
					break;
					
					case 1: blocks[i][j] = new GrassBlock(i, j);
					break;
						
					case 2: blocks[i][j] = new DirtBlock(i, j);
					break;
					
					case 3: { blocks[i][j] = new WoodBlock(i, j); treeLocations[woodblockCount][0] = i; treeLocations[woodblockCount][1] = j; woodblockCount++; }
					break;
				}
			}
		
		System.out.println(woodblockCount);
	}
	
	public Block[] getTrees()
	{
		for(int i = 0; i < NUM_TREES; i++)
		{
			trees[i] = blocks[treeLocations[i][0]][treeLocations[i][1]];
		}
		
		return trees;
	}
	

	
	public Block getBlockAt(int i, int j)
	{
		if((x + i) >= 0 &&  (x + i) < WORLD_HEIGHT && (j + y) >= 0 && (j + y) < WORLD_HEIGHT) //if valid indices
			return blocks[x + i][j + y];
		else
			return new Block();
	}
	public Image getImageToRenderAt(int a , int b)
	{
		Image toReturn = blocks[x + a][y + b].getImage();
		return toReturn;
	}

	
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
}
