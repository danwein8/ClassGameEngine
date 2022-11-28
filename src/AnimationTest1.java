import java.awt.*;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

import com.weiner.javagamebook.graphics.Animation;

public class AnimationTest1 {
	
	public static void main(String[] args)
	{
		DisplayMode displayMode;
		
		if (args.length == 3)
		{
			displayMode = new DisplayMode(
					Integer.parseInt(args[0]),
					Integer.parseInt(args[1]),
					Integer.parseInt(args[2]),
					DisplayMode.REFRESH_RATE_UNKNOWN);
		}
		else
		{
			displayMode = new DisplayMode(800, 600, 16,
					DisplayMode.REFRESH_RATE_UNKNOWN);
		}
		
		AnimationTest1 test = new AnimationTest1();
		test.run(displayMode);
	}
	
	private static final long DEMO_TIME = 5000;
	
	private SimpleScreenManager screen;
	private Image bgImage;
	private Animation anim;
	
	public void loadImages()
	{
		bgImage = loadImage("images/background.jpg");
		Image player1 = loadImage("images/player1.png");
		Image player2 = loadImage("images/player2.png");
		Image player3 = loadImage("images/player3.png");
		
		anim = new Animation();
		anim.addFrame(player1, 250);
		anim.addFrame(player2, 150);
		anim.addFrame(player1, 150);
		anim.addFrame(player2, 150);
		anim.addFrame(player3, 200);
		anim.addFrame(player2, 150);
	}
	
	private Image loadImage(String fileName)
	{
		return new ImageIcon(fileName).getImage();
	}
	
	public void run(DisplayMode displayMode)
	{
		screen = new SimpleScreenManager();
		try {
			screen.setFullScreen(displayMode, new JFrame());
			loadImages();
			animationLoop();
		}
		finally {
			screen.restoreScreen();
		}
	}
	
	public void animationLoop()
	{
		long startTime = System.currentTimeMillis();
		long currTime = startTime;
		
		while (currTime - startTime < DEMO_TIME)
		{
			long elapsedTime = System.currentTimeMillis() - currTime;
			currTime += elapsedTime;
			
			anim.update(elapsedTime);
			
			Graphics g = screen.getFullScreenWindow().getGraphics();
			draw(g);
			g.dispose();
			
			try {
				Thread.sleep(20);
			}
			catch (InterruptedException e) { }
		}
	}
	
	public void draw(Graphics g)
	{
		g.drawImage(bgImage, 0, 0, null);
		
		g.drawImage(anim.getImage(), 0, 0, null);
	}
}
