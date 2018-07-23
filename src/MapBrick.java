

import java.awt.*;

/**
 * Created by David on 7/25/17.
 */
// 这是一个地形的父类
public class MapBrick {
    public static int BRICK_WIDTH = Tank.WIDTH;
    public static int BRICK_HEIGHT = Tank.HEIGHT;
    protected static Toolkit tk = Toolkit.getDefaultToolkit();
    protected Image image;
    protected int x;
    protected int y;
    protected TankClient tc;

    public MapBrick(int x, int y, TankClient tc, BrickType type) {
        this.y = y;
        this.tc = tc;
        this.x = x;
        switch(type){
            case WALL:
                image = tk.getImage(PlayerTank.class.getClassLoader().getResource("images/wall.gif"));
                break;
            case GRASS:
                image=tk.getImage(PlayerTank.class.getClassLoader().getResource("images/grass.png"));
                break;
            case WATER:
                image = tk.getImage(PlayerTank.class.getClassLoader().getResource("images/water.gif"));
                break;
            case STEEL:
                image =  tk.getImage(PlayerTank.class.getClassLoader().getResource("images/steel.gif"));
                break;
            case HOME:
                image = tk.getImage(PlayerTank.class.getClassLoader().getResource("images/home.jpg"));
                break;
        }
    }

    public void draw(Graphics g){
g.drawImage(image,x,y,BRICK_WIDTH,BRICK_HEIGHT,null);
}

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    Rectangle getRect(){
        return new Rectangle(x,y,BRICK_WIDTH,BRICK_HEIGHT);
}
}
