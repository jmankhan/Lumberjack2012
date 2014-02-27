import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.Rectangle;


import javax.swing.ImageIcon;

public class Player
{
	private double x, y;
	private double dx, dy;
	
	private Image image;
	private Image imgUp, imgDown, imgLeft, imgRight;
	private Image imgUpRight, imgUpLeft, imgDownLeft, imgDownRight;
	
	private boolean upInUse, rightInUse, downInUse, leftInUse;
	
	private double sin45;
	
	private int UP = 0;
	private int UPRIGHT = 1;
	private int RIGHT = 2;
	private int DOWNRIGHT = 3;
	private int DOWN = 4;
	private int DOWNLEFT = 5;
	private int LEFT = 6;
	private int UPLEFT = 7;
	private int direction;
	
	public Player(int px, int py)
	{
		x = px;
		y = py;
		dx = dy = 0;
		sin45 = Math.sqrt(2) / 2.0;
		direction = UP;

		imgUp = loadImage("upchar");
		imgUpRight = loadImage("uprightchar");
		imgRight = loadImage("rightchar");
		imgDownRight = loadImage("downrightchar");
		imgDown = loadImage("downchar");
		imgDownLeft = loadImage("downleftchar");
		imgLeft = loadImage("leftchar");
		imgUpLeft = loadImage("upleftchar");
		
		image = imgUp;
		
		upInUse = rightInUse = downInUse = leftInUse = false;
	}
	
	private Image loadImage(String fileName)
	{
		return new ImageIcon(this.getClass().getResource( fileName + ".png")).getImage();
	}
	
	public void move() //called by the timer
	{
		x += dx;
		y += dy;
	}
	
	public int getXLocation(int gx)
	{
		return (int)(x / 15) - gx; //integer division returns (x / 15) -((x % 15) / 15))
	}
	
	public int getYLocation(int gy)
	{
		return (int)(y / 15) - gy;
	}
	
	public int getX()
	{
		return (int)x - 7;
	}
	
	public int getY()
	{
		return (int)y - 7;
	}
	
	public int getDirection()
	{
		return direction;
	}
	public Image getImage()
	{
		return image;
	}
	
	public Rectangle getBounds()
	{
		if(direction == 0 || direction == 4) //if player moving vertically
			return new Rectangle(getX(), getY() + 4, 13, 7);
		else if(direction == 2 || direction == 6) //if moving horizontally
			return new Rectangle(getX() + 4, getY(), 7, 13);
		else
			return new Rectangle(getX() + 1, getY() + 1, 13, 13);
	}
	public void keyPressed(KeyEvent e)
	{
		int key = e.getKeyCode();
		
		if(key == KeyEvent.VK_UP)
		{
			upInUse = true;
			downInUse = false;
		}
		else if(key == KeyEvent.VK_RIGHT)
		{
			rightInUse = true;
			leftInUse = false;
		}		
		else if(key == KeyEvent.VK_DOWN)
		{
			downInUse = true;
			upInUse = false;
		}		
		else if(key == KeyEvent.VK_LEFT)
		{
			leftInUse = true;
			rightInUse = false;
		}
		
		if(upInUse)
		{
			if(rightInUse)
			{
				dy = sin45 * -1;
				dx = sin45;
				image = imgUpRight;
				direction = UPRIGHT;
			}
			else if(leftInUse)
			{
				dy = sin45 * -1;
				dx = sin45 * -1;
				image = imgUpLeft;
				direction = UPLEFT;
			}
			else
			{
				dy = -1;
				dx = 0;
				image = imgUp;
				direction = UP;
			}
		}
		else if(downInUse)
		{
			if(rightInUse)
			{
				dy = sin45;
				dx = sin45;
				image = imgDownRight;
				direction = DOWNRIGHT;
			}
			else if(leftInUse)
			{
				dy = sin45;
				dx = sin45 * -1;
				image = imgDownLeft;
				direction = DOWNLEFT;
			}
			else
			{
				dy = 1;
				dx = 0;
				image = imgDown;
				direction = DOWN;
			}
		}
		else //neither down nor up is in use
		{
			if(rightInUse)
			{
				dy = 0;
				dx = 1;
				image = imgRight;
				direction = RIGHT;
			}
			else if(leftInUse)
			{
				dy = 0;
				dx = -1;
				image = imgLeft;
				direction = LEFT;
			}
		}
		
	}
	
	public void keyReleased(KeyEvent e)
	{
		int key = e.getKeyCode();
		
		if(key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN) //if up or down key released	
		{
			dy = 0; //cancel vertical movement
			
			if(key == KeyEvent.VK_UP) //if up key released
				upInUse = false;
			else //if down key released
				downInUse = false;
			
			if(rightInUse) 
			{
				dx = 1;
				image = imgRight;
				direction = RIGHT;
			}
			else if(leftInUse)
			{
				dx = -1;
				image = imgLeft;
				direction = LEFT;
			}
		}
		else if(key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_LEFT) //if right or left released
		{
			dx = 0; //cancel horizontal movement
			
			if(key == KeyEvent.VK_RIGHT) //if right was released
				rightInUse = false;
			else //if left was released
				leftInUse = false;
			
			if(upInUse)
			{
				dy = -1;
				image = imgUp;
				direction = UP;
			}
			else if(downInUse)
			{
				dy = 1;
				image = imgDown;
				direction = DOWN;
			}
		}
		
		
	}
	
}
