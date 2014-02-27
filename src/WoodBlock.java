import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class WoodBlock extends Block
{
	private Image image, imgWood, imgStump;
	private int i, j;
	private int damage;
	private boolean choppedDown;
	private int NUM_HITS = 10;
	
	public WoodBlock(int bi, int bj)
	{
		imgWood = new ImageIcon(this.getClass().getResource("wood.png")).getImage();
		imgStump = new ImageIcon(this.getClass().getResource("stump.png")).getImage();
		image = imgWood;
		
		i = bi;
		j = bj;
		
		damage = 0;
		choppedDown = false;
	}
	
	public void hit()
	{
		damage++;
		
		if(damage >= NUM_HITS)
		{
			act();
		}

	}
	
	public boolean isChoppedDown()
	{
		return choppedDown;
	}
	
	public void act()
	{
		choppedDown = true;
		image = imgStump;
	}
	
	public Image getImage()
	{
		return image;
	}
	
	public int getI()
	{
		return i;
	}
	
	public int getJ()
	{
		return j;
	}

	public Rectangle getBounds(int x, int y)
	{
		return new Rectangle((i - y) * 15, (j - x) * 15, 15, 15);
	}
}
