import javax.swing.JFrame;

public class Runner extends JFrame
{
	private static final int TOP_THICKNESS = 15;
	private static final int SIDE_THICKNESS = 0;
	
	public Runner()
	{
		add(new Board());
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(375 + SIDE_THICKNESS, 375 + TOP_THICKNESS);
        setLocationRelativeTo(null);
        setTitle("Lumberjack 2012");
        setResizable(true);
        setVisible(true);
	}
	
	public static void main(String[] args)
	{
		new Runner();
	}
	
	/*
	public void load()
	{
		
	}
	*/
}
