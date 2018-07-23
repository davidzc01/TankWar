import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 7/26/17.
 */

//游戏地图
public class GameMap {
    private List<MapBlock> mapBlocks = new ArrayList<>();
    public Home h = null;
    boolean hasHome;
    TankClient tc;

    GameMap(Boolean hasHome,TankClient tc){
        if (hasHome) {
            this.hasHome = hasHome;
            this.tc = tc;
            h = new Home(tc);
            MapBlock w1 = new MapBlock(h.x - Home.HOME_LENGTH, TankClient.GAME_HEIGHT - Home.HOME_LENGTH, Home.HOME_LENGTH / MapBrick.BRICK_WIDTH, Home.HOME_LENGTH / MapBrick.BRICK_HEIGHT + 1, BrickType.WALL, tc),
                    w2 = new MapBlock(h.x + Home.HOME_LENGTH, TankClient.GAME_HEIGHT - Home.HOME_LENGTH, Home.HOME_LENGTH / MapBrick.BRICK_WIDTH, Home.HOME_LENGTH / MapBrick.BRICK_HEIGHT + 1, BrickType.WALL, tc),
                    w3 = new MapBlock(h.x - Home.HOME_LENGTH, h.y - Home.HOME_LENGTH, 2 + Home.HOME_LENGTH / MapBrick.BRICK_WIDTH, 1, BrickType.WALL, tc);
            mapBlocks.add(h);
            mapBlocks.add(w1);
            mapBlocks.add(w2);
            mapBlocks.add(w3);
        }
    }

    public void add(MapBlock gb){
        mapBlocks.add(gb);
    }

    public void draw(Graphics g){
        if (mapBlocks != null && mapBlocks.size()>0)
            for(MapBlock gb: mapBlocks){
                gb.draw(g);
            }
    }

    void hit(Missile m){
        for(MapBlock gb: mapBlocks){
            m.hitGB(gb);
        }
    }

    boolean collides(Tank tank){
        for(MapBlock gb: mapBlocks){
            tank.collidesWithGeo(gb);
        }
        return false;
    }


}
