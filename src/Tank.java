
import javax.swing.*;
import java.applet.AudioClip;
import java.awt.*;
import java.util.Random;

/**
 * Created by David on 7/26/17.
 */
public abstract class Tank {
    public int id;
    public static final int XSPEED = 5;
    public static final int YSPEED = 5;
    protected static Toolkit tk = Toolkit.getDefaultToolkit();
    private static Random r = new Random();
    protected boolean live = true;
    protected int life = 100;
    protected boolean good;
    protected int x;
    protected int y;
    protected int oldX;
    protected int oldY;
    boolean teamate = false;

    protected Direction dir = Direction.STOP;
    TankClient tc;
    protected Direction ptDir = Direction.U;

    public static final int WIDTH = 30;
    public static final int HEIGHT = 30;

    AudioClip ac = JApplet.newAudioClip(TankClient.class.getClassLoader().getResource("Music/fire.wav"));


    public Tank(boolean good, int x, int y) {
        this.oldX = x;
        this.good = good;
        this.x = x;
        this.oldY = y;
        this.y = y;
    }

    public Tank(int x, int y, boolean good, Direction dir, TankClient tc) {
        this(good, x, y);
        this.dir = dir;
        this.tc = tc;
    }

    public abstract void draw(Graphics g);

    abstract void move();

    public void stay() {
        x = oldX;
        y = oldY;
    }

    protected boolean bL = false;
    protected boolean bU = false;
    protected boolean bR = false;
    protected boolean bD = false;

    void locateDirection() {
        Direction oldDir = this.dir;
        if (bL && !bU && !bR && !bD) dir = Direction.L;
        else if (!bL && bU && !bR && !bD) dir = Direction.U;
        else if (!bL && !bU && bR && !bD) dir = Direction.R;
        else if (!bL && !bU && !bR && bD) dir = Direction.D;
        else if (!bL && !bU && !bR && !bD) dir = Direction.STOP;
        if(tc.mode == GameMode.OnlineSingle) {
            TankMoveMSG msg = new TankMoveMSG(id, x, y, dir, ptDir);
            tc.player.send(msg);
        }
    }


    public Missile fire(Direction dir) {
        if (!live) return null;
        int x = this.x + WIDTH / 2;
        int y = this.y + HEIGHT / 2;
        Missile m = new Missile(x, y, good, dir, this.tc, this);
        tc.missiles.add(m);
        ac.play();
        return m;
    }

    public Missile fire() {
        return fire(ptDir);
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, WIDTH, PlayerTank.HEIGHT);
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public boolean isGood() {
        return good;
    }

    /**
     * 撞墙
     *
     * @param w 被撞的墙
     * @return 撞上了返回true，否则false
     */
    public boolean collidesWithGeo(MapBlock w) {
        if (w == null)
            return false;
        if (this.live && w.intersect(this.getRect())) {
            if (!w.tThroughable)
                this.stay();
            return true;
        }
        return false;
    }



    public boolean collidesWithTanks(java.util.List<EnemyTank> tanks) {
        if (tanks == null)
            return false;
        for (int i = 0; i < tanks.size(); i++) {
            EnemyTank t = tanks.get(i);
            if (this != t) {
                if (this.live && t.isLive() && this.getRect().intersects(t.getRect())) {
                    this.stay();
                    t.stay();
                    return true;
                }
            }
        }
        return false;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public abstract boolean eat(Blood b);





    public void playFire() {
        AudioClip ac = JApplet.newAudioClip(TankClient.class.getClassLoader().getResource("Music/fire.wav"));
        ac.play();
    }



}

