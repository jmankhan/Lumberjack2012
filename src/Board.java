import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.Timer;




public class Board extends JPanel implements ActionListener
{
	private Timer timer;
	private Player player;
	private World world;
	private final int RENDER_HEIGHT = 25, RENDER_WIDTH = 25;
	private Block facing;

	private Block[] trees = new Block[World.NUM_TREES];
	private ArrayList<Ellipse2D.Double> circles = new ArrayList<Ellipse2D.Double>();
	private double time = 0;
	private boolean isDone = false, inGame = false, finished = false;
	private Image imgStump = new ImageIcon(this.getClass().getResource("stump.png")).getImage();
	private ArrayList<Double> scores = new ArrayList<Double>();
	private ArrayList<String> scoreNames = new ArrayList<String>();
	JButton start, highscore, credits, instructions, restart, menu;
	JFrame endscreenframe;
	JPanel endscreen;
	private int pausecount;
	
	public Board()
	{
		pausecount = 0;
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		
		setLayout(gridbag);
		c.gridx = 0;
		c.gridy = 0;
		start = new JButton("Start");
		gridbag.setConstraints(start, c);
		
		c.gridy = 1;
		instructions = new JButton("Instructions");
		gridbag.setConstraints(instructions, c);
		
		c.gridy = 2;
		highscore = new JButton("Highscores");
		gridbag.setConstraints(highscore, c);
		
		c.gridy = 3;
		credits = new JButton("Credits");
		gridbag.setConstraints(credits, c);
		
		start.addActionListener(this);
		highscore.addActionListener(this);
		credits.addActionListener(this);
		instructions.addActionListener(this);
		
		add(start);
		add(highscore);
		add(credits);
		add(instructions);
		
		world = new World();
		player = new Player(12 * 15, 24 * 15);
		facing = new Block(0, 0);
		
        setFocusable(true);
        addKeyListener(new TAdapter());
        setDoubleBuffered(true);
        
        trees = world.getTrees();
        
       
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		Graphics2D g2 = (Graphics2D)g;
		
		if(inGame)
		{
		//reset circles
		for(int i = 0; i < circles.size();)
			circles.remove(0);
		
		for(Block e : trees)
		{
			if(e.getImage() != imgStump)
				circles.add(new Ellipse2D.Double((e.getI() - 1) * 15, (e.getJ() - 1) * 15, 45, 45));
		}
	        
		renderWorld(g2);
		
		for(Ellipse2D.Double e : circles)
		{
			g2.draw(e);
		}
		
		g2.drawString("Time: " + (int)(time/10) / 100.0   + "seconds", 10, 10);
		if(pausecount % 2 == 1)
		{
			g2.drawString("PAUSE", this.getWidth()/2, this.getHeight()/2);
		}
		if(circles.isEmpty())//if they win
			finished = true;
		
		g2.drawImage(player.getImage(), player.getX(), player.getY(), this);
		//g2.draw(player.getBounds());
		g2.draw(facing.getBounds(world.getX(), world.getY()));
		}
	}
	
