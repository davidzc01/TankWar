
import javax.swing.*;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 这个类的作用是坦克游戏的主窗口
 *
 */

public class TankClient extends Frame {
	private static final long serialVersionUID = -123987588869522520L;
	public GameMode mode;
	private Image cover = MapBrick.tk.getImage(TankClient.class.getClassLoader().getResource("images/Cover.png"));
	/**
	 * 整个坦克游戏的宽度
	 */
	public static final int GAME_WIDTH = 800;
	public static final int GAME_HEIGHT = 600;
	GameMap gameMap = new GameMap(true,this);
	Home h = gameMap.h;
	GameMap onlineMap = new GameMap(false,this);
	ConnDialog dialog = new ConnDialog();

	PlayerTank myPlayerTank = initPlayer();;


    MapBlock
	w4 = new MapBlock(100,100,1,300/ MapBrick.BRICK_HEIGHT, BrickType.WALL,this),
	w5 = new MapBlock(100 + MapBrick.BRICK_WIDTH,100,200/ MapBrick.BRICK_WIDTH,1, BrickType.WALL,this),
	w6 = new MapBlock(100 + MapBrick.BRICK_WIDTH,250,150/ MapBrick.BRICK_WIDTH,1, BrickType.WALL,this),
	w7 = new MapBlock(100 + MapBrick.BRICK_WIDTH,400 - MapBrick.BRICK_HEIGHT,200/ MapBrick.BRICK_WIDTH,1, BrickType.WALL,this),
	wa1 = new MapBlock(100, 450, 600/ MapBrick.BRICK_WIDTH,2, BrickType.WATER,this),
	s = new MapBlock(100,500,600/ MapBrick.BRICK_WIDTH,1, BrickType.STEEL,this),
	grass = new MapBlock(400,300,600/ MapBrick.BRICK_WIDTH,1, BrickType.GRASS,this);


	List<Explode> explodes = new ArrayList<Explode>();
	List<Missile> missiles = new ArrayList<Missile>();
	List<EnemyTank> tanks = new ArrayList<>();
	Image offScreenImage = cover;

	Blood b = new Blood();

	PlayerTank initPlayer(){
		if (mode == GameMode.OffLineSingle){
			return new PlayerTank(h);
		}else {
		    Random r = new Random();
		    int x = r.nextInt(GAME_WIDTH);
		    int y = r.nextInt(GAME_HEIGHT);
            PlayerTank pt = new PlayerTank(x,y,Direction.STOP,this);
            while(gameMap.collides(pt) || pt.collidesWithTanks(tanks)){
                pt = new PlayerTank(x,y,Direction.STOP,this);
            }
            return pt;
        }
	}

	Player player = new Player(this);


	public void paint(Graphics g) {
		GameMap gm;
		if(mode == GameMode.OffLineSingle){
			gm = gameMap;
		}else {
			gm = onlineMap;
		}
		/*
		 * 指明子弹-爆炸-坦克的数量
		 * 以及坦克的生命值
		 */
		g.drawString("missiles count:" + missiles.size(), 10, 50);
		g.drawString("explodes count:" + explodes.size(), 10, 70);
		g.drawString("tanks    count:" + tanks.size(), 10, 90);
		g.drawString("tanks     life:" + myPlayerTank.getLife(), 10, 110);

        Random r = new Random();

		if(tanks.size() <= 0 && mode == GameMode.OffLineSingle) {
			while(tanks.size()<Integer.parseInt(PropertyMgr.getProperty("reProduceTankCount"))) {
			    int x = r.nextInt(GAME_WIDTH);
                EnemyBot enemyTank = new EnemyBot(x,50,Direction.D,this);
			    if(!enemyTank.collidesWithTanks(tanks)) {
                    tanks.add(enemyTank);
                }
			}
		}
		gm.add(w4);
		gm.add(w5);
		gm.add(w6);
		gm.add(w7);
		gm.add(wa1);
		gm.add(s);
		gm.add(grass);


		for(int i=0; i<missiles.size(); i++) {
			Missile m = missiles.get(i);
			m.hitTanks(tanks);
			m.hitTank(myPlayerTank);
			gm.hit(m);
			m.draw(g);
			if(!m.isLive()) missiles.remove(m);
			else m.draw(g);
		}

		for(int i=0; i<explodes.size(); i++) {
			Explode e = explodes.get(i);
			e.draw(g);
		}

		for(int i=0; i<tanks.size(); i++) {
			EnemyTank t = tanks.get(i);
			gm.collides(t);
			t.collidesWithTanks(tanks);
			t.draw(g);
			t.eat(b);
		}

       gm.collides(myPlayerTank);
		myPlayerTank.collidesWithTanks(tanks);
		myPlayerTank.draw(g);
		myPlayerTank.eat(b);
		b.draw(g);
		gm.draw(g);
        if(!myPlayerTank.isLive()||(h != null && !h.isAlive)){
            stopBGM();
            g.drawImage(MapBrick.tk.getImage(TankClient.class.getClassLoader().getResource("images/gameover.jpg")), 0, 0, null);
        }
	}


