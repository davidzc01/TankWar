import javax.swing.*;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;



/**
 * 坦克实体类
 *
 */
public class PlayerTank extends Tank {

	private static Image[] tankImages = null;
    private static Map<String, Image> imgs = new HashMap<String, Image>();
    private BloodBar bb = new BloodBar();

	static {
		tankImages = new Image[] {
				tk.getImage(PlayerTank.class.getClassLoader().getResource("images/myTankL.png")),
				tk.getImage(PlayerTank.class.getClassLoader().getResource("images/myTankU.png")),
				tk.getImage(PlayerTank.class.getClassLoader().getResource("images/myTankR.png")),
				tk.getImage(PlayerTank.class.getClassLoader().getResource("images/myTankD.png")),
		};

		imgs.put("L", tankImages[0]);
		imgs.put("U", tankImages[1]);
		imgs.put("R", tankImages[2]);
		imgs.put("D", tankImages[3]);

	}


	public PlayerTank(int x, int y) {
		super( true, x, y);
	}

	public PlayerTank(int x, int y, Direction dir, TankClient tc) {
		this(x, y);
		this.dir = dir;
		this.tc = tc;
	}

    public PlayerTank(Home home) {
        super(home.x + 2 * WIDTH, TankClient.GAME_HEIGHT - HEIGHT/2,true,Direction.STOP,home.tc);
    }

    @Override
	public void move(){
			this.oldX = x;
			this.oldY = y;

			switch (dir) {
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

			if (this.dir != Direction.STOP) {
				this.ptDir = this.dir;
			}
		if(x < 0) x = 0;
		if(y < 30) y = 30;
		if(x + WIDTH > TankClient.GAME_WIDTH) x = TankClient.GAME_WIDTH - WIDTH;
		if(y + PlayerTank.HEIGHT > TankClient.GAME_HEIGHT) y = TankClient.GAME_HEIGHT - PlayerTank.HEIGHT;
	}

	@Override
    public void draw(Graphics g) {
        if(good) bb.draw(g);

        switch(ptDir) {
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

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key) {
		case KeyEvent.VK_F2 :
			if(!this.live) {
				this.live = true;
				this.life = 100;
			}
			break;
		case KeyEvent.VK_LEFT :
			bL = true;
			break;
		case KeyEvent.VK_UP :
			bU = true;
			break;
		case KeyEvent.VK_RIGHT :
			bR = true;
			break;
		case KeyEvent.VK_DOWN :
			bD = true;
			break;
		}
		locateDirection();
	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key) {
		case KeyEvent.VK_SPACE:
			fire();
			break;
		case KeyEvent.VK_LEFT :
			bL = false;
			break;
		case KeyEvent.VK_UP :
			bU = false;
			break;
		case KeyEvent.VK_RIGHT :
			bR = false;
			break;
		case KeyEvent.VK_DOWN :
			bD = false;
			break;
		case KeyEvent.VK_A :
			superFire();
			break;
		}
		locateDirection();
	}


	private void superFire() {
		Direction[] dirs = Direction.values();
		for(int i=0; i<8; i++) {
			fire(dirs[i]);
		}
	}

	private class BloodBar {
		public void draw(Graphics g) {
			Color c = g.getColor();
			g.setColor(Color.RED);
			g.drawRect(x, y-10, WIDTH, 10);
			int w = WIDTH * life/100 ;
			g.fillRect(x, y-10, w, 10);
			g.setColor(c);
		}
	}

	@Override
	public boolean eat(Blood b) {
		if(this.live && b.isLive() && this.getRect().intersects(b.getRect())) {
			this.life = 100;
			playEat();
			b.setLive(false);
			return true;
		}
		return false;
	}

	@Override
	public Missile fire(){
		if(tc.mode == GameMode.OnlineSingle) {
			TankFireMSG tfm = new TankFireMSG(this);
			tc.player.send(tfm);
		}
		return fire(ptDir);
	}

	public void playEat() {
		AudioClip ac = JApplet.newAudioClip(TankClient.class.getClassLoader().getResource("Music/pick.wav"));
		ac.play();
	}
}
