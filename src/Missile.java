
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 子弹实体类
 *
 */
public class Missile {
	
	public static final int XSPEED = 10;
	public static final int YSPEED = 10;
	
	public static final int WIDTH = 10;
	public static final int HEIGHT = 10;
	
	int x, y;
	Direction dir;
	
	private boolean good;
	private boolean live = true;
	
	private TankClient tc;

	Tank origin;
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image[] missileImages = null;
	private static Map<String, Image> imgs = new HashMap<String, Image>();
	static {
		missileImages = new Image[] {
				tk.getImage(Missile.class.getClassLoader().getResource("images/missileL.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource("images/missileU.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource("images/missileR.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource("images/missileD.gif")),
		};
		
		imgs.put("L", missileImages[0]);
		imgs.put("U", missileImages[1]);
		imgs.put("R", missileImages[2]);
		imgs.put("D", missileImages[3]);
	}
	
	public Missile(int x, int y, Direction dir) {
		this.x = x;
		this.y = y;
		this.dir = dir;
	}
	
	public Missile(int x, int y, boolean good, Direction dir, TankClient tc, Tank origin) {
		this(x, y, dir);
		this.good = good;
		this.tc = tc;
		this.origin = origin;
	}
	
	public void draw(Graphics g) {
		if(!live) {
			tc.missiles.remove(this);
			return;
		}
		
		switch(dir) {
		case L:
			g.drawImage(imgs.get("L"), x, y, null);
			break;
		case U:
			g.drawImage(imgs.get("U"), x, y, null);
			break;
		case R:
			g.drawImage(imgs.get("R"), x, y, null);
			break;
		case D:
			g.drawImage(imgs.get("D"), x, y, null);
			break;
		}
		
		move();
	}

	private void move() {
		
		
		switch(dir) {
		case L:
			x -= XSPEED;
			break;
		case U:
			y -= YSPEED;
			break;
		case R:
			x += XSPEED;
			break;
		case D:
			y += YSPEED;
			break;
		case STOP:
			break;
		}
		
		if(x < 0 || y < 0 || x > TankClient.GAME_WIDTH || y > TankClient.GAME_HEIGHT) {
			live = false;
		}		
	}

	public boolean isLive() {
		return live;
	}
	
	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
	
	boolean hitTank(Tank t) {
		if(t != origin&&this.live && this.getRect().intersects(t.getRect()) && t.isLive() && (this.good != t.isGood() || ((this.good == t.good) && !t.teamate))) {
		        this.live = false;
				t.setLife(t.getLife() - 20);
				if (t.getLife() <= 0) {
					t.setLive(false);
					Explode e = new Explode(x, y, tc);
					tc.explodes.add(e);
					if(tc.mode == GameMode.OnlineSingle) {
                        TankDeathMSG tdm = new TankDeathMSG(t);
                        tc.player.send(tdm);
					}
				}
				return  true;
			}
        return false;
	}
	
	boolean hitTanks(List<EnemyTank> tanks) {
		for (EnemyTank tank : tanks) {
			if (hitTank(tank)) {
				return true;
			}
		}
		return false;
	}
	
	void hitGB(MapBlock gb) {
		if(this.live && gb.intersect(this.getRect())) {
			if (!gb.mThroughable)
				this.live = false;
			if(gb.destroyable){
			    if (gb.isHome){
			        gb.isAlive = false;
                }
				gb.geographies.removeIf(wall -> wall.getRect().intersects(this.getRect()));
			}
		}
	}
}