	public void renderWorld(Graphics2D g2)
	{
		
		for(int i = 0; i < RENDER_WIDTH; i++)
			for(int j = 0; j < RENDER_HEIGHT; j++)
			{
				g2.drawImage(world.getImageToRenderAt(j, i), j * 15, i * 15, this);
				//g2.draw(world.getBlockAt(j, i).getBounds());
			}
		
		
	}
	
	
	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getSource() == start && !inGame)
		{
			remove(start);
			remove(highscore);
			remove(instructions);
			remove(credits);
			
			inGame = true;
			timer = new Timer(25, this);
	        timer.start();
	        
	        requestFocus();
		}
		if(!isDone)
			time += 25;
		
		if(finished)
		{
			timer.stop();
			double timeToWrite = (int)(time / 10) / 100.0;
			
			String name = JOptionPane.showInputDialog(this, "Enter your name: ");
			scores.add(timeToWrite);
			scoreNames.add(name);
			
			File log = new File("scores.txt");
		    try
			{
		    if(log.exists()==false)
		    	{
		            log.createNewFile();
		    	}
		    PrintWriter out = new PrintWriter(new FileWriter(log, true));
		    out.println(timeToWrite + " " + name);
		    out.close();
		    }catch(IOException e)
		    {
		        System.out.println(e);
		    }
		    
		    //end screen
		    
		    endscreenframe = new JFrame();
		    endscreen = new JPanel();
		    String[] top5 = getHighScore();
		    Arrays.sort(top5);
		    
		    JTextArea highscoresend = new JTextArea("Highscores:");
		    if(top5.length >= 5)
		    {
		    	for(int i = 0; i < 5; i++)
		    	{
		    		highscoresend.append("\n" + top5[i]);
		    	}
		    }
		    else if(top5.length < 5)
		    {
		    	for(int i = 0; i < top5.length; i++)
		    	{
		    		highscoresend.append("\n" + top5[i]);
		    	}
		    }
		    
		    endscreenframe.setLocationRelativeTo(null);
		    endscreenframe.setVisible(true);
		    endscreenframe.setSize(this.getWidth(), this.getHeight());
		    endscreenframe.add(endscreen);
		    
		    endscreen.add(highscoresend);
		    
		    restart = new JButton("Restart");
		    restart.addActionListener(new ActionListener()
		    {
		    	public void actionPerformed(ActionEvent e)
		    	{
		    		if(e.getSource() == restart)
		    		{
		    			System.out.println("should restart this");
		    			endscreenframe.dispose();
		    			inGame = true;
		    			finished = false;
		    			timer.restart();
		    			repaint();
		    		}
		    	}
		    });
		    menu = new JButton("Menu");
		    menu.addActionListener(this);
		    
		    endscreen.add(restart);
		    endscreen.add(highscore);
		    endscreen.add(menu);
		}
		
		if(ae.getSource() == credits)
		{
			JOptionPane.showMessageDialog(this, "Original Concept and Design by Joshua D Hellerick\nLead Programmer: Jalal Khan\nManager: Isaac Mosebrook\nLead Designer: Sean Clees\nAssistant Designer: Andrej Quatrone");
		}
		
		if(ae.getSource() == highscore)
		{
			
			JOptionPane.showMessageDialog(this, getHighScore());
		}

		if(ae.getSource() == menu)
		{
			System.out.println("hue");
		}
		
		player.move();
		updateFacing();
		repaint();
		
	}
	
	
	public String[] getHighScore()
	{
		//sorting almost works, seems to break with very small numbers, but should be ok for practical use
		ArrayList<String> lines = new ArrayList<String>();
		
		try
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("scores.txt")));
			while(reader.ready())
			{
				lines.add(reader.readLine());
			}
			reader.close();
		}
		catch(IOException e)
		{
			System.out.println(e);
		}
		Collections.sort(lines);
		String[] scoresToPrint = new String[lines.size()];
		for(int i = 0; i < lines.size(); i++)
		{
			lines.set(i, i + 1 + ". " + lines.get(i));
			scoresToPrint[i] = lines.get(i);
		}
		return scoresToPrint;
	}
	public void updateFacing()
	{
		int playerX = player.getXLocation(world.getX());
		int playerY = player.getYLocation(world.getY());
		int facingX = 0;
		int facingY = 0;
		int direction = player.getDirection();
		
		if(direction == 0 || direction == 4) //if 1 or 4
			facingX = playerX;
		else if(direction >= 1 && direction <= 3) //if between 1 and 3 inc
			facingX = playerX + 1;
		else if(direction >= 5 && direction <= 7) //if between 5 and 7 inc
			facingX = playerX - 1;
		
		if(direction == 2 || direction == 6)
			facingY = playerY;
		else if(direction >= 3 && direction <= 5)
			facingY = playerY + 1;
		else if(direction == 0 || direction == 1 || direction == 7)
			facingY = playerY - 1;
		
		facing = world.getBlockAt(facingX + world.getX(), facingY + world.getY());
	}
	
//	private void loadScore()
//	{
//		String[] chicken = new String[NUM_SCORES];
//		
//		try
//		{
//			Scanner in = new Scanner(new File("scores.txt"));
//			
//			int i = 0;
//			while(in.hasNextLine())
//			{
//				chicken[i] = in.nextLine();
//				i++;
//			}
//		}
//		catch(FileNotFoundException e)
//		{
//			e.printStackTrace();
//		}
//		
//		for(int i = 0; i < NUM_SCORES; i++)
//		{
//			int from, to;
//			
//			from = 0;
//			to = chicken[i].indexOf(' ');
//			scoreNames.add(chicken[i].substring(from, to));
//			
//			chicken[i].replace(" ", "x");
//			
//			from = chicken[i].indexOf('x') + 1;
//			to = chicken[i].indexOf(' ');
//			scores.add(Double.valueOf(chicken[i].substring(from, to)));
//	
//		}
//	}
			
		
	private class TAdapter extends KeyAdapter
	{
	    public void keyReleased(KeyEvent e)
	    {
	    	player.keyReleased(e);
	    }

	    public void keyPressed(KeyEvent e)
	    {
	    	if(e.getKeyCode() == KeyEvent.VK_E)
	    	{
	    		finished = true;
	    	}
	    	if(e.getKeyCode() == KeyEvent.VK_P)
	    	{
	    		if(pausecount % 2 == 0)
	    		{
	    			timer.stop();
	    			pausecount++;
	    			
	    		}
	    		else if(pausecount % 2 == 1)
	    		{
	    			timer.restart();
	    			pausecount++;
	    			repaint();
	    		}
	    	}
	    	if(e.getKeyCode() != KeyEvent.VK_SPACE)
	    		player.keyPressed(e);
	    	else //if spacebar was pressed
	    		facing.hit();
	    }
	}
}