    AudioClip ac = null;
    AudioClip sbgm = JApplet.newAudioClip(TankClient.class.getClassLoader().getResource("Music/bgm.wav"));
    AudioClip bgm = JApplet.newAudioClip(TankClient.class.getClassLoader().getResource("Music/game.wav"));
	public void update(Graphics g) {
		if(offScreenImage == cover) {
            setBGM(sbgm);
		}else {
            setBGM(bgm);
            offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
		    Graphics gOffScreen = offScreenImage.getGraphics();
		    Color c = gOffScreen.getColor();
		    gOffScreen.setColor(Color.GRAY);
		    gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
		    gOffScreen.setColor(c);
		    paint(gOffScreen);
		}
		g.drawImage(offScreenImage, 0, 0, null);
		loopBGM();
	}

	void setBGM(AudioClip audioClip){
	    ac = audioClip;
    }

	public void keyPressed1(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_SPACE) {
			offScreenImage = null;
		}
//		if (key == KeyEvent.VK_C) {
//			dialog.setVisible(true);
//		}
	}


	/**
	 * 本方法显示坦克主窗口
	 *
	 */
	public void lauchFrame() {
		//this.setLocation(400, 300);
		this.setSize(GAME_WIDTH, GAME_HEIGHT);
		this.setTitle("TankWar");
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if(mode == GameMode.OnlineSingle) {
					TankDeathMSG tdm = new TankDeathMSG(myPlayerTank);
					player.send(tdm);
				}
				System.exit(0);
			}
		});
		this.setResizable(false);
		this.setBackground(Color.GREEN);

		this.addKeyListener(new KeyMonitor());

		setVisible(true);

		new Thread(new PaintThread()).start();
	}

	public static void main(String[] args) {
		TankClient tc = new TankClient();
		tc.dialog.setVisible(true);
		tc.lauchFrame();
	}


	private class PaintThread implements Runnable {

		public void run() {
			while(true) {
				repaint();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}



	AudioClip oldAc;

    public void loopBGM() {
        if (ac == null)
            return;
	    if (ac != oldAc){
	        if (oldAc != null)
	            oldAc.stop();
            oldAc = ac;
			ac.loop();
	    }
        return;
    }

    public void stopBGM() {
        ac.stop();
    }


	private class KeyMonitor extends KeyAdapter {

		public void keyReleased(KeyEvent e) {

		    myPlayerTank.keyReleased(e);
		}

		public void keyPressed(KeyEvent e) {
			keyPressed1(e);

		    myPlayerTank.keyPressed(e);
		}

	}

	class ConnDialog extends Dialog {
    	Button a = new Button("Offline mode (protect base)");
		Button b = new Button("Connect to server");
		TextField tfIP = new TextField("127.0.0.1", 12);
		TextField tfPort = new TextField("" + GameServer.TCP_PORT, 4);
		TextField tfMyUDPPort = new TextField("3333", 4);
		public ConnDialog() {
			super(TankClient.this, true);

			this.setLayout(new FlowLayout());
			this.add(new Label("IP:"));
			this.add(tfIP);
			this.add(new Label("Port:"));
			this.add(tfPort);
			this.add(new Label("My UDP Port:"));
			this.add(tfMyUDPPort);
			this.add(b);
			this.add(a);
			this.setLocation(300, 300);
			this.pack();
			this.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					setVisible(false);
				}
			});
			b.addActionListener(e -> {
				mode = GameMode.OnlineSingle;
                String IP = tfIP.getText().trim();
                int port = Integer.parseInt(tfPort.getText().trim());
                int myUDPPort = Integer.parseInt(tfMyUDPPort.getText().trim());
                player.setUdpPort(myUDPPort);
                player.connect(IP, port);
                setVisible(false);
            });
			a.addActionListener(e->{
				mode = GameMode.OffLineSingle;
				myPlayerTank = initPlayer();
				setVisible(false);
			});
		}

	}
}













