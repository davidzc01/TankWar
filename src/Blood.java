import java.awt.*;

public class Blood {

	private int x, y, w, h;
	TankClient tc; 
	
	private int step = 0;
	private boolean live = true;

	private static Toolkit tk = Toolkit.getDefaultToolkit();

	private Image bloodImage = tk.getImage(PlayerTank.class.getClassLoader().getResource("images/hp.png"));
	
	//指明血块运动的轨迹，由pos中各个点构成
	private int[][] pos = {
			          {350, 300}, {360, 300}, {375, 275}, {400, 200}, {360, 270}, {365, 290}, {340, 280}
					  };
	
	public Blood() {
		x = pos[0][0];
		y = pos[0][1];
		w = h = 15;
	}
	
	public void draw(Graphics g) {
		if(!live) return;
		
		g.drawImage(bloodImage,x,y,w,h,null);
		
		move();
	}

	private void move() {
		step ++;
		if(step == pos.length){
			step = 0;
		}
		x = pos[step][0];
		y = pos[step][1];
	}
	
	public Rectangle getRect() {
		return new Rectangle(x, y, w , h);
	}

	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}
	
}
