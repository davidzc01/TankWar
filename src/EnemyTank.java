import java.awt.*;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by David on 7/26/17.
 */
public class EnemyTank extends  Tank {
    private  static Map<String, Image> imgs = new HashMap<String, Image>();

    static {
        Image[] tankImages = new Image[]{
                tk.getImage(PlayerTank.class.getClassLoader().getResource("images/tankL.png")),
                tk.getImage(PlayerTank.class.getClassLoader().getResource("images/tankU.png")),
                tk.getImage(PlayerTank.class.getClassLoader().getResource("images/tankR.png")),
                tk.getImage(PlayerTank.class.getClassLoader().getResource("images/tankD.png")),
        };

        imgs.put("L", tankImages[0]);
        imgs.put("U", tankImages[1]);
        imgs.put("R", tankImages[2]);
        imgs.put("D", tankImages[3]);

    }


    EnemyTank(int x, int y, Direction dir, TankClient tc) {
        super(x, y, false, dir, tc);
    }

    @Override
    public void draw(Graphics g) {
        if(!live) {
            tc.tanks.remove(this);
            return;
        }
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

    @Override
    void move() {

    }

    @Override
    public boolean eat(Blood b) {
        return false;
    }

}
