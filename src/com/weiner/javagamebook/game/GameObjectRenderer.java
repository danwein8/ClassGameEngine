package com.weiner.javagamebook.game;

import java.awt.Graphics2D;
import com.weiner.javagamebook.game.GameObject;

/**
    The GameObjectRenderer interface provides a method for
    drawing a GameObject.
*/
public interface GameObjectRenderer {

    /**
        Draws the object and returns true if any part of the
        object is visible.
    */
    public boolean draw(Graphics2D g, GameObject object);

}