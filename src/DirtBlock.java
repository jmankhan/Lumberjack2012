import java.awt.Image;
import javax.swing.ImageIcon;
import java.awt.Rectangle;

public class DirtBlock extends Block
{
	private Image image;
	private int i, j;
	
	public DirtBlock(int bi, int bj)
	{
		image = new ImageIcon(this.getClass().getResource("dirt.png")).getImage();
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
