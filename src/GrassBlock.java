import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class GrassBlock extends Block
{
	private Image image;
	private int i, j;
	
	public GrassBlock(int bi, int bj)
	{
		image = new ImageIcon(this.getClass().getResource("grass.png")).getImage();
		i = bi;
		j = bj;
	}
	
	public Image getImage()
	{
			return image;
		
	}
	
	
	
	public Rectangle getBounds(int x, int y)
	{
		return new Rectangle((i - y) * 15, (j - x) * 15, 15, 15);
	}

}
