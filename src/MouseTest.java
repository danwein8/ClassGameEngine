import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;

import com.weiner.javagamebook.graphics.*;
import com.weiner.javagamebook.test.GameCore;

/**
    A simple mouse test. Draws a "Hello World!" message at
    the location of the cursor. Click to change to "trail mode"
    to draw several messages. Use the mouse wheel (if available)
    to change colors.
*/
public class MouseTest extends GameCore implements KeyListener,
    MouseMotionListener, MouseListener, MouseWheelListener

{

    public static void main(String[] args) {
        new MouseTest().run();
    }

    private static final int TRAIL_SIZE = 10;
    private static final Color[] COLORS = {
        Color.white, Color.black, Color.yellow, Color.magenta
    };

    private LinkedList trailList;
    private boolean trailMode;
    private int colorIndex;


    public void init() {
        super.init();
        trailList = new LinkedList();

        Window window = screen.getFullScreenWindow();
        window.addMouseListener(this);
        window.addMouseMotionListener(this);
        window.addMouseWheelListener(this);
        window.addKeyListener(this);
    }


    public synchronized void draw(Graphics2D g) {
    	// sets count to trailList size
        int count = trailList.size();
        
        // makes size 1 if trailMode is off
        if (count > 1 && !trailMode) {
            count = 1;
        }

        Window window = screen.getFullScreenWindow();

        // draw background
        g.setColor(window.getBackground());
        g.fillRect(0, 0, screen.getWidth(), screen.getHeight());

        // draw instructions
        g.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setColor(window.getForeground());
        g.drawString("MouseTest. Press Escape to exit.", 5,
            FONT_SIZE);

        // draw mouse trail
        for (int i=0; i<count; i++) {
        	// gets element i from trailList and casts it to a Point object
            Point p = (Point)trailList.get(i);
            g.drawString("Hello World!", p.x, p.y);
        }
    }


    // from the MouseListener interface
    public void mousePressed(MouseEvent e) {
        trailMode = !trailMode;
    }


    // from the MouseListener interface
    public void mouseReleased(MouseEvent e) {
        // do nothing
    }


    // from the MouseListener interface
    public void mouseClicked(MouseEvent e) {
        // called after mouse is released - ignore it
    }


    // from the MouseListener interface
    public void mouseEntered(MouseEvent e) {
        mouseMoved(e);
    }


    // from the MouseListener interface
    public void mouseExited(MouseEvent e) {
        mouseMoved(e);
    }


    // from the MouseMotionListener interface
    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }


    // from the MouseMotionListener interface
    /**
     * adds the location of the mouse to the trailList as a point and
     * removes the last point in the list when it exceeds TRAIL_SIZE
     */
    public synchronized void mouseMoved(MouseEvent e) {
    	// creates a new point where the mouse currently is
        Point p = new Point(e.getX(), e.getY());
        // adds point to trailList
        trailList.addFirst(p);
        // removes last element in trailList if it grows too large
        while (trailList.size() > TRAIL_SIZE) {
            trailList.removeLast();
        }
    }


    // from the MouseWheelListener interface
    /**
     * sets the text(foreground) color using the mouse wheel rotation
     */
    public void mouseWheelMoved(MouseWheelEvent e) {
    	// sets the color index
        colorIndex = (colorIndex + e.getWheelRotation()) %
            COLORS.length;

        if (colorIndex < 0) {
            colorIndex+=COLORS.length;
        }
        // sets the text color
        Window window = screen.getFullScreenWindow();
        window.setForeground(COLORS[colorIndex]);
    }


    // from the KeyListener interface
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            // exit the program
            stop();
        }
    }


    // from the KeyListener interface
    public void keyReleased(KeyEvent e) {
        // do nothing
    }


    // from the KeyListener interface
    public void keyTyped(KeyEvent e) {
        // do nothing
    }
}
