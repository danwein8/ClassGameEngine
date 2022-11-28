import java.awt.Graphics2D;
import java.awt.Color;
import java.io.IOException;
import java.util.*;

import com.weiner.javagamebook.math3D.*;
import com.weiner.javagamebook.bsp2D.*;
import com.weiner.javagamebook.shooter3D.*;
import com.weiner.javagamebook.game.*;
import com.weiner.javagamebook.path.*;

public class PathFindingTest extends ShooterCore {

    public static void main(String[] args) {
        new PathFindingTest(args, "../images/sample2.map").run();
    }

    protected BSPTree bspTree;
    protected CollisionDetection collisionDetection;
    protected String mapFile;

    public PathFindingTest(String[] args, String defaultMap) {
        super(args);
        for (int i=0; mapFile == null && i<args.length; i++) {
            if (mapFile == null && !args[i].startsWith("-")) {
                mapFile = args[i];
            }
        }
        if (mapFile == null) {
            mapFile = defaultMap;
        }
    }

    public void createPolygons() {
        Graphics2D g = screen.getGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0,0, screen.getWidth(), screen.getHeight());
        g.setColor(Color.WHITE);
        g.drawString("Loading...", 5, screen.getHeight() - 5);
        screen.update();

        float ambientLightIntensity = .2f;
        List lights = new LinkedList();
        lights.add(new PointLight3D(-100,100,100, .3f, -1));
        lights.add(new PointLight3D(100,100,0, .3f, -1));

        MapLoader loader = new MapLoader(
            new BSPTreeBuilderWithPortals());
        loader.setObjectLights(lights, ambientLightIntensity);

        try {
            bspTree = loader.loadMap(mapFile);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }

        collisionDetection =
            new CollisionDetectionWithSliding(bspTree);
        gameObjectManager = new GridGameObjectManager(
            bspTree.calcBounds(), collisionDetection);
        // TODO: FIND OUT HOW TO MAKE PLAYER CLASS WORK HERE
        //gameObjectManager.addPlayer(new Player());

        ((BSPRenderer)polygonRenderer).setGameObjectManager(
            gameObjectManager);


        createGameObjects(loader.getObjectsInMap());
        Transform3D start = loader.getPlayerStartLocation();
        gameObjectManager.getPlayer().getTransform().setTo(start);
    }

    protected void createGameObjects(List mapObjects) {
        PathFinder pathFinder =
            new AStarSearchWithBSP(bspTree);

        Iterator i= mapObjects.iterator();
        while (i.hasNext()) {
            PolygonGroup group = (PolygonGroup)i.next();
            String filename = group.getFilename();
            if ("aggressivebot.obj".equals(filename)) {
                PathBot bot = new PathBot(group);
                bot.setPathFinder(pathFinder);
                gameObjectManager.add(bot);
            }
            else {
                // static object
                gameObjectManager.add(new GameObject(group));
            }
        }
    }



    public void drawPolygons(Graphics2D g) {

        polygonRenderer.startFrame(g);

        // draw polygons in bsp tree (set z buffer)
        ((BSPRenderer)polygonRenderer).draw(g, bspTree);

        // draw game object polygons (check and set z buffer)
        gameObjectManager.draw(g,
            (GameObjectRenderer)polygonRenderer);

        polygonRenderer.endFrame(g);

    }
}
