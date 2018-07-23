

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by David on 7/25/17.
 */
//地形块父类
public class MapBlock {
    ArrayList<MapBrick> geographies = new ArrayList<>();
    boolean isHome = false;
    boolean isAlive = true;
    boolean destroyable;
    boolean mThroughable;
    boolean tThroughable;

    public MapBlock(int x, int y, int w, int h, BrickType type, TankClient tc){
        int i, j;
        MapBrick mapBrick = null;
        if (w < 1 || h < 1 || x < 0 || y < 0 || x > TankClient.GAME_WIDTH || y > TankClient.GAME_HEIGHT)
            return;
        for (i = 0; i < w; i++) {
            for (j = 0; j < h; j++) {
                mapBrick = new MapBrick(x + i * MapBrick.BRICK_WIDTH,y + j * MapBrick.BRICK_HEIGHT,tc,type);
                geographies.add(mapBrick);
            }
        }
        setFeatures(type);
    }

    void setFeatures(BrickType type){
        switch (type) {
            case WALL:
                tThroughable = false;
                mThroughable = false;
                destroyable = true;
                break;
            case WATER:
                tThroughable = false;
                mThroughable = true;
                destroyable = false;
                break;
            case STEEL:
                mThroughable = false;
                tThroughable = false;
                destroyable = false;
                break;
            case GRASS:
                tThroughable = true;
                mThroughable = true;
                destroyable = false;
                break;
        }
    }


    public void draw(Graphics g){
        for (MapBrick geo : geographies){
            geo.draw(g);
        }
    }

    boolean intersect(Rectangle rect){
        for (MapBrick geo : geographies){
            if (rect.intersects(geo.getRect())){
                return true;
            }
        }
        return false;
    }
}
