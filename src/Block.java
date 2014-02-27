import java.awt.Image;
import javax.swing.ImageIcon;
import java.awt.Rectangle;

public class Block
{
	private int type;
	private int time;
	private int damage = 0;
	private int i, j;
	private Image image;

	
	public Block()
	{
		i = 0;
		j = 0;
		type = -1;
		damage = -1;
		image = new ImageIcon(this.getClass().getResource("block.png")).getImage();

	}
	
	public Block(int bi, int bj)
	{
		type = 0;
		time = 0;
		image = new ImageIcon(this.getClass().getResource("block.png")).getImage();

		i = bi;
		j = bj;
	}
	
	public int getI()
	{
		return i;
	}
	
	public int getJ()
	{
		return j;
	}
	
	public int getTime()
	{
		return time;
	}
	
	public Image getImage()
	{
		return image;
	}
	public Rectangle getBounds(int x, int y)
	{
		return new Rectangle((i - y) * 15, (j - x) * 15, 15, 15);
	}

	
	public void wait(int period)
	{
		time += period;
	}
	
	public void progress()
	{
		if(time > 0)
			time -= 1;
		else
			act();
	}
	
	public void hit()
	{
		
	}
	
	public void act()
	{
		
	}
	
	public void setImage(Image img)
	{
		image = img;
	}
}
